package simulator.control;

/**
 * Genocide event
 * 
 * @author tico
 *
 */
public class Genocide extends Event {
	
	private double prob = -99;

	public Genocide(double p) {
		prob = p;
	}

	@Override
	public void execute(Simulation simulation) {
		simulation.genocide(prob);
	}
	
	public String toString() {
		return "Genocide (" + prob + ")";
	}

}