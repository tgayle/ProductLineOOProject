package model;

public class AudioPlayer extends Product implements MultimediaControl {

  private String audioSpecification;
  private String mediaType;

  public AudioPlayer(String name, String manufacturer, String audioSpecification) {
    super(name, manufacturer);
    this.audioSpecification = audioSpecification;
  }

  @Override
  public void play() {
    System.out.println(name + " is playing.");
  }

  @Override
  public void stop() {
    System.out.println(name + " has stopped playing.");
  }

  @Override
  public void previous() {
    System.out.println(name + " is playing the previous media.");
  }

  @Override
  public void next() {
    System.out.println(name + " is playing the next media.");
  }

  @Override
  public String toString() {
    String output = super.toString() + "\n";
    output += "Audio Spec: " + audioSpecification + "\n";
    output += "Media Type: " + mediaType;
    return output;
  }

  public String getAudioSpecification() {
    return audioSpecification;
  }

  public void setAudioSpecification(String audioSpecification) {
    this.audioSpecification = audioSpecification;
  }

  public String getMediaType() {
    return mediaType;
  }

  public void setMediaType(String mediaType) {
    this.mediaType = mediaType;
  }
}
