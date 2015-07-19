package simulator.scenarios;

import java.io.IOException;

import simulator.old.Ulloa1;

/**
 * Based on FlacheExperiment3 this class implements:
 * 1. Homophily
 * 2. Social Influence
 * 3. Probabilistic change confronting agents homophily and institution homophily 
 * (and an alpha associated)
 * 4. Probabilistic institutional change according to homophily between neighbor's
 * institution and the agent's (and an alpha prima associated)
 * 
 * @author tico
 *
 */
public class ScenarioAFlache5 extends Ulloa1 {

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
	protected int [] institution_candidates;
	
	
	
	@Override
	public void setup() {
		super.setup();
		votes = new int[FEATURES][TRAITS];
		feature_candidates = new int[FEATURES];
		trait_candidates = new int[TRAITS];
		institution_candidates = new int[NEIGHBOURS + 1]; // + 1 because own institution is possible
		
	}
	
	@Override
	protected void reset() {
		super.reset();
		votes = null;
		feature_candidates = null;
		trait_candidates = null;
		institution_candidates = null;
	}

	@Override
	public void run_experiment() {
		
		// Variables are declared globally because there might be 
		// a small chance to increase performance.
		int r;
		int c;
		int institution;
		int institution_candidatesN;
		int max_neighbors_institution_overlap; 	
		int nr;
		int nc;
		int neighbor_institution;
		int mismatchesN;
		int neighbors_institution_overlap;
		int agents_overlap;
		int neighbors_institution;
		int feature_candidatesN;
		int current_trait;
		int selected_feature;
		int current_votes;
		int max_votes;
		int trait_candidatesN;
		int selected_trait;
		int institution_trait;
		int neighbors_institution_trait;
		int temp_v;
		double institution_resistance;
		
		for (iteration = 0; iteration < ITERATIONS; iteration++) {
			for (int ic = 0; ic < CHECKPOINT; ic++) {
				for (int i = 0; i < TOTAL_AGENTS; i++) {
					
					// select the agent
					r = rand.nextInt(ROWS);
					c = rand.nextInt(COLS);

					// select the agent's institution
					institution = institutions[r][c];
					
					// get the number of identical traits between the agent and its own institution
					int institution_overlap = 0; 
					for (int f = 0; f < FEATURES; f++) {
						if (beliefs[r][c][f] == institution_beliefs[institution][f]) {
							institution_overlap++;
						}
					}
					
					// clean the votes
					for (int f = 0; f < FEATURES; f++) {
						for (int t = 0; t < TRAITS; t++) {
							votes[f][t] = 0;
						}
					}
					
					// Start with my own institution as a candidate
					institution_candidates[0] = institution;
					
					// Number of institution candidates
					institution_candidatesN = 1;
					
					// Max institution overlap of with a neighbor's. Start with my own institution.
					max_neighbors_institution_overlap = institution_overlap;

					
					// iterate over the neighbors to calculate the votes
					for (int n = 0; n < neighboursN[r][c]; n++){

						// row and column of the current neighbor
						nr = neighboursX[r][c][n];
						nc = neighboursY[r][c][n];
					
						// get the neighbors institution						
						neighbor_institution = institutions[nr][nc];
	
						// get the number of identical traits between the agent and its neighbors's institution
						neighbors_institution_overlap = 0;
						
						// get the number of mismatches between the two agents
						mismatchesN = 0;

						// Calculate the mismatches and the cultural overlap with the neighbors
						for (int f = 0; f < FEATURES; f++) {
							if (beliefs[r][c][f] != beliefs[nr][nc][f]) {
								mismatches[mismatchesN] = f;
								mismatchesN++;
							}
							if (beliefs[r][c][f] == institution_beliefs[neighbor_institution][f]) {
								neighbors_institution_overlap++;
							}
						}
						// calculate the overlap between the agents						
						agents_overlap = FEATURES - mismatchesN;
					

						// check if there is actual interaction checking against the homophily and 
						// considering the selection error. The present formula integrates homophily
						// and selection error into one probability. 
						if (rand.nextFloat() <  
								( (agents_overlap / (float) FEATURES) * (1-SELECTION_ERROR) + 
								(     mismatchesN / (float) FEATURES) * SELECTION_ERROR  ) ) {

							
							// include the neighbor's beliefs into the votes
							for (int f = 0; f < FEATURES; f++) {
								votes[f][beliefs[nr][nc][f]]++;	
							}	
							
							// Add institution candidate
							if (neighbors_institution_overlap >= max_neighbors_institution_overlap) {
								//strictly bigger: then start a new set of candidates
								if (neighbors_institution_overlap > max_neighbors_institution_overlap) {
									// restart the counter
									institution_candidatesN = 0;
									institution_candidates[institution_candidatesN++] = neighbor_institution;
									max_neighbors_institution_overlap = neighbors_institution_overlap;
								} 
								// exactly the same
								else {
									
									// we add all the institutions of all the candidates
									// There will be repetitions but the repetitions just give more common
									// institutions, more chances of being selected
									institution_candidates[institution_candidatesN++] = neighbor_institution;
									
								}
								
							} // End of add institution candidate		
							
						}  // End of interaction
						
					} // End of voting
					
					// Get the candidates features
					feature_candidatesN = 0;
					for (int f = 0; f < FEATURES; f++) {
						current_trait = beliefs[r][c][f];
						for (int t = 0; t < TRAITS; t++) {
							if (t != current_trait && votes[f][t] >= votes[f][current_trait]){
								feature_candidates[feature_candidatesN++] = f;
								t = TRAITS; // break the loop
							}
						}					
					} // End of getting the possible feature
					
					// Select the candidate
					if (feature_candidatesN > 0){
						selected_feature = feature_candidates[rand.nextInt(feature_candidatesN)];
						current_votes = votes[selected_feature][beliefs[r][c][selected_feature]];
						max_votes = current_votes;
						trait_candidatesN = 0;
						
						// get the candidate traits
						for (int t = 0; t < TRAITS; t++) {
							temp_v = votes[selected_feature][t];
							if (max_votes == temp_v) {
								trait_candidates[trait_candidatesN++] = t;							
							} else if (max_votes < temp_v) {
								trait_candidates[0] = t;
								trait_candidatesN = 1;
								max_votes = temp_v;
							}						
						}						

						// select the neighbors institution
						// TODO should it be a majority?
						neighbors_institution = institution_candidates[rand.nextInt(institution_candidatesN)];
						
						// select the trait
						selected_trait = trait_candidates[rand.nextInt(trait_candidatesN)];
						institution_trait = institution_beliefs[institution][selected_feature];
						neighbors_institution_trait = institution_beliefs[neighbors_institution][selected_feature];

												
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
						// agent's trait is different from the institution then the cultural overlap 
						// will increase
						if (institution_overlap == selected_trait && 
								beliefs[r][c][selected_feature] != neighbors_institution_trait) {
							max_neighbors_institution_overlap++;
						} 
						//otherwise, if the new trait is different from the institution trait, and 
						// the current agent's trait is the same of the institution then the cultural 
						// overlap will decrease
						else if (neighbors_institution_trait != selected_trait && 
								beliefs[r][c][selected_feature] == neighbors_institution_trait) {
							max_neighbors_institution_overlap--;
						}
						
						// if the selected feature of the neighbor's institution hasn't been
						// assigned yet, then it is an opportunity to be more similar
						if (institution_beliefs[neighbors_institution][selected_feature] == -1) {
							max_neighbors_institution_overlap++;
						}
						
						//////////////////////
						// CHANGE THE TRAIT //
						//////////////////////
						// we change the trait after adjusting the overlaps //
						beliefs[r][c][selected_feature] = selected_trait;
						
						// when the agent doesn't have any similarity with the institutions then
						// he loses his identity and accept the trait. This also avoid divisions 
						// by 0 in the next condition.
						if (institution_overlap == 0 && max_neighbors_institution_overlap == 0) {
							// Nothing happen when the agent is not similar to any of the two institutions
						} else {
							
							institution_resistance = institution_overlap * ALPHA_PRIME;
							
							if (rand.nextFloat() >= institution_resistance / 
									(float) max_neighbors_institution_overlap * BETA_PRIME +  
									institution_resistance) {
							
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

							} // END of institutional shock
							
						} // END of else
						
					} // END of else (institutional conflict)
					
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
