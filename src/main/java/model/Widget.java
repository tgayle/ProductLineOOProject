package model;

/**
 * A general instance of a Product for working with items produced when the exact type or
 * functionality is not required.
 */
public class Widget extends Product {

  public Widget(String name, String manufacturer) {
    super(name, manufacturer);
  }

  public Widget(String name, String manufacturer, ItemType type) {
    super(name, manufacturer);
    itemType = type;
  }

  /**
   * Creates a basic product.
   *
   * @param id The product's id.
   * @param name The Product's name
   * @param type The type of the product
   * @param manufacturer The product's manufacturer.
   */
  public Widget(int id, String name, ItemType type, String manufacturer) {
    super(name, manufacturer);
    this.id = id;
    this.itemType = type;
  }
}
