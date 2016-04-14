package simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import simulator.control.Controller;
import simulator.control.ControllerBatch;
import simulator.control.Printable;
import simulator.control.events.Event;

/**
 * Main text-based interface of the simulation. It implements a command based
 * version of the software that can be used in servers. The configuration of the
 * simulation is given by parameters in the command, or by files.
 * 
 * There is basically two ways of running a simulation, by file or by directory.
 * The two ways are used for different purposes.
 * 
 * By file: it uses a csv file with an experimental design as the main input.
 * This csv contains the parameters that the simulation is going to run. The
 * purpose here is to run the simulation to let cultures emerge. Although
 * introducing events is possible, for most of the cases it really makes little
 * sense to introduce them because the system hasn't converge to any particular
 * state. A big exception occurs when the simulation is not initialized at
 * random, but with only one culture. The results of the simulation is stored in
 * one folder that becomes the main input of the other way of running the
 * simulation, i.e. by directory.
 * 
 * By folder: it uses a folder produced by the "by file" way of running the
 * simulation (see above). This folder contains the final simulation states of
 * (presumably) converged states in which cultures have emerged. The main
 * purpose of running the simulation with the "by directory" mode is to test the
 * effects of events on those converge states.
 * 
 * 
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 *
 */
public class Main {
	private static Printer printer;

	/**
	 * The main method for the text-based interaction throught the command line
	 * 
	 * @param args
	 *            the main arguments are as follows: -ef is the initial
	 *            experimental csv file (for the "by file" mode); -rd, the
	 *            results directory (for the "by directory" mode); -id, an
	 *            identifier for the simulation used in the directory file names
	 *            that are created automatically; -r specifies the number of
	 *            repetitions (for the "by directory" mode); -evs, to send the
	 *            event sets that should be executed
	 */
	public static void main(String[] args) {
		String experimental_file = null;
		String results_directory = null;
		String identifier = "results";
		int rep = -1;
		boolean collecting_events_args = false;
		ArrayList<Event> events = new ArrayList<Event>();

		for (int i = 0; i < args.length; i++) {
			switch (args[i].charAt(0)) {
			case '-':
				collecting_events_args = false;
				if (args[i].length() < 2)
					throw new IllegalArgumentException("Not a valid argument: " + args[i]);
				if (args.length - 1 == i)
					throw new IllegalArgumentException("Expected argument after: " + args[i]);
				String argu = args[i].substring(1, args[i].length());

				if (argu.equals("ef")) {
					experimental_file = args[i + 1];
					i++;
				} else if (argu.equals("rd")) {
					results_directory = args[i + 1];
					i++;
				} else if (argu.equals("id")) {
					identifier = args[i + 1];
					i++;
				} else if (argu.equals("r")) {
					try {
						rep = Integer.parseInt(args[i + 1]);
						i++;
					} catch (Exception e) {
						throw new IllegalArgumentException("Invalid argument for -r: " + args[i + 1]);
					}

				} else if (argu.equals("evs")) {
					collecting_events_args = true;
				} else {
					throw new IllegalArgumentException("Invalid argument: " + args[i]);
				}
				break;
			default:
				if (collecting_events_args) {
					events.add(Event.parseEvent(args[i]));
				} else {
					throw new IllegalArgumentException("Unexpected argument: " + args[i]);
				}
			}
		}

		printer = new Main.Printer();

		if (results_directory != null && experimental_file != null) {
			throw new IllegalArgumentException("WARNING: either the experimental file (-ef) or results directory (-rd)"
					+ " but not both has to be specified.");
		} else if (results_directory == null && experimental_file == null) {
			printer.print(-1, "WARNING: no experimental file or results directory has been specified,"
					+ " 'sample.csv' will be used as experimental file.");
			experimental_file = "sample.csv";
		}

		if (events.size() > 0) {
			printer.print(-1, "The following events have been set up for the scenarios: \n");
			for (Iterator<Event> iterator = events.iterator(); iterator.hasNext();) {
				printer.print(-1, iterator.next() + "\n");
			}
		}

		if (results_directory != null) {
			run_from_directory(results_directory, events, identifier, rep);

		} else if (experimental_file != null) {
			if (rep != -1) {
				throw new IllegalArgumentException(
						"The number of iterations are take from the csv file. Parameter -i is ambiguos when a csv is specified.");
			}
			run_from_file(experimental_file, events, identifier);
		}

	}

	/**
	 * Run the simulation in the "by directory" mode.
	 * 
	 * @param rd
	 *            the results directory is the main input of the simulation
	 * @param events
	 *            the events that will be executed in the converged states
	 *            stored in the results directory
	 * @param id
	 *            the id of the simulation to identify the results
	 * @param rep
	 *            the number of repetitions that this will be executed (it makes
	 *            more sense for randomized events)
	 * @return the controller of the simulation
	 */
	private static ControllerBatch run_from_directory(String rd, ArrayList<Event> events, String id, int rep) {
		ControllerBatch controller = new ControllerBatch(printer, null);
		ArrayList<String> sim_list = new ArrayList<String>();

		File rdf = new File(rd);
		File simulations_dir = new File(rdf.getAbsolutePath() + "/" + ControllerBatch.SIMULATIONS_DIR);
		if (simulations_dir.exists() && simulations_dir.isDirectory()) {
			File[] directoryListing = simulations_dir.listFiles();
			if (directoryListing != null) {
				for (File child : directoryListing) {
					sim_list.add(child.getAbsolutePath());
				}
			} else {
				throw new IllegalArgumentException("The " + rd + ControllerBatch.SIMULATIONS_DIR
						+ " directory didn't contain any simulations. Please make sure you are "
						+ "providing a directory with the results of a Batch process.");
			}
		} else {
			throw new IllegalArgumentException("The " + rd + ControllerBatch.SIMULATIONS_DIR
					+ " directory didn't contain any simulations. Please make sure you are "
					+ "providing a directory with the results of a Batch process.");
		}

		try {
			if (rep == -1) {
				controller.load_simulations(sim_list, events, 1);
			} else {
				controller.load_simulations(sim_list, events, rep);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("One of the files in the directory was not found.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(
					"One of the files in the directory is not of the type simulation. Maybe an old version?.");
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("I/O Exception.");
		}

		controller.start(id, rd + "/");
		return controller;

	}

	/**
	 * Run the simulation in the "by file" mode.
	 * 
	 * @param ef
	 *            the experimental file is the main input of the simulation, a
	 *            csv file describing the parameters and repetition of each
	 *            configuration
	 * @param events
	 *            the events that will be executed in the simulations, it makes
	 *            more sense when the simulation is not initialized at random in
	 *            the parameters of the csv file
	 * @param id
	 *            the id of the simulation to identify the results
	 * @return the controller of the simulation
	 */
	private static ControllerBatch run_from_file(String ef, ArrayList<Event> events, String id) {
		ControllerBatch controller = new ControllerBatch(printer, null);

		try {
			controller.load_simulations_from_file(ef, events);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("File not found: " + ef);
		}

		controller.start(id, Controller.WORKSPACE_DIR);

		return controller;
	}

	/**
	 * An proxy class to the system.out. It basically allows for the simulations
	 * to print in the terminal
	 * 
	 * @author Roberto Ulloa
	 * @version 1.0, April 2016
	 *
	 */
	private static class Printer implements Printable {
		public void print(int id, String str) {
			if (id < 0) {
				System.out.print(str);
			} else {
				System.out.print("(ID: " + id + "): " + str);
			}
		}
	}

}
