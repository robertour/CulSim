package axelrod;

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class CulturalSimulator extends JFrame {


	private static final long serialVersionUID = -6498782807057797553L;
	private JPanel contentPane;
	
	private JButton btn_pause;
	private JButton btn_resume;
	public JTextField tf_results_dir;
	public JTextField tf_experimental_file;
	public static String EXPERIMENTAL_FILE = "";
	public static String RESULTS_DIR = "";
	
	private JFileChooser jfc_experiment = new JFileChooser("./sample.csv");
	private JFileChooser jfc_results = new JFileChooser("./");
	
	private Controller controller = new Controller();
	private JButton btn_start;
	private JButton btn_stop;
	private JButton btn_open_file;
	private JButton btn_open_experiment;
	public static JTextArea TA_OUTPUT;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CulturalSimulator frame = new CulturalSimulator();
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
	public CulturalSimulator() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 586, 409);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btn_start = new JButton("Start");
		btn_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				EXPERIMENTAL_FILE = tf_experimental_file.getText();
				
				try {
					controller.load_tasks();
					btn_start.setEnabled(true);
				} catch (FileNotFoundException e) {
					System.out.println("WARNING: Sample experimental file not found (" + EXPERIMENTAL_FILE + ")");
					e.printStackTrace();
				}
				
				File dir = new File(tf_results_dir.getText() + "/results/");
				
				for( int i = 0; dir.exists(); i++) {
					dir = new File(tf_results_dir.getText() + "/results" + i + "/");	
				}

				RESULTS_DIR = dir.getAbsolutePath() + "/";
				(new File(RESULTS_DIR + "/iterations/")).mkdirs();
				
				
				controller.start();
				btn_start.setEnabled(false);
				btn_pause.setEnabled(true);
				btn_resume.setEnabled(false);
				btn_stop.setEnabled(true);
				btn_open_experiment.setEnabled(false);
				btn_open_file.setEnabled(false);

			}
		});
		btn_start.setBounds(12, 169, 117, 25);
		contentPane.add(btn_start);
		
		btn_stop = new JButton("Stop");
		btn_stop.setEnabled(false);
		btn_stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.cancel_all();
				btn_start.setEnabled(true);
				btn_pause.setEnabled(false);
				btn_resume.setEnabled(false);
				btn_stop.setEnabled(false);
				btn_open_experiment.setEnabled(true);
				btn_open_file.setEnabled(true);
			}
		});
		btn_stop.setBounds(446, 169, 117, 25);
		contentPane.add(btn_stop);
		
		btn_pause = new JButton("Pause");
		btn_pause.setEnabled(false);
		btn_pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.suspend_all();
				btn_start.setEnabled(false);
				btn_pause.setEnabled(false);
				btn_resume.setEnabled(true);
				btn_stop.setEnabled(false);
			}
		});
		btn_pause.setBounds(155, 169, 117, 25);
		contentPane.add(btn_pause);
		
		btn_resume = new JButton("Resume");
		btn_resume.setEnabled(false);
		btn_resume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.resume_all();
				btn_start.setEnabled(false);
				btn_pause.setEnabled(true);
				btn_resume.setEnabled(false);
				btn_stop.setEnabled(true);
			}
		});
		btn_resume.setBounds(303, 169, 117, 25);
		contentPane.add(btn_resume);
		
		btn_open_experiment = new JButton("Open...");
		btn_open_experiment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (jfc_experiment.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					tf_experimental_file.setText(jfc_experiment.getSelectedFile().getAbsolutePath());
					jfc_results.setCurrentDirectory(jfc_experiment.getCurrentDirectory());
					tf_results_dir.setText(jfc_experiment.getCurrentDirectory().getAbsolutePath());
					EXPERIMENTAL_FILE = tf_experimental_file.getText();
					try {
						controller.load_tasks();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		btn_open_experiment.setBounds(446, 45, 117, 25);
		contentPane.add(btn_open_experiment);
		
		tf_results_dir = new JTextField();
		tf_results_dir.setEditable(false);
		tf_results_dir.setBounds(12, 110, 408, 27);
		contentPane.add(tf_results_dir);
		tf_results_dir.setColumns(10);
		
		JLabel lblExperimentalDesignFile = new JLabel("Experimental Design File:");
		lblExperimentalDesignFile.setBounds(12, 17, 247, 15);
		contentPane.add(lblExperimentalDesignFile);
		
		JLabel lblResultsFolder = new JLabel("Results Folder:");
		lblResultsFolder.setBounds(12, 83, 128, 15);
		contentPane.add(lblResultsFolder);
		
		tf_experimental_file = new JTextField();
		tf_experimental_file.setEditable(false);
		tf_experimental_file.setColumns(10);
		tf_experimental_file.setBounds(12, 44, 408, 27);
		
		
		
		contentPane.add(tf_experimental_file);
		
		jfc_experiment.setDialogTitle("Select an Experimental Design File");
		jfc_experiment.setSelectedFile(new File("sample.csv"));
		jfc_results.setDialogTitle("Select a Results Folder");
		jfc_results.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc_results.setAcceptAllFileFilterUsed(false);
		tf_experimental_file.setText(jfc_experiment.getSelectedFile().getAbsolutePath());
		tf_results_dir.setText(jfc_results.getCurrentDirectory().getAbsolutePath());
		btn_open_file = new JButton("Open...");
		btn_open_file.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (jfc_results.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					tf_results_dir.setText(jfc_results.getSelectedFile().getAbsolutePath());
				}
				
			}
		});
		btn_open_file.setBounds(446, 111, 117, 25);
		contentPane.add(btn_open_file);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 217, 551, 152);
		contentPane.add(scrollPane);
		
		TA_OUTPUT = new JTextArea();
		TA_OUTPUT.setEditable(false);
		scrollPane.setViewportView(TA_OUTPUT);
	}
}
