package simulator.control;

/**
 * Destroy institution content event
 * 
 * @author tico
 *
 */
public class DestroyInstitutionsContent extends Event {
	
	private double prob = -99;

	public DestroyInstitutionsContent(double p) {
		prob = p;
	}

	@Override
	public void execute(Simulation simulation) {
		simulation.destroy_institutions_content(prob);
	}
	
	public String toString() {
		return "Destroy institutions content (" + prob + ")";
	}

}