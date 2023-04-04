package picture.graphPanels;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class GraphPanel extends JPanel {

    public static void createAndShow(List<Integer> list) {
        //средняя линия
        double sum = 0;
        for (Integer integer : list) {
            sum += integer;
        }
        double middleNum = sum/list.size();

        XYSeries series = new XYSeries("height/pixel");
        System.out.println(list.size());
        for (int i = 0; i < list.size(); i++) {
            series.add(i, list.get(i));
            System.out.println(list.get(i) + " , " + i);
        }

        XYDataset xyDataset = new XYSeriesCollection(series);

        JFreeChart chart = ChartFactory
                .createXYLineChart("Grapf panel", "pixel", "height",
                        xyDataset,
                        PlotOrientation.VERTICAL,
                        true, true, true);

        ValueMarker marker = new ValueMarker(middleNum);  // position is the value on the axis
        marker.setPaint(Color.YELLOW);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.addRangeMarker(marker);

        JFrame frame = new JFrame("Graph");
        frame.setVisible(true);
        // Помещаем график на фрейм
        frame.getContentPane()
                .add(new ChartPanel(chart));
        frame.setSize(1600,800);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
    }
}

