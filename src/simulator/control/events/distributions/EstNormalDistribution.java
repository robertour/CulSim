package simulator.control.events.distributions;

import simulator.control.Simulation;

/**
 * 
 * Similar to the AproxNormalDistribution but an expected value of affected 
 * institutions or agents is given instead. This value is used to estimate an
 * standard deviation.
 * 
 * @author Roberto Ulloa
 * @version 1.1, June 2016
 * 
 */
public class EstNormalDistribution extends Distribution {
	private static final long serialVersionUID = -4393398359817530266L;

	/**
	 * Creates a NORMAL_ESTIMATED distribution
	 * 
	 * @param row_ratio
	 *            the center row of the event (as a ratio of the rows)
	 * @param col_ratio
	 *            the center column of the event (as a ratio of the columns)
	 * @param ceil
	 *            the maximum probabilistic value that the distribution can throw,
	 *            i.e. the value in the center of the normal curved. If negative, 
	 *            the ceil is given by the standard normal curve
	 * @param expected
	 *            the expected ratio (%) of agents or institutions that will be
	 *            affected by the distribution
	 * @throws IllegalArgumentException when an illegal argument is present
	 */
	public EstNormalDistribution(double row_ratio, double col_ratio, double ceil, double expected) throws IllegalArgumentException {
		
		if (row_ratio > 1) {
			throw new IllegalArgumentException("Invalid argument in NORMAL ESTIMATED distribution: x cannot be bigger than 1.");
		}
		if (col_ratio > 1) {
			throw new IllegalArgumentException("Invalid argument in NORMAL ESTIMATED distribution: y cannot be bigger than 1.");
		}
		if (ceil <= 0 || ceil > 1) {
			throw new IllegalArgumentException("Invalid argument in NORMAL ESTIMATED distribution: ceil cannot be negative or bigger than 1.");
		}
		if (expected < 0 || expected > 1) {
			throw new IllegalArgumentException("Invalid argument in NORMAL ESTIMATED distribution: expected cannot be negative or bigger than 1.");
		}
		if (ceil < expected + 0.05) {
			throw new IllegalArgumentException("Invalid argument in NORMAL ESTIMATED distribution: 'ceil' cannot be much smaller (0.05) than 'expected'.");
		}
		

		this.type = NORMAL_EXPECTED;
		this.row_ratio = this.calculated_row_ratio = row_ratio;
		this.col_ratio = this.calculated_col_ratio = col_ratio;
		this.ceil = ceil;
		this.expected = expected;		
	}
	
	/**
	 * This function estimates an sd starting from an expected value of agents or institutions affected.
	 * This estimation is based in an iterative method so it is an approximation, but improves in usability
	 * because user might be more familiar with expected values than standard deviation
	 * 
     * @param s
	 *            the simulation in which the event will be executed
	 * @return the NormalProbabilityDensityFuntion describing how the event will
	 *         be distributed
	 */
	public NormalProbabilityDensityFuntion estimateDiagonalNormalDistribution(Simulation s){
		
		double acc = 0;
		double sd_down = 0.0;	
		double sd = 1;
		double sd_up = 1;
		double max = -1;
		double conf = 0.0001;
		int x = getRow(s);
		int y = getCol(s);
		
		NormalProbabilityDensityFuntion npdf = null;
		
		while (true){
			npdf =  new NormalProbabilityDensityFuntion(sd * (Math.pow(s.ROWS * s.ROWS + s.COLS * s.COLS, 0.5) / 2));
			max = npdf.max_density();
			acc = 0;
			
			for(int r = 0; r < s.ROWS; r++){
				for(int c = 0; c < s.COLS; c++){
					acc += (npdf.density(x,y,r,c)/max)*ceil;
				}			
			}

			acc = acc / s.TOTAL_AGENTS; 
					
			this.sd = sd;
			
			if (acc < expected - conf){
				if (sd == sd_up){
				 sd_up *= 10;	
				}
				sd_down = sd;
				sd = (sd + sd_up) / 2;
			} else if (acc > expected + conf){								
				sd_up = sd;
				sd = (sd + sd_down) / 2;
			} else {
				break;
			}

		}

		return npdf;
	}

	
	/**
	 * Creates a String representation of the distribution.
	 */
	public String toString() {
		return "E(C:" + _s(getRow_ratio()) + "," + _s(getCol_ratio()) + " M:" + _s(ceil) + " E:" + _s(expected)+")";
	}
	
	
}
