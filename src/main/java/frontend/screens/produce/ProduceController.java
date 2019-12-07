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
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import misc.SuppressFBWarnings;
import model.Product;

public class ProduceController extends BaseController {

  public static final String controllerId = "Produce";
  private static ProduceController controller;
  @FXML
  private JFXComboBox<String> produceQuantityCBox;
  @FXML
  private JFXListView<Product> produceProductList;
  @FXML
  private HBox produceTabRoot;

  @SuppressFBWarnings(
      value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD",
      justification = STATIC_REFERENCE_JUSTIFICATION)
  public ProduceController() {
    controller = this;
  }

  public static ProduceController get() {
    return controller;
  }

  /**
   * Prepares the Product tab's ComboBox and ProductList.
   */
  public void prepare() {
    List<String> numbers = IntStream.range(1, 11).boxed().map(Object::toString)
        .collect(Collectors.toList());
    produceQuantityCBox.getItems().addAll(numbers); // Add 1-10 to ComboBox
    produceQuantityCBox.setEditable(true); // Allow users to enter their own quantities
    produceQuantityCBox.getSelectionModel().selectFirst(); // Select first item in list

    // User can only produce one product a a time.
    produceProductList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
  }

  /**
   * Updates the values of the tab's ProductList.
   */
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
      database.recordProductions(product, Integer.parseInt(quantity), LocalDateTime.now());
      produceProductList.getSelectionModel().clearSelection();
    }

  }
}
