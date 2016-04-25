package simulator.gui;

import java.awt.BorderLayout;

import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSpinner;
import javax.swing.JLabel;

import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;

import simulator.CulturalSimulator;
import simulator.control.Controller;
import simulator.control.ControllerSingle;
import simulator.control.Simulation;

import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.ImageIcon;

/**
 * Interface for setting the initial parameters of the simulation.
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 */
public class CulturalParameters extends JDialog {

	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();

	/**
	 * Available implementations of simulations/worlds
	 */
	public static JComboBox<String> classSelector;

	@SuppressWarnings("rawtypes")

	/**
	 * The classes for the available implementations
	 */
	public static ArrayList<Class> classes;

	/**
	 * Pretty names for the implementations
	 */
	public static ArrayList<String> prettyNames;

	/**
	 * The components that keep the values of the parameters
	 */
	public static JCheckBox cb_random_initialization;
	public static JSpinner sp_influence;
	public static JSpinner sp_loyalty;
	public static JSpinner sp_democracy;
	public static JSpinner sp_propaganda;
	public static JSpinner sp_mutation;
	public static JSpinner sp_sel_error;
	public static JSpinner sp_iterations;
	public static JSpinner sp_speed;
	public static JSpinner sp_buffer;
	public static JSpinner sp_features;
	public static JSpinner sp_radious;
	public static JSpinner sp_rows;
	public static JSpinner sp_cols;
	public static JSpinner sp_traits;

	/**
	 * The packages that contains the available simulations
	 */
	private String[] packs = { "simulator.worlds" };

	/**
	 * File chooser to load and save parameter configurations
	 */
	private JFileChooser jfc_load = new JFileChooser(Controller.WORKSPACE_DIR + Controller.CONFIGURATIONS_DIR);
	private JFileChooser jfc_workspace = new JFileChooser(Controller.WORKSPACE_DIR);

	/**
	 * Save the workspace directory
	 */
	private JTextField tf_workspace_dir;

	/**
	 * This is the controller of the simulations
	 */
	public static ControllerSingle controller = CulturalSimulator.controller;

