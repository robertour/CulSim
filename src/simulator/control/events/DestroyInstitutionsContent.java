package simulator.control.events;

import simulator.control.Simulation;

/**
 * Destroy institution content event
 * 
 * @author tico
 *
 */
public class DestroyInstitutionsContent extends Event {
	private static final long serialVersionUID = 6865761072879604679L;

	public DestroyInstitutionsContent(Distribution d) {
		super(d);
	}

	@Override
	public void trigger(int r, int c, double p, Simulation simulation) {
		if (simulation.getRand().nextDouble() < p){
			simulation.remove_institution_content(r * simulation.COLS + c);
		}
	}
	
	public String toString() {
		return "Full Content: " + super.toString();
	}


}