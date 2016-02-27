package simulator;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import simulator.control.events.Distribution;

import javax.swing.JRadioButton;
import javax.swing.UIManager;
import java.awt.Color;

import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

public class DistributionPanel extends JPanel {
	private static final long serialVersionUID = -1571052661129757863L;
	private JRadioButton rdbtnUniformDistribution;
	private JRadioButton rdbtnAproxNormalDistribution;
	private JSpinner sp_prob;
	private JSpinner sp_row;
	private JSpinner sp_col;
	private JSpinner sp_sd;
	private JRadioButton rdbtnNewmannonProbabilistic;
	private JLabel label;
	private JSpinner sp_row_newman;
	private JLabel label_1;
	private JSpinner sp_col_newman;
	private JLabel lblWithRadious;
	private JSpinner sp_radious;

	/**
	 * Create the panel.
	 */
	public DistributionPanel(Distribution d, String title) {
		setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), title, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		setLayout(null);
		
		rdbtnUniformDistribution = new JRadioButton("Uniform Distribution:");
		rdbtnUniformDistribution.setToolTipText("The event occur with Probability p to each agent or feature");
		rdbtnUniformDistribution.setBounds(6, 19, 157, 23);
		add(rdbtnUniformDistribution);
		
		rdbtnAproxNormalDistribution = new JRadioButton("Aprox. Normal Distribution:");
		rdbtnAproxNormalDistribution.setToolTipText("The event occurs with a normal probability distribution to each agent or feature. The normal distribution is center in a particular Row and Column with a given Standard Deviation. Use -1 in Row and/or Columns to indicate that the center of the normal curve can be picked randomly. This distribution is approximate because a grid is discrete, its range is finite, and currently the approximation follow a Newmann's Distance.");
		rdbtnAproxNormalDistribution.setBounds(6, 78, 157, 23);
		add(rdbtnAproxNormalDistribution);

