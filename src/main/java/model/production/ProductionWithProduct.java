package model.production;

import java.time.LocalDateTime;
import model.Product;

public class ProductionWithProduct extends Production {

  private Product product;
  private String productSimpleName;

  public ProductionWithProduct(int productionId, int quantity, LocalDateTime manufacturedOn,
      Product product) {
    super(product.getId(), quantity, manufacturedOn);
    setProductionId(productionId);
    this.product = product;
    this.productSimpleName = product.getSimpleName();
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public String getProductSimpleName() {
    return productSimpleName;
  }
}
