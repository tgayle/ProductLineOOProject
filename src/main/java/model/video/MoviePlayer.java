package model.video;

import model.ItemType;
import model.MediaProduct;
import model.mediabehavior.moviebehavior.MovieNext;
import model.mediabehavior.moviebehavior.MoviePlay;
import model.mediabehavior.moviebehavior.MoviePrevious;
import model.mediabehavior.moviebehavior.MovieStop;

public class MoviePlayer extends MediaProduct {

  private Screen screen;
  private MonitorType monitorType;

  /**
   * Creates a movie player.
   *
   * @param name The name of the player
   * @param manufacturer The player's manufacturer
   * @param screen The screen for the player
   * @param monitorType The type of monitor.
   */
  public MoviePlayer(String name, String manufacturer, Screen screen, MonitorType monitorType) {
    super(name, manufacturer);
    this.screen = screen;
    this.monitorType = monitorType;
    setItemType(ItemType.Visual);
    setPlay(new MoviePlay());
    setStop(new MovieStop());
    setPrevious(new MoviePrevious());
    setNext(new MovieNext());
  }

  /*@Override
  public void play() {
    System.out.println("Playing");
  }

  @Override
  public void stop() {
    System.out.println("Stopping");
  }

  @Override
  public void previous() {
    System.out.println("Previous");
  }

  @Override
  public void next() {
    System.out.println("Next");
  }*/

  public Screen getScreen() {
    return screen;
  }

  public void setScreen(Screen screen) {
    this.screen = screen;
  }

  public MonitorType getMonitorType() {
    return monitorType;
  }

  public void setMonitorType(MonitorType monitorType) {
    this.monitorType = monitorType;
  }

  @Override
  public String toString() {
    return String.format(
        "%s%nScreen:%n%s%nMonitor Type: %s",
        super.toString(), screen.toString(), monitorType.toString());
  }
}
