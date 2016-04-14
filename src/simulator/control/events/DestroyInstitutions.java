package simulator.control.events;

import simulator.control.Simulation;

/**
 * This event removes all the agents that belongs to an institution. This agents
 * became stateless, i.e. they are assigned their own empty institutions (i.e.
 * institutions that have no traits - represented by -1)
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 */
public class DestroyInstitutions extends Event {
	private static final long serialVersionUID = -9186276200796543594L;

	/**
	 * Constructor of the destroy instituions event.
	 * 
	 * @param d
	 *            represents the distribution of the event in the grid, it could
	 *            be a deterministic distribution (e.g. a Neumann's
	 *            neighborhood) or a probilistic distribution (e.g. uniform
	 *            distribution, in which an event could occur with equal
	 *            probability in each cell)
	 */
	public DestroyInstitutions(Distribution d) {
		super(d);
	}

	@Override
	public void trigger(int r, int c, double p, Simulation simulation) {
		if (simulation.getRand().nextDouble() < p) {
			simulation.destoy_institution(r, c);
		}
	}

	@Override
	public String toString() {
		return "Structure: " + super.toString();
	}

}