
class Circle extends Ellipse {

    protected int radius;
    public Circle(int s){ super(s,s); radius = s;}
    public double area() {return Math.PI * Math.pow(radius, 2);}
    public String toString() {return ("a circle of radius "+ radius);}
    public void drawShape(){
      System.out.println("Feature not implemented.");
    }
}
