package simulator;

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JList;
import javax.swing.JCheckBox;

public class BatchMode extends JDialog {


	private static final long serialVersionUID = -6498782807057797553L;
	private JPanel contentPane;
	
	private JButton btn_pause;
	private JButton btn_resume;
	public JTextField tf_results_dir;
	public JTextField tf_experimental_file;
	private String EXPERIMENTAL_FILE = "";
	private static String RESULTS_DIR = "./";
	
	private JFileChooser jfc_experiment = new JFileChooser("./sample.csv");
	private JFileChooser jfc_results = new JFileChooser("./");
	
	private ControllerCSV controller;
	private JButton btn_start;
	private JButton btn_stop;
	private JButton btn_open_file;
	private JButton btn_open_experiment;
	private static OutputArea TA_OUTPUT;
	private JPanel panel;
	private JPanel panel_1;
	private JSpinner spinner;
	private JButton btnAddConfigurationFile;
	private JButton btnAddFolder;
	private JPanel panel_2;
	private JButton button;
	private JTextField textField;
	private JLabel lblFolderWithResults;
	
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
	public BatchMode(JFrame owner) {
		super (owner);
		
		setBounds(100, 100, 618, 581);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		jfc_experiment.setDialogTitle("Select an Experimental Design File");
		jfc_experiment.setSelectedFile(new File("sample.csv"));
		jfc_results.setDialogTitle("Select a Results Folder");
		jfc_results.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc_results.setAcceptAllFileFilterUsed(false);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 344, 582, 187);
		contentPane.add(scrollPane);
		
		TA_OUTPUT = new OutputArea();
		TA_OUTPUT.setEditable(false);
		scrollPane.setViewportView(TA_OUTPUT);
		
