package misc;

public class Util {

  public static String padLeft(String input, int totalLength, String paddingChar) {
    return String.format("%1$" + totalLength + "s", input).replace(" ", paddingChar);
  }
}
