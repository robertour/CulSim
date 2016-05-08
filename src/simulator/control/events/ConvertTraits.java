package simulator.control.events;

import simulator.control.Simulation;

/**
 * When a probabilistic distribution is used, this event convert some
 * institutions traits towards foreigner traits using the probability that
 * corresponds to the institution. When a deterministic distribution is used,
 * there is no difference between this event and the ConvertInstitutions (all
 * the traits are converted towards the foreigner traits)
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 */
public class ConvertTraits extends Event {
	private static final long serialVersionUID = 5017064892401274812L;

	/**
	 * Constructor of the convert institutional traits event.
	 * @param d represents the distribution of the event in the grid, it could be
	 * a deterministic distribution (e.g. a Neumann's neighborhood) or a 
	 * probilistic distribution (e.g. uniform distribution, in which an event
	 * could occur with equal probability in each cell)
	 */
	public ConvertTraits(Distribution d) {
		super(d);
	}
	
	@Override
	public void trigger(int r, int c, double p, Simulation simulation) {
		simulation.convert_partial_institution(r, c, p, distribution.rand);
	}
	
	@Override
	public String toString() {
		return "Convert traits: " + super.toString();
	}
		
}