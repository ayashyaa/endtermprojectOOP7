package models;

public class Pyramid extends Figure {
    private double baseLength;
    private double baseWidth;
    private double height;

    public Pyramid(double baseLength, double baseWidth, double height) {
        super("Pyramid");
        setBaseLength(baseLength);
        setBaseWidth(baseWidth);
        setHeight(height);
    }

    public double getBaseLength() {
        return baseLength;
    }

    public void setBaseLength(double baseLength) {
        this.baseLength = baseLength;
    }

    public double getBaseWidth() {
        return baseWidth;
    }

    public void setBaseWidth(double baseWidth) {
        this.baseWidth = baseWidth;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public double calculateArea() {
        double slantHeight = Math.sqrt((baseLength / 2) * (baseLength / 2) + height * height);
        double baseArea = baseLength * baseWidth;
        double lateralArea = baseLength * slantHeight + baseWidth * slantHeight;
        return baseArea + lateralArea;
    }

    @Override
    public double calculateVolume() {
        return (baseLength * baseWidth * height) / 3;
    }

    @Override
    public double calculatePerimeter() {
        return 2 * (baseLength + baseWidth);
    }
}
