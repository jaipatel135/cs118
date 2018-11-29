import java.awt.Color;

abstract class Figure {
    protected Color color = Color.blue;
    public void setColor(Color c) {color = c;}
    public Color getColor() {return color;}
    public abstract double area();
    public abstract String toString();
    //public abstract void drawShape();

}
