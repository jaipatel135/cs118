import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import uk.ac.warwick.dcs.maze.logic.Maze;
import uk.ac.warwick.dcs.maze.generators.PrimGenerator;
import uk.ac.warwick.dcs.maze.logic.IRobot;
import uk.ac.warwick.dcs.maze.logic.RobotImpl;
import java.awt.Point;

/*
This class contains unit tests for the HomingController class.
*/
public class HomingControllerTest {
  // the dimensions of the test maze
  private int columns = 5;
  private int rows = 5;
  // the maze used for testing
  private Maze maze;
  // the robot used for testing
  private RobotImpl robot;
  // the controller used for testing
  private HomingController controller;

  /*
  This method is run before all tests.
  */
  @Before
  public void setupTests() {
    // generate a maze with the test dimensions
    this.maze = new Maze(this.columns, this.rows);

    // fill the maze with passages
    clearMaze();

    // set the starting point somewhere near the middle
    this.maze.setStart(2,2);
    this.maze.setFinish(0,0);

    // initialise the robot
    this.robot = new RobotImpl();
    this.robot.setMaze(this.maze);

    // initialise the random robot controller
    this.controller = new HomingController();
    this.controller.setRobot(this.robot);
  }

  public void clearMaze(){
    for (int i=0; i<this.columns; i++) {
      for (int j=0; j<this.rows; j++) {
        this.maze.setCellType(i, j, Maze.PASSAGE);
      }
    }
  }

  /*
  Tests whether the homing controller's isTargetNorth
  method works as specified.
  */
  @Test(timeout=10000)
  public void isTargetNorthTest() {
    // move the target to some cells north of the robot and
    // test whether isTargetNorth correctly identifies this
    for(int i=0; i<this.columns; i++) {
      this.robot.setTargetLocation(new Point(i,0));

      assertTrue(
      "HomingController doesn't think the target is north!",
      this.controller.isTargetNorth() == 1);
    }

    // move the target to some cells south of the robot and
    // test whether isTargetNorth correctly identifies this
    for(int i=0; i<this.columns; i++) {
      this.robot.setTargetLocation(new Point(i,4));

      assertTrue(
      "HomingController doesn't think the target is south!",
      this.controller.isTargetNorth() == -1);
    }

    // move the target to some cells on the same y-level as the
    // robot and test whether isTargetNorth correctly identifies this
    for(int i=0; i<this.columns; i++) {
      this.robot.setTargetLocation(new Point(i,2));

      assertTrue(
      "HomingController doesn't think the target is on the same level!",
      this.controller.isTargetNorth() == 0);
    }
  }

  /*
  Tests whether the homing controller's isTargetEast
  method works as specified.
  */
  @Test(timeout=10000)
  public void isTargetEastTest() {
    // move the target to some cells east of the robot and
    // test whether isTargetEast correctly identifies this
    for(int i=0; i<this.columns; i++) {
      this.robot.setTargetLocation(new Point(4,i));

      assertTrue(
      "HomingController doesn't think the target is east!",
      this.controller.isTargetEast() == 1);
    }

    // move the target to some cells west of the robot and
    // test whether isTargetEast correctly identifies this
    for(int i=0; i<this.columns; i++) {
      this.robot.setTargetLocation(new Point(0,i));

      assertTrue(
      "HomingController doesn't think the target is west!",
      this.controller.isTargetEast() == -1);
    }

    // move the target to some cells on the same x-level as the
    // robot and test whether isTargetEast correctly identifies this
    for(int i=0; i<this.columns; i++) {
      this.robot.setTargetLocation(new Point(2,i));

      assertTrue(
      "HomingController doesn't think the target is on the same level!",
      this.controller.isTargetEast() == 0);
    }
  }

