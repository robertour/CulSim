package simulator.gui;

/**
 * Any class that implements this method is meant to be updated by changes
 * in another part of the simulation or interface.
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 */
public interface Notifiable {
	
	/**
	 * Updates any instance that implements this interface
	 */
	public abstract void update();
}
