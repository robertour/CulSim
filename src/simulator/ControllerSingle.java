package simulator;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
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
public class ControllerSingle extends Controller
{


	/**
	 * References the Executor service that handles the threads
	 */
	private ExecutorService exec = null;
	
	/**
	 * Instance to the current simulation when is in single mode
	 */
	private Simulation simulation = null;
	
	/**
	 * Indicate if the simulation have been saved
	 */
	public boolean is_saved = true;
	
	public int get_iteration(){
		return simulation.iteration;
	}
	
	
	/** 
	 * Save the simulation object
	 */
	public void save_simulation(String simfile){
		if (simulation != null){
			ObjectOutputStream write;
			try {
				write = new ObjectOutputStream (new FileOutputStream(simfile));				
				write.writeObject(simulation);
				is_saved = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** 
	 * Load a simulation object
	 */
	public void load_simulation(String simfile){
		if (simulation != null){
			simulation.clean(); // clean the memory
		}
		try {
			ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(simfile));
			simulation = (Simulation) inFile.readObject();
			is_saved = true;
			inFile.close();
			if (simulation.iteration > 0){
				simulation.update_gui();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		restore_parameters_to_interface();
	}
	
	
	/** 
	 * Load a parameters of a simulation object
	 */
	public void load_parameters(String simfile){
		if (simulation != null){
			simulation.clean(); // clean the memory
		}
		try {
			ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(simfile));
			simulation = ((Simulation) inFile.readObject()).clone();
			is_saved = true;
			inFile.close();
			CulturalSimulator.clean_belief_spaces();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		restore_parameters_to_interface();
	}
	
	/**
	 * Restore the parameters of the simulation to the interface
	 */
	public void restore_parameters_to_interface(){
		String the_class = simulation.getClass().getSimpleName()+ " (" + simulation.getClass().getPackage().getName()+")";
		CulturalParameters.classSelector.setSelectedItem(the_class);
		CulturalParameters.sp_iterations.setValue(simulation.ITERATIONS);
    	CulturalParameters.sp_checkpoints.setValue(simulation.CHECKPOINT);
    	CulturalParameters.sp_buffer.setValue(simulation.BUFFERED_SIZE);
    	CulturalParameters.sp_rows.setValue(simulation.ROWS);
    	CulturalParameters.sp_cols.setValue(simulation.COLS);
    	CulturalParameters.sp_features.setValue(simulation.FEATURES);
    	CulturalParameters.sp_traits.setValue(simulation.TRAITS);
    	CulturalParameters.sp_radious.setValue(simulation.RADIUS);
    	CulturalParameters.sp_influence.setValue(simulation.ALPHA);
    	CulturalParameters.sp_loyalty.setValue(simulation.ALPHA_PRIME);
    	CulturalParameters.sp_democracy.setValue(simulation.FREQ_DEM);
    	CulturalParameters.sp_propaganda.setValue(simulation.FREQ_PROP);
    	CulturalParameters.sp_mutation.setValue(simulation.MUTATION);
    	CulturalParameters.sp_sel_error.setValue(simulation.SELECTION_ERROR);

	}
	
	/**
	 * Restart the simulation leaving no garbage behind
	 */
	public void restart_simulation(){
		if (simulation != null){
			simulation.clean();
			simulation = simulation.clone();	
			CulturalSimulator.clean_belief_spaces();
		}
	}
	
	/**
	 * Load the parameters from the interface
	 */
	public void load_parameters_from_interface(){
		if (simulation != null){
			simulation.clean();
		}
		
		int ind = CulturalParameters.classSelector.getSelectedIndex();
		try {
			simulation = (Simulation) CulturalParameters.classes.get(ind).newInstance();
			simulation.ITERATIONS = (int) CulturalParameters.sp_iterations.getValue();
	    	simulation.CHECKPOINT = (int) CulturalParameters.sp_checkpoints.getValue();
	    	simulation.BUFFERED_SIZE = (int) CulturalParameters.sp_buffer.getValue();
	    	simulation.ROWS = (int) CulturalParameters.sp_rows.getValue();
	    	simulation.COLS = (int) CulturalParameters.sp_cols.getValue();
	    	simulation.FEATURES = (int) CulturalParameters.sp_features.getValue();
	    	simulation.TRAITS = (int) CulturalParameters.sp_traits.getValue();;
	    	simulation.RADIUS = (int) CulturalParameters.sp_radious.getValue();;
	    	simulation.ALPHA = (float) CulturalParameters.sp_influence.getValue();
	    	simulation.ALPHA_PRIME = (float) CulturalParameters.sp_loyalty.getValue();
	    	simulation.FREQ_DEM =  (int) CulturalParameters.sp_democracy.getValue();;
	    	simulation.FREQ_PROP =  (int) CulturalParameters.sp_propaganda.getValue();;
	    	simulation.MUTATION = (float) CulturalParameters.sp_mutation.getValue();
	    	simulation.SELECTION_ERROR = (float) CulturalParameters.sp_sel_error.getValue();
	    	CulturalSimulator.clean_belief_spaces();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 
	 */
    public void play() 
    {
 
    	// Load static variables that the simulation is going to access
    	IS_BATCH = false;
    	TA_OUTPUT = CulturalSimulator.TA_OUTPUT;
    	RESULTS_DIR = CulturalSimulator.RESULTS_DIR;
    	is_saved = false;
    		    	
    	// This is a pool of threads of the size of the cores of the computer
    	exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    	
    	simulation.IDENTIFIER = -999;
    	exec.submit(simulation);

    	TA_OUTPUT.append("Task submitted\n");
    	
    	exec.shutdown();
    	
    	(new SimulationExecuter()).start();
        
    }
    
    /** 
     * Cancel the active thread (visualization mode)
     * @throws IOException
     */
    public void cancel() {
    	simulation.cancel();
    	
    }
    
    /**
     * Suspend/Pause the active thread (visualization mode)
     */
    public void suspend() {
    	simulation.suspend();
    }
    
    /**
     * Resume the active thread (visualization mode)
     */
    public void resume(){
    	simulation.resume();
    }
		
    /**
     * Destroy the simulation institution 
     **/
    public void destroy_institutions(){
    	simulation.setDestroy_institutions_content();
    	simulation.setDestroy_institutions_structure();
    	simulation.update_gui();    	
    }
    
    /**
     * Destroy the simulation institution 
     **/
    public void destroy_institutions_content(){
    	simulation.setDestroy_institutions_content();
    	simulation.update_gui();    	
    }
    
    /**
     * Destroy the simulation institution 
     **/
    public void destroy_institutions_structure(){
    	simulation.setDestroy_institutions_structure();
    	simulation.update_gui();    	
    }
    
    /**
     * Invading culture 
     **/
    public void invasion(int radius){
    	simulation.setInvasion(radius);
    	simulation.update_gui();
    }
    
    /**
     * Genocide kills the population 
     **/
    public void genocide(double prob){
    	simulation.setGenocide(prob);
    	simulation.update_gui();
    }
    
    public void institutional_conversion(double prob){
    	simulation.setInstitutional_conversion(prob);
    	simulation.update_gui();
    }
    
    public void institutional_trait_conversion(double prob){
    	simulation.setInstitutional_trait_conversion(prob);
    	simulation.update_gui();    	
    }

    
    /**
     * Write the final or intermediate (when interrupted) results
     * @throws IOException
     */
    public void write_results() throws IOException {
    	// Write the results to the file
    	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
              new FileOutputStream(RESULTS_DIR + "results.csv"), "utf-8"));
    	// TODO this is weird
        writer.write(Simulation.header());
       	writer.write(simulation.get_results());
        writer.close();    	
    }
    
    
    
    /**
     * Handles the finalization of the experiments and its possible interruptions
     * 
     * @author tico
     */
    private class SimulationExecuter extends Thread {
    	
    	public void run (){
	    	TA_OUTPUT.append("Simulation Executor Started\n");
    		try {
				exec. awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
				TA_OUTPUT.append("SUCCESS: No Errors Reported!\n");
			} catch (InterruptedException e) {
				TA_OUTPUT.append("Simulation interrupted\n");
			}
	    	
	    	try {
				write_results();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
   
}