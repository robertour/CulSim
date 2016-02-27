package simulator.control.events;

import simulator.control.Simulation;

/**
 * Invasion event
 * 
 * @author tico
 *
 */
public class Invasion extends Event {

	private static final long serialVersionUID = 8805514313035882018L;

	private int institution = -9999;

	public Invasion(Distribution d) {
		super(d);
	}

	public void execute(Simulation simulation) {
		institution = simulation.pre_invasion(distribution.getRow(simulation.ROWS, simulation.getRand()), 
											distribution.getRow(simulation.ROWS, simulation.getRand()));
		super.execute(simulation);
	}
	
	@Override
	public void trigger(int r, int c, double p, Simulation simulation){
		if (simulation.getRand().nextDouble() < p){
			simulation.invade(r, c, institution);
		}
	}
	

	public String toString() {
		return "Invasion: " + super.toString();
	}

}
