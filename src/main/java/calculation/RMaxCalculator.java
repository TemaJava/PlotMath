package calculation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static java.lang.Math.abs;

public class RMaxCalculator {

    public static double calculate(List<Integer> defaultList) {
        MiddleLine middleLineObj = new MiddleLine(defaultList);
        int middleLine = middleLineObj.getMiddleLine();
        List<Integer> updatedList = new ArrayList<>(defaultList);
        updatedList.replaceAll(a -> Math.abs(a - middleLine));
        //перебором находим максимальное значение
        double rMax = 0;
        for (Integer num : updatedList) {
            if (abs(num) > rMax) {
                rMax = abs(num);
            }
        }
        return rMax / 10000; //перевод из ангстрем
    }
}
