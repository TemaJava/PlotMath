package calculation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static java.lang.Math.abs;

public class RzCalculator {

    public static double calculate(List<Integer> list) {
        MiddleLine middleLineObj = new MiddleLine(list);
        int middleLine = middleLineObj.getMiddleLine();
        List<Integer> updatedList = new ArrayList<>(list);
        updatedList.replaceAll(a -> Math.abs(a - middleLine));
        Collections.sort(updatedList);
        //расчет сумм первых и последних 5 по величине значений. Для наименьших
        //берем первые 5 показаний из отсортированного Array, для наибольших - последние
        double least = abs(updatedList.get(0)) + abs(updatedList.get(1)) + abs(updatedList.get(2)) +
                abs(updatedList.get(3)) + abs(updatedList.get(4));
        double largest = abs(updatedList.get(updatedList.size() - 1)) + abs(updatedList.get(updatedList.size() - 2))
                + abs(updatedList.get(updatedList.size() - 3))
                + abs(updatedList.get(updatedList.size() - 4)) + abs(updatedList.get(updatedList.size() - 5));
        return (least + largest) / 5 / 10000; //перевод из ангстрем
    }
}

