package simulator.control.events;

import java.io.Serializable;

import simulator.control.Simulation;

public abstract class Event implements Serializable{
	private static final long serialVersionUID = 8176633784954763258L;
	
	protected Distribution distribution = null;
	
	public Event(Distribution d) {
		distribution = d;
	}

	public void execute(Simulation simulation) {
		if (distribution.getType() == Distribution.UNIFORM) {
			simulation.uniform_event(distribution.getProbability(), this);
		} else if (distribution.getType() == Distribution.NORMAL) {
			simulation.normal_event(distribution.getDiagonalNormalDistribution(simulation),
					distribution.getRow(simulation),
					distribution.getCol(simulation),this);			
		} else if (distribution.getType() == Distribution.NEWMANN) {
			simulation.newman_event(distribution.getRow(simulation), 
									distribution.getCol(simulation),
									distribution.getRadious(), this);			
		}
		
	}
	
	public abstract void trigger(int r, int c, double p, Simulation simulation);
	
	public String toString(){
		return distribution.toString();
	}

}
