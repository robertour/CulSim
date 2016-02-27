package simulator;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.BorderLayout;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import java.awt.FlowLayout;

public class EventPanel extends JPanel implements Notifiable{
	private static final long serialVersionUID = -7027366295698613067L;
	private JTextArea ta_desc;
	
	JDialog confDialog;
	private JButton btnApply;

	/**
	 * Create the panel.
	 */
	public EventPanel(String title, JDialog confDialog) {
		setBorder(new TitledBorder(null, title, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new BorderLayout(0, 0));
		
		this.confDialog = confDialog;
		
		ta_desc = new JTextArea();
		ta_desc.setForeground(SystemColor.textHighlight);
		ta_desc.setBackground(SystemColor.control);
		ta_desc.setEditable(false);
		
		ta_desc.setRows(2);
		ta_desc.setFont(new Font("Tahoma", Font.PLAIN, 9));
		ta_desc.setText(confDialog.toString());
		add(ta_desc, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		
		JButton btnNewButton = new JButton("Configure");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventPanel.this.confDialog.setVisible(true);
			}
		});
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
		panel.add(btnNewButton);
		
		btnApply = new JButton("Ok");
		panel.add(btnApply);

	}
	
	public void update(){
		ta_desc.setText(confDialog.toString());
	}
	
	public void addActionListener (ActionListener al){
		btnApply.addActionListener(al);
	}

}
