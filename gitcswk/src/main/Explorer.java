import uk.ac.warwick.dcs.maze.logic.*;
import java.awt.Point;

/*
1000 NORTH
1001 EAST
1002 SOUTH
1003 WEST

2000 AHEAD
2001 RIGHT
2002 BEHIND
2003 LEFT

3000 WALL
3001 PASSAGE
4000 BEENBEFORE

numeric directions for arithmetic on them.
*/

public class Explorer implements IRobotController {
  // the robot in the maze
  private IRobot robot;
  // a flag to indicate whether we are looking for a path
  private boolean active = false;
  // a value (in ms) indicating how long we should wait
  // between moves
  private int delay;
  //RobotData object for tracking junctions.
  private RobotData robotData;
  //1 = explore, 0 = backtrack
  private int explorerMode;

  // this method is called when the "start" button is clicked
  // in the user interface
  public void start() {
    //onetime setup
    this.active = true;
    int direction;
    //the explorerMode is set once at the start of every run instead
    //of in the reset function.
    explorerMode = 1;
    //if this is the robots first run then a new RobotData object is
    //created to store data on the maze.
    if(this.robot.getRuns() == 0){
      this.robotData = new RobotData(robot);
    }
    move(randomNonWall());

    //run loop for moving
    while(!robot.getLocation().equals(robot.getTargetLocation()) && active) {
      int exits = this.nonwallExits();
      if(passageExits() >=1){
        explorerMode = 1;
      }else{
        explorerMode = 0;
      }
      if(explorerMode == 1){
        System.out.println("Exploring");
        robot.sleep(delay);
        exploreControl(exits);
      }else{
        System.out.println("backtracking");
        robot.sleep(delay);
        backtrackControl(exits);
      }
      // wait for a while if we are supposed to
      if (delay > 0)
      robot.sleep(1500);
    }
    //prints all junctions it knows about
    printAllData();
  }

/*
  public void start(){
    this.active = true;
    int direction;
    //the explorerMode is set once at the start of every run instead
    //of in the reset function.
    explorerMode = 1;
    //if this is the robots first run then a new RobotData object is
    //created to store data on the maze.
    if(this.robot.getRuns() == 0){
      this.robotData = new RobotData(robot);
    }

    while(!robot.getLocation().equals(robot.getTargetLocation()) && active) {
      for(int i=0;i<4;i++){
        faceHeading(IRobot.NORTH+i);
        robot.sleep(delay);
      }
      if (delay > 0)
      robot.sleep(delay);
    }
  }
*/
  //for exploring new tiles in the maze.
  public void exploreControl(int exits){
    if(exits == 2){
      move(corridor());
    }else if(exits == 3){
      testPosition();
      move(junction());
    }else if(exits == 4){
      testPosition();
      move(crossroads());
    }
  }

  private void move(int direction){
    robot.face(direction);
    robot.advance();
  }

  //for traversing explored tiles in a maze until we reach unexplored tiles.
  public void backtrackControl(int exits){
    if(exits > 2){
      int dir = robotData.searchJunction(robot.getLocation().x,robot.getLocation().y);
      if(dir >=1002){
        dir = dir-2;
      }else{
        dir = dir+2;
      }
    }
    move(corridor());
  }

  // this method causes the robot to face to the absolute
  // direction that is specified as argument and returns
  // what sort of square there is
  public void faceHeading(int absoluteDirection) {
      //get the current absolute heading of the robot
      int previousHeading = robot.getHeading();
      //checks if absolute direction is a valid value
      if(absoluteDirection >= 1000 && absoluteDirection <=1003){
          if(previousHeading < absoluteDirection){
            /*returns the object that is in the absolute position given
            if the absoluteDirection value is larger than the previousHeading value*/
            int diff = absoluteDirection - previousHeading;
            robot.face(IRobot.AHEAD+diff);
          }else if(previousHeading > absoluteDirection){
            /*returns the object that is in the absolute position given
            if the absoluteDirection is smaller than the previousHeading value*/
            int diff = previousHeading - absoluteDirection;
            robot.face(2004-diff);
          }else{
            //if the values match then the robot can look ahead
            robot.face(robot.AHEAD);
          }
      }else{
        //if the value of absoluteDirection is not a NORTH EAST SOUTH WEST value then return 0
      }
  }


