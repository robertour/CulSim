package simulator.control.events.distributions;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Random;

import simulator.control.Simulation;

/**
 * This class represent the shape in which an event is distributed. The name
 * Distribution might cause confusion with probabilistic distribution. Although
 * there is some relation with it, it shouldn't be interpreted strictly since
 * some of the distributions might actually be deterministic.
 * 
 * Currently four distributions are available: UNIFORM, in which all events
 * occur with equal probability in every cell; APROX. NORMAL and EST. NORMAL, in
 * which the probability is distributed normally in the grid given; NEUMANN, in
 * which the events are distributed in a Neumann neighborhood; and RECTANGULAR,
 * in which the events are distributed in a rectangle.
 * 
 * @author Roberto Ulloa
 * @version 1.1, June 2016
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
	 * Identifies a NORMAL distribution
	 */
	public static final int NORMAL_EXPECTED = 4;

	/**
	 * Identifies the current distribution
	 */
	protected int type = -1;

	/**
	 * The probability of an event to occur
	 */
	protected double probability = 0.1;

	/**
	 * If different of -1, the calculated_row_ratio will use this value. If
	 * equal to -1, the calculated_row_ratio will be picked up at random.
	 */
	public double row_ratio = 0.5;
	/**
	 * If different of -1, the calculated_col_ratio will use this value. If
	 * equal to -1, the calculated_col_ratio will be picked up at random.
	 */
	public double col_ratio = 0.5;
	/**
	 * If different of -1, the calculated_row2_ratio will use this value. If
	 * equal to -1, the calculated_row3_ratio will be picked up at random.
	 */
	protected double row2_ratio = 0.9;
	/**
	 * If different of -1, the calculated_col2_ratio will use this value. If
	 * equal to -1, the calculated_col_ratio will be picked up at random.
	 */
	protected double col2_ratio = 0.5;

	/**
	 * A row that indicates one position where the event is located represented
	 * as a ratio of the total rows to keep the events scalable across different
	 * word sizes. For NEUMANN and NORMAL distributions, it represent the row in
	 * which the event is centered. For RECTANGULAR distribution, it represents
	 * the row of the initial position of the rectangle.
	 */
	public double calculated_row_ratio = -1;
	/**
	 * A column that indicates one position where the event is located
	 * represented as a ratio of the total columns to keep the events scalable
	 * across different word sizes. For NEUMANN and NORMAL distributions, it
	 * represent the column in which the event is centered. For RECTANGULAR
	 * distribution, it represents the column of the initial position of the
	 * rectangle.
	 */
	public double calculated_col_ratio = -1;
	/**
	 * For RECTANGULAR distribution, it represents the row of the final position
	 * of the rectangle.
	 */
	protected double calculated_row2_ratio = -1;
	/**
	 * For RECTANGULAR distribution, it represents the column of the final
	 * position of the rectangle.
	 */
	protected double calculated_col2_ratio = -1;

	/**
	 * Maximum probabilistic value that the normal "density" function can take.
	 * The value o on the center. Use -1 to ignore and simply use the standard
	 * normal distribution on the range -1 to 1
	 */
	protected double ceil = 1.0;
	/**
	 * Ratio (%) of entities that will receive the event. It is used to estimate
	 * a standard deviation that satisfies the condition.
	 */
	protected double expected = 0.3;
	/**
	 * The standard deviation of the NORMAL distribution
	 */
	protected double sd = 0.1;
	/**
	 * The radius of the NEUMANN distribution
	 */
	protected int radius = 6;

	/**
	 * The seeder guarantees that all events are different 
	 */
	protected static Random seeder = new Random();
	
	/**
	 * Random number generation for the events. It will change each time the
	 * event is executed.
	 */
	protected Random rand = null;

	/**
	 * The seed of the random number generator
	 */
	protected long seed = -1L;
	
	
	public Distribution clone(){
		Distribution d =  new Distribution();
		return d;		
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
	 * Calculates the exact row (center for UNIFORM or NEUMAN, initial position
	 * for RECTANGULAR) of the event according to the world size of the
	 * simulation.
	 * 
	 * @param sim
	 *            the simulation in which the event is executed
	 * @return the exact row depending on the number of rows of the simulation
	 */
	public int getRow(Simulation sim) {
		if (calculated_row_ratio < 0) {
			calculated_row_ratio = rand.nextDouble();
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
			calculated_col_ratio = rand.nextDouble();
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
			calculated_row2_ratio = rand.nextDouble();
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
			calculated_col2_ratio = rand.nextDouble();
		}
		return (int) Math.round(calculated_col2_ratio * (sim.COLS - 1));
	}

	/**
	 * Return the maximum possible value of the normal distribution, i.e. the on
	 * that occurs in the center of the distribution
	 * 
	 * @return the ceil of the distribution
	 */
	public double getCeil() {
		return ceil;
	}

	/**
	 * Return the ration (%) of entities that will receive the event. It is used
	 * to estimate a standard deviation that satisfies the condition.
	 * 
	 * @return expected number of entities that will be affected by the event
	 */
	public double getExpected() {
		return expected;
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
	 * Return the random number generator
	 * 
	 * @return random number generator
	 */
	public Random getRand() {
		return rand;
	}

	/**
	 * Reset the random seed
	 */
	public void reset_rand() {
		rand = new Random();
		seed = seeder.nextLong();
		rand.setSeed(seed); 
	}

	/**
	 * Return the seed associated to the current distribution
	 * 
	 * @return the seed of the distribution of the event
	 */
	public long get_seed() {
		return seed;
	}
	

	/**
	 * Transform a double in a legible string
	 * @param d the double that should be converted into string
	 * @return a string representing the double
	 */
	protected final String _s (double d){
		final DecimalFormat df = new DecimalFormat(".0#");
		return df.format(d);
	}

	/**
	 * Creates a distribution based on a string that represents it
	 * 
	 * @param s
	 *            in the form of "U@p", "W@x,y,radius", or "N@x,y,ceil,sd",
	 *            "E@x,y,ceil,expected" or "R@x1,y1,x2,y2"
	 * @return an instance of the Distribution class that represent the
	 *         distribution represented in the string s
	 */
	public static Distribution parseDistribution(String s) {
		if (s.charAt(0) != '@') {
			throw new IllegalArgumentException(
					"'@' missing at the begining of the distribution: " + s);
		}

		String[] params = s.substring(1, s.length()).split(",");
		if (params[0].length() != 1) {
			throw new IllegalArgumentException(params[0]
					+ " is not a valid distribution in " + s);
		}
		switch (params[0].charAt(0)) {
		case 'U':
			if (params.length != 2) {
				throw new IllegalArgumentException(
						"Uniform distribution accepts exactly one parameter after the 'U' in "
								+ s);
			}
			return new UniformDistribution(Double.parseDouble(params[1]));
		case 'W':
			if (params.length != 4) {
				throw new IllegalArgumentException(
						"Neumann's distribution accepts exactly 3 parameters after the 'W' in "
								+ s);
			}
			return new NeumannDistribution(Double.parseDouble(params[1]),
					Double.parseDouble(params[2]), Integer.parseInt(params[3]));
		case 'N':
			if (params.length != 5) {
				throw new IllegalArgumentException(
						"Normal distribution accepts exactly 4 parameters after the 'N' in "
								+ s);
			}
			return new AproxNormalDistribution(Double.parseDouble(params[1]),
					Double.parseDouble(params[2]),
					Double.parseDouble(params[3]),
					Double.parseDouble(params[4]));
		case 'E':
			if (params.length != 5) {
				throw new IllegalArgumentException(
						"Normal distribution accepts exactly 4 parameters after the 'N' in "
								+ s);
			}
			return new EstNormalDistribution(Double.parseDouble(params[1]),
					Double.parseDouble(params[2]),
					Double.parseDouble(params[3]),
					Double.parseDouble(params[4]));
		case 'R':
			if (params.length != 5) {
				throw new IllegalArgumentException(
						"Rectangular distribution accepts exactly 4 parameters after the 'R' in "
								+ s);
			}
			return new RectangularDistribution(Double.parseDouble(params[1]),
					Double.parseDouble(params[2]),
					Double.parseDouble(params[3]),
					Double.parseDouble(params[4]));
		default:
			throw new IllegalArgumentException(
					"Invalid distribution: "
							+ s
							+ ". Options are (U,p), (W,x,y,radious), N(x,y,center), and R(x1,y1,x2,y2)");
		}
	}

}
