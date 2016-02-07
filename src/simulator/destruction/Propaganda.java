package simulator.destruction;


/**
 * Based on FlacheExperiment1 this class implements:
 * 1. Homophily
 * 2. Probabilistic change confronting agents homophily and institution homophily 
 * (and an alpha associated)
 * 3. Probabilistic institutional change according to homophily between neighbor's
 * culture and the agent's (and an alpha prima associated)
 * 
 * @author tico
 *
 */
public class Propaganda extends Democracy {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5227488572760673961L;

	@Override
	public void run_iteration() {

		for (int ic = 0; ic < CHECKPOINT; ic++) {
			for (int i = 0; i < TOTAL_AGENTS; i++) {
				
				// select the agent
				r = rand.nextInt(ROWS);
				c = rand.nextInt(COLS);

				// select the neighbor that might influence the agent
				n = rand.nextInt(neighboursN[r][c]);
				nr = neighboursX[r][c][n];
				nc = neighboursY[r][c][n];
				
				// select the agent's and neighbor's institution
				institution = institutions[r][c];					
				neighbors_institution = institutions[nr][nc];

				// get the number of mismatches between the two agents					
				mismatchesN = 0;
				
				// get the number of identical traits between the agent and its institution					
				institution_overlap = 0;		
				
				// get the number of identical traits between the agent and its neighbors's institution					
				neighbors_institution_overlap = 0;
				
				for (int f = 0; f < FEATURES; f++) {
					if (beliefs[r][c][f] != beliefs[nr][nc][f]) {
						mismatches[mismatchesN] = f;
						mismatchesN++;
					}
					if (beliefs[r][c][f] == institution_beliefs[neighbors_institution][f]) {
						neighbors_institution_overlap++;
					}
					if (beliefs[r][c][f] == institution_beliefs[institution][f]) {
						institution_overlap++;
					}
				}

				// if arrive here by selection error, there is still a chance of influence the institution
				if (mismatchesN == 0 ) {  
					selected_feature = rand.nextInt(FEATURES);
				} else {
					selected_feature = mismatches[rand.nextInt(mismatchesN)];
				}
				
				// select the traits in play (agent's, neighbor's and institution's)
				selected_trait = beliefs[nr][nc][selected_feature];
				institution_trait = institution_beliefs[institution][selected_feature];
				institution_trait = institution_beliefs[institution][selected_feature];
				neighbors_institution_trait = institution_beliefs[neighbors_institution][selected_feature];
				

				
				// When there is no institutional conflict because the institution favors the selected trait
				// or because the trait is already different, then just use homophily
				if (selected_trait == institution_trait || 
						institution_trait != -1 && beliefs[r][c][selected_feature] != institution_trait){
					
					// check if there is actual interaction checking against the homophily and 
					// considering the selection error. The present formula integrates homophily
					// and selection error into one probability. 
					if (rand.nextFloat() <  
							( ((FEATURES - mismatchesN) / (float) FEATURES) * (1-SELECTION_ERROR) + 
							(              mismatchesN  / (float) FEATURES) * SELECTION_ERROR  ) ) {

						//////////////////////
						// CHANGE THE TRAIT //
						//////////////////////
						beliefs[r][c][selected_feature] = selected_trait;
					}
						
				} else {
					
					// Probability of interaction taking into account selection error
					float prob_int = ( ((FEATURES - mismatchesN) / (float) FEATURES) * (1-SELECTION_ERROR) + 
									   (            mismatchesN  / (float) FEATURES) * SELECTION_ERROR  );
					
					// Calculate the institutional resistance
					institution_resistance = ALPHA * institution_overlap / (float) FEATURES;
					
					// check if there is actual interaction 
					if (rand.nextFloat() >= institution_resistance / (prob_int * BETA + institution_resistance) ) {
					
						
						// at this point, we are sure that the agent is losing similarity with the institution
						// if the new trait is the same of the institution trait, and the current 
						// agent's trait is different from the institution then the cultural overlap 
						// will increase
						if (institution_trait == selected_trait && 
								beliefs[r][c][selected_feature] != institution_trait) {
							 institution_overlap++;
						} 
						//otherwise, if the new trait is different from the institution trait, and 
						// the current agent's trait is the same of the institution then the cultural 
						// overlap will decrease
						else if (institution_trait != selected_trait && 
								beliefs[r][c][selected_feature] == institution_trait) {
							institution_overlap--;
						}
													
						
						// if the new trait is the same of the institution trait, and the current 
						// agent's trait is different from the institution then the institutional overlap 
						// will increase
						if (neighbors_institution_trait == selected_trait && 
								beliefs[r][c][selected_feature] != neighbors_institution_trait) {
							 neighbors_institution_overlap++;
						} 
						//otherwise, if the new trait is different from the institution trait, and 
						// the current agent's trait is the same of the institution then the institutional 
						// overlap will decrease
						else if (neighbors_institution_trait != selected_trait && 
								beliefs[r][c][selected_feature] == neighbors_institution_trait) {
							neighbors_institution_overlap--;
						}
						
						// if the selected feature of the neighbor's institution hasn't been
						// assigned yet, then it is an opportunity to be more similar
						if (institution_beliefs[neighbors_institution][selected_feature] == -1) {
							neighbors_institution_overlap++;
						}						
						

						//////////////////////
						// CHANGE THE TRAIT //
						//////////////////////
						// we change the trait after adjusting the overlaps //
						beliefs[r][c][selected_feature] = selected_trait;
					
						// when the agent doesn't have any similarity with the institutions then
						// he loses his identity and accept the trait. This also avoid divisions 
						// by 0 in the next condition.
						if (institution_overlap == 0 && neighbors_institution_overlap == 0) {
							// Nothing happen when the agent is not similar to any of the two institutions
						}						
						// if there is no institutional shock (current trait is different to its institution's), 
						// accept the change
						else {
							institutional_factor = institution_overlap * ALPHA_PRIME;
						
							if (rand.nextFloat() >= institutional_factor / 
									(float) neighbors_institution_overlap * BETA_PRIME +  
									institutional_factor) {
									
								// if the institutions are different, then institution change to its neighbors
								if (institution != neighbors_institution) {
									// its institution lost a citizen
									institutionsN[institution]--;
									institutions[r][c] = neighbors_institution;
									institutionsN[neighbors_institution]++;	
								} // END of different institution
								
								// if there is no trait selected for the selected feature, then make the
								// selected trait part of the institution
								if (institution_beliefs[neighbors_institution][selected_feature] == -1) {
									institution_beliefs[neighbors_institution][selected_feature] = selected_trait;
								} // END of add a institutional trait to institution
		
							} // END of institution change
							
						} // END of else
						
					} // END of institutional shock
					
				} // END of else
				
				
				// mutation
				if ( rand.nextFloat() >= 1 - MUTATION ) {
					beliefs[r][c][rand.nextInt(FEATURES)] = rand.nextInt(TRAITS);
				}
				
			} // END of total agents
			
			
			if ((iteration * CHECKPOINT + ic) % FREQ_PROP == 0){
			
				// Propaganda Process
				// traverse rows
				for (r = 0; r < ROWS; r++) {
					
					// traverse columns
					for (c = 0; c < COLS; c++) {
						
						// if it hasn't vote
						if (votes_flags[r][c] == hasnt_vote_flag) {
													
							// select the institution
							institution = institutions[r][c];
							
							// include my votes
							nr = r;
							nc = c;
							temp_r = nr;
							
                            do {
                            	
                            	// reset the mismatches counter
                            	mismatchesN = 0;
                            	
                            	// count mismatches between the agent and the institution 
                            	for (int f = 0; f < FEATURES; f++) {
            						if (institution_beliefs[institution][f] != beliefs[nr][nc][f]) {
            							mismatchesN++;
            						}
            					}
                            	
                            	for (int f = 0; f < FEATURES; f++) {
	                            	// check if the propaganda has effect by measuring the similarity with the institution 
            						if (institution_beliefs[institution][f] != -1 && beliefs[nr][nc][f] != institution_beliefs[institution][f] && rand.nextFloat() > mismatchesN  / (float) FEATURES ) {
            							beliefs[nr][nc][f] = institution_beliefs[institution][f];
            						}
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

							
						} // END of it hasn't vote
						
					} // END of cols
					
				} // END Propaganda Process
					
				// change the flag
				hasnt_vote_flag = !hasnt_vote_flag;
			
			}
				
		} // END of checkpoint

	} // END of run_experiment
	
}