package simulator;

import java.awt.EventQueue;
import java.awt.Graphics2D;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTextArea;
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

public class CulturalSimulator extends JFrame {


	private static final long serialVersionUID = -6498782807057797553L;
	private JPanel contentPane;
	
	public static String EXPERIMENTAL_FILE = "";
	public static String RESULTS_DIR = "";
	
	private JFileChooser jfc_load = new JFileChooser("./worlds/");
	
	public static ControllerSingle controller = new ControllerSingle();
	public static JTextArea TA_OUTPUT;
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
	private JLabel lblCultures;
	public static GraphPanel graph_cultures;
	private JToolBar toolBar_1;
	private JLabel lblJjlkjasdKlasfjdlkjAsdkfjasdfkj;
	private JPanel panel_13;
	private JLabel lblBiggestCulture;
	private JPanel panel_14;
	private JPanel panel_15;
	private JLabel lblInstitutions;
	private JLabel lblEnergy;
	public static GraphPanel graph_institutions;
	public static GraphPanel graph_borderless_cultures;
	
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 899, 722);
		
		parameters_dialog =  new CulturalParameters(this);
		batch_mode_dialog = new BatchMode(this);
		
		controller.load_simulation("./simulation.parameters");
		
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
						controller.load_simulation(conf_file);
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
		
		mnSimulation = new JMenu("Simulation");
		menuBar.add(mnSimulation);
		
		JMenuItem mntmDefineParameters = new JMenuItem("Parameters");
		mntmDefineParameters.setAction(action);
		mnSimulation.add(mntmDefineParameters);
		mnSimulation.addSeparator();
		
		JMenuItem mntmCollapse = new JMenuItem("Collapse");
		mnSimulation.add(mntmCollapse);
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
		panel_11.setLayout(new GridLayout(4, 1, 0, 0));
		
		panel_12 = new JPanel();
		panel_11.add(panel_12);
		panel_12.setLayout(new BorderLayout(0, 0));
		
		lblCultures = new JLabel("Cultures");
		lblCultures.setHorizontalAlignment(SwingConstants.CENTER);
		panel_12.add(lblCultures, BorderLayout.NORTH);
		
		graph_cultures = new GraphPanel();
		panel_12.add(graph_cultures, BorderLayout.CENTER);
		
		panel_13 = new JPanel();
		panel_11.add(panel_13);
		panel_13.setLayout(new BorderLayout(0, 0));
		
		lblBiggestCulture = new JLabel("Institutions");
		lblBiggestCulture.setHorizontalAlignment(SwingConstants.CENTER);
		panel_13.add(lblBiggestCulture, BorderLayout.NORTH);
		
		graph_institutions = new GraphPanel();
		panel_13.add(graph_institutions, BorderLayout.CENTER);
		
		panel_14 = new JPanel();
		panel_11.add(panel_14);
		panel_14.setLayout(new BorderLayout(0, 0));
		
		lblInstitutions = new JLabel("Borderless Cultures");
		lblInstitutions.setHorizontalAlignment(SwingConstants.CENTER);
		panel_14.add(lblInstitutions, BorderLayout.NORTH);
		
		graph_borderless_cultures = new GraphPanel();
		panel_14.add(graph_borderless_cultures, BorderLayout.CENTER);
		
		panel_15 = new JPanel();
		panel_11.add(panel_15);
		panel_15.setLayout(new BorderLayout(0, 0));
		
		lblEnergy = new JLabel("Energy");
		lblEnergy.setHorizontalAlignment(SwingConstants.CENTER);
		panel_15.add(lblEnergy, BorderLayout.NORTH);
		
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
		
		TA_OUTPUT = new JTextArea();
		TA_OUTPUT.setEditable(false);
		scrollPane.setViewportView(TA_OUTPUT);
		splitPane.setDividerLocation(500);
		
		panel_7 = new JPanel();
		splitPane_1.setLeftComponent(panel_7);
		panel_7.setLayout(new BorderLayout(0, 0));
		
		panel_1 = new JPanel();
		panel_7.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		panel_8 = new JPanel();
		panel_8.setPreferredSize(new Dimension(10, 270));
		panel_8.setBorder(new TitledBorder(null, "Events", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.add(panel_8, BorderLayout.NORTH);
		panel_8.setLayout(null);
		
		panel_9 = new JPanel();
		panel_9.setBounds(10, 20, 171, 145);
		panel_9.setBorder(new TitledBorder(null, "Institutions", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_8.add(panel_9);
		panel_9.setLayout(null);
		
		btnCollapse = new JButton("Destroy Content");
		btnCollapse.setBounds(10, 20, 150, 23);
		panel_9.add(btnCollapse);
		
		btnDestroyInstitutionsStructure = new JButton("Destroy Structure");
		btnDestroyInstitutionsStructure.setBounds(10, 50, 150, 23);
		panel_9.add(btnDestroyInstitutionsStructure);
		
		btnConversion = new JButton("Convert");
		btnConversion.setBounds(10, 80, 101, 23);
		panel_9.add(btnConversion);
		
		btnTraitConversion = new JButton("Convert Traits");
		btnTraitConversion.setBounds(10, 110, 101, 23);
		panel_9.add(btnTraitConversion);
		
		sp_convert = new JSpinner();
		sp_convert.setModel(new SpinnerNumberModel(new Double(0.1), new Double(0.0), new Double(1.0), new Double(0.1)));
		sp_convert.setBounds(121, 81, 39, 20);
		panel_9.add(sp_convert);
		
		sp_convert_traits = new JSpinner();
		sp_convert_traits.setModel(new SpinnerNumberModel(new Double(0.1), new Double(0.0), new Double(1.0), new Double(0.1)));
		sp_convert_traits.setBounds(121, 110, 39, 20);
		panel_9.add(sp_convert_traits);
		
		panel_10 = new JPanel();
		panel_10.setBounds(10, 167, 171, 92);
		panel_10.setBorder(new TitledBorder(null, "Population", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_8.add(panel_10);
		panel_10.setLayout(null);
		
		btnInvasion = new JButton("Invasion");
		btnInvasion.setBounds(10, 20, 101, 23);
		panel_10.add(btnInvasion);
		
		btnGenocide = new JButton("Genocide");
		btnGenocide.setBounds(10, 50, 101, 23);
		panel_10.add(btnGenocide);
		
		sp_invasion = new JSpinner();
		sp_invasion.setModel(new SpinnerNumberModel(new Integer(6), 1, null, new Integer(1)));
		sp_invasion.setBounds(121, 20, 39, 20);
		panel_10.add(sp_invasion);
		
		sp_genocide = new JSpinner();
		sp_genocide.setModel(new SpinnerNumberModel(new Double(0.1), new Double(0.0), new Double(1.0), new Double(0.1)));
		sp_genocide.setBounds(121, 49, 39, 20);
		panel_10.add(sp_genocide);
		
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
		btnGenocide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.genocide((double) sp_genocide.getValue());
			}
		});
		btnInvasion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.invasion((int) sp_invasion.getValue());
			}
		});
		btnTraitConversion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.institutional_trait_conversion((double) sp_convert_traits.getValue());
			}
		});
		btnConversion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.institutional_conversion((double) sp_convert.getValue());
			}
		});
		btnDestroyInstitutionsStructure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.destroy_institutions_structure();
			}
		});
		btnCollapse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.destroy_institutions_content();
			}
		});
		splitPane_1.setDividerLocation(190);
		DefaultCaret caret = (DefaultCaret)TA_OUTPUT.getCaret();
		splitPane_2.setDividerLocation(700);
		
		toolBar_1 = new JToolBar();
		toolBar_1.setFloatable(false);
		contentPane.add(toolBar_1, BorderLayout.SOUTH);
		
		lblJjlkjasdKlasfjdlkjAsdkfjasdfkj = new JLabel("jj;lkjasd klasfjdlkj asd;kfjasdfkj ;klasjd");
		toolBar_1.add(lblJjlkjasdKlasfjdlkjAsdkfjasdfkj);
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
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
		graph_cultures.scores = new ArrayList<Double>();
		graph_cultures.scores2 = new ArrayList<Double>();
		graph_cultures.update();
		graph_institutions.scores = new ArrayList<Double>();
		graph_institutions.scores2 = new ArrayList<Double>();
		graph_institutions.update();
		graph_borderless_cultures.scores = new ArrayList<Double>();
		graph_borderless_cultures.scores2 = new ArrayList<Double>();
		graph_borderless_cultures.update();
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
			parameters_dialog.setVisible(true);
		}
	}
	
	private class PlayAL extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent arg0) {
			
			if (tglbtnPlay.isEnabled() && mntmPlay.isEnabled()){
				
				if (tglbtnPause.isSelected() || tglbtnStop.isSelected()){
				
					if (!tglbtnPause.isSelected() && tglbtnStop.isSelected()){
							
						File dir = new File(parameters_dialog.tf_results_dir.getText() + "/results/");
						
						for( int i = 0; dir.exists(); i++) {
							dir = new File(parameters_dialog.tf_results_dir.getText() + "/results" + i + "/");	
						}
			
						RESULTS_DIR = dir.getAbsolutePath() + "/";
						(new File(RESULTS_DIR + "/iterations/")).mkdirs();

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
					
					mntmSafeWorldState.setEnabled(false);
					mntmLoadWorldState.setEnabled(false);
					mnSimulation.setEnabled(false);
					

//					btnCollapse.setEnabled(false);
//					btnInvasion.setEnabled(false);
//					btnGenocide.setEnabled(false);
					
					
				} else {
					TA_OUTPUT.append("Warning: GUI in an unstable state");
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
		}
	}
}

