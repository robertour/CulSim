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
		if (distribution == null){
			trigger(-1,-1,-1,s);
		}else if (distribution.getType() == Distribution.UNIFORM) {
			uniform_event(distribution.getProbability(), s);
		} else if (distribution.getType() == Distribution.NORMAL) {
			normal_event(distribution.getDiagonalNormalDistribution(s),	distribution.getRow(s),distribution.getCol(s),s);			
		} else if (distribution.getType() == Distribution.NEWMANN) {
			newman_event(distribution.getRow(s), distribution.getCol(s), distribution.getRadious(), s);			
		}else if (distribution.getType() == Distribution.RECTANGULAR) {
			rectangular_event(distribution.getRow(s), distribution.getCol(s), 
					distribution.getRow2(s), distribution.getCol2(s), s);			
		}
		
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
	
	/**
	 * An event is distributed in a rectangular shape according to coordinates
	 * @param r1
	 * @param c1
	 * @param r2
	 * @param c2
	 * @param s
	 */
	private void rectangular_event (int r1, int c1, int r2, int c2, Simulation s){
				
		for (int r = Math.min(r1, r2); r <= Math.min(Math.max(r1, r2), s.ROWS-1); r++){
			for (int c = Math.min(c1, c2); c <= Math.min(Math.max(c1, c2), s.COLS-1); c++){
				trigger(r, c, 1.0, s);
			}			
		}
		
	}
	
	/**
	 * Parse a string and return an event
	 * @param s
	 * @return
	 */
	public static Event parseEvent(String s) throws IllegalArgumentException {
		switch (s.charAt(0)){
		case 'A':
			return new Apostasy(Distribution.parseDistribution(s.substring(1)));
		case 'D':
			return new DestroyInstitutions(Distribution.parseDistribution(s.substring(1)));
		case 'R':
			switch (s.charAt(1)){
			case 'P':
				return new RemoveInstitutionsPartialContent(Distribution.parseDistribution(s.substring(2)));
			case 'F':
				return new RemoveInstitutionsContent(Distribution.parseDistribution(s.substring(2)));
			default:
				throw new IllegalArgumentException("Unexpected letter '" + s.charAt(1) + "' after '" + s.charAt(0) + " in " + s + ". Options are S (Structure), P (Partial) and F (Full).");
			}
		case 'C':
			switch (s.charAt(1)){
			case 'P':
				return new ConvertTraits(Distribution.parseDistribution(s.substring(2)));
			case 'F':
				return new ConvertInstitutions(Distribution.parseDistribution(s.substring(2)));
			default:
				throw new IllegalArgumentException("Unexpected letter '" + s.charAt(1) + "' after '" + s.charAt(0) + " in " + s + ". Options are P (Partial) and F (Full).");
			}
		case 'I':
			return new Invasion(Distribution.parseDistribution(s.substring(1)));
		case 'G':
			return new Genocide(Distribution.parseDistribution(s.substring(1)));
		case 'P':
			return ParameterChange.parseParameterChange(s.substring(1));
		default:
			throw new IllegalArgumentException("Unexpected letter '" + s.charAt(1) + "' in " + s + 
					". Options are DP (Partial Destruction), DF (Full Destruction), DS (Structural Destruction)."
					+ "CP (Partial Conversion), CF (Full Conversion), I (Invasion) and G (Genocide), followed by "
					+ "a distribution: e.g G(U, 0.1), genocided in which all agents have a probability of dying "
					+ "of 0.1, I(W,0.5,0.5,6), invasion in which all agents in a Newmann's radious of 6 from the "
					+ "center (0.5*ROWS, 0.5*COLS) will be the invadors, or CP(N,-1,-1,0.2), partial conversion "
					+ "in which the probability of the institutional trais being converted is distributed normally "
					+ "from a randomly selected center (-1,-1) which has probility of 1.0.");
		}
	}

}