  // returns a number indicating how many non-wall exits there
  // are surrounding the robot's current position
  public int nonwallExits() {
    int direction = IRobot.AHEAD;
    int exits = 0;
    for(int i=0;i<4;i++){
      if(robot.look(direction) != IRobot.WALL){exits++;}
      direction++;
    }
    return exits;
  }

  public int passageExits(){
    int direction = IRobot.AHEAD;
    int exits = 0;
    for(int i=0;i<4;i++){
      if(robot.look(direction) == IRobot.PASSAGE){exits++;}
      direction++;
    }
    return exits;
  }

  public int beenBeforeExits(){
    int direction = IRobot.AHEAD;
    int exits = 0;
    for(int i=0;i<4;i++){
      if(robot.look(direction) == IRobot.BEENBEFORE){exits++;}
      direction++;
    }
    return exits;
  }

  private void testPosition(){
    if(beenBeforeExits() == 1){
      robotData.addJunction();
    }
  }

  private void printAllData(){
    robotData.printAllJunctions();
  }

  //returns an random direction with minimal bias.
  public int randomDirection(){
    int direction;
    int randno = (int) Math.round(Math.random()*4);
    if (randno == 1){
        direction = IRobot.LEFT;
    }else if (randno == 2){
        direction = IRobot.RIGHT;
    }else if (randno == 3){
        direction = IRobot.BEHIND;
    }else{
        direction = IRobot.AHEAD;
    }
    return direction;
  }

  //returns a random direction that will not be a wall.
  public int randomNonWall(){
    int direction = randomDirection();
    while(robot.look(direction) == IRobot.WALL){
      direction = randomDirection();
    }
    return direction;
  }

  //returns a random direction out of 2 possible directions.
  public int randomDirection(int directionOne, int directionTwo){
    if (Math.random()<=0.4999f){
      return directionOne;
    }else{
      return directionTwo;
    }
  }

  //returns a random direction out of 3 possible directions.
  public int randomDirection(int directionOne, int directionTwo, int directionThree){
    int randno = (int) Math.round(Math.random()*3);
    if(randno == 1){
      return directionOne;
    }else if(randno == 2){
      return directionTwo;
    }else{
      return directionThree;
    }
  }

  //returns the valid direction when the robot hits a dead end.
  public int deadEnd(){
    if(robot.look(IRobot.BEHIND) != IRobot.WALL){
      return IRobot.BEHIND;
    }else{
      return randomNonWall();
    }
  }

  //returns a valid direction when a corridor is encountered.
  public int corridor(){
    int direction = IRobot.AHEAD;
    for(int i=0;i<4;i++){
      if(robot.look(direction) != IRobot.WALL){
        if(direction != IRobot.BEHIND){
          return direction;
        }
      }
      direction++;
    }
    return 0;
  }

  //returns a valid direction when a junction is encountered.
  public int junction(){
    int direction = IRobot.AHEAD;
    int lastDirection = 1;
    int pE = passageExits();
    for(int i=0;i<4;i++){
      int dir = robot.look(direction);
      if(dir != IRobot.WALL){
        if(pE == 1){
          if(dir == IRobot.PASSAGE){
            return direction;
          }
        }else if(pE == 2){
          if(dir == IRobot.PASSAGE){
            if(lastDirection == 1){
              lastDirection = direction;
            }else{
              return randomDirection(direction,lastDirection);
            }
          }
        }else{
          return randomNonWall();
        }
      }
      direction++;
    }
    return 0;
  }

  //returns a valid direction when a crossroads is encountered.
  public int crossroads(){
    int direction = IRobot.AHEAD;
    int firstDirection = 1;
    int lastDirection = 1;
    int pE = passageExits();
    for(int i=0;i<4;i++){
      int dir = robot.look(direction);
      if(pE == 1){
        if(dir == IRobot.PASSAGE){
          return direction;
        }
      }else if(pE == 2){
        if(dir == IRobot.PASSAGE){
          if(lastDirection == 1){
            lastDirection = direction;
          }else{
            return randomDirection(direction,lastDirection);
          }
        }
      }else if(pE == 3){
        if(dir == IRobot.PASSAGE){
          if(firstDirection == 1){
            firstDirection = direction;
          }
          if(firstDirection != direction){
            if(lastDirection == 1){
              lastDirection = direction;
            }else{
              return randomDirection(firstDirection,lastDirection,direction);
            }
          }
        }
      }else{
        return randomDirection();
      }
      direction++;
    }
    return 0;
  }

