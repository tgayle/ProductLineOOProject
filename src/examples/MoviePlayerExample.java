package examples;

import model.video.MonitorType;
import model.video.MoviePlayer;
import model.video.Screen;

/**
 * A driver class for testing MoviePlayers providing an example of inheritance.
 *
 * @author Travis Gayle
 */
public class MoviePlayerExample {

  /**
   * The main entry point for this class.
   * @param args Optional string arguments -- no use for this example.
   */
  public static void main(String[] args) {
    MoviePlayer player = new MoviePlayer("iPod Touch", "Apple",
        new Screen("1920x1080", 60, 1), MonitorType.LCD);
    System.out.println(player);
  }
}
