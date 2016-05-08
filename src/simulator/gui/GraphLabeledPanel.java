package simulator.gui;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

public class GraphLabeledPanel extends JPanel {
	private static final long serialVersionUID = 5573328979469651718L;
	private JLabel lcounters;
	private GraphPanel graph;
	
	public GraphLabeledPanel(String title) {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel ltitle = new JLabel(title + ":");
		ltitle.setBorder(new EmptyBorder(0, 5, 0, 0));
		panel.add(ltitle, BorderLayout.WEST);
		
		lcounters = new JLabel(" ");
		lcounters.setBorder(new EmptyBorder(0, 0, 0, 5));
		panel.add(lcounters, BorderLayout.EAST);		
		
		graph = new GraphPanel();
		add(graph, BorderLayout.CENTER);
	}

	public void addScores(double s1, double s2, double s3){
		DecimalFormat df = new DecimalFormat(".00");
		DecimalFormat df2 = new DecimalFormat(".0");
		String counters = "";
		if (s1 >= 0){
			graph.scores.add(s1);
			if (s1 <1 ){
				counters += "<span color='blue'>"+df.format(s1)+"</span>";
			} else {
				counters += "<span color='blue'>"+df2.format(s1)+"</span>";
			}
			
		} 
		if (s2 >= 0){
			graph.scores2.add(s2);
			if (!counters.equals("")){
				counters += "/";
			}
			if (s2 <1 ){
				counters += "<span color='red'>"+df.format(s2)+"</span>";
			} else {
				counters += "<span color='red'>"+df2.format(s2)+"</span>";
			}
			
		} 
		if (s3 >= 0){
			graph.scores3.add(s3);
			if (!counters.equals("")){
				counters += "/";
			}
			if (s3 <1 ){
				counters += "<span color='green'>"+df.format(s3)+"</span>";
			} else {
				counters += "<span color='green'>"+df2.format(s3)+"</span>";
			}
		} 
		lcounters.setText("<html>"+counters+"</html>");
		graph.update();
	}
	
	public void setCounterLabels(String l1, String l2, String l3){
		String tooltip = "";
		if (!l1.equals("")){
			tooltip += "<span color='blue'>"+l1+"</span>";			
		}
		if (!l2.equals("")){
			if (!tooltip.equals("")){
				tooltip += " / ";
			}
			tooltip += "<span color='red'>"+l2+"</span>";	
		}
		if (!l3.equals("")){
			if (!tooltip.equals("")){
				tooltip += " / ";
			}
			tooltip += "<span color='green'>"+l3+"</span>";
		}
		lcounters.setToolTipText("<html>"+tooltip+"</html>");		
	}
	
	public void clean(){
		graph.clean();
	}
}
