package axelrod;

import java.io.IOException;


public class Axelrod extends Simulation {
	public Axelrod (){
		TYPE = "AXELROD";
		RUN++;
		IDENTIFIER = RUN;		
	}

	public void run_experiment() {
		for (iteration = 0; iteration < ITERATIONS; iteration++) {
			for (int ic = 0; ic < CHECKPOINT; ic++) {
				for (int i = 0; i < TOTAL_AGENTS; i++) {
					int r = rand.nextInt(ROWS);
					int c = rand.nextInt(COLS);
	
					int n = rand.nextInt(neighboursN[r][c]);
					int nr = neighboursX[r][c][n];
					int nc = neighboursY[r][c][n];
	
					// get the mismatches
					int mismatchesN = 0;
					for (int f = 0; f < FEATURES; f++) {
						if (beliefs[r][c][f] != beliefs[nr][nc][f]) {
							mismatches[mismatchesN] = f;
							mismatchesN++;
						}
					}
					int overlap = FEATURES - mismatchesN;
	
					// check if there is actual interaction 
					if (overlap != FEATURES
							&& rand.nextFloat() > 1 - ((float) overlap / (float) FEATURES)) {
						int selected_feature = mismatches[rand.nextInt(mismatchesN)];
						beliefs[r][c][selected_feature] = beliefs[nr][nc][selected_feature];
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
