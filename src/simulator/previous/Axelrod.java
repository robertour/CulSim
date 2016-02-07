package simulator.previous;

import simulator.Simulation;

public class Axelrod extends Simulation {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7545052442932217544L;
	
	/**
	 * Register all the mismatches between two neighbors.
	 */
	protected int [] mismatches;
	
	public Axelrod (){
		super();
		TYPE = "AXELROD";	
	}

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
				for (int f = 0; f < FEATURES; f++) {
					if (beliefs[r][c][f] != beliefs[nr][nc][f]) {
						mismatches[mismatchesN] = f;
						mismatchesN++;
					}
				}
				int agents_overlap = FEATURES - mismatchesN;

				// check if there is actual interaction 
				if (agents_overlap != FEATURES
						&& rand.nextFloat() > 1 - ((float) agents_overlap / (float) FEATURES)) {
					int selected_feature = mismatches[rand.nextInt(mismatchesN)];
					beliefs[r][c][selected_feature] = beliefs[nr][nc][selected_feature];
				}
			}
		} // END of checkpoint
		
	} // END of run_experiment
	
}
