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

import simulator.destruction.Flache;
import simulator.Notifiable;
import simulator.control.events.Event;
import simulator.destruction.Axelrod;
import simulator.destruction.Flache2;
import simulator.destruction.Ulloa;
 
/**
 * This controller creates the tasks base on a CSV configuration file and it
 * can provide an interface for the user to interrupt, suspend or resume the 
 * simulation, or run in a console mode
 * 
 * @author tico
 */
public class ControllerBatch extends Controller
{

	/**
	 * The area to append the results (log)
	 */
	protected static Printable log = null;
	
	/**
	 * Keep a reference to the events that are going to be executed
	 */
	protected ArrayList<Event> events = new ArrayList<Event>();
	

	/**
	 * List of simulations taht need to be run
	 */
	protected ArrayList<Simulation> tasks = null;
	
	private Notifiable notifiable = null;
	
	public ControllerBatch(Printable output, Notifiable n) {
		log = output;
		this.notifiable = n;
	}

	/** 
	 * Load a simulation object
	 * @throws IOException 
	 */
	public Simulation load_simulation(String simfile){
		Simulation s = null;
		try {
			ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(simfile));
			s = (Simulation) inFile.readObject();
			inFile.close();
		}  catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * Open the files and creates the tasks for the experiments
	 * @throws FileNotFoundException
	 */
    public void load_tasks(ArrayList<String> sim_files, int repetitions) {
    	tasks = new ArrayList<Simulation>();

    	for (Iterator<String> iterator = sim_files.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			for (int j = 0; j < repetitions; j++) {
    			tasks.add(this.load_simulation(string));	
			}	
		}
    }
    
	/**
	 * Open the files and creates the tasks for the experiments
	 * @throws FileNotFoundException
	 */
    public void load_simulations_from_directory(ArrayList<String> sim_files, ArrayList<Event> events, int repetitions) {
    	tasks = new ArrayList<Simulation>();
    	IS_BATCH = true;
    	this.events = events;

    	for (Iterator<String> iterator = sim_files.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			for (int j = 0; j < repetitions; j++) {
				Simulation s = this.load_simulation(string);
				s.log = log;
				s.events(events);
    			tasks.add(s);	
			}	
		}
    }
	
	/**
	 * Open the files and creates the tasks for the experiments
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
         
        tasks = new ArrayList<Simulation>();
        
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
		        	tasks.add( rand.nextInt(tasks.size()+1), clone);
	        	}
	        	
	        	if (scanner.hasNextLine()) {
	        		scanner.nextLine();
	        	}
        	}
        }
         
        //Do not forget to close the scanner 
        scanner.close();
    }
    
    /**
     * Write the final or intermediate (when interrupted) results
     * @throws IOException
     */
    public void write_results() throws IOException {
    	
    	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
              new FileOutputStream(results_dir + identifier + ".csv"), "utf-8"));
    	BufferedWriter writer2 = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream((new File(results_dir)).getParent() + "/" + RESULTSET_DIR + 
                		(new File(results_dir)).getName() + "-" + identifier + ".csv"), "utf-8"));
        writer.write(Simulation.header());
        writer2.write(Simulation.header());
        for(Simulation w : tasks) {
			writer.write(w.get_results());
			writer2.write(w.get_results());
			
        }
        writer.close();    	
        writer2.close();
    }
    
    /**
     * Write the events that this experiment was subject to
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
    
    /**
     * Load the file and starts the simulation
     * @throws FileNotFoundException
     */
    protected void play(){
    	
    	IS_BATCH = true;
   	
    	// This is a pool of threads of the size of the cores of the computer
    	exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    	
    	int id = 0;
    	for(Simulation w : tasks) {
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
     * Cancel all threads
     * @throws IOException
     */
    public void clean_all() {
    	if (tasks != null) {
	    	for(Simulation w : tasks) {
	    		w.clean();
	    	} 
    	}
    }
    
    /** 
     * Cancel all threads
     * @throws IOException
     */
    public void cancel_all() {
    	if (tasks != null){
	    	for(Simulation w : tasks) {
	    		w.cancel();
	    	} 
	    	exec.shutdownNow();
    	}
    }
    
    /**
     * Suspend all threads
     */
    public void suspend_all() {
    	for(Simulation w : tasks) {
    		w.suspend();
    	} 
    }
    
    /**
     * Resume all threads
     */
    public void resume_all(){
    	for(Simulation w : tasks) {
    		w.resume();
    	}
    }
    
    /**
     * Handles the finalization of the experiments and its possible interruptions
     * 
     * @author tico
     */
    private class SimulationExecuter extends Thread {
    	
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