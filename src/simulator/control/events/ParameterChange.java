package simulator.control.events;

import simulator.control.Simulation;

/**
 * Genocide event
 * 
 * @author tico
 *
 */
public class ParameterChange extends Event {
	private static final long serialVersionUID = 3408040472593937200L;
	
	public int iterations = -1;
	public int checkpoints = -1;
	public float mutation = -1;
	public float selection_error = -1;
	public float influence = -1;
	public float loyalty = -1;
	public int democracy = -1;
	public int propaganda = -1;
	

	public ParameterChange() {
		super(null);
	}

	@Override
	public void trigger(int r, int c, double p, Simulation s){
		if (iterations >= 0)
			s.ITERATIONS = iterations;
		if (checkpoints >= 0)
			s.CHECKPOINT = checkpoints;
		if (mutation >= 0)
			s.MUTATION = mutation;
		if (selection_error >= 0)
			s.SELECTION_ERROR = selection_error;
		if (influence >= 0)
			s.ALPHA = influence;
		if (loyalty >= 0)
			s.ALPHA_PRIME = loyalty;
		if (democracy >= 0)
			s.FREQ_DEM = democracy;
		if (propaganda >= 0)
			s.FREQ_PROP = propaganda;
		
	}
	
	public String toString() {
		String s = "";
		
		s += (iterations < 0)?"":"Iterations(" + iterations + ") +";
		s += (checkpoints < 0)?"":"Speed(" + checkpoints + ") +";
		s += (mutation < 0)?"":"Mutation(" + String.format("%.2g", mutation) + ") +";
		s += (selection_error < 0)?"Selection error":"(" + String.format("%.2g", selection_error) + ") +";
		s += (influence < 0)?"":"Influence(" + String.format("%.2g", influence) + ") +";
		s += (loyalty < 0)?"":"Loyalty(" + String.format("%.2g", loyalty) + ") +";
		s += (democracy < 0)?"":"Democracy(" + democracy + ") +";
		s += (propaganda < 0)?"":"Propaganda(" + propaganda + ") +";
		
		if (s.length() > 0){
			s = s.substring(0, s.length() - 2);
		}
		
		return s;
	}
	
	public static ParameterChange parseParameterChange(String s){
		if (s.charAt(0) != '('){
			throw new IllegalArgumentException("'(' missing at the begining of the distribution: " + s);
		} else if (s.charAt(s.length()-1) != ')'){
			throw new IllegalArgumentException("')' missing at the end of the distribution: " + s);
		}
		
		String[] params = s.substring(1, s.length()-1).split(",");
		if (params[0].length() != 1){
			throw new IllegalArgumentException(params[0] + " is not a valid distribution in " + s);
		}
		
		ParameterChange pc = new ParameterChange();
		if (params[0].toLowerCase().contains("iterations"))
			pc.iterations = Integer.parseInt(params[1]);
		else if (params[0].toLowerCase().contains("speed"))
			pc.checkpoints = Integer.parseInt(params[1]);
		else if (params[0].toLowerCase().contains("mutation"))
			pc.mutation = Float.parseFloat(params[1]);
		else if (params[0].toLowerCase().contains("selection"))
			pc.selection_error = Float.parseFloat(params[1]);
		else if (params[0].toLowerCase().contains("influence"))
			pc.influence = Float.parseFloat(params[1]);
		else if (params[0].toLowerCase().contains("loyalty"))
			pc.loyalty = Float.parseFloat(params[1]);
		else if (params[0].toLowerCase().contains("democracy"))
			pc.democracy = Integer.parseInt(params[1]);
		else if (params[0].toLowerCase().contains("propaganda"))
			pc.propaganda = Integer.parseInt(params[1]);
		else
			throw new IllegalArgumentException("Invalid parameter: " + s + ". Options are "
					+ "(parameter,y), where parameter can be iteration, speed, mutation, "
					+ "selection, influence, loyalty, democracia, or propaganda");
		
		return pc;
		
	}

}