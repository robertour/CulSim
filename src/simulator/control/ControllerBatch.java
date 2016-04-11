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

import simulator.control.events.Event;
import simulator.gui.Notifiable;
import simulator.worlds.Axelrod;
import simulator.worlds.Flache;
import simulator.worlds.Flache2;
import simulator.worlds.Ulloa;
 
/**
 * The controller of the simulations handles the simulations and run them in batch
 * mode. It creates threads for the simulations, and provide methods to start, stop, 
 * suspend or resume them, load simulations from a CSV file or a results directory,
 * interact with a GUI and add events to the simulations
 * 
 * @author Roberto Ulloa
 * @version 1.0, March 2016
 */
public class ControllerBatch extends Controller
{

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
	 * Constructor of the controller that handles multiple simulations (as threads) and 
	 * alternatively the user batch interface (@see simulator.BatchMode) or 
	 * CommandLine (@see simulator.Main).
	 * @param printable the object which will be in charge of displaying the messages 
	 * from the simulations and the controller
	 * @param notifiable the object that will be notified when the simulations are finished
	 */
	public ControllerBatch(Printable output, Notifiable n) {
		super (n);
		log = output;
	}

	/** 
	 * Load a single simulation object from a file
	 * 
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private Simulation load_simulation(String simfile) throws FileNotFoundException, IOException, ClassNotFoundException{
		Simulation s = null;
		ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(simfile));
		s = (Simulation) inFile.readObject();
		s.log = log;
		inFile.close();
		return s;
	}

    
	/**
	 * Load the simulation stored in the simulation files as many times as specified
	 * in repetitions and add them to the simulation list. It also add the events that
	 * will be happening in all the loaded simulations.
	 * 
	 * @param sim_files the list of simulation files
	 * @param events the list of events that will be executed in the simuations
	 * @param repetitions the number of times each simulation on a file will be executed
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException 
	 */
    public void load_simulations(ArrayList<String> sim_files, ArrayList<Event> events, int repetitions) throws FileNotFoundException, ClassNotFoundException, IOException {
    	simulations = new ArrayList<Simulation>();
    	IS_BATCH = true;
    	if (events != null){
    		this.events = events;
    	}

    	for (Iterator<String> iterator = sim_files.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			for (int j = 0; j < repetitions; j++) {
				Simulation s = this.load_simulation(string);
				if (events != null){
					s.events(events);
				}
    			simulations.add(s);	
			}	
		}
    }
	
	/**
	 * Load simulation from an experimental design stored in a CSV file, this is
	 * the main way to start interacting with the console mode. It creates a simulation
	 * for each line in the csv file (Refer to the user manual on how to create a
	 * CSV file). It also accepts a list of events that will be executed in the scenarios.
	 * 
	 * @param csv_file the csv file containing the experimental design
	 * @param events the list of events that will be executed in the simulation
	 * @throws FileNotFoundException
	 */
    public void load_simulations_from_file(String csv_file, ArrayList<Event> events) throws FileNotFoundException {
    	
    	this.events = events;
    	
    	// This is used to randomize the experiment.
    	Random rand = new Random();
    	
    	//Get scanner instance        
    	Scanner scanner = new Scanner(new File(csv_file));
        
    	//skip the column titles
        scanner.nextLine();
        scanner.useDelimiter(",");
         
        simulations = new ArrayList<Simulation>();
        
        //Start reading the file
        while (scanner.hasNext())
        {
        	int repetitions = Integer.parseInt(scanner.next());
        	if (repetitions > 0) {
        		Simulation simulation = null;
        		String type = scanner.next();
		    		switch (type) {
		    		case "Flache": simulation = new Flache(); break;
		    		case "Ulloa": simulation = new Ulloa(); break;		    		
        			case "FLACHE1":	simulation = new Axelrod();	break;
        			case "FLACHE2":	simulation = new Flache2();	break;
        		}
		    	simulation.RANDOM_INITIALIZATION = Boolean.parseBoolean(scanner.next());
	        	simulation.ITERATIONS = Integer.parseInt(scanner.next());
	        	simulation.CHECKPOINT = Integer.parseInt(scanner.next());
	        	simulation.BUFFERED_SIZE = Integer.parseInt(scanner.next());
	        	simulation.ROWS = Integer.parseInt(scanner.next());
	        	simulation.COLS = Integer.parseInt(scanner.next());
	        	simulation.FEATURES = Integer.parseInt(scanner.next());
	        	simulation.TRAITS = Integer.parseInt(scanner.next());
	        	simulation.RADIUS = Integer.parseInt(scanner.next());
	        	simulation.ALPHA = Float.parseFloat(scanner.next());
	        	simulation.ALPHA_PRIME = Float.parseFloat(scanner.next());
	        	simulation.FREQ_DEM =  Integer.parseInt(scanner.next());
	        	simulation.FREQ_PROP =  Integer.parseInt(scanner.next());
	        	simulation.MUTATION = Float.parseFloat(scanner.next());
	        	simulation.SELECTION_ERROR = Float.parseFloat(scanner.next());
				simulation.log = log;

	        	
	        	// Generate tasks per repetitions
	        	Simulation clone;
	        	for (int r = 0; r < repetitions; r++) {
	        		clone = simulation.clone();

	        		if (events.size() > 0) {
	        			clone.events(events);
	        		}
		        	simulations.add( rand.nextInt(simulations.size()+1), clone);
	        	}
	        	
	        	if (scanner.hasNextLine()) {
	        		scanner.nextLine();
	        	}
        	}
        }
         
        //Do not forget to close the scanner 
        scanner.close();
    }
    
