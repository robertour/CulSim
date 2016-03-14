package simulator.control.events;

import simulator.control.Simulation;

/**
 * Destroy institution content event
 * 
 * @author tico
 *
 */
public class RemoveInstitutionsPartialContent extends Event {
	private static final long serialVersionUID = 6865761072879604679L;

	public RemoveInstitutionsPartialContent(Distribution d) {
		super(d);
	}

	@Override
	public void trigger(int r, int c, double p, Simulation simulation) {
		simulation.remove_partial_institution_content(r, c, p);
	}
	
	public String toString() {
		return "Partial Content: " + super.toString();
	}


}