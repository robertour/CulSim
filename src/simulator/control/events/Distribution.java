package simulator.control.events;

import java.io.Serializable;

import simulator.control.Simulation;

public class Distribution implements Serializable{
	private static final long serialVersionUID = -3545632412211320724L;
	
	public static final int UNIFORM = 0;
	public static final int NORMAL = 1;
	public static final int NEWMANN = 2;
	public static final int RECTANGULAR = 3;
	
	private int type = -1;
	private double probability = 0.1;
	
	private double row_ratio = 0.5;
	private double col_ratio = 0.5;
	private double row2_ratio = 0.9;
	private double col2_ratio = 0.5;

	
	private double calculated_row_ratio = -1;
	private double calculated_col_ratio = -1;
	private double calculated_row2_ratio = -1;
	private double calculated_col2_ratio = -1;
	
	private double sd = 0.1;
	
	private int radious = 6;

	
	/**
	 * Creates a uniform distribution
	 * @param probability
	 */
	public Distribution(double probability) throws IllegalArgumentException {
		if (probability < 0 && probability > 1){
			throw new IllegalArgumentException("Invalid argument in Uniform Distribution: probability has to be between 0 and 1.");
		}
		this.type = UNIFORM;
		this.probability = probability;	
	}

	/**
	 * Creates a rectangular distribution
	 * @param row_ratio
	 * @param col_ratio
	 * @param row2_ratio
	 * @param col2_ratio
	 */
	public Distribution(double row_ratio, double col_ratio, double row2_ratio, double col2_ratio) throws IllegalArgumentException{
		if (row_ratio > 1){
			throw new IllegalArgumentException("Invalid argument in normal distribution: x cannot be bigger than 1.");
		}
		if (col_ratio > 1){
			throw new IllegalArgumentException("Invalid argument in normal distribution: y cannot be bigger than 1.");
		}
		if (row2_ratio > 1){
			throw new IllegalArgumentException("Invalid argument in normal distribution: x2 cannot be bigger than 1.");
		}
		if (col2_ratio > 1){
			throw new IllegalArgumentException("Invalid argument in normal distribution: y2 cannot be bigger than 1.");
		}
		
		this.type = RECTANGULAR;
		this.row_ratio = this.calculated_row_ratio = row_ratio;
		this.col_ratio = this.calculated_col_ratio = col_ratio;
		this.row2_ratio = this.calculated_row2_ratio = row2_ratio;
		this.col2_ratio = this.calculated_col2_ratio = col2_ratio;

    }
	
	
	/**
	 * Creates a normal distribution
	 * @param row_ratio
	 * @param col_ratio
	 * @param sd
	 */
	public Distribution(double row_ratio, double col_ratio, double sd) throws IllegalArgumentException{
		if (row_ratio > 1){
			throw new IllegalArgumentException("Invalid argument in normal distribution: x cannot be bigger than 1.");
		}
		if (col_ratio > 1){
			throw new IllegalArgumentException("Invalid argument in normal distribution: y cannot be bigger than 1.");
		}
		if (sd < 0 ){
			throw new IllegalArgumentException("Invalid argument in normal distribution: sd cannot be negative.");
		}
		
		
		this.type = NORMAL;
		this.row_ratio = this.calculated_row_ratio = row_ratio;
		this.col_ratio = this.calculated_col_ratio = col_ratio;
		this.sd = sd;
    }
	
	/**
	 * Creates a Newmann (non-probabilistic) distribution of events
	 * @param row_ratio
	 * @param col_ratio
	 * @param r
	 */
	public Distribution(double row_ratio, double col_ratio, int r) throws IllegalArgumentException{
		if (row_ratio > 1){
			throw new IllegalArgumentException("Invalid argument in normal distribution: x cannot be bigger than 1.");
		}
		if (col_ratio > 1){
			throw new IllegalArgumentException("Invalid argument in normal distribution: y cannot be bigger than 1.");
		}
		if (r < 0 ){
			throw new IllegalArgumentException("Invalid argument in normal distribution: r cannot be negative.");
		}
		this.type = NEWMANN;
		this.row_ratio = this.calculated_row_ratio = row_ratio;
		this.col_ratio = this.calculated_col_ratio = col_ratio;
		this.radious = r;
    }
	
	/**
	 * Return the type of distribution
	 * @return
	 */
	public int getType() {
		return type;
	}

	public double getProbability() {
		return probability;
	}
	
	public int getRow(Simulation sim){
		if (calculated_row_ratio < 0){
			calculated_row_ratio = sim.getRand().nextDouble(); 
		} 
		return (int) Math.round(calculated_row_ratio*(sim.ROWS-1));
	}
	
