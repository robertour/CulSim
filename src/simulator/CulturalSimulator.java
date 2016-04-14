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
import simulator.gui.BatchMode;
import simulator.gui.CulturalParameters;
import simulator.gui.DistributionDoubleDialog;
import simulator.gui.DistributionSingleDialog;
import simulator.gui.EventPanel;
import simulator.gui.GraphPanel;
import simulator.gui.Notifiable;
import simulator.gui.OutputArea;
import simulator.gui.ParametersEventDialog;
import simulator.control.events.Distribution;
import simulator.control.events.Event;
import simulator.control.events.Murder;
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

/**
 * Main interface of the simulation. It contains components to control all the
 * graphical functionality of the software.
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 *
 */
public class CulturalSimulator extends JFrame implements Notifiable {

	private static final long serialVersionUID = -6498782807057797553L;
	private JPanel contentPane;

	/**
	 * Save and load dialogs for simualtions and events
	 */
	private JFileChooser jfc_simulation_state = new JFileChooser();
	private JFileChooser jfc_events = new JFileChooser();

	/**
	 * Output area of the simulation
	 */
	private OutputArea output_area;

	/**
	 * The buttons that control the simulation
	 */
	private JToggleButton tglbtnPlay;
	private JToggleButton tglbtnPause;
	private JToggleButton tglbtnStop;
	private JButton btnClear;
	private JButton btnSave;
	private JButton btnReload;

	/**
	 * The menu items that need to be controlled when the simulation is running
	 */
	private JMenuItem mntmSafeWorldState;
	private JMenuItem mntmLoadSimulationState;
	private JMenuItem mntmPlay;
	private JMenuItem mntmPause;
	private JMenuItem mntmStop;
	private JMenuItem mntmClear;
	private JMenuItem mntmSave;
	private JMenuItem mntmReload;
	private JMenu mnSimulation;

	/**
	 * These components are related to the event sets
	 */
	private JTextArea taEventSet;

	/**
	 * These are the dialogs for configuring the events
	 */
	private DistributionDoubleDialog structureDialog;
	private DistributionDoubleDialog contentDialog;
	private DistributionDoubleDialog conversionDialog;
	private DistributionSingleDialog invasionDialog;
	private DistributionSingleDialog genocideDialog;
	private ParametersEventDialog parametersDialog;

	/**
	 * These are the dialogs for the initial parameters configuration and the
	 * batch mode
	 */
	private static CulturalParameters parameters_dialog;
	private static BatchMode batch_mode_dialog;

	/**
	 * These are the informational spaces in the middle
	 */
	private static JLabel belief_space = new JLabel("");
	private static JLabel alife_institutional_beliefs_space = new JLabel("");
	private static JLabel alife_institutions = new JLabel("");
	private static JLabel institutional_beliefs_association = new JLabel("");

	/**
	 * This are the labels on the status bar
	 */
	public static JLabel l_start_identification;
	public static JLabel l_current_identification;

	/**
	 * These are the graph panels on the right
	 */
	public static GraphPanel graphCultures;
	public static GraphPanel graphInstitutions;
	public static GraphPanel graphNeumann;
	public static GraphPanel graphEnergy;
	public static GraphPanel graphNeumannSimilarity;
	public static GraphPanel graphCultureSimilarity;
	public static GraphPanel graphPixels;

	/**
	 * This are labels that can be updated from the controller
	 */
	public static JLabel lblEnergy;
	public static JLabel lblCultures;
	public static JLabel lblNeumann;
	public static JLabel lblInstitutions;
	public static JLabel lblNeumannSimilarity;
	public static JLabel lblPixels;
	public static JLabel lblCultureSimilarity;

	/**
	 * This defines the easy speed configuration
	 */
	private static JLabel lblSpeedValue;
	private static JSlider sliderSpeed;

	/**
	 * Non graphical components
	 */
	public static ControllerSingle controller;
	private ArrayList<Event> events = new ArrayList<Event>();
	private static int speed;

