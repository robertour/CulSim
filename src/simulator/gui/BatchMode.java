package simulator.gui;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import simulator.CulturalSimulator;
import simulator.control.Controller;
import simulator.control.ControllerBatch;
import simulator.control.events.Event;

import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;

/**
 * Interface of the batch mode. It allows the configuration of repetitions of
 * the same experiment.
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 */
public class BatchMode extends JDialog implements Notifiable {

	private static final long serialVersionUID = -6498782807057797553L;

	/**
	 * The main panel of the simulations
	 */
	private JPanel contentPane;

	/**
	 * The main tabs of the simulations
	 */
	private JTabbedPane tp_batch_mode;
	private JPanel tab_conf_file;
	private JPanel tab_catastrophic;

	/**
	 * The file dialogs for the folder an file selection of the simulations
	 */
	private JFileChooser jfc_experiment = new JFileChooser(Controller.WORKSPACE_DIR + "sample.csv");
	private JFileChooser jfc_results = new JFileChooser(Controller.WORKSPACE_DIR);
	private JFileChooser jfc_disasters = new JFileChooser(Controller.WORKSPACE_DIR + Controller.EVENTS_DIR);
	private JFileChooser jfc_scenarios = new JFileChooser(Controller.WORKSPACE_DIR);
	private JFileChooser jfc_configurations = new JFileChooser(
			Controller.WORKSPACE_DIR + Controller.CONFIGURATIONS_DIR);

	/**
	 * Textfields to keep some of the paths that will be set in the simulations
	 */
	private JTextField tf_scenarios_dir;

	/**
	 * List of files that will be used in the simulations
	 */
	private DefaultListModel<String> conf_list;
	private ArrayList<String> file_list;

	/**
	 * Events for the catastrophic batch mode
	 */
	private ArrayList<Event> events = new ArrayList<Event>();
	private JTextArea ta_set_events;

	/**
	 * Number of repetitions of the different configurations
	 */
	private JSpinner sp_repetitions;
	private JSpinner sp_repetitions_events;

	/**
	 * Controls of the simulations
	 */
	private JToggleButton btn_play;
	private JToggleButton btn_stop;
	private JToggleButton btn_pause;
	private OutputArea taOutput;

	/**
	 * Batch controller of the simulations
	 */
	private ControllerBatch controller;

