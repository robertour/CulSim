package simulator.control.events;

import simulator.control.Simulation;

/**
 * Convert all traits in some institutions
 * 
 * @author tico
 *
 */
public class ConvertInstitutions extends Event {
	private static final long serialVersionUID = -1746540685442267804L;

	public ConvertInstitutions(Distribution d) {
		super(d);
	}

	
	@Override
	public void trigger(int r, int c, double p, Simulation simulation) {
		if (simulation.getRand().nextDouble() < p){
			simulation.convert_institution(r, c);
		}
	}

	
	public String toString() {
		return "Convert Institutions: " + super.toString();
	}
	
}