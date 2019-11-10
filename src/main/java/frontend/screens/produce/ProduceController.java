package frontend.screens.produce;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import frontend.BaseController;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import model.Product;
import model.production.ProductionWithProduct;

public class ProduceController extends BaseController {

  public static final String controllerId = "Produce";
  private static ProduceController controller;
  public JFXComboBox<String> produceQuantityCBox;
  public JFXListView<Product> produceProductList;
  public AnchorPane produceTabRoot;

  public ProduceController() {
    super(controllerId);
    controller = this;
  }

  public static ProduceController get() {
    return controller;
  }

  public void prepare() {
    List<String> numbers = IntStream.range(1, 11).boxed().map(Object::toString)
        .collect(Collectors.toList());
    produceQuantityCBox.getItems().addAll(numbers); // Add 1-10 to ComboBox
    produceQuantityCBox.setEditable(true); // Allow users to enter their own quantities
    produceQuantityCBox.getSelectionModel().selectFirst(); // Select first item in list

    // User can only produce one product a a time.
    produceProductList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
  }

  public void update() {
    try {
      List<Product> allProducts = database.getAllProducts();
      produceProductList.setItems(FXCollections.observableList(allProducts));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Called when the "Record" button is pressed on the Produce screen and submits a list of products
   * based on what the user has selected.
   *
   * @param actionEvent Information about the action around the button click.
   */
  public void produceRecordBtnClick(ActionEvent actionEvent) {
    String quantity = produceQuantityCBox.getValue();
    Product product = produceProductList.getSelectionModel().getSelectedItem();

    if (product != null) {
      ProductionWithProduct production = new ProductionWithProduct(product,
          Integer.parseInt(quantity), LocalDateTime.now());

      try {
        int count = database.getProductItemTypeCount(product.getItemType());
        production.generateSerialNumber(count + 1);
        database.recordProduction(production);
        produceProductList.getSelectionModel().clearSelection();
      } catch (SQLException e) {
        System.out
            .println(
                "There was an issue recording the production. (Error generating serial number)");
        e.printStackTrace();
      }
    }

  }
}