  // this method returns a description of this controller.
  public String getDescription() {
    return "A controller which explores the maze in a structured way";
  }

  // sets the delay
  public void setDelay(int millis) {
    delay = millis;
  }

  // gets the current delay
  public int getDelay() {
    return delay;
  }

  // stops the controller
  public void reset() {
    active = false;
    this.robotData.resetJunctionCounter();
  }

  // sets the reference to the robot
  public void setRobot(IRobot robot) {
    this.robot = robot;
  }

}

class RobotData{
  public static int maxJunctions = 10000;
  private static int junctionCounter;
  private DynamicDataArray tiles;
  private IRobot robot;

  public RobotData(IRobot robot){
    resetJunctionCounter();
    tiles = new DynamicDataArray(1);
    this.robot = robot;
  }

  public void addJunction(){
    JunctionData thisTile = new JunctionData(robot);
    tiles.add(thisTile);
    junctionCounter++;
  }

  //takes robots x,y and returns robots heading when it first encountered a
  //junction.
  public int searchJunction(int x, int y){
    for(int i=0;i<junctionCounter;i++){
      JunctionData tile = tiles.get(i);
      if(tile.getX() == x && tile.getY() == y){
        return tile.getArrivedDirection();
      }
    }
    tiles.add(new JunctionData(robot));
    return tiles.get(junctionCounter).getArrivedDirection();
  }

  public DynamicDataArray getData(){
    return tiles;
  }

  public void resetJunctionCounter(){
    junctionCounter = 0;
  }

  public JunctionData pop(){
    return tiles.pop();
  }

  public void printJunction(int index){
    JunctionData t = tiles.get(index);
    int x = t.getX();
    int y = t.getY();
    int arrivedDirection = t.getArrivedDirection();
    String dir;
    if(arrivedDirection == 1000){
      dir = "NORTH";
    }else if(arrivedDirection == 1001){
      dir = "EAST";
    }else if(arrivedDirection == 1002){
      dir = "SOUTH";
    }else if(arrivedDirection == 1003){
      dir = "WEST";
    }else{
      dir = "invalid";
    }
    System.out.println("Junction " + index + "(x=" + x + ",y=" + y + ") heading " + dir);
  }

  public void printAllJunctions(){
    for(int i=0;i<junctionCounter;i++){
      printJunction(i);
    }
  }
}

class DynamicDataArray{
  private JunctionData[] array;
  private JunctionData[] newArray;
  private int capacity;
  private int nextFree;

  public DynamicDataArray(int initialCapacity){
    array = new JunctionData[initialCapacity];
    capacity = initialCapacity;
    nextFree = 0;
  }

  public int getSize(){
    int size=0;
    for(int i=0;i<capacity;i++){
      if(array[i] != null){
        size++;
      }
    }
    return size;
  }

  public int getCapacity(){
    return this.capacity;
  }

  private void resize() {
    newArray = new JunctionData[capacity+1];
    for(int i=0;i<capacity;i++){
      newArray[i] = array[i];
    }
    array = newArray;
    capacity= capacity+1;
  }

  public void add(JunctionData JunctionData){
    if(nextFree >= capacity-1){
      this.resize();
    }
    array[nextFree] = JunctionData;
    nextFree++;
  }

  public JunctionData get(int index){
    return array[index];
  }

  public JunctionData pop(){
    nextFree--;
    return array[nextFree];
  }
}

class JunctionData{
  private int x;
  private int y;
  private int arrivedDirection;
  private int walls;
  private int passagesLeft;

  public JunctionData(IRobot robot){
    setData(robot);
  }

  private void setData(IRobot robot){
    x = robot.getLocation().x;
    y = robot.getLocation().y;
    arrivedDirection = robot.getHeading();
    passagesLeft = setPassagesLeft(robot);
  }

  private int setPassagesLeft(IRobot robot){
    int direction = IRobot.AHEAD;
    int exits = 0;
    for(int i=0;i<4;i++){
      if(robot.look(direction) == IRobot.PASSAGE){exits++;}
      direction++;
    }
    return exits;
  }

  public int getX(){
    return x;
  }

  public int getY(){
    return y;
  }

  public int getArrivedDirection(){
    return arrivedDirection;
  }

  public int getPassagesLeft(){
    return passagesLeft;
  }
}
