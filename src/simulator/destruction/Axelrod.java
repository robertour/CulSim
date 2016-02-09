package simulator.destruction;

import simulator.Simulation;

public class Axelrod extends Simulation {
	private static final long serialVersionUID = 8293331514566105316L;

	/**
	 * Register all the mismatches between two neighbors.
	 */
	protected int [] mismatches;
	

	@Override
	public void setup() {
		mismatches = new int [TRAITS];		
	}
	
	@Override
	protected void reset() {
		super.reset();
		mismatches = null;	
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
				
				// differences consider death (or just born) agents after genocide				
				int differences = 0;
				for (int f = 0; f < FEATURES; f++) {
					// if the neighbor has a DEAD_TRAIT don't take him into account
					if (beliefs[nr][nc][f] != DEAD_TRAIT){
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
					beliefs[r][c][rand.nextInt(FEATURES)] = rand.nextInt(TRAITS);
				}
				
			} // END of total agents
			
		} // END of checkpoint
		
	} // END of run_experiment	
}
