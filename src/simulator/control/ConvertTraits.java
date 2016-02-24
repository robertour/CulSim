package simulator.control;

/**
 * Convert some traits in institutions
 * 
 * @author tico
 *
 */
public class ConvertTraits extends Event {
	
	private double prob = -99;

	public ConvertTraits(double p) {
		prob = p;
	}

	@Override
	public void execute(Simulation simulation) {
		simulation.institutional_trait_conversion(prob);
	}
	
	public String toString() {
		return "Convert institutions' traits (" + prob + ")";
	}

}