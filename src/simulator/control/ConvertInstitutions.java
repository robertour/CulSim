package simulator.control;

/**
 * Convert all traits in some institutions
 * 
 * @author tico
 *
 */
public class ConvertInstitutions extends Event {
	
	private double prob = -99;

	public ConvertInstitutions(double p) {
		prob = p;
	}

	@Override
	public void execute(Simulation simulation) {
		simulation.institutional_conversion(prob);
	}
	
	public String toString() {
		return "Convert institutions (" + prob + ")";
	}

}