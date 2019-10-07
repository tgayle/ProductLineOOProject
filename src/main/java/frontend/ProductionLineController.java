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
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import misc.SuppressFBWarnings;
import model.ItemType;
import model.Product;
import model.Widget;
import model.audio.AudioPlayer;
import model.production.Production;
import model.production.ProductionWithProduct;
import model.video.MonitorType;
import model.video.MoviePlayer;
import model.video.Screen;

public class ProductionLineController {

  public JFXTextField pdLnProductNameText;
  public JFXTextField pdLnManufacturerText;
  public ChoiceBox<String> pdLnItemTypeCBox;

  public JFXComboBox<String> produceQuantityCBox;
  public JFXListView<Product> produceProductList;
  public Tab productionLogTab;
  public TabPane mainTabPane;

  public TableColumn<ProductionWithProduct, String> columnProductName;
  public TableColumn<ProductionWithProduct, Integer> columnQuantityProduced;
  public TableColumn<ProductionWithProduct, String> columnDateProduced;
  public TableView<ProductionWithProduct> productionLogTable;

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
    prepareProductionLogTab();

    mainTabPane.getSelectionModel().selectedItemProperty().addListener(
        (observableValue, oldTab, newTab) -> {
          System.out.println(newTab.getId());
          switch (newTab.getId()) {
            case "productionLineTab":
              updateProductionLineTab();
              break;
            case "produceTab":
              updateProduceTab();
              break;
            case "productionLogTab":
              updateProductionLogTab();
              break;
          }
        });
  }

  private void prepareProductionLogTab() {
    columnDateProduced.setCellValueFactory(new PropertyValueFactory<>("manufacturedOn"));
    columnProductName.setCellValueFactory(new PropertyValueFactory<>("productSimpleName"));
    columnQuantityProduced.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    updateProductionLogTab();
  }

  private void prepareProduceTab() {
    List<String> numbers = IntStream.range(1, 11).boxed().map(Object::toString)
        .collect(Collectors.toList());
    produceQuantityCBox.getItems().addAll(numbers); // Add 1-10 to ComboBox
    produceQuantityCBox.setEditable(true); // Allow users to enter their own quantities
    produceQuantityCBox.getSelectionModel().selectFirst(); // Select first item in list

    updateProduceTab();
  }

  private void prepareProductionLineTab() {
    for (ItemType type : ItemType.values()) {
      pdLnItemTypeCBox.getItems().add(type.toString());
    }

    pdLnItemTypeCBox.getSelectionModel().selectFirst();
    updateProductionLineTab();
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
    Product product;
    String manufacturer = pdLnManufacturerText.getText();
    String productName = pdLnProductNameText.getText();
    ItemType itemType = ItemType.valueOf(pdLnItemTypeCBox.getValue());

    switch (itemType) {
      case Audio:
        product = new AudioPlayer(productName, manufacturer);
        break;
      case Visual:
        product = new MoviePlayer(productName, manufacturer, new Screen("1920x1080", 60, 2),
            MonitorType.LCD);
        break;
      default:
        product = new Widget(productName, manufacturer, itemType.toString());
    }

    if (product != null) {
      int result = database.insertProduct(product);
      System.out.println(result);
    } else {
      System.out.println("Product was null? Likely an itemtype we haven't setup yet.");
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

    List<Production> productions = produceProductList.getSelectionModel().getSelectedItems()
        .stream()
        .map(item -> {
          return new Production(item.getId(), Integer.parseInt(quantity), LocalDateTime.now());
        }).collect(Collectors.toList());

    database.recordProductions(productions);
    produceProductList.getSelectionModel().clearSelection();
  }

  private void updateProductionLogTab() {
    try {
      List<ProductionWithProduct> allProductions = database.getAllProductionsWithItems();
      productionLogTable.setItems(FXCollections.observableList(allProductions));

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void updateProduceTab() {
    try {
      List<Product> allProducts = database.getAllProducts();
      produceProductList.setItems(FXCollections.observableList(allProducts));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void updateProductionLineTab() {
    // TODO: Show items in database
  }
}
