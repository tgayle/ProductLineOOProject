package examples;

import model.video.MonitorType;
import model.video.MoviePlayer;
import model.video.Screen;

public class MoviePlayerExample {

  public static void main(String[] args) {
    MoviePlayer player = new MoviePlayer("iPod Touch", "Apple",
        new Screen("1920x1080", 60, 1), MonitorType.LCD);
    System.out.println(player);
  }
}
