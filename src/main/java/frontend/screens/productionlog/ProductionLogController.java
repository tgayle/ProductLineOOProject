package frontend.screens.productionlog;

import frontend.BaseController;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.production.ProductionWithProduct;

public class ProductionLogController extends BaseController {

  public static final String controllerId = "ProdLog";

  public TableColumn<ProductionWithProduct, String> columnProductName;
  public TableColumn<ProductionWithProduct, Integer> columnQuantityProduced;
  public TableColumn<ProductionWithProduct, String> columnDateProduced;
  public TableColumn<ProductionWithProduct, String> columnProductionSerialNumber;
  public TableView<ProductionWithProduct> productionLogTable;
  public AnchorPane productionLogRoot;

  private static ProductionLogController controller;

  public static ProductionLogController get() {
    return controller;
  }

  public ProductionLogController() {
    super(controllerId);
    controller = this;
  }

  public void prepare() {
    columnDateProduced.setCellValueFactory(new PropertyValueFactory<>("formattedManufacturedDate"));
    columnProductName.setCellValueFactory(new PropertyValueFactory<>("productSimpleName"));
    columnProductionSerialNumber.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
    columnQuantityProduced.setCellValueFactory(new PropertyValueFactory<>("quantity"));
  }

  public void update() {
    try {
      List<ProductionWithProduct> allProductions = database.getAllProductionsWithItems();
      productionLogTable.setItems(FXCollections.observableList(allProductions));

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
