package simulator;

import java.util.concurrent.ExecutorService;


public abstract class Controller {

	
	// References the Executor service that handles the threads
	protected ExecutorService exec = null;
	
	/**
	 * Directory to write results
	 */
	public static String RESULTS_DIR = null;
	

	/**
	 * The area to append the results (log)
	 */
	public static Printable TA_OUTPUT;
	
	/**
	 * Indicates if several simulations are running at the same time
	 */
	public static boolean IS_BATCH;
	

	public Controller(Printable ta_output) {
		super();
		TA_OUTPUT = ta_output;
	}
	
	public static void setRESULTS_DIR(String results_dir) {
		RESULTS_DIR = results_dir;
	}
	
		
}
