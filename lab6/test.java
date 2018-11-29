
public class test{

  public void test(){
  }

  public static void main(String[] args){
    Figure[] myFigures = new Figure[10];
    myFigures[0] = new Rectangle(5,6);
    myFigures[1] = new Square(10);
    myFigures[2] = new MutableSquare(19);
    myFigures[3] = new Ellipse(5,10);
    myFigures[4] = new Circle(45);
    myFigures[5] = new MutableCircle(25);
    System.out.println(myFigures[0].area());
  }
}
