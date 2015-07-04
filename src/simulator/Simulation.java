package simulator;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.Callable;

public abstract class Simulation  implements Callable<String>  {

	/**
	 * String text to identify the type of simulation
	 */
	public String TYPE = null;
	/**
	 * Buffered size to avoid writing each time.
	 */
	public int BUFFERED_SIZE = 512;
	
	
	// Identify the simulation
	/**
	 * Identify the current simulation object
	 */
	protected int IDENTIFIER = 0;
	
	
	// Size of the world
	/**
	 * ROWS of the world
	 */
	public int ROWS = 32;
	/**
	 * COLS of the World
	 */
	public int COLS = 32;
	/**
	 * Total agents in the world
	 */
	protected int TOTAL_AGENTS = ROWS * COLS;
	
	
	// Cultural Space
	/**
	 * Number of FEATURES of the cultural space
	 */
	public int FEATURES = 5;
	/**
	 * Number of FEATURES of the cultural space
	 */
	public int TRAITS = 15;
	
	/**
	 * alpha value for cultural resilience
	 */
	public float ALPHA = 0.5f;
	protected float BETA = 0.5f;

	/**
	 * alpha value for cultural mobility
	 */
	public float ALPHA_PRIME = 0.5f;
	protected float BETA_PRIME = 0.5f;
	
	
	// Neighborhood
	/**
	 * Define the RADIOUS of the cultural space
	 */
	public int RADIUS = 6;
	/**
	 * Number of NEIGHOURS Of the cultural space
	 */
	protected int NEIGHBOURS = RADIUS * RADIUS + ( RADIUS + 1 ) * ( RADIUS + 1 ) - 1;
	/**
	 * Define X coordinates of the neighbors
	 */
	protected int [][][] neighboursX = null;
	/**
	 * Define Y coordinates of the neighbors
	 */
	protected int [][][] neighboursY = null;
	/**
	 * Define the total number of neighbors. It is different per agent because of
	 * the non-toroidal configuration.
	 */
	protected int [][] neighboursN = null;
	
	
	// Noise
	/**
	 * Define the MUTATION error. This is when the agent changes one trait randomly
	 */
	public float MUTATION = 0.0001f;
	/**
	 * Define SELECTION_ERROR. This is when the agent randomly changes its decision to 
	 * interact or not with an specific agent 
	 */
	public float SELECTION_ERROR = 0.0001f;
	
	/**
	 * Individual belief space
	 */
	protected int [][][] beliefs = null;
	
	
	// Main loop control
	/**
	 * Number of ITERATIONS of the checkpoints
	 */
	public int ITERATIONS = 10;
	/**
	 * Save results and check thread status each 1000 iterations
	 */
	public int CHECKPOINT = 1000;
	
	// Internal control
	/**
	 * Random number generator
	 */
	protected Random rand = new Random();
	
	// Recursion control
	/**
	 * Internal iteration of the simulation counter
	 */
	protected int iteration = 0;
	/**
	 * Flag for recursion
	 */
	private boolean flag_mark = true;
	/**
	 * Controls recursion
	 */
	private boolean [][] flags;
	/**
	 * Internal recursion matrix to indicate culture
	 */
	private int [][] cultures;
	/**
	 * Size of the current cluster
	 */
	private int cluster_size = 0;

	// Main simulation outputs
	/**
	 * Number of members of the biggest cluster
	 */
	protected int biggest_cluster = 0;
	/**
	 * Number of clusters
	 */
	protected int cultureN;

	
	/**
	 * Indicates if the thread should be running. If not, it would stop or suspend as 
	 * soon as it can if not. This is when a checkpoint is finalized.
	 */
	protected volatile boolean playing = true;
	/**
	 * Indicates if the thread was suspended. It would suspend as soon as it can.
	 * This is when a checkpoint is finalized.
	 */
	protected volatile boolean suspended = false;
	/**
	 * Indicates if the thread was cancelled. It would be cancelled as soon as it can.
	 * This is when a checkpoint is finalized.
	 */
	protected volatile boolean cancelled = false;
	/**
	 * This object is necessary in order to suspend the thread
	 */
	protected Object o = new Object();
	
	/**
	 * Indicates if the simulation ended completely, without any interruptions.
	 */
	public boolean is_finished = false;
	
	
	/**
	 * Activate this when something went wrong in the simulation
	 */
	public boolean failed = false;
	
	/**
	 * Buffer to write the output
	 */
	protected BufferedWriter writer = null;
	
	/**
	 * Register the time when the experiment started
	 */
	protected long startTime;
	/**
	 * Register the time when the experiment finished
	 */
	protected long endTime = 0l;

	/**
	 * Return a csv header for the output
	 * @return
	 */
	public static String header() {
		return "id,timestamp,duration,iterations,checkpoint,type,rows,cols,features,traits,radius,alpha,alpha_prime,mutation,selection_error,iteration," +
				"cultures,cultures_norm,biggest_cluster,biggest_norm,culturesU,cultures_normU,biggest_clusterU,biggest_normU\n";		
	}