	/**
	 * This the event that launches the application
	 * 
	 * @param args
	 *            no arguments defined
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

					UIManager.getDefaults().put("TabbedPane.contentBorderInsets", new Insets(1, 0, 0, 0));
					UIManager.getDefaults().put("TabbedPane.tabAreaInsets", new Insets(0, 0, 0, 0));
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
				if (want_to_continue(CulturalSimulator.this)) {
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

		File world_dir = new File(Controller.WORKSPACE_DIR + Controller.WORLDS_DIR);
		;
		if (!world_dir.exists()) {
			world_dir.mkdirs();
		}
		jfc_simulation_state.setCurrentDirectory(world_dir);
		mntmSafeWorldState = new JMenuItem("Safe Simualtion State");
		mntmSafeWorldState.setToolTipText("Save the current state of the simulation");
		mntmSafeWorldState
				.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/document-save.png")));
		mntmSafeWorldState.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jfc_simulation_state.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					if (want_to_continue(jfc_simulation_state)) {
						String conf_file = jfc_simulation_state.getSelectedFile().getAbsolutePath();
						controller.save_simulation(conf_file);
					}
				}
			}
		});
		mnFile.add(mntmSafeWorldState);

		mntmLoadSimulationState = new JMenuItem("Load Simulation State");
		mntmLoadSimulationState.setToolTipText("Load a simulation state from a file");
		mntmLoadSimulationState
				.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/document-open.png")));
		mntmLoadSimulationState.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jfc_simulation_state.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					if (want_to_continue(jfc_simulation_state)) {
						String conf_file = jfc_simulation_state.getSelectedFile().getAbsolutePath();
						try {
							controller.load_simulation(conf_file);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		mnFile.add(mntmLoadSimulationState);
		mnFile.addSeparator();
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setToolTipText("Exit the cultural simulator");
		mntmExit.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/system-shutdown.png")));
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (want_to_continue(CulturalSimulator.this)) {
					controller.cancel();
					CulturalSimulator.this.dispose();
				}
			}
		});
		mnFile.add(mntmExit);

		JMenu mnControls = new JMenu("Controls");
		menuBar.add(mnControls);

		mntmPlay = new JMenuItem("Play");
		mntmPlay.setToolTipText("Play the simulation");
		mntmPlay.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/media-playback-start.png")));
		mntmPlay.addActionListener(new PlayAL());
		mnControls.add(mntmPlay);

		mntmPause = new JMenuItem("Pause");
		mntmPause.setToolTipText("Pause the simulation");
		mntmPause
				.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/media-playback-pause.png")));
		mntmPause.addActionListener(new PauseAL());
		mnControls.add(mntmPause);

		mntmStop = new JMenuItem("Stop");
		mntmStop.setToolTipText("Stop the simulation");
		mntmStop.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/media-playback-stop.png")));
		mntmStop.addActionListener(new StopAL());
		mnControls.add(mntmStop);

		mntmClear = new JMenuItem("Restart");
		mntmClear.setToolTipText("Restart the simulation");
		mntmClear.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/media-skip-backward.png")));
		mntmClear.addActionListener(new ClearAL());
		mnControls.add(mntmClear);

		mntmReload = new JMenuItem("Reload");
		mntmReload.setToolTipText("Reload previously saved state of the simulation");
		mntmReload.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/edit-undo.png")));
		mntmReload.addActionListener(new ReloadAL());

		mntmSave = new JMenuItem("Save");
		mntmSave.setToolTipText("Save the state of the simulation");
		mntmSave.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/flag-yellow.png")));
		mntmSave.addActionListener(new SaveAL());
		mnControls.add(mntmSave);
		mnControls.add(mntmReload);

		mnSimulation = new JMenu("Simulation");
		menuBar.add(mnSimulation);

		JMenuItem mntmParameters = new JMenuItem("Parameters");
		mntmParameters.setToolTipText("Define the initial parameters of the simulation");
		mntmParameters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.restore_parameters_to_interface();
				parameters_dialog.setVisible(true);
			}
		});

		mntmParameters
				.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/run-build-configure.png")));
		mnSimulation.add(mntmParameters);
		mnSimulation.addSeparator();

		JMenuItem mntmBatchMode = new JMenuItem("Batch Mode");
		mntmBatchMode.setToolTipText("Dialog to run big experimental design");
		mntmBatchMode
				.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/view-calendar-tasks.png")));
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

		JSplitPane splitRestVSGraphs = new JSplitPane();
		contentPane.add(splitRestVSGraphs, BorderLayout.CENTER);

		JPanel panelGraphs = new JPanel();
		splitRestVSGraphs.setRightComponent(panelGraphs);
		panelGraphs.setLayout(new GridLayout(4, 2, 0, 0));

		JPanel panelGraphControls = new JPanel();
		panelGraphs.add(panelGraphControls);

		JPanel energyPanel = new JPanel();
		panelGraphs.add(energyPanel);
		energyPanel.setLayout(new BorderLayout(0, 0));

		JPanel panelEnergyLabels = new JPanel();
		energyPanel.add(panelEnergyLabels, BorderLayout.NORTH);
		panelEnergyLabels.setLayout(new BorderLayout(0, 0));

		JLabel lblEnergyTitle = new JLabel("Energy:");
		lblEnergyTitle.setToolTipText(
				"Blue line (left number) is the energy of the system, and red line (right number) is the dispersion of the foreign trait.");
		panelEnergyLabels.add(lblEnergyTitle, BorderLayout.WEST);
		lblEnergyTitle.setHorizontalAlignment(SwingConstants.LEFT);

		lblEnergy = new JLabel("");
		lblEnergy.setForeground(SystemColor.textHighlight);
		lblEnergy.setToolTipText(
				"Blue line (left number) is the energy of the system, and red line (right number) is the dispersion of the foreign trait.");
		panelEnergyLabels.add(lblEnergy, BorderLayout.EAST);

		graphEnergy = new GraphPanel();
		energyPanel.add(graphEnergy, BorderLayout.CENTER);

		JPanel culturesPanel = new JPanel();
		panelGraphs.add(culturesPanel);
		culturesPanel.setLayout(new BorderLayout(0, 0));

		graphCultures = new GraphPanel();
		culturesPanel.add(graphCultures, BorderLayout.CENTER);

		JPanel panelCulturesLabels = new JPanel();
		culturesPanel.add(panelCulturesLabels, BorderLayout.NORTH);
		panelCulturesLabels.setLayout(new BorderLayout(0, 0));

		JLabel lblCulturesTitle = new JLabel("Cultures:");
		lblCulturesTitle.setToolTipText(
				"Blue line (left number) is the number of cultures, and red line (right number) the size of the biggest culture. The cultures are defined through the scope of immediate neighbors.");
		panelCulturesLabels.add(lblCulturesTitle, BorderLayout.WEST);

		lblCultures = new JLabel("");
		lblCultures.setBorder(null);
		lblCultures.setForeground(SystemColor.textHighlight);
		lblCultures.setToolTipText(
				"Blue line (left number) is the number of cultures, and red line (right number) the size of the biggest culture. The cultures are defined through the scope of immediate neighbors.");
		panelCulturesLabels.add(lblCultures, BorderLayout.EAST);

		JPanel neumannPanel = new JPanel();
		panelGraphs.add(neumannPanel);
		neumannPanel.setLayout(new BorderLayout(0, 0));

		JPanel panelNeumannLabels = new JPanel();
		neumannPanel.add(panelNeumannLabels, BorderLayout.NORTH);
		panelNeumannLabels.setLayout(new BorderLayout(0, 0));

		JLabel lblNeumannTitle = new JLabel("Newmann's:");
		lblNeumannTitle.setToolTipText(
				"Blue line (left number) is the number of cultures, and red line (right number) the size of the biggest culture. The cultures are defined through the scope of the neighborhood radius.");
		panelNeumannLabels.add(lblNeumannTitle, BorderLayout.WEST);
		lblNeumannTitle.setHorizontalAlignment(SwingConstants.LEFT);

		lblNeumann = new JLabel("");
		lblNeumann.setForeground(SystemColor.textHighlight);
		lblNeumann.setToolTipText(
				"Blue line (left number) is the number of cultures, and red line (right number) the size of the biggest culture. The cultures are defined through the scope of the neighborhood radius.");
		panelNeumannLabels.add(lblNeumann, BorderLayout.EAST);

		graphNeumann = new GraphPanel();
		neumannPanel.add(graphNeumann, BorderLayout.CENTER);

		JPanel cultureSimilarityPanel = new JPanel();
		panelGraphs.add(cultureSimilarityPanel);
		cultureSimilarityPanel.setLayout(new BorderLayout(0, 0));

		graphCultureSimilarity = new GraphPanel();
		cultureSimilarityPanel.add(graphCultureSimilarity, BorderLayout.CENTER);

		JPanel panelCultureSimilarityLabels = new JPanel();
		cultureSimilarityPanel.add(panelCultureSimilarityLabels, BorderLayout.NORTH);
		panelCultureSimilarityLabels.setLayout(new BorderLayout(0, 0));

		JLabel lblCultureSimilarityTitle = new JLabel("Culture's Sim:");
		lblCultureSimilarityTitle.setToolTipText(
				"Blue line (left number) is the energy of the system, and red line (right number) is the dispersion of the foreign trait.");
		lblCultureSimilarityTitle.setHorizontalAlignment(SwingConstants.LEFT);
		panelCultureSimilarityLabels.add(lblCultureSimilarityTitle, BorderLayout.WEST);

		lblCultureSimilarity = new JLabel("");
		lblCultureSimilarity.setToolTipText(
				"Blue line (left number) is the energy of the system, and red line (right number) is the dispersion of the foreign trait.");
		lblCultureSimilarity.setForeground(SystemColor.textHighlight);
		panelCultureSimilarityLabels.add(lblCultureSimilarity, BorderLayout.EAST);

		JPanel neumannSimilarityPanel = new JPanel();
		panelGraphs.add(neumannSimilarityPanel);
		neumannSimilarityPanel.setPreferredSize(new Dimension(10, 160));
		neumannSimilarityPanel.setLayout(new BorderLayout(0, 0));

		JPanel panelNeumannSimilarityLabels = new JPanel();
		neumannSimilarityPanel.add(panelNeumannSimilarityLabels, BorderLayout.NORTH);
		panelNeumannSimilarityLabels.setLayout(new BorderLayout(0, 0));

		JLabel lblNeumannSimilarityTitle = new JLabel("Newmann's Sim:");
		lblNeumannSimilarityTitle.setToolTipText(
				"Blue line (left number) is the energy of the system, and red line (right number) is the dispersion of the foreign trait.");
		lblNeumannSimilarityTitle.setHorizontalAlignment(SwingConstants.LEFT);
		panelNeumannSimilarityLabels.add(lblNeumannSimilarityTitle, BorderLayout.WEST);

		lblNeumannSimilarity = new JLabel("");
		lblNeumannSimilarity.setForeground(SystemColor.textHighlight);
		lblNeumannSimilarity.setToolTipText(
				"Blue line (left number) is the energy of the system, and red line (right number) is the dispersion of the foreign trait.");
		panelNeumannSimilarityLabels.add(lblNeumannSimilarity, BorderLayout.EAST);

		graphNeumannSimilarity = new GraphPanel();
		neumannSimilarityPanel.add(graphNeumannSimilarity, BorderLayout.CENTER);

		JPanel institutionsPanel = new JPanel();
		panelGraphs.add(institutionsPanel);
		institutionsPanel.setLayout(new BorderLayout(0, 0));

		graphInstitutions = new GraphPanel();
		institutionsPanel.add(graphInstitutions, BorderLayout.CENTER);

		JPanel panelInstitutionsLabels = new JPanel();
		institutionsPanel.add(panelInstitutionsLabels, BorderLayout.NORTH);
		panelInstitutionsLabels.setLayout(new BorderLayout(0, 0));

		JLabel lblInstitutionsTitle = new JLabel("Institutions:");
		lblInstitutionsTitle.setToolTipText(
				"Blue line (left number) is the number of instititutions, and red line (right number) the size of the biggest institution");
		panelInstitutionsLabels.add(lblInstitutionsTitle, BorderLayout.WEST);

		lblInstitutions = new JLabel("");
		lblInstitutions.setForeground(SystemColor.textHighlight);
		lblInstitutions.setToolTipText(
				"Blue line (left number) is the number of instititutions, and red line (right number) the size of the biggest institution");
		panelInstitutionsLabels.add(lblInstitutions, BorderLayout.EAST);

		JPanel pixelPanel = new JPanel();
		panelGraphs.add(pixelPanel);
		pixelPanel.setLayout(new BorderLayout(0, 0));

		JPanel panelPixelsLabels = new JPanel();
		pixelPanel.add(panelPixelsLabels, BorderLayout.NORTH);
		panelPixelsLabels.setLayout(new BorderLayout(0, 0));

		JLabel lblPixelsTitle = new JLabel("Pixels:");
		panelPixelsLabels.add(lblPixelsTitle, BorderLayout.WEST);
		lblPixelsTitle.setToolTipText(
				"Blue line (left number) is the number of instititutions, and red line (right number) the size of the biggest institution");

		lblPixels = new JLabel("");
		lblPixels.setToolTipText(
				"Blue line (left number) is the number of instititutions, and red line (right number) the size of the biggest institution");
		lblPixels.setForeground(SystemColor.textHighlight);
		panelPixelsLabels.add(lblPixels, BorderLayout.EAST);

		graphPixels = new GraphPanel();
		pixelPanel.add(graphPixels, BorderLayout.CENTER);

		JSplitPane splitSideVsRest = new JSplitPane();
		splitRestVSGraphs.setLeftComponent(splitSideVsRest);

		JSplitPane splitGraphVsOutput = new JSplitPane();
		splitSideVsRest.setRightComponent(splitGraphVsOutput);
		splitGraphVsOutput.setOrientation(JSplitPane.VERTICAL_SPLIT);

		JPanel panelSpaces = new JPanel();
		splitGraphVsOutput.setLeftComponent(panelSpaces);
		panelSpaces.setLayout(new GridLayout(2, 2, 10, 10));

		JPanel panelBeliefSpace = new JPanel();
		panelSpaces.add(panelBeliefSpace);
		panelBeliefSpace.setLayout(new BorderLayout(0, 0));

		JLabel lblBeliefSpace = new JLabel("Belief Space");
		lblBeliefSpace.setHorizontalTextPosition(SwingConstants.CENTER);
		lblBeliefSpace.setHorizontalAlignment(SwingConstants.CENTER);

		panelBeliefSpace.add(lblBeliefSpace, BorderLayout.NORTH);
		panelBeliefSpace.add(belief_space, BorderLayout.CENTER);
		belief_space.setBackground(Color.WHITE);
		belief_space.setOpaque(true);

		JPanel panelInstBeliefSpace = new JPanel();
		panelSpaces.add(panelInstBeliefSpace);
		panelInstBeliefSpace.setLayout(new BorderLayout(0, 0));

		JLabel lblCorrespondinInstitutionalBelief = new JLabel("Corresponding Institutional Belief Space");
		lblCorrespondinInstitutionalBelief.setHorizontalAlignment(SwingConstants.CENTER);
		lblCorrespondinInstitutionalBelief.setHorizontalTextPosition(SwingConstants.CENTER);
		panelInstBeliefSpace.add(lblCorrespondinInstitutionalBelief, BorderLayout.NORTH);
		panelInstBeliefSpace.add(institutional_beliefs_association, BorderLayout.CENTER);
		institutional_beliefs_association.setOpaque(true);
		institutional_beliefs_association.setBackground(Color.WHITE);

		JPanel panelInstitutions = new JPanel();
		panelSpaces.add(panelInstitutions);
		panelInstitutions.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel = new JLabel("Institutions");
		lblNewLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

		panelInstitutions.add(lblNewLabel, BorderLayout.NORTH);
		panelInstitutions.add(alife_institutional_beliefs_space, BorderLayout.CENTER);
		alife_institutional_beliefs_space.setBackground(Color.WHITE);
		alife_institutional_beliefs_space.setOpaque(true);

		JPanel panelAlifeInstitutions = new JPanel();
		panelSpaces.add(panelAlifeInstitutions);
		panelAlifeInstitutions.setLayout(new BorderLayout(0, 0));

		JLabel lblExistentInstitutions = new JLabel("Existent Institutions");
		lblExistentInstitutions.setHorizontalTextPosition(SwingConstants.CENTER);
		lblExistentInstitutions.setHorizontalAlignment(SwingConstants.CENTER);
		panelAlifeInstitutions.add(lblExistentInstitutions, BorderLayout.NORTH);
		panelAlifeInstitutions.add(alife_institutions, BorderLayout.CENTER);
		alife_institutions.setOpaque(true);
		alife_institutions.setBackground(Color.WHITE);

		JPanel panelOutput = new JPanel();
		splitGraphVsOutput.setRightComponent(panelOutput);
		panelOutput.setMinimumSize(new Dimension(10, 100));
		panelOutput.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setMinimumSize(new Dimension(23, 200));
		panelOutput.add(scrollPane);

		OutputArea output_area = new OutputArea();
		output_area.setLineWrap(true);
		output_area.setFont(new Font("Tahoma", Font.PLAIN, 10));
		output_area.setEditable(false);
		scrollPane.setViewportView(output_area);
		splitGraphVsOutput.setDividerLocation(500);

		controller = new ControllerSingle(output_area, this);
		parameters_dialog = new CulturalParameters(this);

		JPanel sidePanel = new JPanel();
		splitSideVsRest.setLeftComponent(sidePanel);
		sidePanel.setLayout(new BorderLayout(0, 0));

		JToolBar toolBar = new JToolBar();
		sidePanel.add(toolBar, BorderLayout.NORTH);
		toolBar.setFloatable(false);

		tglbtnPlay = new JToggleButton("");
		tglbtnPlay.setToolTipText("Play the simulation");
		tglbtnPlay
				.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/media-playback-start.png")));
		tglbtnPlay.addActionListener(new PlayAL());
		toolBar.add(tglbtnPlay);

		tglbtnPause = new JToggleButton("");
		tglbtnPause.setToolTipText("Pause the simulation");
		tglbtnPause
				.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/media-playback-pause.png")));
		tglbtnPause.addActionListener(new PauseAL());
		tglbtnPause.setEnabled(false);
		toolBar.add(tglbtnPause);

		tglbtnStop = new JToggleButton("");
		tglbtnStop
				.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/media-playback-stop.png")));
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

		JPanel panelSpeed = new JPanel();
		sidePanel.add(panelSpeed, BorderLayout.CENTER);
		panelSpeed.setLayout(new BorderLayout(0, 0));

		JPanel panelProgress = new JPanel();
		panelSpeed.add(panelProgress, BorderLayout.NORTH);
		panelProgress.setLayout(new BorderLayout(0, 0));

		JPanel panelSpeedLabels = new JPanel();
		FlowLayout fl_panelSpeedLabels = (FlowLayout) panelSpeedLabels.getLayout();
		fl_panelSpeedLabels.setHgap(0);
		panelProgress.add(panelSpeedLabels, BorderLayout.WEST);

		JLabel lblSpeed = new JLabel("Speed: ");
		panelSpeedLabels.add(lblSpeed);

		lblSpeedValue = new JLabel("1000");
		panelSpeedLabels.add(lblSpeedValue);
		lblSpeedValue.setPreferredSize(new Dimension(24, 20));
		lblSpeedValue.setHorizontalTextPosition(SwingConstants.LEFT);
		lblSpeedValue.setHorizontalAlignment(SwingConstants.LEFT);
		lblSpeedValue.setForeground(SystemColor.textHighlight);

		sliderSpeed = new JSlider();
		sliderSpeed.setMinimumSize(new Dimension(36, 20));
		sliderSpeed.setPreferredSize(new Dimension(200, 20));
		sliderSpeed.setMinimum(1);
		sliderSpeed.setMaximum(30);
		panelProgress.add(sliderSpeed);

		set_speed((int) CulturalParameters.sp_checkpoints.getValue());

		JPanel eventsPanel = new JPanel();
		panelSpeed.add(eventsPanel, BorderLayout.CENTER);
		eventsPanel.setPreferredSize(new Dimension(10, 270));
		eventsPanel.setBorder(null);
		eventsPanel.setLayout(new BorderLayout(0, 0));

		structureDialog = new DistributionDoubleDialog(new Distribution(0.5, 0.5, 0.8), null, "Apostasy", "Destroy",
				this);
		contentDialog = new DistributionDoubleDialog(new Distribution(1.0), null, "Partial", "Full", this);
		conversionDialog = new DistributionDoubleDialog(null, new Distribution(0.5, 0.5, 0.2), "Partial", "Full", this);
		invasionDialog = new DistributionSingleDialog(new Distribution(0.5, 0.5, 0.2), "Invasion", this);
		genocideDialog = new DistributionSingleDialog(new Distribution(0.5, 0.5, 0.2), "Genocide", this);
		parametersDialog = new ParametersEventDialog("Parameter Change Event", this);

		JPanel eventPanels = new JPanel();
		eventPanels.setBorder(new EmptyBorder(5, 0, 0, 0));
		eventPanels.setPreferredSize(new Dimension(10, 446));
		eventsPanel.add(eventPanels, BorderLayout.NORTH);
		eventPanels.setLayout(new GridLayout(6, 0, 0, 0));
		EventPanel structurePanelSet = new EventPanel("Institutional structure attack", structureDialog);
		structurePanelSet.addAddActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (structureDialog.get_distribution1() != null) {
					events.add(new Apostasy(structureDialog.get_distribution1()));
				}
				if (structureDialog.get_distribution2() != null) {
					events.add(new DestroyInstitutions(structureDialog.get_distribution2()));
				}
				update_event_set();
			}
		});
		structurePanelSet.addApplyActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Event> events = new ArrayList<Event>();
				if (structureDialog.get_distribution1() != null) {
					events.add(new Apostasy(structureDialog.get_distribution1()));
				}
				if (structureDialog.get_distribution2() != null) {
					events.add(new DestroyInstitutions(structureDialog.get_distribution2()));
				}
				controller.add_events(events);
			}
		});
		structureDialog.addNotifiable(structurePanelSet);
		eventPanels.add(structurePanelSet);
		EventPanel contentPanelSet = new EventPanel("Institutional content removal", contentDialog);
		contentPanelSet.addAddActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (contentDialog.get_distribution1() != null) {
					events.add(new RemoveInstitutionsPartialContent(contentDialog.get_distribution1()));
				}
				if (contentDialog.get_distribution2() != null) {
					events.add(new RemoveInstitutionsContent(contentDialog.get_distribution2()));
				}
				update_event_set();
			}
		});
		contentPanelSet.addApplyActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Event> events = new ArrayList<Event>();
				if (contentDialog.get_distribution1() != null) {
					events.add(new RemoveInstitutionsPartialContent(contentDialog.get_distribution1()));
				}
				if (contentDialog.get_distribution2() != null) {
					events.add(new RemoveInstitutionsContent(contentDialog.get_distribution2()));
				}
				controller.add_events(events);
			}
		});
		contentDialog.addNotifiable(contentPanelSet);
		eventPanels.add(contentPanelSet);
		EventPanel conversionPanelSet = new EventPanel("Institutional conversion", conversionDialog);
		conversionPanelSet.addAddActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (conversionDialog.get_distribution1() != null) {
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
				if (conversionDialog.get_distribution1() != null) {
					events.add(new ConvertTraits(conversionDialog.get_distribution1()));
				}
				if (conversionDialog.get_distribution2() != null) {
					events.add(new ConvertInstitutions(conversionDialog.get_distribution2()));
				}
				controller.add_events(events);
			}
		});
		conversionDialog.addNotifiable(conversionPanelSet);
		eventPanels.add(conversionPanelSet);
		EventPanel invasionPanelSet = new EventPanel("Invasion", invasionDialog);
		invasionPanelSet.addAddActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (invasionDialog.get_distribution() != null) {
					events.add(new Invasion(invasionDialog.get_distribution()));
					update_event_set();
				}
			}
		});
		invasionPanelSet.addApplyActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Event> events = new ArrayList<Event>();
				if (invasionDialog.get_distribution() != null) {
					events.add(new Invasion(invasionDialog.get_distribution()));
				}
				controller.add_events(events);
			}
		});
		invasionDialog.addNotifiable(invasionPanelSet);
		eventPanels.add(invasionPanelSet);
		EventPanel genocidePanelSet = new EventPanel("Genocide", genocideDialog);
		genocidePanelSet.addAddActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (genocideDialog.get_distribution() != null) {
					events.add(new Murder(genocideDialog.get_distribution()));
					update_event_set();
				}
			}
		});
		genocidePanelSet.addApplyActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Event> events = new ArrayList<Event>();
				if (genocideDialog.get_distribution() != null) {
					events.add(new Murder(genocideDialog.get_distribution()));
				}
				controller.add_events(events);
			}
		});
		genocideDialog.addNotifiable(genocidePanelSet);
		eventPanels.add(genocidePanelSet);
		EventPanel parameterEventPanel = new EventPanel("Parameter Change Event", parametersDialog);
		parameterEventPanel.addAddActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (parametersDialog.get_parameter_change_event() != null) {
					events.add(parametersDialog.get_parameter_change_event());
					update_event_set();
				}
			}
		});
		parameterEventPanel.addApplyActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Event> events = new ArrayList<Event>();
				if (parametersDialog.get_parameter_change_event() != null) {
					events.add(parametersDialog.get_parameter_change_event());
				}
				controller.add_events(events);
			}
		});
		parametersDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				controller.restore_parameters_to_interface();
				parametersDialog.refresh_dialog();
			}
		});
		parametersDialog.addNotifiable(parameterEventPanel);
		eventPanels.add(parameterEventPanel);

		JPanel panelSet = new JPanel();
		panelSet.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Set", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 0, 0)));
		eventsPanel.add(panelSet, BorderLayout.CENTER);
		panelSet.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollEventSet = new JScrollPane();
		scrollEventSet.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelSet.add(scrollEventSet, BorderLayout.NORTH);

		taEventSet = new JTextArea();
		taEventSet.setRows(5);
		taEventSet.setFont(new Font("Arial Narrow", Font.PLAIN, 10));
		taEventSet.setEditable(false);
		scrollEventSet.setViewportView(taEventSet);

		JPanel eventSetControls = new JPanel();
		FlowLayout fl_eventSetControls = (FlowLayout) eventSetControls.getLayout();
		fl_eventSetControls.setVgap(1);
		fl_eventSetControls.setAlignment(FlowLayout.RIGHT);
		panelSet.add(eventSetControls, BorderLayout.CENTER);

		JButton btnOpenEventSet = new JButton("");
		btnOpenEventSet.setToolTipText("Open events");

		File eve_dir = new File(Controller.WORKSPACE_DIR + Controller.EVENTS_DIR);
		;
		if (!eve_dir.exists()) {
			eve_dir.mkdirs();
		}
		jfc_events.setCurrentDirectory(eve_dir);
		btnOpenEventSet.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				File eve_dir = new File(Controller.WORKSPACE_DIR + Controller.EVENTS_DIR);
				;
				if (!eve_dir.exists()) {
					eve_dir.mkdirs();
					jfc_events.setCurrentDirectory(eve_dir);
				}
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
		btnOpenEventSet.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/document-open.png")));
		btnOpenEventSet.setSize(new Dimension(33, 9));
		btnOpenEventSet.setMargin(new Insets(2, 2, 2, 2));
		eventSetControls.add(btnOpenEventSet);

		JButton btnSaveEventSet = new JButton("");
		btnSaveEventSet.setToolTipText("Save events");
		btnSaveEventSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File eve_dir = new File(Controller.WORKSPACE_DIR + Controller.EVENTS_DIR);
				;
				if (!eve_dir.exists()) {
					eve_dir.mkdirs();
					jfc_events.setCurrentDirectory(eve_dir);
				}
				if (jfc_events.showOpenDialog(contentPane) == JFileChooser.APPROVE_OPTION) {
					String dis_file = jfc_events.getSelectedFile().getAbsolutePath();
					try {
						ObjectOutputStream write = new ObjectOutputStream(new FileOutputStream(dis_file));
						write.writeObject(events);
						write.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}

			}

		});
		btnSaveEventSet.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/document-save.png")));
		btnSaveEventSet.setSize(new Dimension(33, 9));
		btnSaveEventSet.setMargin(new Insets(2, 2, 2, 2));
		eventSetControls.add(btnSaveEventSet);

		JButton btnRemoveEventSet = new JButton("");
		btnRemoveEventSet.setToolTipText("Clear events");
		btnRemoveEventSet.setSize(new Dimension(33, 9));
		btnRemoveEventSet.setMargin(new Insets(2, 2, 2, 2));
		btnRemoveEventSet
				.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/edit-clear-list.png")));
		eventSetControls.add(btnRemoveEventSet);
		btnRemoveEventSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				events.clear();
				update_event_set();
			}
		});

		JButton btnExecuteEventSet = new JButton("");
		btnExecuteEventSet.setToolTipText("Apply events");
		btnExecuteEventSet.setMargin(new Insets(2, 2, 2, 2));
		btnExecuteEventSet
				.setIcon(new ImageIcon(CulturalSimulator.class.getResource("/simulator/img/go-jump-locationbar.png")));
		eventSetControls.add(btnExecuteEventSet);
		btnExecuteEventSet.addActionListener(new ActionListener() {

			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent arg0) {
				controller.add_events((ArrayList<Event>) events.clone());

			}
		});

		sliderSpeed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (sliderSpeed.getValue() < 10) {
					speed = sliderSpeed.getValue();
				} else if (sliderSpeed.getValue() < 20) {
					speed = 10 * (sliderSpeed.getValue() - 9);
				} else if (sliderSpeed.getValue() < 30) {
					speed = 100 * (sliderSpeed.getValue() - 19);
				}
				lblSpeedValue.setText(speed + "");

				ParametersEventDialog.checkpoints = speed;
				ParameterChange pc = new ParameterChange();
				pc.checkpoints = speed;
				ArrayList<Event> evs = new ArrayList<Event>();
				evs.add(pc);
				controller.add_events(evs);
			}
		});

		splitSideVsRest.setDividerLocation(195);
		splitRestVSGraphs.setDividerLocation(700);

		JPanel statusBarPanel = new JPanel();
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

		DefaultCaret caret = (DefaultCaret) output_area.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}

	/**
	 * Updates the display of the event set
	 */
	private void update_event_set() {
		int i = 1;
		taEventSet.setText("");
		for (Iterator<Event> iterator = events.iterator(); iterator.hasNext();) {
			Event event = (Event) iterator.next();
			taEventSet.append(i + ". " + event + "\n");
			i++;
		}
	}

