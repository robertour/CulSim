/**
 * 
 */
package simulator.old;

import java.io.IOException;

/**
 * @author tico
 *
 */
public class UlloaFlache2 extends Ulloa1 {

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
		
		// selected agent
		int r, c,
		
		// nationality of the agent
		nationality,
		
		// row and column of the neighbor
		nr, nc,
				
		// neighbor's nationality
		neighbors_nationality,
		
		// number of agents that vote
		voters,
		
		// number of mismatches
		mismatchesN,
		
		// similarity between agents
		agents_overlap,
		
		// current neighbor's nationality overlap
		neighbors_nationality_overlap,
		
		// max neighbor's nationality overlap,
		max_neighbors_nationality_overlap,
		
		// number of nationality candidates
		nationality_candidatesN,
		
		// number of features that are candidates
		feature_candidatesN,
		
		// the current trait of the agent
		current_trait,
		
		// votes of the current trait
		current_trait_votes,
		
		// the feature that is going to change
		selected_feature,
		
		// the trait that has the most votes
		max_trait,
		
		// the votes of the current trait
		current_votes,
		
		// the votes of the trait that has the most votes
		max_votes,
		
		// candidates of possible traits
		trait_candidatesN,
		
		//temporal variable for votes
		v,
		
		// selected trait
		selected_trait,
		
		// the trait of the nationality
		nationality_trait,
		
		// number of identical traits between the agent and its nationality
		nationality_overlap,
		
		// loop indexes
		ic, i, f, t, n, nation;
		
		// average of the cultural overlap with the neighbors
		float agents_overlap_ave;
		
		// is there a selection error
		boolean is_selection_error = rand.nextFloat() >= 1 - SELECTION_ERROR,
		
		// is there interaction? 
		is_interaction,
		
		// does the neigbor has a culture that is different from the current candidates
		is_different_culture;
		

