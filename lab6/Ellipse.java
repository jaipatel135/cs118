class Ellipse extends Figure {

    protected int axis1;
    protected int axis2;

    public Ellipse(int h, int w) {
	axis1 = h; axis2 = w;}

    public double area() {return Math.PI * axis1 * axis2;}
    public String toString() {return ("ellipse of axis "+ axis1 + " and "+ axis2);}

    public void drawShape(){
      System.out.println("Feature not implemented.");
    }
}
