import java.util.Scanner;

public class Figures2{
  public static void main(String[] args){
    String shape;
    int parameterOne;
    int parameterTwo;
    if(args[0].charAt(0) == 'i'){
      Scanner sc = new Scanner(System.in);
      System.out.println("What type of shape do you want to make?");
      System.out.println("Supported shapes are:");
      System.out.print("r - Rectangle \ns - Square\nms - Mutable Square\ne - Ellipse\nc - Circle\nmc - Mutable Circle\n");
      shape = sc.next();
      int params = 1;
      if(shape.charAt(0) == 'r' || shape.charAt(0) == 'e'){
        params = 2;
      }
      System.out.println("You have selected " + shape);
      System.out.println("This requires " + params + " parameters.");
      System.out.println("Input one number and hit enter to confirm.");
      parameterOne = sc.nextInt();
      if(params == 2){
        System.out.println("Now input your next number");
        System.out.println("");
        parameterTwo = sc.nextInt();
      }else{
        parameterTwo = 0;
      }
    }else{
      shape = (String)args[0];
      parameterOne = Integer.parseInt(args[1]);
      if(args.length >= 3){
        parameterTwo = Integer.parseInt(args[2]);
      }else{parameterTwo = 0;}
    }
    if(shape.charAt(0) == 'r'){
      Rectangle r = new Rectangle(parameterOne,parameterTwo);
      System.out.println("Made a Rectangle");
      r.drawShape();
    }else if(shape.charAt(0) == 's'){
      Square s = new Square(parameterOne);
      System.out.println("Made a square");
      s.drawShape();
    }else if(shape.charAt(0) == 'm' && shape.charAt(1) == 's'){
      MutableSquare ms = new MutableSquare(parameterOne);
      System.out.println("Made a Mutable Square");
      ms.drawShape();
    }else if(shape.charAt(0) == 'e'){
      Ellipse e = new Ellipse(parameterOne,parameterTwo);
      System.out.println("Made a Ellipse");
      e.drawShape();
    }else if(shape.charAt(0) == 'c'){
      Circle c = new Circle(parameterOne);
      System.out.println("Made a Circle");
      c.drawShape();
    }else if(shape.charAt(0) == 'm' && shape.charAt(1) == 'c' ){
      MutableCircle mc = new MutableCircle(parameterOne);
      System.out.println("Made a Mutable Circle");
      mc.drawShape();
    }else{
      System.out.println("Unsupported shape type");
      System.out.println(shape);
    }
  }
}
