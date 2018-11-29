import uk.ac.warwick.dcs.maze.logic.*;
import java.awt.Point;

public class RandomController implements IRobotController {
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

        // loop while we haven't found the exit and the robot
        // has not been interrupted
        while(!robot.getLocation().equals(robot.getTargetLocation()) && active) {
            //generates a number between 0-8 and uses it to run random direction 1 in every 8 moves.
            int rand = (int)Math.round(Math.random()*8);
            if(rand <=1){
              randomDirection();
            }
            //Checks if there is a wall infront of the robot and will turn the robot to a new direction
            //if there is a wall. if there is no wall then the robot will move forward one tile.
            if(robot.look(IRobot.AHEAD) == IRobot.WALL ){
              randomDirection();
            }else{
              // move one step into the direction the robot is facing
              robot.advance();
            }
            // wait for a while if we are supposed to
            if (delay > 0)
                robot.sleep(delay);
       }
    }

    private void randomDirection(){
      // generate a random number between 0-4 (inclusive)
      int rand = (int)Math.round(Math.random()*4);
      if(rand == 0 | rand == 4){
        rand = 4;
      }
      // turn into one of the four directions, as determined
      // by the random number that was generated:
      // 1: ahead
      // 2: left
      // 3: right
      // 4: behind
      switch (rand) {
      case 1:
          robot.face(IRobot.AHEAD);
          robot.getLogger().log(IRobot.AHEAD);
          break;
      case 2:
          robot.face(IRobot.LEFT);
          robot.getLogger().log(IRobot.LEFT);
          break;
      case 3:
          robot.face(IRobot.RIGHT);
          robot.getLogger().log(IRobot.RIGHT);
          break;
      case 4:
          robot.face(IRobot.BEHIND);
          robot.getLogger().log(IRobot.BEHIND);
          break;
        }
    }

    // this method returns a description of this controller
    public String getDescription() {
       return "A controller which randomly chooses where to go";
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
