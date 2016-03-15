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
import simulator.control.events.Apostasy;
import simulator.control.events.ConvertInstitutions;
import simulator.control.events.ConvertTraits;
import simulator.control.events.RemoveInstitutionsContent;
import simulator.control.events.DestroyInstitutions;
import simulator.control.events.RemoveInstitutionsPartialContent;
import simulator.control.events.Distribution;
import simulator.control.events.Event;
import simulator.control.events.Genocide;
import simulator.control.events.Invasion;
import simulator.control.events.ParameterChange;

import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import java.awt.Font;
import javax.swing.JTextArea;
import java.awt.SystemColor;
import java.awt.FlowLayout;
import javax.swing.ScrollPaneConstants;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class CulturalSimulator extends JFrame implements Notifiable {


	private static final long serialVersionUID = -6498782807057797553L;
	private JPanel contentPane;
	
	public static String EXPERIMENTAL_FILE = "";
	public String results_dir = "";
	
	private JFileChooser jfc_load = new JFileChooser();
	private JFileChooser jfc_events = new JFileChooser();
	
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
	private JPanel panel_26;
	private JPanel progressPanel;
	private JPanel panel_7;
	private JLabel lblSpeed;
	private JPanel panel_11;
	private JPanel pixel_panel;
	private JPanel culture_similarity_panel;
	private JPanel panel_14;
	private JLabel lblPixel;
	private JPanel panel_12;
	private JLabel lbl_culture_similarity;

	protected DoubleDistributionDialog structureDialog;
	protected DoubleDistributionDialog contentDialog;
	protected DoubleDistributionDialog conversionDialog;
	protected SingleDistributionDialog invasionDialog;
	protected SingleDistributionDialog genocideDialog;
	protected ParametersDialog parametersDialog;


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
	
	private static JLabel lblSpeedValue;
	private static JSlider sliderSpeed;
	private static int speed;
	private EventPanel parameterEventPanel;
	private JPanel panel_8;
	private JButton button;
	private JButton button_1;

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
		
		File world_dir = new File( Controller.WORKSPACE_DIR + Controller.WORLDS_DIR);;
		if (!world_dir.exists()){
			world_dir.mkdirs();
        }
		jfc_load.setCurrentDirectory(world_dir);
		mntmSafeWorldState = new JMenuItem("Safe World State");
		mntmSafeWorldState.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/document-save.png")));
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
		mntmLoadWorldState.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/document-open.png")));
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
		mntmExit.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/system-shutdown.png")));
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
		mntmPlay.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/media-playback-start.png")));
		mntmPlay.addActionListener(new PlayAL());
		mnControls.add(mntmPlay);
		
		mntmPause = new JMenuItem("Pause");
		mntmPause.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/media-playback-pause.png")));
		mntmPause.addActionListener(new PauseAL());
		mnControls.add(mntmPause);
		
		mntmStop = new JMenuItem("Stop");
		mntmStop.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/media-playback-stop.png")));
		mntmStop.addActionListener(new StopAL());
		mnControls.add(mntmStop);
		
		mntmClear = new JMenuItem("Clear");
		mntmClear.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/media-skip-backward.png")));
		mntmClear.addActionListener(new ClearAL());
		mnControls.add(mntmClear);
		
		mntmReload = new JMenuItem("Reload");
		mntmReload.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/edit-undo.png")));
		mntmReload.addActionListener(new ReloadAL());
		
		mntmSave = new JMenuItem("Save");
		mntmSave.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/flag-yellow.png")));
		mntmSave.addActionListener(new SaveAL());
		mnControls.add(mntmSave);
		mnControls.add(mntmReload);
		
		mnSimulation = new JMenu("Simulation");
		menuBar.add(mnSimulation);
		
		JMenuItem mntmDefineParameters = new JMenuItem("Parameters");
		
		mntmDefineParameters.setAction(action);
		mntmDefineParameters.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/run-build-configure.png")));
		mnSimulation.add(mntmDefineParameters);
		mnSimulation.addSeparator();
		
		mntmBatchMode = new JMenuItem("Batch Mode");
		mntmBatchMode.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/view-calendar-tasks.png")));
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
		
		controller = new ControllerSingle(output_area, this);
		parameters_dialog = new CulturalParameters(this);
		
		sidePanel = new JPanel();
		splitPane_1.setLeftComponent(sidePanel);
		sidePanel.setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar = new JToolBar();
		sidePanel.add(toolBar, BorderLayout.NORTH);
		toolBar.setFloatable(false);
		
		tglbtnPlay = new JToggleButton("");
		tglbtnPlay.setToolTipText("Play the simulation");
		tglbtnPlay.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/media-playback-start.png")));
		tglbtnPlay.addActionListener(new PlayAL());
		toolBar.add(tglbtnPlay);
		
		tglbtnPause = new JToggleButton("");
		tglbtnPause.setToolTipText("Pause the simulation");
		tglbtnPause.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/media-playback-pause.png")));
		tglbtnPause.addActionListener(new PauseAL());
		tglbtnPause.setEnabled(false);
		toolBar.add(tglbtnPause);
		
		tglbtnStop = new JToggleButton("");
		tglbtnStop.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/media-playback-stop.png")));
		tglbtnStop.setToolTipText("Stop the simulation");
		tglbtnStop.addActionListener(new StopAL());
		tglbtnStop.setEnabled(false);
		tglbtnStop.setSelected(true);
		toolBar.add(tglbtnStop);
		
		btnClear = new JButton("");
		btnClear.setToolTipText("Restart the simulation");
		btnClear.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/media-skip-backward.png")));
		btnClear.addActionListener(new ClearAL());
		toolBar.add(btnClear);
		toolBar.addSeparator();
		
		btnReload = new JButton("");
		btnReload.setToolTipText("Reload saved simulation");
		btnReload.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/edit-undo.png")));
		btnReload.setEnabled(false);
		btnReload.addActionListener(new ReloadAL());		
		
		btnSave = new JButton("");
		btnSave.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/flag-yellow.png")));
		btnSave.setToolTipText("Save current state of the simulation");
		btnSave.addActionListener(new SaveAL());
		btnSave.setEnabled(false);
		toolBar.add(btnSave);
		toolBar.add(btnReload);
		
		
		
		panel_8 = new JPanel();
		sidePanel.add(panel_8, BorderLayout.CENTER);
		panel_8.setLayout(new BorderLayout(0, 0));
		
		progressPanel = new JPanel();
		panel_8.add(progressPanel, BorderLayout.NORTH);
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
		sliderSpeed.setMinimumSize(new Dimension(36, 20));
		sliderSpeed.setPreferredSize(new Dimension(200, 20));
		sliderSpeed.setMinimum(1);
		sliderSpeed.setMaximum(30);
		progressPanel.add(sliderSpeed);
		
		set_speed((int) CulturalParameters.sp_checkpoints.getValue());

		tabEventSet = new JPanel();
		panel_8.add(tabEventSet, BorderLayout.CENTER);
		tabEventSet.setPreferredSize(new Dimension(10, 270));
		tabEventSet.setBorder(null);
		tabEventSet.setLayout(new BorderLayout(0, 0));
		
		structureDialog = new DoubleDistributionDialog(new Distribution(0.5,0.5,0.8), null, "Apostasy", "Destroy", this);
		contentDialog = new DoubleDistributionDialog(new Distribution(1.0),null, "Partial", "Full", this);
		conversionDialog = new DoubleDistributionDialog(null, new Distribution(0.5,0.5,0.2), "Partial", "Full", this);
		invasionDialog = new SingleDistributionDialog(new Distribution(0.5,0.5,0.2), "Invasion", this);
		genocideDialog = new SingleDistributionDialog(new Distribution(0.5,0.5,0.2), "Genocide", this);
		parametersDialog = new ParametersDialog("Parameter Change Event", this);

		
		JPanel eventpanels = new JPanel();
		eventpanels.setBorder(new EmptyBorder(5, 0, 0, 0));
		eventpanels.setPreferredSize(new Dimension(10, 446));
		tabEventSet.add(eventpanels, BorderLayout.NORTH);
		eventpanels.setLayout(new GridLayout(6, 0, 0, 0));
		EventPanel structurePanelSet = new EventPanel("Institutional structure attack", structureDialog);
		structurePanelSet.addAddActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (structureDialog.get_distribution1() != null){
					events.add(new Apostasy(structureDialog.get_distribution1()));
				}
				if (structureDialog.get_distribution2() != null){
					events.add(new DestroyInstitutions(structureDialog.get_distribution2()));
				}
				update_event_set();
			}
		});
		structurePanelSet.addApplyActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Event> events = new ArrayList<Event>();
				if (structureDialog.get_distribution1() != null){
					events.add(new Apostasy(structureDialog.get_distribution1()));
				}
				if (structureDialog.get_distribution2() != null){
					events.add(new DestroyInstitutions(structureDialog.get_distribution2()));
				}
				controller.add_events(events);
			}
		});
		structureDialog.addNotifiable(structurePanelSet);
		eventpanels.add(structurePanelSet);
		EventPanel contentPanelSet = new EventPanel("Institutional content removal", contentDialog);
		contentPanelSet.addAddActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (contentDialog.get_distribution1() != null){
					events.add(new RemoveInstitutionsPartialContent(contentDialog.get_distribution1()));
				}
				if (contentDialog.get_distribution2() != null){
					events.add(new RemoveInstitutionsContent(contentDialog.get_distribution2()));
				}
				update_event_set();
			}
		});
		contentPanelSet.addApplyActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Event> events = new ArrayList<Event>();
				if (contentDialog.get_distribution1() != null){
					events.add(new RemoveInstitutionsPartialContent(contentDialog.get_distribution1()));
				}
				if (contentDialog.get_distribution2() != null){
					events.add(new RemoveInstitutionsContent(contentDialog.get_distribution2()));
				}
				controller.add_events(events);
			}
		});
		contentDialog.addNotifiable(contentPanelSet);
		eventpanels.add(contentPanelSet);
		EventPanel conversionPanelSet = new EventPanel("Institutional conversion", conversionDialog);
		conversionPanelSet.addAddActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (conversionDialog.get_distribution1() != null){
					events.add(new ConvertTraits(conversionDialog.get_distribution1()));
				}
				if (conversionDialog.get_distribution2() != null) {
					events.add(new ConvertInstitutions(conversionDialog.get_distribution2()));
				}
				update_event_set();
			}
		});
		conversionPanelSet.addApplyActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Event> events = new ArrayList<Event>();
				if (conversionDialog.get_distribution1() != null){
					events.add(new ConvertTraits(conversionDialog.get_distribution1()));
				}
				if (conversionDialog.get_distribution2() != null) {
					events.add(new ConvertInstitutions(conversionDialog.get_distribution2()));
				}
				controller.add_events(events);
			}
		});
		conversionDialog.addNotifiable(conversionPanelSet);
		eventpanels.add(conversionPanelSet);
		EventPanel invasionPanelSet = new EventPanel("Invasion", invasionDialog);
		invasionPanelSet.addAddActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (invasionDialog.get_distribution() != null){
					events.add(new Invasion(invasionDialog.get_distribution()));
					update_event_set();
				}
			}
		});
		invasionPanelSet.addApplyActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Event> events = new ArrayList<Event>();
				if (invasionDialog.get_distribution() != null){
					events.add(new Invasion(invasionDialog.get_distribution()));
				}
				controller.add_events(events);
			}
		});
		invasionDialog.addNotifiable(invasionPanelSet);
		eventpanels.add(invasionPanelSet);
		EventPanel genocidePanelSet = new EventPanel("Genocide", genocideDialog);
		genocidePanelSet.addAddActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (genocideDialog.get_distribution() != null){
					events.add(new Genocide(genocideDialog.get_distribution()));
					update_event_set();
				}
			}
		});
		genocidePanelSet.addApplyActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Event> events = new ArrayList<Event>();
				if (genocideDialog.get_distribution() != null){
					events.add(new Genocide(genocideDialog.get_distribution()));
				}
				controller.add_events(events);
			}
		});
		genocideDialog.addNotifiable(genocidePanelSet);
		eventpanels.add(genocidePanelSet);
		parameterEventPanel = new EventPanel("Parameter Change Event", parametersDialog);
		parameterEventPanel.addAddActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (parametersDialog.get_parameter_change_event() != null){
					events.add(parametersDialog.get_parameter_change_event());
					update_event_set();
				}
			}
		});
		parameterEventPanel.addApplyActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Event> events = new ArrayList<Event>();
				if (parametersDialog.get_parameter_change_event() != null){
					events.add(parametersDialog.get_parameter_change_event());
				}
				controller.add_events(events);
			}
		});
		parametersDialog.addWindowListener(
				new WindowAdapter() {
					@Override
		            public void windowActivated(WindowEvent e) {
						controller.restore_parameters_to_interface();
						parametersDialog.refresh_dialog();
		            }
				});
		parametersDialog.addNotifiable(parameterEventPanel);
		eventpanels.add(parameterEventPanel);
		
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
		flowLayout.setVgap(1);
		flowLayout.setAlignment(FlowLayout.RIGHT);
		panel_27.add(panel_26, BorderLayout.CENTER);
		
		button_1 = new JButton("");
		button_1.setToolTipText("Open events");
		
		File eve_dir = new File( Controller.WORKSPACE_DIR + Controller.EVENTS_DIR);;
		if (!eve_dir.exists()){
			eve_dir.mkdirs();
        }
		jfc_events.setCurrentDirectory(eve_dir);
		
		button_1.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				if (jfc_events.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					String dis_file = jfc_events.getSelectedFile().getAbsolutePath();
					try {
						ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(dis_file));
						events = (ArrayList<Event>) inFile.readObject();
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
		button_1.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/document-open.png")));
		button_1.setSize(new Dimension(33, 9));
		button_1.setMargin(new Insets(2, 2, 2, 2));
		panel_26.add(button_1);
		
		button = new JButton("");
		button.setToolTipText("Save events");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (jfc_events.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					String dis_file = jfc_events.getSelectedFile().getAbsolutePath();
					try {
						ObjectOutputStream write = new ObjectOutputStream (new FileOutputStream(dis_file));				
						write.writeObject(events);
						write.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}
				
			}
			
		});
		button.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/document-save.png")));
		button.setSize(new Dimension(33, 9));
		button.setMargin(new Insets(2, 2, 2, 2));
		panel_26.add(button);
		
		button_6 = new JButton("");
		button_6.setToolTipText("Clear events");
		button_6.setSize(new Dimension(33, 9));
		button_6.setMargin(new Insets(2, 2, 2, 2));
		button_6.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/edit-clear-list.png")));
		panel_26.add(button_6);
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				events.clear();
				update_event_set();
			}
		});
		
		JButton btnClear_1 = new JButton("");
		btnClear_1.setToolTipText("Apply events");
		btnClear_1.setMargin(new Insets(2, 2, 2, 2));
		btnClear_1.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/go-jump-locationbar.png")));
		panel_26.add(btnClear_1);
		btnClear_1.addActionListener(new ActionListener() {
			
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent arg0) {
				controller.add_events((ArrayList<Event>) events.clone());
				
			}
		});
		
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
				
				ParametersDialog.checkpoints = speed;
				ParameterChange pc = new ParameterChange();
				pc.checkpoints = speed;
			    ArrayList<Event> evs =  new ArrayList<Event>();
			    evs.add(pc);
				controller.add_events(evs);
			}
		});

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
			parametersDialog.refresh_dialog();
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
		return (controller.is_saved() || 
				JOptionPane.showConfirmDialog(c, 
						"There is a non-saved simulation running.\nAre you sure you want to "
						+ "continue and discard the current simulation?", 
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
						controller.start(parameters_dialog.tf_workspace_dir.getText(), "results");
						
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

	@Override
	public void update() {
		tglbtnStop.doClick();
		
	}
}