    @Override
    public void write_results() throws IOException {
    	
    	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
              new FileOutputStream(results_dir + identifier + ".csv"), "utf-8"));
    	BufferedWriter writer2 = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream((new File(results_dir)).getParent() + "/" + RESULTSET_DIR + 
                		(new File(results_dir)).getName() + "-" + identifier + ".csv"), "utf-8"));
        writer.write(Simulation.header());
        writer2.write(Simulation.header());
        for(Simulation w : simulations) {
			writer.write(w.get_results());
			writer2.write(w.get_results());
			
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
    	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
              new FileOutputStream(results_dir + "events.txt"), "utf-8"));
    	if (events.size() > 0){
    		writer.write("The following events have been set up for the scenarios: \n");
	    	for (Iterator <Event> iterator = events.iterator(); iterator.hasNext();) {
				writer.write(iterator.next() + "\n");
			}
	    } else {
	    	writer.write("No events were set up for the present scenario. \n");
	    }
        writer.close();    	
    }
    
    @Override
    protected void play(){
    	
    	IS_BATCH = true;
   	
    	// This is a pool of threads of the size of the cores of the computer
    	exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    	
    	int id = 0;
    	for(Simulation w : simulations) {
    		w.IDENTIFIER = id++;
    		w.log = log;
    		w.results_dir = results_dir;
    		exec.submit(w);
    	} 
    
    	log.print(-1,"All Tasks Submitted\n");
    	
    	exec.shutdown();
    	
    	(new SimulationExecuter()).start();
        
    }

    /** 
     * Clean all the simulations structrues that are not going to be 
     * used anymore
     * @throws IOException
     */
    public void clean_all() {
    	if (simulations != null) {
	    	for(Simulation w : simulations) {
	    		w.clean();
	    	} 
    	}
    }
    
    @Override
    public void cancel() {
    	if (simulations != null){
	    	for(Simulation w : simulations) {
	    		w.cancel();
	    	} 
	    	exec.shutdownNow();
    	}
    }
    
    @Override
    public void suspend() {
    	for(Simulation w : simulations) {
    		w.suspend();
    	} 
    }
    
    @Override
    public void resume(){
    	for(Simulation w : simulations) {
    		w.resume();
    	}
    }
    
    /**
     * Handles the general execution of the simulations, it notifies the 
     * interface (if any) of the finalization of the simulation and its 
     * possible interruptions
     * 
     * @author Roberto Ulloa
     * @version 1.0, March 2016
     */
    private class SimulationExecuter extends Thread {
    	
    	/**
    	 * Runs and wait for the simulations to finish. Display errors and messages 
    	 * in the the printable, and notify the end of the execution. It also save 
    	 * the events that are going to be executed inside the simulations.
    	 */
    	public void run (){
    		log.print(-1, "Simulation Executor Started\n");
        	try {
    			write_events();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		try {
				exec. awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
				log.print(-1, "The threads were finished and no errors where reported\n");
			} catch (InterruptedException e) {
				log.print(-1, "Simulation interrupted\n");
			}
    		try {
		    	write_results();
			} catch (IOException e) {
				e.printStackTrace();
			}
    		if (notifiable != null){
    			notifiable.update();
    		}    		

    	}
    }
   
}