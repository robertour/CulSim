package simulator;

import javax.swing.JTextArea;

import simulator.control.Printable;

public class OutputArea extends JTextArea implements Printable {

	private static final long serialVersionUID = 1L;

	@Override
	public void print(String str) {
		this.append(str);	
	}
}