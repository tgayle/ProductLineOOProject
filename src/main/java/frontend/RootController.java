package frontend;

import frontend.screens.employee.EmployeeController;
import frontend.screens.produce.ProduceController;
import frontend.screens.productionlog.ProductionLogController;
import frontend.screens.productline.ProductLineController;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;

public class RootController extends BaseController {

  public static final String controllerId = "rootController";

  @FXML
  private TabPane mainTabPane;
  @FXML
  private Tab productionLineTab;
  @FXML
  private Tab produceTab;
  @FXML
  private Tab productionLogTab;
  @FXML
  private Tab employeeTab;

  @Override
  public void prepare() {
    mainTabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
    System.out.println("Controller started!");
    mainTabPane.getSelectionModel().selectedItemProperty().addListener(
        (observableValue, oldTab, newTab) -> {
          Actionable controller;
          System.out.println("RootController Event! " + newTab.getId());
          switch (newTab.getId()) {
            case "productionLineTab":
              controller = ProductLineController.get();
              break;
            case "produceTab":
              controller = ProduceController.get();
              break;
            case "productionLogTab":
              controller = ProductionLogController.get();
              break;
            case "employeeTab":
              controller = EmployeeController.get();
              break;
            default:
              controller = null;
              System.out
                  .println("New tab selected but not handled elsewhere? Tab: " + newTab.toString());
          }

          if (controller != null) {
            controller.update();
          } else {
            System.out.println("Unsure which tab to update, updating all of them.");
            update();
          }
        });
  }

  @Override
  public void update() {
    ProductLineController.get().update();
    ProduceController.get().update();
    ProductionLogController.get().update();
    EmployeeController.get().update();
  }
}
