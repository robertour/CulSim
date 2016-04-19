package simulator.control;

import java.io.Serializable;

/**
 * This class saves general statistics of a particular culture: the size, the
 * geographical position, the traits of the culture.
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 */
public class CultureStatistics implements Serializable {
	private static final long serialVersionUID = -7499666293108729939L;

	private double x = -101;
	private double y = -102;
	private int size = -103;
	private int traits[] = null;
	private Simulation sim;

	/**
	 * Constructor of the class. It receives the size, the position, the
	 * cultural traits and an instance of the simulation.
	 * 
	 * @param size
	 *            size of the culture
	 * @param x
	 *            x-axis of the center of the culture
	 * @param y
	 *            y-axis of the center of the culture
	 * @param traits
	 *            the traits of the culture
	 * @param sim
	 *            the simulation
	 */
	public CultureStatistics(int size, double x, double y, int[] traits, Simulation sim) {
		this.traits = traits;
		this.size = size;
		this.x = x;
		this.y = y;
		this.sim = sim;
	}

	/**
	 * Compare the position of two cultures
	 * 
	 * @param o
	 *            the other culture
	 * @return a normalized value comparing the two positions of the cultures, 1
	 *         for exactly the same position, and 0 for completely different
	 *         positions
	 */
	public double compare_positions(CultureStatistics o) {

		double y_diff = y/sim.COLS - o.y/sim.COLS;
		double x_diff = x/sim.ROWS - o.x/sim.ROWS;

		double diff = Math.pow(x_diff * x_diff + y_diff * y_diff, 0.5);
		
		return 1 - diff;
	}

	/**
	 * Compare the size of two cultures
	 * 
	 * @param o
	 *            the other culture
	 * @return a normalized value comparing the two sizes of the cultures, 1 for
	 *         exactly the same size, and 0 for completely different sizes
	 */
	public double compare_size(CultureStatistics o) {

		return 1 - Math.abs(size - o.size) / (double) sim.TOTAL_AGENTS;

	}

	/**
	 * Compare the traits of two cultures
	 * 
	 * @param o
	 *            the other culture
	 * @return a normalized value comparing the traits of the cultures, 1 for
	 *         exactly the same traits, and 0 for completely different traits
	 */
	public double compare_traits(CultureStatistics other) {
		double traits_sim = 0;
		for (int i = 0; i < this.traits.length; i++) {
			if (this.traits[i] == other.traits[i]) {
				traits_sim = traits_sim + 1.0;
			}
		}

		return traits_sim / traits.length;
	}

	/**
	 * Compare the two cultures in terms of traits, sizes and positions
	 * 
	 * @param o
	 *            the other culture
	 * @return a normalized value comparing the two cultures, 1 for exactly the
	 *         same culture, and 0 for completely different cultures
	 */
	public double compare(CultureStatistics o) {
		return compare_traits(o) * compare_size(o) * compare_positions(o);
	}

}
