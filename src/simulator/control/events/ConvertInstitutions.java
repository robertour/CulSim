package simulator.control.events;

import simulator.control.Simulation;

/**
 * This event convert institutions. An institution is converted when all 
 * the traits are changed towards foreigner traits.   
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 */
public class ConvertInstitutions extends Event {
	private static final long serialVersionUID = -1746540685442267804L;

	/**
	 * Constructor of the convert institutions event.
	 * @param d represents the distribution of the event in the grid, it could be
	 * a deterministic distribution (e.g. a Newmann's neighborhood) or a 
	 * probilistic distribution (e.g. uniform distribution, in which an event
	 * could occur with equal probability in each cell)
	 */
	public ConvertInstitutions(Distribution d) {
		super(d);
	}
	
	@Override
	public void trigger(int r, int c, double p, Simulation simulation) {
		if (simulation.getRand().nextDouble() < p){
			simulation.convert_full_institution(r, c);
		}
	}

	@Override
	public String toString() {
		return "Convert Institutions: " + super.toString();
	}
	
}