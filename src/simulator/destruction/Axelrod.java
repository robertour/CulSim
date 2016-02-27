package simulator.destruction;

import simulator.control.Simulation;

public class Axelrod extends Simulation {

	private static final long serialVersionUID = 8293331514566105316L;

	/**
	 * Register all the mismatches between two neighbors.
	 */
	protected int [] mismatches;
	
	
	
	/**
	 * Internal variable to keep the non death traits
	 */
	protected int non_death_features[];
	

	/**
	 * 
	 */
	protected transient int mutant_feature;
	
	@Override
	public void setup() {
		mismatches = new int [TRAITS];		
		non_death_features = new int [FEATURES];
	}
	
	@Override
	protected void reset() {
		super.reset();
		mismatches = null;
		non_death_features = null;
	}
	
	@Override
	public void run_iteration() {
		for (int ic = 0; ic < CHECKPOINT; ic++) {
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
				
				// differences consider death (or just born) agents after genocide				
				int differences = 0;
				for (int f = 0; f < FEATURES; f++) {
					if (beliefs[nr][nc][f] != DEAD_TRAIT){
						non_death_features[non_death_traitsN] = f;
						non_death_traitsN++;
						if (beliefs[r][c][f] != beliefs[nr][nc][f]) {							
							mismatches[mismatchesN] = f;
							mismatchesN++;
							if (beliefs[r][c][f] != DEAD_TRAIT){
								differences++;
							}
						}
					} else {
						differences++;
					}
				}
				
				if (non_death_traitsN > 0){
									
					int agents_overlap = FEATURES - differences;
					
					// Check for selection error
					boolean is_selection_error = rand.nextFloat() >= 1 - SELECTION_ERROR;
					// Check for interaction
					boolean is_interaction = rand.nextFloat() >= 1 - ((float) agents_overlap / (float) FEATURES);
	
					// check if there is actual interaction 
					if (agents_overlap != FEATURES
							&& (is_interaction && !is_selection_error 
									|| !is_interaction && is_selection_error)) {
						int selected_feature = mismatches[rand.nextInt(mismatchesN)];
						beliefs[r][c][selected_feature] = beliefs[nr][nc][selected_feature];
					}
	
					// mutation
					if ( rand.nextFloat() >= 1 - MUTATION ) {
						mutant_feature = rand.nextInt(FEATURES);
						// Don't change dead features
						if (mutant_feature != DEAD_TRAIT){
							beliefs[r][c][mutant_feature] = rand.nextInt(TRAITS);
						}
					}
				}
				
			} // END of total agents
			
		} // END of checkpoint
		
	} // END of run_experiment	
}
