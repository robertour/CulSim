package axelrod;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.Callable;


public class Simulation implements Callable<String> {
	// Template experiment:  AXELROD,  FLACHE_EXPERIMENT1, FLACHE_EXPERIMENT2
	public String TYPE = "AXELROD";
	public int BUFFERED_SIZE = 512;
	public static int RUN = 0;
	private int IDENTIFIER = 0;
	
	// Main world boundaries
	public int ROWS = 32;
	public int COLS = 32;
	private int TOTAL_AGENTS = ROWS * COLS;
	
	// Culture space
	public int FEATURES = 5;
	public int TRAITS = 15;
	
	// Radius of the neighborhood
	public int RADIUS = 6;
	private int NEIGHBOURS = RADIUS * RADIUS + ( RADIUS + 1 ) * ( RADIUS + 1 ) - 1;
	
	// Noise variables
	public float MUTATION = 0.0001f;
	public float SELECTION_ERROR = 0.0001f;	
	
	// Main matrix. It keeps the internal beliefs of the agents
	private int [][][] beliefs = null;
	
	// Auxiliary variables that keeps the neighbors of each agent
	private int [][][] neighboursX = null;
	private int [][][] neighboursY = null;
	private int [][] neighboursN = null;
	
	
	// Internal variables declared just one
	private int [][] votes ;
	private int [] mismatches;
	private int [] feature_candidates;
	private int [] trait_candidates;	

	// Control variables
	public int ITERATIONS = 10;
	public int CHECKPOINT = 1000;
	private Random rand = new Random();
	private int iteration = 0;
	
	// Recursive variables to count clusters
	private boolean flag_mark = true;
	private boolean [][] flags;
	private int [][] cultures;
	private int biggest_cluster = 0;
	private int cluster_size = 0;
	private int cultureN;
	
	// Thread control variables
	private volatile boolean playing = true;  
	private volatile boolean suspended = true;  
	private Object o = new Object();
	public boolean is_finished = false;
	
	// I/O variables
	BufferedWriter writer = null;
	
	public Simulation (){
		RUN++;
		IDENTIFIER = RUN;		
	}

