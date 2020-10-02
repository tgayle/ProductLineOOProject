package model;

public abstract class Product implements Item, Comparable {

  protected int id;
  protected ItemType itemType;
  protected String manufacturer;
  protected String name;

  public Product(String name, String manufacturer) {
	  System.out.println("test");
    this.name = name;
    this.manufacturer = manufacturer;
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
