package simulator.control;

import java.io.Serializable;

/**
 * This class saves general statistics of a particular culture: the size, the
 * geographical position, the beliefs of the culture.
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 */
public class CultureStatistics implements Serializable {
	private static final long serialVersionUID = -7499666293108729939L;

	private double x = -101;
	private double y = -102;
	private int size = -103;
	private int beliefs[] = null;
	private double diagonal;
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
	 * @param beliefs
	 *            the beliefs of the culture
	 * @param sim
	 *            the simulation
	 */
	public CultureStatistics(int size, double x, double y, int[] beliefs, Simulation sim) {
		this.beliefs = beliefs;
		this.size = size;
		this.x = x;
		this.y = y;
		this.sim = sim;
		diagonal = Math.pow(sim.ROWS * sim.ROWS + sim.COLS * sim.COLS, 0.5);

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

		double y_diff = y - o.y;
		double x_diff = x - o.x;

		double diff = Math.pow(x_diff * x_diff + y_diff * y_diff, 0.5);
		if (diff > diagonal) {
			return 0;
		} else {
			return 1 - diff / diagonal;
		}
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
	 * Compare the beliefs of two cultures
	 * 
	 * @param o
	 *            the other culture
	 * @return a normalized value comparing the beliefs of the cultures, 1 for
	 *         exactly the same beliefs, and 0 for completely different beliefs
	 */
	public double compare_beliefs(CultureStatistics other) {
		double beliefs_sim = 0;
		for (int i = 0; i < this.beliefs.length; i++) {
			if (this.beliefs[i] == other.beliefs[i]) {
				beliefs_sim = beliefs_sim + 1.0;
			}
		}

		return beliefs_sim / beliefs.length;
	}

	/**
	 * Compare the two cultures in terms of beliefs, sizes and positions
	 * 
	 * @param o
	 *            the other culture
	 * @return a normalized value comparing the two cultures, 1 for exactly the
	 *         same culture, and 0 for completely different cultures
	 */
	public double compare(CultureStatistics o) {
		return compare_beliefs(o) * compare_size(o) * compare_positions(o);
	}

}
