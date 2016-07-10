package simulator.control.events;

import simulator.control.Simulation;
import simulator.control.events.distributions.Distribution;

/**
 * This event introduces foreign agents (settlers) in the simulation that
 * carries their own institution. A settler has a cultural vector with the
 * foreign traits (represented by the same value of the variable TRAITS, and
 * belongs to the settler institution that also has a cultural vector with
 * settler traits. The settler take a position of a currently existent agent
 * (who is simply replaced by it)
 * 
 * @author Roberto Ulloa
 * @version 1.1, July 2016
 */
public class Settlement extends Event {

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
	 * Constructor of the Settlement event.
	 * 
	 * @param d
	 *            represents the distribution of the event in the grid, it could
	 *            be a deterministic distribution (e.g. a Neumann's
	 *            neighborhood) or a probabilistic distribution (e.g. uniform
	 *            distribution, in which an event could occur with equal
	 *            probability in each cell)
	 */
	public Settlement(Distribution d) {
		super(d);
	}

	@Override
	protected void pre_execute(Simulation simulation) {
		super.pre_execute(simulation);
		i_r = distribution.getRow(simulation);
		i_c = distribution.getCol(simulation);
		simulation.pre_settlement(i_r, i_c);
	}

	@Override
	public void trigger(int r, int c, double p, Simulation simulation) {
		if (distribution.getRand().nextDouble() < p) {
			simulation.settle(r, c, i_r, i_c);
		}
	}

	@Override
	public String toString() {
		return "Settlement: " + super.toString();
	}

}
