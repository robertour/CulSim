package simulator.control.events.distributions;

import java.io.Serializable;

/**
 * Probability density function for the standardized Normal distribution
 * 
 * @author Roberto Ulloa
 * @version 1.1, June 2016
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
	private double density(double x) {
		return coef * Math.exp(-(x * x) / (2 * var));
	}
	
	/**
	 * Return the maximum density of the function, useful to normalize things
	 * @return maximum density
	 */
	public double max_density(){
		return density (0);
	}
	
	/** 
	 * Return the density on a point
	 * @param x center x for the distribution
	 * @param y center y for the distribution
	 * @param r point r for the distribution
	 * @param c point c for the distribution
	 * @return the density in the point r,c when x,y is the center
	 */
	public double density(int x, int y, int r, int c){
		return density(Math.abs(x - r) + Math.abs(y - c));
	}
}
