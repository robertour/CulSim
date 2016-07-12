package simulator.control.events;

import simulator.control.Simulation;
import simulator.control.events.distributions.Distribution;

/**
 * This event introduces foreign agents (immigrants) in the simulation.
 * Contrasting with settlers, immigrants don't have their own institutions. An
 * immigrant has a cultural vector with the foreign traits (represented by the
 * same value of the variable TRAITS. The immigrant take a position of a
 * currently existent agent (who is simply replaced by it)
 * 
 * @author Roberto Ulloa
 * @version 1.1, July 2016
 */
public class Immigration extends Event {

	private static final long serialVersionUID = 8805514313035882018L;

	/**
	 * Constructor of the Immigration event.
	 * 
	 * @param d
	 *            represents the distribution of the event in the grid, it could
	 *            be a deterministic distribution (e.g. a Neumann's
	 *            neighborhood) or a probabilistic distribution (e.g. uniform
	 *            distribution, in which an event could occur with equal
	 *            probability in each cell)
	 */
	public Immigration(Distribution d) {
		super(d);
	}

	@Override
	public void trigger(int r, int c, double p, Simulation simulation) {
		if (distribution.getRand().nextDouble() < p) {
			simulation.immigrate(r, c);
		}
	}

	@Override
	public String toString() {
		return "Immigration: " + super.toString();
	}

}
