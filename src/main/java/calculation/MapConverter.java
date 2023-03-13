package calculation;

import java.util.HashMap;
import java.util.Map;

/*класс, позволяющий перевести полученный из textArea массив в удобный для расчётов
формат map, где key - номер пикселя, value - значение в ангстремах*/
public class MapConverter {
    public static Map<Integer, Double> toMap(String str) {
        Map<Integer, Double> map = new HashMap<>();
        String[] pairs = str.split("\n");
        for (String pair : pairs) {
            String[] keyValue = pair.split("\t");
            map.put(Integer.valueOf(keyValue[0]), Double.valueOf(keyValue[1]));
        }
        return map;
    }
}
