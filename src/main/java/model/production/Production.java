package model.production;

import java.time.LocalDateTime;

/**
 * A record of production containing information on the product produced, the amount produced, and
 * when this production was completed.
 */
public class Production {

  private int productionId; // id
  private int productId;
  private int quantity;
  private LocalDateTime manufacturedOn;

  /**
   * Creates an instance of a production record.
   *
   * @param productId The id of the product that was produced
   * @param quantity The number of items produced
   * @param manufacturedOn The date and time that the product was produced.
   */
  public Production(int productId, int quantity, LocalDateTime manufacturedOn) {
    this.productId = productId;
    this.quantity = quantity;
    this.manufacturedOn = manufacturedOn;
  }

  public Production(int productionId, int productId, int quantity,
      LocalDateTime manufacturedOn) {
    this.productionId = productionId;
    this.productId = productId;
    this.quantity = quantity;
    this.manufacturedOn = manufacturedOn;
  }

  public int getProductionId() {
    return productionId;
  }

  public void setProductionId(int productionId) {
    this.productionId = productionId;
  }

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public LocalDateTime getManufacturedOn() {
    return manufacturedOn;
  }


  public void setManufacturedOn(LocalDateTime manufacturedOn) {
    this.manufacturedOn = manufacturedOn;
  }
}
