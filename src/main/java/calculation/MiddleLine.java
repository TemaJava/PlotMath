package calculation;

import picture.Pixel;

import java.util.List;
import java.util.Map;

//класс, рассчитывающий и возвращающий значение средней линии графика в ангстремах
public class MiddleLine {
    int middleLine;

    public MiddleLine(List<Integer> list) {
        int sum = 0;
        for (int pixel : list) {
            sum+=pixel;
        }
        this.middleLine = sum/list.size();
    }

    public int getMiddleLine() {
        return middleLine;
    }
}
