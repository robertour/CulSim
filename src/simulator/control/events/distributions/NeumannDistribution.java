package simulator.control.events.distributions;


/**
 * 
 * The event is distributed according to Neumann neighborhood of the specified
 * radious. There is no probability associated.
 * 
 * @author Roberto Ulloa
 * @version 1.1, June 2016
 * 
 */
public class NeumannDistribution extends Distribution {
	private static final long serialVersionUID = -4393398359817530266L;
	

	/**
	 * Creates a NEUMANN (non-probabilistic) distribution of events
	 * 
	 * @param row_ratio
	 *            the center row of the event (as a ratio of the rows)
	 * @param col_ratio
	 *            the center column of the event (as a ratio of the columns)
	 * @param r
	 *            the radius of the NEUMANN distribution
	 */
	public NeumannDistribution(double row_ratio, double col_ratio, int r) throws IllegalArgumentException {
		if (row_ratio > 1) {
			throw new IllegalArgumentException("Invalid argument in NORMAL distribution: x cannot be bigger than 1.");
		}
		if (col_ratio > 1) {
			throw new IllegalArgumentException("Invalid argument in NORMAL distribution: y cannot be bigger than 1.");
		}
		if (r < 0) {
			throw new IllegalArgumentException("Invalid argument in NORMAL distribution: r cannot be negative.");
		}
		this.type = NEUMANN;
		this.row_ratio = this.calculated_row_ratio = row_ratio;
		this.col_ratio = this.calculated_col_ratio = col_ratio;
		this.radius = r;
	}


	
	/**
	 * Creates a String representation of the distribution.
	 */
	public String toString() {		
		return "Neumann(C:" + _s(getRow_ratio()) + "," + _s(getCol_ratio()) + " R:" + getRadius()+")";
	}

	
}
