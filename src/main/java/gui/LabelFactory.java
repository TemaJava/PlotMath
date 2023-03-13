package gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/*данный класс позволяет сократить код использованием фабричного паттерна
при необходимости создать лейбл вызываем статичный метод create данного класса*/
public class LabelFactory {
    static Border solidBorder = BorderFactory.createLineBorder(Color.BLACK, 1);

    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setVerticalAlignment(JLabel.TOP);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBorder(solidBorder);
        label.setFont(new Font("Times New Roman", Font.PLAIN, 28));
        return label;
    }
}
