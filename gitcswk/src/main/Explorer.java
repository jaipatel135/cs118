import uk.ac.warwick.dcs.maze.logic.*;
import java.awt.Point;

public class Explorer implements IRobotController {
  // the robot in the maze
  private IRobot robot;
  // a flag to indicate whether we are looking for a path
  private boolean active = false;
  // a value (in ms) indicating how long we should wait
  // between moves
  private int delay;

  private RobotData robotData;

  // this method is called when the "start" button is clicked
  // in the user interface
  public void start() {
    this.active = true;
    if(this.robot.getRuns() == 0){
      this.robotData = new RobotData();
    }
    while(!robot.getLocation().equals(robot.getTargetLocation()) && active) {
      int exits = this.nonwallExits();
      int direction = IRobot.AHEAD;
      testPosition();
      if(exits == 1){
        direction = deadEnd();
      }else if(exits == 2){
        direction = corridor();
      }else if(exits == 3){
        direction = junction();
      }else if(exits == 4){
        direction = crossroads();
      }
      robot.face(direction);
      robot.advance();
      // wait for a while if we are supposed to
      if (delay > 0)
      robot.sleep(delay);
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
    robotData.addTile(robot);
  }
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

  public int randomNonWall(){
    int direction = randomDirection();
    while(robot.look(direction) == IRobot.WALL){
      direction = randomDirection();
    }
    return direction;
  }

  public int randomDirection(int directionOne, int directionTwo){
    if (Math.random()<=0.4999f){
      return directionOne;
    }else{
      return directionTwo;
    }
  }

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

  public int deadEnd(){
    if(robot.look(IRobot.BEHIND) != IRobot.WALL){
      return IRobot.BEHIND;
    }else{
      return randomNonWall();
    }
  }

  public int corridor(){
    int direction = IRobot.AHEAD;
    int possibleDirectionOne = 1;
    int possibleDirectionTwo;
    for(int i=0;i<4;i++){
      if(robot.look(direction) != IRobot.WALL){
        if(robot.look(direction) == IRobot.PASSAGE){return direction;}
        if(possibleDirectionOne == 1){
          possibleDirectionOne = direction;
        }else{
          possibleDirectionTwo = direction;
          return randomDirection(possibleDirectionOne,possibleDirectionTwo);
        }
      }
      direction++;
    }
    return 0;
  }

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

  // this method returns a description of this controller
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

private class RobotData{
  private static int maxJunctions = 10000;
  private static int junctionCounter;
  private DynamicDataArray tiles[];

  public void RobotData(){
    resetJunctionCounter();
    tiles = new DynamicDataArray(1);
    addTile();
  }

  public void addTile(robot robot){
    TileData thisTile = new TileData();
    tiles.add(thisTile);
  }

  public void resetJunctionCounter(){
    junctionCounter = 0;
  }
}

private class DynamicDataArray{
  private TileData[] array;
  private TileData[] newArray;
  private int capacity;
  private int nextFree;

  public DynamicDataArray(int initialCapacity){
    array = new TileData[initialCapacity];
    capacity = initialCapacity;
    nextFree = 0;
  }

  public getSize(){
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
    newArray = new TileData[capacity+1];
    for(int i=0;i<capacity;i++){
      newArray[i] = array[i];
    }
    array = newArray;
    capacity= capacity+1;
  }

  public void add(TileData tileData){
    if(nextFree >= capacity-1){
      this.resize();
    }
    array[nextFree] = tileData;
    nextFree++;
  }

  public TileData get(int index){
    return array[index];
  }
}


private class TileData{
  private int x;
  private int y;
  private int arrivedDirection;
  private int walls;
  private int passagesLeft;
  private

  public void TileData(){
    setData();
  }

  private setData(){
    x = robot.getLocation().x;
    y = robot.getLocation().y;
    arrivedDirection = robot.getHeading();
    exits = nonwallExits();
    passagesLeft = passageExits()-1;
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

  public int getExits(){
    return exits;
  }

  public int passagesLeft(){
    return passagesLeft;
  }
}
