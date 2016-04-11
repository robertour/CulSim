package simulator;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import simulator.control.events.Distribution;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.event.ActionEvent;

/**
 * Interface that let you pick up and configure different distributions for one
 * event.
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 */
public class DistributionSingleDialog extends JDialog {

	private static final long serialVersionUID = -8199562513877809300L;

	/**
	 * The distribution panel to configure a distribution
	 */
	private DistributionPanel d_panel;

	/**
	 * The list of components this interface has to notify after changes are are
	 * applied
	 */
	private ArrayList<Notifiable> notifiables = new ArrayList<Notifiable>();

	/**
	 * Create a distribution dialog panel
	 * 
	 * @param d
	 *            the initial distribution that will be showed
	 * @param title
	 *            the tile and identifier name for the dialog
	 * @param owner
	 *            the owner for the modal
	 */
	public DistributionSingleDialog(Distribution d, String title, JFrame owner) {
		super(owner);

		setTitle("Distribution Dialog");

		setBounds(100, 100, 214, 543);
		getContentPane().setLayout(new BorderLayout());
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			d_panel = new DistributionPanel(d, title);

			contentPanel.add(d_panel, BorderLayout.CENTER);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						for (Iterator<Notifiable> i = notifiables.iterator(); i.hasNext();) {
							Notifiable notifiable = (Notifiable) i.next();
							notifiable.update();
						}
						DistributionSingleDialog.this.setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

	/**
	 * Returns the configured distribution of the event
	 * 
	 * @return the distribution that the event will follow
	 */
	public Distribution get_distribution() {
		return d_panel.get_distribution();
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

	/**
	 * Returns a representation of the distribution that has been configured
	 */
	public String toString() {
		return ((d_panel.get_distribution() == null) ? "None" : d_panel.get_distribution().toString());
	}
}
