package simulator.old;

import java.io.IOException;

/**
 * Based on FlacheExperiment1 this class implements:
 * 1. Central repositories
 * 2. Probabilistic change confronting agents homophily and culture homophily
 * 3. Deterministic cultural change if the neighbor's culture is stronger (bigger) than agent's
 * @author tico
 *
 */
public class Ulloa1 extends Flache1 {
	
	/**
	 * Nationality of each culture
	 */
	protected int nationalities[][] = null;
	
	/**
	 * Possible cultures
	 */
	protected int [][] cultures = null;
	protected int [] culturesN = null;
	
	/**
	 * Metrics for my own implementation
	 */
	private int culturesU;
	private int biggest_clusterU;
	
	@Override
	public void setup() {
		super.setup();
		
		nationalities = new int[ROWS][COLS];
		
		cultures = new int[ROWS*COLS][FEATURES];
		culturesN = new int[ROWS*COLS];
		
		// Initialize cultures and nationalities
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				nationalities[r][c] = r * ROWS + c;
				culturesN[r * ROWS + c] = 1;
				for (int f = 0; f < FEATURES; f++) {
					cultures[r * ROWS + c][f] = -1;
				}
			}
		}
		
	}

	
	@Override
	protected void reset(){
		super.reset();
		nationalities = null;
		cultures = null;
		culturesN = null;
	}

	@Override
	public void run_experiment() {
		for (iteration = 0; iteration < ITERATIONS; iteration++) {
			for (int ic = 0; ic < CHECKPOINT; ic++) {
				for (int i = 0; i < TOTAL_AGENTS; i++) {
					
					// select the agent
					int r = rand.nextInt(ROWS);
					int c = rand.nextInt(COLS);
	
					// select the neighbor that might influence the agent
					int n = rand.nextInt(neighboursN[r][c]);
					int nr = neighboursX[r][c][n];
					int nc = neighboursY[r][c][n];
					
					// select the nationality
					int nationality = nationalities[r][c];
	
					// get the number of mismatches between the two agents
					int mismatchesN = 0;
					// get the number of identical traits between the agent and its culture
					int cultural_overlap = 0;
					for (int f = 0; f < FEATURES; f++) {
						if (beliefs[r][c][f] != beliefs[nr][nc][f]) {
							mismatches[mismatchesN] = f;
							mismatchesN++;
						}
						if (beliefs[r][c][f] == cultures[nationality][f]) {
							cultural_overlap++;
						}
					}
					int agents_overlap = FEATURES - mismatchesN;
					
					// Check for selection error
					boolean is_selection_error = rand.nextFloat() >= 1 - SELECTION_ERROR;
					// Check for interaction based on homophily
					boolean is_interaction = rand.nextFloat() >= 1 - ((float) agents_overlap / (float) FEATURES);
	
					// check if there is actual interaction 
					if (/*agents_overlap != FEATURES // this is avoiding cultural change!!
							&& */ (is_interaction && !is_selection_error || !is_interaction && is_selection_error)) {
						
						int selected_feature = -1;
						// if arrive here by selection error, there is still a chance of influence the culture
						if (mismatchesN == 0 ) {  
							selected_feature = rand.nextInt(FEATURES);
						} else {
							selected_feature = mismatches[rand.nextInt(mismatchesN)];
						}
						int selected_trait = beliefs[nr][nc][selected_feature];
						int nationality_trait = cultures[nationality][selected_feature];
						
						// if there is no cultural shock (current trait is different to its nationality's), 
						// accept the change
						if (beliefs[r][c][selected_feature] != nationality_trait || 
							// if the agent's current trait is equal to its nationality's (cultural shock),
							// then the agent will impose resistance to change depending how identified it is 
							// with its nationality	(cultural overlap)	
							beliefs[r][c][selected_feature] == nationality_trait &&
							// Cultural resilience: resistance to change based on cultural 
							// similarity or agent similarity
							(rand.nextFloat() > cultural_overlap / 
									// Math.max because it might have been a selection error
									(float) (Math.max(1, agents_overlap) +  
											cultural_overlap))) {

							
							// If there is no cultural shock or the the agent wins the roll against the culture, 
							// then change the trait	
							beliefs[r][c][selected_feature] = selected_trait;
						
							// get the number of identical traits between the agent and its neighbors's culture
							int neighbors_culture_overlap = 0;
							int neighbors_nationality = nationalities[nr][nc];
							for (int f = 0; f < FEATURES; f++) {
								if (beliefs[r][c][f] == cultures[neighbors_nationality][f]) {
									neighbors_culture_overlap++;
								}
							}
							
							// If, after the interaction, the similarity with the neighbor's culture is bigger 
							// (or equal to consider the new assimilated trait) than the similarity with its 
							// own culture then the agent will change its culture to its neighbor's
							if ( neighbors_culture_overlap >= cultural_overlap ) {
								
								// if the nationalities are different, then nationality change to its neighbors
								if (nationality != neighbors_nationality) {
									
									// its culture lost a citizen
									culturesN[nationality]--;
									nationalities[r][c] = neighbors_nationality;
									culturesN[neighbors_nationality]++;
									
								} // END of different nationality
								
								// if there is no trait selected for the selected feature, then make the
								// selected trait part of the culture
								
								//if (cultures[neighbors_nationality][selected_feature] == -1) {
								cultures[neighbors_nationality][selected_feature] = selected_trait;
								//} // END of add a cultural trait to nationality
								
							}// END of change of nationality
							
						} // END of cultural shock
						
					} // END of checking for interaction
					
					// mutation
					if ( rand.nextFloat() >= 1 - MUTATION ) {
						beliefs[r][c][rand.nextInt(FEATURES)] = rand.nextInt(TRAITS);
					}
					
				} // END of total agents
				
			} // END of checkpoint
			
			// write results of the current checkpoint
			try {
				writer.write(results());				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			// check if the user hasn't cancelled or suspended the thread
			if (!playing){
				if (suspended){
					set_suspended();
				} 
				if (cancelled) { 
					break; 
				} 
			} // END of !playing
			
			
		} // END of iterations
		
		if (iteration == ITERATIONS){
			is_finished = true;
		}
	} // END of run_experiment
	
	/**
	 * Search for the biggest culture and counts the number of cultures so far
	 */
	private void count_clustersU(){
		biggest_clusterU = 0;
		culturesU = 0;
		for (int i = 0; i < culturesN.length; i++) {
			if (culturesN[i] > 0) {
				culturesU++;
				if (culturesN[i] > biggest_clusterU){
					biggest_clusterU = culturesN[i];					
				}
			}
		}
	}

	@Override
	protected String results() {
		count_clustersU();
		return super.results();
	}

	@Override
	public String get_results() {
		return  IDENTIFIER + "," +
				new java.sql.Timestamp(startTime) + "," +
				((endTime == 0) ? (System.currentTimeMillis() - startTime) : (endTime - startTime)) + "," +
				ITERATIONS + "," +  
				CHECKPOINT + "," +  
				TYPE + "," +  
				ROWS + "," +
				COLS + "," +  
				FEATURES + "," +  
				TRAITS + "," +  
				RADIUS + "," +  
				ALPHA + "," +
				MUTATION + "," +  
				SELECTION_ERROR + "," +
				iteration * CHECKPOINT+ "," +
				cultureN  + "," +
				(float) cultureN / TOTAL_AGENTS + "," +
				biggest_cluster + "," +
				(float) biggest_cluster / TOTAL_AGENTS + "," + 
				culturesU  + "," +
				(float) culturesU / TOTAL_AGENTS + "," +
				biggest_clusterU + "," +
				(float) biggest_clusterU / TOTAL_AGENTS +  "\n";				
	}
	
}