	/**
	 * Create the frame.
	 * 
	 * @param cultural_simulator
	 *            the interface of the cultural simulator batch
	 */
	public BatchMode(CulturalSimulator cultural_simulator) {
		super(cultural_simulator);
		setTitle("Batch Mode");

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				controller.cancel();
				dispose();
			}
		});

		setBounds(100, 100, 618, 551);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		jfc_experiment.setDialogTitle("Select an Experimental Design File");
		jfc_experiment.setSelectedFile(new File("sample.csv"));
		jfc_results.setDialogTitle("Select a Results Folder");
		jfc_results.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc_results.setAcceptAllFileFilterUsed(false);

		JScrollPane scrollOutput = new JScrollPane();
		scrollOutput.setBounds(10, 274, 582, 227);
		contentPane.add(scrollOutput);

		taOutput = new OutputArea();
		taOutput.setFont(new Font("Tahoma", Font.PLAIN, 9));
		taOutput.setLineWrap(true);
		taOutput.setEditable(false);
		scrollOutput.setViewportView(taOutput);

		controller = new ControllerBatch(taOutput, this);

		tp_batch_mode = new JTabbedPane(JTabbedPane.TOP);
		tp_batch_mode.setBounds(10, 11, 582, 208);
		contentPane.add(tp_batch_mode);

		conf_list = new DefaultListModel<String>();
		file_list = new ArrayList<String>();

		File conf_dir = new File(Controller.WORKSPACE_DIR + Controller.CONFIGURATIONS_DIR);
		if (!conf_dir.exists()) {
			conf_dir.mkdirs();
		}
		jfc_configurations.setCurrentDirectory(conf_dir);

		// File[] directoryListing =
		// jfc_configurations.getCurrentDirectory().listFiles();
		// if (directoryListing != null) {
		// for (File child : directoryListing) {
		// conf_list.addElement(child.getAbsolutePath());
		// file_list.add(child.getAbsolutePath());
		// }
		// }

		tab_conf_file = new JPanel();
		tp_batch_mode.addTab("From Configuration Files", null, tab_conf_file, null);
		tab_conf_file.setLayout(null);

		sp_repetitions = new JSpinner();
		sp_repetitions.setToolTipText("How many times each configuration will be repeated?");
		sp_repetitions.setModel(new SpinnerNumberModel(new Integer(10), new Integer(1), null, new Integer(1)));
		sp_repetitions.setBounds(96, 152, 55, 20);
		tab_conf_file.add(sp_repetitions);

		JLabel lblRepetitions = new JLabel("Repetitions:");
		lblRepetitions.setToolTipText("How many times each configuration will be repeated?");
		lblRepetitions.setBounds(10, 155, 70, 14);
		tab_conf_file.add(lblRepetitions);

		JButton btnAddConfigurationFile = new JButton("Add Files");
		btnAddConfigurationFile.setToolTipText("Add more configurations");
		btnAddConfigurationFile.setIcon(new ImageIcon(BatchMode.class.getResource("/simulator/img/document-open.png")));
		btnAddConfigurationFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File conf_dir = new File(Controller.WORKSPACE_DIR + Controller.CONFIGURATIONS_DIR);
				if (!conf_dir.exists()) {
					conf_dir.mkdirs();
					jfc_configurations.setCurrentDirectory(conf_dir);
				}
				jfc_configurations.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc_configurations.setMultiSelectionEnabled(true);
				if (jfc_configurations.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					File[] files = jfc_configurations.getSelectedFiles();
					for (int i = 0; i < files.length; i++) {
						conf_list.addElement(files[i].getAbsolutePath());
						file_list.add(files[i].getAbsolutePath());

					}

				}
			}
		});
		btnAddConfigurationFile.setBounds(462, 146, 105, 25);
		tab_conf_file.add(btnAddConfigurationFile);

		JButton btn_clearlist = new JButton("Clear");
		btn_clearlist.setToolTipText("Clear the configuration list");
		btn_clearlist.setIcon(new ImageIcon(BatchMode.class.getResource("/simulator/img/edit-clear-list.png")));
		btn_clearlist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conf_list.clear();
				file_list.clear();
			}
		});
		btn_clearlist.setBounds(347, 146, 105, 25);
		tab_conf_file.add(btn_clearlist);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(96, 11, 471, 123);
		tab_conf_file.add(scrollPane_1);

		JList<String> sel_conf_files = new JList<String>();
		sel_conf_files.setToolTipText(
				"Configurations of the simulations that are going to be tested. Usually saved with 'Simulation -> Parameters'");
		scrollPane_1.setViewportView(sel_conf_files);
		sel_conf_files.setModel(conf_list);

		JLabel lblConfigurations = new JLabel("Configurations:");
		lblConfigurations.setToolTipText(
				"Configurations of the simulations that are going to be tested. Usually saved with 'Simulation -> Parameters'");
		lblConfigurations.setLabelFor(scrollOutput);
		lblConfigurations.setBounds(10, 13, 85, 14);
		tab_conf_file.add(lblConfigurations);

		tab_catastrophic = new JPanel();
		tp_batch_mode.addTab("From Results Folder", null, tab_catastrophic, null);
		tab_catastrophic.setLayout(null);

		JButton btnOpen = new JButton("Select");
		btnOpen.setIcon(new ImageIcon(BatchMode.class.getResource("/simulator/img/document-open.png")));
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jfc_scenarios.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (jfc_scenarios.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					tf_scenarios_dir.setText(jfc_scenarios.getSelectedFile().getAbsolutePath() + File.separator);
				}
			}
		});
		btnOpen.setBounds(484, 10, 83, 25);
		tab_catastrophic.add(btnOpen);

		tf_scenarios_dir = new JTextField();
		tf_scenarios_dir.setToolTipText(
				"This folder serves as a starting point to apply the catastrophes. Basically, a folder with the results obtained in the other two tabs \"From Configuration Files\"");
		tf_scenarios_dir.setText(jfc_scenarios.getCurrentDirectory().getAbsolutePath() + File.separator);
		tf_scenarios_dir.setEditable(false);
		tf_scenarios_dir.setColumns(10);
		tf_scenarios_dir.setBounds(97, 10, 377, 25);
		tab_catastrophic.add(tf_scenarios_dir);

		JLabel lblFolderWithResults = new JLabel("Result Folder:");
		lblFolderWithResults.setToolTipText(
				"This folder serves as a starting point to apply the catastrophes. Basically, a folder with the results obtained in the other two tabs \"From Configuration Files\"");
		lblFolderWithResults.setBounds(10, 15, 70, 15);
		tab_catastrophic.add(lblFolderWithResults);

		JScrollPane scroll_pane = new JScrollPane();
		scroll_pane.setToolTipText("The events that will be applied to the scenarios contained in the result folder");
		scroll_pane.setBounds(97, 46, 377, 94);
		tab_catastrophic.add(scroll_pane);

		ta_set_events = new JTextArea();
		ta_set_events.setFont(new Font("Sans Serif", Font.PLAIN, 9));
		ta_set_events.setEditable(false);
		scroll_pane.setViewportView(ta_set_events);

		JButton btnLoad = new JButton("Load");
		btnLoad.setToolTipText("Load events from file");
		btnLoad.setIcon(new ImageIcon(BatchMode.class.getResource("/simulator/img/document-open.png")));
		btnLoad.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				if (jfc_disasters.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					String dis_file = jfc_disasters.getSelectedFile().getAbsolutePath();
					try {
						ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(dis_file));
						events.addAll((ArrayList<Event>) inFile.readObject());
						inFile.close();
						update_event_set();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnLoad.setBounds(484, 115, 85, 25);
		tab_catastrophic.add(btnLoad);

		JButton btnClearEvents = new JButton("Clear");
		btnClearEvents.setToolTipText("Clean event set");
		btnClearEvents.setIcon(new ImageIcon(BatchMode.class.getResource("/simulator/img/edit-clear-list.png")));
		btnClearEvents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				events.clear();
				update_event_set();
			}
		});
		btnClearEvents.setBounds(484, 81, 83, 25);
		tab_catastrophic.add(btnClearEvents);

		JLabel lblEvents = new JLabel("Events");
		lblEvents.setToolTipText("The events that will be applied to the scenarios contained in the result folder");
		lblEvents.setBounds(10, 47, 55, 15);
		tab_catastrophic.add(lblEvents);

		JLabel lblEventRepetitions = new JLabel("Event Repetitions:");
		lblEventRepetitions.setToolTipText("How many times will the events be repeated in each scenario");
		lblEventRepetitions.setBounds(10, 155, 99, 14);
		tab_catastrophic.add(lblEventRepetitions);

		sp_repetitions_events = new JSpinner();
		sp_repetitions_events.setToolTipText("How many times will the events be repeated in each scenario");
		sp_repetitions_events.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		sp_repetitions_events.setBounds(107, 152, 55, 20);
		tab_catastrophic.add(sp_repetitions_events);

		btn_play = new JToggleButton("Start");
		btn_play.setToolTipText("Start/Resume the simulations");
		btn_play.setIcon(new ImageIcon(BatchMode.class.getResource("/simulator/img/media-playback-start.png")));
		btn_play.setBounds(302, 230, 90, 25);
		contentPane.add(btn_play);

		btn_pause = new JToggleButton("Pause");
		btn_pause.setToolTipText("Pause the simulations");
		btn_pause.setIcon(new ImageIcon(BatchMode.class.getResource("/simulator/img/media-playback-pause.png")));
		btn_pause.setBounds(402, 230, 90, 25);
		contentPane.add(btn_pause);
		btn_pause.setEnabled(false);

		btn_stop = new JToggleButton("Stop");
		btn_stop.setToolTipText("Stop the simulations");
		btn_stop.setSelected(true);
		btn_stop.setIcon(new ImageIcon(BatchMode.class.getResource("/simulator/img/media-playback-stop.png")));
		btn_stop.setBounds(502, 230, 90, 25);
		contentPane.add(btn_stop);
		btn_stop.setEnabled(false);
		btn_stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (btn_stop.isEnabled()) {
					controller.cancel();

					btn_play.setEnabled(true);
					btn_play.setSelected(false);
					btn_pause.setEnabled(false);
					btn_stop.setEnabled(false);

					setEnableRec(tp_batch_mode, true);
				}
			}
		});
		btn_pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (btn_pause.isEnabled()) {
					controller.suspend();

					btn_play.setEnabled(true);
					btn_play.setSelected(false);
					btn_stop.setEnabled(false);
					btn_pause.setEnabled(false);
				}
			}
		});
		btn_play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (btn_play.isEnabled()) {

					if (btn_pause.isSelected() || btn_stop.isSelected()) {

						if (!btn_pause.isSelected() && btn_stop.isSelected()) {

							controller.clean_all();

							if (tp_batch_mode.getSelectedComponent() == tab_conf_file) {
								try {
									controller.load_configurations(file_list, null, (int) sp_repetitions.getValue());
								} catch (ClassNotFoundException | IOException e) {
									e.printStackTrace();
								}
								controller.start("results", Controller.WORKSPACE_DIR);

							} else if (tp_batch_mode.getSelectedComponent() == tab_catastrophic) {
								ArrayList<String> sim_list = new ArrayList<String>();

								File simulations_dir = new File(
										tf_scenarios_dir.getText() + ControllerBatch.SIMULATIONS_DIR);
								if (simulations_dir.exists() && simulations_dir.isDirectory()) {
									File[] directoryListing = simulations_dir.listFiles();
									if (directoryListing != null) {
										for (File child : directoryListing) {
											sim_list.add(child.getAbsolutePath());
										}
									} else {
										taOutput.print(-1,
												"The " + ControllerBatch.SIMULATIONS_DIR + " directory didn't contain "
														+ "any simulations. Please make sure you are providing a directory with "
														+ "the results of a Batch process.");
										return;
									}
								} else {
									taOutput.print(-1,
											"The " + ControllerBatch.SIMULATIONS_DIR + " directory was not found on "
													+ "the provided directory. Please make sure you are providing a directory with "
													+ "the results of a Batch process.");
									return;
								}
								try {
									controller.load_simulations(sim_list, events,
											(int) sp_repetitions_events.getValue());
								} catch (ClassNotFoundException | IOException e) {
									e.printStackTrace();
								}

								controller.start("results", tf_scenarios_dir.getText());
							}
							// Resume
						} else {
							controller.resume();
						}

						btn_play.setEnabled(false);
						btn_pause.setEnabled(true);
						btn_pause.setSelected(false);
						btn_stop.setEnabled(true);
						btn_stop.setSelected(false);
						tp_batch_mode.setEnabled(false);
						setEnableRec(tp_batch_mode, false);

					}
				}
			}
		});

		DefaultCaret caret = (DefaultCaret) taOutput.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}

	/**
	 * Update the event set in the interface
	 */
	private void update_event_set() {
		int i = 1;
		ta_set_events.setText("");
		for (Iterator<Event> iterator = events.iterator(); iterator.hasNext();) {
			Event event = (Event) iterator.next();
			ta_set_events.append(i + ". " + event + "\n");
			i++;
		}
	}

	@Override
	public void update() {
		if (btn_stop.isEnabled()) {
			btn_stop.doClick();
		}
	}

	/**
	 * Method that enables/disables the components recursively.
	 * 
	 * @param container
	 *            the container to enable/disable
	 * @param enable
	 *            the new set status
	 */
	private void setEnableRec(Component container, boolean enable) {
		container.setEnabled(enable);

		try {
			Component[] components = ((Container) container).getComponents();
			for (int i = 0; i < components.length; i++) {
				setEnableRec(components[i], enable);
			}
		} catch (ClassCastException e) {

		}
	}

}
