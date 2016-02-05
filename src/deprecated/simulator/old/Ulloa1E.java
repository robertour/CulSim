package deprecated.simulator.old;

import java.io.IOException;

/**
 * Based on FlacheExperiment1 this class implements:
 * 1. Central repositories
 * 2. Probabilistic change confronting agents homophily and culture homophily
 * 3. Probabilistic cultural change according to homophily between neighbor's culture 
 * and the agent's
 * 4. Reconciliation process (votes) in which the members of each culture decides all 
 * the active cultural traits. At the end one of them is selected
 * @author tico
 *
 */
public class Ulloa1E extends Ulloa1D {
	

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
	
					// get the number of mismatches between the two agents
					int mismatchesN = 0;
					// get the number of identical traits between the agent and its culture
					int cultural_overlap = 0;
					for (int f = 0; f < FEATURES; f++) {
						if (beliefs[r][c][f] != beliefs[nr][nc][f]) {
							mismatches[mismatchesN] = f;
							mismatchesN++;
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
							int neighbors_nationality = institutions[nr][nc];
							for (int f = 0; f < FEATURES; f++) {
								if (beliefs[r][c][f] == institution_beliefs[neighbors_nationality][f]) {
									neighbors_culture_overlap++;
								}
							}
							
							// avoid divisions by 0
							if (cultural_overlap == 0 && neighbors_culture_overlap == 0) {
								cultural_overlap = neighbors_culture_overlap = 1;
							}
							
							float cultural_factor = institutionsN[neighbors_nationality] * cultural_overlap;
							// If, after the interaction, the amount of citizens in the neighbors culture times
							// its similarity is bigger than the agent's then the agent will change its culture 
							// to its neighbor's according to a related probability
							if (rand.nextFloat() > cultural_factor  / 
									(float) (institutionsN[nationality] * neighbors_culture_overlap  + cultural_factor )){
							
								// if the nationalities are different, then nationality change to its neighbors
								if (nationality != neighbors_nationality) {
									
									// its culture lost a citizen
									institutionsN[nationality]--;
									institutions[r][c] = neighbors_nationality;
									institutionsN[neighbors_nationality]++;
									
									// Temporal variables of the agent's right and left country men
									int rr = countryman_right_r[r][c];
									int rc = countryman_right_c[r][c];
									int lr = countryman_left_r[r][c];
									int lc = countryman_left_c[r][c];
									
									// Remove the agent from the current culture asking my country men
									// to grab each other
									countryman_left_r[rr][rc] = lr;
									countryman_left_c[rr][rc] = lc;
									countryman_right_r[lr][lc] = rr;
									countryman_right_c[lr][lc] = rc;
									
									// Temporal variables of the right's neighbor country man
									int nrr = countryman_right_r[nr][nc];
									int nrc = countryman_right_c[nr][nc];
									
									// The agent grab the neighbor with the left and the right's neighbor 
									// country man with the right
									countryman_right_r[r][c] = nrr;
									countryman_right_c[r][c] = nrc;
									countryman_left_r[r][c] = nr;
									countryman_left_c[r][c] = nc;
									
									// The neighbor and its right countryman grab the agent 
									countryman_right_r[nr][nc] = r;
									countryman_right_c[nr][nc] = c;
									countryman_left_r[nrr][nrc] = r;
									countryman_left_c[nrr][nrc] = c;
									
								} // END of different nationality
								
								// if there is no trait selected for the selected feature, then make the
								// selected trait part of the culture
								if (institution_beliefs[neighbors_nationality][selected_feature] == -1) {
									institution_beliefs[neighbors_nationality][selected_feature] = selected_trait;
								} // END of add a cultural trait to nationality
								
								
							}// END of change of nationality
							
						} // END of cultural shock
						
					} // END of checking for interaction
					
					// mutation
					if ( rand.nextFloat() >= 1 - MUTATION ) {
						beliefs[r][c][rand.nextInt(FEATURES)] = rand.nextInt(TRAITS);
					}
					
				} // END of total agents
				

				
				// traverse the neighbors
				for (int r = 0; r < ROWS; r++) {
					
					for (int c = 0; c < COLS; c++) {
						
						// if it hasn't vote
						if (votes_flags[r][c] == hasnt_vote_flag) {
													
							// select the nationality
							int nationality = institutions[r][c];
							
														
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
								if (institution_beliefs[nationality][f] != -1){
									culture_current_trait_votes = votes[f][institution_beliefs[nationality][f]];
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
	
								int current_trait =  institution_beliefs[nationality][selected_feature];
								// if there was actually a trait that got more (and only more) votes
								// then randomly select one out of the winners and change the trait
								if (current_trait == -1 || max_difference_trait_votes > 0){
									institution_beliefs[nationality][selected_feature] = max_traits[feature_trait_index];
								}
							}
							
						} // END of it hasn't vote
						
					} // END of cols
					
				} // END of rows
					
				// change the flag
				hasnt_vote_flag = !hasnt_vote_flag;
				
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
