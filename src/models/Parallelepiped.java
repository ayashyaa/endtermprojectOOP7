package models;

public class Parallelepiped extends Figure {
    private double length;
    private double width;
    private double height;

    public Parallelepiped(double length, double width, double height) {
        super("Parallelepiped");
        setLength(length);
        setWidth(width);
        setHeight(height);
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public double calculateArea() {
        return 2 * (length * width + length * height + width * height);
    }

    @Override
    public double calculateVolume() {
        return length * width * height;
    }

    @Override
    public double calculatePerimeter() {
        return 4 * (length + width + height);
    }
}