  /*
  Tests whether the homing controller's lookHeading method
  works correctly.
  */
  @Test(timeout=10000)
  public void lookHeadingTest() {
    // add some walls to the maze
    this.maze.setCellType(2, 1, Maze.WALL);
    this.maze.setCellType(2, 3, Maze.WALL);

    // test lookHeading for when the robot is facing north
    this.robot.setHeading(IRobot.NORTH);
    assertTrue(
    "HomingController doesn't see a wall in the north!",
    this.controller.lookHeading(IRobot.NORTH) == IRobot.WALL);
    assertTrue(
    "HomingController doesn't see a passage in the east!",
    this.controller.lookHeading(IRobot.EAST) == IRobot.PASSAGE);
    assertTrue(
    "HomingController doesn't see a wall in the south!",
    this.controller.lookHeading(IRobot.SOUTH) == IRobot.WALL);
    assertTrue(
    "HomingController doesn't see a passage in the west!",
    this.controller.lookHeading(IRobot.WEST) == IRobot.PASSAGE);

    // test lookHeading for when the robot is facing east
    this.robot.setHeading(IRobot.EAST);
    assertTrue(
    "HomingController doesn't see a wall in the north!",
    this.controller.lookHeading(IRobot.NORTH) == IRobot.WALL);
    assertTrue(
    "HomingController doesn't see a passage in the east!",
    this.controller.lookHeading(IRobot.EAST) == IRobot.PASSAGE);
    assertTrue(
    "HomingController doesn't see a wall in the south!",
    this.controller.lookHeading(IRobot.SOUTH) == IRobot.WALL);
    assertTrue(
    "HomingController doesn't see a passage in the west!",
    this.controller.lookHeading(IRobot.WEST) == IRobot.PASSAGE);

    // test lookHeading for when the robot is facing south
    this.robot.setHeading(IRobot.SOUTH);
    assertTrue(
    "HomingController doesn't see a wall in the north!",
    this.controller.lookHeading(IRobot.NORTH) == IRobot.WALL);
    assertTrue(
    "HomingController doesn't see a passage in the east!",
    this.controller.lookHeading(IRobot.EAST) == IRobot.PASSAGE);
    assertTrue(
    "HomingController doesn't see a wall in the south!",
    this.controller.lookHeading(IRobot.SOUTH) == IRobot.WALL);
    assertTrue(
    "HomingController doesn't see a passage in the west!",
    this.controller.lookHeading(IRobot.WEST) == IRobot.PASSAGE);

    // test lookHeading for when the robot is facing west
    this.robot.setHeading(IRobot.WEST);
    assertTrue(
    "HomingController doesn't see a wall in the north!",
    this.controller.lookHeading(IRobot.NORTH) == IRobot.WALL);
    assertTrue(
    "HomingController doesn't see a passage in the east!",
    this.controller.lookHeading(IRobot.EAST) == IRobot.PASSAGE);
    assertTrue(
    "HomingController doesn't see a wall in the south!",
    this.controller.lookHeading(IRobot.SOUTH) == IRobot.WALL);
    assertTrue(
    "HomingController doesn't see a passage in the west!",
    this.controller.lookHeading(IRobot.WEST) == IRobot.PASSAGE);
  }

