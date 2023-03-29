package picture;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

public class Picture {

    //регекс настраиваем в зависимости от разделителя в файле csv
    String regexInFile = ",";

    BufferedImage defaultPicture;
    BufferedImage currentPicture;
    double middleNum = 0;

    int colorScale = 30;
    PictureAxisDrawer pictureAxisDrawer;



    Map<Integer, Pixel> defaultPixelMap = new HashMap<>();
    Map<Integer, Pixel> currentPixelMap = new HashMap<>();
    public BufferedImage setPicture(File file) throws IOException {
        String csv = Files.readString(file.toPath());
        String[] lines = csv.trim().split("\n");
        int pictureHeight = lines.length;
        String[] line = lines[0].trim().split(regexInFile);
        int pictureWidth = line.length;
        chooseMiddleColor(lines);
        System.out.println(middleNum);
        int generatedId = 0;
        defaultPicture = new BufferedImage(pictureWidth,
                pictureHeight, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < lines.length; y++) {
            String lineFromLines = lines[y];
            String[] parts = lineFromLines.trim().split(regexInFile);

            for (int x = 0; x < parts.length; x++) {
                generatedId++;
                double num = Double.parseDouble(parts[x]);
                if (num > middleNum) {
                    defaultPicture.setRGB(x , y,  new Color((int) (128 + num/colorScale),
                            (int) (128 + num/colorScale), (int) (128 + num/colorScale) ).getRGB());

                    Pixel newPixel = new Pixel(generatedId, x, y,
                            new Color((int)(128 + num/colorScale), (int)(128 + num/colorScale), (int)(128 + num/colorScale)),
                            (int) num);
                    defaultPixelMap.put(generatedId, newPixel);
                } else {
                    defaultPicture.setRGB(x, y, new Color((int) (128 - num/colorScale),
                            (int) (128 - num/colorScale), (int) (128 - num/colorScale)).getRGB());

                    Pixel newPixel = new Pixel(generatedId, x, y,
                            new Color((int)(128 - num/colorScale), (int)(128 - num/colorScale), (int)(128 - num/colorScale)),
                            (int)num);
                    defaultPixelMap.put(generatedId, newPixel);
                }

            }
        }
        currentPixelMap.putAll(defaultPixelMap);

        pictureAxisDrawer = new PictureAxisDrawer(defaultPicture.getWidth(),
                defaultPicture.getHeight());
        return defaultPicture;


    }

    public BufferedImage recolorPixel(boolean firstPoint) {
        if (firstPoint) {
            ColorModel cm = defaultPicture.getColorModel();
            boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
            WritableRaster raster = defaultPicture.copyData(null);
            currentPicture = new BufferedImage(cm, raster, isAlphaPremultiplied, null);
            currentPixelMap.clear();
            currentPixelMap.putAll(defaultPixelMap);
        }
        return currentPicture;
    }

    private void chooseMiddleColor(String[] lines) {
        double amount = 0;
        int num = 0;
        for (String line : lines) {
            String[] parts = line.trim().split(regexInFile);
            for (String part : parts) {
                amount += Double.parseDouble(part);
                num += 1;
            }
        }
        middleNum = amount/num;
    }

    public String setColorScale(int scale) {
        colorScale = scale;
        return "Успешная установка скалирования";
    }

    public BufferedImage getDefaultPicture() {
        return defaultPicture;
    }

    public BufferedImage getCurrentPicture() {
        return currentPicture;
    }

    public Map<Integer, Pixel> getDefaultPixelMap() {
        return defaultPixelMap;
    }

    public Map<Integer, Pixel> getCurrentPixelMap() {
        return currentPixelMap;
    }

    public void setCurrentPicture(BufferedImage bufferedImage) {
        this.currentPicture = bufferedImage;
    }

    public void setData() {
        int generatedKey = 0;
        for (int y = 0; y < currentPicture.getHeight(); y++) {
            for (int x = 0; x < currentPicture.getWidth(); x++) {
                generatedKey++;
                int clr = currentPicture.getRGB(x, y);
                if (Color.red.getRGB() == clr) {
                    Pixel newPixel = new Pixel(generatedKey, x, y, Color.RED);
                    newPixel.setValue(currentPixelMap.get(generatedKey).getValue());
                    currentPixelMap.replace(generatedKey, newPixel);
                }
            }
        }
    }

    public void pressEventStart() {
        currentPicture = defaultPicture;
        currentPixelMap.clear();
        currentPixelMap.putAll(defaultPixelMap);
    }

    public String getRegexInFile() {
        return regexInFile;
    }

    public void setRegexInFile(String regex) {
        StringBuilder newRegex = new StringBuilder();
        if (regex.charAt(0) == '"') {
            int length = regex.length() - 2;
            newRegex.append(" ".repeat(Math.max(0, length)));
        }
        this.regexInFile = newRegex.toString();
    }

    public BufferedImage drawXAxisImage() {
        return pictureAxisDrawer.drawImageXAxis();
    }

    public BufferedImage drawYAxisImage() {
        return pictureAxisDrawer.drawImageYAxis();
    }
}
