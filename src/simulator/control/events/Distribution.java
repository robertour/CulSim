package simulator.control.events;

import java.io.Serializable;

import simulator.control.Simulation;

/**
 * This class represent the shape in which an event is distributed. The name
 * Distribution might get confusion with probabilistic distribution. Although
 * there is some relation with it, it shouldn't be interpreted strictly since
 * some of the distributions might actually be deterministic.
 * 
 * Currently three distributions are available: UNIFORM, in which all events
 * occur with equal probability in every cell; NORMAL, in which the probability
 * is distributed normally in the grid; NEUMANN, in which the events are
 * distributed in a Neumann neighborhood; and RECTANGULAR, in which the events
 * are distributed in a rectangle.
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 *
 */
public class Distribution implements Serializable {
	private static final long serialVersionUID = -3545632412211320724L;

	/**
	 * Identifies a UNIFORM distribution
	 */
	public static final int UNIFORM = 0;
	/**
	 * Identifies a NORMAL distribution
	 */
	public static final int NORMAL = 1;
	/**
	 * Identifies a NEUMANN distribution
	 */
	public static final int NEUMANN = 2;
	/**
	 * Identifies a RECTANGULAR distribution
	 */
	public static final int RECTANGULAR = 3;

	/**
	 * Identifies the current distribution
	 */
	private int type = -1;
	/**
	 * The probability of an event to occur
	 */
	private double probability = 0.1;

	/**
	 * If different of -1, the calculated_row_ratio will use this value. If
	 * equal to -1, the calculated_row_ratio will be picked up at random.
	 */
	private double row_ratio = 0.5;
	/**
	 * If different of -1, the calculated_col_ratio will use this value. If
	 * equal to -1, the calculated_col_ratio will be picked up at random.
	 */
	private double col_ratio = 0.5;
	/**
	 * If different of -1, the calculated_row2_ratio will use this value. If
	 * equal to -1, the calculated_row3_ratio will be picked up at random.
	 */
	private double row2_ratio = 0.9;
	/**
	 * If different of -1, the calculated_col2_ratio will use this value. If
	 * equal to -1, the calculated_col_ratio will be picked up at random.
	 */
	private double col2_ratio = 0.5;

	/**
	 * A row that indicates one position where the event is located represented
	 * as a ratio of the total rows to keep the events scalable across different
	 * word sizes. For NEUMANN and NORMAL distributions, it represent the row in
	 * which the event is centered. For RECTANGULAR distribution, it represents
	 * the row of the initial position of the rectangle.
	 */
	private double calculated_row_ratio = -1;
	/**
	 * A column that indicates one position where the event is located
	 * represented as a ratio of the total columns to keep the events scalable
	 * across different word sizes. For NEUMANN and NORMAL distributions, it
	 * represent the column in which the event is centered. For RECTANGULAR
	 * distribution, it represents the column of the initial position of the
	 * rectangle.
	 */
	private double calculated_col_ratio = -1;
	/**
	 * For RECTANGULAR distribution, it represents the row of the final position
	 * of the rectangle.
	 */
	private double calculated_row2_ratio = -1;
	/**
	 * For RECTANGULAR distribution, it represents the column of the final
	 * position of the rectangle.
	 */
	private double calculated_col2_ratio = -1;
	/**
	 * The standard deviation of the NORMAL distribution
	 */
	private double sd = 0.1;
	/**
	 * The radius of the NEUMANN distribution
	 */
	private int radius = 6;

