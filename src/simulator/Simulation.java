package simulator;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.Callable;


public abstract class Simulation  implements Callable<String>, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8793729684553266527L;
	
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
	public int FEATURES = 6;
	/**
	 * Number of FEATURES of the cultural space
	 */
	public int TRAITS = 14;
	
	/**
	 * alpha value for cultural resilience
	 */
	public float ALPHA = 0.85f;
	protected float BETA = 0.5f;

	/**
	 * alpha value for cultural mobility
	 */
	public float ALPHA_PRIME = 0.5f;
	protected float BETA_PRIME = 0.5f;
	
	/**
	 * Frequency of regulatory process (democracy or propaganda)
	 */
	protected int FREQ_DEM = 10;
	
	/**
	 * Frequency of regulatory process 2 (propaganda)
	 */
	protected int FREQ_PROP = 3;
	
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
	public int ITERATIONS = 1000;
	/**
	 * Save results and check thread status each 1000 iterations
	 */
	public int CHECKPOINT = 100;
	
	// Internal control
	/**
	 * Random number generator
	 */
	protected Random rand = new Random();
	
	
	/**
	 * This indicates how many times the generation have been restart
	 */
	protected int epoch = 0;
	/**
	 * Internal iteration of the simulation counter
	 */
	protected int iteration = 0;
	
	/**
	 * Internal iteration of the simulation counter
	 */
	protected int generations = 0;
	
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
	 * Number of alife institutions
	 */
	protected int alife_institutions = -1;
	/**
	 * Members of the biggest institution
	 */
	protected int biggest_institution = -1;
	/**
	 * Number of members of the biggest borderless cluster
	 */
	protected int biggest_borderless_cluster = 0;
	/**
	 * Number of borderless clusters
	 */
	protected int culture_borderlessN;
	/**
	 * Energy of the System
	 */
	protected int energy = 0;
	/**
	 * Spread of the foreigners traits
	 */
	protected int foreiners_traits = 0;
	/**
	 * Similarity with the initial state
	 */
	protected int similarity;
	

	
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
	 * Indicates if there are any catastrophic events scheduled
	 */
	protected volatile boolean invasion = false;
	protected volatile int inv_radius = -1;
	protected volatile boolean destroy_institutions_structure = false;
	protected volatile boolean destroy_institutions_content = false;
	protected volatile boolean genocide = false;
	protected volatile double gen_prob = -1;
	protected volatile boolean institutional_conversion = false;
	protected volatile double ic_prob = -1;
	protected volatile boolean institutional_trait_conversion = false;
	protected volatile double itc_prob = -1;
	protected volatile boolean set_parameters = false;
	
	/**
	 * This object is necessary in order to suspend the thread
	 */
	protected transient Object o = null;
	
	/**
	 * Indicates if the simulation ended completely, without any interruptions.
	 */
	public boolean is_finished = false;
	
	
	/**
	 * Activate this when something went wrong in the simulation
	 */
	public boolean failed = false;
	

	
	/**
	 * Register the time when the experiment started
	 */
	protected long startTime;
	/**
	 * Register the time when the experiment finished
	 */
	protected long endTime = 0l;
	
	/**
	 * # that represent a TRAIT of a death agent
	 */
	protected static int DEAD_TRAIT = -2;
	
	/**
	 * This is the starting state of the simulation to
	 * calculate similarity/change of worlds.
	 */
	protected Simulation starter = null;


	/**
	 * The constructor just loads the class TYPE. The rest of the
	 * things are decided in the setup() which is safer.
	 */
	public Simulation(){
		TYPE = this.getClass().getSimpleName().toUpperCase();
		save_state();
	}
	
	/**
	 * Performs a deep cloning of the simulation taking advantage of 
	 * Java serialization
	 * @return
	 */
	protected void save_state() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			starter = (Simulation) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Starting point of execution
	 * @returns the last line of results
	 */
	public String call() {
		o = new Object();
		playing = true;
		suspended = false;
		cancelled = false;
		is_finished = false;
		failed = false;
		BufferedWriter writer = null;
		
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
			        new FileOutputStream(Controller.RESULTS_DIR + "/iterations/" + IDENTIFIER + "_" + TYPE + "_" + ROWS + "x" + COLS + ".csv"), "utf-8"), BUFFERED_SIZE);
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if (generations == 0){
			try {
				simulation_setup();
			} catch (Exception e1) {
				e1.printStackTrace();
				Controller.TA_OUTPUT.print("simulation_setup() failed");
				failed = true;
			}
			Controller.TA_OUTPUT.print("(ID: " + IDENTIFIER +  "): " + "Simulation setup ready. \n");
			try {
				setup();
			} catch (Exception e1) {
				e1.printStackTrace();
				Controller.TA_OUTPUT.print("simulation_setup() failed");
				failed = true;
			}
			Controller.TA_OUTPUT.print("(ID: " + IDENTIFIER +  "): " + TYPE + " setup ready. \n");
			save_state();
			try {
				writer.write(results());				
			} catch (IOException e) {
				e.printStackTrace();
				Controller.TA_OUTPUT.print("writer.write(results()); failed");
				failed = true;
			}
		} else {
			epoch++;
			save_state();
		}
		
		
		String r = "";
		Controller.TA_OUTPUT.print("(ID: " + IDENTIFIER +  "): " + "Starting the experiment... \n");
		startTime = System.currentTimeMillis();
	    try {
	    	if (Controller.IS_BATCH){
	    		run_experiment_batch(writer);
	    	}else {
	    		r = run_experiment_single(writer);
	    	}
		} catch (Exception e) {
			e.printStackTrace();
			Controller.TA_OUTPUT.print("run_experiment(); failed");
			failed = true;
		}
	    endTime = System.currentTimeMillis();
	    
		try {
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			Controller.TA_OUTPUT.print("system.gc(); failed");
			failed = true;
		}
		
	    if (is_finished){
		    Controller.TA_OUTPUT.print("Finished: " + get_identification() + "\n");
	    } else if (failed){
	    	Controller.TA_OUTPUT.print("Failed: " + get_identification() + "\n");
		} else {
	    	Controller.TA_OUTPUT.print("Stopped: " + get_identification() + "\n");
	    }
	    
	    /** 
	     * Always make sure to save and clear memory in batch mode
	     */
	    if (Controller.IS_BATCH) {
	    	/**
	    	 * TODO Safe the final state of each simulation
	    	 */
	    	clean();
	    }

		return r;			
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
		
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
			        new FileOutputStream(Controller.RESULTS_DIR + "/iterations/" + IDENTIFIER + "_" + TYPE + "_" + ROWS + "x" + COLS + ".csv"), "utf-8"), BUFFERED_SIZE);
			writer.write(header());
			writer.flush();
			writer.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	protected abstract void setup();

	/**
	 * It is a sort of destructor to help the garbage collector
	 */
	protected void reset(){
		beliefs = null;
		neighboursX = null;
		neighboursY = null;
		neighboursN = null;
		flags = null;
		cultures = null;		
	}

	private void run_experiment_batch(BufferedWriter writer){
		Controller.TA_OUTPUT.print("(ID: " + IDENTIFIER +  "): " + "Batch Mode (Multi-thread) \n");
		for (iteration = 0; iteration < ITERATIONS; iteration++) {
			//Controller.TA_OUTPUT.print("(ID: " + IDENTIFIER +  "): " + iteration + "\n");
			run_iteration();
			
			// write results of the current checkpoint
			try {
				writer.write(results());				
			} catch (IOException e) {
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
	}
	
	private String run_experiment_single(BufferedWriter writer){
		Controller.TA_OUTPUT.print("(ID: " + IDENTIFIER +  "): " + "Executed in single mode (no multi-thread). \n");
		String r = "";
		for (iteration = 0; iteration < ITERATIONS; iteration++) {
			
			check_for_events();
			update_gui();
			run_iteration();
			generations += iteration * CHECKPOINT;
					
			r = results();
			// write results of the current checkpoint
			try {
				writer.write(r);				
			} catch (IOException e) {
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
		
		return r;
	}
		
	
	protected abstract void run_iteration();
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
	            	 Controller.TA_OUTPUT.print("Error while trying to wait" + "\n");
	             }    
	         }                           
	     } 
	}

	/**
	 * Suspend this thread
	 */
	public void suspend() {          
	    playing = false;  
	    suspended = true;
	}

	/**
	 * Cancel this thread
	 */
	public void cancel() {          
	    playing = false;  
	    cancelled = true;
	}

	/** 
	 * clean memory structures
	 */
	public void clean(){
	    try {
			reset();
		} catch (Exception e) {
			e.printStackTrace();
			Controller.TA_OUTPUT.print("reset(); failed");
			failed = true;
		}
		try {
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
			Controller.TA_OUTPUT.print("system.gc(); failed");
			failed = true;
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
			clone.FREQ_DEM = this.FREQ_DEM;
			clone.FREQ_PROP = this.FREQ_PROP;
			clone.MUTATION = this.MUTATION;
			clone.SELECTION_ERROR = this.SELECTION_ERROR;			
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return clone;	
	}

	/**
	 * Return a csv header for the output
	 * @return
	 */
	public static String header() {
		return "id,timestamp,duration,iterations,checkpoint,"
				+ "type,rows,cols,features,traits,radius,"
				+ "alpha,alpha_prime,freq_proc,freq_proc2,mutation,selection_error,"
				+ "iteration,generations,"
				+ "cultures,biggest_cluster,institutions,biggest_institution,"
				+ "borderless_cultures, biggest_borderless_culture, energy, foreign_dispersion\n";		
	}
	
	public String get_identification(){
		return TYPE + " " + 
				ROWS + "x" + COLS + "(" + RADIUS + "): " +
				"F/T:" + FEATURES + "/" + TRAITS + " | " +
				"M/S:" + MUTATION + "/" + SELECTION_ERROR + " | " +
				"a/a\':" + ALPHA + "/" + ALPHA_PRIME + " | " +
				"D/P:" + FREQ_DEM + "/" + FREQ_PROP + 
				" @ " + generations * CHECKPOINT + " (" +
				"Cultures: " + cultureN + "/" + biggest_cluster + " | " +
				"Borderless: " + culture_borderlessN + "/" + biggest_borderless_cluster + " | " +
				"Institution: " + alife_institutions + "/" + biggest_institution + " | " +
				"E/Dispersion: " + energy + "/" + foreiners_traits  + " | " + 
				"Similarity: " + similarity + "/-" + ")";	
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
				FREQ_DEM + "," +  
				FREQ_PROP + "," +  
				MUTATION + "," +  
				SELECTION_ERROR + "," +
				iteration * CHECKPOINT + "," +
				generations + "," +
				cultureN  + "," +
				biggest_cluster + "," +
				"-1," +
				"-1," +
				culture_borderlessN + "," +
				biggest_borderless_cluster + "," +
				energy + "," +
				foreiners_traits + ",";			
	}

	/**
	 * Count the cluster and returns a CSV line with the results
	 * @return a CSV line with the results
	 */
	protected String results() {
		count_clusters();
		count_borderless_clusters();
		energy_and_foreigners_traits();
		calculate_similarity();
		return this.get_results();				
	}
	
	
	private void calculate_similarity(){
		similarity = 0;
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				for (int f = 0; f < FEATURES; f++) {
					if (beliefs[r][c][f] == starter.beliefs[r][c][f]){
						similarity++;						
					}
				}
			}
		}
	}

	private void count_clusters() {
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

	private void expand(int r, int c) {
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

	private void count_borderless_clusters() {
		biggest_borderless_cluster = 0;
		culture_borderlessN = 0;
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				if (flags[r][c] != flag_mark){
					cluster_size = 0;
					expand_borderless(r, c);					
					if (cluster_size > biggest_borderless_cluster) {
						biggest_borderless_cluster = cluster_size;						
					}
					culture_borderlessN++;
				}
			}
		}
		flag_mark = !flag_mark;
	}

	private void expand_borderless(int r, int c) {
		flags[r][c] = flag_mark;
		cultures[r][c] = culture_borderlessN;
		cluster_size++;
		
		int nr = 0;
		int nc = 0;
		
		for (int n = 0; n < neighboursN[r][c]; n++) {
			nr = neighboursX[r][c][n];
			nc = neighboursY[r][c][n];
			if ( flags[nr][nc] != flag_mark 
					&& this.is_same_culture(beliefs[r][c], beliefs[nr][nc])){
				expand_borderless(nr, nc);
			}
		}
		
	}

	private boolean is_same_culture(int [] c1, int [] c2) {
		boolean fellow = true;				
		for (int f = 0; f < FEATURES; f++) {
			if ( c1[f] != c2[f] ) {
				fellow = false;
				f = FEATURES;
			}
		}
		return fellow;
	}

	private void energy_and_foreigners_traits() {
		energy = 0;
		foreiners_traits = 0;
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				for (int f = 0; f < FEATURES; f++) {
					if (beliefs[r][c][f] == TRAITS){
						foreiners_traits++;
					} 
					if (c+1 < COLS && (beliefs[r][c][f] != beliefs[r][c+1][f])){
						energy++;
					}
					if (r+1 < COLS && (beliefs[r+1][c][f] != beliefs[r+1][c][f])){
						energy++;
					}
				}
			}
		}
	}

	public void check_for_events(){
		if (invasion){
			invasion(inv_radius);
			invasion = false;
		}
		if (genocide){
			genocide(gen_prob);
			genocide = false;
		}
		if (destroy_institutions_content){
			destroy_institutions_content();
			destroy_institutions_content = false;
		}
		if (destroy_institutions_structure){
			destroy_institutions_structure();
			destroy_institutions_structure = false;
		}
		if (institutional_conversion) {
			institutional_conversion(ic_prob);
			institutional_conversion = false;
		}
		if (institutional_trait_conversion) {
			institutional_trait_conversion(itc_prob);
			institutional_trait_conversion = false;
		}
		if (set_parameters){
			set_parameters();
			set_parameters = false;
		}
	}

	public void setDestroy_institutions_structure() {
		if (playing){
			this.destroy_institutions_structure = true;
		} else {
			destroy_institutions_structure();
			update_gui();
		}
	}

	public void setDestroy_institutions_content() {
		if (playing){
			this.destroy_institutions_content = true;
		} else {
			destroy_institutions_content();
			update_gui();
		}
	}

	public void setInvasion(int radius) {
		if (playing){
			this.invasion = true;
			inv_radius = radius;
		} else {
			invasion(radius);
			update_gui();
		}
	}

	public void setInstitutional_conversion(double prob) {
		if (playing){
			this.institutional_conversion = true;
			this.ic_prob = prob;
		} else {
			institutional_conversion(prob);
			update_gui();
		}
	}

	public void setInstitutional_trait_conversion(double prob) {
		if (playing){
			this.institutional_trait_conversion = true;
			this.itc_prob = prob;
		} else {
			institutional_trait_conversion(prob);
			update_gui();
		}
	}

	public void setGenocide(double prob) {
		if (playing){
			this.genocide = true;
			this.gen_prob = prob;
		} else {
			genocide(prob);
			update_gui();
		}
	}
	
	public void setParameters() {
		if (playing){
			this.set_parameters = true;
		} else {
			set_parameters();
		}
	}

	protected void destroy_institutions_structure(){}
	protected void destroy_institutions_content(){}
	
	/**
	 * This method modifies the parameters of the simulation during execution.
	 */
	protected void set_parameters(){
		ALPHA = (float) CulturalSimulator.sp_influence.getValue();
		ALPHA_PRIME = (float) CulturalSimulator.sp_loyalty.getValue();
		MUTATION = (float) CulturalSimulator.sp_mutation.getValue();
		SELECTION_ERROR = (float) CulturalSimulator.sp_selection_error.getValue();
		FREQ_DEM = (int) CulturalSimulator.sp_democracy.getValue();
		FREQ_PROP = (int) CulturalSimulator.sp_propaganda.getValue();
	}
	
	/**
	 * An invasion will introduce a foreign group with neighborhood 
	 * of radius
	 * @param radius
	 */
	protected void invasion (int radius){
		int r = ROWS/2;
		int c = COLS/2;
		
		for (int f=0; f < FEATURES; f++){
			beliefs[r][c][f] = TRAITS;
		}
		
		for (int i = 0; i <= radius; i++) {
			for (int j = 0; j <= radius; j++) {
				if ( j + i + 2 <= radius ) {
					if ( r + i + 1 < ROWS && c + j + 1 < COLS ) {
						for (int f=0; f < FEATURES; f++){
							beliefs[r + i + 1][c + j + 1][f]=TRAITS;
						}
					}
					if ( r - i - 1 >= 0 && c - j - 1 >= 0 ) {			
						for (int f=0; f < FEATURES; f++){
							beliefs[r - i - 1][c - j - 1][f]=TRAITS;
						}
					}
				}
				if ( j + i <= radius && ( j != 0 || i != 0 ) ) {
					if ( r - i >= 0 && c + j < COLS ) {
						for (int f=0; f < FEATURES; f++){
							beliefs[r - i][c + j][f]=TRAITS;
						}
					}
					if ( r + i < ROWS && c - j >= 0 ) {
						for (int f=0; f < FEATURES; f++){
							beliefs[r + i][c - j][f]=TRAITS;
						}
					}								
				}
			}
		}
	}

	protected void institutional_conversion(double prob){}
	protected void institutional_trait_conversion(double prob){}
	
	/**
	 * A genocide would indicate traits as dead.
	 * 
	 * @param probability
	 */
	protected void genocide(double probability){
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				for (int f = 0; f < FEATURES; f++) {
					if (probability > Math.random()){
						beliefs[r][c][f] = DEAD_TRAIT;
					}
				}
			}
		}
	}

	
	protected void update_gui(){
		print_belief_spaces();
		update_culture_graphs();
		CulturalSimulator.l_start_identification.setText("S: " + starter.get_identification());
		CulturalSimulator.l_current_identification.setText("C: " + get_identification());
	}
	
	private void update_culture_graphs(){
		CulturalSimulator.graph_cultures.scores.add((double) cultureN / TOTAL_AGENTS);
		CulturalSimulator.graph_cultures.scores2.add((double) biggest_cluster / TOTAL_AGENTS);
		CulturalSimulator.l_cultures.setText(cultureN + "/" + biggest_cluster);
		CulturalSimulator.graph_cultures.update();
		CulturalSimulator.graph_borderless_cultures.scores.add((double) culture_borderlessN / TOTAL_AGENTS);
		CulturalSimulator.graph_borderless_cultures.scores2.add((double) biggest_borderless_cluster / TOTAL_AGENTS);
		CulturalSimulator.l_borderless.setText(culture_borderlessN + "/" + biggest_borderless_cluster);
		CulturalSimulator.graph_borderless_cultures.update();
		CulturalSimulator.graph_energy_foreign_trait.scores.add((double) energy / (TOTAL_AGENTS*FEATURES));
		CulturalSimulator.graph_energy_foreign_trait.scores2.add((double) foreiners_traits / (TOTAL_AGENTS*FEATURES));
		CulturalSimulator.l_energy_foreigners.setText(energy + "/" + foreiners_traits);
		CulturalSimulator.graph_energy_foreign_trait.update();
		
	}
		
	private void print_belief_spaces(){
		
		BufferedImage image = new BufferedImage(ROWS, COLS, BufferedImage.TYPE_INT_RGB);

		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				String ohex = "";
				for (int f = 0; f < FEATURES; f++) {
					if (beliefs[r][c][f] == -1){
						ohex += Integer.toHexString(15);
					} else if (beliefs[r][c][f] == DEAD_TRAIT){
						ohex += Integer.toHexString(0);
					} else {
						ohex += Integer.toHexString(beliefs[r][c][f]+1);
					}
				}
				ohex = "#" + ohex;
				
				image.setRGB(r, c, Color.decode(ohex).getRGB());
			}
		}
		
		CulturalSimulator.set_belief_space(image);
		
	}
	



}