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
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import simulator.CulturalParameters;
import simulator.CulturalSimulator;
import simulator.Notifiable;
import simulator.ParametersDialog;
import simulator.control.events.Event;
import simulator.destruction.Ulloa;

 
/**
 * The controller of the simulations handles the simulation. It creates the thread
 * for the simulation, and provide methods to start, stop, suspend or resume the 
 * simulation, load and save states of the simulation, update to and from the GUI
 * and add events to the simulation
 * 
 * @author Roberto Ulloa
 * @version 1.0, March 2016
 */
public class ControllerSingle extends Controller
{

	/**
	 * The area to append the results (log)
	 */
	protected static Printable log = null;
	
	/**
	 * Instance to the current simulation when is in single mode
	 */
	private Simulation simulation = null;
	
	/**
	 * Indicate if the simulation have been saved
	 */
	private boolean is_saved = true;
	
	/**
	 * Constructor of the controller that handles one simulation (thread) and the user 
	 * interface (CulturalParameters).
	 * @param printable the object which will be in charge of displaying the messages 
	 * from the simulation and the controller
	 * @param notifiable the object that will be notified when the simulation is finished
	 */
	public ControllerSingle(Printable printable, Notifiable notifiable) {
		super (notifiable);
		log = printable;
	}
	
		
	/** 
	 * Initialize a new simulation. 
	 */
	public void initialize_simulation(){
		if (simulation != null){
			simulation.clean();
		}
		simulation = new Ulloa();
		is_saved = true;
		CulturalSimulator.clean_belief_spaces();
		if (simulation.iteration > 0){
			simulation.save_state();
			simulation.results();
			simulation.update_gui();
		}
		restore_parameters_to_interface();
	}
	
	
	/**
	 * Save the simulation in a file
	 * @param simfile the file to save the simulation on
	 */
	public void save_simulation(String simfile){
		if (simulation != null){

			try {
				
				FileOutputStream fos = new FileOutputStream(simfile);
				GZIPOutputStream gos = new GZIPOutputStream(fos);
				ObjectOutputStream oos = new ObjectOutputStream (gos);		
				oos.writeObject(simulation);
				is_saved = true;
				oos.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Return if the simulation has been saved in a file.
	 * @return is_saved whether the simulation has been saved or not
	 */
	public boolean is_saved() {
		return is_saved;
	}
	
	/**
	 * Load a simulation from a file
	 * @param simfile the file where the simulation is saved
	 * @throws IOException
	 */
	public void load_simulation(String simfile) throws IOException{
		if (simulation != null){
			simulation.clean(); // clean the memory
		}
		try {

			FileInputStream fis = new FileInputStream(simfile);
			GZIPInputStream gis = new GZIPInputStream(fis);
			ObjectInputStream ois = new ObjectInputStream (gis);				
			
			simulation = (Simulation) ois.readObject();
			
			is_saved = true;
			ois.close();
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
	 * Load parameters of a simulation file, it just reads the parameters
	 * of the simulation stored in the file but not the state (see @link #load_simulation)
	 * @param simfile the file that contains the simulation
	 */
	public void load_parameters(String simfile){
		if (simulation != null){
			simulation.clean(); // clean the memory
		}
		try {
			FileInputStream fis = new FileInputStream(simfile);
			GZIPInputStream gis = new GZIPInputStream(fis);
			ObjectInputStream ois = new ObjectInputStream (gis);			
			simulation = ((Simulation) ois.readObject()).clone();
			is_saved = true;
			ois.close();
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
	 * Restart the simulation removing references so the garbage collector
	 * pick up the objects
	 */
	public void restart_simulation(){
		if (simulation != null){
			simulation.starter.clean();
			simulation.clean();
			simulation = simulation.clone();
			CulturalSimulator.clean_belief_spaces();
			is_saved = true;
		}
	}
	
	/**
	 * Save the current state of the simulation to reload from it
	 */
	public void save_state(){
		if (simulation != null){
			simulation.save_state();
			simulation.results();
			simulation.update_gui();
		}
	}
	
	/**
	 * Restart the simulation removing references so the garbage collector
	 * pick up the objects
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
	 * Restore the parameters of the simulation to the interface
	 */
	public void restore_parameters_to_interface(){
		String the_class = simulation.getClass().getSimpleName()+ " (" + simulation.getClass().getPackage().getName()+")";
		CulturalParameters.classSelector.setSelectedItem(the_class);
		CulturalParameters.cb_random_initialization.setSelected(simulation.RANDOM_INITIALIZATION);
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
    	
    	ParametersDialog.iterations = simulation.ITERATIONS;
    	ParametersDialog.checkpoints = simulation.CHECKPOINT;
    	ParametersDialog.selection_error = simulation.SELECTION_ERROR;
    	ParametersDialog.mutation = simulation.MUTATION;
    	ParametersDialog.influence = simulation.ALPHA;
    	ParametersDialog.loyalty = simulation.ALPHA_PRIME;
    	ParametersDialog.democracy = simulation.FREQ_DEM;
    	ParametersDialog.propaganda = simulation.FREQ_PROP;
    	CulturalSimulator.set_speed(simulation.CHECKPOINT);
    	
    	CulturalSimulator.l_start_identification.setText("S: " + simulation.get_identification());
    	CulturalSimulator.l_current_identification.setText("C: " + simulation.get_identification());
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
			simulation.RANDOM_INITIALIZATION = CulturalParameters.cb_random_initialization.isSelected();
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
	
	@Override
    public void play() {

    	// Load static variables that the simulation is going to access
    	IS_BATCH = false;
    	is_saved = false;
    		    	
    	// This is a pool of threads of the size of the cores of the computer
    	exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    	
    	simulation.IDENTIFIER = -999;
    	simulation.log = log;
    	simulation.results_dir = results_dir;
    	exec.submit(simulation);

    	log.print(simulation.IDENTIFIER, "Task submitted\n");
    	
    	exec.shutdown();
    	
    	(new SimulationExecuter()).start();
    }
    
	@Override
    public void cancel() {
    	simulation.cancel();
    }
    
    @Override
    public void suspend() {
    	simulation.suspend();
    }
    
    @Override
    public void resume(){
    	simulation.resume();
    }
	
    /**
     * Add catastrophic events to the simulation
     * @param events the list of events that will be applied to the simulation
     */
	public void add_events(ArrayList<Event> events){
		if (events.size() > 0){
			simulation.events(events);
		}
	}    
	
	@Override
    public void write_results() throws IOException {
    	// Write the results to the file
    	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
              new FileOutputStream(results_dir + identifier + ".csv"), "utf-8"));
        writer.write(Simulation.header());
       	writer.write(simulation.get_results());
        writer.close();    	
    }
    
    /**
     * Handles the general execution of the simulation, it notifies the interface
     * the finalization of the simulation and its possible interruptions
     * 
     * @author Roberto Ulloa
     * @version 1.0, March 2016
     */
    private class SimulationExecuter extends Thread {
    	
    	/**
    	 * Runs and wait for the simulation to finish. Display errors and messages 
    	 * in the the printable, and notify the end of the execution.
    	 */
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
	    	
	    	if (notifiable != null){
    			notifiable.update();
    		}   

    	}
    }
   
}