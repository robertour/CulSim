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

public class SingleDistributionDialog extends JDialog {
	
	private static final long serialVersionUID = -8199562513877809300L;
	
	private final JPanel contentPanel = new JPanel();
	private DistributionPanel d_panel;
	private ArrayList<Notifiable> notifiables = new ArrayList<Notifiable>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Distribution d = new Distribution(0.1);
			
			SingleDistributionDialog dialog = new SingleDistributionDialog(d, "Example", null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SingleDistributionDialog(Distribution d, String title, JFrame owner) {
		super(owner);

		setTitle("Distribution Dialog");
		
		setBounds(100, 100, 214, 543);
		getContentPane().setLayout(new BorderLayout());
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
						SingleDistributionDialog.this.setVisible(false);						
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
	
	public Distribution get_distribution() {
		return d_panel.get_distribution();
	}
	
	public void addNotifiable(Notifiable n){
		notifiables.add(n);
	}
	
	public String toString(){
		return ((d_panel.get_distribution()== null)?"None":d_panel.get_distribution().toString());
	}
}
