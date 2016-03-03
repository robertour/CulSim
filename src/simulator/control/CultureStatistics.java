package simulator.control;

import java.io.Serializable;


public class CultureStatistics implements Serializable{
	private static final long serialVersionUID = -7499666293108729939L;
	
	private double x = -101;
	private double y = -102;
	private int size = -103;
	private int beliefs[] = null;
	private double diagonal;
	private Simulation sim;
	
	
	public CultureStatistics (int size, double x, double y, int[] beliefs, Simulation sim){
		this.beliefs =  beliefs;
		this.size = size;
		this.x = x;
		this.y = y;
		this.sim = sim;
		diagonal = Math.pow(sim.ROWS*sim.ROWS+sim.COLS*sim.COLS, 0.5);
		
	}
	
	public double compare_positions(CultureStatistics o){
		
		double y_diff = y-o.y;
		double x_diff = x-o.x;
		
		double diff = Math.pow(x_diff*x_diff + y_diff*y_diff, 0.5);
		if (diff > diagonal){
			return 0;
		}else {
			return 1-diff/diagonal;
		}
	}
	
	public double compare_size(CultureStatistics o){
		
		return 1 - Math.abs(size - o.size)/ (double )sim.TOTAL_AGENTS;

	}
	
	public double compare_beliefs (CultureStatistics other){
		double beliefs_sim = 0;
		for (int i = 0; i < this.beliefs.length; i++) {
			if (this.beliefs[i] == other.beliefs[i]){
				beliefs_sim = beliefs_sim + 1.0;
			}
		}

		return beliefs_sim / beliefs.length;
	}
	
	public double compare (CultureStatistics o){
		return  compare_beliefs(o) * compare_size(o) * compare_positions(o);
	}

}
