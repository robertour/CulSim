package simulator.control;

import java.util.concurrent.ExecutorService;


public abstract class Controller {

	
	// References the Executor service that handles the threads
	protected ExecutorService exec = null;
	
	
	/**
	 * Indicates if several simulations are running at the same time
	 */
	public static boolean IS_BATCH;

	
		
		
}
