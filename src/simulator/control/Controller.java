package simulator.control;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

import simulator.gui.Notifiable;

/**
 * The controller of the simulations handles the simulation. It creates the
 * tasks and provide an interface for the user to interrupt, suspend or resume
 * the simulation.
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 */
public abstract class Controller {

	/**
	 * Workspace Directory
	 */
	public static String WORKSPACE_DIR = "./workspace/";

	/**
	 * Default directory containing saved disasters (combination of catastrophic
	 * events)
	 */
	public static final String EVENTS_DIR = "events/";

	/**
	 * Default directory containing simulation configurations (combination of
	 * parameters for a simulation)
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
	 * Default directory (base)name to save results, if RESULT_DIR exists in the
	 * destination folder then another folder called RESULTS_DIR# is created,
	 * where # increases automatically
	 */
	public static final String RESULTSET_DIR = "resultset/";

	/**
	 * Directory to write results
	 */
	protected String results_dir = "results/";

	/**
	 * Directory to write results
	 */
	protected String identifier = "results";

	/**
	 * Directory to save the CSV results of the simulations
	 */
	public static final String ITERATIONS_DIR = "iterations/";

	/**
	 * Directory to save the final state of the simulations after any
	 * experiment.
	 */
	public static final String SIMULATIONS_DIR = "simulations/";

	/**
	 * References the Executor service that handles the threads
	 */
	protected ExecutorService exec = null;

	/**
	 * Component that gets notified when the threads are finished
	 */
	protected Notifiable notifiable = null;

	/**
	 * Indicates if several simulations are running at the same time
	 */
	public static boolean IS_BATCH;

	/**
	 * The constructor of the controller that receives an interface to be
	 * notified of the finalization of the simulations
	 * 
	 * @param notifiable
	 */
	public Controller(Notifiable notifiable) {
		this.notifiable = notifiable;
	}

	/**
	 * 
	 * It creates the directories for the simulation to start. After that it
	 * start the simulations.
	 * 
	 * @param ws_dir
	 *            directory where the results are to be stored
	 * @param id
	 *            identifier of the simulation, used for creating respective
	 *            files/directories
	 */
	public void start(String id, String r_d) {

		identifier = id;
		// Look for a non existent results directory
		File dir = new File(r_d + identifier + "/");
		String result_dir = identifier;
		for (int i = 0; dir.exists(); i++) {
			dir = new File(r_d + result_dir + i + "/");
		}

		results_dir = dir.getAbsolutePath() + "/";
		(new File(results_dir + ITERATIONS_DIR)).mkdirs();
		(new File(results_dir + SIMULATIONS_DIR)).mkdirs();

		File resultset_dir = new File(r_d + RESULTSET_DIR);
		if (!resultset_dir.exists()) {
			resultset_dir.mkdirs();
		}

		play();
	}

	/**
	 * Start or resume running the simulation(s)
	 */
	protected abstract void play();

	/**
	 * Cancel the active simulation(s)
	 * 
	 * @throws IOException
	 */
	public abstract void cancel();

	/**
	 * Suspend/Pause the active simulation(s)
	 */
	public abstract void suspend();

	/**
	 * Resume the active simulation(s)
	 */
	public abstract void resume();

	/**
	 * Write the final results (or the intermediate ones when
	 * interrupted/stopped) in the results directory. In batch mode, it also
	 * create a copy in the resultset directory in the workspace.
	 * 
	 * @throws IOException
	 */
	public abstract void write_results() throws IOException;

}
