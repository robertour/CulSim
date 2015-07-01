package simulator.paper;

import java.io.IOException;

import simulator.old.Ulloa1;

/**
 * Based on FlacheExperiment1 this class implements:
 * 1. Central repositories
 * 2. Probabilistic change confronting agents homophily and culture homophily
 * 3. Probabilistic cultural change according to homophily between neighbor's 
 * culture and the agent's
 * @author tico
 *
 */
public class U_H_IL_CC extends Ulloa1 {

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
					int nationality = institutions[r][c];
					// select the neighbors nationality
					int neighbors_nationality = institutions[nr][nc];
	
					// get the number of mismatches between the two agents
					int mismatchesN = 0;
					// get the number of identical traits between the agent and its culture
					int cultural_overlap = 0;					
					// get the number of identical traits between the agent and its neighbors's culture
					int neighbors_cultural_overlap = 0;
					
					for (int f = 0; f < FEATURES; f++) {
						if (beliefs[r][c][f] != beliefs[nr][nc][f]) {
							mismatches[mismatchesN] = f;
							mismatchesN++;
						}
						if (beliefs[r][c][f] == institution_beliefs[neighbors_nationality][f]) {
							neighbors_cultural_overlap++;
						}
						if (beliefs[r][c][f] == institution_beliefs[nationality][f]) {
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
						int nationality_trait = institution_beliefs[nationality][selected_feature];
						int neighbors_nationality_trait = institution_beliefs[neighbors_nationality][selected_feature];

						// if the new trait is the same of the nationality trait, and the current 
						// agent's trait is different from the nationality then the cultural overlap 
						// will increase
						if (nationality_trait == selected_trait && 
								beliefs[r][c][selected_feature] != nationality_trait) {
							 cultural_overlap++;
						} 
						//otherwise, if the new trait is different from the nationality trait, and 
						// the current agent's trait is the same of the nationality then the cultural 
						// overlap will decrease
						else if (nationality_trait != selected_trait && 
								beliefs[r][c][selected_feature] == nationality_trait) {
							 cultural_overlap--;
						}
						
						
						// if the new trait is the same of the nationality trait, and the current 
						// agent's trait is different from the nationality then the cultural overlap 
						// will increase
						if (neighbors_nationality_trait == selected_trait && 
								beliefs[r][c][selected_feature] != neighbors_nationality_trait) {
							 neighbors_cultural_overlap++;
						} 
						//otherwise, if the new trait is different from the nationality trait, and 
						// the current agent's trait is the same of the nationality then the cultural 
						// overlap will decrease
						else if (neighbors_nationality_trait != selected_trait && 
								beliefs[r][c][selected_feature] == neighbors_nationality_trait) {
							neighbors_cultural_overlap--;
						}
						
						// if the selected feature of the neighbor's nationality hasn't been
						// assigned yet, then it is an opportunity to be more similar
						if (institution_beliefs[neighbors_nationality][selected_feature] == -1) {
							neighbors_cultural_overlap++;
						}
						
						// when the agent doesn't have any similarity with the cultures then
						// he loses his identity and accept the trait. This also avoid divisions 
						// by 0 in the next condition.
						if (cultural_overlap == 0 && neighbors_cultural_overlap == 0) {

							// It also accepts the trait since there is no cultural resistance
							beliefs[r][c][selected_feature] = selected_trait;
							
							// its culture lost a citizen
							institutionsN[nationality]--;
							institutions[r][c] = r * ROWS + c;
							institutionsN[institutions[r][c]]++;
							
							//delete the agent identity
							for (int f = 0; f < FEATURES; f++) {
								institution_beliefs[institutions[r][c]][f] = -1;
							}
						}
						// if there is no cultural shock (current trait is different to its nationality's), 
						// accept the change
						else if (beliefs[r][c][selected_feature] != nationality_trait || 
							// if the agent's current trait is equal to its nationality's (cultural shock),
							// then the agent will impose resistance to change depending how identified it is 
							// with its nationality	(cultural overlap)	
							beliefs[r][c][selected_feature] == nationality_trait &&
							// Cultural resilience: resistance to change based on cultural 
							// similarity or agent similarity
							(rand.nextFloat() > cultural_overlap / 
									// Math.max because it might have been a selection error
									(float) neighbors_cultural_overlap +  
											cultural_overlap)) {
							
							// change the trait	
							beliefs[r][c][selected_feature] = selected_trait;
							
							
							// if the nationalities are different, then nationality change to its neighbors
							if (nationality != neighbors_nationality) {
								// its culture lost a citizen
								institutionsN[nationality]--;
								institutions[r][c] = neighbors_nationality;
								institutionsN[neighbors_nationality]++;	
							} // END of different nationality
							
							// if there is no trait selected for the selected feature, then make the
							// selected trait part of the culture
							if (institution_beliefs[neighbors_nationality][selected_feature] == -1) {
								institution_beliefs[neighbors_nationality][selected_feature] = selected_trait;
							} // END of add a cultural trait to nationality


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