	/**
	 * Setups the object in order to run the experiment. Initialize all the variables
	 */
	private void simulation_setup() {
		TYPE = this.getClass().getSimpleName().toUpperCase();
		NEIGHBOURS = RADIUS * RADIUS + ( RADIUS + 1 ) * ( RADIUS + 1 ) - 1;
		TOTAL_AGENTS = ROWS * COLS;
		BETA = 1 - ALPHA;
		BETA_PRIME = 1 - ALPHA_PRIME;
				
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
	public String call() {
		
		
		try {
			simulation_setup();
		} catch (Exception e1) {
			e1.printStackTrace();
			CulturalSimulator.TA_OUTPUT.append("simulation_setup() failed");
			failed = true;
		}
		CulturalSimulator.TA_OUTPUT.append("(ID: " + IDENTIFIER +  "): " + "Simulation setup ready. \n");
		try {
			setup();
		} catch (Exception e1) {
			e1.printStackTrace();
			CulturalSimulator.TA_OUTPUT.append("simulation_setup() failed");
			failed = true;
		}
		CulturalSimulator.TA_OUTPUT.append("(ID: " + IDENTIFIER +  "): " + TYPE + " setup ready. \n");
		
		try {
			writer.write(results());				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CulturalSimulator.TA_OUTPUT.append("writer.write(results()); failed");
			failed = true;
		}
		
		CulturalSimulator.TA_OUTPUT.append("(ID: " + IDENTIFIER +  "): " + "Starting the experiment... \n");
		startTime = System.currentTimeMillis();
	    try {
			run_experiment();
		} catch (Exception e) {
			e.printStackTrace();
			CulturalSimulator.TA_OUTPUT.append("run_experiment(); failed");
			failed = true;
		}
	    endTime = System.currentTimeMillis();
		
	    String r = "";
	    try {
			r = results();
		} catch (Exception e) {
			e.printStackTrace();
			CulturalSimulator.TA_OUTPUT.append("r=results; failed");
			failed = true;
		}
	
	    CulturalSimulator.TA_OUTPUT.append("Finished: " + IDENTIFIER + "_" + TYPE + "_" + ROWS + "x" + COLS + ": " + r + "\n");
	    
	    try {
			finish();
		} catch (Exception e) {
			e.printStackTrace();
			CulturalSimulator.TA_OUTPUT.append("finish(); failed");
			failed = true;
		}
	    
	    try {
			reset();
		} catch (Exception e) {
			e.printStackTrace();
			CulturalSimulator.TA_OUTPUT.append("reset(); failed");
			failed = true;
		}
	    
		try {
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
			CulturalSimulator.TA_OUTPUT.append("system.gc(); failed");
			failed = true;
		}
	
		return r;			
	}
	
	protected abstract void setup();
	protected abstract void run_experiment();
	protected abstract void reset();

	/**
	 * Suspend this thread
	 */
	public void suspend() {          
	    playing = false;  
	    suspended = true;
	}
	
	/**
	 * Set a suspended state. Just wait until the thread is resumed.
	 */
	protected void set_suspended(){
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

	/**
	 * Cancel this thread
	 */
	public void cancel() {          
	    playing = false;  
	    cancelled = true;
	}

	/**
	 * Continue the execution of the thread
	 */
	public void resume() {       
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
	public Simulation clone() {
		Simulation clone = null;
		try {
			clone = this.getClass().newInstance();
			clone.ITERATIONS = this.ITERATIONS;
			clone.CHECKPOINT = this.CHECKPOINT; 
			clone.TYPE = this.TYPE;
			clone.ROWS = this.ROWS;
			clone.COLS = this.COLS;
			clone.FEATURES = this.FEATURES; 
			clone.TRAITS = this.TRAITS;
			clone.RADIUS = this.RADIUS;
			clone.ALPHA = this.ALPHA;
			clone.ALPHA_PRIME = this.ALPHA_PRIME;
			clone.MUTATION = this.MUTATION;
			clone.SELECTION_ERROR = this.SELECTION_ERROR;			
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clone;	
	}


	/**
	 * Count the cluster and returns a CSV line with the results
	 * @return a CSV line with the results
	 */
	protected String results() {
		count_clusters();
		return this.get_results();				
	}

	/**
	 * Create a CSV line for the results
	 * @return a CSV line with current results
	 */
	public String get_results() {
		return  IDENTIFIER + "," +
				new java.sql.Timestamp(startTime) + "," +
				((endTime == 0) ? (System.currentTimeMillis() - startTime) : (endTime - startTime)) + "," +
				ITERATIONS + "," +  
				CHECKPOINT + "," +  
				TYPE + "," +  
				ROWS + "," +
				COLS + "," +  
				FEATURES + "," +  
				TRAITS + "," +  
				RADIUS + "," +
				ALPHA + "," +  
				ALPHA_PRIME + "," +  
				MUTATION + "," +  
				SELECTION_ERROR + "," +
				iteration * CHECKPOINT+ "," +
				cultureN  + "," +
				(float) cultureN / TOTAL_AGENTS + "," +
				biggest_cluster + "," +
				(float) biggest_cluster / TOTAL_AGENTS + ",-1,-1,-1,-1\n";				
	}

	/**
	 * It is a sort of destructor to help the garbage collector
	 */
	protected void finish() {
		
		try {
			if (failed) {
				writer.write(results());
			} else {
				writer.write("FAILED:" + results());
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CulturalSimulator.TA_OUTPUT.append("Error inside finish()");
			failed = true;
		}
		
		beliefs = null;
		
		// Auxiliary variables that keeps the neighbors of each agent
		neighboursX = null;
		neighboursY = null;
		neighboursN = null;
		flags = null;
		cultures = null;
	}

	public void count_clusters() {
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

	public void expand(int r, int c) {
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

	public boolean is_same_culture(int [] c1, int [] c2) {
		boolean fellow = true;				
		for (int f = 0; f < FEATURES; f++) {
			if ( c1[f] != c2[f] ) {
				fellow = false;
				f = FEATURES;
			}
		}
		return fellow;
	}

	public void print_cultures() {
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