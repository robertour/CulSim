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
import java.awt.GridLayout;

public class DoubleDistributionDialog extends JDialog {
	
	private static final long serialVersionUID = -8199562513877809300L;
	
	private final JPanel contentPanel = new JPanel();
	private DistributionPanel d1_panel;
	private DistributionPanel d2_panel;
	private String title1;
	private String title2;
	private ArrayList<Notifiable> notifiables = new ArrayList<Notifiable>();


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Distribution d1 = new Distribution(0.1);
			Distribution d2 = new Distribution(0.1);
			DoubleDistributionDialog dialog = new DoubleDistributionDialog(d1, d2, "Example 1", "Example 2", null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DoubleDistributionDialog(Distribution d1, Distribution d2, String title1, 
			String title2, JFrame owner) {
		super(owner);
		setTitle("Distribution Dialog");
		this.title1 = title1;
		this.title2 = title2;
		
		setBounds(100, 100, 375, 379);
		getContentPane().setLayout(new BorderLayout());
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
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						for (Iterator<Notifiable> i = notifiables.iterator(); i.hasNext();) {
							Notifiable notifiable = (Notifiable) i.next();
							notifiable.update();
						}
						DoubleDistributionDialog.this.setVisible(false);						
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
	
	public Distribution get_distribution1() {
		return d1_panel.get_distribution();
	}
	
	public Distribution get_distribution2() {
		return d2_panel.get_distribution();
	}
	
	public String toString(){
		return title1 + ": " + d1_panel.get_distribution() + "\n" +
				title2 + ": " + d2_panel.get_distribution();
	}
	
	public void addNotifiable(Notifiable n){
		notifiables.add(n);
	}
}
