package simulator.control.events;

import simulator.control.Simulation;

/**
 * Convert some traits in institutions
 * 
 * @author tico
 *
 */
public class ConvertTraits extends Event {
	private static final long serialVersionUID = 5017064892401274812L;

	public ConvertTraits(Distribution d) {
		super(d);
	}
	
	@Override
	public void trigger(int r, int c, double p, Simulation simulation) {
		simulation.convert_partial_institution(r, c, p);
	}
	
	public String toString() {
		return "Convert traits: " + super.toString();
	}
		
}