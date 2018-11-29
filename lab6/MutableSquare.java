class MutableSquare extends Square {
    public MutableSquare(int s){ super(s);}
    public void Resize (int x) {super.side = x;}
    public void drawShape(){
      for(int i=0;i <side;i++){
        for(int j=0;j <side;j++){
          System.out.print("+");
        }
        System.out.print("\n");
      }
    }
}
