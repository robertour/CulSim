package simulator.control;

/**
 * Genocide event
 * 
 * @author tico
 *
 */
public class Genocide extends Event {
	private static final long serialVersionUID = 3408040472593937200L;

	public Genocide(Distribution d) {
		super(d);
	}

	@Override
	public void trigger(int r, int c, double p, Simulation simulation){
		if (simulation.rand.nextDouble() < p){
			simulation.kill_individual(r, c);
		}
	}
	
	public String toString() {
		return "Genocide: " + super.toString();
	}

}