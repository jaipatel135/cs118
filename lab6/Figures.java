
public class Figures{
  public static void main(String[] args){
    String shape = (String)args[0];
    int parameterOne = Integer.parseInt(args[1]);
    int parameterTwo = 0;
    if(args.length == 3){
      parameterTwo = Integer.parseInt(args[2]);
    }
    if(shape.charAt(0) == 'r'){
      Rectangle r = new Rectangle(parameterOne,parameterTwo);
      System.out.println("Made a Rectangle");
    }else if(shape.charAt(0) == 's'){
      Square s = new Square(parameterOne);
      System.out.println("Made a square");
    }else if(shape.charAt(0) == 'm' && shape.charAt(1) == 's'){
      MutableSquare ms = new MutableSquare(parameterOne);
      System.out.println("Made a Mutable Square");
    }else if(shape.charAt(0) == 'e'){
      Ellipse e = new Ellipse(parameterOne,parameterTwo);
      System.out.println("Made a Ellipse");
    }else if(shape.charAt(0) == 'c'){
      Circle c = new Circle(parameterOne);
      System.out.println("Made a Circle");
    }else if(shape.charAt(0) == 'm' && shape.charAt(1) == 'c' ){
      MutableCircle mc = new MutableCircle(parameterOne);
      System.out.println("Made a Mutable Circle");
    }else{
      System.out.println("Unsupported shape type");
      System.out.println(shape);
    }
  }
}
