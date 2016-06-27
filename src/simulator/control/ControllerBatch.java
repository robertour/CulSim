package simulator.control;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import simulator.control.events.Event;
import simulator.gui.Notifiable;
import simulator.worlds.E1;
import simulator.worlds.E3;
import simulator.worlds.E2;
import simulator.worlds.Inst;

/**
 * The controller of the simulations handles the simulations and run them in
 * batch mode. It creates threads for the simulations, and provide methods to
 * start, stop, suspend or resume them, load simulations from a CSV file or a
 * results directory, interact with a GUI and add events to the simulations
 * 
 * @author Roberto Ulloa
 * @version 1.0, March 2016
 */
public class ControllerBatch extends Controller {

	/**
	 * Area to display the messages
	 */
	protected static Printable log = null;

	/**
	 * Events that are going to be executed in the simulation
	 */
	protected ArrayList<Event> events = new ArrayList<Event>();

	/**
	 * List of simulations that need to be run
	 */
	protected ArrayList<Simulation> simulations = null;

	/**
	 * Constructor of the controller that handles multiple simulations (as
	 * threads) and alternatively the user batch interface (@see
	 * simulator.BatchMode) or CommandLine (@see simulator.Main).
	 * 
	 * @param printable
	 *            the object which will be in charge of displaying the messages
	 *            from the simulations and the controller
	 * @param notifiable
	 *            the object that will be notified when the simulations are
	 *            finished
	 */
	public ControllerBatch(Printable output, Notifiable n) {
		super(n);
		log = output;
	}

	/**
	 * Load a single simulation object from a file
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private Simulation load_simulation(String simfile)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		Simulation s = null;
		
		FileInputStream fis = new FileInputStream(simfile);
		GZIPInputStream gis = new GZIPInputStream(fis);
		ObjectInputStream ois = new ObjectInputStream(gis);
		
		s = (Simulation) ois.readObject();
		s.log = log;
		ois.close();
		return s;
	}

	/**
	 * Load the simulation stored in the simulation files as many times as
	 * specified in repetitions and add them to the simulation list. It also add
	 * the events that will be happening in all the loaded simulations.
	 * 
	 * @param sim_files
	 *            the list of simulation files
	 * @param events
	 *            the list of events that will be executed in the simuations
	 * @param repetitions
	 *            the number of times each event set will be executed
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws FileNotFoundException
	 */
	public void load_simulations(ArrayList<String> sim_files, ArrayList<Event> events, int repetitions)
			throws FileNotFoundException, ClassNotFoundException, IOException {
		simulations = new ArrayList<Simulation>();
		IS_BATCH = true;
		if (events != null) {
			this.events = events;
		}

		for (Iterator<String> iterator = sim_files.iterator(); iterator.hasNext();) {
			String simstate_file = (String) iterator.next();
			for (int j = 0; j < repetitions; j++) {
				Simulation s = this.load_simulation(simstate_file);
				if (events != null) {
					s.events(events);
				}
				simulations.add(s);
			}
		}
	}
	
	/**
	 * Load the simulation configuration store in the simulation files as many times as
	 * specified in repetitions and add them to the simulation list. It also add
	 * the events that will be happening in all the loaded simulations.
	 * 
	 * @param sim_files
	 *            the list of simulation files
	 * @param events
	 *            the list of events that will be executed in the simuations
	 * @param repetitions
	 *            the number of times each simulation with the provided configuration will be executed
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws FileNotFoundException
	 */
	public void load_configurations(ArrayList<String> sim_files, ArrayList<Event> events, int repetitions)
			throws FileNotFoundException, ClassNotFoundException, IOException {
		simulations = new ArrayList<Simulation>();
		IS_BATCH = true;
		if (events != null) {
			this.events = events;
		}

		for (Iterator<String> iterator = sim_files.iterator(); iterator.hasNext();) {
			String simstate_file = (String) iterator.next();
			Simulation s = this.load_simulation(simstate_file);
			s.starter.clean();
			s.clean();
			for (int j = 0; j < repetitions; j++) {
				// A new random seed is generated
				Simulation clone = s.clone();
				if (events != null) {
					clone.events(events);
				}
				simulations.add(clone);
			}
		}
	}

