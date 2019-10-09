package examples;

import model.audio.AudioPlayer;

public class AudioPlayerExample {

  public static void main(String[] args) {
    AudioPlayer player = new AudioPlayer("DP-X1A", "Onkyo",
        "DSD/FLAC/ALAC/WAV/AIFF/MQA/Ogg-Vorbis/MP3/AAC", "M3U/PLS/WPL");
    player.setType("AUDIO");
    System.out.println(player);
    player.play();
    player.stop();
    player.next();
    player.previous();
  }
}
