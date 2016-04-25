package simulator.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import simulator.control.events.ParameterChange;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.Dimension;

/**
 * Interface to configure a parameter event in the simulation, i.e. an event
 * that changes one of the initial parameters.
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 */
public class ParametersEventDialog extends JDialog {

	private static final long serialVersionUID = -8199562513877809300L;

	private final JPanel contentPanel = new JPanel();
	private ArrayList<Notifiable> notifiables = new ArrayList<Notifiable>();

	/**
	 * Copy of the current values of the parameters in the simulation.
	 */
	public static int iterations = -1;
	public static int speed = -1;
	public static float mutation = 0.001f;
	public static float selection_error = -1;
	public static float influence = -1;
	public static float loyalty = -1;
	public static int democracy = -1;
	public static int propaganda = -1;

	/**
	 * The new values that the user has set up for the simulation
	 */
	private JSpinner sp_selection_error;
	private JSpinner sp_influence;
	private JSpinner sp_loyalty;
	private JSpinner sp_democracy;
	private JSpinner sp_propaganda;
	private JSpinner sp_iterations;
	private JSpinner sp_speed;
	private JSpinner sp_mutation;

	/**
	 * Create the Dialog with the respective title and owner for the modal
	 * 
	 * @param title
	 *            title of the dialog
	 * @param owner
	 *            owner for the modal
	 */
	public ParametersEventDialog(String title, JFrame owner) {
		super(owner);

		setTitle("Parameters Event");

		setBounds(100, 100, 204, 391);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JPanel panel_8 = new JPanel();
		panel_8.setLayout(null);
		panel_8.setBorder(new TitledBorder(
				new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Noise", TitledBorder.LEADING,
						TitledBorder.TOP, null, new Color(0, 0, 0)),
				"Controls", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_8.setBounds(5, 5, 180, 78);
		contentPanel.add(panel_8);

		JLabel lblIteration = new JLabel("Iterations:");
		lblIteration.setToolTipText("How many iterations would you let the simulation run for? ");
		lblIteration.setBounds(10, 23, 74, 14);
		panel_8.add(lblIteration);

		JLabel lblSpeed_1 = new JLabel("Speed:");
		lblSpeed_1.setToolTipText(
				"How often do you want to save results, update graphs, check for Events or Pause/Stop/Resume states? The highes the value, the faster it executes, but it doesn't update the interface or store results as fast.");
		lblSpeed_1.setBounds(10, 48, 74, 14);
		panel_8.add(lblSpeed_1);

		sp_iterations = new JSpinner();
		sp_iterations
				.setModel(new SpinnerNumberModel(new Integer(1000000), new Integer(1000), null, new Integer(1000)));
		sp_iterations.setToolTipText("How many iterations would you let the simulation run for? ");
		sp_iterations.setBounds(90, 20, 80, 20);
		panel_8.add(sp_iterations);

		sp_speed = new JSpinner();
		sp_speed.setModel(new SpinnerNumberModel(new Integer(100), new Integer(0), null, new Integer(10)));
		sp_speed.setToolTipText(
				"How often do you want to save results, update graphs, check for Events or Pause/Stop/Resume states? The highes the value, the faster it executes, but it doesn't update the interface or store results as fast.");
		sp_speed.setBounds(90, 45, 80, 20);
		panel_8.add(sp_speed);

		JPanel panel_23 = new JPanel();
		panel_23.setLayout(null);
		panel_23.setBorder(new TitledBorder(
				new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Noise", TitledBorder.LEADING,
						TitledBorder.TOP, null, new Color(0, 0, 0)),
				"", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_23.setBounds(5, 94, 180, 78);
		contentPanel.add(panel_23);

		JLabel label_2 = new JLabel("Mutation:");
		label_2.setToolTipText("How often a random change in a feature occurs?");
		label_2.setBounds(10, 23, 74, 14);
		panel_23.add(label_2);

		JLabel label_3 = new JLabel("Selection Error:");
		label_3.setToolTipText(
				"How often do you want to save results, update graphs, check for Pause/Stop/Resume states?");
		label_3.setBounds(10, 48, 85, 14);
		panel_23.add(label_3);

		sp_mutation = new JSpinner();
		sp_mutation
				.setModel(new SpinnerNumberModel(new Float(0.00001), new Float(0), new Float(1), new Float(0.000001)));
		((JSpinner.NumberEditor) sp_mutation.getEditor()).getFormat().setMinimumFractionDigits(6);
		sp_mutation.setValue(0.000001f);
		sp_mutation.setToolTipText("How often a random change in a feature occurs?");
		sp_mutation.setBounds(90, 20, 80, 20);
		panel_23.add(sp_mutation);

		sp_selection_error = new JSpinner();
		sp_selection_error
				.setModel(new SpinnerNumberModel(new Float(0.00001), new Float(0), new Float(1), new Float(0.000001)));
		((JSpinner.NumberEditor) sp_selection_error.getEditor()).getFormat().setMinimumFractionDigits(6);
		sp_selection_error.setValue(0.000001f);
		sp_selection_error.setToolTipText(
				"How often an agent confuses the selection of an agent that can be influnce by or not?");
		sp_selection_error.setBounds(90, 45, 80, 20);
		panel_23.add(sp_selection_error);

		JPanel panel_24 = new JPanel();
		panel_24.setLayout(null);
		panel_24.setBorder(new TitledBorder(
				new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Controls4", TitledBorder.LEADING,
						TitledBorder.TOP, null, new Color(0, 0, 0)),
				"Institutions", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_24.setBounds(5, 183, 180, 124);
		contentPanel.add(panel_24);

		JLabel label_4 = new JLabel("Influence:");
		label_4.setToolTipText("Institutional influence over the agent");
		label_4.setBounds(10, 23, 64, 14);
		panel_24.add(label_4);

		JLabel label_5 = new JLabel("Loyalty:");
		label_5.setToolTipText("Agent's loyalty towards the institution");
		label_5.setBounds(10, 48, 64, 14);
		panel_24.add(label_5);

		JLabel label_6 = new JLabel("Democracy:");
		label_6.setToolTipText("How often a democratic process occurs?");
		label_6.setBounds(10, 73, 64, 14);
		panel_24.add(label_6);

		sp_influence = new JSpinner();
		sp_influence.setModel(new SpinnerNumberModel(new Float(0.5), new Float(0), new Float(1), new Float(0.05)));
		((JSpinner.NumberEditor) sp_influence.getEditor()).getFormat().setMinimumFractionDigits(3);
		sp_influence.setValue(0.85f);
		sp_influence.setToolTipText("Institutional influence over the agent");
		sp_influence.setBounds(90, 20, 80, 20);
		panel_24.add(sp_influence);

		sp_loyalty = new JSpinner();
		sp_loyalty.setModel(new SpinnerNumberModel(new Float(0.5), new Float(0), new Float(1), new Float(0.05)));
		((JSpinner.NumberEditor) sp_loyalty.getEditor()).getFormat().setMinimumFractionDigits(3);
		sp_loyalty.setValue(0.5f);
		sp_loyalty.setToolTipText("Agent's loyalty towards the institution");
		sp_loyalty.setBounds(90, 45, 80, 20);
		panel_24.add(sp_loyalty);

		sp_democracy = new JSpinner();
		sp_democracy.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		sp_democracy.setToolTipText("How often a democratic process occurs?");
		sp_democracy.setBounds(90, 70, 80, 20);
		panel_24.add(sp_democracy);

		JLabel label_7 = new JLabel("Propaganda:");
		label_7.setToolTipText("How often a propaganda process occurs?");
		label_7.setBounds(10, 98, 64, 14);
		panel_24.add(label_7);

		sp_propaganda = new JSpinner();
		sp_propaganda.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		sp_propaganda.setToolTipText("How often a propaganda process occurs?");
		sp_propaganda.setBounds(90, 95, 80, 20);
		panel_24.add(sp_propaganda);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setPreferredSize(new Dimension(50, 25));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						update_notifiables();
						ParametersEventDialog.this.setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

	/**
	 * Get an event that describes the established parameter change
	 * 
	 * @return a parameter change event according to the values in the interface
	 */
	public ParameterChange get_parameter_change_event() {
		ParameterChange pc = new ParameterChange();
		if (iterations != (int) sp_iterations.getValue())
			pc.iterations = (int) sp_iterations.getValue();
		if (speed != (int) sp_speed.getValue())
			pc.speed = (int) sp_speed.getValue();
		if (mutation != (float) sp_mutation.getValue())
			pc.mutation = (float) sp_mutation.getValue();
		if (selection_error != (float) sp_selection_error.getValue())
			pc.selection_error = (float) sp_selection_error.getValue();
		if (influence != (float) sp_influence.getValue())
			pc.influence = (float) sp_influence.getValue();
		if (loyalty != (float) sp_loyalty.getValue())
			pc.loyalty = (float) sp_loyalty.getValue();
		if (democracy != (int) sp_democracy.getValue())
			pc.democracy = (int) sp_democracy.getValue();
		if (propaganda != (int) sp_propaganda.getValue())
			pc.propaganda = (int) sp_propaganda.getValue();
		return pc;
	}

	/**
	 * The interfaces that need to be notified of the changes in this interface
	 * 
	 * @param n
	 *            the notifiable interface
	 */
	public void addNotifiable(Notifiable n) {
		notifiables.add(n);
	}

	/**
	 * A description of the configured event
	 */
	public String toString() {
		return get_parameter_change_event().toString();
	}

	/**
	 * A method to update the elements that need to be notified
	 */
	private void update_notifiables() {
		for (Iterator<Notifiable> i = notifiables.iterator(); i.hasNext();) {
			Notifiable notifiable = (Notifiable) i.next();
			notifiable.update();
		}
	}

	/**
	 * Refresh the interface with the parameters of the simulation
	 */
	public void refresh_dialog() {
		sp_iterations.setValue(iterations);
		sp_speed.setValue(speed);
		sp_mutation.setValue(mutation);
		sp_selection_error.setValue(selection_error);
		sp_influence.setValue(influence);
		sp_loyalty.setValue(loyalty);
		sp_democracy.setValue(democracy);
		sp_propaganda.setValue(propaganda);
		update_notifiables();
	}
}
