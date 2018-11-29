import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.warwick.dcs.maze.logic.Maze;
import uk.ac.warwick.dcs.maze.generators.PrimGenerator;
import uk.ac.warwick.dcs.maze.logic.RobotImpl;

/*
    This class contains unit tests for the RandomController class.
*/
public class RandomControllerTest {

  // the maze used for testing
  private Maze maze;
  // the robot used for testing
  private RobotImpl robot;
  // the controller used for testing
  private RandomController controller;

    /*
        Tests whether the random controller causes the robot
        to walk into walls.
    */
    @Test(timeout=10000)
    public void doesNotRunIntoWallsTest() {
        // generate a random maze
        this.maze = (new PrimGenerator()).generateMaze();

        // initialise the robot
        RobotImpl robot = new RobotImpl();
        robot.setMaze(maze);

        // initialise the random robot controller
        this.controller = new RandomController();
        this.controller.setRobot(robot);

        // run the controller
        this.controller.start();

        // test whether the robot walked into walls during this run
        assertTrue(
            "RandomController walks into walls!",
            robot.getCollisions() == 0);
    }

}
