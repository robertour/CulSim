package simulator.control.events;

import java.io.Serializable;
import java.util.Random;

import simulator.control.Simulation;

/**
 * This is the superclass for the possible events that can be introduced to the
 * simulations. Each event represent an occurrence in the simulation. It could
 * be change of parameters (e.g. change of propaganda rates) or a catastrohic
 * event (e.g. genocide)
 * 
 * @author Roberto Ulloa
 * @version 1.0, March 2016
 *
 */
public abstract class Event implements Serializable {
	private static final long serialVersionUID = 8176633784954763258L;

	/**
	 * This represents the distribution of the event in the grid, it could be a
	 * deterministic distribution (e.g. a Neumann's neighborhood) or a
	 * probilistic distribution (e.g. uniform distribution, in which an event
	 * could occur with equal probability in each cell)
	 */
	protected Distribution distribution = null;

	/**
	 * Constructor of the Event
	 * 
	 * @param d
	 *            represents the distribution of the event in the grid, it could
	 *            be a deterministic distribution (e.g. a Neumann's
	 *            neighborhood) or a probilistic distribution (e.g. uniform
	 *            distribution, in which an event could occur with equal
	 *            probability in each cell)
	 */
	public Event(Distribution d) {
		distribution = d;
	}

	/**
	 * It execute the event according to the respective distribution of the
	 * event.
	 * 
	 * @param s
	 *            simulation in which the event will be executed
	 */
	public void execute(Simulation s) {

		if (distribution == null) {
			trigger(-1, -1, -1, s);
		} else {
			distribution.rand = new Random();
			distribution.seed = distribution.rand.nextLong();
			distribution.rand.setSeed(distribution.seed);
			if (distribution.getType() == Distribution.UNIFORM) {
				uniform_event(distribution.getProbability(), s);
			} else if (distribution.getType() == Distribution.NORMAL) {
				normal_event(distribution.getDiagonalNormalDistribution(s), distribution.getRow(s),
						distribution.getCol(s), s);
			} else if (distribution.getType() == Distribution.NEUMANN) {
				neumann_event(distribution.getRow(s), distribution.getCol(s), distribution.getRadius(), s);
			} else if (distribution.getType() == Distribution.RECTANGULAR) {
				rectangular_event(distribution.getRow(s), distribution.getCol(s), distribution.getRow2(s),
						distribution.getCol2(s), s);
			}
		}
		if (s.log != null) {

			s.log.print(s.IDENTIFIER, "Event executed: " + this.toString() + seedToString());
		}
	}

	/**
	 * Return the seed of the distribution in a string
	 * 
	 * @return the seed of the distribution in a string
	 */
	public String seedToString() {
		return " Seed: " + ((distribution == null) ? "n/a" : (distribution.get_seed() + ""));
	}

	/**
	 * Trigger the event with in a particular cell with an specific probability.
	 * 
	 * @param r
	 *            row of the cell
	 * @param c
	 *            column of the cell
	 * @param p
	 *            probability of the event
	 * @param simulation
	 *            simulation in which the event would occur
	 */
	public abstract void trigger(int r, int c, double p, Simulation simulation);

	/**
	 * A representation of the event.
	 */
	public String toString() {
		return distribution.toString();
	}

	/**
	 * An event is normally distributed among the population, and centered in
	 * the (x, y) cell. This cell receives a maximum probability (1.0), and the
	 * rest a probability that is proportional to the normal distribution nd.
	 * 
	 * @param nd
	 *            the probabilities of the event happening are distributed
	 *            proportionally to this distribution
	 * @param x
	 *            row in which the distribution is centered
	 * @param y
	 *            column in which the distribution is centered
	 * @param s
	 *            simulation in which the even occurs
	 */
	private void normal_event(Distribution.NormalProbabilityDensityFuntion nd, int x, int y, Simulation s) {

		double max = nd.density(0);

		for (int r = 0; r < s.ROWS; r++) {
			for (int c = 0; c < s.COLS; c++) {
				trigger(r, c, nd.density(Math.abs(x - r) + Math.abs(y - c)) / max, s);
			}
		}
	}

