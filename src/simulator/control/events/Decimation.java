package simulator.control.events;

import simulator.control.Simulation;
import simulator.control.events.distributions.Distribution;

/**
 * This event "kills" agents in the simulation. An agent is killed by removing
 * all its traits (represented by -2).
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 */
public class Decimation extends Event {
	private static final long serialVersionUID = 3408040472593937200L;

	/**
	 * Constructor of the Decimation event.
	 * 
	 * @param d
	 *            represents the distribution of the event in the grid, it could
	 *            be a deterministic distribution (e.g. a Neumann's
	 *            neighborhood) or a probilistic distribution (e.g. uniform
	 *            distribution, in which an event could occur with equal
	 *            probability in each cell)
	 */
	public Decimation(Distribution d) {
		super(d);
	}

	@Override
	public void trigger(int r, int c, double p, Simulation simulation) {
		if (distribution.getRand().nextDouble() < p) {
			simulation.kill_individual(r, c);
		}
	}

	@Override
	public String toString() {
		return "Decimation: " + super.toString();
	}

}