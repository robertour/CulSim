package axelrod;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.Callable;

public abstract class Simulation  implements Callable<String>  {

	public String TYPE = null;
	public int BUFFERED_SIZE = 512;
	public static int RUN = 0;
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
	public int FEATURES = 5;
	public int TRAITS = 15;
	
	// Neighborhood
	public int RADIUS = 6;
	private int NEIGHBOURS = RADIUS * RADIUS + ( RADIUS + 1 ) * ( RADIUS + 1 ) - 1;
	
	// Noise
	public float MUTATION = 0.0001f;
	public float SELECTION_ERROR = 0.0001f;
	
	
	protected int [][][] beliefs = null;
	
	
	
	protected int [][][] neighboursX = null;
	protected int [][][] neighboursY = null;
	protected int [][] neighboursN = null;
	
	
	protected int [][] votes;
	protected int [] mismatches;
	protected int [] feature_candidates;
	protected int [] trait_candidates;
	
	
	public int ITERATIONS = 10;
	public int CHECKPOINT = 1000;
	
	
	protected Random rand = new Random();
	protected int iteration = 0;
	private boolean flag_mark = true;
	private boolean [][] flags;
	private int [][] cultures;
	private int biggest_cluster = 0;
	private int cluster_size = 0;
	private int cultureN;
	
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
	 * Buffer to write the output
	 */
	protected BufferedWriter writer = null;

	/**
	 * Return a csv header for the output
	 * @return
	 */
	public static String header() {
		return "id,iterations,checkpoint,type,rows,cols,features,traits,radius,mutation,selection_error,iteration,cultures,cultures_norm,biggest_cluster,biggest_norm\n";		
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
	public String call() {
		setup();
		try {
			writer.write(results());				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    run_experiment();
		
	    String r = results();
	
	    CulturalSimulator.TA_OUTPUT.append("Finished: " + IDENTIFIER + "_" + TYPE + "_" + ROWS + "x" + COLS + ": " + r + "\n");
	    finish();
	
		return r;			
	}
	
	public abstract void run_experiment();

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
			clone.MUTATION = this.MUTATION;
			clone.SELECTION_ERROR = this.SELECTION_ERROR;
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clone;	
	}

	public Simulation() {
		super();
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