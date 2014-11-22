package axelrod;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;

public class MyFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6498782807057797553L;
	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MyFrame frame = new MyFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MyFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnMyButton = new JButton("My Button");
		btnMyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Controller controller = new Controller();
				try {
					controller.open();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnMyButton.setBounds(148, 208, 117, 25);
		contentPane.add(btnMyButton);
		
		textField = new JTextField();
		textField.setBounds(151, 147, 114, 19);
		contentPane.add(textField);
		textField.setColumns(10);
	}
}
