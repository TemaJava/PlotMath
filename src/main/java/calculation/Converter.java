package calculation;

import picture.Pixel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Converter {
    public static List<Integer> getMarkedDotsValuesArray(Map<Integer, Pixel> map) {
        List<Integer> array = new ArrayList<>();
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
            array.add(pointsPixels.get(i).getValue());
            i++;
        }
        return array;
    }
}
