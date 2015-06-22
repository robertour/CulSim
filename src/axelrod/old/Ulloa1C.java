package axelrod.old;

import java.io.IOException;

/**
 * Based on FlacheExperiment1 this class implements:
 * 1. Central repositories
 * 2. Probabilistic change confronting agents homophily and culture homophily
 * 3. Probabilistic cultural change according to homophily between neighbor's culture 
 * and the agent's
 * 4. Reconciliation process (votes) in which the members of each culture decides one and
 * just one of the active cultural traits
 * @author tico
 *
 */
public class Ulloa1C extends Ulloa1B {

	
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
								
								// if the nationalities are different, then nationality change to its
								// neighbors
								if (nationality != neighbors_nationality) {
									
									// its culture lost a citizen
									culturesN[nationality]--;
									nationalities[r][c] = neighbors_nationality;
									culturesN[neighbors_nationality]++;
									
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
								if (cultures[neighbors_nationality][selected_feature] == -1) {
									cultures[neighbors_nationality][selected_feature] = selected_trait;
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
							int nationality = nationalities[r][c];
							
							// select the features different from -1
							int active_featuresN = 0;
							for (int f = 0; f < FEATURES; f++) {
								if (cultures[nationality][f] != -1) {
									active_features[active_featuresN++] = f; 
								}
							}
							
							// enter the votes if there is at least one active feature
							if (active_featuresN > 0) {
							
								// clean the votes of the active features
								for (int f = 0; f < active_featuresN; f++) {
									for (int t = 0; t < TRAITS; t++) {
										votes[active_features[f]][t] = 0;
									}
								}
								
								// include my votes
								int nr = r;
								int nc = c;
								int temp_r;
								
								// country-men votes
								do {
									
									// let the agent vote on all the active features
									for (int f = 0; f < active_featuresN; f++) {
										votes[active_features[f]][beliefs[nr][nc][active_features[f]]]++;
									}
									
									votes_flags[nr][nc] = !hasnt_vote_flag;
									
									// avoid overwriting the nr before time
									temp_r = nr;
									
									// look for the next country man on the right
									nr = countryman_right_r[nr][nc];
									nc = countryman_right_c[temp_r][nc];
									
									
									if (votes_flags[nr][nc] != hasnt_vote_flag && !(nr == r && nc == c)) {
										System.out.println("Circular list Kaput!!! Somebody already voted.");
									}
									
									// while the next agent hasn't vote (nr == r && nc == c)
								} while (votes_flags[nr][nc] == hasnt_vote_flag ) ;
								// END of country men votes
								
	
								// set winner traits for the current culture
								int current_feature = active_features[rand.nextInt(active_featuresN)];
								int current_trait = cultures[nationality][current_feature];;
								int max_trait_votes = votes[current_feature][current_trait];
								int max_traitsN = 0;
									
								// search for the traits with most votes
								for (int t = 0; t < TRAITS; t++){
									if ( max_trait_votes < votes[current_feature][t] ){
										max_trait_votes = votes[current_feature][t];
										max_traitsN = 0;
										max_traits[max_traitsN++] = t;
									} else if ( max_trait_votes == votes[current_feature][t] ){
										max_traits[max_traitsN++] = t;
									}
								} // END of search for the traits with most votes
								
								// if there was actually a trait that got more (and only more) votes
								// then randomly select one out of the winners and change the trait
								if (max_trait_votes > votes[current_feature][current_trait]){
									cultures[nationality][current_feature] = max_traits[rand.nextInt(max_traitsN)];
								}
									
								
							} // END of if activeN > 0
							else {
								votes_flags[r][c] = !hasnt_vote_flag;
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
