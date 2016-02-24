package simulator.control;

import java.io.Serializable;

public abstract class Event implements Serializable{
	private static final long serialVersionUID = 8176633784954763258L;

	public abstract void execute(Simulation simulation);

}
