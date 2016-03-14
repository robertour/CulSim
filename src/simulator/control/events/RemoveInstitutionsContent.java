package simulator.control.events;

import simulator.control.Simulation;

/**
 * Destroy institution content event
 * 
 * @author tico
 *
 */
public class RemoveInstitutionsContent extends Event {
	private static final long serialVersionUID = 6865761072879604679L;

	public RemoveInstitutionsContent(Distribution d) {
		super(d);
	}

	@Override
	public void trigger(int r, int c, double p, Simulation simulation) {
		if (simulation.getRand().nextDouble() < p){
			simulation.remove_full_institution_content(r, c);
		}
	}
	
	public String toString() {
		return "Full Content: " + super.toString();
	}


}