package model.production;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * A record of production containing information on the product produced, the amount produced, and
 * when this production was completed.
 */
public class Production implements Comparable {

  protected int productionId; // id
  protected int productId;
  protected int quantity;
  protected String serialNumber;
  protected LocalDateTime manufacturedOn;

  /**
   * Creates an instance of a production record.
   *
   * @param productId The id of the product that was produced
   * @param quantity The number of items produced
   * @param manufacturedOn The date and time that the product was produced.
   */
  public Production(int productId, int quantity, String serialNumber,
      LocalDateTime manufacturedOn) {
    this.productId = productId;
    this.quantity = quantity;
    this.serialNumber = serialNumber;
    this.manufacturedOn = manufacturedOn;
  }

  /**
   * Creates an instance of a production record.
   *
   * @param productionId The id of this production
   * @param productId The id of the product produced
   * @param quantity The number of products produced
   * @param manufacturedOn The date and time that this production was processed
   */
  public Production(int productionId, int productId, int quantity,
      LocalDateTime manufacturedOn) {
    this.productionId = productionId;
    this.productId = productId;
    this.quantity = quantity;
    this.manufacturedOn = manufacturedOn;
  }

  /**
   * Creates a production record given a product id, a quantity, and the date and time this
   * production was processed.
   *
   * @see #Production(int, int, int, LocalDateTime)
   */
  public Production(int productId, int quantity, LocalDateTime manufacturedOn) {
    this.productId = productId;
    this.quantity = quantity;
    this.manufacturedOn = manufacturedOn;
  }

  /**
   * Creates a production record .
   *
   * @see #Production(int, int, int, LocalDateTime)
   */
  public Production(int productionId, int productId, int quantity, String serialNumber,
      LocalDateTime manufacturedOn) {
    this.productionId = productionId;
    this.productId = productId;
    this.quantity = quantity;
    this.serialNumber = serialNumber;
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

  public String getFormattedManufacturedDate() {
    return manufacturedOn.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
  }

  @Override
  public String toString() {
    String formatter = "Production Num: %s%n Product ID: %s %nSerial Num: %s %nDate: %s";
    return String.format(
        formatter, productionId, productId, serialNumber, getFormattedManufacturedDate());
  }

  @Override
  public int compareTo(Object o) {
    if (o instanceof Production) {
      return this.serialNumber.compareTo(((Production) o).serialNumber);
    }

    return 0;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj instanceof Production) {
      Production objAsProd = (Production) obj;

      return this.serialNumber.equals(objAsProd.serialNumber)
          && this.manufacturedOn.equals(objAsProd.manufacturedOn)
          && this.productId == objAsProd.productId
          && this.quantity == objAsProd.quantity
          && this.productionId == objAsProd.productionId;
    }
    return false;
  }
}
