import uk.ac.warwick.dcs.maze.logic.*;
import java.awt.Point;

public class HomingController implements IRobotController {
    // the robot in the maze
    private IRobot robot;
    // a flag to indicate whether we are looking for a path
    private boolean active = false;
    // a value (in ms) indicating how long we should wait
    // between moves
    private int delay;

    // this method is called when the "start" button is clicked
    // in the user interface
    public void start() {
        this.active = true;
        int direction;
        /*
        1000 NORTH
        1001 EAST
        1002 SOUTH
        1003 WEST

        2000 AHEAD
        2001 RIGHT
        2002 BEHIND
        2003 LEFT

        numeric directions for arithmetic on them.
        */

        //runs atleast once and will keep looping while the robot has not found the exit or been stopped
        do{
            //chooses a direction and then turns to that direction using custom defined methods
            robot.setHeading(determineHeading());
            //if there is no wall then advance
            if(robot.look(IRobot.AHEAD) != IRobot.WALL){
              robot.advance();
            }
            // wait for a while if we are supposed to
            if (delay > 0){
                robot.sleep(delay);
            }
        } while (!robot.getLocation().equals(robot.getTargetLocation()) && active);
    }

    //chooses a random direction relative to the robot. (no longer used can be removed)
    private int randomDirection(){
      int direction;
      int randno = (int) Math.round(Math.random()*4);//didnt have equal probililities as *3;
      // change the direction based on the random number
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

    // this method returns 1 if the target is north of the
    // robot, -1 if the target is south of the robot, or
    // 0 if otherwise.
    public byte isTargetNorth() {
        if(robot.getLocation().y > robot.getTargetLocation().y){
          return 1;//if the target is north
        }else if(robot.getLocation().y < robot.getTargetLocation().y){
          return -1;//if the target is south
        }
        return 0;//if they are on the same y level
    }

    // this method returns 1 if the target is east of the
    // robot, -1 if the target is west of the robot, or
    // 0 if otherwise.
    public byte isTargetEast() {
        if(robot.getLocation().x < robot.getTargetLocation().x){
          return 1;//if target is east
        }else if(robot.getLocation().x > robot.getTargetLocation().x){
          return -1;//if target is west
        }
        return 0;//if target is on the same x level
    }

    // this method causes the robot to look to the absolute
    // direction that is specified as argument and returns
    // what sort of square there is
    public int lookHeading(int absoluteDirection) {
        //get the current absolute heading of the robot
        int previousHeading = robot.getHeading();
        //checks if absolute direction is a valid value
        if(absoluteDirection >= 1000 && absoluteDirection <=1003){
            if(previousHeading < absoluteDirection){
              /*returns the object that is in the absolute position given
              if the absoluteDirection value is larger than the previousHeading value*/
              int diff = absoluteDirection - previousHeading;
              return robot.look(IRobot.AHEAD+diff);
            }else if(previousHeading > absoluteDirection){
              /*returns the object that is in the absolute position given
              if the absoluteDirection is smaller than the previousHeading value*/
              int diff = previousHeading - absoluteDirection;
              return robot.look(2004-diff);
            }else{
              //if the values match then the robot can look ahead
              return robot.look(robot.AHEAD);
            }
        }else{
          //if the value of absoluteDirection is not a NORTH EAST SOUTH WEST value then return 0
          return 0;
        }
    }

    //returns a random heading (absolute direction)
    private int randomHeading(){
      int direction;
      //each statement has a 1/4 chance to run.
      do{
        int randno = (int) Math.round(Math.random()*4);
        if (randno == 1){
          direction = IRobot.NORTH;
        }else if (randno == 2){
          direction = IRobot.EAST;
        }else if (randno == 3){
          direction = IRobot.SOUTH;
        }else{
          direction = IRobot.WEST;
        }}while(lookHeading(direction) == IRobot.WALL);
        return direction;
      }

    //chooses between two integers with a  roughly 50/50 chance
    private int randomHeading(int directionOne, int directionTwo){
      if (Math.random()<=0.4999f){
        return directionOne;
      }else{
        return directionTwo;
      }
    }

    // this method determines the heading in which the robot
    // should head next to move closer to the target
    public int determineHeading() {
        int direction = IRobot.NORTH;
        //variables to be used to combine directions
        int n = 2;//NORTH 1 south 0 same-level 2
        int e = 2;//EAST 1 west 0 same-level 2
        /*this if structure will check if the target is north south east of west and
        assign the direction to one of the four values it will also change the combination variables
        so that if there is more than on41)
	at HomingControllerTest.testAssert(HomingCoe prefered exit or no prefered exit it can be handled.*/
        if(isTargetNorth() == 1 && (lookHeading(IRobot.NORTH) != IRobot.WALL)){

          direction = IRobot.NORTH;
          n = 1;
        }else if(isTargetNorth() == -1 && (lookHeading(IRobot.SOUTH) !=IRobot.WALL)){
          direction = IRobot.SOUTH;
          n = 0;
        }
        if(isTargetEast() == 1 && (lookHeading(IRobot.EAST) != IRobot.WALL)){
          direction = IRobot.EAST;
          e = 1;
        }else if(isTargetEast() == -1 && (lookHeading(IRobot.WEST) !=IRobot.WALL)){
          direction = IRobot.WEST;
          e = 0;
        }
        //handles combinations of exits and lack of exits
        if(e==1 & n==1){
          //NORTH EAST
          direction = randomHeading(IRobot.NORTH,IRobot.EAST);
        }else if(e==2 & n==2){
          //NO AVAILABLE EXITS THAT ARE PREFERED
          direction = randomHeading();
        }else if(e==1 & n==0){
          //SOUTH EAST
          direction = randomHeading(IRobot.SOUTH,IRobot.EAST);
        }else if(e==0 & n==0){
          //SOUTH WEST
          direction = randomHeading(IRobot.SOUTH,IRobot.WEST);
        }else if(e==0 & n==1){
          // NORTH WEST
          direction = randomHeading(IRobot.NORTH,IRobot.WEST);
        }else if(e==2 & (n==1 || n==0)){
          if(n==1){
            //directly north
            direction = IRobot.NORTH;
          }else{
            //directly south
            direction = IRobot.SOUTH;
          }
        }else if(n==2 & (e==1 | e==0)){
          if(e==1){
            //directly east
            direction = IRobot.EAST;
          }else{
            //directly west
            direction = IRobot.WEST;
          }
        }
        return direction;
    }

    // this method returns a description of this controller
    public String getDescription() {
       return "A controller which homes in on the target";
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
    }

    // sets the reference to the robot
    public void setRobot(IRobot robot) {
       this.robot = robot;
    }
}
