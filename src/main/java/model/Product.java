package model;

import misc.Util;

public abstract class Product implements Item, Comparable {

  protected int id;
  protected ItemType itemType;
  protected String manufacturer;
  protected String name;
  protected String serialNumber;

  /**
   * Creates a Product with an unspecified serialNumber.
   *
   * @see #Product(String, String, String)
   */
  public Product(String name, String manufacturer) {
    this.name = name;
    this.manufacturer = manufacturer;
  }

  /**
   * Creates an instance of a product with a name, manufacturer, and serial number.
   *
   * @param name The name of the product
   * @param manufacturer The product's manufacturer
   * @param serialNumber The product's serial number.
   */
  public Product(String name, String manufacturer, String serialNumber) {
    this.name = name;
    this.manufacturer = manufacturer;
    this.serialNumber = serialNumber;
  }

  /**
   * Generates a serial number for a product.
   *
   * @param uuid The uuid that will be assigned to this production. The next
   */
  public void generateSerialNumber(int uuid) {
    String manufacturerPrefix =
        manufacturer.length() > 3 ? manufacturer.substring(0, 3) : manufacturer;

    String paddedUuid = Util.padLeft(String.valueOf(uuid), 5, "0");
    String serialNum = manufacturerPrefix + getItemType().getCode() + paddedUuid;

    setSerialNumber(serialNum);
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public String getType() {
    return itemType.toString();
  }

  public ItemType getItemType() {
    return itemType;
  }

  public void setItemType(ItemType itemType) {
    this.itemType = itemType;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getManufacturer() {
    return manufacturer;
  }

  @Override
  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  @Override
  public String toString() {
    String format = "Name: %s%nManufacturer: %s%nType: %s";
    return String.format(format, name, manufacturer, getType());
  }

  public String getSimpleName() {
    return String.format("%s %s (%s)", manufacturer, name, getItemType());
  }

  @Override
  public int compareTo(Object o) {
    if (o instanceof Product) {
      return Integer.compare(this.id, ((Product) o).id);
    }

    return 0;
  }
}
