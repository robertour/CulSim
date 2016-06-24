package simulator.control.events.distributions;

/**
 * 
 * An entity, agent or institution, is going to be targeted by the event
 * accoriding to a probabibility.
 * 
 * @author Roberto Ulloa
 * @version 1.1, June 2016
 * 
 */
public class UniformDistribution extends Distribution {
	private static final long serialVersionUID = -4393398359817730266L;

	/**
	 * Creates a UNIFORM distribution
	 * 
	 * @param probability
	 *            the probability of the event to occur in any particular cell.
	 */
	public UniformDistribution(double probability)
			throws IllegalArgumentException {
		super();
		if (probability < 0 && probability > 1) {
			throw new IllegalArgumentException(
					"Invalid argument in Uniform Distribution: probability has to be between 0 and 1.");
		}
		this.type = UNIFORM;
		this.probability = probability;
	}

	/**
	 * Creates a String representation of the distribution.
	 */
	public String toString() {
		return "Uniform(p:" + _s(getProbability())+")";
		
	}

}
