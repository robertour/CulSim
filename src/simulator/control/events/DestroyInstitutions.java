package simulator.control.events;

import simulator.control.Simulation;
import simulator.control.events.distributions.Distribution;
import simulator.worlds.Inst;

/**
 * This event removes all the agents that belongs to an institution. This agents
 * became stateless, i.e. they are assigned their own empty institutions (i.e.
 * institutions that have no traits - represented by -1)
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 */
public class DestroyInstitutions extends Event {
	private static final long serialVersionUID = -9186276200796543594L;

	/**
	 * Constructor of the destroy instituions event.
	 * 
	 * @param d
	 *            represents the distribution of the event in the grid, it could
	 *            be a deterministic distribution (e.g. a Neumann's
	 *            neighborhood) or a probilistic distribution (e.g. uniform
	 *            distribution, in which an event could occur with equal
	 *            probability in each cell)
	 */
	public DestroyInstitutions(Distribution d) {
		super(d);
	}
	
	@Override
	protected void pre_execute(Simulation simulation) {
		super.pre_execute(simulation);
		if (simulation instanceof Inst){
			distribution.calculated_row_ratio = distribution.row_ratio;
			distribution.calculated_col_ratio = distribution.col_ratio;
			int i_r = distribution.getRow(simulation);
			int i_c = distribution.getCol(simulation);
			int []coor = ((Inst) simulation).search_nearest_institutionCenter(i_r, i_c);
			distribution.calculated_row_ratio = (double) coor[0] / simulation.ROWS;
			distribution.calculated_col_ratio = (double) coor[1] / simulation.COLS;
		}
		
	}

	@Override
	public void trigger(int r, int c, double p, Simulation simulation) {
		if (distribution.getRand().nextDouble() < p) {
			simulation.destoy_institution(r, c);
		}
	}

	@Override
	public String toString() {
		return "Structure: " + super.toString();
	}

}