		controller = new ControllerCSV(TA_OUTPUT);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 582, 172);
		contentPane.add(tabbedPane);
		
		panel_1 = new JPanel();
		tabbedPane.addTab("Configuration Files", null, panel_1, null);
		panel_1.setLayout(null);
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(50), null, null, new Integer(1)));
		spinner.setBounds(89, 116, 55, 20);
		panel_1.add(spinner);
		
		JList list = new JList();
		list.setBounds(10, 13, 557, 92);
		panel_1.add(list);
		
		JLabel lblRepetitions = new JLabel("Repetitions:");
		lblRepetitions.setBounds(20, 117, 70, 14);
		panel_1.add(lblRepetitions);
		
		btnAddConfigurationFile = new JButton("Add Configuration Files ...");
		btnAddConfigurationFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnAddConfigurationFile.setBounds(408, 115, 159, 23);
		panel_1.add(btnAddConfigurationFile);
		
		btnAddFolder = new JButton("Add Folder");
		btnAddFolder.setBounds(266, 115, 132, 23);
		panel_1.add(btnAddFolder);
		
		panel = new JPanel();
		tabbedPane.addTab("CSV", null, panel, null);
		panel.setLayout(null);
		
		JLabel lblExperimentalDesignFile = new JLabel("Experimental Design File (CSV):");
		lblExperimentalDesignFile.setBounds(10, 11, 247, 15);
		panel.add(lblExperimentalDesignFile);
		
		tf_experimental_file = new JTextField();
		tf_experimental_file.setBounds(10, 38, 408, 27);
		panel.add(tf_experimental_file);
		tf_experimental_file.setEditable(false);
		tf_experimental_file.setColumns(10);
		tf_experimental_file.setText(jfc_experiment.getSelectedFile().getAbsolutePath());
		
		btn_open_experiment = new JButton("Open...");
		btn_open_experiment.setBounds(444, 39, 117, 25);
		panel.add(btn_open_experiment);
		
		panel_2 = new JPanel();
		tabbedPane.addTab("Catastrophic Setup", null, panel_2, null);
		panel_2.setLayout(null);
		
		button = new JButton("Open...");
		button.setBounds(444, 39, 117, 25);
		panel_2.add(button);
		
		textField = new JTextField();
		textField.setText("C:\\Users\\tico\\Desktop\\eclipse\\sample.csv");
		textField.setEditable(false);
		textField.setColumns(10);
		textField.setBounds(10, 38, 408, 27);
		panel_2.add(textField);
		
		lblFolderWithResults = new JLabel("Folder with results experments of Configuration Files or CSV:");
		lblFolderWithResults.setBounds(10, 11, 371, 15);
		panel_2.add(lblFolderWithResults);
		
		JCheckBox chckbxCollapse = new JCheckBox("Collapse");
		chckbxCollapse.setBounds(126, 94, 97, 23);
		panel_2.add(chckbxCollapse);
		
		JCheckBox chckbxInvasion = new JCheckBox("Invasion");
		chckbxInvasion.setBounds(225, 94, 97, 23);
		panel_2.add(chckbxInvasion);
		
		JCheckBox chckbxGenocide = new JCheckBox("Genocide");
		chckbxGenocide.setBounds(324, 94, 97, 23);
		panel_2.add(chckbxGenocide);
		btn_open_experiment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (jfc_experiment.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					tf_experimental_file.setText(jfc_experiment.getSelectedFile().getAbsolutePath());
					jfc_results.setCurrentDirectory(jfc_experiment.getCurrentDirectory());
					tf_results_dir.setText(jfc_experiment.getCurrentDirectory().getAbsolutePath());
					EXPERIMENTAL_FILE = tf_experimental_file.getText();
					try {
						controller.load_tasks(EXPERIMENTAL_FILE);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		btn_open_file = new JButton("Open...");
		btn_open_file.setBounds(475, 307, 117, 25);
		contentPane.add(btn_open_file);
		
		tf_results_dir = new JTextField();
		tf_results_dir.setBounds(10, 306, 455, 27);
		contentPane.add(tf_results_dir);
		tf_results_dir.setEditable(false);
		tf_results_dir.setColumns(10);
		tf_results_dir.setText(jfc_results.getCurrentDirectory().getAbsolutePath());
		
		JLabel lblResultsFolder = new JLabel("Results Folder:");
		lblResultsFolder.setBounds(10, 279, 128, 15);
		contentPane.add(lblResultsFolder);
		
		btn_start = new JButton("Start");
		btn_start.setBounds(10, 240, 117, 25);
		contentPane.add(btn_start);
		
		btn_pause = new JButton("Pause");
		btn_pause.setBounds(153, 240, 117, 25);
		contentPane.add(btn_pause);
		btn_pause.setEnabled(false);
		
		btn_resume = new JButton("Resume");
		btn_resume.setBounds(301, 240, 117, 25);
		contentPane.add(btn_resume);
		btn_resume.setEnabled(false);
		
		btn_stop = new JButton("Stop");
		btn_stop.setBounds(444, 240, 117, 25);
		contentPane.add(btn_stop);
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
		btn_resume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.resume_all();
				btn_start.setEnabled(false);
				btn_pause.setEnabled(true);
				btn_resume.setEnabled(false);
				btn_stop.setEnabled(true);
			}
		});
		btn_pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.suspend_all();
				btn_start.setEnabled(false);
				btn_pause.setEnabled(false);
				btn_resume.setEnabled(true);
				btn_stop.setEnabled(false);
			}
		});
		btn_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				EXPERIMENTAL_FILE = tf_experimental_file.getText();
				
				try {
					controller.load_tasks(EXPERIMENTAL_FILE);
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
				ControllerCSV.RESULTS_DIR = RESULTS_DIR;	
				
				controller.start();
				btn_start.setEnabled(false);
				btn_pause.setEnabled(true);
				btn_resume.setEnabled(false);
				btn_stop.setEnabled(true);
				btn_open_experiment.setEnabled(false);
				btn_open_file.setEnabled(false);

			}
		});
		btn_open_file.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (jfc_results.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					tf_results_dir.setText(jfc_results.getSelectedFile().getAbsolutePath());
				}
				
			}
		});
		DefaultCaret caret = (DefaultCaret)TA_OUTPUT.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}

	public static class OutputArea extends JTextArea implements Printable {
		@Override
		public void print(String str) {
			TA_OUTPUT.append(str);		
		}
	}
}
