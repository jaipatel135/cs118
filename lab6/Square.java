class Square extends Rectangle {

    protected int side;
    public Square(int s){ super(s,s); side = s;}
    public double area() {return side * side;}
    public String toString() {return ("a square of side "+side);}
    public void drawShape(){
      for(int i=0;i <side;i++){
        for(int j=0;j <side;j++){
          System.out.print("+");
        }
        System.out.print("\n");
      }
    }
}
