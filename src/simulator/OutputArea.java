package simulator;

import javax.swing.JTextArea;

import simulator.control.Printable;

public class OutputArea extends JTextArea implements Printable {

	private static final long serialVersionUID = 1L;

	@Override
	public void print(int id, String str) {
		if (id < 0){
			this.append(str);
		} else {
			this.append("(ID: " + id +  "): " + str);
		}
	}
}