		for (iteration = 0; iteration < ITERATIONS; iteration++) {
			for (ic = 0; ic < CHECKPOINT; ic++) {
				for (i = 0; i < TOTAL_AGENTS; i++) {
					
					// select the agent
					r = rand.nextInt(ROWS);
					c = rand.nextInt(COLS);
	
					// select the nationality
					nationality = institutions[r][c];
					
					// get the number of identical traits between the agent and its own culture
					nationality_overlap = 0;
					for (f = 0; f < FEATURES; f++) {
						if (beliefs[r][c][f] == institution_beliefs[nationality][f]) {
							nationality_overlap++;
						}
					}
					
					// clean the votes
					for (f = 0; f < FEATURES; f++) {
						for (t = 0; t < TRAITS; t++) {
							votes[f][t] = 0;
						}
					}
					
					agents_overlap_ave = 0.0f;
					voters = 0;
					
					// Start with my own nationality as a candidate
					nationality_candidates[0] = nationality;
					
					// Number of cultural candidates
					nationality_candidatesN = 1;
					
					// Max culture overlap of with a neighbor's
					max_neighbors_nationality_overlap = nationality_overlap;
					
					// Initializing the neighbors nationality overlap
					neighbors_nationality_overlap = -1;
					
					// iterate over the neighbors to calculate the votes
					for (n = 0; n < neighboursN[r][c]; n++){
						
						// row and column of the neighbor
						nr = neighboursX[r][c][n];
						nc = neighboursY[r][c][n];

						// get the neighbors nationality
						neighbors_nationality = institutions[nr][nc];
						
						// get the number of mismatches between the two agents
						mismatchesN = 0;
						
						// get the number of identical traits between the agent and its culture
						neighbors_nationality_overlap = 0;
						
						for (f = 0; f < FEATURES; f++) {
							if (beliefs[r][c][f] != beliefs[nr][nc][f]) {
								mismatches[mismatchesN] = f;
								mismatchesN++;
							}
							if (beliefs[r][c][f] == institution_beliefs[neighbors_nationality][f]) {
								neighbors_nationality_overlap++;
							}
						}
						agents_overlap = FEATURES - mismatchesN;


						// Check for selection error
						is_selection_error = rand.nextFloat() >= 1 - SELECTION_ERROR;
						
						// Check for interaction based on homophily
						is_interaction = rand.nextFloat() >= 1 - ((float) agents_overlap / (float) FEATURES);
						
						
						// check if there is actual interaction 
						if (is_interaction && !is_selection_error || !is_interaction && is_selection_error) {
							
							// include the neighbor's beliefs into the votes
							for (f = 0; f < FEATURES; f++) {
								votes[f][beliefs[nr][nc][f]]++;	
								voters++;
								agents_overlap_ave += agents_overlap;
							}	
							
							// Add nationality candidate
							if (neighbors_nationality_overlap >= max_neighbors_nationality_overlap) {
								//strictly bigger
								if (neighbors_nationality_overlap > max_neighbors_nationality_overlap) {
									// restart the counter
									nationality_candidatesN = 0;
									nationality_candidates[nationality_candidatesN++] = neighbors_nationality;
									max_neighbors_nationality_overlap = neighbors_nationality_overlap;
								} 
								// exactly the same
								else {
									
									is_different_culture = true;
									
									// make sure the nationality is already included, reverse for efficiency
									for(nation = nationality_candidatesN - 1; nation >= 0; nation--) {
										if (nationality_candidates[nation] == neighbors_nationality ){
											is_different_culture = false;
										}
									}
									
									try {
									// if the nationality is different from the current ones, then add it
									if (is_different_culture) {										
										nationality_candidates[nationality_candidatesN++] = neighbors_nationality;
									}
									}catch (Exception e) {
										System.out.println("the nationality candidates is " + nationality_candidates.length);
										System.out.println("trying to access nationality_candidatesN - 1 " + nationality_candidatesN);
									}
										
								}
								
							} // End of add nationality candidate							
							
						}  // End of interaction
						
					} // End of voting
					
					agents_overlap_ave /= voters;
					
					// Get the candidates features
					feature_candidatesN = 0;
					for (f = 0; f < FEATURES; f++) {
						current_trait = beliefs[r][c][f];
						current_trait_votes = votes[f][current_trait];
						for (t = 0; t < TRAITS; t++) {
							if (t != current_trait && votes[f][t] >= current_trait_votes){
								feature_candidates[feature_candidatesN++] = f;
								t = TRAITS;
							}
						}					
					} // End of getting the possible feature
					
					// Select the candidate
					if (feature_candidatesN > 0){
						selected_feature = feature_candidates[rand.nextInt(feature_candidatesN)];
						max_trait = beliefs[r][c][selected_feature];
						current_votes = votes[selected_feature][max_trait];
						max_votes = current_votes;
						trait_candidatesN = 0;
						
						// get the candidate traits
						for (t = 0; t < TRAITS; t++) {
							v = votes[selected_feature][t];
							if (max_votes == v) {
								trait_candidates[trait_candidatesN++] = t;							
							} else if (max_votes < v) {
								trait_candidates[0] = t;
								trait_candidatesN = 1;
								max_votes = v;
							}						
						}						
						
						// select the trait
						selected_trait = trait_candidates[rand.nextInt(trait_candidatesN)];
						nationality_trait = institution_beliefs[nationality][selected_feature];
						

						
						
						// if there is no cultural shock (current trait is different to its nationality's), 
						// accept the change
						if (beliefs[r][c][selected_feature] != nationality_trait || 
							// if the agent's current trait is equal to its nationality's (cultural shock),
							// then the agent will impose resistance to change depending how identified it is 
							// with its nationality	(cultural overlap)	
							beliefs[r][c][selected_feature] == nationality_trait &&
							// Cultural resilience: resistance to change based on cultural 
							// similarity or agent similarity
							(rand.nextFloat() > nationality_overlap / 
									// Math.max because it might have been a selection error
									(float) (Math.max(1, agents_overlap_ave) +  
											nationality_overlap))) {
							
							beliefs[r][c][selected_feature] = selected_trait;
							
							// select the nationality (it could be the same), and we already know that the overlap is bigger
							neighbors_nationality = nationality_candidates[rand.nextInt(nationality_candidatesN)];
							
							// If, after the interaction, the similarity with the neighbor's culture is bigger 
							// (or equal to consider the new assimilated trait) than the similarity with its 
							// own culture then the agent will change its culture to its neighbor's
							// according to a related probability
							if (rand.nextFloat() > (nationality_overlap) / 
									(float) (max_neighbors_nationality_overlap + nationality_overlap)){
						
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
							
							}
							
						}						
						
					}
					
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
