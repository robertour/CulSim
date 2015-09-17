package simulator.pretests;

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
public class U_H_CC_A_F extends Ulloa1 {

	// Internal variables declared just one (same as FLACHE_EXPERIMENT2)
	/**
	 * Counts the votes of each trait per feature
	 */
	protected int [][] votes;
	/**
	 * Candidates for the feature
	 */
	protected int [] feature_candidates;
	/**
	 * Candidates for the trait
	 */
	protected int [] trait_candidates;
	/**
	 * Candidates for the trait
	 */
	protected int [] nationality_candidates; 	
	
	@Override
	public void setup() {
		super.setup();
		votes = new int[FEATURES][TRAITS];
		feature_candidates = new int[FEATURES];
		trait_candidates = new int[TRAITS];
		nationality_candidates = new int[NEIGHBOURS + 1]; // + 1 because own nationality is possible
		
	}
	
	@Override
	protected void reset() {
		super.reset();
		votes = null;
		feature_candidates = null;
		trait_candidates = null;
		nationality_candidates = null;
	}

	@Override
	public void run_experiment() {
		for (iteration = 0; iteration < ITERATIONS; iteration++) {
			for (int ic = 0; ic < CHECKPOINT; ic++) {
				for (int i = 0; i < TOTAL_AGENTS; i++) {
					
					// select the agent
					int r = rand.nextInt(ROWS);
					int c = rand.nextInt(COLS);

					// select the nationality
					int nationality = institutions[r][c];
					
					// get the number of identical traits between the agent and its own culture
					int cultural_overlap = 0; 
					for (int f = 0; f < FEATURES; f++) {
						if (beliefs[r][c][f] == institution_beliefs[nationality][f]) {
							cultural_overlap++;
						}
					}
					
					// clean the votes
					for (int f = 0; f < FEATURES; f++) {
						for (int t = 0; t < TRAITS; t++) {
							votes[f][t] = 0;
						}
					}
					
					// Start with my own nationality as a candidate
					nationality_candidates[0] = nationality;
					
					// Number of cultural candidates
					int nationality_candidatesN = 1;
					
					// Max culture overlap of with a neighbor's. Start with my own culture.
					int max_neighbors_cultural_overlap = cultural_overlap;
					
					// iterate over the neighbors to calculate the votes
					for (int n = 0; n < neighboursN[r][c]; n++){

						// row and column of the current neighbor		
						int nr = neighboursX[r][c][n];
						int nc = neighboursY[r][c][n];
					
						// get the neighbors nationality
						int neighbors_nationality = institutions[nr][nc];
	
						// get the number of mismatches between the two agents
						int mismatchesN = 0;					
						// get the number of identical traits between the agent and its neighbors's culture
						int neighbors_cultural_overlap = 0;

						// Calculate the mismatches and the cultural overlap with the neighbors
						for (int f = 0; f < FEATURES; f++) {
							if (beliefs[r][c][f] != beliefs[nr][nc][f]) {
								mismatches[mismatchesN] = f;
								mismatchesN++;
							}
							if (beliefs[r][c][f] == institution_beliefs[neighbors_nationality][f]) {
								neighbors_cultural_overlap++;
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

							// include the neighbor's beliefs into the votes
							for (int f = 0; f < FEATURES; f++) {
								votes[f][beliefs[nr][nc][f]]++;	
							}	
							
							// Add nationality candidate
							if (neighbors_cultural_overlap >= max_neighbors_cultural_overlap) {
								//strictly bigger: then start a new set of candidates
								if (neighbors_cultural_overlap > max_neighbors_cultural_overlap) {
									// restart the counter
									nationality_candidatesN = 0;
									nationality_candidates[nationality_candidatesN++] = neighbors_nationality;
									max_neighbors_cultural_overlap = neighbors_cultural_overlap;
								} 
								// exactly the same
								else {
									
									// let's assume that the culture is different
									boolean is_different_culture = true;
									
									// make sure the nationality is already included, reverse for efficiency
									for(int nation = nationality_candidatesN - 1; nation >= 0; nation--) {
										if (nationality_candidates[nation] == neighbors_nationality ){
											is_different_culture = false;
										}
									}
									
									// if the nationality is different from the current ones, then add it to the candidates
									if (is_different_culture) {										
										nationality_candidates[nationality_candidatesN++] = neighbors_nationality;
									}
								}
								
							} // End of add nationality candidate		
							
						}  // End of interaction
						
					} // End of voting

					// select the nationality (it could be the same), and we already know that the overlap is bigger
					int neighbors_nationality = nationality_candidates[rand.nextInt(nationality_candidatesN)];
					
					
					// Get the candidates features
					int feature_candidatesN = 0;
					for (int f = 0; f < FEATURES; f++) {
						int current_trait = beliefs[r][c][f];
						int current_trait_votes = votes[f][current_trait];
						for (int t = 0; t < TRAITS; t++) {
							if (t != current_trait && votes[f][t] >= current_trait_votes){
								feature_candidates[feature_candidatesN++] = f;
								t = TRAITS; // break the loop
							}
						}					
					} // End of getting the possible feature
					
					// Select the candidate
					if (feature_candidatesN > 0){
						int selected_feature = feature_candidates[rand.nextInt(feature_candidatesN)];
						int max_trait = beliefs[r][c][selected_feature];
						int current_votes = votes[selected_feature][max_trait];
						int max_votes = current_votes;
						int trait_candidatesN = 0;
						
						// get the candidate traits
						for (int t = 0; t < TRAITS; t++) {
							int v = votes[selected_feature][t];
							if (max_votes == v) {
								trait_candidates[trait_candidatesN++] = t;							
							} else if (max_votes < v) {
								trait_candidates[0] = t;
								trait_candidatesN = 1;
								max_votes = v;
							}						
						}						
						
						// select the trait
						int selected_trait = trait_candidates[rand.nextInt(trait_candidatesN)];
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
						if (cultural_overlap == selected_trait && 
								beliefs[r][c][selected_feature] != neighbors_nationality_trait) {
							cultural_overlap++;
						} 
						//otherwise, if the new trait is different from the nationality trait, and 
						// the current agent's trait is the same of the nationality then the cultural 
						// overlap will decrease
						else if (neighbors_nationality_trait != selected_trait && 
								beliefs[r][c][selected_feature] == neighbors_nationality_trait) {
							max_neighbors_cultural_overlap--;
						}
						
						// if the selected feature of the neighbor's nationality hasn't been
						// assigned yet, then it is an opportunity to be more similar
						if (institution_beliefs[neighbors_nationality][selected_feature] == -1) {
							max_neighbors_cultural_overlap++;
						}
						

						// when the agent doesn't have any similarity with the cultures then
						// he loses his identity and accept the trait. This also avoid divisions 
						// by 0 in the next condition.
						if (cultural_overlap == 0 && max_neighbors_cultural_overlap == 0) {

							// Nothing happen when the agent is not similar to any of the two cultures
						} else {
							// the alpha regulates how resilient the culture is
							float cultural_factor = cultural_overlap * ALPHA;
						
							// if there is no cultural shock (current trait is different to its nationality's), 
							// accept the change
							if (beliefs[r][c][selected_feature] != nationality_trait || 
							// if the agent's current trait is equal to its nationality's (cultural shock),
							// then the agent will impose resistance to change depending how identified it is 
							// with its nationality	(cultural overlap)	
							beliefs[r][c][selected_feature] == nationality_trait &&
							// Cultural resilience: resistance to change based on cultural 
							// similarity or agent similarity
							(rand.nextFloat() >= cultural_factor / 
									(float) max_neighbors_cultural_overlap * BETA +  
									cultural_factor)) {
							
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
														
						} // END of else
						
					} // END of checking for interaction
					
					// mutation
					if ( rand.nextFloat() >= 1 - MUTATION ) {
						beliefs[r][c][rand.nextInt(FEATURES)] = rand.nextInt(TRAITS);
					}
				}
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