	/**
	 * Load simulation from an experimental design stored in a CSV file, this is
	 * the main way to start interacting with the console mode. It creates a
	 * simulation for each line in the csv file (Refer to the user manual on how
	 * to create a CSV file). It also accepts a list of events that will be
	 * executed in the scenarios.
	 * 
	 * @param csv_file
	 *            the csv file containing the experimental design
	 * @param events
	 *            the list of events that will be executed in the simulation
	 * @throws FileNotFoundException
	 */
	public void load_simulations_from_file(String csv_file, ArrayList<Event> events) throws FileNotFoundException {

		this.events = events;

		// This is used to randomize the experiment.
		Random rand = new Random();

		File f = new File(csv_file);
		if (!f.exists()){
			throw new FileNotFoundException("The file " + f.getAbsolutePath() + "doesn't exist.");
		}
		
		// Get scanner instance
		Scanner scanner = new Scanner(new File(csv_file));

		// skip the column titles
		scanner.nextLine();
		scanner.useDelimiter(",");

		simulations = new ArrayList<Simulation>();

		// Start reading the file
		while (scanner.hasNext()) {
			int repetitions = Integer.parseInt(scanner.next());
			if (repetitions > 0) {
				Simulation simulation = null;
				String type = scanner.next();
				if (type.equals(E3.class.getSimpleName()))
					simulation = new E3();
				else if (type.equals(Inst.class.getSimpleName()))
					simulation = new Inst();
				else if (type.equals(E1.class.getSimpleName()))
					simulation = new E1();
				else if (type.equals(E2.class.getSimpleName()))
					simulation = new E2();
				else{
					log.print(-1, "No class was recognized with the type: " + type + ". Please check your csv.");
					System.exit(1);
				}
					
				simulation.RANDOM_INITIALIZATION = Boolean.parseBoolean(scanner.next());
				simulation.ITERATIONS = Integer.parseInt(scanner.next());
				simulation.SPEED = Integer.parseInt(scanner.next());
				simulation.BUFFERED_SIZE = Integer.parseInt(scanner.next());
				simulation.ROWS = Integer.parseInt(scanner.next());
				simulation.COLS = Integer.parseInt(scanner.next());
				simulation.RADIUS = Integer.parseInt(scanner.next());
				simulation.FEATURES = Integer.parseInt(scanner.next());
				simulation.TRAITS = Integer.parseInt(scanner.next());
				simulation.MUTATION = Float.parseFloat(scanner.next());
				simulation.SELECTION_ERROR = Float.parseFloat(scanner.next());
				simulation.ALPHA = Float.parseFloat(scanner.next());
				simulation.ALPHA_PRIME = Float.parseFloat(scanner.next());
				simulation.FREQ_DEM = Integer.parseInt(scanner.next());
				simulation.FREQ_PROP = Integer.parseInt(scanner.next());
				simulation.log = log;

				// Generate tasks per repetitions
				Simulation clone;
				for (int r = 0; r < repetitions; r++) {
					clone = simulation.clone();

					if (events.size() > 0) {
						clone.events(events);
					}
					simulations.add(rand.nextInt(simulations.size() + 1), clone);
				}

				if (scanner.hasNextLine()) {
					scanner.nextLine();
				}
			}
		}

		// Do not forget to close the scanner
		scanner.close();
	}

	@Override
	public void write_results() throws IOException {
		BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(results_dir + identifier + ".csv"), "utf-8"));
		BufferedWriter writer2 = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream((new File(results_dir)).getParent() + "/" + RESULTSET_DIR
						+ (new File(results_dir)).getName() + "-" + identifier + ".csv"), "utf-8"));
		writer.write(Simulation.header());
		writer.newLine();
		writer2.write(Simulation.header());
		writer2.newLine();
		for (Simulation w : simulations) {
			writer.write(w.get_results());
			writer.newLine();
			writer2.write(w.get_results());
			writer2.newLine();
		}
		writer.close();
		writer2.close();
	}

	/**
	 * Write the events that happened in this simulation.
	 * 
	 * @throws IOException
	 */
	protected void write_events() throws IOException {
		// Write the results to the file
		BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(results_dir + "events.txt"), "utf-8"));
		if (events.size() > 0) {
			writer.write("The following events have been set up for the scenarios:");
			writer.newLine();
			for (Iterator<Event> iterator = events.iterator(); iterator.hasNext();) {
				Event ev = iterator.next();
				writer.write(ev.toString() + ev.seedToString());
				writer.newLine();
			}
		} else {
			writer.write("No events were set up for the present scenario.");
			writer.newLine();
		}
		writer.close();
	}

	@Override
	protected void play() {

		IS_BATCH = true;

		// This is a pool of threads of the size of the cores of the computer
		exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		int id = 0;
		for (Simulation w : simulations) {
			w.IDENTIFIER = id++;
			w.log = log;
			w.results_dir = results_dir;
			exec.submit(w);
		}

		log.print(-1, "All Tasks Submitted.\n");

		exec.shutdown();

		(new SimulationExecuter()).start();

	}

	/**
	 * Clean all the simulations structrues that are not going to be used
	 * anymore
	 * 
	 * @throws IOException
	 */
	public void clean_all() {
		if (simulations != null) {
			for (Simulation w : simulations) {
				w.clean();
			}
		}
	}

	@Override
	public void cancel() {
		if (simulations != null) {
			for (Simulation w : simulations) {
				w.cancel();
			}
			exec.shutdownNow();
		}
	}

	@Override
	public void suspend() {
		for (Simulation w : simulations) {
			w.suspend();
		}
	}

	@Override
	public void resume() {
		for (Simulation w : simulations) {
			w.resume();
		}
	}

	/**
	 * Handles the general execution of the simulations, it notifies the
	 * interface (if any) of the finalization of the simulation and its possible
	 * interruptions
	 * 
	 * @author Roberto Ulloa
	 * @version 1.0, March 2016
	 */
	private class SimulationExecuter extends Thread {

		/**
		 * Runs and wait for the simulations to finish. Display errors and
		 * messages in the the printable, and notify the end of the execution.
		 * It also save the events that are going to be executed inside the
		 * simulations.
		 */
		public void run() {
			log.print(-1, "Simulation Executor Started.\n");
			try {
				write_events();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				exec.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
				log.print(-1, "Thread execution has finished.\n");
			} catch (InterruptedException e) {
				log.print(-1, "Simulation interrupted.\n");
			}
			try {
				write_events();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				write_results();
				log.print(-1, "Final results written.\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (notifiable != null) {
				notifiable.update();
			}

		}
	}

}