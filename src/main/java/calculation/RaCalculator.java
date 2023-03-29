package calculation;

import picture.Pixel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static java.lang.Math.abs;

public class RaCalculator {

    public static double calculate(List<Integer> defaultList) {
        MiddleLine middleLineObj = new MiddleLine(defaultList);
        int middleLine = middleLineObj.getMiddleLine();
        List<Integer> updatedList = new ArrayList<>(defaultList);
        updatedList.replaceAll(a -> Math.abs(a - middleLine));
        //перебором находим сумму все значений в Array
        double sum = 0;
        for (int num : updatedList) {
            sum += num;
        }
        return  sum / updatedList.size() / 10000; //перевод из ангстрем
    }
}
