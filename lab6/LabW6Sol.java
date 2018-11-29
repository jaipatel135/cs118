public class LabW6Sol {

    public static void main(String[] args) {

	/* Notice the slight trickery here: because we have a method defined
           in MutableSquare but not in Figure, we allow the same object to have
           two names with different types, so we can see an individual object as
	   a member of a class or a subclass as needed. */
	MutableSquare f1 =   new MutableSquare(45);
	Figure f2 = f1;
	System.out.println(f2.toString());
	/* Another way to print, allowing formatting of values */
	System.out.format("and area %.2f.\n", f2.area());
	f1.Resize(25);
	System.out.println(f2.toString());
	/* Preview of how we can use command line arguments */
	if (args.length > 0) {
	    System.out.println(args[0]);}
    }
}