	/**
	 * Setups the object in order to run the experiment. Initialize all the variables
	 */
	public void setup() {
		NEIGHBOURS = RADIUS * RADIUS + ( RADIUS + 1 ) * ( RADIUS + 1 ) - 1;
		TOTAL_AGENTS = ROWS * COLS;
		
		votes = new int[FEATURES][TRAITS];
		feature_candidates = new int[FEATURES];
		trait_candidates = new int[TRAITS];
		mismatches = new int [TRAITS];
				
		beliefs = new int[ROWS][COLS][FEATURES];
		neighboursX = new int[ROWS][COLS][NEIGHBOURS];
		neighboursY = new int[ROWS][COLS][NEIGHBOURS];
		neighboursN = new int[ROWS][COLS];
		
		flags = new boolean[ROWS][COLS];
		cultures = new int[ROWS][COLS];
		
		int n = 0;
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				for (int f = 0; f < FEATURES; f++) {
					beliefs[r][c][f] = rand.nextInt(TRAITS);
				}
				n = 0;
				for (int i = 0; i <= RADIUS; i++) {
					for (int j = 0; j <= RADIUS; j++) {
						if ( j + i + 2 <= RADIUS ) {
							if ( r + i + 1 < ROWS && c + j + 1 < COLS ) {
								neighboursX[r][c][n] = r + i + 1;
								neighboursY[r][c][n] = c + j + 1;
								n++;
							}
							if ( r - i - 1 >= 0 && c - j - 1 >= 0 ) {
								neighboursX[r][c][n] = r - i - 1;
								neighboursY[r][c][n] = c - j - 1;								
								n++;
							}
						}
						if ( j + i <= RADIUS && ( j != 0 || i != 0 ) ) {
							if ( r - i >= 0 && c + j < COLS ) {
								neighboursX[r][c][n] = r - i;
								neighboursY[r][c][n] = c + j;
								n++;
							}
							if ( r + i < ROWS && c - j >= 0 ) {
								neighboursX[r][c][n] = r + i;
								neighboursY[r][c][n] = c - j;
								n++;
							}								
						}
					}
					neighboursN[r][c] = n;
				}
			}			
		}
		
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
			        new FileOutputStream(CulturalSimulator.RESULTS_DIR + "/iterations/" + IDENTIFIER + "_" + TYPE + "_" + ROWS + "x" + COLS + ".csv"), "utf-8"), BUFFERED_SIZE);
			writer.write(header());
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Starting point of execution
	 * @returns the last line of results
	 */
	public String call(){
		setup();
		try {
			writer.write(results());				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    switch (TYPE) {
	    	case "AXELROD":	axelrod();	break;
	        case "FLACHE_EXPERIMENT1":	flache_experiment1();	break;
	        case "FLACHE_EXPERIMENT2":	flache_experiment2();	break;
	        case "FLACHE_EXPERIMENT3":	flache_experiment3();	break;
	    }
	    String r = results();

	    CulturalSimulator.TA_OUTPUT.append("Finished: " + IDENTIFIER + "_" + TYPE + "_" + ROWS + "x" + COLS + ": " + r + "\n");
	    finish();
	
		return r;			
	}

	/**
	 * Suspend this thread
	 */
	public void suspend(){          
        playing = false;  
        suspended = true;
    }
	
	/**
	 * Cancel this thread
	 */
	public void cancel(){          
        playing = false;  
    }  


	/**
	 * Continue the execution of the thread
	 */
    public void resume(){       
        playing = true;
        suspended = false;
        synchronized (o) {  
            o.notifyAll();  
        }  
    } 

    /**
     * Clone this object
     * @return a clone of this object
     */
	public Simulation clone (){
		Simulation clone = new Simulation();
    	clone.ITERATIONS = this.ITERATIONS;
    	clone.CHECKPOINT = this.CHECKPOINT; 
    	clone.TYPE = this.TYPE;
    	clone.ROWS = this.ROWS;
    	clone.COLS = this.COLS;
    	clone.FEATURES = this.FEATURES; 
    	clone.TRAITS = this.TRAITS;
    	clone.RADIUS = this.RADIUS;
    	clone.MUTATION = this.MUTATION;
    	clone.SELECTION_ERROR = this.SELECTION_ERROR;
    	return clone;	
	}
	
	/**
	 * Return a csv header for the output
	 * @return
	 */
	public static String header(){
		return "id,iterations,checkpoint,type,rows,cols,features,traits,radius,mutation,selection_error,iteration,cultures,cultures_norm,biggest_cluster,biggest_norm\n";		
	}
	
	/**
	 * Count the cluster and returns a CSV line with the results
	 * @return a CSV line with the results
	 */
	private String results() {
		count_clusters();
		return this.get_results();				
	}
	
	/**
	 * Create a CSV line for the results
	 * @return a CSV line with current results
	 */
	public String get_results() {
		return  IDENTIFIER + "," +
				ITERATIONS + "," +  
				CHECKPOINT + "," +  
				TYPE + "," +  
				ROWS + "," +
				COLS + "," +  
				FEATURES + "," +  
				TRAITS + "," +  
				RADIUS + "," +  
				MUTATION + "," +  
				SELECTION_ERROR + "," +
				iteration * CHECKPOINT+ "," +
				cultureN  + "," +
				(float) cultureN / TOTAL_AGENTS + "," +
				biggest_cluster + "," +
				(float) biggest_cluster / TOTAL_AGENTS + "\n";				
	}
	
	
	
	/**
	 * It is a sort of destructor to help the garbage collector
	 */
	public void finish() {
		
		try {
			writer.write(results());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		beliefs = null;
		
		// Auxiliary variables that keeps the neighbors of each agent
		neighboursX = null;
		neighboursY = null;
		neighboursN = null;
		
		
		// Internal variables declared just one
		votes = null;
		mismatches = null;
		feature_candidates = null;
		trait_candidates = null;
				
		System.gc();
		
	}
	
	public void flache_experiment3() {
		int ic = 0;
		for (iteration = 0; iteration < ITERATIONS; iteration++) {
			for (ic = 0; ic < CHECKPOINT && playing; ic++) {
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
					for (int n = 0; n < neighboursN[r][c]; n++){
						
						// row and column of the neighbor
						int nr = neighboursX[r][c][n];
						int nc = neighboursY[r][c][n];
						
						// get the number of identical traits
						int matches = 0;
						for (int f = 0; f < FEATURES; f++) {
							if (beliefs[r][c][f] == beliefs[nr][nc][f]) {
								matches++;
							}
						}						

						//selection error
						boolean is_selection_error = rand.nextFloat() > 1 - SELECTION_ERROR;
						
						// check homophily
						if (rand.nextFloat() < matches / (float) FEATURES) {
							
							// if there isn't selection error, then don't include the neighbor
							if (!is_selection_error) { 
								
								// include the neighbor's beliefs
								for (int f = 0; f < FEATURES; f++) {
									votes[f][beliefs[nr][nc][f]]++;	
								}
							}
						} 
						
						// if it was not selected but there was a selection error, then include the neighbor
						else if (is_selection_error){ 
							
							// include the neighbor's beliefs
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
			
			// if finalize the loop correctly
			if (ic == CHECKPOINT) {
				try {
					writer.write(results());				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// moving the counter down so it is possible to suspend
				// and resume from the same point
				ic = 0;
			} else {
				iteration--;
			}
			
			
			if (!playing){
				// if the thread is suspended, then wait 
				if (suspended){
					 while(suspended){  
                         synchronized(o){  
                        	 try {                   
                                 while(suspended){  
                                     synchronized(o){  
                                         o.wait();  
                                     }                           
                                 }                       
                             }  
                             catch (InterruptedException e) {                    
                            	 CulturalSimulator.TA_OUTPUT.append("Error while trying to wait" + "\n");
                             }    
                         }                           
                     } 
				} 
				// if the thread is suspended or cancelled, stop the loop
				else { // if cancelled
					break; 
				}
			}
		} // END of iterations
		
		if (iteration == ITERATIONS){
			is_finished = true;
		}
	}
	
	
	public void flache_experiment2() {
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
			try {
				writer.write(results());				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} // END of iterations
	}
	
	public void flache_experiment1() {
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
					
					boolean is_selection_error = rand.nextFloat() >= 1 - SELECTION_ERROR;
					boolean is_interaction = rand.nextFloat() >= 1 - ((float) overlap / (float) FEATURES);
					
					// check if there is actual interaction 
					if (overlap != FEATURES
							&& (is_interaction && !is_selection_error || !is_interaction && is_selection_error)) {
						int selected_feature = mismatches[rand.nextInt(mismatchesN)];
						beliefs[r][c][selected_feature] = beliefs[nr][nc][selected_feature];
					}
					
					// mutation
					if ( rand.nextFloat() >= 1 - MUTATION ) {
						beliefs[r][c][rand.nextInt(FEATURES)] = rand.nextInt(TRAITS);
					}
				}
			} // END of checkpoint
			try {
				writer.write(results());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} // END of iterations
	}
	
	public void axelrod() {
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
			try {
				writer.write(results());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} // END of iterations
	}
	
	public void count_clusters(){
		biggest_cluster = 0;
		cultureN = 0;
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				if (flags[r][c] != flag_mark){
					cluster_size = 0;
					expand(r, c);					
					if (cluster_size > biggest_cluster) {
						biggest_cluster = cluster_size;						
					}
					cultureN++;
				}
			}
		}
		flag_mark = !flag_mark;
	}
	
	public void expand(int r, int c){
		flags[r][c] = flag_mark;
		cultures[r][c] = cultureN;
		cluster_size++;
		
		int nr = r - 1;
		int nc = c;
		if ( nr >= 0 && flags[nr][nc] != flag_mark && this.is_same_culture(beliefs[r][c], beliefs[nr][nc])){
			expand(nr, nc);					
		}
		nr = r + 1;
		if ( nr < ROWS && flags[nr][nc] != flag_mark && this.is_same_culture(beliefs[r][c], beliefs[nr][nc])){
			expand(nr, nc);					
		}
		nr = r;
		nc = c - 1;
		if ( nc >= 0 && flags[nr][nc] != flag_mark && this.is_same_culture(beliefs[r][c], beliefs[nr][nc])){
			expand(nr, nc);					
		}
		nc = c + 1;
		if ( nc < COLS && flags[nr][nc] != flag_mark && this.is_same_culture(beliefs[r][c], beliefs[nr][nc])){
			expand(nr, nc);					
		}
		
		
	}
	
	public boolean is_same_culture(int [] c1, int [] c2){
		boolean fellow = true;				
		for (int f = 0; f < FEATURES; f++) {
			if ( c1[f] != c2[f] ) {
				fellow = false;
				f = FEATURES;
			}
		}
		return fellow;
	}
	
	public void print_cultures(){
		String s = "";
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				s += cultures[r][c] * 1000 + " ";
			}
			s += "\n";
		}
		CulturalSimulator.TA_OUTPUT.append(s);

	}
	
	

}
