package simulator.control.events;

import simulator.control.Simulation;

/**
 * An apostate agent is an agent that decides to voluntarily abandon the
 * institution they belong and become stateless. In terms of implementation,
 * they look for an institution that has no agents and joins it deleting all
 * cultural traits in the institution
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 */
public class Apostasy extends Event {
	private static final long serialVersionUID = -9186276200796543594L;

	/**
	 * Constructor of the apostasy event.
	 * 
	 * @param d
	 *            represents the distribution of the event in the grid, it could
	 *            be a deterministic distribution (e.g. a Neumann's
	 *            neighborhood) or a probilistic distribution (e.g. uniform
	 *            distribution, in which an event could occur with equal
	 *            probability in each cell)
	 */
	public Apostasy(Distribution d) {
		super(d);
	}

	@Override
	public void trigger(int r, int c, double p, Simulation simulation) {
		if (distribution.rand.nextDouble() < p) {
			simulation.apostasy(r, c);
		}
	}

	@Override
	public String toString() {
		return "Apostasy: " + super.toString();
	}

}