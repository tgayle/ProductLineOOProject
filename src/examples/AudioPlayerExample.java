package examples;

import model.audio.AudioPlayer;

/**
 * A driver class for testing AudioPlayers providing an example of inheritance.
 *
 * @author Travis Gayle
 */
public class AudioPlayerExample {

  /**
   * The main entry point for this class.
   * @param args Optional string arguments -- no use for this example.
   */
  public static void main(String[] args) {
    AudioPlayer player = new AudioPlayer("DP-X1A", "Onkyo",
        "DSD/FLAC/ALAC/WAV/AIFF/MQA/Ogg-Vorbis/MP3/AAC", "M3U/PLS/WPL");
    System.out.println(player);
    player.play();
    player.stop();
    player.next();
    player.previous();
  }
}
