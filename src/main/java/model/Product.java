package model;

public abstract class Product implements Item {

  protected int id;
  protected String type;
  protected String manufacturer;
  protected String name;

  public Product(String name, String manufacturer) {
    this.name = name;
    this.manufacturer = manufacturer;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
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
    String format = "Name: %s\nManufacturer: %s\nType: %s";
    return String.format(format, name, manufacturer, type);
  }

  public String getSimpleName() {
    return String.format("%s %s (%s)", manufacturer, name, type);
  }
}
