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
import javax.swing.ImageIcon;
import java.awt.Insets;

public class EventPanel extends JPanel implements Notifiable{
	private static final long serialVersionUID = -7027366295698613067L;
	private JTextArea ta_desc;
	
	JDialog confDialog;
	private JButton btnAdd;
	private JButton btnPlay;

	/**
	 * Create the panel.
	 */
	public EventPanel(String title, JDialog confDialog) {
		setBorder(new TitledBorder(null, title, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new BorderLayout(0, 0));
		
		this.confDialog = confDialog;
		
		ta_desc = new JTextArea();
		ta_desc.setLineWrap(true);
		add(ta_desc, BorderLayout.NORTH);
		ta_desc.setForeground(SystemColor.textHighlight);
		ta_desc.setBackground(SystemColor.control);
		ta_desc.setEditable(false);
		
		//ta_desc.setRows(2);
		ta_desc.setFont(new Font("Tahoma", Font.PLAIN, 9));
		ta_desc.setText(confDialog.toString());
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.setMargin(new Insets(2, 2, 2, 2));
		btnNewButton.setToolTipText("Configure Event");
		btnNewButton.setIcon(new ImageIcon(EventPanel.class.getResource("/simulator/img/configure.png")));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventPanel.this.confDialog.setVisible(true);
			}
		});
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
		panel.add(btnNewButton);
		
		btnAdd = new JButton("");
		btnAdd.setMargin(new Insets(2, 2, 2, 2));
		btnAdd.setIconTextGap(2);
		btnAdd.setToolTipText("Add event to the set");
		btnAdd.setIcon(new ImageIcon(EventPanel.class.getResource("/simulator/img/list-add.png")));
		panel.add(btnAdd);
		
		btnPlay = new JButton("");
		btnPlay.setMargin(new Insets(2, 2, 2, 2));
		btnPlay.setIcon(new ImageIcon(EventPanel.class.getResource("/simulator/img/arrow-right.png")));
		btnPlay.setToolTipText("Execute it immediately (if it is playing)");
		panel.add(btnPlay);

	}
	
	public void update(){
		ta_desc.setText(confDialog.toString());
	}
	
	public void addAddActionListener (ActionListener al){
		btnAdd.addActionListener(al);
	}
	
	public void addApplyActionListener (ActionListener al){
		btnPlay.addActionListener(al);
	}

}
