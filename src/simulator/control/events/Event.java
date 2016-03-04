package simulator.control.events;

import java.io.Serializable;

import simulator.control.Simulation;

public abstract class Event implements Serializable{
	private static final long serialVersionUID = 8176633784954763258L;
	
	protected Distribution distribution = null;
	
	public Event(Distribution d) {
		distribution = d;
	}

	public void execute(Simulation s) {
		if (distribution.getType() == Distribution.UNIFORM) {
			uniform_event(distribution.getProbability(), s);
		} else if (distribution.getType() == Distribution.NORMAL) {
			normal_event(distribution.getDiagonalNormalDistribution(s),	distribution.getRow(s),distribution.getCol(s),s);			
		} else if (distribution.getType() == Distribution.NEWMANN) {
			newman_event(distribution.getRow(s), distribution.getCol(s), distribution.getRadious(), s);			
		}
		s.log.print(s.IDENTIFIER, "Event executed: " + this + "\n");
		
	}
	
	public abstract void trigger(int r, int c, double p, Simulation simulation);
	
	public String toString(){
		return distribution.toString();
	}
	
	
	/**
	 * An event is normally distributed in the population
	 * 
	 * @param probability
	 */
	private void normal_event(Distribution.NormalProbabilityDensityFuntion nd, 
							int x, int y, Simulation s){
		
		double max = nd.density(0);
			
		for (int r = 0; r < s.ROWS; r++) {
			for (int c = 0; c < s.COLS; c++) {
				trigger(r, c,nd.density(Math.abs(x-r)+Math.abs(y-c))/max, s);
			}
		}
	}
	
	/**
	 * An event is distributed in a Newmann's neighborhood of radius
	 * @param radius
	 */
	private void newman_event (int r, int c, int radius, Simulation s){
		
		trigger(r, c, 1.0, s);
		
		for (int i = 0; i <= radius; i++) {
			for (int j = 0; j <= radius; j++) {
				if ( j + i + 2 <= radius ) {
					if ( r + i + 1 < s.ROWS && c + j + 1 < s.COLS ) {
						trigger(r + i + 1, c + j + 1, 1.0, s);
					}
					if ( r - i - 1 >= 0 && c - j - 1 >= 0 ) {			
						trigger(r - i - 1, c - j - 1, 1.0, s);
					}
				}
				if ( j + i <= radius && ( j != 0 || i != 0 ) ) {
					if ( r - i >= 0 && c + j < s.COLS ) {
						trigger(r - i, c + j, 1.0, s);
					}
					if ( r + i < s.ROWS && c - j >= 0 ) {
						trigger(r + i, c - j, 1.0, s);
					}								
				}
			}
		}
	}
	
	
	
	/**
	 * An event is distributed with uniform probability
	 * 
	 * @param probability
	 */
	private void uniform_event(double probability, Simulation s){
		for (int r = 0; r < s.ROWS; r++) {
			for (int c = 0; c < s.COLS; c++) {
				trigger(r, c, probability, s);
			}
		}
	}

}
