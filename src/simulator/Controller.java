package simulator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import simulator.old.Flache1;
import simulator.old.Flache1Ulloa;
import simulator.old.Flache2;
import simulator.old.Flache3;
import simulator.old.NHUlloa10;
import simulator.old.NHUlloa1D;
import simulator.old.NHUlloa1E;
import simulator.old.NHUlloa1EM;
import simulator.old.NHUlloa2;
import simulator.old.NHUlloaA2;
import simulator.old.NHUlloaA2M;
import simulator.old.NHUlloaB1;
import simulator.old.NHUlloaB1M;
import simulator.old.Ulloa1;
import simulator.old.Ulloa10;
import simulator.old.Ulloa10Alpha;
import simulator.old.Ulloa1B;
import simulator.old.Ulloa1C;
import simulator.old.Ulloa1D;
import simulator.old.Ulloa1E;
import simulator.old.Ulloa1EAlpha;
import simulator.old.Ulloa1EM;
import simulator.old.Ulloa2;
import simulator.old.Ulloa2Alpha;
import simulator.old.Ulloa2EvenSimpler;
import simulator.old.Ulloa2Simpler;
import simulator.old.Ulloa2SimplerAlpha;
import simulator.old.Ulloa3;
import simulator.old.Ulloa4;
import simulator.old.Ulloa5;
import simulator.old.Ulloa6;
import simulator.old.Ulloa7;
import simulator.old.Ulloa8;
import simulator.old.Ulloa9;
import simulator.old.UlloaA1;
import simulator.old.UlloaA2;
import simulator.old.UlloaA2Alpha;
import simulator.old.UlloaA2M;
import simulator.old.UlloaB1;
import simulator.old.UlloaB1Alpha;
import simulator.old.UlloaB1M;
import simulator.old.UlloaFlache1;
import simulator.old.UlloaFlache2;
import simulator.paper.U_H;
import simulator.paper.U_H_D;
 
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
    	
    	// This is used to randomize the experiment.
    	Random rand = new Random();
    	
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
					case "U_H":	simulation = new U_H();	break;
					case "U_H_D":	simulation = new U_H_D();	break;
		    		case "FLACHE1ULLOA":	simulation = new Flache1Ulloa();	break;
					case "ULLOA2EVENSIMPLER":	simulation = new Ulloa2EvenSimpler();	break;
		    		case "ULLOA2SIMPLERALPHA":	simulation = new Ulloa2SimplerAlpha();	break;
        			case "ULLOA2SIMPLER":	simulation = new Ulloa2Simpler();	break;
        			case "ULLOA10ALPHA":	simulation = new Ulloa10Alpha();	break;
        			case "ULLOAB1ALPHA":	simulation = new UlloaB1Alpha();	break;
        			case "ULLOA1EALPHA":	simulation = new Ulloa1EAlpha();	break;
    				case "ULLOAA2ALPHA":	simulation = new UlloaA2Alpha();	break;
        			case "ULLOA2ALPHA":	simulation = new Ulloa2Alpha();	break;
        			case "NHULLOA1EM":	simulation = new NHUlloa1EM();	break;
        			case "NHULLOAB1M":	simulation = new NHUlloaB1M();	break;
	    			case "NHULLOAA2M":	simulation = new NHUlloaA2M();	break;
	    			case "NHULLOA10":	simulation = new NHUlloa10();	break;
	    			case "ULLOA1EM":	simulation = new Ulloa1EM();	break;
	    			case "ULLOAA2M":	simulation = new UlloaA2M();	break;
	    			case "ULLOAB1M":	simulation = new UlloaB1M();	break;	    			
	        		case "NHULLOA1D":	simulation = new NHUlloa1D();	break;
	    			case "NHULLOA1E":	simulation = new NHUlloa1E();	break;
	    			case "NHULLOAB1":	simulation = new NHUlloaB1();	break;
	    			case "NHULLOA2":	simulation = new NHUlloa2();	break;
	    			case "NHULLOAA2":	simulation = new NHUlloaA2();	break;	    			
        			case "ULLOAB1":	simulation = new UlloaB1();	break;
        			case "ULLOAA1":	simulation = new UlloaA1();	break;
        			case "ULLOAA2":	simulation = new UlloaA2();	break;
          			case "ULLOA1B":	simulation = new Ulloa1B();	break;
        			case "ULLOA1C":	simulation = new Ulloa1C();	break;
        			case "ULLOA1D":	simulation = new Ulloa1D();	break;
        			case "ULLOA1E":	simulation = new Ulloa1E();	break;
        			case "ULLOA1":	simulation = new Ulloa1();	break;
        			case "ULLOA2":	simulation = new Ulloa2();	break;
        			case "ULLOA3":	simulation = new Ulloa3();	break;
        			case "ULLOA4":	simulation = new Ulloa4();	break;
        			case "ULLOA5":	simulation = new Ulloa5();	break;
        			case "ULLOA6":	simulation = new Ulloa6();	break;
        			case "ULLOA7":	simulation = new Ulloa7();	break;
        			case "ULLOA8":	simulation = new Ulloa8();	break;
        			case "ULLOA9":	simulation = new Ulloa9();	break;
        			case "ULLOA10":	simulation = new Ulloa10();	break;
        			case "AXELROD":	simulation = new Axelrod();	break;
        			case "FLACHE1":	simulation = new Flache1();	break;
        			case "FLACHE2":	simulation = new Flache2();	break;
	    	        case "FLACHE3":	simulation = new Flache3();	break;
	    	        case "ULLOA_FLACHE1":	simulation = new UlloaFlache1();	break;
	    	        case "ULLOA_FLACHE2":	simulation = new UlloaFlache2();	break;
        		}
	        	simulation.ITERATIONS = Integer.parseInt(scanner.next());
	        	simulation.CHECKPOINT = Integer.parseInt(scanner.next());
	        	simulation.BUFFERED_SIZE = Integer.parseInt(scanner.next());
	        	simulation.ROWS = Integer.parseInt(scanner.next());
	        	simulation.COLS = Integer.parseInt(scanner.next());
	        	simulation.FEATURES = Integer.parseInt(scanner.next());
	        	simulation.TRAITS = Integer.parseInt(scanner.next());
	        	simulation.RADIUS = Integer.parseInt(scanner.next());
	        	simulation.ALPHA = Float.parseFloat(scanner.next());
	        	simulation.MUTATION = Float.parseFloat(scanner.next());
	        	simulation.SELECTION_ERROR = Float.parseFloat(scanner.next());
	        	tasks.add(rand.nextInt(tasks.size()+1), simulation);
	        	
	        	// Generate tasks per repetitions
	        	for (int r = 1; r < repetitions; r++) {
		        	tasks.add( rand.nextInt(tasks.size()+1), simulation.clone());
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
        writer.write(Flache3.header());
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
    	
    	int id = 0;
    	for(Simulation w : tasks) {
    		w.IDENTIFIER = id++;
    		exec.submit(w);
    	} 
    
    	CulturalSimulator.TA_OUTPUT.append("All Tasks Submitted\n");
    	
    	exec.shutdown();
    	
    	(new SimulationExecuter()).start();
        
    }
    
//    public void start() 
//    {
//    	for(Simulation w : tasks) {
//    		w.call();
//    	} 
//    }
    
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