package simulator.paper;

import java.io.IOException;

import simulator.old.Ulloa1D;

/**
 * Based on FlacheExperiment1 this class implements:
 * 1. Central repositories
 * 2. Probabilistic change confronting agents homophily and culture homophily
 * 3. Probabilistic cultural change according to homophily between neighbor's 
 * culture and the agent's
 * @author tico
 *
 */
public class U_H_CC_P_D extends Ulloa1D {

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
					// select the neighbors nationality
					int neighbors_nationality = nationalities[nr][nc];
	
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
						if (beliefs[r][c][f] == cultures[neighbors_nationality][f]) {
							neighbors_cultural_overlap++;
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
						int neighbors_nationality_trait = cultures[neighbors_nationality][selected_feature];

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
						if (cultures[neighbors_nationality][selected_feature] == -1) {
							neighbors_cultural_overlap++;
						}
						
						
						// when the agent doesn't have any similarity with the cultures then
						// he loses his identity and accept the trait. This also avoid divisions 
						// by 0 in the next condition.
						if (cultural_overlap == 0 && neighbors_cultural_overlap == 0) {

							// Nothing happen when the agent is not similar to any of the two cultures

						} else {
							
							// Cultural resilience: resistance to change based on cultural 
							// similarity or agent similarity
							boolean is_cultural_resilience = nationality_trait != -1 && 
									rand.nextFloat() <= cultural_overlap / 
									// Math.max because it might have been a selection error
									((float) neighbors_cultural_overlap +  
											cultural_overlap);
							
							// if the culture roll against the change, it give the
							// trace back to the agent
							if (is_cultural_resilience){
								beliefs[r][c][selected_feature] = nationality_trait;
							}
							// if there is no cultural shock (current trait is different to its nationality's), 
							// accept the change
							else  {	
								
								// change the trait	
								beliefs[r][c][selected_feature] = selected_trait;
								
								
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
	
	
							} // END of cultural shock
						}
						
					} // END of checking for interaction
					
					// mutation
					if ( rand.nextFloat() >= 1 - MUTATION ) {
						beliefs[r][c][rand.nextInt(FEATURES)] = rand.nextInt(TRAITS);
					}
					
				} // END of total agents

				// Democratic Process
				// traverse rows
				for (int r = 0; r < ROWS; r++) {
					
					// traverse columns
					for (int c = 0; c < COLS; c++) {
						
						// if it hasn't vote
						if (votes_flags[r][c] == hasnt_vote_flag) {
													
							// select the nationality
							int nationality = nationalities[r][c];
							
														
							// clean the votes of the features
							for (int f = 0; f < FEATURES; f++) {
								for (int t = 0; t < TRAITS; t++) {
									votes[f][t] = 0;
								}
							}
							
							// include my votes
							int nr = r;
							int nc = c;
							int temp_r;
							
							// country-men votes
							do {
								
								// let the agent vote on all the active features
								for (int f = 0; f < FEATURES; f++) {
									votes[f][beliefs[nr][nc][f]]++;
								}
								
								votes_flags[nr][nc] = !hasnt_vote_flag;
								
								// avoid overwriting the nr before time
								temp_r = nr;
								
								// look for the next country man on the right
								nr = countryman_right_r[nr][nc];
								nc = countryman_right_c[temp_r][nc];
								
								
								if (votes_flags[nr][nc] != hasnt_vote_flag && !(nr == r && nc == c)) {
									System.out.println("Circular list Kaputt!!! Somebody already voted.");
								}
								
								// while the next agent hasn't vote (nr == r && nc == c)
							} while (votes_flags[nr][nc] == hasnt_vote_flag ) ;
							// END of country men votes
							
							// set winner traits for the current culture		
							int max_difference_trait_votes = 0;
							int max_feature_traitN = 0;
							int culture_current_trait_votes = 0;
							
							// iterate over the active features
							for (int f = 0; f < FEATURES; f++) {
								culture_current_trait_votes = 0;
								if (cultures[nationality][f] != -1){
									culture_current_trait_votes = votes[f][cultures[nationality][f]];
								}
								// search for the traits with most votes
								for (int t = 0; t < TRAITS; t++){									
									if ( max_difference_trait_votes < votes[f][t] - culture_current_trait_votes ){
										max_difference_trait_votes = votes[f][t] - culture_current_trait_votes;
										max_feature_traitN = 0;										
										max_traits[max_feature_traitN] = t;
										max_features[max_feature_traitN++] = f;
									} else if ( max_difference_trait_votes == votes[f][t] - culture_current_trait_votes  ){
										max_traits[max_feature_traitN] = t;
										max_features[max_feature_traitN++] = f;
									}
								} // END of search for the traits with most votes
							
							} // END of the iteration over the active features
							
							// if there is maximal group
							if (max_feature_traitN > 0){
								int feature_trait_index = rand.nextInt(max_feature_traitN);
								int selected_feature = max_features[feature_trait_index];
	
								int current_trait =  cultures[nationality][selected_feature];
								// if there was actually a trait that got more (and only more) votes
								// then randomly select one out of the winners and change the trait
								if (current_trait == -1 || max_difference_trait_votes > 0){
									cultures[nationality][selected_feature] = max_traits[feature_trait_index];
								}
							}
							
						} // END of it hasn't vote
						
					} // END of cols
					
				} // END Democratic Process
				
				
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
