package model;

public enum ItemType {
  Audio("AU"),
  Visual("VI"),
  AudioMobile("AM"),
  VisualMobile("VM");

  private final String code;

  ItemType(String code) {
    this.code = code;
  }

  /**
   * Finds an ItemType from a given code.
   *
   * @param code The code for the ItemType we want to find.
   * @return ItemType
   */
  public static ItemType fromCode(String code) {
    for (ItemType type : ItemType.values()) {
      if (code.equals(type.code)) {
        return type;
      }
    }
    throw new RuntimeException("Tried to get an ItemType that doesn't exist: " + code);
  }

  public String getCode() {
    return code;
  }

}
