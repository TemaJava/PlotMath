package picture;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class PictureAxisDrawer {
    private int width = 800;
    private int heigth = 400;
    private int padding = 10;
    private int labelPadding = 10;
    private Color lineColor = new Color(44, 102, 230, 180);
    private Color pointColor = new Color(100, 100, 100, 180);
    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private int pointWidth = 4;
    private int numberYDivisions = 50;
    private int xAxisWidth;
    private int yAxisHeigth;

    public PictureAxisDrawer(int width, int heigth) {
        this.xAxisWidth = width;
        this.yAxisHeigth = heigth;
    }

    public BufferedImage drawImageXAxis() {
        BufferedImage xAxisImage;
        if (yAxisHeigth < 300) {
            xAxisImage = new BufferedImage(xAxisWidth + 20, yAxisHeigth/3, BufferedImage.TYPE_BYTE_GRAY);
        } else {
            xAxisImage = new BufferedImage(xAxisWidth + 20, yAxisHeigth/7, BufferedImage.TYPE_BYTE_GRAY);
        }

        Graphics g = xAxisImage.getGraphics();
        g.setColor ( Color.WHITE );
        g.fillRect ( 0, 0, xAxisImage.getWidth(), xAxisImage.getHeight());

        //отрисовка оси x
        g.setColor(Color.BLACK);
        g.drawLine(0, 10, xAxisWidth + 20, 10);

        //отрисовка цифр
        List<Integer> listOfNumsForAxis = new ArrayList<>();
        listOfNumsForAxis.add(0);
        for (int i = 40; i < xAxisWidth; i++) {
            if (i % 40 == 0) {
                listOfNumsForAxis.add(i);
            }
        }
        listOfNumsForAxis.add(xAxisWidth);
        FontMetrics metrics = g.getFontMetrics();
        for (Integer num : listOfNumsForAxis) {
            String xLabel = num + "";
            int labelWidth = metrics.stringWidth(xLabel);
            g.drawString(xLabel, num - labelWidth / 2 + 10, 10 + metrics.getHeight() );
            //отрисовка рисочек
            int x1 = num - labelWidth / 2 + 15;
            int y1 = 1;
            int x2 = num - labelWidth / 2 + 15;
            int y2 = metrics.getHeight() - 5;
            g.drawLine(x1, y1, x2, y2);
        }
        return xAxisImage;
    }

    public BufferedImage drawImageYAxis() {
        BufferedImage yAxisImage;
        if (yAxisHeigth < 300) {
            yAxisImage = new BufferedImage(xAxisWidth/3, yAxisHeigth + 20, BufferedImage.TYPE_BYTE_GRAY);
        } else {
            yAxisImage = new BufferedImage(xAxisWidth, yAxisHeigth + 20, BufferedImage.TYPE_BYTE_GRAY);
        }

        Graphics g = yAxisImage.getGraphics();
        g.setColor (Color.WHITE);
        g.fillRect (0, 0, yAxisImage.getWidth(), yAxisImage.getHeight());

        //отрисовка оси y
        g.setColor(Color.BLACK);
        g.drawLine(xAxisWidth/6 - 1, 0, xAxisWidth/6 - 1, yAxisHeigth + 20);

        //отрисовка цифр
        List<Integer> listOfNumsForAxis = new ArrayList<>();
        listOfNumsForAxis.add(0);
        for (int i = 20; i < yAxisHeigth; i++) {
            if (i % 20 == 0) {
                listOfNumsForAxis.add(i);
            }
        }
        listOfNumsForAxis.add(yAxisHeigth);
        FontMetrics metrics = g.getFontMetrics();
        for (Integer num : listOfNumsForAxis) {
            String xLabel = num + "";
            int labelWidth = metrics.stringWidth(xLabel);
            g.drawString(xLabel, xAxisWidth/6 - 35, num - labelWidth / 2 + 17 );
            //отрисовка рисочек
            int x1 = xAxisWidth/6 - 1;
            int y1 = num - labelWidth / 2 + 17;
            int x2 = xAxisWidth/6 - 10;
            int y2 = num - labelWidth / 2 + 17;
            g.drawLine(x1, y1, x2, y2);
        }
        return yAxisImage;
    }
}