	/**
	 * Check if the current simulation is saved, if it isn't it ask the user if
	 * he want to continue with the current operation
	 * 
	 * @param c
	 *            component for the modal
	 * @return answer of the user
	 */
	public static boolean want_to_continue(Component c) {
		return (controller.is_saved() || JOptionPane.showConfirmDialog(c,
				"There is a non-saved simulation running.\nAre you sure you want to "
						+ "continue and discard the current simulation?",
				"Do you want to continue?", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION);
	}

	/**
	 * This method resize the image rendering according the informational space
	 * 
	 * @param image
	 *            the image to be render
	 * @param width
	 *            the current width of the image
	 * @param height
	 *            the current height of the image
	 * @return a resized image
	 */
	public static BufferedImage resize(BufferedImage image, int width, int height) {
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
		Graphics2D g2d = (Graphics2D) bi.createGraphics();
		g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
		g2d.drawImage(image, 0, 0, width, height, null);
		g2d.dispose();
		return bi;
	}

	/**
	 * This method clean all the informational spaces
	 */
	public static void clean_informational_spaces() {
		belief_space.setIcon(null);
		institutional_beliefs_association.setIcon(null);
		alife_institutional_beliefs_space.setIcon(null);
		alife_institutions.setIcon(null);
		graphCultures.clean();
		graphInstitutions.clean();
		graphNeumann.clean();
		graphEnergy.clean();
		graphNeumannSimilarity.clean();
		graphCultureSimilarity.clean();
		graphPixels.clean();
	}

	/**
	 * Set the belief space image
	 * 
	 * @param image
	 *            image that contains the belief space
	 */
	public static void set_belief_space(BufferedImage image) {
		belief_space.setIcon(new ImageIcon(resize(image, belief_space.getWidth(), belief_space.getHeight())));
	}

	/**
	 * Set the belief space associated to the institution
	 * 
	 * @param image
	 *            the image that contains the belief space associated to the
	 *            institution
	 */
	public static void set_institutional_beliefs_association(BufferedImage image) {
		institutional_beliefs_association.setIcon(new ImageIcon(resize(image,
				institutional_beliefs_association.getWidth(), institutional_beliefs_association.getHeight())));
	}

	/**
	 * Set the institutions that are alife
	 * 
	 * @param image
	 *            the image that contains the representation of the alife
	 *            institutions
	 */
	public static void set_alife_institutions(BufferedImage image) {
		alife_institutions
				.setIcon(new ImageIcon(resize(image, alife_institutions.getWidth(), alife_institutions.getHeight())));
	}

	/**
	 * Set the belief space of the alife institutions
	 * 
	 * @param image
	 *            the image that contains the belief space of the alife
	 *            institutions
	 */
	public static void set_alife_institutional_beliefs_space(BufferedImage image) {
		alife_institutional_beliefs_space.setIcon(new ImageIcon(resize(image,
				alife_institutional_beliefs_space.getWidth(), alife_institutional_beliefs_space.getHeight())));
	}

	/**
	 * Action that controls the starting or resuming of the simulation
	 * 
	 * @author Roberto Ulloa
	 */
	private class PlayAL extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (tglbtnPlay.isEnabled() && mntmPlay.isEnabled()) {

				if (tglbtnPause.isSelected() || tglbtnStop.isSelected()) {

					if (!tglbtnPause.isSelected() && tglbtnStop.isSelected()) {
						controller.start("results", Controller.WORKSPACE_DIR);

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
					mntmLoadSimulationState.setEnabled(false);
					mnSimulation.setEnabled(false);

				} else {
					output_area.append("Warning: GUI in an unstable state");
				}
			}

		}
	}

