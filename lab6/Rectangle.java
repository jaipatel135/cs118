class Rectangle extends Figure {

    protected int height;
    protected int width;

    public Rectangle(int h, int w) {
      height = h; width=w;
    }

    public double area() {return height * width;}
    public String toString() {return ("rectangle of height "+height);}

    public void drawShape(){
      for(int i=0;i <height;i++){
        for(int j=0;j <width;j++){
          System.out.print("+");
        }
        System.out.print("\n");
      }
    }
}
