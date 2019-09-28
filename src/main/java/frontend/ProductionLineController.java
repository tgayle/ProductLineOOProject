package frontend;

import backend.DatabaseProvider;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import misc.SuppressFBWarnings;

public class ProductionLineController {

  public JFXTextField pdLnProductNameText;
  public JFXTextField pdLnManufacturerText;
  public ChoiceBox<String> pdLnItemTypeCBox;

  public JFXComboBox<Integer> rcPdQuantityCBox;

  private DatabaseProvider database;

  /**
   * Run when this Controller is first created. Populates ComboBoxes and prepares the UI for user
   * interaction.
   */
  @SuppressFBWarnings(
      value = {
          "UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD",
          "NP_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD"
      },
      justification = "These fields are instantiated by the FXMLLoader, which FindBugs does not detect.")
  public void initialize() {
    System.out.println("Controller started!");

    List<String> types = Arrays.asList("Audio", "Visual", "AudioMobile", "VisualMobile");

    pdLnItemTypeCBox.setItems(FXCollections.observableList(types)); //
    pdLnItemTypeCBox.getSelectionModel().selectFirst();

    List<Integer> numbers = IntStream.range(1, 11).boxed().collect(Collectors.toList());
    rcPdQuantityCBox.getItems().addAll(numbers); // Add 1-10 to ComboBox
    rcPdQuantityCBox.getSelectionModel().selectFirst(); // Select first item in list
    rcPdQuantityCBox.setEditable(true); // Allow users to enter their own quantities
  }

  public void setDatabase(DatabaseProvider database) {
    this.database = database;
  }

  /**
   * Triggered when the "Add Product" button is clicked on the Product Line tab. Inserts into the
   * database a new Product with information taken from user input.
   *
   * @param evt Information about the button click event.
   */
  @SuppressFBWarnings(value = {"NP_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD"},
      justification = "These fields are instantiated by the FXMLLoader, which FindBugs does not detect.")
  public void addProductBtnClick(ActionEvent evt) {
    int result = database.insertProduct(
        pdLnItemTypeCBox.getValue(),
        pdLnManufacturerText.getText(),
        pdLnProductNameText.getText()
    );

    System.out.println(result);
  }
}