	/**
	 * Create the dialog of the simulation
	 * 
	 * @param owner
	 *            the owner for the modal
	 */
	public CulturalParameters(JFrame owner) {
		super(owner);

		setTitle("Simulation Parameters");
		setBounds(100, 100, 687, 380);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setToolTipText(
				"How often an agent confuses the selection of an agent that can be influnce by or not?");
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		jfc_workspace.setDialogTitle("Select a Results Folder");
		jfc_workspace.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc_workspace.setAcceptAllFileFilterUsed(false);

		{
			JPanel panel_1 = new JPanel();
			panel_1.setBorder(
					new TitledBorder(null, "Configuration", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel_1.setBounds(10, 11, 655, 243);
			contentPanel.add(panel_1);
			panel_1.setLayout(null);
			{
				JPanel panel_1_1 = new JPanel();
				panel_1_1.setBounds(308, 56, 176, 78);
				panel_1.add(panel_1_1);
				panel_1_1.setLayout(null);
				panel_1_1.setBorder(new TitledBorder(
						new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Noise", TitledBorder.LEADING,
								TitledBorder.TOP, null, new Color(0, 0, 0)),
						"", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
				{
					JLabel lblMutation = new JLabel("Mutation:");
					lblMutation.setToolTipText("How often a random change in a feature occurs?");
					lblMutation.setBounds(10, 23, 74, 14);
					panel_1_1.add(lblMutation);
				}
				{
					JLabel lblSelectionError = new JLabel("Selection Error:");
					lblSelectionError.setToolTipText(
							"How often do you want to save results, update graphs, check for Pause/Stop/Resume states?");
					lblSelectionError.setBounds(10, 48, 85, 14);
					panel_1_1.add(lblSelectionError);
				}
				{
					sp_mutation = new JSpinner();

					sp_mutation.setModel(new SpinnerNumberModel(new Float(0.00002), new Float(0), new Float(1),
							new Float(0.000001)));
					((JSpinner.NumberEditor) sp_mutation.getEditor()).getFormat().setMinimumFractionDigits(6);
					sp_mutation.setValue(0.000001f);

					sp_mutation.setToolTipText("How often a random change in a feature occurs?");
					sp_mutation.setBounds(94, 20, 70, 20);
					panel_1_1.add(sp_mutation);
				}
				{
					sp_sel_error = new JSpinner();
					sp_sel_error.setToolTipText(
							"How often an agent confuses the selection of an agent that can be influnce by or not?");
					sp_sel_error.setModel(new SpinnerNumberModel(new Float(0.00002), new Float(0), new Float(1),
							new Float(0.000001)));
					((JSpinner.NumberEditor) sp_sel_error.getEditor()).getFormat().setMinimumFractionDigits(6);
					sp_sel_error.setValue(0.000001f);
					sp_sel_error.setBounds(94, 45, 70, 20);
					panel_1_1.add(sp_sel_error);
				}
			}

			JPanel panel = new JPanel();
			panel.setBounds(173, 56, 125, 149);
			panel_1.add(panel);
			panel.setBorder(new TitledBorder(null, "World", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel.setLayout(null);
			{
				JLabel lblFeatures = new JLabel("Features:");
				lblFeatures.setToolTipText("Number of features of the culture");
				lblFeatures.setBounds(10, 98, 57, 14);
				panel.add(lblFeatures);
			}
			{
				JLabel lblTraits = new JLabel("Traits:");
				lblTraits.setToolTipText("Number of traits available per features ");
				lblTraits.setBounds(10, 123, 46, 14);
				panel.add(lblTraits);
			}
			{
				sp_features = new JSpinner();
				sp_features.setToolTipText("Number of features of the culture");
				sp_features.setModel(new SpinnerNumberModel(new Integer(6), null, null, new Integer(1)));
				sp_features.setBounds(64, 95, 53, 20);
				panel.add(sp_features);
			}
			{
				JLabel lblRows = new JLabel("Rows:");
				lblRows.setToolTipText("Number of rows of the World Grid");
				lblRows.setBounds(10, 23, 46, 14);
				panel.add(lblRows);
			}
			{
				JLabel lblColumns = new JLabel("Columns:");
				lblColumns.setToolTipText("Number of columns of the World Grid");
				lblColumns.setBounds(10, 48, 46, 14);
				panel.add(lblColumns);
			}
			{
				JLabel lblRadius = new JLabel("Radius:");
				lblRadius.setToolTipText("The radious of the neighbors that can reach each agent");
				lblRadius.setBounds(10, 73, 46, 14);
				panel.add(lblRadius);
			}
			{
				sp_radious = new JSpinner();
				sp_radious.setToolTipText("The radious of the neighbors that can reach each agent");
				sp_radious.setModel(new SpinnerNumberModel(new Integer(6), null, null, new Integer(1)));
				sp_radious.setBounds(64, 70, 53, 20);
				panel.add(sp_radious);
			}
			{
				sp_rows = new JSpinner();
				sp_rows.setToolTipText("Number of rows of the World Grid");
				sp_rows.setModel(new SpinnerNumberModel(new Integer(100), null, null, new Integer(1)));
				sp_rows.setBounds(64, 20, 53, 20);
				panel.add(sp_rows);
			}
			{
				sp_cols = new JSpinner();
				sp_cols.setToolTipText("Number of columns of the World Grid");
				sp_cols.setModel(new SpinnerNumberModel(new Integer(100), null, null, new Integer(1)));
				sp_cols.setBounds(64, 45, 53, 20);
				panel.add(sp_cols);
			}
			{
				sp_traits = new JSpinner();
				sp_traits.setToolTipText("Number of traits available per features ");
				sp_traits.setModel(new SpinnerNumberModel(new Integer(14), null, null, new Integer(1)));
				sp_traits.setBounds(64, 120, 53, 20);
				panel.add(sp_traits);
			}
			{
				JPanel panel_2 = new JPanel();
				panel_2.setBounds(10, 56, 150, 124);
				panel_1.add(panel_2);
				panel_2.setBorder(
						new TitledBorder(null, "Controls", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				panel_2.setLayout(null);
				{
					JLabel lblNewLabel = new JLabel("Iterations: ");
					lblNewLabel.setToolTipText("How many iterations would you let the simulation run for? ");
					lblNewLabel.setBounds(10, 46, 64, 14);
					panel_2.add(lblNewLabel);
				}
				{
					JLabel lblSpeed = new JLabel("Speed:");
					lblSpeed.setToolTipText(
							"How often do you want to save results, update graphs, check for Events or Pause/Stop/Resume states? The highes the value, the faster it executes, but it doesn't update the interface or store results as fast.");
					lblSpeed.setBounds(10, 71, 64, 14);
					panel_2.add(lblSpeed);
				}
				{
					JLabel lblBufferSize = new JLabel("Buffer Size:");
					lblBufferSize.setToolTipText(
							"Size of the buffer to send results to hard drive? Small values constantly send to hard drive (slow), high values avoid sending to hard drive often but you could lost all the data if the simulation crashes (Risky)");
					lblBufferSize.setBounds(10, 96, 64, 14);
					panel_2.add(lblBufferSize);
				}

				sp_iterations = new JSpinner();
				sp_iterations.setToolTipText("How many iterations would you let the simulation run for? ");
				sp_iterations.setModel(
						new SpinnerNumberModel(new Integer(1000000), new Integer(1000), null, new Integer(1000)));
				sp_iterations.setBounds(67, 43, 73, 20);
				panel_2.add(sp_iterations);

				sp_speed = new JSpinner();
				sp_speed.setToolTipText(
						"How often do you want to save results, update graphs, check for Events or Pause/Stop/Resume states? The highes the value, the faster it executes, but it doesn't update the interface or store results as fast.");
				sp_speed.setModel(new SpinnerNumberModel(new Integer(100), null, null, new Integer(1)));
				sp_speed.setBounds(84, 68, 56, 20);
				panel_2.add(sp_speed);

				sp_buffer = new JSpinner();
				sp_buffer.setToolTipText(
						"Size of the buffer to send results to hard drive? Small values constantly send to hard drive (slow), high values avoid sending to hard drive often but you could lost all the data if the simulation crashes (Risky)");
				sp_buffer.setModel(new SpinnerNumberModel(new Integer(512), null, null, new Integer(1)));
				sp_buffer.setBounds(84, 93, 56, 20);
				panel_2.add(sp_buffer);

				cb_random_initialization = new JCheckBox("Random Initialization");
				cb_random_initialization.setSelected(true);
				cb_random_initialization.setBounds(6, 16, 134, 23);
				panel_2.add(cb_random_initialization);
			}
			{
				JPanel panel_1_1 = new JPanel();
				panel_1_1.setBounds(494, 55, 150, 124);
				panel_1.add(panel_1_1);
				panel_1_1.setLayout(null);
				panel_1_1.setBorder(new TitledBorder(
						new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Controls4", TitledBorder.LEADING,
								TitledBorder.TOP, null, new Color(0, 0, 0)),
						"Institutions", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
				{
					JLabel lblInfluence = new JLabel("Influence:");
					lblInfluence.setToolTipText("Institutional influence over the agent");
					lblInfluence.setBounds(10, 23, 64, 14);
					panel_1_1.add(lblInfluence);
				}
				{
					JLabel lblLoyalty = new JLabel("Loyalty:");
					lblLoyalty.setToolTipText("Agent's loyalty towards the institution");
					lblLoyalty.setBounds(10, 48, 64, 14);
					panel_1_1.add(lblLoyalty);
				}
				{
					JLabel lblDemocracy = new JLabel("Democracy:");
					lblDemocracy.setToolTipText("How often a democratic process occurs?");
					lblDemocracy.setBounds(10, 73, 64, 14);
					panel_1_1.add(lblDemocracy);
				}
				{
					sp_influence = new JSpinner();
					sp_influence.setModel(
							new SpinnerNumberModel(new Float(0.85), new Float(0), new Float(1), new Float(0.05)));
					((JSpinner.NumberEditor) sp_influence.getEditor()).getFormat().setMinimumFractionDigits(3);
					sp_influence.setValue(0.85f);
					sp_influence.setToolTipText("Institutional influence over the agent");
					sp_influence.setBounds(84, 20, 54, 20);
					panel_1_1.add(sp_influence);
				}
				{
					sp_loyalty = new JSpinner();
					sp_loyalty.setModel(
							new SpinnerNumberModel(new Float(0.5), new Float(0), new Float(1), new Float(0.05)));
					((JSpinner.NumberEditor) sp_loyalty.getEditor()).getFormat().setMinimumFractionDigits(3);
					sp_loyalty.setValue(0.5f);
					sp_loyalty.setToolTipText("Agent's loyalty towards the institution");
					sp_loyalty.setBounds(84, 45, 54, 20);
					panel_1_1.add(sp_loyalty);
				}
				{
					sp_democracy = new JSpinner();
					sp_democracy.setModel(new SpinnerNumberModel(new Integer(10), null, null, new Integer(1)));
					sp_democracy.setToolTipText("How often a democratic process occurs?");
					sp_democracy.setBounds(84, 70, 54, 20);
					panel_1_1.add(sp_democracy);
				}
				{
					JLabel lblPropaganda = new JLabel("Propaganda:");
					lblPropaganda.setToolTipText("How often a propaganda process occurs?");
					lblPropaganda.setBounds(10, 98, 64, 14);
					panel_1_1.add(lblPropaganda);
				}
				{
					sp_propaganda = new JSpinner();
					sp_propaganda.setModel(new SpinnerNumberModel(new Integer(3), null, null, new Integer(1)));
					sp_propaganda.setToolTipText("How often a propaganda process occurs?");
					sp_propaganda.setBounds(84, 95, 54, 20);
					panel_1_1.add(sp_propaganda);
				}
			}
			{
				JLabel lblModel = new JLabel("Model:");
				lblModel.setBounds(17, 21, 46, 14);
				panel_1.add(lblModel);
			}

			classSelector = new JComboBox<String>();
			classSelector.setBounds(90, 18, 554, 20);
			panel_1.add(classSelector);

			try {
				classSelector.setModel(new DefaultComboBoxModel<String>(getClasses(packs)));
				{
					JButton btn_save = new JButton("Save");
					btn_save.setToolTipText("Save configuration to file");
					btn_save.setIcon(
							new ImageIcon(CulturalParameters.class.getResource("/simulator/img/document-save.png")));
					btn_save.setBounds(432, 208, 101, 23);
					panel_1.add(btn_save);
					{
						File conf_dir = new File(Controller.WORKSPACE_DIR + Controller.CONFIGURATIONS_DIR);
						if (!conf_dir.exists()) {
							conf_dir.mkdirs();
						}
						jfc_load.setCurrentDirectory(conf_dir);
						JButton btn_load = new JButton("Load");
						btn_load.setIcon(new ImageIcon(
								CulturalParameters.class.getResource("/simulator/img/document-open.png")));
						btn_load.setToolTipText("Load configuration from file");
						btn_load.setBounds(543, 208, 101, 23);
						panel_1.add(btn_load);
						{
							JLabel lblWorkspaceDirectory = new JLabel("Workspace:");
							lblWorkspaceDirectory.setBounds(10, 275, 104, 20);
							contentPanel.add(lblWorkspaceDirectory);
						}

						tf_workspace_dir = new JTextField();
						tf_workspace_dir.setEditable(false);
						tf_workspace_dir.setBounds(76, 275, 467, 20);
						contentPanel.add(tf_workspace_dir);
						tf_workspace_dir.setColumns(10);
						tf_workspace_dir.setText(jfc_workspace.getCurrentDirectory().getAbsolutePath() + "\\");

						JButton btnBrowse = new JButton("Browse");
						btnBrowse.setIcon(new ImageIcon(
								CulturalParameters.class.getResource("/simulator/img/document-open-folder.png")));
						btnBrowse.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								if (jfc_workspace.showOpenDialog(contentPanel) == JFileChooser.APPROVE_OPTION) {
									tf_workspace_dir.setText(jfc_workspace.getSelectedFile().getAbsolutePath() + "\\");
								}
							}
						});
						btnBrowse.setBounds(553, 274, 108, 23);
						contentPanel.add(btnBrowse);
						btn_load.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								File conf_dir = new File(Controller.WORKSPACE_DIR + Controller.CONFIGURATIONS_DIR);
								if (!conf_dir.exists()) {
									conf_dir.mkdirs();
									jfc_load.setCurrentDirectory(conf_dir);
								}
								if (jfc_load.showOpenDialog(contentPanel) == JFileChooser.APPROVE_OPTION) {
									if (CulturalSimulator.want_to_continue(jfc_load)) {

										String conf_file = jfc_load.getSelectedFile().getAbsolutePath();
										controller.load_parameters(conf_file);
									}

								}
							}
						});
					}
					btn_save.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							File conf_dir = new File(Controller.WORKSPACE_DIR + Controller.CONFIGURATIONS_DIR);
							if (!conf_dir.exists()) {
								conf_dir.mkdirs();
								jfc_load.setCurrentDirectory(conf_dir);
							}
							if (jfc_load.showSaveDialog(contentPanel) == JFileChooser.APPROVE_OPTION) {
								// This will overwrite the controller in the simulation. 
								if (CulturalSimulator.want_to_continue(jfc_load)) {
									String conf_file = jfc_load.getSelectedFile().getAbsolutePath();
									controller.load_parameters_from_interface();
									controller.save_simulation(conf_file);
								}
							}
						}
					});
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			} 
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (CulturalSimulator.want_to_continue(CulturalParameters.this)) {
							if (jfc_workspace.getSelectedFile() != null){
								Controller.WORKSPACE_DIR = jfc_workspace.getSelectedFile().getAbsolutePath() + "\\";
							}
							controller.load_parameters_from_interface();
							CulturalParameters.this.setVisible(false);
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						jfc_workspace.setCurrentDirectory(new File(Controller.WORKSPACE_DIR));
						tf_workspace_dir.setText(jfc_workspace.getCurrentDirectory().getAbsolutePath() + "\\");
						controller.restore_parameters_to_interface();
						CulturalParameters.this.setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	/**
	 * Scans all classes accessible from the context class loader which belong
	 * to the given package and subpackages.
	 *
	 * @param packageName
	 *            The base package
	 * @return The classes that the package contains
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	private static String[] getClasses(String[] packageNames) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		classes = new ArrayList<Class>();
		prettyNames = new ArrayList<String>();

		assert classLoader != null;

		for (String packageName : packageNames) {
			String path = packageName.replace('.', '/');
			Enumeration<URL> resources = classLoader.getResources(path);
			List<File> dirs = new ArrayList<File>();
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				dirs.add(new File(resource.getFile()));
			}

			for (File directory : dirs) {
				findClasses(directory, packageName);
			}
		}

		return prettyNames.toArray(new String[prettyNames.size()]);
	}

	/**
	 * Recursive method used to find all classes in a given directory and
	 * subdirectory.
	 *
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private static void findClasses(File directory, String packageName) throws ClassNotFoundException {

		if (directory.exists()) {
			File[] files = directory.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					assert !file.getName().contains(".");
					findClasses(file, packageName + "." + file.getName());
				} else if (file.getName().endsWith(".class")) {
					String name = file.getName().substring(0, file.getName().length() - 6);
					
					Class<?> sim = Class.forName(packageName + '.' + name);
					try {
						Simulation simulation = (Simulation) sim.newInstance();
						prettyNames.add(simulation.getModelDescription());
						classes.add(sim);
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
							
					
				}
			}
		}
	}

}
