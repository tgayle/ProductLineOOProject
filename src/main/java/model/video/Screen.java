package model.video;

public class Screen implements ScreenSpec {

  private String resolution;
  private int refreshRate;
  private int responseTime;

  /**
   * Creates a screen given a resolution, refresh rate, and response time.
   *
   * @param resolution The screen's resolution in the form of "{width}x{height}"
   * @param refreshRate The screen's refresh rate, in hertz.
   * @param responseTime The screen's response time in milliseconds.
   */
  public Screen(String resolution, int refreshRate, int responseTime) {
    this.resolution = resolution;
    this.refreshRate = refreshRate;
    this.responseTime = responseTime;
  }

  @Override
  public String getResolution() {
    return resolution;
  }

  @Override
  public int getRefreshRate() {
    return refreshRate;
  }

  @Override
  public int getResponseTime() {
    return responseTime;
  }

  @Override
  public String toString() {
    String format = "Resolution: %s%nRefresh Rate: %d%nResponse Time: %d hz";
    return String.format(format, resolution, refreshRate, responseTime);
  }
}
