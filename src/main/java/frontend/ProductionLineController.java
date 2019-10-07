package frontend;

import backend.DatabaseProvider;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import misc.SuppressFBWarnings;
import model.ItemType;
import model.Product;
import model.Production;

public class ProductionLineController {

  public JFXTextField pdLnProductNameText;
  public JFXTextField pdLnManufacturerText;
  public ChoiceBox<String> pdLnItemTypeCBox;

  public JFXComboBox<String> produceQuantityCBox;
  public JFXListView<Product> produceProductList;

  private DatabaseProvider database = DatabaseProvider.get();

  /**
   * Run when this Controller is first created. Populates ComboBoxes and prepares the UI for user
   * interaction.
   */
  @SuppressFBWarnings(
      value = {
          "UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD",
          "NP_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD"
      },
      justification
          = "These fields are instantiated by the FXMLLoader, which FindBugs does not detect.")
  public void initialize() {
    System.out.println("Controller started!");

    prepareProductionLineTab();
    prepareProduceTab();
  }

  private void prepareProduceTab() {
    List<String> numbers = IntStream.range(1, 11).boxed().map(Object::toString)
        .collect(Collectors.toList());
    produceQuantityCBox.getItems().addAll(numbers); // Add 1-10 to ComboBox
    produceQuantityCBox.setEditable(true); // Allow users to enter their own quantities
    produceQuantityCBox.getSelectionModel().selectFirst(); // Select first item in list

    try {
      List<Product> allProducts = database.getAllProducts();
      produceProductList.getItems().addAll(allProducts);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void prepareProductionLineTab() {
    for (ItemType type : ItemType.values()) {
      pdLnItemTypeCBox.getItems().add(type.toString());
    }

    pdLnItemTypeCBox.getSelectionModel().selectFirst();
  }

  /**
   * Triggered when the "Add Product" button is clicked on the Product Line tab. Inserts into the
   * database a new Product with information taken from user input.
   *
   * @param evt Information about the button click event.
   */
  @SuppressFBWarnings(value = {"NP_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD"},
      justification
          = "These fields are instantiated by the FXMLLoader, which FindBugs does not detect.")
  public void addProductBtnClick(ActionEvent evt) {
    int result = database.insertProduct(
        pdLnItemTypeCBox.getValue(),
        pdLnManufacturerText.getText(),
        pdLnProductNameText.getText()
    );

    System.out.println(result);
  }

  /**
   * Called when the "Record" button is pressed on the Produce screen and submits a list of products
   * based on what the user has selected.
   *
   * @param actionEvent Information about the action around the button click.
   */
  public void produceRecordBtnClick(ActionEvent actionEvent) {
    String quantity = produceQuantityCBox.getValue();

    List<Production> productions = produceProductList.getSelectionModel().getSelectedItems()
        .stream()
        .map(item -> {
          return new Production(item.getId(), Integer.parseInt(quantity), LocalDateTime.now());
        }).collect(Collectors.toList());

    database.recordProductions(productions);
  }
}