	/**
	 * Creates a UNIFORM distribution
	 * 
	 * @param probability
	 *            the probability of the event to occur in any particular cell.
	 */
	public Distribution(double probability) throws IllegalArgumentException {
		if (probability < 0 && probability > 1) {
			throw new IllegalArgumentException(
					"Invalid argument in Uniform Distribution: probability has to be between 0 and 1.");
		}
		this.type = UNIFORM;
		this.probability = probability;
	}

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
	public Distribution(double row_ratio, double col_ratio, double row2_ratio, double col2_ratio)
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
	 * Creates a NORMAL distribution
	 * 
	 * @param row_ratio
	 *            the center row of the event (as a ratio of the rows)
	 * @param col_ratio
	 *            the center column of the event (as a ratio of the columns)
	 * @param sd
	 *            the standard deviation used to distribute the probabilities
	 */
	public Distribution(double row_ratio, double col_ratio, double sd) throws IllegalArgumentException {
		if (row_ratio > 1) {
			throw new IllegalArgumentException("Invalid argument in NORMAL distribution: x cannot be bigger than 1.");
		}
		if (col_ratio > 1) {
			throw new IllegalArgumentException("Invalid argument in NORMAL distribution: y cannot be bigger than 1.");
		}
		if (sd < 0) {
			throw new IllegalArgumentException("Invalid argument in NORMAL distribution: sd cannot be negative.");
		}

		this.type = NORMAL;
		this.row_ratio = this.calculated_row_ratio = row_ratio;
		this.col_ratio = this.calculated_col_ratio = col_ratio;
		this.sd = sd;
	}

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
	public Distribution(double row_ratio, double col_ratio, int r) throws IllegalArgumentException {
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
	 * Return the type of distribution
	 * 
	 * @return the type of distribution: UNIFORM, NORMAL, RECTANGULAR or NEUMANN
	 */
	public int getType() {
		return type;
	}

	/**
	 * Returns the probability of the distribution (when UNIFORM)
	 * 
	 * @return the probability of the distribution (when UNIFORM)
	 */
	public double getProbability() {
		return probability;
	}

	/**
	 * Calculates the exact row (center for UNIFORM or NEUMAN, initIal position
	 * for RECTANGULAR) of the event according to the world size of the
	 * simulation.
	 * 
	 * @param sim
	 *            the simulation in which the event is executed
	 * @return the exact row depending on the number of rows of the simulation
	 */
	public int getRow(Simulation sim) {
		if (calculated_row_ratio < 0) {
			calculated_row_ratio = sim.getRand().nextDouble();
		}
		return (int) Math.round(calculated_row_ratio * (sim.ROWS - 1));
	}

	/**
	 * Calculates the exact column (center for UNIFORM or NEUMAN, initIal
	 * position for RECTANGULAR) of the event according to the world size of the
	 * simulation.
	 * 
	 * @param sim
	 *            the simulation in which the event is executed
	 * @return the exact column depending on the number of columns of the
	 *         simulation
	 */
	public int getCol(Simulation sim) {
		if (calculated_col_ratio < 0) {
			calculated_col_ratio = sim.getRand().nextDouble();
		}
		return (int) Math.round(calculated_col_ratio * (sim.COLS - 1));
	}

	/**
	 * Calculates the exact row (final position for RECTANGULAR) of the event
	 * according to the world size of the simulation.
	 * 
	 * @param sim
	 *            the simulation in which the event is executed
	 * @return the exact row depending on the number of rows of the simulation
	 */
	public int getRow2(Simulation sim) {
		if (calculated_row2_ratio < 0) {
			calculated_row2_ratio = sim.getRand().nextDouble();
		}
		return (int) Math.round(calculated_row2_ratio * (sim.ROWS - 1));
	}

	/**
	 * Calculates the exact column (final position for RECTANGULAR) of the event
	 * according to the world size of the simulation.
	 * 
	 * @param sim
	 *            the simulation in which the event is executed
	 * @return the exact column depending on the number of columns of the
	 *         simulation
	 */
	public int getCol2(Simulation sim) {
		if (calculated_col2_ratio < 0) {
			calculated_col2_ratio = sim.getRand().nextDouble();
		}
		return (int) Math.round(calculated_col2_ratio * (sim.COLS - 1));
	}

	/**
	 * Return the standard deviation of the distribution (for NORMAL
	 * distribution only)
	 * 
	 * @return the standard distribution
	 */
	public double getSd() {
		return sd;
	}

	/**
	 * Return the radius of the distribution (for NEUMANN distribution only)
	 * 
	 * @return the radius of the NEUMANN distribution
	 */
	public int getRadius() {
		return radius;
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
	public NormalProbabilityDensityFuntion getDiagonalNormalDistribution(Simulation s) {
		return new NormalProbabilityDensityFuntion(getSd() * (Math.pow(s.ROWS * s.ROWS + s.COLS * s.COLS, 0.5) / 2));
	}

	/**
	 * Return the row as a ratio of the rows in the simulation
	 * 
	 * A row that indicates one position where the event is located represented
	 * as a ratio of the total rows to keep the events scalable across different
	 * word sizes. For NEUMANN and NORMAL distributions, it represent the row in
	 * which the event is centered. For RECTANGULAR distribution, it represents
	 * the row of the initial position of the rectangle.
	 * 
	 * @return the row ratio
	 */
	public double getRow_ratio() {
		return row_ratio;
	}

	/**
	 * Return the column as a ratio of the rows in the simulation
	 * 
	 * A column that indicates one position where the event is located
	 * represented as a ratio of the total columns to keep the events scalable
	 * across different word sizes. For NEUMANN and NORMAL distributions, it
	 * represent the column in which the event is centered. For RECTANGULAR
	 * distribution, it represents the column of the initial position of the
	 * rectangle.
	 * 
	 * @return the column ratio
	 */
	public double getCol_ratio() {
		return col_ratio;
	}

	/**
	 * Return the second row as a ratio of the rows in the simulation
	 * 
	 * For RECTANGULAR distribution, it represents the row of the final position
	 * of the rectangle.
	 * 
	 * @return the second row ratio
	 */
	public double getRow2_ratio() {
		return row2_ratio;
	}

	/**
	 * Return the second row as a ratio of the rows in the simulation
	 * 
	 * For RECTANGULAR distribution, it represents the column of the final
	 * position of the rectangle.
	 * 
	 * @return the second column ratio
	 */
	public double getCol2_ratio() {
		return col2_ratio;
	}

	/**
	 * Creates a String representation of the distribution.
	 */
	public String toString() {
		String r = "";
		if (getType() == UNIFORM) {
			r += "Uniform: p=" + getProbability() + "";
		} else if (getType() == NORMAL) {
			r += "Normal: C=" + getRow_ratio() + "," + getCol_ratio() + " SD=" + getSd();
		} else if (getType() == NEUMANN) {
			r += "Neumann: C=" + getRow_ratio() + "," + getCol_ratio() + " R=" + getRadius();
		} else if (getType() == RECTANGULAR) {
			r += "Rect: p1=" + getRow_ratio() + "," + getCol_ratio() + " p2=" + getRow2_ratio() + "," + getCol2_ratio();
		}
		return r;
	}

	/**
	 * Probability density function for the standardized Normal distribution
	 * 
	 * @author tico
	 */
	public class NormalProbabilityDensityFuntion implements Serializable {
		private static final long serialVersionUID = -8284063229856075731L;

		/**
		 * Variance of the probability density function.
		 */
		private double var;
		/**
		 * Coefficient of the probability density function
		 */
		private double coef;

		/**
		 * Constructor
		 * 
		 * @param sd
		 *            the standard distribution of the normal distribution.
		 */
		public NormalProbabilityDensityFuntion(double sd) {
			this.coef = 1 / (sd * Math.pow(2 * Math.PI, 0.5));
			this.var = sd * sd;
		}

		/**
		 * Returns the density of the function at any the point x
		 * 
		 * @param x
		 *            the value in the x axis
		 * @return the density
		 */
		public double density(double x) {
			return coef * Math.exp(-(x * x) / (2 * var));
		}
	}

	/**
	 * Creates a distribution based on a string that represents it
	 * 
	 * @param s
	 *            in the form of "U@p", "W@x,y,radius", or "N@x,y,sd" or "R@x1,y1,x2,y2"
	 * @return an instance of the Distribution class that represent the
	 *         distribution represented in the string s
	 */
	public static Distribution parseDistribution(String s) {
		if (s.charAt(0) != '@') {
			throw new IllegalArgumentException("'@' missing at the begining of the distribution: " + s);
		}

		String[] params = s.substring(1, s.length() - 1).split(",");
		if (params[0].length() != 1) {
			throw new IllegalArgumentException(params[0] + " is not a valid distribution in " + s);
		}
		switch (params[0].charAt(0)) {
		case 'U':
			if (params.length != 2) {
				throw new IllegalArgumentException(
						"Uniform distribution accepts exactly one parameter after the 'U' in " + s);
			}
			return new Distribution(Double.parseDouble(params[1]));
		case 'W':
			if (params.length != 4) {
				throw new IllegalArgumentException(
						"Neumann's distribution accepts exactly 3 parameters after the 'W' in " + s);
			}
			return new Distribution(Double.parseDouble(params[1]), Double.parseDouble(params[2]),
					Integer.parseInt(params[3]));
		case 'N':
			if (params.length != 4) {
				throw new IllegalArgumentException(
						"Normal distribution accepts exactly 3 parameters after the 'N' in " + s);
			}
			return new Distribution(Double.parseDouble(params[1]), Double.parseDouble(params[2]),
					Double.parseDouble(params[3]));
		case 'R':
			if (params.length != 5) {
				throw new IllegalArgumentException(
						"Rectangular distribution accepts exactly 4 parameters after the 'R' in " + s);
			}
			return new Distribution(Double.parseDouble(params[1]), Double.parseDouble(params[2]),
					Double.parseDouble(params[3]));
		default:
			throw new IllegalArgumentException("Invalid distribution: " + s
					+ ". Options are (U,p), (W,x,y,radious), N(x,y,center), and R(x1,y1,x2,y2)");
		}
	}
}
