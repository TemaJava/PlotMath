package calculation;

import picture.Pixel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Converter {
    double middleNum = 0;
    List<Integer> markedDotsList;
    public List<Integer> getMarkedDotsValuesArray() {
        return markedDotsList;
    }

    public double getMiddleNum() {
        return middleNum;
    }

    public Converter(Map<Integer, Pixel> map) {
        markedDotsList = new ArrayList<>();
        List<Pixel> pointsPixels = new ArrayList<>();
        for (Pixel p : map.values()) {
            if (p.getColor().getRGB() == Color.RED.getRGB()) {
                pointsPixels.add(p);
            }
        }
        pointsPixels.sort(Comparator.comparingInt(Pixel::getId));
        for (int i = 0; i < pointsPixels.size(); ) {
            System.out.println(pointsPixels.get(i).getId() + " " + pointsPixels.get(i).getX()
                    + " " + pointsPixels.get(i).getY() + " " + pointsPixels.get(i).getValue());
            markedDotsList.add(pointsPixels.get(i).getValue());
            i++;
        }

        //нахождение среднего значения по выделенным точкам
        double sum = 0;
        int size = markedDotsList.size();
        for (Integer num : markedDotsList) {
            sum += num;
        }
        middleNum = sum/size;
    }
}
