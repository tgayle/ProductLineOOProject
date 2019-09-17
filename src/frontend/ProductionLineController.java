package frontend;

import backend.DatabaseProvider;

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
}