	/**
	 * Action that controls the stopping of the simulation
	 * 
	 * @author Roberto Ulloa
	 */
	private class StopAL extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (tglbtnStop.isEnabled() && mntmStop.isEnabled()) {

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
				mntmLoadSimulationState.setEnabled(true);
				mnSimulation.setEnabled(true);

				controller.cancel();
			}

		}
	}

	/**
	 * Action that controls the pausing of the simulation
	 * 
	 * @author Roberto Ulloa
	 */
	private class PauseAL extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (tglbtnPause.isEnabled() && mntmPause.isEnabled()) {

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

	/**
	 * Action that controls the restarting of the simulation
	 * 
	 * @author Roberto Ulloa
	 */
	private class ClearAL extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {

			controller.restart_simulation();

			btnReload.setEnabled(false);
			mntmReload.setEnabled(false);
			btnSave.setEnabled(false);
			mntmSave.setEnabled(false);
		}
	}

	/**
	 * Action that controls the reloading of the simulation
	 * 
	 * @author Roberto Ulloa
	 */
	private class ReloadAL extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			controller.reload_state();
		}
	}

	/**
	 * Action that controls the saving state of the simulation
	 * 
	 * @author Roberto Ulloa
	 */
	private class SaveAL extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {

			controller.save_state();
		}
	}

	/**
	 * This method can control the speed (how often the interface is updated and
	 * the files updated) of the simulation
	 * 
	 * @param s
	 *            the speed of the simulation
	 */
	public static void set_speed(int s) {
		speed = s;
		lblSpeedValue.setText(speed + "");
		if (speed < 10) {
			sliderSpeed.setValue(speed);
		} else if (speed < 100) {
			sliderSpeed.setValue(9 + (int) Math.round(speed / 10.0));
		} else if (speed < 1000) {
			sliderSpeed.setValue(19 + (int) Math.round(speed / 100.0));
		} else {
			sliderSpeed.setValue(30);
		}
	}

	@Override
	public void update() {
		if (tglbtnStop.isEnabled() && mntmStop.isEnabled()) {
			tglbtnStop.doClick();
		}
	}
}
