package picture.graphPanels;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GaussPanel extends JPanel {

    public static void createAndShow(List<Integer> list) {
         Map<Integer, Integer> numMap;

        //key - score, value - size
        numMap = new TreeMap<>();
        for (Integer num : list) {
            numMap.put(num, 0);
        }

        for (Integer num : list) {
            int size = numMap.get(num) + 1;
            numMap.replace(num, size);
        }

        XYSeries series = new XYSeries("num of pixel/height");
        for (Integer integer : numMap.keySet()) {
            series.add((double)integer, (double) numMap.get(integer));
        }

        XYDataset xyDataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory
                .createXYLineChart("gauss panel", "height", "num of pixels",
                        xyDataset,
                        PlotOrientation.VERTICAL,
                        true, true, true);
        JFrame frame = new JFrame("Gauss graph");
        frame.setVisible(true);
        // Помещаем график на фрейм
        frame.getContentPane()
                .add(new ChartPanel(chart));
        frame.setSize(1600,800);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
    }
}
