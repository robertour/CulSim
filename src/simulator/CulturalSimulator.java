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
import simulator.control.events.ConvertInstitutions;
import simulator.control.events.ConvertTraits;
import simulator.control.events.DestroyInstitutionsContent;
import simulator.control.events.DestroyInstitutionsStructure;
import simulator.control.events.DestroyPartialInstitutionsContent;
import simulator.control.events.Distribution;
import simulator.control.events.Event;
import simulator.control.events.Genocide;
import simulator.control.events.Invasion;

import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
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
import java.awt.Insets;

import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.Font;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import java.awt.SystemColor;
import java.awt.FlowLayout;
import javax.swing.ScrollPaneConstants;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class CulturalSimulator extends JFrame {


	private static final long serialVersionUID = -6498782807057797553L;
	private JPanel contentPane;
	
	public static String EXPERIMENTAL_FILE = "";
	public String results_dir = "";
	
	private JFileChooser jfc_load = new JFileChooser(Controller.WORKSPACE_DIR + Controller.WORLDS_DIR);
	
	public static ControllerSingle controller;
	private ArrayList<Event> events = new ArrayList<Event>();
	
	private OutputArea output_area;
	private final Action action = new SwingAction();

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
	private JButton btnClear;
	private JMenu mnSimulation;
	private JMenuItem mntmClear;
	private JSplitPane splitPane_1;
	private JPanel sidePanel;
	private JPanel panel_1;
	private JPanel tabEvents;
	private JSplitPane splitPane_2;
	private JPanel panelGraphs;
	private JPanel cultures_panel;
	private JPanel institution_panel;
	private JPanel newmann_panel;
	private JPanel energy_panel;
	private JLabel lblInstitutions;
	private JLabel lblEnergy;
	private JPanel panel_19;
	private JPanel statusBarPanel;
	private JPanel tabParameters;
	private JPanel panel_23;
	private JLabel label_2;
	private JLabel label_3;
	private JPanel panel_24;
	private JLabel label_4;
	private JLabel label_5;
	private JLabel label_6;
	private JLabel label_7;
	private JTabbedPane tabbedPane;
	private JPanel tabEventSet;
	private JPanel panel_27;
	private JButton button_6;
	private JTextArea ta_event_set;
	private JPanel newmann_similarity_panel;
	private JPanel panel_29;
	private JLabel lbl_newman_similarity;
	private JButton btnReload;
	private JMenuItem mntmReload;
	private JButton btnSave;
	private JMenuItem mntmSave;
	private EventPanel instDestructionPanel;
	private EventPanel instConversionPanel;
	private EventPanel invasionPanel;
	private EventPanel genocidePanel;
	private JPanel panel_10;
	private JPanel panel_26;
	private JPanel progressPanel;
	private JPanel panel_7;
	private JLabel lblSpeed;
	private JPanel panel_8;
	private JLabel lblIteration;
	private JLabel lblSpeed_1;
	private JPanel panel_11;
	private JPanel pixel_panel;
	private JPanel culture_similarity_panel;
	private JPanel panel_14;
	private JLabel lblPixel;
	private JPanel panel_12;
	private JLabel lbl_culture_similarity;

	protected TripleDistributionDialog destructionDialog;
	protected DoubleDistributionDialog conversionDialog;
	protected SingleDistributionDialog invasionDialog;
	protected SingleDistributionDialog genocideDialog;


	public static CulturalParameters parameters_dialog;
	public static BatchMode batch_mode_dialog;

	private static JLabel belief_space = new JLabel("");
	private static JLabel alife_institutional_beliefs_space = new JLabel("");
	private static JLabel alife_institutions = new JLabel("");
	private static JLabel institutional_beliefs_association = new JLabel("");

	public static JLabel l_start_identification;
	public static JLabel l_current_identification;
	
	public static GraphPanel graph_cultures;
	public static GraphPanel graph_institutions;
	public static GraphPanel graph_newmann_cultures;
	public static GraphPanel graph_energy;
	public static GraphPanel graph_newmann_similarity;
	public static GraphPanel graph_culture_similarity;
	public static GraphPanel graph_pixels;
	
	public static JLabel l_energy_foreigners;
	public static JLabel l_cultures;
	public static JLabel l_newmann;
	public static JLabel l_institutions;
	public static JLabel l_newmann_similarity;
	public static JLabel l_pixels;
	public static JLabel l_culture_similarity;

	public static JSpinner sp_selection_error;
	public static JSpinner sp_influence;
	public static JSpinner sp_loyalty;
	public static JSpinner sp_democracy;
	public static JSpinner sp_propaganda;
	public static JSpinner sp_iterations;
	public static JSpinner sp_checkpoints;
	public static JSpinner sp_mutation;
	
	private static JLabel lblSpeedValue;
	private static JSlider sliderSpeed;
	private static int speed;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
//				double [][] cov = new double[2][2];
//				cov[0][0] = 1000000; //=1000
//				cov[0][1] = 500000;
//				cov[1][0] = 500000;
//				cov[1][1] = 1000000; //=1000
//				MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(new double[] {5000,5000}, cov);
//				double [][]sample = mnd.sample(10);
//								
//				
//				for (int i = 0; i < sample.length; i++) {
//					for (int j = 0; j < sample[0].length; j++) {
//						System.out.print(sample[i][j] + " ");
//					}
//					System.out.println();					
//				}
				
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

				    UIManager.getDefaults().put("TabbedPane.contentBorderInsets", new Insets(1,0,0,0));
				    UIManager.getDefaults().put("TabbedPane.tabAreaInsets", new Insets(0,0,0,0));
				    UIManager.getDefaults().put("TabbedPane.tabsOverlapBorder", true);
				    UIManager.getDefaults().put("TabbedPane.lightHighlight", Color.BLACK);
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
		setBounds(0, 0, 1024, 722);
		
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
		
		panelGraphs = new JPanel();
		splitPane_2.setRightComponent(panelGraphs);
		panelGraphs.setLayout(new GridLayout(4, 2, 0, 0));
		
		panel_11 = new JPanel();
		panelGraphs.add(panel_11);
		
		energy_panel = new JPanel();
		panelGraphs.add(energy_panel);
		energy_panel.setLayout(new BorderLayout(0, 0));
		
		panel_19 = new JPanel();
		energy_panel.add(panel_19, BorderLayout.NORTH);
		panel_19.setLayout(new BorderLayout(0, 0));
		
		lblEnergy = new JLabel("Energy:");
		lblEnergy.setToolTipText("Blue line (left number) is the energy of the system, and red line (right number) is the dispersion of the foreign trait.");
		panel_19.add(lblEnergy, BorderLayout.WEST);
		lblEnergy.setHorizontalAlignment(SwingConstants.LEFT);
		
		l_energy_foreigners = new JLabel("");
		l_energy_foreigners.setForeground(SystemColor.textHighlight);
		l_energy_foreigners.setToolTipText("Blue line (left number) is the energy of the system, and red line (right number) is the dispersion of the foreign trait.");
		panel_19.add(l_energy_foreigners, BorderLayout.EAST);
		
		graph_energy = new GraphPanel();
		energy_panel.add(graph_energy, BorderLayout.CENTER);
		
		cultures_panel = new JPanel();
		panelGraphs.add(cultures_panel);
		cultures_panel.setLayout(new BorderLayout(0, 0));
		
		graph_cultures = new GraphPanel();
		cultures_panel.add(graph_cultures, BorderLayout.CENTER);
		
		JPanel panel_16 = new JPanel();
		cultures_panel.add(panel_16, BorderLayout.NORTH);
		panel_16.setLayout(new BorderLayout(0, 0));
		
		JLabel lblCultures_1 = new JLabel("Cultures:");
		lblCultures_1.setToolTipText("Blue line (left number) is the number of cultures, and red line (right number) the size of the biggest culture. The cultures are defined through the scope of immediate neighbors.");
		panel_16.add(lblCultures_1, BorderLayout.WEST);
		
		l_cultures = new JLabel("");
		l_cultures.setBorder(null);
		l_cultures.setForeground(SystemColor.textHighlight);
		l_cultures.setToolTipText("Blue line (left number) is the number of cultures, and red line (right number) the size of the biggest culture. The cultures are defined through the scope of immediate neighbors.");
		panel_16.add(l_cultures, BorderLayout.EAST);
		
		newmann_panel = new JPanel();
		panelGraphs.add(newmann_panel);
		newmann_panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_18 = new JPanel();
		newmann_panel.add(panel_18, BorderLayout.NORTH);
		panel_18.setLayout(new BorderLayout(0, 0));
		
		lblInstitutions = new JLabel("Newmann's:");
		lblInstitutions.setToolTipText("Blue line (left number) is the number of cultures, and red line (right number) the size of the biggest culture. The cultures are defined through the scope of the neighborhood radius.");
		panel_18.add(lblInstitutions, BorderLayout.WEST);
		lblInstitutions.setHorizontalAlignment(SwingConstants.LEFT);
		
		l_newmann = new JLabel("");
		l_newmann.setForeground(SystemColor.textHighlight);
		l_newmann.setToolTipText("Blue line (left number) is the number of cultures, and red line (right number) the size of the biggest culture. The cultures are defined through the scope of the neighborhood radius.");
		panel_18.add(l_newmann, BorderLayout.EAST);
		
		graph_newmann_cultures = new GraphPanel();
		newmann_panel.add(graph_newmann_cultures, BorderLayout.CENTER);
		
		culture_similarity_panel = new JPanel();
		panelGraphs.add(culture_similarity_panel);
		culture_similarity_panel.setLayout(new BorderLayout(0, 0));
		
		graph_culture_similarity = new GraphPanel();
		culture_similarity_panel.add(graph_culture_similarity, BorderLayout.CENTER);
		
		panel_12 = new JPanel();
		culture_similarity_panel.add(panel_12, BorderLayout.NORTH);
		panel_12.setLayout(new BorderLayout(0, 0));
		
		lbl_culture_similarity = new JLabel("Culture's Sim:");
		lbl_culture_similarity.setToolTipText("Blue line (left number) is the energy of the system, and red line (right number) is the dispersion of the foreign trait.");
		lbl_culture_similarity.setHorizontalAlignment(SwingConstants.LEFT);
		panel_12.add(lbl_culture_similarity, BorderLayout.WEST);
		
		l_culture_similarity = new JLabel("");
		l_culture_similarity.setToolTipText("Blue line (left number) is the energy of the system, and red line (right number) is the dispersion of the foreign trait.");
		l_culture_similarity.setForeground(SystemColor.textHighlight);
		panel_12.add(l_culture_similarity, BorderLayout.EAST);
		
		newmann_similarity_panel = new JPanel();
		panelGraphs.add(newmann_similarity_panel);
		newmann_similarity_panel.setPreferredSize(new Dimension(10, 160));
		newmann_similarity_panel.setLayout(new BorderLayout(0, 0));
		
		panel_29 = new JPanel();
		newmann_similarity_panel.add(panel_29, BorderLayout.NORTH);
		panel_29.setLayout(new BorderLayout(0, 0));
		
		lbl_newman_similarity = new JLabel("Newmann's Sim:");
		lbl_newman_similarity.setToolTipText("Blue line (left number) is the energy of the system, and red line (right number) is the dispersion of the foreign trait.");
		lbl_newman_similarity.setHorizontalAlignment(SwingConstants.LEFT);
		panel_29.add(lbl_newman_similarity, BorderLayout.WEST);
		
		l_newmann_similarity = new JLabel("");
		l_newmann_similarity.setForeground(SystemColor.textHighlight);
		l_newmann_similarity.setToolTipText("Blue line (left number) is the energy of the system, and red line (right number) is the dispersion of the foreign trait.");
		panel_29.add(l_newmann_similarity, BorderLayout.EAST);
		
		graph_newmann_similarity = new GraphPanel();
		newmann_similarity_panel.add(graph_newmann_similarity, BorderLayout.CENTER);
		
		institution_panel = new JPanel();
		panelGraphs.add(institution_panel);
		institution_panel.setLayout(new BorderLayout(0, 0));
		
		graph_institutions = new GraphPanel();
		institution_panel.add(graph_institutions, BorderLayout.CENTER);
		
		JPanel panel_17 = new JPanel();
		institution_panel.add(panel_17, BorderLayout.NORTH);
		panel_17.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel_1 = new JLabel("Institutions:");
		lblNewLabel_1.setToolTipText("Blue line (left number) is the number of instititutions, and red line (right number) the size of the biggest institution");
		panel_17.add(lblNewLabel_1, BorderLayout.WEST);
		
		l_institutions = new JLabel("");
		l_institutions.setForeground(SystemColor.textHighlight);
		l_institutions.setToolTipText("Blue line (left number) is the number of instititutions, and red line (right number) the size of the biggest institution");
		panel_17.add(l_institutions, BorderLayout.EAST);
		
		pixel_panel = new JPanel();
		panelGraphs.add(pixel_panel);
		pixel_panel.setLayout(new BorderLayout(0, 0));
		
		panel_14 = new JPanel();
		pixel_panel.add(panel_14, BorderLayout.NORTH);
		panel_14.setLayout(new BorderLayout(0, 0));
		
		lblPixel = new JLabel("Pixels:");
		panel_14.add(lblPixel, BorderLayout.WEST);
		lblPixel.setToolTipText("Blue line (left number) is the number of instititutions, and red line (right number) the size of the biggest institution");
		
		l_pixels = new JLabel("");
		l_pixels.setToolTipText("Blue line (left number) is the number of instititutions, and red line (right number) the size of the biggest institution");
		l_pixels.setForeground(SystemColor.textHighlight);
		panel_14.add(l_pixels, BorderLayout.EAST);
		
		graph_pixels = new GraphPanel();
		pixel_panel.add(graph_pixels, BorderLayout.CENTER);
		
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
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setMinimumSize(new Dimension(23, 200));
		panel_2.add(scrollPane);
		
		output_area = new OutputArea();
		output_area.setLineWrap(true);
		output_area.setFont(new Font("Tahoma", Font.PLAIN, 10));
		output_area.setEditable(false);
		scrollPane.setViewportView(output_area);
		splitPane.setDividerLocation(500);
		
		controller = new ControllerSingle(output_area);
		parameters_dialog = new CulturalParameters(this);
		
		sidePanel = new JPanel();
		splitPane_1.setLeftComponent(sidePanel);
		sidePanel.setLayout(new BorderLayout(0, 0));
		
		panel_1 = new JPanel();
		panel_1.setBorder(new EmptyBorder(0, 0, 0, 0));
		sidePanel.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 10));
		
		progressPanel = new JPanel();
		panel_1.add(progressPanel, BorderLayout.NORTH);
		progressPanel.setLayout(new BorderLayout(0, 0));
		
		panel_7 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_7.getLayout();
		flowLayout_1.setHgap(0);
		progressPanel.add(panel_7, BorderLayout.WEST);
		
		lblSpeed = new JLabel("Speed: ");
		panel_7.add(lblSpeed);

		lblSpeedValue = new JLabel("1000");
		panel_7.add(lblSpeedValue);
		lblSpeedValue.setPreferredSize(new Dimension(24, 20));
		lblSpeedValue.setHorizontalTextPosition(SwingConstants.LEFT);
		lblSpeedValue.setHorizontalAlignment(SwingConstants.LEFT);
		lblSpeedValue.setForeground(SystemColor.textHighlight);
		
		
		sliderSpeed = new JSlider();
		sliderSpeed.setMinimum(1);
		sliderSpeed.setMaximum(30);
		progressPanel.add(sliderSpeed);
		
		set_speed((int) CulturalParameters.sp_checkpoints.getValue());
		
		sliderSpeed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (sliderSpeed.getValue() < 10){
					speed = sliderSpeed.getValue();
				}
				else if (sliderSpeed.getValue() < 20){
					speed = 10*(sliderSpeed.getValue()-9);
				}
				else if (sliderSpeed.getValue() < 30){
					speed = 100*(sliderSpeed.getValue()-19);
				}
				lblSpeedValue.setText(speed + "");
				CulturalSimulator.sp_checkpoints.setValue(speed);
				controller.setParameters();
			}
		});
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(null);
		tabbedPane.setPreferredSize(new Dimension(5, 375));
		panel_1.add(tabbedPane, BorderLayout.CENTER);
		
		tabEvents = new JPanel();
		tabbedPane.addTab("Event", null, tabEvents, null);
		tabEvents.setPreferredSize(new Dimension(10, 270));
		tabEvents.setBorder(new EmptyBorder(5, 0, 0, 0));
		
		
		tabEvents.setLayout(new BorderLayout(0, 0));
		
		panel_10 = new JPanel();
		panel_10.setPreferredSize(new Dimension(10, 330));
		tabEvents.add(panel_10, BorderLayout.NORTH);
		panel_10.setLayout(new GridLayout(4, 0, 0, 0));
		
		destructionDialog = new TripleDistributionDialog(null, new Distribution(1.0),null,
				"Structure", "Partial Content", "Full Content", this);
		instDestructionPanel = new EventPanel("Institutions destruction", destructionDialog);
		instDestructionPanel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Event> events = new ArrayList<Event>();
				if (destructionDialog.get_distribution1() != null){
					events.add(new DestroyInstitutionsStructure(destructionDialog.get_distribution1()));
				}
				if (destructionDialog.get_distribution2() != null){
					events.add(new DestroyInstitutionsContent(destructionDialog.get_distribution2()));
				}
				if (destructionDialog.get_distribution3() != null){
					events.add(new DestroyPartialInstitutionsContent(destructionDialog.get_distribution3()));
				}
				controller.add_events(events);
			}
		});
		destructionDialog.addNotifiable(instDestructionPanel);		
		panel_10.add(instDestructionPanel);
		
		conversionDialog = new DoubleDistributionDialog(null, new Distribution(-1.0,-1.0,0.2), "Full", "Partial", this);
		instConversionPanel = new EventPanel("Institutional conversion", conversionDialog);
		instConversionPanel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Event> events = new ArrayList<Event>();
				if (conversionDialog.get_distribution1() != null) {
					events.add(new ConvertInstitutions(conversionDialog.get_distribution1()));
				}
				if (conversionDialog.get_distribution2() != null) {
					events.add(new ConvertTraits(conversionDialog.get_distribution2()));
				}
				controller.add_events(events);
			}
		});
		conversionDialog.addNotifiable(instConversionPanel);
		panel_10.add(instConversionPanel);
		
		invasionDialog = new SingleDistributionDialog(new Distribution(-1.0,-1.0,0.2), "Invasion", this);
		invasionPanel = new EventPanel("Invasion", invasionDialog);
		invasionPanel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Event> events = new ArrayList<Event>();
				if (invasionDialog.get_distribution() != null){
					events.add(new Invasion(invasionDialog.get_distribution()));
					controller.add_events(events);
				}
			}
		});
		invasionDialog.addNotifiable(invasionPanel);
		panel_10.add(invasionPanel);
		
		genocideDialog = new SingleDistributionDialog(new Distribution(-1.0,-1.0,0.2), "Genocide", this);
		genocidePanel = new EventPanel("Genocide", genocideDialog);
		genocidePanel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Event> events = new ArrayList<Event>();
				if (genocideDialog.get_distribution() != null){
					events.add(new Genocide(genocideDialog.get_distribution()));
					controller.add_events(events);
				}
			}
		});
		genocideDialog.addNotifiable(genocidePanel);
		panel_10.add(genocidePanel);
		
		tabEventSet = new JPanel();
		tabEventSet.setPreferredSize(new Dimension(10, 270));
		tabEventSet.setBorder(null);
		tabbedPane.addTab("Event Set", null, tabEventSet, null);
		tabEventSet.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_9 = new JPanel();
		panel_9.setBorder(new EmptyBorder(5, 0, 0, 0));
		panel_9.setPreferredSize(new Dimension(10, 340));
		tabEventSet.add(panel_9, BorderLayout.NORTH);
		panel_9.setLayout(new GridLayout(4, 0, 0, 0));
		
		EventPanel instituionPanelSet = new EventPanel("Institutions destruction", destructionDialog);
		instituionPanelSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (destructionDialog.get_distribution1() != null){
					events.add(new DestroyInstitutionsStructure(destructionDialog.get_distribution1()));
				}
				if (destructionDialog.get_distribution2() != null){
					events.add(new DestroyInstitutionsContent(destructionDialog.get_distribution2()));
				}
				update_event_set();
			}
		});
		destructionDialog.addNotifiable(instituionPanelSet);
		panel_9.add(instituionPanelSet);
		
		
		EventPanel conversionPanelSet = new EventPanel("Institutional conversion", conversionDialog);
		conversionPanelSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (conversionDialog.get_distribution1() != null){
					events.add(new ConvertInstitutions(conversionDialog.get_distribution1()));
				}
				if (conversionDialog.get_distribution2() != null) {
					events.add(new ConvertTraits(conversionDialog.get_distribution2()));
				}
				update_event_set();
			}
		});
		conversionDialog.addNotifiable(conversionPanelSet);
		panel_9.add(conversionPanelSet);
		
		EventPanel invasionPanelSet = new EventPanel("Invasion", invasionDialog);
		invasionPanelSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (invasionDialog.get_distribution() != null){
					events.add(new Invasion(invasionDialog.get_distribution()));
					update_event_set();
				}
			}
		});
		invasionDialog.addNotifiable(invasionPanelSet);
		panel_9.add(invasionPanelSet);
		
		EventPanel genocidePanelSet = new EventPanel("Genocide", genocideDialog);
		genocidePanelSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (genocideDialog.get_distribution() != null){
					events.add(new Genocide(genocideDialog.get_distribution()));
					update_event_set();
				}
			}
		});
		genocideDialog.addNotifiable(genocidePanelSet);
		panel_9.add(genocidePanelSet);
		
		panel_27 = new JPanel();
		panel_27.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Set", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		tabEventSet.add(panel_27, BorderLayout.CENTER);
		panel_27.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel_27.add(scrollPane_1, BorderLayout.NORTH);
		
		ta_event_set = new JTextArea();
		ta_event_set.setRows(5);
		ta_event_set.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
		ta_event_set.setEditable(false);
		scrollPane_1.setViewportView(ta_event_set);
		
		panel_26 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_26.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		panel_27.add(panel_26, BorderLayout.CENTER);
		
		button_6 = new JButton("Clear");
		panel_26.add(button_6);
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				events.clear();
				update_event_set();
			}
		});
		
		JButton btnClear_1 = new JButton("Go");
		panel_26.add(btnClear_1);
		btnClear_1.addActionListener(new ActionListener() {
			
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent arg0) {
				controller.add_events((ArrayList<Event>) events.clone());
				
			}
		});
		
		tabParameters = new JPanel();
		tabbedPane.addTab("Parameters", null, tabParameters, null);
		tabParameters.setBorder(null);
		tabParameters.setLayout(null);
		
		panel_8 = new JPanel();
		panel_8.setLayout(null);
		panel_8.setBorder(new TitledBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Noise", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), "Controls", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_8.setBounds(5, 5, 180, 78);
		tabParameters.add(panel_8);
		
		lblIteration = new JLabel("Iterations:");
		lblIteration.setToolTipText("How many iterations would you let the simulation run for? ");
		lblIteration.setBounds(10, 23, 74, 14);
		panel_8.add(lblIteration);
		
		lblSpeed_1 = new JLabel("Speed:");
		lblSpeed_1.setToolTipText("How often do you want to save results, update graphs, check for Events or Pause/Stop/Resume states? The highes the value, the faster it executes, but it doesn't update the interface or store results as fast.");
		lblSpeed_1.setBounds(10, 48, 74, 14);
		panel_8.add(lblSpeed_1);
		
		sp_iterations = new JSpinner();
		sp_iterations.setModel(new SpinnerNumberModel(new Integer(1000000), new Integer(1000), null, new Integer(1000)));
		sp_iterations.setToolTipText("How many iterations would you let the simulation run for? ");
		sp_iterations.setBounds(90, 20, 80, 20);
		panel_8.add(sp_iterations);
		
		sp_checkpoints = new JSpinner();
		sp_checkpoints.setModel(new SpinnerNumberModel(new Integer(100), new Integer(0), null, new Integer(10)));
		sp_checkpoints.setToolTipText("How often do you want to save results, update graphs, check for Events or Pause/Stop/Resume states? The highes the value, the faster it executes, but it doesn't update the interface or store results as fast.");
		sp_checkpoints.setBounds(90, 45, 80, 20);
		panel_8.add(sp_checkpoints);
		
		panel_23 = new JPanel();
		panel_23.setLayout(null);
		panel_23.setBorder(new TitledBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Noise", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_23.setBounds(5, 94, 180, 78);
		tabParameters.add(panel_23);
		
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
		panel_24.setBounds(5, 183, 180, 124);
		tabParameters.add(panel_24);
		
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
				set_speed((int) sp_checkpoints.getValue());
			}
		});
		btnSetParameters.setBounds(5, 319, 180, 23);
		tabParameters.add(btnSetParameters);
		
		JToolBar toolBar = new JToolBar();
		sidePanel.add(toolBar, BorderLayout.NORTH);
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
		
		statusBarPanel = new JPanel();
		statusBarPanel.setBackground(SystemColor.control);
		contentPane.add(statusBarPanel, BorderLayout.SOUTH);
		statusBarPanel.setLayout(new GridLayout(2, 1, 0, 0));
		
			
		l_start_identification = new JLabel("S:");
		l_start_identification.setForeground(SystemColor.textHighlight);
		l_start_identification.setFont(new Font("Tahoma", Font.PLAIN, 10));
		statusBarPanel.add(l_start_identification);
		
		l_current_identification = new JLabel("C:");
		l_current_identification.setForeground(SystemColor.textHighlight);
		l_current_identification.setFont(new Font("Tahoma", Font.PLAIN, 10));
		statusBarPanel.add(l_current_identification);
		
		
		batch_mode_dialog = new BatchMode(this);
		
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
		graph_newmann_cultures.clean();
		graph_energy.clean();
		graph_newmann_similarity.clean();
		graph_culture_similarity.clean();
		graph_pixels.clean();
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
						controller.run(parameters_dialog.tf_results_dir.getText());
						
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
	
	public static void set_speed(int s){
		speed = s;
		lblSpeedValue.setText(speed + "");
		if (speed < 10){
			sliderSpeed.setValue(speed);	
		} else if (speed < 100){
			sliderSpeed.setValue(9 + (int) Math.round(speed/10.0));
		} else if (speed < 1000){
			sliderSpeed.setValue(19 + (int) Math.round(speed/100.0));
		} else {
			sliderSpeed.setValue(30);
		}
	}
}

