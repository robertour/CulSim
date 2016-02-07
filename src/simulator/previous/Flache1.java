package simulator.previous;


public class Flache1 extends Axelrod {
	private static final long serialVersionUID = 8293331514566105316L;

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
				for (int f = 0; f < FEATURES; f++) {
					if (beliefs[r][c][f] != beliefs[nr][nc][f]) {
						mismatches[mismatchesN] = f;
						mismatchesN++;
					}
				}
				int agents_overlap = FEATURES - mismatchesN;
				
				// Check for selection error
				boolean is_selection_error = rand.nextFloat() >= 1 - SELECTION_ERROR;
				// Check for interaction
				boolean is_interaction = rand.nextFloat() >= 1 - ((float) agents_overlap / (float) FEATURES);

				// check if there is actual interaction 
				if (agents_overlap != FEATURES
						&& (is_interaction && !is_selection_error || !is_interaction && is_selection_error)) {
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
