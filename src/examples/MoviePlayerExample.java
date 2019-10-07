package examples;

import model.MonitorType;
import model.MoviePlayer;
import model.Screen;

public class MoviePlayerExample {

  public static void main(String[] args) {
    MoviePlayer player = new MoviePlayer("iPod Touch", "Apple",
        new Screen("1920x1080", 60, 1), MonitorType.LCD);
    System.out.println(player);
  }
}
