package frontend.screens.productionlog;

import frontend.BaseController;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import misc.SuppressFBWarnings;
import model.production.ProductionWithProduct;

public class ProductionLogController extends BaseController {

  public static final String controllerId = "ProdLog";
  private static ProductionLogController controller;
  public TableColumn<ProductionWithProduct, String> columnProductName;
  public TableColumn<ProductionWithProduct, Integer> columnQuantityProduced;
  public TableColumn<ProductionWithProduct, String> columnDateProduced;
  public TableColumn<ProductionWithProduct, String> columnProducedBy;
  public TableView<ProductionWithProduct> productionLogTable;

  @SuppressFBWarnings(
      value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD",
      justification = STATIC_REFERENCE_JUSTIFICATION)
  public ProductionLogController() {
    controller = this;
  }

  public static ProductionLogController get() {
    return controller;
  }

  /**
   * Prepares the ProductionLog's TableView.
   */
  public void prepare() {
    columnDateProduced.setCellValueFactory(new PropertyValueFactory<>("formattedManufacturedDate"));
    columnProductName.setCellValueFactory(new PropertyValueFactory<>("productSimpleName"));
    columnQuantityProduced.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    columnProducedBy.setCellValueFactory(new PropertyValueFactory<>("producedByName"));
  }

  /**
   * Updates the Productions of the TableView.
   */
  public void update() {
    try {
      List<ProductionWithProduct> allProductions = database.getAllProductionsWithItems();
      productionLogTable.setItems(FXCollections.observableList(allProductions));

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
