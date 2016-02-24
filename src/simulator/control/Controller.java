package simulator.control;

import java.util.concurrent.ExecutorService;


public abstract class Controller {
	

	/**
	 * Workspace Directory
	 */
	public static final String WORKSPACE_DIR = "./";
	
	/**
	 * Default directory containing saved disasters (combination of catastrophic events)
	 */
	public static final String DISASTERS_DIR = "disasters/";
	
	/**
	 * Default directory containing simulation configurations (combination of parameters
	 * for a simulation)
	 */
	public static final String CONFIGURATIONS_DIR = "configurations/";
	
	/**
	 * Default directory to save interesting worlds after N iterations
	 */
	public static final String WORLDS_DIR = "worlds/";
	
	/**
	 * Directory to save preset configurations, e.g. Axelrod or Flache defaults 
	 */
	public static final String PRESETS_DIR = "presets/";

	/**
	 * Default directory (base)name to save results, if RESULT_DIR exists 
	 * in the destination folder then another folder called RESULTS_DIR#
	 * is created, where # increases automatically
	 */
	public static final String RESULTS_DIR = "results/";
	
	/**
	 * Directory to save the CSV results of the simulations
	 */
	public static final String ITERATIONS_DIR = "iterations/";

	/**
	 * Directory to save the final state of the simulations after 
	 * any experiment.
	 */
	public static final String SIMULATIONS_DIR = "simulations/";
	
	
	/**
	 * References the Executor service that handles the threads
	 */
	protected ExecutorService exec = null;
	
	
	/**
	 * Indicates if several simulations are running at the same time
	 */
	public static boolean IS_BATCH;

		
}
