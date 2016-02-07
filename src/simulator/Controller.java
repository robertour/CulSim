package simulator;

import java.util.concurrent.ExecutorService;

import javax.swing.JTextArea;

public abstract class Controller {

	
	// References the Executor service that handles the threads
	protected ExecutorService exec = null;
	
	/**
	 * Directory to write results
	 */
	public static String RESULTS_DIR = "./";
	
	/**
	 * The area to append the results (log)
	 */
	public static JTextArea TA_OUTPUT;
	
	/**
	 * Indicates if several simulations are running at the same time
	 */
	public static boolean IS_BATCH;
		
}
