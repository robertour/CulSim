package axelrod;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
 
/**
 * The controller of the simulations handles the simulation. It creates the tasks
 * and provide an interface for the user to interrupt, suspend or resume the 
 * simulation.
 * 
 * @author tico
 */
public class Controller
{

	// Keep the tasks in a list. Don't start until the entire file is read.
	private ArrayList<Simulation> tasks = null;
	
	// References the Executor service that handles the threads
	private ExecutorService exec = null;
	
	/**
	 * Open the files and creates the tasks for the experiments
	 * @throws FileNotFoundException
	 */
    public void load_tasks() throws FileNotFoundException {
    	
    	Simulation.RUN = 0;
    	
    	//Get scanner instance        
    	Scanner scanner = new Scanner(new File(CulturalSimulator.EXPERIMENTAL_FILE));
        
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
        			case "AXELROD":	simulation = new Axelrod();	break;
        			case "FLACHE_EXPERIMENT1":	simulation = new FlacheExperiment1();	break;
        			case "FLACHE_EXPERIMENT2":	simulation = new FlacheExperiment2();	break;
	    	        case "FLACHE_EXPERIMENT3":	simulation = new FlacheExperiment3();	break;
        		}
	        	simulation.ITERATIONS = Integer.parseInt(scanner.next());
	        	simulation.CHECKPOINT = Integer.parseInt(scanner.next());
	        	simulation.BUFFERED_SIZE = Integer.parseInt(scanner.next());
	        	simulation.ROWS = Integer.parseInt(scanner.next());
	        	simulation.COLS = Integer.parseInt(scanner.next());
	        	simulation.FEATURES = Integer.parseInt(scanner.next());
	        	simulation.TRAITS = Integer.parseInt(scanner.next());
	        	simulation.RADIUS = Integer.parseInt(scanner.next());
	        	simulation.MUTATION = Float.parseFloat(scanner.next());
	        	simulation.SELECTION_ERROR = Float.parseFloat(scanner.next());
	        	tasks.add(simulation);
	        	
	        	// Generate tasks per repetitions
	        	for (int r = 1; r < repetitions; r++) {
		        	tasks.add(simulation.clone());
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
    	// Write the results to the file
    	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
              new FileOutputStream(CulturalSimulator.RESULTS_DIR + "results.csv"), "utf-8"));
        writer.write(FlacheExperiment3.header());
        for(Simulation w : tasks) {
			writer.write(w.get_results());
        }
        writer.close();    	
    }
    
    /**
     * Load the file and starts the simulation
     * @throws FileNotFoundException
     */
    public void start() 
    {
    	// This is a pool of threads of the size of the cores of the computer
    	exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    	
    	for(Simulation w : tasks) {
    		exec.submit(w);
    	} 
    
    	CulturalSimulator.TA_OUTPUT.append("All Tasks Submitted\n");
    	
    	exec.shutdown();
    	
    	(new SimulationExecuter()).start();
        
    }
    
    /** 
     * Cancel all threads
     * @throws IOException
     */
    public void cancel_all() {
    	for(Simulation w : tasks) {
    		w.cancel();
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
	    	CulturalSimulator.TA_OUTPUT.append("Simulation Executor Started\n");
    		try {
				exec. awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
				CulturalSimulator.TA_OUTPUT.append("All the experiments where finished succesfully\n");
			} catch (InterruptedException e) {
				CulturalSimulator.TA_OUTPUT.append("Simulation interrupted\n");
			}
	    	
	    	try {
				write_results();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
   
}