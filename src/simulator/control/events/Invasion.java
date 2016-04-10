package simulator.control.events;

import simulator.control.Simulation;

/**
 * This event introduces foreign agents (invaders) in the simulation. An invader
 * has a cultural vector with the invader traits (represented by the same value
 * of TRAITS, and belongs to the invader institution that also has a cultural
 * vector with invader traits. The invader take a position of a currently existent
 * agent (who is simply replaced by it)
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 */
public class Invasion extends Event {

	private static final long serialVersionUID = 8805514313035882018L;

	/**
	 * The row position that the agent is going to take
	 */
	private int i_r = -9998;
	/**
	 * The column position that the agent is going to take
	 */
	private int i_c = -9999;

	/**
	 * Constructor of the invasion event.
	 * @param d represents the distribution of the event in the grid, it could be
	 * a deterministic distribution (e.g. a Newmann's neighborhood) or a 
	 * probilistic distribution (e.g. uniform distribution, in which an event
	 * could occur with equal probability in each cell)
	 */
	public Invasion(Distribution d) {
		super(d);
	}

	@Override
	public void execute(Simulation simulation) {
		i_r = distribution.getRow(simulation);
		i_c = distribution.getCol(simulation);
		simulation.pre_invasion(i_r, i_c);
		super.execute(simulation);
	}
	
	@Override
	public void trigger(int r, int c, double p, Simulation simulation){
		if (simulation.getRand().nextDouble() < p){
			simulation.invade(r, c, i_r, i_c);
		}
	}

	@Override
	public String toString() {
		return "Invasion: " + super.toString();
	}

}
