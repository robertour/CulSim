package simulator.control;

/**
 * Destroy institution structure event
 * 
 * @author tico
 *
 */
public class DestroyInstitutionsStructure extends Event {
	
	private double prob = -99;

	public DestroyInstitutionsStructure(double p) {
		prob = p;
	}

	@Override
	public void execute(Simulation simulation) {
		simulation.destroy_institutions_structure(prob);
	}
	
	public String toString() {
		return "Destroy institutions structure (" + prob + ")";
	}

}