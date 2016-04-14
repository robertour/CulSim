package simulator.control.events;

import simulator.control.Simulation;

/**
 * When a probabilistic distribution is used, this event remove some
 * institutions traits using the probability that corresponds to the event in
 * the coordinate. When a deterministic distribution is used, there is no
 * difference between this event and the RemoveInstitutionsContent (all the
 * traits are removed)
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 */
public class RemoveInstitutionsPartialContent extends Event {
	private static final long serialVersionUID = 6865761072879604679L;

	/**
	 * Constructor of the remove institutions partial content.
	 * 
	 * @param d
	 *            represents the distribution of the event in the grid, it could
	 *            be a deterministic distribution (e.g. a Newmann's
	 *            neighborhood) or a probilistic distribution (e.g. uniform
	 *            distribution, in which an event could occur with equal
	 *            probability in each cell)
	 */
	public RemoveInstitutionsPartialContent(Distribution d) {
		super(d);
	}

	@Override
	public void trigger(int r, int c, double p, Simulation simulation) {
		simulation.remove_partial_institution_content(r, c, p);
	}

	@Override
	public String toString() {
		return "Partial Content: " + super.toString();
	}

}