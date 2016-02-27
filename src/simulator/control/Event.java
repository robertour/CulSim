package simulator.control;

import java.io.Serializable;

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
			simulation.event_normal(distribution.getRowNormalDistribution(simulation.ROWS, simulation.rand),
					distribution.getColumnNormalDistribution(simulation.COLS, simulation.rand),	this);			
		} else if (distribution.getType() == Distribution.NEWMANN) {
			simulation.newman_event(distribution.getRow(simulation.ROWS, simulation.rand), 
									distribution.getCol(simulation.COLS, simulation.rand),
									distribution.getRadious(), this);			
		}
		
	}
	
	public abstract void trigger(int r, int c, double p, Simulation simulation);
	
	public String toString(){
		return distribution.toString();
	}

}