	public int getCol(Simulation sim){
		if (calculated_col_ratio < 0){
			calculated_col_ratio = sim.getRand().nextDouble(); 
		} 
		return (int) Math.round(calculated_col_ratio*(sim.COLS-1));
	}
	
	public int getRow2(Simulation sim){
		if (calculated_row2_ratio < 0){
			calculated_row2_ratio = sim.getRand().nextDouble(); 
		} 
		return (int) Math.round(calculated_row2_ratio*(sim.ROWS-1));
	}
	
	public int getCol2(Simulation sim){
		if (calculated_col2_ratio < 0){
			calculated_col2_ratio = sim.getRand().nextDouble(); 
		} 
		return (int) Math.round(calculated_col2_ratio*(sim.COLS-1));
	}
	
	

	public double getSd() {
		return sd;
	}
	
	public int getRadious(){
		return radious;
	}
	
	public NormalProbabilityDensityFuntion getDiagonalNormalDistribution(Simulation s){
		return new NormalProbabilityDensityFuntion(getSd()*(Math.pow(s.ROWS*s.ROWS+s.COLS*s.COLS,0.5)/2));
	}
	
	public double getRow_ratio() {
		return row_ratio;
	}

	public double getCol_ratio() {
		return col_ratio;
	}
	public double getRow2_ratio() {
		return row2_ratio;
	}

	public double getCol2_ratio() {
		return col2_ratio;
	}
	
	public String toString(){
		String r = "";
		if (getType() == UNIFORM) {
			r += "Uniform: p=" + getProbability() + "";
		} else if (getType() == NORMAL) {
			r += "Normal: C=" + getRow_ratio() + "," + 
					getCol_ratio() + " SD=" + getSd();
		} else if (getType() == NEWMANN) {
			r += "Newmann: C=" + getRow_ratio() + "," + 
					getCol_ratio() + " R=" + getRadious();
		} else if (getType() == RECTANGULAR) {
			r += "Rect: p1=" + getRow_ratio() + "," + 
					getCol_ratio() + " p2=" + getRow2_ratio() + "," + 
					getCol2_ratio();
		} 
		return r;
	}

	/**
	 * Probability density function for the normal
	 * @author tico
	 */
	public class NormalProbabilityDensityFuntion implements Serializable{
		private static final long serialVersionUID = -8284063229856075731L;
		
		private double var;
		private double coef;
		
		
		public NormalProbabilityDensityFuntion(double sd){
			this.coef = 1/(sd*Math.pow(2*Math.PI,0.5));
			this.var = sd*sd;
			
		}
		
		public double density(double x){
			return coef*Math.exp(-(x*x)/(2*var));
		}
	}
	
	/**
	 * Creates a distribution based on a string that represents it
	 * @param s in the form of (U,p), (W,x,y,radius) or (N,x,y,sd)
	 * @return
	 */
	public static Distribution parseDistribution(String s){
		if (s.charAt(0) != '('){
			throw new IllegalArgumentException("'(' missing at the begining of the distribution: " + s);
		} else if (s.charAt(s.length()-1) != ')'){
			throw new IllegalArgumentException("')' missing at the end of the distribution: " + s);
		}
		

		String[] params = s.substring(1, s.length()-1).split(",");
		if (params[0].length() != 1){
			throw new IllegalArgumentException(params[0] + " is not a valid distribution in " + s);
		}
		switch (params[0].charAt(0)){
		case 'U':
			if (params.length != 2){
				throw new IllegalArgumentException("Uniform distribution accepts exactly one parameter after the 'U' in " + s);
			}
			return new Distribution(Double.parseDouble(params[1]));
		case 'W':
			if (params.length != 4){
				throw new IllegalArgumentException("Newmann's distribution accepts exactly 3 parameters after the 'W' in " + s);
			}
			return new Distribution(Double.parseDouble(params[1]),Double.parseDouble(params[2]),Integer.parseInt(params[3]));
		case 'N':
			if (params.length != 4){
				throw new IllegalArgumentException("Normal distribution accepts exactly 3 parameters after the 'N' in " + s);
			}
			return new Distribution(Double.parseDouble(params[1]),Double.parseDouble(params[2]),Double.parseDouble(params[3]));
		case 'R':
			if (params.length != 5){
				throw new IllegalArgumentException("Rectangular distribution accepts exactly 4 parameters after the 'R' in " + s);
			}
			return new Distribution(Double.parseDouble(params[1]),Double.parseDouble(params[2]),Double.parseDouble(params[3]));
		default:
			throw new IllegalArgumentException("Invalid distribution: " + s + ". Options are (U,p), (W,x,y,radious), N(x,y,center), and R(x1,y1,x2,y2)");
		}
	}
}
