package simulator.control.events;

import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;

public class Distribution {
	
	public static final int UNIFORM = 0;
	public static final int NORMAL = 1;
	public static final int NEWMANN = 2;
	
	private int type = -1;
	private double probability = 0.1;
	
	private double row_ratio = -1;
	private double col_ratio = -1;
	
	private double calculated_row_ratio = -1;
	private double calculated_col_ratio = -1;
	
	private double sd = 0.1;
	
	private int radious = 6;
	
	/**
	 * Creates a uniform distribution
	 * @param probability
	 */
	public Distribution(double probability){
		this.type = UNIFORM;
		this.probability = probability;		
	}
	
	/**
	 * Creates a normal distribution
	 * @param row_ratio
	 * @param col_ratio
	 * @param sd
	 */
	public Distribution(double row_ratio, double col_ratio, double sd){
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
	public Distribution(double row_ratio, double col_ratio, int r){
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
	
	public int getRow(int rows, Random rand){
		if (calculated_row_ratio < 0){
			calculated_row_ratio = rand.nextDouble(); 
		} 
		return (int) Math.round(calculated_row_ratio*rows);
	}
	
	public int getCol(int cols, Random rand){
		if (calculated_col_ratio < 0){
			calculated_col_ratio = rand.nextDouble(); 
		} 
		return (int) Math.round(calculated_col_ratio*cols);
	}

	public double getSd() {
		return sd;
	}
	
	public int getRadious(){
		return radious;
	}
	
	public NormalDistribution getColumnNormalDistribution(int cols, Random rand){		
		return new NormalDistribution(getCol(cols,rand), getSd()*cols);
	}
	
	public NormalDistribution getRowNormalDistribution(int rows, Random rand){
		return new NormalDistribution(getRow(rows, rand), getSd()*rows);
	}
	
	public double getRow_ratio() {
		return row_ratio;
	}

	public double getCol_ratio() {
		return col_ratio;
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
		} 
		return r;
	}
}
