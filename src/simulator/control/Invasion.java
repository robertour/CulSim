package simulator.control;

/**
 * Invasion event
 * 
 * @author tico
 *
 */
public class Invasion extends Event {
	
	private int radius = -99;

	public Invasion(int r) {
		radius = r;
	}

	@Override
	public void execute(Simulation simulation) {
		simulation.invasion(radius);
	}
	
	public String toString() {
		return "Invasion (" + radius + ")";
	}

}
