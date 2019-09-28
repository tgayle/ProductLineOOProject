package frontend;

import backend.DatabaseProvider;
import javafx.scene.input.MouseEvent;

public class ProductionLineController {

  private DatabaseProvider database;

  public void initialize() {
    System.out.println("Controller started!");
  }

  public void setDatabase(DatabaseProvider database) {
    this.database = database;
  }

  public void prodLineBtnClicked() {
    System.out.println("Hello from the production line!");
  }

  public void recordBtnClicked() {
    System.out.println("Hello from record production!");
  }

  public void prodLabBtnClicked() {
    System.out.println("Hello from the production lab!");
  }

  public void addProductBtnClick(MouseEvent mouseEvent) {
    System.out.println("Hello from product line button!");
  }
}