  private void testAssert(){
    HomingController r = this.controller;
    int dH = r.determineHeading();
    assertTrue("The method ran into a wall",
    r.lookHeading(dH) != IRobot.WALL);

    assertTrue(
    "The method did not return an optimal value or ran into a wall",
    //checks the robot doesnt run into walls
    ((r.lookHeading(dH) != IRobot.WALL) &
    //checks if the robot can get closer to the target it will do
    (((dH == IRobot.NORTH & r.isTargetNorth() == 1) ||
    (dH == IRobot.EAST & r.isTargetEast() == 1) ||
    (dH == IRobot.SOUTH & r.isTargetNorth() == -1) ||
    (dH == IRobot.WEST & r.isTargetEast() == -1)) ||
    //checks if the robot has no optimal route then a random direction will be chosen
    ((dH == IRobot.NORTH || dH == IRobot.SOUTH ||
    dH == IRobot.EAST || dH == IRobot.WEST) &
    (

    //northwest blocked
    (((r.isTargetNorth() == 1 & r.lookHeading(IRobot.NORTH) == IRobot.WALL) &
    (r.isTargetEast() == 1 & r.lookHeading(IRobot.EAST) == IRobot.WALL)) &
    (dH == IRobot.SOUTH & r.lookHeading(IRobot.SOUTH) != IRobot.WALL ) ||
    (dH == IRobot.WEST & r.lookHeading(IRobot.WEST) != IRobot.WALL)) ||
    //northeast blocked
    (((r.isTargetNorth() == 1 & r.lookHeading(IRobot.NORTH) == IRobot.WALL) &
    (r.isTargetEast() == -1 & r.lookHeading(IRobot.WEST) == IRobot.WALL)) &
    (dH == IRobot.SOUTH & r.lookHeading(IRobot.SOUTH) != IRobot.WALL ) ||
    (dH == IRobot.EAST & r.lookHeading(IRobot.EAST) != IRobot.WALL)) ||
    //southeast blocked
    (((r.isTargetNorth() == -1 & r.lookHeading(IRobot.SOUTH) == IRobot.WALL) &
    (r.isTargetEast() == 1 & r.lookHeading(IRobot.EAST) == IRobot.WALL)) &
    (dH == IRobot.NORTH & r.lookHeading(IRobot.NORTH) != IRobot.WALL ) ||
    (dH == IRobot.WEST & r.lookHeading(IRobot.WEST) != IRobot.WALL))||
    //southwest blocked
    (((r.isTargetNorth() == -1 & r.lookHeading(IRobot.SOUTH) == IRobot.WALL) &
    (r.isTargetEast() == -1 & r.lookHeading(IRobot.WEST) == IRobot.WALL)) &
    (dH == IRobot.NORTH & r.lookHeading(IRobot.NORTH) != IRobot.WALL ) ||
    (dH == IRobot.EAST & r.lookHeading(IRobot.EAST) != IRobot.WALL)) ||
    //just north blocked and optimal
    (((r.isTargetNorth() == 1 & r.lookHeading(IRobot.NORTH) == IRobot.WALL) &
    (r.isTargetEast() == 0)) &
    (dH == IRobot.NORTH & r.lookHeading(IRobot.SOUTH) != IRobot.WALL ) ||
    (dH == IRobot.WEST & r.lookHeading(IRobot.WEST) != IRobot.WALL ) ||
    (dH == IRobot.EAST & r.lookHeading(IRobot.EAST) != IRobot.WALL)) ||
    //just east blocked and optimal
    (((r.isTargetNorth() == 0) &
    (r.isTargetEast() == 1 & r.lookHeading(IRobot.EAST) == IRobot.WALL)) &
    (dH == IRobot.NORTH & r.lookHeading(IRobot.NORTH) != IRobot.WALL ) ||
    (dH == IRobot.WEST & r.lookHeading(IRobot.WEST) != IRobot.WALL ) ||
    (dH == IRobot.SOUTH & r.lookHeading(IRobot.SOUTH) != IRobot.WALL)) ||
    //just south blocked and optimal
    (((r.isTargetNorth() == -1 & r.lookHeading(IRobot.SOUTH) == IRobot.WALL) &
    (r.isTargetEast() == 0)) &
    (dH == IRobot.NORTH & r.lookHeading(IRobot.NORTH) != IRobot.WALL ) ||
    (dH == IRobot.WEST & r.lookHeading(IRobot.WEST) != IRobot.WALL ) ||
    (dH == IRobot.EAST & r.lookHeading(IRobot.EAST) != IRobot.WALL)) ||
    //just west blocked and optimal
    (((r.isTargetNorth() == 0) &
    (r.isTargetEast() == -1 & r.lookHeading(IRobot.WEST) == IRobot.WALL)) &
    (dH == IRobot.NORTH & r.lookHeading(IRobot.NORTH) != IRobot.WALL ) ||
    (dH == IRobot.SOUTH & r.lookHeading(IRobot.SOUTH) != IRobot.WALL ) ||
    (dH == IRobot.EAST & r.lookHeading(IRobot.EAST) != IRobot.WALL))

    )
    )
    )
    )
    );
  }

  private void setupMaze(int i, int j, int k, int l){
    clearMaze();
    if(i==1){
      this.maze.setCellType(2, 1, Maze.WALL);//north wall toggle
    }
    if(j==1){
      this.maze.setCellType(2, 3, Maze.WALL);//south wall toggle
    }
    if(k==1){
      this.maze.setCellType(1, 2, Maze.WALL);//west wall toggle
    }
    if(l==1){
      this.maze.setCellType(3, 2, Maze.WALL);//east wall toggle
    }
    //if the robot is fully blocked in clear the maze because this scenario is impossible to return a direction for
    if(i==1 & j==1 & k==1& l==1){
      clearMaze();
    }
  }

  //tests the 128 scenarios of randomdirection (repeats 4 of them 4 times 0,0 0,4 4,0 4,4)
  @Test(timeout=10000)
  public void determineHeadingTest(){
    //sets up the North scenario
    for(int i=0;i<2;i++){
      for(int j=0;j<2;j++){
        for(int k=0;k<2;k++){
          for(int l=0;l<2;l++){
            //i,j,k,l are 0 or 1 for toggling walls
            setupMaze(i,j,k,l);
            /*8 points are set and then tested for each maze layout
            N NW NE W E S SW SE*/
            this.robot.setTargetLocation(new Point(0,0));
            testAssert();
            this.robot.setTargetLocation(new Point(2,0));
            testAssert();
            this.robot.setTargetLocation(new Point(4,0));
            testAssert();
            this.robot.setTargetLocation(new Point(0,2));
            testAssert();
            this.robot.setTargetLocation(new Point(4,2));
            testAssert();
            this.robot.setTargetLocation(new Point(0,4));
            testAssert();
            this.robot.setTargetLocation(new Point(2,4));
            testAssert();
            this.robot.setTargetLocation(new Point(4,4));
            testAssert();
          }
        }
      }
    }
  }

}
