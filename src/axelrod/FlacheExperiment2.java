package axelrod;

import java.io.IOException;


public class FlacheExperiment2 extends Simulation {
	public FlacheExperiment2 (){
		TYPE = "FLACHE_EXPERIMENT2";
		RUN++;
		IDENTIFIER = RUN;		
	}

	public void run_experiment() {
		for (iteration = 0; iteration < ITERATIONS; iteration++) {
			for (int ic = 0; ic < CHECKPOINT; ic++) {
				for (int i = 0; i < TOTAL_AGENTS; i++) {
					int r = rand.nextInt(ROWS);
					int c = rand.nextInt(COLS);
	
					// clean the votes
					for (int f = 0; f < FEATURES; f++) {
						for (int t = 0; t < TRAITS; t++) {
							votes[f][t] = 0;
						}
					}
					
					// iterate over the neighbors to calculate the votes
					for (int n = 0; n < neighboursN[r][c]; n++){
						// selection error
						if (rand.nextFloat() < 1 - SELECTION_ERROR){
							int nr = neighboursX[r][c][n];
							int nc = neighboursY[r][c][n];
							for (int f = 0; f < FEATURES; f++) {
								votes[f][beliefs[nr][nc][f]]++;	
							}						
						}
					} 
					
					// get the candidates features
					int feature_candidatesN = 0;
					for (int f = 0; f < FEATURES; f++) {
						int current_trait = beliefs[r][c][f];
						int current_trait_votes = votes[f][current_trait];
						for (int t = 0; t < TRAITS; t++) {
							if (t != current_trait && votes[f][t] >= current_trait_votes){
								feature_candidates[feature_candidatesN++] = f;
								t = TRAITS;
							}
						}					
					}
					
					// select the candidate
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
						if (max_votes > current_votes){
							beliefs[r][c][selected_feature] = trait_candidates[rand.nextInt(trait_candidatesN)];
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
