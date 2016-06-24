package simulator.control.events.distributions;

/**
 * 
 * The event is distributed according to a rectangule specified by
 * its coordinates. No probability is associated to it.
 * 
 * @author Roberto Ulloa
 * @version 1.1, June 2016
 * 
 */
public class RectangularDistribution extends Distribution {
	private static final long serialVersionUID = -4393398359817530266L;

	
	/**
	 * Creates a RECTANGULAR distribution
	 * 
	 * @param row_ratio
	 *            the initial row of the event (as a ratio of the rows)
	 * @param col_ratio
	 *            the initial column of the event (as a ratio of the columns)
	 * @param row2_ratio
	 *            the final row of the event (as a ratio of the rows)
	 * @param col2_ratio
	 *            the final column of the event (as a ratio of the columns)
	 */
	public RectangularDistribution(double row_ratio, double col_ratio, double row2_ratio, double col2_ratio)
			throws IllegalArgumentException {
		if (row_ratio > 1) {
			throw new IllegalArgumentException("Invalid argument in NORMAL distribution: x cannot be bigger than 1.");
		}
		if (col_ratio > 1) {
			throw new IllegalArgumentException("Invalid argument in NORMAL distribution: y cannot be bigger than 1.");
		}
		if (row2_ratio > 1) {
			throw new IllegalArgumentException("Invalid argument in NORMAL distribution: x2 cannot be bigger than 1.");
		}
		if (col2_ratio > 1) {
			throw new IllegalArgumentException("Invalid argument in NORMAL distribution: y2 cannot be bigger than 1.");
		}

		this.type = RECTANGULAR;
		this.row_ratio = this.calculated_row_ratio = row_ratio;
		this.col_ratio = this.calculated_col_ratio = col_ratio;
		this.row2_ratio = this.calculated_row2_ratio = row2_ratio;
		this.col2_ratio = this.calculated_col2_ratio = col2_ratio;

	}
	
	/**
	 * Creates a String representation of the distribution.
	 */
	public String toString() {
		return "Rect(p1:" + _s(getRow_ratio()) + "," + _s(getCol_ratio()) + " p2:" + _s(getRow2_ratio()) + "," + _s(getCol2_ratio())+")";
	}
	

	
}