		rdbtnNewmannonProbabilistic = new JRadioButton("Newmann (non probabilistic):");
		rdbtnNewmannonProbabilistic.setToolTipText("The event always occur to the agents in the neighborhood of the specified row and column. Use -1 to specify random selection of rows and columns.");
		rdbtnNewmannonProbabilistic.setBounds(6, 189, 169, 23);
		add(rdbtnNewmannonProbabilistic);
		
	
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnUniformDistribution);
		group.add(rdbtnAproxNormalDistribution);
		group.add(rdbtnNewmannonProbabilistic);
		
		if (d.getType() == Distribution.UNIFORM)
			rdbtnUniformDistribution.setSelected(true);
		else if (d.getType() == Distribution.NORMAL)
			rdbtnAproxNormalDistribution.setSelected(true);
		else if (d.getType() == Distribution.NEWMANN)
			rdbtnNewmannonProbabilistic.setSelected(true);
		
		JLabel lblNewLabel = new JLabel("Probability:");
		lblNewLabel.setToolTipText("The probability of the event occuring to any agent or feature given.");
		lblNewLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel.setBounds(16, 49, 99, 14);
		add(lblNewLabel);
		
		sp_prob = new JSpinner();
		sp_prob.setToolTipText("The probability of the event occuring to any agent or feature given.");
		sp_prob.setModel(new SpinnerNumberModel(new Double(d.getProbability()), new Double(0.0), new Double(1.0), new Double(0.1)));
		sp_prob.setBounds(123, 44, 52, 20);
		add(sp_prob);
		
		JLabel lblCenterAtRow = new JLabel("Center at Row:");
		lblCenterAtRow.setToolTipText("Row where the aproximate normal distribution is centered, given as a ration of the existent rows");
		lblCenterAtRow.setHorizontalAlignment(SwingConstants.TRAILING);
		lblCenterAtRow.setBounds(16, 111, 99, 14);
		add(lblCenterAtRow);
		
		sp_row = new JSpinner();
		sp_row.setToolTipText("Row where the aproximate normal distribution is centered, given as a ration of the existent rows");
		sp_row.setModel(new SpinnerNumberModel(new Double(d.getRow_ratio()), new Double(-1.0), new Double(1.0), new Double(0.1)));
		sp_row.setBounds(123, 108, 52, 20);
		add(sp_row);
		
		JLabel lblAndColumn = new JLabel("and Column:");
		lblAndColumn.setToolTipText("Column where the aproximate normal distribution is centered, given as a ratio of the existent columns");
		lblAndColumn.setHorizontalAlignment(SwingConstants.TRAILING);
		lblAndColumn.setBounds(26, 136, 88, 14);
		add(lblAndColumn);
		
		sp_col = new JSpinner();
		sp_col.setToolTipText("Column where the aproximate normal distribution is centered, given as a ratio of the existent columns");
		sp_col.setModel(new SpinnerNumberModel(new Double(d.getCol_ratio()), new Double(-1.0), new Double(1.0), new Double(0.1)));
		sp_col.setBounds(123, 133, 52, 20);
		add(sp_col);
		
		JLabel lblWithSd = new JLabel("with SD:");
		lblWithSd.setToolTipText("Standard Deviation of the distribution, given as a ratio of the existent Rows or Columns");
		lblWithSd.setHorizontalAlignment(SwingConstants.TRAILING);
		lblWithSd.setBounds(16, 161, 98, 14);
		add(lblWithSd);
		
		sp_sd = new JSpinner();
		sp_sd.setToolTipText("Standard Deviation of the distribution, given as a ratio of the existent Rows or Columns");
		sp_sd.setModel(new SpinnerNumberModel(new Double(d.getSd()), new Double(0.0), new Double(1.0), new Double(0.1)));
		sp_sd.setBounds(123, 158, 52, 20);
		add(sp_sd);
		
		label = new JLabel("Center at Row:");
		label.setToolTipText("Row where the aproximate normal distribution is centered, given as a ration of the existent rows");
		label.setHorizontalAlignment(SwingConstants.TRAILING);
		label.setBounds(16, 222, 99, 14);
		add(label);
		
		sp_row_newman = new JSpinner();
		sp_row_newman.setToolTipText("Row where the aproximate normal distribution is centered, given as a ratio of the existent rows");
		sp_row_newman.setModel(new SpinnerNumberModel(new Double(d.getRow_ratio()), new Double(-1.0), new Double(1.0), new Double(0.1)));
		sp_row_newman.setBounds(123, 219, 52, 20);
		add(sp_row_newman);
		
		label_1 = new JLabel("and Column:");
		label_1.setToolTipText("Column where the aproximate normal distribution is centered, given as a ratio of the existent columns");
		label_1.setHorizontalAlignment(SwingConstants.TRAILING);
		label_1.setBounds(26, 247, 88, 14);
		add(label_1);
		
		sp_col_newman = new JSpinner();
		sp_col_newman.setToolTipText("Column where the aproximate normal distribution is centered, given as a ratio of the existent columns");
		sp_col_newman.setModel(new SpinnerNumberModel(new Double(d.getCol_ratio()), new Double(-1.0), new Double(1.0), new Double(0.1)));
		sp_col_newman.setBounds(123, 244, 52, 20);
		add(sp_col_newman);
		
		lblWithRadious = new JLabel("with Radious:");
		lblWithRadious.setToolTipText("The radio (of the Newman's neighborhood) of the attack");
		lblWithRadious.setHorizontalAlignment(SwingConstants.TRAILING);
		lblWithRadious.setBounds(16, 272, 98, 14);
		add(lblWithRadious);
		
		sp_radious = new JSpinner();
		sp_radious.setModel(new SpinnerNumberModel(new Integer(d.getRadious()), null, null, new Integer(2)));
		sp_radious.setToolTipText("The radio (of the Newman's neighborhood) of the attack");
		
		sp_radious.setBounds(123, 269, 52, 20);
		add(sp_radious);
		
		
		
	}
	
	public Distribution get_distribution(){
		Distribution d = null;
		if (rdbtnUniformDistribution.isSelected()){
			d = new Distribution((double) sp_prob.getValue());
		}else if (rdbtnAproxNormalDistribution.isSelected()){
			d = new Distribution((double) sp_row.getValue(), (double) sp_col.getValue(), (double) sp_sd.getValue() );
		}else if (rdbtnNewmannonProbabilistic.isSelected()){
			d = new Distribution((double) sp_row_newman.getValue(), (double) sp_col_newman.getValue(), (int) sp_radious.getValue() );
		}
		return d;
	}
}

