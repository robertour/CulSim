package simulator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Graph that displays the progression of up to 3 response variables
 * 
 * @author Roberto Ulloa
 * @version 1.0, April 2016
 */
public class GraphPanel extends JPanel {
	private static final long serialVersionUID = -4160115280107877425L;

	/**
	 * Some general attributes to configure the graph display
	 */
	private int padding = 5;
	private int labelPadding = 12;
	private Color gridColor = new Color(200, 200, 200, 200);
	private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
	private int pointWidth = 0;
	private int numberYDivisions = 5;
	private int minScore = -1;
	private int maxScore = -1;

	/**
	 * The colors for the 3 response variables
	 */
	private Color lineColor = new Color(44, 102, 230, 180);
	private Color lineColor2 = new Color(230, 102, 44, 180);
	private Color lineColor3 = new Color(44, 230, 102, 180);

	/**
	 * The lists that keeps the values of the progression for the response
	 * variables
	 */
	public ArrayList<Double> scores;
	public ArrayList<Double> scores2;
	public ArrayList<Double> scores3;

	/**
	 * A test constructor that has the lists for the 3 response variables
	 * 
	 * @param scores
	 *            values for the 1st response variable
	 * @param scores2
	 *            values for the 2nd response variable
	 * @param scores3
	 *            values for the 3rd response variable
	 */
	public GraphPanel(ArrayList<Double> scores, ArrayList<Double> scores2, ArrayList<Double> scores3) {
		this.scores = scores;
		this.scores2 = scores2;
		this.scores3 = scores3;
	}

	/**
	 * The main constructor of the GraphPanel
	 */
	public GraphPanel() {
		this.scores = new ArrayList<Double>();
		this.scores2 = new ArrayList<Double>();
		this.scores3 = new ArrayList<Double>();
		this.setMaxScore(1);
		this.setMinScore(0);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (scores.size() - 1);
		double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxScore() - getMinScore());

