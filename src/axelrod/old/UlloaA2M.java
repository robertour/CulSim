package axelrod.old;

import java.io.IOException;

/**
 * Based on FlacheExperiment1 this class implements:
 * 1. Central repositories
 * 2. Probabilistic change confronting agents homophily and culture homophily
 * 3. Probabilistic cultural change according to homophily between neighbor's 
 * culture and the agent's
 * 4. Cultural resilience might be able to set back a trait in an agent
 * 5. Also, the possibility to go back to its own culture without having to roll again
 * @author tico
 *
 */
public class UlloaA2M extends Ulloa1 {

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
					// Check for interaction
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
						
						// Cultural resilience: resistance to change based on cultural 
						// similarity or agent similarity
						boolean is_cultural_resilience = nationality_trait != -1 && 
								rand.nextFloat() <= cultural_overlap / 
								// Math.max because it might have been a selection error
								(float) (Math.max(1, agents_overlap) +  
										cultural_overlap);
						
						// if the culture roll against the change, it give the
						// trace back to the agent
						if (is_cultural_resilience){
							beliefs[r][c][selected_feature] = nationality_trait;
						}
						// if there is no cultural shock (current trait is different to its nationality's), 
						// accept the change
						else  {							
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
							
							// avoid divisions by 0
							if (cultural_overlap == 0 && neighbors_culture_overlap == 0) {
								cultural_overlap = neighbors_culture_overlap = 1;
							}
							
							float cultural_factor = culturesN[neighbors_nationality] * cultural_overlap;
							// If, after the interaction, the amount of citizens in the neighbors culture times
							// its similarity is bigger than the agent's then the agent will change its culture 
							// to its neighbor's according to a related probability
							if (rand.nextFloat() > cultural_factor  / 
									(float) (culturesN[nationality] * neighbors_culture_overlap  + cultural_factor )){
							
								// if the nationalities are different, then nationality change to its neighbors
								if (nationality != neighbors_nationality) {
									
									// its culture lost a citizen
									culturesN[nationality]--;
									nationalities[r][c] = neighbors_nationality;
									culturesN[neighbors_nationality]++;
									
								} // END of different nationality
								
								// if there is no trait selected for the selected feature, then make the
								// selected trait part of the culture
								if (cultures[neighbors_nationality][selected_feature] == -1) {
									cultures[neighbors_nationality][selected_feature] = selected_trait;
								} // END of add a cultural trait to nationality
								
							} 
							// if after the interaction I realize that there is no identification with
							// my culture, then go back to free agent
							else if (cultural_overlap == 0) {
								System.out.println("identity lost");
								// its culture lost a citizen
								culturesN[nationality]--;
								nationalities[r][c] = r * ROWS + c;
								culturesN[nationalities[r][c]]++;
								
								//delete de agent identity
								for (int f = 0; f < FEATURES; f++) {
									cultures[nationalities[r][c]][f] = -1;
								}
								
							} // END of change of nationality
							
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
	
}
