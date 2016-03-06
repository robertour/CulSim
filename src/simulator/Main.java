package simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import simulator.control.Controller;
import simulator.control.ControllerBatch;
import simulator.control.Printable;
import simulator.control.events.Event;


public class Main {
	private static Printer printer;
	

	public static void main(String[] args) {
		String experimental_file = null;
		String results_directory = null;
		boolean collecting_events_args = false;
		ArrayList<Event> events = new ArrayList<Event>();
		
	    for (int i = 0; i < args.length; i++) {
	        switch (args[i].charAt(0)) {
		        case '-':
	                collecting_events_args = false;                	
		            if (args[i].length() < 2)
		                throw new IllegalArgumentException("Not a valid argument: "+args[i]);
	                if (args.length-1 == i)
	                    throw new IllegalArgumentException("Expected argument after: "+args[i]);
	                String argu = args[i].substring(1, args[i].length());

	                if (argu.equals("ef")){
	                	experimental_file = args[i+1];
	                	i++;
	                } else if (argu.equals("rd")){
	                	results_directory = args[i+1];
	                	i++;
	                } else if (argu.equals("evs")){
	                	collecting_events_args = true;
	                } else {
	                	throw new IllegalArgumentException("Invalid argument: "+args[i]);
	                }
		            break;
		        default:
		        	if (collecting_events_args){
		        		events.add(Event.parseEvent(args[i]));
		        	} else {
		        		throw new IllegalArgumentException("Unexpected argument: "+args[i]);
		        	}
	        }
	    }
	    
	    printer = new Main.Printer();
	    
	    if (results_directory != null && experimental_file != null ){
	    	throw new IllegalArgumentException("WARNING: either the experimental file (-ef) or results directory (-rd)"
	    			+ " but not both has to be specified.");
	    } else if (results_directory == null && experimental_file == null ){
	    	printer.print(-1, "WARNING: no experimental file or results directory has been specified,"
	    			+ " 'sample.csv' will be used as experimental file.");
	    	experimental_file = "sample.csv";
	    }
	    
	    if (events.size() > 0){
	    	printer.print(-1, "The folling events have been set up for the scenarios: \n");
	    	for (Iterator <Event> iterator = events.iterator(); iterator.hasNext();) {
				printer.print(-1, iterator.next() + "\n");
			}
	    }
	    
	    if (results_directory != null){
	    	run_from_directory(results_directory, events);
	    	
	    } else if (experimental_file != null){
	    	run_from_file(experimental_file, events);
	    	
	    }
	}
	
	private static void run_from_directory(String rd, ArrayList<Event> events){
		ControllerBatch controller = new ControllerBatch(printer);
		ArrayList<String> sim_list = new ArrayList<String>();
		
		File rdf = new File(rd);
		File simulations_dir = new File (rdf.getAbsolutePath() + "\\" + ControllerBatch.SIMULATIONS_DIR);
    	if (simulations_dir.exists() && simulations_dir.isDirectory()){
			File[] directoryListing = simulations_dir.listFiles();
			if (directoryListing != null) {
				for (File child : directoryListing) {
					sim_list.add(child.getAbsolutePath());
				}
			} else {
				throw new IllegalArgumentException("The " + rd + ControllerBatch.SIMULATIONS_DIR + 
						" directory didn't contain any simulations. Please make sure you are "
						+ "providing a directory with the results of a Batch process.");
			}
		} else {
			throw new IllegalArgumentException("The " + rd + ControllerBatch.SIMULATIONS_DIR + 
					" directory didn't contain any simulations. Please make sure you are "
					+ "providing a directory with the results of a Batch process.");
		}
    	controller.load_simulations_from_directory(sim_list, events, 1);
    	
		controller.run( rd + "\\");
	
	}

	private static void run_from_file(String ef, ArrayList<Event> events){
		ControllerBatch controller = new ControllerBatch(printer);
		
		try {
			controller.load_simulations_from_file(ef,events);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("File not found: "+ ef);
		}

		controller.run(Controller.WORKSPACE_DIR);
	}
		
	private static class Printer implements  Printable {
		public void print(int id, String str){
			if (id < 0){
				System.out.print(str);
			} else {
				System.out.print("(ID: " + id +  "): " + str);
			}
		} 
	}

}
