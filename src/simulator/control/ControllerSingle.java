package simulator.control;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import simulator.CulturalParameters;
import simulator.CulturalSimulator;
import simulator.control.events.Event;
import simulator.destruction.Ulloa;

 
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
	 * The area to append the results (log)
	 */
	protected static Printable log = null;
	
	/**
	 * Directory to write results
	 */
	protected static String RESULTS_DIR = null;
	
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
	
	/**
	 * Constructor
	 * @param ta_output
	 */
	public ControllerSingle(Printable output) {
		log = output;
	}
		
	public void setRESULTS_DIR(String results_dir) {
		RESULTS_DIR = results_dir;
	}
	
	public int get_iteration(){
		return simulation.iteration;
	}
	
	/** 
	 * Call when there is no simulation assigned.
	 */
	public void initialize_simulation(){
		simulation = new Ulloa();
	}
	
	
	/** 
	 * Save the simulation object
	 */
	public void save_simulation(String simfile){
		if (simulation != null){
			try {
				ObjectOutputStream write = new ObjectOutputStream (new FileOutputStream(simfile));				
				write.writeObject(simulation);
				is_saved = true;
				write.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** 
	 * Load a simulation object
	 * @throws IOException 
	 */
	public void load_simulation(String simfile) throws IOException{
		if (simulation != null){
			simulation.clean(); // clean the memory
		}
		try {
			ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(simfile));
			simulation = (Simulation) inFile.readObject();
			is_saved = true;
			inFile.close();
			CulturalSimulator.clean_belief_spaces();
			if (simulation.iteration > 0){
				simulation.save_state();
				simulation.results();
				simulation.update_gui();
			}
			restore_parameters_to_interface();
		}  catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
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
    	
    	CulturalSimulator.sp_iterations.setValue(simulation.ITERATIONS);
    	CulturalSimulator.sp_checkpoints.setValue(simulation.CHECKPOINT);
    	CulturalSimulator.sp_selection_error.setValue(simulation.SELECTION_ERROR);
    	CulturalSimulator.sp_mutation.setValue(simulation.MUTATION);
    	CulturalSimulator.sp_influence.setValue(simulation.ALPHA);
    	CulturalSimulator.sp_loyalty.setValue(simulation.ALPHA_PRIME);
    	CulturalSimulator.sp_democracy.setValue(simulation.FREQ_DEM);
    	CulturalSimulator.sp_propaganda.setValue(simulation.FREQ_PROP);
    	CulturalSimulator.set_speed(simulation.CHECKPOINT);
    	
    	CulturalSimulator.l_start_identification.setText("S: " + simulation.get_identification());
    	CulturalSimulator.l_current_identification.setText("C: " + simulation.get_identification());
	}
	
	/**
	 * Restart the simulation leaving no garbage behind
	 */
	public void restart_simulation(){
		if (simulation != null){
			simulation.starter.clean();
			simulation.clean();
			simulation = simulation.clone();
			CulturalSimulator.clean_belief_spaces();
		}
	}
	
	/**
	 * Restart the simulation leaving no garbage behind
	 */
	public void reload_state(){
		if (simulation != null){
			simulation.clean();
			simulation = simulation.starter;
			simulation.save_state();
			CulturalSimulator.clean_belief_spaces();
			simulation.results();
			simulation.update_gui();
		}
	}
	
	/**
	 * Save the current state of the simulation to reload from it
	 */
	public void save_state(){
		if (simulation != null){
			simulation.save_state();
			simulation.results();
			//CulturalSimulator.clean_belief_spaces();
			simulation.update_gui();
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
	    	restore_parameters_to_interface();
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
    	is_saved = false;
    		    	
    	// This is a pool of threads of the size of the cores of the computer
    	exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    	
    	simulation.IDENTIFIER = -999;
    	exec.submit(simulation);

    	log.print(simulation.IDENTIFIER, "Task submitted\n");
    	
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
     * Add a catastrophic event to the simulation
     * @param e
     */
	public void add_event(Event e){
		simulation.event(e);
	}
	
    /**
     * Add a catastrophic events to the simulation
     * @param e
     */
	public void add_events(ArrayList<Event> events){
		if (events.size() > 0){
			simulation.events(events);
		}
	}

	
	/**
	 * Set the new parameters
	 */
    public void setParameters(){
    	simulation.setParameters();
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
	    	log.print(-1, "Simulation Executor Started\n");
    		try {
				exec. awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
				log.print(-1, "No errors reported by the thread execution\n");
			} catch (InterruptedException e) {
				log.print(-1, "Simulation interrupted\n");
			}

	    	try {
				write_results();
			} catch (IOException e) {
				e.printStackTrace();
			}

    	}
    }
   
}