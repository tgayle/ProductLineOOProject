package model.production;

import java.time.LocalDateTime;
import misc.Util;
import model.Product;

public class ProductionWithProduct extends Production {

  private Product product;
  private String productSimpleName;

  public ProductionWithProduct(int productionId, int quantity, LocalDateTime manufacturedOn,
      Product product) {
    super(product.getId(), quantity, "", manufacturedOn);
    this.productionId = productionId;
    this.product = product;
    this.productSimpleName = product.getSimpleName();
  }

  public ProductionWithProduct(Product product, int quantity, LocalDateTime manufacturedOn) {
    super(product.getId(), quantity, manufacturedOn);
    this.productionId = 0;
    this.product = product;
    this.productSimpleName = product.getSimpleName();
  }

  public ProductionWithProduct(int productionId, int quantity,
      String serialNumber, LocalDateTime manufacturedOn, Product product) {
    super(productionId, product.getId(), quantity, serialNumber, manufacturedOn);

    setProduct(product);
  }

  public Product getProduct() {
    return product;
  }

  /**
   * Generates a serial number for a product.
   *
   * @param uuid The uuid that will be assigned to this production. The next
   * @return the generated serial number
   */
  public void generateSerialNumber(int uuid) {
    String manufacturer = product.getManufacturer();
    String manufacturerPrefix =
        manufacturer.length() > 3 ? manufacturer.substring(0, 3) : manufacturer;
    String paddedUUID = Util.padLeft(String.valueOf(uuid), 5, "0");
    String serialNum = manufacturerPrefix + product.getItemType().getCode() + paddedUUID;

    setSerialNumber(serialNum);
  }

  public void setProduct(Product product) {
    this.product = product;
    this.productSimpleName = product.getSimpleName();
  }

  public String getProductSimpleName() {
    return productSimpleName;
  }
}
