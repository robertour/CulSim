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

	private int i_r = -9998;
	private int i_c = -9999;

	public Invasion(Distribution d) {
		super(d);
	}

	public void execute(Simulation simulation) {
		i_r = distribution.getRow(simulation);
		i_c = distribution.getCol(simulation);
		simulation.pre_invasion(i_r, i_c);
		super.execute(simulation);
	}
	
	@Override
	public void trigger(int r, int c, double p, Simulation simulation){
		if (simulation.getRand().nextDouble() < p){
			simulation.invade(r, c, i_r, i_c);
		}
	}
	

	public String toString() {
		return "Invasion: " + super.toString();
	}

}
