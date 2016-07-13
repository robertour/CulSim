package simulator.worlds;

import simulator.control.Simulation;

/**
 * This class implements Axelrod (1997), but includes the perturbation elements
 * tested in the experiment 1 (The collapse of diversity in Axelrod model) of
 * Flache and Macy (2011): 1. Mutation: in each possible interaction, randomly
 * change one cultural trait of the individual 2. Selection error: in each
 * possible interaction, there is a possibility of a perception error, so that
 * an agent would reject to interact with a similar agent (homophily) or accept
 * to interact with a dissimilar one
 * 
 * 
 * @author Roberto Ulloa
 * @version 1.0, March 2016
 */
public class M1 extends Simulation {

	private static final long serialVersionUID = 8293331514566105316L;

	/**
	 * Register all the mismatches between two neighbors.
	 */
	protected int[] mismatches;

	/**
	 * Internal variable to keep the non death traits
	 */
	protected int non_death_features[];

	/**
	 * Variable that I use inside run_iteration, declared as fields for
	 * efficiency, to avoid re-declaration. This one in particular keeps the
	 * feature that is to be mutated.
	 */
	protected transient int mutant_feature;

	@Override
	public void setup() {
		mismatches = new int[FEATURES];
		non_death_features = new int[FEATURES];
	}

	@Override
	protected void reset() {
		super.reset();
		mismatches = null;
		non_death_features = null;
	}

	@Override
	public String getModelDescription() {
		return MODEL
				+ ": Homophily (Axelrod, 1997) including mutation and selection error - Experiment 1, Flache & Macy (2011)";
	}

	@Override
	public void run_iterations() {
		for (int ic = 0; ic < SPEED; ic++) {
			for (int i = 0; i < TOTAL_AGENTS; i++) {

				// select the agent
				int r = rand.nextInt(ROWS);
				int c = rand.nextInt(COLS);

				// select the neighbor that might influence the agent
				int n = rand.nextInt(neighboursN[r][c]);
				int nr = neighboursX[r][c][n];
				int nc = neighboursY[r][c][n];

				// get the number of mismatches between the two agents
				int mismatchesN = 0;
				int non_death_traitsN = 0;

				// differences consider death (or just born) agents after
				// decimation
				int differences = 0;
				for (int f = 0; f < FEATURES; f++) {
					if (traits[nr][nc][f] != DEAD_TRAIT) {
						non_death_features[non_death_traitsN] = f;
						non_death_traitsN++;
						if (traits[r][c][f] != traits[nr][nc][f]) {
							mismatches[mismatchesN] = f;
							mismatchesN++;
							if (traits[r][c][f] != DEAD_TRAIT) {
								differences++;
							}
						}
					} else {
						differences++;
					}
				}

				if (non_death_traitsN > 0) {

					int agents_overlap = FEATURES - differences;

					// Check for selection error
					boolean is_selection_error = rand.nextFloat() >= 1 - SELECTION_ERROR;
					// Check for interaction
					boolean is_interaction = rand.nextFloat() >= 1 - ((float) agents_overlap / (float) FEATURES);

					// check if there is actual interaction
					if (is_interaction && !is_selection_error || !is_interaction && is_selection_error) {
						int selected_feature = -99;
						if (mismatchesN > 0)
							selected_feature = mismatches[rand.nextInt(mismatchesN)];
						else
							selected_feature = mismatches[rand.nextInt(FEATURES)];
						traits[r][c][selected_feature] = traits[nr][nc][selected_feature];
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

			} // END of total agents

		} // END of checkpoint

	} // END of run_experiment
}
