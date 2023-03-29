package picture;

import java.awt.*;
import java.util.Objects;

public class Pixel {
    int id;
    int x;
    int y;
    Color color;

    int value;

    public Pixel(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public Pixel(int id, int x, int y, Color color, int value) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.color = color;
        this.value = value;
    }

    public Pixel(int id, int x, int y, Color color) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public Pixel(){}

    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pixel pixel = (Pixel) o;
        return x == pixel.x && y == pixel.y && Objects.equals(color, pixel.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, color);
    }
}
