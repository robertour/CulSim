package simulator.control.events;

import simulator.control.Simulation;
import simulator.control.events.distributions.Distribution;

/**
 * This event remove the institutions full content, i.e. all the traits in the
 * cultural vector (represented by -1 after being removed).
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 */
public class RemoveInstitutionsContent extends Event {
	private static final long serialVersionUID = 6865761072879604679L;

	/**
	 * Constructor of the remove institutions full content.
	 * 
	 * @param d
	 *            represents the distribution of the event in the grid, it could
	 *            be a deterministic distribution (e.g. a Neumann's
	 *            neighborhood) or a probilistic distribution (e.g. uniform
	 *            distribution, in which an event could occur with equal
	 *            probability in each cell)
	 */
	public RemoveInstitutionsContent(Distribution d) {
		super(d);
	}

	@Override
	public void trigger(int r, int c, double p, Simulation simulation) {
		if (distribution.getRand().nextDouble() < p) {
			simulation.remove_full_institution_content(r, c);
		}
	}

	@Override
	public String toString() {
		return "Full Content: " + super.toString();
	}

}