		// draw white background
		g2.setColor(Color.WHITE);
		g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding,
				getHeight() - 2 * padding - labelPadding);
		g2.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.PLAIN, 8));

		// create hatch marks and grid lines for y axis.
		for (int i = 0; i < numberYDivisions + 1; i++) {
			int x0 = padding + labelPadding;
			int x1 = pointWidth + padding + labelPadding;
			int y0 = getHeight()
					- ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
			int y1 = y0;
			if (scores.size() > 0) {
				g2.setColor(gridColor);
				g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
				g2.setColor(Color.BLACK);
				String yLabel = ((int) ((getMinScore()
						+ (getMaxScore() - getMinScore()) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
				FontMetrics metrics = g2.getFontMetrics();
				int labelWidth = metrics.stringWidth(yLabel);
				g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
			}
			g2.drawLine(x0, y0, x1, y1);
		}

		// and for x axis
		if (scores.size() > 1) {
			Stroke oldStroke = g2.getStroke();
			int x0, x1, y0, y1;
			int bx0, bx1, by0, by1;
			bx1 = (int) (0 * xScale + padding + labelPadding);
			by1 = (int) ((getMaxScore() - scores.get(0)) * yScale + padding);

			int rx0, rx1 = 0, ry0, ry1 = 0;
			if (scores2.size() > 0) {
				rx1 = (int) (0 * xScale + padding + labelPadding);
				ry1 = (int) ((getMaxScore() - scores2.get(0)) * yScale + padding);
			}
			int gx0, gx1 = 0, gy0, gy1 = 0;
			if (scores3.size() > 0) {
				gx1 = (int) (0 * xScale + padding + labelPadding);
				gy1 = (int) ((getMaxScore() - scores3.get(0)) * yScale + padding);
			}

			for (int i = 0; i < scores.size(); i++) {

				x0 = i * (getWidth() - padding * 2 - labelPadding) / (scores.size() - 1) + padding + labelPadding;
				x1 = x0;
				y0 = getHeight() - padding - labelPadding;
				y1 = y0 - pointWidth;

				g2.setStroke(oldStroke);
				if ((i % ((int) ((scores.size() / 5.0)) + 1)) == 0) {
					g2.setColor(gridColor);
					g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
					g2.setColor(Color.BLACK);
					String xLabel = i + "";
					FontMetrics metrics = g2.getFontMetrics();
					int labelWidth = metrics.stringWidth(xLabel);
					g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
				}
				g2.drawLine(x0, y0, x1, y1);

				g2.setStroke(GRAPH_STROKE);

				g2.setColor(lineColor);
				bx0 = bx1;
				by0 = by1;
				bx1 = (int) (i * xScale + padding + labelPadding);
				by1 = (int) ((getMaxScore() - scores.get(i)) * yScale + padding);
				g2.drawLine(bx0, by0, bx1, by1);

				if (scores2.size() > 0) {
					g2.setColor(lineColor2);
					rx0 = rx1;
					ry0 = ry1;
					rx1 = (int) (i * xScale + padding + labelPadding);
					ry1 = (int) ((getMaxScore() - scores2.get(i)) * yScale + padding);
					g2.drawLine(rx0, ry0, rx1, ry1);
				}

				if (scores3.size() > 0) {
					g2.setColor(lineColor3);
					gx0 = gx1;
					gy0 = gy1;
					gx1 = (int) (i * xScale + padding + labelPadding);
					gy1 = (int) ((getMaxScore() - scores3.get(i)) * yScale + padding);
					g2.drawLine(gx0, gy0, gx1, gy1);
				}

			}
			g2.setStroke(oldStroke);
		}

		// create x and y axes
		g2.setColor(Color.BLACK);
		g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
		g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding,
				getHeight() - padding - labelPadding);

	}

	/**
	 * Returns the minimum score to be display in the graph
	 * 
	 * @return the minimum value of the graph
	 */
	private double getMinScore() {
		if (minScore > -1) {
			return minScore;
		}
		double minScore = Double.MAX_VALUE;
		for (Double score : scores) {
			minScore = Math.min(minScore, score);
		}
		return minScore;
	}

	/**
	 * Set the minimum score of the graph
	 * 
	 * @param minScore
	 *            the minimum score to be set
	 */
	public void setMinScore(int minScore) {
		this.minScore = minScore;
	}

	/**
	 * Returns the maximum score to be display in the graph
	 * 
	 * @return the maximum value of the graph
	 */
	private double getMaxScore() {
		if (maxScore > -1) {
			return maxScore;
		}
		double maxScore = Double.MIN_VALUE;
		for (Double score : scores) {
			maxScore = Math.max(maxScore, score);
		}
		return maxScore;
	}

	/**
	 * Set the maximum score of the graph
	 * 
	 * @param minScore
	 *            the minimum score to be set
	 */
	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}

	/**
	 * Update the interface
	 */
	public void update() {
		invalidate();
		this.repaint();
	}

	/**
	 * Clean the score lists
	 */
	public void clean() {
		scores = new ArrayList<Double>();
		scores2 = new ArrayList<Double>();
		scores3 = new ArrayList<Double>();
		update();
	}

	/**
	 * Method to test and display the interface with some random values
	 */
	private static void createAndShowGui() {
		ArrayList<Double> scores = new ArrayList<>();
		ArrayList<Double> scores2 = new ArrayList<>();
		ArrayList<Double> scores3 = new ArrayList<>();

		Random random = new Random();
		int maxDataPoints = 40;
		int maxScore = 10;
		for (int i = 0; i < maxDataPoints; i++) {
			scores.add((double) random.nextDouble() * maxScore);
			scores2.add((double) random.nextDouble() * maxScore);
			scores3.add((double) random.nextDouble() * maxScore);
			// scores.add((double) i);
		}
		GraphPanel mainPanel = new GraphPanel(scores, scores2, scores3);
		mainPanel.setPreferredSize(new Dimension(800, 600));
		JFrame frame = new JFrame("DrawGraph");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(mainPanel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/**
	 * A main method to test the graph with random values
	 * 
	 * @param args
	 *            no arguments accepted for this main method
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGui();
			}
		});
	}
}
