package simulator.worlds;

/**
 * This class implements the experiment 3 (Social influence and homophily
 * combined) of Flache (2011), in which multilateral social influence is
 * introduced (i.e. the interactions between agents don't happen only between
 * two agents but many of them) together with Homophily, Axelrod (1997).
 * 
 * @author Roberto Ulloa
 * @version 1.0, March 2016
 */
public class E3 extends E2 {

	private static final long serialVersionUID = -4468160398655999146L;
	
	@Override
	public String getModelDescription(){
		return TYPE + ": Multilateral social influence with homophily - Experiment 3, Flache(2011)";
	}

	@Override
	public void run_iterations() {
		for (int ic = 0; ic < SPEED; ic++) {
			for (int i = 0; i < TOTAL_AGENTS; i++) {

				// row and column of the participating agent
				int r = rand.nextInt(ROWS);
				int c = rand.nextInt(COLS);

				// clean the votes
				for (int f = 0; f < FEATURES; f++) {
					for (int t = 0; t < TRAITS; t++) {
						votes[f][t] = 0;
					}
				}

				// iterate over the neighbors to calculate the votes
				for (int n = 0; n < neighboursN[r][c]; n++) {

					// row and column of the neighbor
					int nr = neighboursX[r][c][n];
					int nc = neighboursY[r][c][n];

					// get the number of identical traits
					int matches = 0;
					for (int f = 0; f < FEATURES; f++) {
						if (traits[nr][nc][f] != DEAD_TRAIT
								&& (traits[r][c][f] == DEAD_TRAIT || traits[r][c][f] == traits[nr][nc][f])) {
							matches++;
						}
					}

					// selection error
					boolean is_selection_error = rand.nextFloat() > 1 - SELECTION_ERROR;

					// check homophily
					if (rand.nextFloat() < matches / (float) FEATURES) {

						// if there isn't selection error,
						// then don't include the neighbor
						if (!is_selection_error) {

							// include the neighbor's traits
							for (int f = 0; f < FEATURES; f++) {
								if (traits[nr][nc][f] != DEAD_TRAIT) {
									votes[f][traits[nr][nc][f]]++;
								}
							}
						}
					}

					// if it was not selected but there was a selection error,
					// then include the neighbor
					else if (is_selection_error) {

						// include the neighbor's traits
						for (int f = 0; f < FEATURES; f++) {
							if (traits[nr][nc][f] != DEAD_TRAIT) {
								votes[f][traits[nr][nc][f]]++;
							}
						}
					}
				}

				// get the candidates features
				int feature_candidatesN = 0;
				for (int f = 0; f < FEATURES; f++) {
					int current_trait = traits[r][c][f];
					int current_trait_votes = 0;
					if (current_trait != DEAD_TRAIT) {
						current_trait_votes = votes[f][current_trait];
					}
					for (int t = 0; t < TRAITS; t++) {
						if (t != current_trait && votes[f][t] >= current_trait_votes) {
							feature_candidates[feature_candidatesN++] = f;
							t = TRAITS;
						}
					}
				}

				// select the candidate
				if (feature_candidatesN > 0) {
					int selected_feature = feature_candidates[rand.nextInt(feature_candidatesN)];
					int max_trait = traits[r][c][selected_feature];
					int current_votes = 0;
					if (max_trait != DEAD_TRAIT) {
						current_votes = votes[selected_feature][max_trait];
					}
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
					if (max_votes > current_votes) {
						traits[r][c][selected_feature] = trait_candidates[rand.nextInt(trait_candidatesN)];
					}
				}

				// mutation
				if (rand.nextFloat() >= 1 - MUTATION) {
					mutant_feature = rand.nextInt(FEATURES);
					// Don't change dead features
					if (mutant_feature != DEAD_TRAIT) {
						traits[r][c][mutant_feature] = rand.nextInt(TRAITS);
					}
				}
			}
		} // END of checkpoint
	} // END of run_experiment

}
