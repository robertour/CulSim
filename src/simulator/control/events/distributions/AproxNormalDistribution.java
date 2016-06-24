package simulator.control.events.distributions;

import simulator.control.Simulation;

/**
 * 
 * Creates a an approximated normal distribution of the event. It is
 * approximated because it a grid is a discrete distribution. The Hamilton
 * distance is used to determine the probability respect to the center.
 * 
 * The ceil establish the maximum probability of the curve, i.e. the highest
 * point and the rest of the values are normally distributed according to it.
 * 
 * @author Roberto Ulloa
 * @version 1.1, June 2016
 * 
 */
public class AproxNormalDistribution extends Distribution {
	private static final long serialVersionUID = -4393398359817530266L;

	/**
	 * Creates a NORMAL distribution
	 * 
	 * @param row_ratio
	 *            the center row of the event (as a ratio of the rows)
	 * @param col_ratio
	 *            the center column of the event (as a ratio of the columns)
	 * @param ceil
	 *            the maximum probabilistic value that the distribution can
	 *            throw, i.e. the value in the center of the normal curved. If
	 *            negative, the ceil is given by the standard normal curve
	 * @param sd
	 *            the standard deviation used to distribute the probabilities
	 */
	public AproxNormalDistribution(double row_ratio, double col_ratio,
			double ceil, double sd) throws IllegalArgumentException {
		if (row_ratio > 1) {
			throw new IllegalArgumentException(
					"Invalid argument in NORMAL distribution: x cannot be bigger than 1.");
		}
		if (col_ratio > 1) {
			throw new IllegalArgumentException(
					"Invalid argument in NORMAL distribution: y cannot be bigger than 1.");
		}
		if (ceil <= 0 || ceil > 1) {
			throw new IllegalArgumentException(
					"Invalid argument in NORMAL ESTIMATED distribution: ceil cannot be negative or bigger than 1.");
		}
		if (sd < 0) {
			throw new IllegalArgumentException(
					"Invalid argument in NORMAL distribution: sd cannot be negative.");
		}

		this.type = NORMAL;
		this.row_ratio = this.calculated_row_ratio = row_ratio;
		this.col_ratio = this.calculated_col_ratio = col_ratio;
		this.sd = sd;
		this.ceil = ceil;
	}

	/**
	 * Returns a NormalProbabilityDensityFuntion to calculate the way in which
	 * the event is distributed for the NORMAL distribution
	 * 
	 * @param s
	 *            the simulation in which the event will be executed
	 * @return the NormalProbabilityDensityFuntion describing how the event will
	 *         be distributed
	 */
	public NormalProbabilityDensityFuntion getDiagonalNormalDistribution(
			Simulation s) {
		return new NormalProbabilityDensityFuntion(getSd()
				* (Math.pow(s.ROWS * s.ROWS + s.COLS * s.COLS, 0.5) / 2));
	}

	/**
	 * Creates a String representation of the distribution.
	 */
	public String toString() {
		return "N(C:" + _s(getRow_ratio()) + "," + _s(getCol_ratio())
				+ " M:" + _s(ceil) + " SD:" + _s(getSd())+")";
	}

}
