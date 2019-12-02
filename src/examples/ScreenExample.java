package examples;

import model.video.Screen;

/**
 * Basic driver class for testing a Screen, their creation and its toString method.
 */
public class ScreenExample {

  public static void main(String[] args) {
    System.out.println(new Screen("1920x1080", 120, 2));
  }

}
