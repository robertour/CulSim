package simulator.control.events;

import simulator.control.Simulation;

/**
 * This event changes parameters in the simulation.
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 */
public class ParameterChange extends Event {
	private static final long serialVersionUID = 3408040472593937200L;

	/**
	 * The new iterations parameter after the event is executed. If -1, there is
	 * no parameter change
	 */
	public int iterations = -1;
	/**
	 * The new checkpoints parameter after the event is executed. If -1, there
	 * is no parameter change
	 */
	public int checkpoints = -1;
	/**
	 * The new mutation parameter after the event is executed. If -1, there is
	 * no parameter change
	 */
	public float mutation = -1;
	/**
	 * The new selection error parameter after the event is executed. If -1,
	 * there is no parameter change
	 */
	public float selection_error = -1;
	/**
	 * The new institutional influence parameter after the event is executed. If
	 * -1, there is no parameter change
	 */
	public float influence = -1;
	/**
	 * The new agent's loyalty parameter after the event is executed. If -1,
	 * there is no parameter change
	 */
	public float loyalty = -1;
	/**
	 * The new democracy parameter after the event is executed. If -1, there is
	 * no parameter change
	 */
	public int democracy = -1;
	/**
	 * The new propaganda parameter after the event is executed. If -1, there is
	 * no parameter change
	 */
	public int propaganda = -1;

	/**
	 * Constructor of the parameter change event
	 */
	public ParameterChange() {
		super(null);
	}

	@Override
	public void trigger(int r, int c, double p, Simulation s) {
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

	@Override
	public String toString() {
		String s = "";

		s += (iterations < 0) ? "" : "Iter(" + iterations + ")+";
		s += (checkpoints < 0) ? "" : "Speed(" + checkpoints + ")+";
		s += (mutation < 0) ? "" : "Mut(" + String.format("%.2g", mutation) + ")+";
		s += (selection_error < 0) ? "" : "Sel(" + String.format("%.2g", selection_error) + ")+";
		s += (influence < 0) ? "" : "Inf(" + String.format("%.2g", influence) + ")+";
		s += (loyalty < 0) ? "" : "Loy(" + String.format("%.2g", loyalty) + ")+";
		s += (democracy < 0) ? "" : "Dem(" + democracy + ")+";
		s += (propaganda < 0) ? "" : "Prop(" + propaganda + ")+";

		if (s.length() > 0) {
			s = s.substring(0, s.length() - 1);
		}

		return s;
	}

	/**
	 * It parses parameter change event represented in a string in the form of
	 * (parameter,y), where parameter can be iteration, speed, mutation,
	 * selection, influence, loyalty, democracia, or propaganda";
	 * 
	 * @param s
	 *            the string representation of the parameter event
	 * @return an event that was represented in the string
	 */
	public static ParameterChange parseParameterChange(String s) {
		if (s.charAt(0) != '(') {
			throw new IllegalArgumentException("'(' missing at the begining of the parameter specification: " + s);
		} else if (s.charAt(s.length() - 1) != ')') {
			throw new IllegalArgumentException("')' missing at the end of the parameter specification: " + s);
		}

		String[] params = s.substring(1, s.length() - 1).split(",");
		if (params.length != 2) {
			throw new IllegalArgumentException(s + " needs 2 arguments, " + params.length + " were given instead");
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