package calculation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GaussPanel extends JPanel {
    private List<Integer> scores;
    private int width = 800;
    private int heigth = 400;
    private int padding = 25;
    private int labelPadding = 25;
    private Color lineColor = new Color(44, 102, 230, 180);
    private Color pointColor = new Color(100, 100, 100, 180);
    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private int pointWidth = 4;
    private int numberYDivisions = 50;

    public GaussPanel(List<Integer> scores) {
        this.scores = scores;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (scores.size() - 1);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxScore() - getMinScore());

        List<Point> graphPoints = new ArrayList<>();
    }

    private double getMinScore() {
        double minScore = Double.MAX_VALUE;
        for (Integer score : scores) {
            minScore = Math.min(minScore, score);
        }
        return minScore;
    }

    private double getMaxScore() {
        double maxScore = Double.MIN_VALUE;
        for (Integer score : scores) {
            maxScore = Math.max(maxScore, score);
        }
        return maxScore;
    }

    public static void createAndShow(List<Integer> list) {
        GaussPanel gaussPanel = new GaussPanel(list);

        JFrame jframe = new JFrame("Gauss graph");
        jframe.setVisible(true);
        jframe.setPreferredSize(new Dimension(1600, 800));
        jframe.getContentPane().add(gaussPanel);
        jframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jframe.setLocationByPlatform(true);
        jframe.pack();
    }
}
