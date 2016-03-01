package simulator.control;

import java.io.Serializable;

import org.apache.commons.math3.random.RandomGenerator;

public class CultureStatistics implements Serializable{
	private static final long serialVersionUID = -7499666293108729939L;
	
	private double x = -101;
	private double y = -102;
	private int size = -103;
	private int beliefs[] = null;
	private double radious;
	
	
	public CultureStatistics (int size, double x, double y, int[] beliefs){
		//this.beliefs = new int[beliefs.length];
	    //for (int i = 0; i < beliefs.length; i++) {
	    //	this.beliefs[i] = beliefs[i];	
		//}
		this.beliefs =  beliefs;
		this.size = size;
		this.x = x;
		this.y = y;
		
		radious = Math.pow(this.size/Math.PI, 0.5);
		
	}
	
	public double compare_positions(CultureStatistics o){
		
		double d = 2*radious;
		double y_sim = (d - Math.abs(y-o.y))/d;
		if (y_sim < 0){
			y_sim = 0;
		}
		y_sim = 1/(1+Math.exp(-10*(y_sim-0.5)));

		//this_r = this_r * 2;
		double x_sim = (d - Math.abs(x-o.x))/d;
		if (x_sim < 0){
			x_sim = 0;
		}
		x_sim = 1/(1+Math.exp(-10*(x_sim-0.5)));
		
		return (x_sim + y_sim)/2;
		
	}
	
	public double compare_size(CultureStatistics o){
		double o_r = Math.pow(o.size/Math.PI, 0.5);
		double s_sim = (radious - Math.abs(radious - o_r))/radious;
		if (s_sim < 0){
			return 0;
		}
		return s_sim;
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
