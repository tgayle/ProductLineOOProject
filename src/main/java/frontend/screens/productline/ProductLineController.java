package frontend.screens.productline;

import com.jfoenix.controls.JFXTextField;
import frontend.BaseController;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import misc.SuppressFBWarnings;
import model.ItemType;
import model.Product;
import model.audio.AudioPlayer;
import model.video.MonitorType;
import model.video.MoviePlayer;
import model.video.Screen;

public class ProductLineController extends BaseController {

  public static final String controllerId = "ProductLine";

  public JFXTextField pdLnProductNameText;
  public JFXTextField pdLnManufacturerText;
  public ChoiceBox<String> pdLnItemTypeCBox;

  public TableColumn<Product, String> productListColumnName;
  public TableColumn<Product, String> productListColumnType;
  public TableColumn<Product, String> productListManufacturer;
  public TableView<Product> productListTable;
  public AnchorPane productLineTabRoot;

  private static ProductLineController controller;

  public static ProductLineController get() {
    return controller;
  }

  public ProductLineController() {
    super(controllerId);
    controller = this;
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
    Product product = null;
    String manufacturer = pdLnManufacturerText.getText();
    String productName = pdLnProductNameText.getText();
    String cboxItemType = pdLnItemTypeCBox.getValue();
    ItemType itemType = ItemType.valueOf(cboxItemType);

    switch (itemType) {
      case Audio:
      case AudioMobile:
        // TODO: Take in audio specification or screen type from user.
        product = new AudioPlayer(productName, manufacturer);
        break;
      case Visual:
      case VisualMobile:
        product = new MoviePlayer(productName, manufacturer, new Screen("1920x1080", 60, 2),
            MonitorType.LCD);
        break;
      default:
        throw new RuntimeException("Unhandled ItemType: " + itemType + " -> " + cboxItemType);
    }

    pdLnManufacturerText.clear();
    pdLnProductNameText.clear();
    int result = database.insertProduct(product);
    update();
    System.out.println(result == 2 ? "Successful insertion!" : "Insertion failure");
  }

  public void prepare() {
    for (ItemType type : ItemType.values()) {
      pdLnItemTypeCBox.getItems().add(type.toString());
    }

    pdLnItemTypeCBox.getSelectionModel().selectFirst();

    productListColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    productListColumnType.setCellValueFactory(new PropertyValueFactory<>("type"));
    productListManufacturer.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
  }

  public void update() {
    // TODO: Show items in database
    try {
      List<Product> allProducts = database.getAllProducts();
      productListTable.setItems(FXCollections.observableList(allProducts));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}