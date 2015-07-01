package simulator.scenarios;

import java.io.IOException;

import simulator.old.Ulloa1;

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
public class ScenarioB extends Ulloa1 {

	@Override
	public void run_experiment() {

		// local variables: avoiding declarations inside the loops
		int r;
		int c;
		int n;
		int nr;
		int nc;
		int institution;
		int neighbors_institution;
		int mismatchesN;
		int institution_overlap;
		int neighbors_institution_overlap;
		int selected_feature;
		int selected_trait;
		int institution_trait;
		int neighbors_institution_trait;
		double institution_resistance;
		float institutional_factor;

		for (iteration = 0; iteration < ITERATIONS; iteration++) {
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
					
					
					// check if there is actual interaction checking against the homophily and 
					// considering the selection error. The present formula integrates homophily
					// and selection error into one probability. 
					if (rand.nextFloat() <  
							( ((FEATURES - mismatchesN) / (float) FEATURES) * (1-SELECTION_ERROR) + 
							(              mismatchesN  / (float) FEATURES) * SELECTION_ERROR  ) ) {

						
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
						// or because the trait is already different
						if (selected_trait == institution_trait || 
								institution_trait != -1 && beliefs[r][c][selected_feature] != institution_trait){

							// change the trait	
							beliefs[r][c][selected_feature] = selected_trait;
							
						} else {
							
							// Calculate the institution resistance						
							//institution_resistance = 1 - Math.pow ( 1- (institution_overlap / (float) FEATURES), ALPHA);
							institution_resistance = Math.pow ( institution_overlap / (float) FEATURES, 1 / ALPHA);
							
							if (// institutional resistence: resistance to change based on institutional 
								// similarity to the agent. This includes when the feature hasn't been writ 
								rand.nextFloat() >= institution_resistance ) {
							
								
								// at this point, we are sure that the agent is losing similarity with the institution
								// if the new trait is the same of the nationality trait, and the current 
								// agent's trait is different from the nationality then the cultural overlap 
								// will increase
								if (institution_trait == selected_trait && 
										beliefs[r][c][selected_feature] != institution_trait) {
									 institution_overlap++;
								} 
								//otherwise, if the new trait is different from the nationality trait, and 
								// the current agent's trait is the same of the nationality then the cultural 
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
								
	
								// change the trait	
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