	/**
	 * The event is distributed in a Neumann's neighborhood which radious is
	 * specified by the parameter radious. The distribution is centered in r, c.
	 * 
	 * @param r
	 *            the row in which this distribution will be centered
	 * @param c
	 *            the column in which this distribution will be centered
	 * @param radius
	 *            the radious of the Neumann's neighborhood
	 * @param s
	 *            the simulation in which the event occurs
	 */
	private void neumann_event(int r, int c, int radius, Simulation s) {

		trigger(r, c, 1.0, s);

		for (int i = 0; i <= radius; i++) {
			for (int j = 0; j <= radius; j++) {
				if (j + i + 2 <= radius) {
					if (r + i + 1 < s.ROWS && c + j + 1 < s.COLS) {
						trigger(r + i + 1, c + j + 1, 1.0, s);
					}
					if (r - i - 1 >= 0 && c - j - 1 >= 0) {
						trigger(r - i - 1, c - j - 1, 1.0, s);
					}
				}
				if (j + i <= radius && (j != 0 || i != 0)) {
					if (r - i >= 0 && c + j < s.COLS) {
						trigger(r - i, c + j, 1.0, s);
					}
					if (r + i < s.ROWS && c - j >= 0) {
						trigger(r + i, c - j, 1.0, s);
					}
				}
			}
		}
	}

	/**
	 * An event is distributed with uniform probability. And event can occur
	 * with the same probability in each cell of the grid.
	 * 
	 * @param probability
	 *            the probability of the event occurring in each cell
	 * @param s
	 *            simulation in which the event occurs
	 */
	private void uniform_event(double probability, Simulation s) {
		for (int r = 0; r < s.ROWS; r++) {
			for (int c = 0; c < s.COLS; c++) {
				trigger(r, c, probability, s);
			}
		}
	}

	/**
	 * An event is distributed in a rectangular shape according to the
	 * coordinates of two cells, (r1,c1) and (r2,c2). The event occurs in all
	 * involved cells.
	 * 
	 * @param r1
	 *            row of the first cell
	 * @param c1
	 *            column of the fist cell
	 * @param r2
	 *            row of the second cell
	 * @param c2
	 *            column of the second cell
	 * @param s
	 *            simulation in which the event occur
	 */
	private void rectangular_event(int r1, int c1, int r2, int c2, Simulation s) {

		for (int r = Math.min(r1, r2); r <= Math.min(Math.max(r1, r2), s.ROWS - 1); r++) {
			for (int c = Math.min(c1, c2); c <= Math.min(Math.max(c1, c2), s.COLS - 1); c++) {
				trigger(r, c, 1.0, s);
			}
		}

	}

	/**
	 * This method parses an event that is represented in a string, that is
	 * useful for console use. The first one or two characters describes the
	 * type of event, and the rest represent the distribution in which the event
	 * happens.
	 * 
	 * @param s
	 *            the string that represent the event
	 * @return the parsed event
	 * @throws IllegalArgumentException
	 *             when the string doesn't comply the event representation.
	 */
	public static Event parseEvent(String s) throws IllegalArgumentException {
		switch (s.charAt(0)) {
		case 'A':
			return new Apostasy(Distribution.parseDistribution(s.substring(1)));
		case 'D':
			return new DestroyInstitutions(Distribution.parseDistribution(s.substring(1)));
		case 'R':
			switch (s.charAt(1)) {
			case 'P':
				return new RemoveInstitutionsPartialContent(Distribution.parseDistribution(s.substring(2)));
			case 'F':
				return new RemoveInstitutionsContent(Distribution.parseDistribution(s.substring(2)));
			default:
				throw new IllegalArgumentException("Unexpected letter '" + s.charAt(1) + "' after '" + s.charAt(0)
						+ " in " + s + ". Options are S (Structure), P (Partial) and F (Full).");
			}
		case 'C':
			switch (s.charAt(1)) {
			case 'P':
				return new ConvertTraits(Distribution.parseDistribution(s.substring(2)));
			case 'F':
				return new ConvertInstitutions(Distribution.parseDistribution(s.substring(2)));
			default:
				throw new IllegalArgumentException("Unexpected letter '" + s.charAt(1) + "' after '" + s.charAt(0)
						+ " in " + s + ". Options are P (Partial) and F (Full).");
			}
		case 'I':
			return new Invasion(Distribution.parseDistribution(s.substring(1)));
		case 'G':
			return new Genocide(Distribution.parseDistribution(s.substring(1)));
		case 'P':
			return ParameterChange.parseParameterChange(s.substring(1));
		default:
			throw new IllegalArgumentException("Unexpected letter '" + s.charAt(1) + "' in " + s
					+ ". Options are DP (Partial Destruction), DF (Full Destruction), DS (Structural Destruction)."
					+ "CP (Partial Conversion), CF (Full Conversion), I (Invasion) and G (Genocide), followed by "
					+ "a distribution: e.g G(U, 0.1), genocided in which all agents have a probability of dying "
					+ "of 0.1, I(W,0.5,0.5,6), invasion in which all agents in a Neumann's radious of 6 from the "
					+ "center (0.5*ROWS, 0.5*COLS) will be the invadors, or CP(N,-1,-1,0.2), partial conversion "
					+ "in which the probability of the institutional trais being converted is distributed normally "
					+ "from a randomly selected center (-1,-1) which has probility of 1.0.");
		}
	}

}
