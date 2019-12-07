package model.production;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import misc.Util;
import model.Product;

/**
 * A record of production containing information on the product produced, the amount produced, and
 * when this production was completed.
 */
public class Production {

  protected int productionId; // id
  protected int productId;
  protected String serialNumber;
  protected LocalDateTime manufacturedOn;

  /**
   * Creates an instance of a production record.
   *
   * @param productId The id of the product that was produced
   * @param manufacturedOn The date and time that the product was produced.
   */
  public Production(int productId, String serialNumber,
      LocalDateTime manufacturedOn) {
    this.productId = productId;
    this.serialNumber = serialNumber;
    this.manufacturedOn = manufacturedOn;
  }

  /**
   * Creates an instance of a production record.
   *
   * @param productionId The id of this production
   * @param productId The id of the product produced
   * @param manufacturedOn The date and time that this production was processed
   */
  public Production(int productionId, int productId,
      LocalDateTime manufacturedOn) {
    this.productionId = productionId;
    this.productId = productId;
    this.manufacturedOn = manufacturedOn;
  }

  public Production(int productId, LocalDateTime manufacturedOn) {
    this.productId = productId;
    this.manufacturedOn = manufacturedOn;
  }

  /**
   * Creates a production record .
   *
   */
  public Production(int productionId, int productId, String serialNumber,
      LocalDateTime manufacturedOn) {
    this.productionId = productionId;
    this.productId = productId;
    this.serialNumber = serialNumber;
    this.manufacturedOn = manufacturedOn;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public String getSerialNumber() {
    return serialNumber;
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

  public LocalDateTime getManufacturedOn() {
    return manufacturedOn;
  }

  public void setManufacturedOn(LocalDateTime manufacturedOn) {
    this.manufacturedOn = manufacturedOn;
  }

  public String getFormattedManufacturedDate() {
    return manufacturedOn.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
  }

  /**
   * Generates a serial number for a product.
   *
   * @param uuid The uuid that will be assigned to this production. The next
   */
  public void generateSerialNumber(Product product, int uuid) {
    String manufacturer = product.getManufacturer();
    String manufacturerPrefix =
        manufacturer.length() > 3 ? manufacturer.substring(0, 3) : manufacturer;

    String paddedUuid = Util.padLeft(String.valueOf(uuid), 5, "0");
    String serialNum = manufacturerPrefix + product.getItemType().getCode() + paddedUuid;

    setSerialNumber(serialNum);
  }

  @Override
  public String toString() {
    String formatter = "Production Num: %s%n Product ID: %s %nSerial Num: %s %nDate: %s";
    return String.format(
        formatter, productionId, productId, serialNumber, getFormattedManufacturedDate());
  }
}
