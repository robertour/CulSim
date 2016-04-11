package simulator.gui;

import javax.swing.JTextArea;

import simulator.control.Printable;

/**
 * A version of the JTextArea that can be used to print messages coming from the
 * simulation as it uses the printable interface.
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 */
public class OutputArea extends JTextArea implements Printable {

	private static final long serialVersionUID = 1L;

	@Override
	public void print(int id, String str) {
		if (id < 0) {
			this.append(str);
		} else {
			this.append("(ID: " + id + "): " + str);
		}
	}
}