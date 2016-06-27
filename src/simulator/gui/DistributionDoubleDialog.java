package simulator.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import simulator.control.events.distributions.Distribution;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Dimension;

/**
 * Interface that let you pick up and configure two different distributions for
 * two events.
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 */
public class DistributionDoubleDialog extends JDialog {

	private static final long serialVersionUID = -8199562513877809300L;

	/**
	 * The distribution panel to configure the first distribution
	 */
	private DistributionPanel d1_panel;
	/**
	 * The distribution panel to configure the second distribution
	 */
	private DistributionPanel d2_panel;

	/**
	 * The title and identifier of the first distribution
	 */
	private String title1;
	/**
	 * The title and identifier of the second distribution
	 */
	private String title2;

	/**
	 * The components that need to be notified of changes in this interface.
	 */
	private ArrayList<Notifiable> notifiables = new ArrayList<Notifiable>();

	/**
	 * Create the dialog receiving the respective distributions and titles
	 * 
	 * @param d1
	 *            distribution of the first event
	 * @param d2
	 *            distribution of the second event
	 * @param title1
	 *            title of the first event
	 * @param title2
	 *            title of the second event
	 * @param owner
	 *            the owner for the modal
	 */
	public DistributionDoubleDialog(Distribution d1, Distribution d2, String title1, String title2, JFrame owner) {
		super(owner);
		setTitle("Distribution Dialog");
		this.title1 = title1;
		this.title2 = title2;

		setBounds(100, 100, 805, 459);
		getContentPane().setLayout(new BorderLayout());
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 2, 0, 10));
		{
			d1_panel = new DistributionPanel(d1, title1);

			contentPanel.add(d1_panel);
		}
		{
			d2_panel = new DistributionPanel(d2, title2);
			contentPanel.add(d2_panel);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setPreferredSize(new Dimension(75, 25));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						for (Iterator<Notifiable> i = notifiables.iterator(); i.hasNext();) {
							Notifiable notifiable = (Notifiable) i.next();
							notifiable.update();
						}
						DistributionDoubleDialog.this.setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

	/**
	 * Returns the configured distribution of the second event
	 * 
	 * @return the distribution that the event will follow
	 */
	public Distribution get_distribution1() {
		return d1_panel.get_distribution();
	}

	/**
	 * Returns the configured distribution of the second event
	 * 
	 * @return the distribution that the event will follow
	 */
	public Distribution get_distribution2() {
		return d2_panel.get_distribution();
	}

	/**
	 * Returns a representation of the distribution that has been configured
	 */
	public String toString() {

		return title1 + ": " + ((d1_panel.get_distribution() == null) ? "None" : d1_panel.get_distribution().toString())
				+ "\n" + title2 + ": "
				+ ((d2_panel.get_distribution() == null) ? "None" : d2_panel.get_distribution().toString());
	}

	/**
	 * Adds instances of components that need to be notified of changes in this
	 * interface
	 * 
	 * @param n
	 *            the instance that need to be notified
	 */
	public void addNotifiable(Notifiable n) {
		notifiables.add(n);
	}
}
