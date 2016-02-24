package simulator;

import java.awt.EventQueue;
import java.awt.Graphics2D;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import simulator.control.Controller;
import simulator.control.ControllerSingle;
import simulator.control.ConvertInstitutions;
import simulator.control.ConvertTraits;
import simulator.control.DestroyInstitutionsContent;
import simulator.control.DestroyInstitutionsStructure;
import simulator.control.Event;
import simulator.control.Genocide;
import simulator.control.Invasion;

import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.RenderingHints;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JToggleButton;
import java.awt.Color;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.Font;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

public class CulturalSimulator extends JFrame {


	private static final long serialVersionUID = -6498782807057797553L;
	private JPanel contentPane;
	
	public static String EXPERIMENTAL_FILE = "";
	public static String RESULTS_DIR = "";
	
	private JFileChooser jfc_load = new JFileChooser(Controller.WORKSPACE_DIR + Controller.WORLDS_DIR);
	
	public static ControllerSingle controller;
	private ArrayList<Event> events = new ArrayList<Event>();
	
	private OutputArea output_area;
	private static JLabel belief_space = new JLabel("");
	private static JLabel alife_institutional_beliefs_space = new JLabel("");
	private static JLabel alife_institutions = new JLabel("");
	private static JLabel institutional_beliefs_association = new JLabel("");
	private final Action action = new SwingAction();
	public static CulturalParameters parameters_dialog;
	public static BatchMode batch_mode_dialog;
	private JToggleButton tglbtnPlay;
	private JToggleButton tglbtnPause;
	private JToggleButton tglbtnStop;
	private JMenuItem mntmPlay;
	private JMenuItem mntmPause;
	private JMenuItem mntmStop;
	private JMenuItem mntmBatchMode;
	private JPanel panel;
	private JPanel panel_2;
	private JPanel panel_3;
	private JPanel panel_4;
	private JPanel panel_5;
	private JPanel panel_6;
	private JLabel lblBeliefSpace;
	private JLabel lblCorrespondinInstitutionalBelief;
	private JLabel lblNewLabel;
	private JLabel lblExistentInstitutions;
	private JMenuItem mntmSafeWorldState;
	private JMenuItem mntmLoadWorldState;
	private JMenuItem mntmExit;
	private JButton btnCollapse;
	private JButton btnInvasion;
	private JButton btnGenocide;
	private JButton btnClear;
	private JMenu mnSimulation;
	private JMenuItem mntmClear;
	private JSplitPane splitPane_1;
	private JPanel panel_7;
	private JButton btnDestroyInstitutionsStructure;
	private JButton btnConversion;
	private JButton btnTraitConversion;
	private JPanel panel_1;
	private JPanel panel_8;
	private JPanel panel_9;
	private JSpinner sp_convert;
	private JSpinner sp_convert_traits;
	private JPanel panel_10;
	private JSpinner sp_invasion;
	private JSpinner sp_genocide;
	private JSplitPane splitPane_2;
	private JPanel panel_11;
	private JPanel panel_12;
	public static GraphPanel graph_cultures;
	public static JLabel l_start_identification;
	private JPanel panel_13;
	private JPanel panel_14;
	private JPanel panel_15;
	private JLabel lblInstitutions;
	private JLabel lblEnergy;
	public static GraphPanel graph_institutions;
	public static GraphPanel graph_borderless_cultures;
	public static GraphPanel graph_energy_foreign_trait;
	private JPanel panel_19;
	public static JLabel l_energy_foreigners;
	public static  JLabel l_cultures;
	public static JLabel l_borderless;
	public static JLabel l_institutions;
	private JPanel panel_20;
	public static JLabel l_current_identification;
	private JPanel panel_21;
	private JPanel panel_22;
	private JLabel label;
	private JLabel label_1;
	private JSpinner spinner;
	private JSpinner spinner_1;
	private JPanel panel_23;
	private JLabel label_2;
	private JLabel label_3;
	public static JSpinner sp_mutation;
	public static JSpinner sp_selection_error;
	private JPanel panel_24;
	private JLabel label_4;
	private JLabel label_5;
	private JLabel label_6;
	public static JSpinner sp_influence;
	public static JSpinner sp_loyalty;
	public static JSpinner sp_democracy;
	private JLabel label_7;
	public static JSpinner sp_propaganda;
	private JTabbedPane tabbedPane;
	private JPanel panel_25;
	private JPanel panel_26;
	private JButton btn_add_destroy_content;
	private JButton btn_add_destroy_structure;
	private JButton btn_convert_institutions;
	private JButton btn_convert_traits;
	private JSpinner sp_add_convert;
	private JSpinner sp_add_convert_traits;
	private JSpinner sp_add_content;
	private JSpinner sp_add_structure;
	private JPanel panel_27;
	private JButton btn_invasion;
	private JButton btn_genocide;
	private JSpinner sp_add_invasion;
	private JSpinner sp_add_genocide;
	private JButton button_6;
	private JSpinner sp_content;
	private JSpinner sp_structure;
	private JTextArea ta_event_set;
	private JPanel panel_28;
	private JPanel panel_29;
	private JLabel lbl_similarity;
	public static JLabel l_similarity;
	public static GraphPanel graph_similarity;
	private JButton btnReload;
	private JMenuItem mntmReload;
	private JButton btnSave;
	private JMenuItem mntmSave;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
		setTitle("Cultural Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 895, 722);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	if (want_to_continue(CulturalSimulator.this)){
            		controller.cancel();
            		dispose();
            	}                
            }
        });
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setHorizontalTextPosition(SwingConstants.LEFT);
		mnFile.setHorizontalAlignment(SwingConstants.LEFT);
		menuBar.add(mnFile);
		
		mntmSafeWorldState = new JMenuItem("Safe World State");
		mntmSafeWorldState.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jfc_load.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					if (want_to_continue(jfc_load)){
						String conf_file = jfc_load.getSelectedFile().getAbsolutePath();
						controller.save_simulation(conf_file);
					}
				}
			}
		});
		mnFile.add(mntmSafeWorldState);
		
		mntmLoadWorldState = new JMenuItem("Load World State");
		mntmLoadWorldState.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jfc_load.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					if (want_to_continue(jfc_load)){
						String conf_file = jfc_load.getSelectedFile().getAbsolutePath();
						try {
							controller.load_simulation(conf_file);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} 
				}
			}
		});
		mnFile.add(mntmLoadWorldState);
		mnFile.addSeparator();
		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (want_to_continue(CulturalSimulator.this)){
					controller.cancel();
					CulturalSimulator.this.dispose();
            	}  
			}
		});
		mnFile.add(mntmExit);
		
		JMenu mnControls = new JMenu("Controls");
		menuBar.add(mnControls);
		
		mntmPlay = new JMenuItem("Play");
		mntmPlay.addActionListener(new PlayAL());
		mnControls.add(mntmPlay);
		
		mntmPause = new JMenuItem("Pause");
		mntmPause.addActionListener(new PauseAL());
		mnControls.add(mntmPause);
		
		mntmStop = new JMenuItem("Stop");
		mntmStop.addActionListener(new StopAL());
		mnControls.add(mntmStop);
		
		mntmClear = new JMenuItem("Clear");
		mntmClear.addActionListener(new ClearAL());
		mnControls.add(mntmClear);
		
		mntmReload = new JMenuItem("Reload");
		mntmReload.addActionListener(new ReloadAL());
		
		mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new SaveAL());
		mnControls.add(mntmSave);
		mnControls.add(mntmReload);
		
		mnSimulation = new JMenu("Simulation");
		menuBar.add(mnSimulation);
		
		JMenuItem mntmDefineParameters = new JMenuItem("Parameters");
		mntmDefineParameters.setAction(action);
		mnSimulation.add(mntmDefineParameters);
		mnSimulation.addSeparator();
		
		mntmBatchMode = new JMenuItem("Batch Mode");
		mntmBatchMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				batch_mode_dialog.setVisible(true);
			}
		});
		mnSimulation.add(mntmBatchMode);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		contentPane.setLayout(new BorderLayout(0, 0));
		
		splitPane_2 = new JSplitPane();
		contentPane.add(splitPane_2, BorderLayout.CENTER);
		
		panel_11 = new JPanel();
		splitPane_2.setRightComponent(panel_11);
		panel_11.setLayout(new GridLayout(4, 2, 0, 0));
		
		panel_12 = new JPanel();
		panel_11.add(panel_12);
		panel_12.setLayout(new BorderLayout(0, 0));
		
		graph_cultures = new GraphPanel();
		panel_12.add(graph_cultures, BorderLayout.CENTER);
		
		JPanel panel_16 = new JPanel();
		panel_12.add(panel_16, BorderLayout.NORTH);
		panel_16.setLayout(new BorderLayout(0, 0));
		
		JLabel lblCultures_1 = new JLabel("Cultures:");
		lblCultures_1.setToolTipText("Blue line (left number) is the number of cultures, and red line (right number) the size of the biggest culture. The cultures are defined through the scope of immediate neighbors.");
		panel_16.add(lblCultures_1, BorderLayout.WEST);
		
		l_cultures = new JLabel("");
		l_cultures.setToolTipText("Blue line (left number) is the number of cultures, and red line (right number) the size of the biggest culture. The cultures are defined through the scope of immediate neighbors.");
		panel_16.add(l_cultures, BorderLayout.EAST);
		
		panel_14 = new JPanel();
		panel_11.add(panel_14);
		panel_14.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_18 = new JPanel();
		panel_14.add(panel_18, BorderLayout.NORTH);
		panel_18.setLayout(new BorderLayout(0, 0));
		
		lblInstitutions = new JLabel("Borderless Cultures:");
		lblInstitutions.setToolTipText("Blue line (left number) is the number of cultures, and red line (right number) the size of the biggest culture. The cultures are defined through the scope of the neighborhood radius.");
		panel_18.add(lblInstitutions, BorderLayout.WEST);
		lblInstitutions.setHorizontalAlignment(SwingConstants.LEFT);
		
		l_borderless = new JLabel("");
		l_borderless.setToolTipText("Blue line (left number) is the number of cultures, and red line (right number) the size of the biggest culture. The cultures are defined through the scope of the neighborhood radius.");
		panel_18.add(l_borderless, BorderLayout.EAST);
		
		graph_borderless_cultures = new GraphPanel();
		panel_14.add(graph_borderless_cultures, BorderLayout.CENTER);
		
		panel_13 = new JPanel();
		panel_11.add(panel_13);
		panel_13.setLayout(new BorderLayout(0, 0));
		
		graph_institutions = new GraphPanel();
		panel_13.add(graph_institutions, BorderLayout.CENTER);
		
		JPanel panel_17 = new JPanel();
		panel_13.add(panel_17, BorderLayout.NORTH);
		panel_17.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel_1 = new JLabel("Institutions:");
		lblNewLabel_1.setToolTipText("Blue line (left number) is the number of instititutions, and red line (right number) the size of the biggest institution");
		panel_17.add(lblNewLabel_1, BorderLayout.WEST);
		
		l_institutions = new JLabel("");
		l_institutions.setToolTipText("Blue line (left number) is the number of instititutions, and red line (right number) the size of the biggest institution");
		panel_17.add(l_institutions, BorderLayout.EAST);
		
		panel_15 = new JPanel();
		panel_11.add(panel_15);
		panel_15.setLayout(new BorderLayout(0, 0));
		
		panel_19 = new JPanel();
		panel_15.add(panel_19, BorderLayout.NORTH);
		panel_19.setLayout(new BorderLayout(0, 0));
		
		lblEnergy = new JLabel("Energy");
		lblEnergy.setToolTipText("Blue line (left number) is the energy of the system, and red line (right number) is the dispersion of the foreign trait.");
		panel_19.add(lblEnergy, BorderLayout.NORTH);
		lblEnergy.setHorizontalAlignment(SwingConstants.LEFT);
		
		l_energy_foreigners = new JLabel("");
		l_energy_foreigners.setToolTipText("Blue line (left number) is the energy of the system, and red line (right number) is the dispersion of the foreign trait.");
		panel_19.add(l_energy_foreigners, BorderLayout.EAST);
		
		graph_energy_foreign_trait = new GraphPanel();
		panel_15.add(graph_energy_foreign_trait, BorderLayout.CENTER);
		
		splitPane_1 = new JSplitPane();
		splitPane_2.setLeftComponent(splitPane_1);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane_1.setRightComponent(splitPane);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		panel = new JPanel();
		splitPane.setLeftComponent(panel);
		panel.setLayout(new GridLayout(2, 2, 10, 10));
		
		panel_5 = new JPanel();
		panel.add(panel_5);
		panel_5.setLayout(new BorderLayout(0, 0));
		
		lblBeliefSpace = new JLabel("Belief Space");
		lblBeliefSpace.setHorizontalTextPosition(SwingConstants.CENTER);
		lblBeliefSpace.setHorizontalAlignment(SwingConstants.CENTER);
		panel_5.add(lblBeliefSpace, BorderLayout.NORTH);
		
		
		panel_5.add(belief_space, BorderLayout.CENTER);
		belief_space.setBackground(Color.WHITE);
		belief_space.setOpaque(true);
		
		panel_4 = new JPanel();
		panel.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		lblCorrespondinInstitutionalBelief = new JLabel("Corresponding Institutional Belief Space");
		lblCorrespondinInstitutionalBelief.setHorizontalAlignment(SwingConstants.CENTER);
		lblCorrespondinInstitutionalBelief.setHorizontalTextPosition(SwingConstants.CENTER);
		panel_4.add(lblCorrespondinInstitutionalBelief, BorderLayout.NORTH);
		
		
		panel_4.add(institutional_beliefs_association, BorderLayout.CENTER);
		institutional_beliefs_association.setOpaque(true);
		institutional_beliefs_association.setBackground(Color.WHITE);
		
		panel_3 = new JPanel();
		panel.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		lblNewLabel = new JLabel("Institutions");
		lblNewLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel_3.add(lblNewLabel, BorderLayout.NORTH);
		
		
		panel_3.add(alife_institutional_beliefs_space, BorderLayout.CENTER);
		alife_institutional_beliefs_space.setBackground(Color.WHITE);
		alife_institutional_beliefs_space.setOpaque(true);
		
		panel_6 = new JPanel();
		panel.add(panel_6);
		panel_6.setLayout(new BorderLayout(0, 0));
		
		lblExistentInstitutions = new JLabel("Existent Institutions");
		lblExistentInstitutions.setHorizontalTextPosition(SwingConstants.CENTER);
		lblExistentInstitutions.setHorizontalAlignment(SwingConstants.CENTER);
		panel_6.add(lblExistentInstitutions, BorderLayout.NORTH);
		
		
		panel_6.add(alife_institutions, BorderLayout.CENTER);
		alife_institutions.setOpaque(true);
		alife_institutions.setBackground(Color.WHITE);
		
		panel_2 = new JPanel();
		splitPane.setRightComponent(panel_2);
		panel_2.setMinimumSize(new Dimension(10, 100));
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setMinimumSize(new Dimension(23, 200));
		panel_2.add(scrollPane);
		
		output_area = new OutputArea();
		output_area.setEditable(false);
		scrollPane.setViewportView(output_area);
		splitPane.setDividerLocation(500);
		
		controller = new ControllerSingle(output_area);
		parameters_dialog =  new CulturalParameters(this);
		batch_mode_dialog = new BatchMode(this);
		
		panel_7 = new JPanel();
		splitPane_1.setLeftComponent(panel_7);
		panel_7.setLayout(new BorderLayout(0, 0));
		
		panel_1 = new JPanel();
		panel_7.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setPreferredSize(new Dimension(5, 375));
		panel_1.add(tabbedPane, BorderLayout.NORTH);
		
		panel_8 = new JPanel();
		tabbedPane.addTab("Event", null, panel_8, null);
		panel_8.setPreferredSize(new Dimension(10, 270));
		panel_8.setBorder(null);
		panel_8.setLayout(null);
		
		panel_9 = new JPanel();
		panel_9.setBounds(5, 5, 180, 145);
		panel_9.setBorder(new TitledBorder(null, "Institutions", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_8.add(panel_9);
		panel_9.setLayout(null);
		
		btnCollapse = new JButton("Destroy Content");
		btnCollapse.setBounds(10, 20, 120, 23);
		panel_9.add(btnCollapse);
		
		btnDestroyInstitutionsStructure = new JButton("Destroy Structure");
		btnDestroyInstitutionsStructure.setBounds(10, 50, 120, 23);
		panel_9.add(btnDestroyInstitutionsStructure);
		
		btnConversion = new JButton("Convert");
		btnConversion.setBounds(10, 80, 120, 23);
		panel_9.add(btnConversion);
		
		btnTraitConversion = new JButton("Convert Traits");
		btnTraitConversion.setBounds(10, 110, 120, 23);
		panel_9.add(btnTraitConversion);
		
		sp_convert = new JSpinner();
		sp_convert.setModel(new SpinnerNumberModel(new Double(0.1), new Double(0.0), new Double(1.0), new Double(0.1)));
		sp_convert.setBounds(135, 81, 39, 20);
		panel_9.add(sp_convert);
		
		sp_convert_traits = new JSpinner();
		sp_convert_traits.setModel(new SpinnerNumberModel(new Double(0.1), new Double(0.0), new Double(1.0), new Double(0.1)));
		sp_convert_traits.setBounds(135, 110, 39, 20);
		panel_9.add(sp_convert_traits);
		
		sp_content = new JSpinner();
		sp_content.setModel(new SpinnerNumberModel(new Double(1.0), new Double(0.0), new Double(1.0), new Double(0.1)));
		sp_content.setBounds(135, 21, 39, 20);
		panel_9.add(sp_content);
		
		sp_structure = new JSpinner();
		sp_structure.setModel(new SpinnerNumberModel(new Double(1.0), new Double(0.0), new Double(1.0), new Double(0.1)));
		sp_structure.setBounds(135, 51, 39, 20);
		panel_9.add(sp_structure);
		
		panel_10 = new JPanel();
		panel_10.setBounds(5, 150, 180, 83);
		panel_10.setBorder(new TitledBorder(null, "Population", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_8.add(panel_10);
		panel_10.setLayout(null);
		
		btnInvasion = new JButton("Invasion");
		btnInvasion.setBounds(10, 20, 120, 23);
		panel_10.add(btnInvasion);
		
		btnGenocide = new JButton("Genocide");
		btnGenocide.setBounds(10, 50, 120, 23);
		panel_10.add(btnGenocide);
		
		sp_invasion = new JSpinner();
		sp_invasion.setModel(new SpinnerNumberModel(new Integer(6), 1, null, new Integer(1)));
		sp_invasion.setBounds(135, 20, 39, 20);
		panel_10.add(sp_invasion);
		
		sp_genocide = new JSpinner();
		sp_genocide.setModel(new SpinnerNumberModel(new Double(0.1), new Double(0.0), new Double(1.0), new Double(0.1)));
		sp_genocide.setBounds(135, 49, 39, 20);
		panel_10.add(sp_genocide);
		btnGenocide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.add_event(new Genocide((double) sp_genocide.getValue()));
			}
		});
		btnInvasion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.add_event(new Invasion((int) sp_invasion.getValue()));
			}
		});
		btnTraitConversion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.add_event(new ConvertTraits((double) sp_convert_traits.getValue()));
			}
		});
		btnConversion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.add_event(new ConvertInstitutions((double) sp_convert.getValue()));
			}
		});
		btnDestroyInstitutionsStructure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.add_event(new DestroyInstitutionsStructure((double) sp_structure.getValue()));
			}
		});
		btnCollapse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.add_event(new DestroyInstitutionsContent((double) sp_content.getValue()));
			}
		});
		
		panel_21 = new JPanel();
		tabbedPane.addTab("Parameters", null, panel_21, null);
		panel_21.setBorder(null);
		panel_21.setLayout(null);
		
		panel_22 = new JPanel();
		panel_22.setBounds(94, 21, 1, 1);
		panel_22.setLayout(null);
		panel_22.setBorder(new TitledBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Noise", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_21.add(panel_22);
		
		label = new JLabel("Mutation:");
		label.setToolTipText("How often a random change in a feature occurs?");
		label.setBounds(10, 23, 74, 14);
		panel_22.add(label);
		
		label_1 = new JLabel("Selection Error:");
		label_1.setToolTipText("How often do you want to save results, update graphs, check for Pause/Stop/Resume states?");
		label_1.setBounds(10, 48, 85, 14);
		panel_22.add(label_1);
		
		spinner = new JSpinner();
		spinner.setToolTipText("How often a random change in a feature occurs?");
		spinner.setBounds(94, 20, 70, 20);
		panel_22.add(spinner);
		
		spinner_1 = new JSpinner();
		spinner_1.setToolTipText("How often an agent confuses the selection of an agent that can be influnce by or not?");
		spinner_1.setBounds(94, 45, 70, 20);
		panel_22.add(spinner_1);
		
		panel_23 = new JPanel();
		panel_23.setLayout(null);
		panel_23.setBorder(new TitledBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Noise", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_23.setBounds(5, 5, 180, 78);
		panel_21.add(panel_23);
		
		label_2 = new JLabel("Mutation:");
		label_2.setToolTipText("How often a random change in a feature occurs?");
		label_2.setBounds(10, 23, 74, 14);
		panel_23.add(label_2);
		
		label_3 = new JLabel("Selection Error:");
		label_3.setToolTipText("How often do you want to save results, update graphs, check for Pause/Stop/Resume states?");
		label_3.setBounds(10, 48, 85, 14);
		panel_23.add(label_3);
		
		sp_mutation = new JSpinner();
		sp_mutation.setModel(new SpinnerNumberModel(new Float(0.00001), new Float(0), new Float(1), new Float(0.000001)));
		((JSpinner.NumberEditor) sp_mutation.getEditor()).getFormat().setMinimumFractionDigits(6);
		sp_mutation.setValue(0.000001f);
		sp_mutation.setToolTipText("How often a random change in a feature occurs?");
		sp_mutation.setBounds(90, 20, 80, 20);
		panel_23.add(sp_mutation);
		
		sp_selection_error = new JSpinner();
		sp_selection_error.setModel(new SpinnerNumberModel(new Float(0.00001), new Float(0), new Float(1), new Float(0.000001)));
		((JSpinner.NumberEditor) sp_selection_error.getEditor()).getFormat().setMinimumFractionDigits(6);
		sp_selection_error.setValue(0.000001f);
		sp_selection_error.setToolTipText("How often an agent confuses the selection of an agent that can be influnce by or not?");
		sp_selection_error.setBounds(90, 45, 80, 20);
		panel_23.add(sp_selection_error);
		
		panel_24 = new JPanel();
		panel_24.setLayout(null);
		panel_24.setBorder(new TitledBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Controls4", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), "Institutions", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_24.setBounds(5, 84, 180, 124);
		panel_21.add(panel_24);
		
		label_4 = new JLabel("Influence:");
		label_4.setToolTipText("Institutional influence over the agent");
		label_4.setBounds(10, 23, 64, 14);
		panel_24.add(label_4);
		
		label_5 = new JLabel("Loyalty:");
		label_5.setToolTipText("Agent's loyalty towards the institution");
		label_5.setBounds(10, 48, 64, 14);
		panel_24.add(label_5);
		
		label_6 = new JLabel("Democracy:");
		label_6.setToolTipText("How often a democratic process occurs?");
		label_6.setBounds(10, 73, 64, 14);
		panel_24.add(label_6);
		
		sp_influence = new JSpinner();
		sp_influence.setModel(new SpinnerNumberModel(new Float(0.5), new Float(0), new Float(1), new Float(0.05)));
		((JSpinner.NumberEditor) sp_influence.getEditor()).getFormat().setMinimumFractionDigits(3);
		sp_influence.setValue(0.85f);
		sp_influence.setToolTipText("Institutional influence over the agent");
		sp_influence.setBounds(90, 20, 80, 20);
		panel_24.add(sp_influence);
		
		sp_loyalty = new JSpinner();
		sp_loyalty.setModel(new SpinnerNumberModel(new Float(0.5), new Float(0), new Float(1), new Float(0.05)));		
		((JSpinner.NumberEditor) sp_loyalty.getEditor()).getFormat().setMinimumFractionDigits(3);
		sp_loyalty.setValue(0.5f);
		sp_loyalty.setToolTipText("Agent's loyalty towards the institution");
		sp_loyalty.setBounds(90, 45, 80, 20);
		panel_24.add(sp_loyalty);
		
		sp_democracy = new JSpinner();
		sp_democracy.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		sp_democracy.setToolTipText("How often a democratic process occurs?");
		sp_democracy.setBounds(90, 70, 80, 20);
		panel_24.add(sp_democracy);
		
		label_7 = new JLabel("Propaganda:");
		label_7.setToolTipText("How often a propaganda process occurs?");
		label_7.setBounds(10, 98, 64, 14);
		panel_24.add(label_7);
		
		sp_propaganda = new JSpinner();
		sp_propaganda.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		sp_propaganda.setToolTipText("How often a propaganda process occurs?");
		sp_propaganda.setBounds(90, 95, 80, 20);
		panel_24.add(sp_propaganda);
		
		JButton btnSetParameters = new JButton("Set Parameters");
		btnSetParameters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.setParameters();
			}
		});
		btnSetParameters.setBounds(5, 215, 180, 23);
		panel_21.add(btnSetParameters);
		
		panel_25 = new JPanel();
		panel_25.setLayout(null);
		panel_25.setPreferredSize(new Dimension(10, 270));
		panel_25.setBorder(null);
		tabbedPane.addTab("Event Set", null, panel_25, null);
		
		panel_26 = new JPanel();
		panel_26.setLayout(null);
		panel_26.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Add Events", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_26.setBounds(5, 5, 180, 200);
		panel_25.add(panel_26);
		
		btn_add_destroy_content = new JButton("Destroy Content");
		btn_add_destroy_content.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				events.add(new DestroyInstitutionsContent((double) sp_add_content.getValue()));
				update_event_set();
			}
		});
		btn_add_destroy_content.setBounds(10, 20, 120, 23);
		panel_26.add(btn_add_destroy_content);
		
		btn_add_destroy_structure = new JButton("Destroy Structure");
		btn_add_destroy_structure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				events.add(new DestroyInstitutionsStructure((double) sp_add_structure.getValue()));
				update_event_set();
			}
		});
		btn_add_destroy_structure.setBounds(10, 50, 120, 23);
		panel_26.add(btn_add_destroy_structure);
		
		btn_convert_institutions = new JButton("Convert");
		btn_convert_institutions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				events.add(new ConvertInstitutions((double) sp_add_convert.getValue()));
				update_event_set();
			}
		});
		btn_convert_institutions.setBounds(10, 80, 120, 23);
		panel_26.add(btn_convert_institutions);
		
		btn_convert_traits = new JButton("Convert Traits");
		btn_convert_traits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				events.add(new ConvertTraits((double) sp_add_convert_traits.getValue()));
				update_event_set();
			}
		});
		btn_convert_traits.setBounds(10, 110, 120, 23);
		panel_26.add(btn_convert_traits);
		
		sp_add_convert = new JSpinner();
		sp_add_convert.setModel(new SpinnerNumberModel(new Double(0.1), new Double(0.0), new Double(1.0), new Double(0.1)));
		sp_add_convert.setBounds(135, 81, 39, 20);
		panel_26.add(sp_add_convert);
		
		sp_add_convert_traits = new JSpinner();
		sp_add_convert_traits.setModel(new SpinnerNumberModel(new Double(0.1), new Double(0.0), new Double(1.0), new Double(0.1)));
		sp_add_convert_traits.setBounds(135, 110, 39, 20);
		panel_26.add(sp_add_convert_traits);
		
		sp_add_content = new JSpinner();
		sp_add_content.setModel(new SpinnerNumberModel(new Double(1.0), new Double(0.0), new Double(1.0), new Double(0.1)));
		sp_add_content.setBounds(135, 21, 39, 20);
		panel_26.add(sp_add_content);
		
		sp_add_structure = new JSpinner();
		sp_add_structure.setModel(new SpinnerNumberModel(new Double(1.0), new Double(0.0), new Double(1.0), new Double(0.1)));
		sp_add_structure.setBounds(135, 51, 39, 20);
		panel_26.add(sp_add_structure);
		
		btn_invasion = new JButton("Invasion");
		btn_invasion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				events.add(new Invasion((int) sp_add_invasion.getValue()));
				update_event_set();
			}
		});
		btn_invasion.setBounds(10, 140, 120, 23);
		panel_26.add(btn_invasion);
		
		sp_add_invasion = new JSpinner();
		sp_add_invasion.setModel(new SpinnerNumberModel(new Integer(6), null, null, new Integer(1)));
		sp_add_invasion.setBounds(135, 140, 39, 20);
		panel_26.add(sp_add_invasion);
		
		btn_genocide = new JButton("Genocide");
		btn_genocide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				events.add(new Genocide((double) sp_add_genocide.getValue()));
				update_event_set();
			}
		});
		btn_genocide.setBounds(10, 170, 120, 23);
		panel_26.add(btn_genocide);
		
		sp_add_genocide = new JSpinner();
		sp_add_genocide.setModel(new SpinnerNumberModel(new Double(0.1), new Double(0.0), new Double(1.0), new Double(0.1)));
		sp_add_genocide.setBounds(135, 170, 39, 20);
		panel_26.add(sp_add_genocide);
		
		panel_27 = new JPanel();
		panel_27.setLayout(null);
		panel_27.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Set", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_27.setBounds(5, 211, 180, 135);
		panel_25.add(panel_27);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 20, 160, 75);
		panel_27.add(scrollPane_1);
		
		ta_event_set = new JTextArea();
		ta_event_set.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
		ta_event_set.setEditable(false);
		scrollPane_1.setViewportView(ta_event_set);
		
		JButton btnClear_1 = new JButton("Go");
		btnClear_1.addActionListener(new ActionListener() {
			
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent arg0) {
				controller.add_events((ArrayList<Event>) events.clone());
				
			}
		});
		btnClear_1.setBounds(95, 101, 75, 23);
		panel_27.add(btnClear_1);
		
		button_6 = new JButton("Clear");
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				events.clear();
				update_event_set();
			}
		});
		button_6.setBounds(10, 101, 75, 23);
		panel_27.add(button_6);
		
		panel_28 = new JPanel();
		panel_28.setPreferredSize(new Dimension(10, 160));
		panel_1.add(panel_28, BorderLayout.SOUTH);
		panel_28.setLayout(new BorderLayout(0, 0));
		
		panel_29 = new JPanel();
		panel_28.add(panel_29, BorderLayout.NORTH);
		panel_29.setLayout(new BorderLayout(0, 0));
		
		lbl_similarity = new JLabel("Similarity");
		lbl_similarity.setToolTipText("Blue line (left number) is the energy of the system, and red line (right number) is the dispersion of the foreign trait.");
		lbl_similarity.setHorizontalAlignment(SwingConstants.LEFT);
		panel_29.add(lbl_similarity, BorderLayout.WEST);
		
		l_similarity = new JLabel("");
		l_similarity.setToolTipText("Blue line (left number) is the energy of the system, and red line (right number) is the dispersion of the foreign trait.");
		panel_29.add(l_similarity, BorderLayout.EAST);
		
		graph_similarity = new GraphPanel();
		panel_28.add(graph_similarity, BorderLayout.CENTER);
		
		JToolBar toolBar = new JToolBar();
		panel_7.add(toolBar, BorderLayout.NORTH);
		toolBar.setFloatable(false);
		
		tglbtnPlay = new JToggleButton("Play");
		tglbtnPlay.addActionListener(new PlayAL());
		toolBar.add(tglbtnPlay);
		
		tglbtnPause = new JToggleButton("Pause");
		tglbtnPause.addActionListener(new PauseAL());
		tglbtnPause.setEnabled(false);
		toolBar.add(tglbtnPause);
		
		tglbtnStop = new JToggleButton("Stop");
		tglbtnStop.addActionListener(new StopAL());
		tglbtnStop.setEnabled(false);
		tglbtnStop.setSelected(true);
		toolBar.add(tglbtnStop);
		
		btnClear = new JButton("Clear");
		btnClear.addActionListener(new ClearAL());
		toolBar.add(btnClear);
		
		btnReload = new JButton("Reload");
		btnReload.setEnabled(false);
		btnReload.addActionListener(new ReloadAL());		
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new SaveAL());
		btnSave.setEnabled(false);
		toolBar.add(btnSave);
		toolBar.add(btnReload);
		
		splitPane_1.setDividerLocation(195);		
		splitPane_2.setDividerLocation(700);
		
		panel_20 = new JPanel();
		panel_20.setBackground(Color.WHITE);
		contentPane.add(panel_20, BorderLayout.SOUTH);
		panel_20.setLayout(new GridLayout(2, 1, 0, 0));
		
		
		
		l_start_identification = new JLabel("S:");
		l_start_identification.setFont(new Font("Tahoma", Font.PLAIN, 10));
		panel_20.add(l_start_identification);
		
		l_current_identification = new JLabel("C:");
		l_current_identification.setFont(new Font("Tahoma", Font.PLAIN, 10));
		panel_20.add(l_current_identification);
		
		try {
			controller.load_simulation("./simulation.parameters");
		} catch (IOException e1) {
			controller.initialize_simulation();
			controller.save_simulation("./simulation.parameters");
		}
		
		DefaultCaret caret = (DefaultCaret)output_area.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}
	
	private void update_event_set(){
		int i = 1;
		ta_event_set.setText("");
		for (Iterator<Event> iterator = events.iterator(); iterator.hasNext();) {
			Event event = (Event) iterator.next();
			ta_event_set.append(i + ". " + event + "\n");
			i++;			
		}
	}
	
	public static boolean want_to_continue(Component c){
		return (controller.is_saved ||
				controller.get_iteration() == 0 
				|| controller.get_iteration() > 0 && 
				JOptionPane.showConfirmDialog(c, 
						"There is a non-saved simulation in the iteration " + controller.get_iteration() + 
						".\nAre you sure you want to continue and overwrite the current simulation?", 
						"Do you want to continue?", JOptionPane.YES_NO_OPTION, 
						JOptionPane.WARNING_MESSAGE)== JOptionPane.YES_OPTION);
	}
	
	public static BufferedImage resize(BufferedImage image, int width, int height) {
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
	    Graphics2D g2d = (Graphics2D) bi.createGraphics();
	    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
	    g2d.drawImage(image, 0, 0, width, height, null);
	    g2d.dispose();
	    return bi;
	}
	
	public static void clean_belief_spaces(){
		belief_space.setIcon(null);
		institutional_beliefs_association.setIcon(null);
		alife_institutional_beliefs_space.setIcon(null);
		alife_institutions.setIcon(null);
		graph_cultures.clean();
		graph_institutions.clean();
		graph_borderless_cultures.clean();
		graph_energy_foreign_trait.clean();
		graph_similarity.clean();
	}
	
	public static void set_belief_space(BufferedImage image){
		belief_space.setIcon(new ImageIcon(resize(image,belief_space.getWidth(),belief_space.getHeight())));
	}
	
	public static void set_institutional_beliefs_association(BufferedImage image){
		institutional_beliefs_association.setIcon(new ImageIcon(resize(image,institutional_beliefs_association.getWidth(),institutional_beliefs_association.getHeight())));
	}
	
	public static void set_alife_institutions(BufferedImage image){
		alife_institutions.setIcon(new ImageIcon(resize(image,alife_institutions.getWidth(),alife_institutions.getHeight())));
	}
	
	public static void set_alife_institutional_beliefs_space(BufferedImage image){
		
		alife_institutional_beliefs_space.setIcon(new ImageIcon(resize(image,alife_institutional_beliefs_space.getWidth(),alife_institutional_beliefs_space.getHeight())));
	}
	
	private class SwingAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public SwingAction() {
			putValue(NAME, "Parameters");
			putValue(SHORT_DESCRIPTION, "Show parameters configuration  of the simulation");
		}
		public void actionPerformed(ActionEvent e) {
			controller.restore_parameters_to_interface();
			parameters_dialog.setVisible(true);
		}
	}
	
	private class PlayAL extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {
			
			if (tglbtnPlay.isEnabled() && mntmPlay.isEnabled()){
				
				if (tglbtnPause.isSelected() || tglbtnStop.isSelected()){
				
					if (!tglbtnPause.isSelected() && tglbtnStop.isSelected()){
							
						File dir = new File(parameters_dialog.tf_results_dir.getText() + Controller.RESULTS_DIR);
						
						String result_dir = Controller.RESULTS_DIR.substring(0, Controller.RESULTS_DIR.length() - 1);
						for( int i = 0; dir.exists(); i++) {
							dir = new File(parameters_dialog.tf_results_dir.getText() + result_dir + i + "/");	
						}
			
						RESULTS_DIR = dir.getAbsolutePath() + "/";
						(new File(RESULTS_DIR + Controller.ITERATIONS_DIR)).mkdirs();
						controller.setRESULTS_DIR(RESULTS_DIR);

						controller.play();
					} else {
						controller.resume();
					}
					
					tglbtnPlay.setEnabled(false);
					mntmPlay.setEnabled(false);
					tglbtnPause.setEnabled(true);
					tglbtnPause.setSelected(false);
					mntmPause.setEnabled(true);
					tglbtnStop.setEnabled(true);
					tglbtnStop.setSelected(false);
					mntmStop.setEnabled(true);
					btnClear.setEnabled(false);
					mntmClear.setEnabled(false);
					btnReload.setEnabled(false);
					mntmReload.setEnabled(false);
					btnSave.setEnabled(false);
					mntmSave.setEnabled(false);
					
					mntmSafeWorldState.setEnabled(false);
					mntmLoadWorldState.setEnabled(false);
					mnSimulation.setEnabled(false);
					

//					btnCollapse.setEnabled(false);
//					btnInvasion.setEnabled(false);
//					btnGenocide.setEnabled(false);
					
					
				} else {
					output_area.append("Warning: GUI in an unstable state");
				}
			} 

		}
	}
	
	private class StopAL extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {
			
			if (tglbtnStop.isEnabled() && mntmStop.isEnabled()){
						
				controller.cancel();
					
				
				tglbtnPlay.setEnabled(true);
				tglbtnPlay.setSelected(false);
				mntmPlay.setEnabled(true);
				tglbtnPause.setEnabled(false);
				mntmPause.setEnabled(false);
				tglbtnStop.setEnabled(false);
				mntmStop.setEnabled(false);
				btnClear.setEnabled(true);
				mntmClear.setEnabled(true);
				btnReload.setEnabled(true);
				mntmReload.setEnabled(true);
				btnSave.setEnabled(true);
				mntmSave.setEnabled(true);
			
				
				mntmSafeWorldState.setEnabled(true);
				mntmLoadWorldState.setEnabled(true);
				mnSimulation.setEnabled(true);
				
				
//				btnCollapse.setEnabled(true);
//				btnInvasion.setEnabled(true);
//				btnGenocide.setEnabled(true);
				
			} 

		}
	}

	private class PauseAL extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {
			
			if (tglbtnPause.isEnabled() && mntmPause.isEnabled()){
			
				controller.suspend();
				
				tglbtnPlay.setEnabled(true);
				tglbtnPlay.setSelected(false);
				mntmPlay.setEnabled(true);
				
				tglbtnStop.setEnabled(false);
				mntmStop.setEnabled(false);
				
				tglbtnPause.setEnabled(false);
				mntmPause.setEnabled(false);
				
			} 
		}
	}
	
	private class ClearAL extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {
			
			controller.restart_simulation();
			
			btnReload.setEnabled(false);
			mntmReload.setEnabled(false);
			btnSave.setEnabled(false);
			mntmSave.setEnabled(false);
		}
	}
	
	
	private class ReloadAL extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {
			
			controller.reload_state();
		}
	}
	
	private class SaveAL extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {
			
			controller.save_state();
		}
	}
}

