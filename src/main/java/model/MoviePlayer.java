package model;

public class MoviePlayer extends Product implements MultimediaControl {

  private Screen screen;
  private MonitorType monitorType;

  public MoviePlayer(String name, String manufacturer, Screen screen, MonitorType monitorType) {
    super(name, manufacturer);
    this.screen = screen;
    this.monitorType = monitorType;
    type = ItemType.Visual.toString();
  }

  @Override
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
  }

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
        "%s%nScreen: %s%nMonitor Type: %s",
        super.toString(), screen.toString(), monitorType.toString());
  }

  public static void main(String[] args) {
    MoviePlayer player = new MoviePlayer("iPod Touch", "Apple",
        new Screen("1920x1080", 60, 1), MonitorType.LCD);
    System.out.println(player);
  }
}