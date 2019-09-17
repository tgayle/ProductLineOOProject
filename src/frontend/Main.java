package frontend;

import backend.DatabaseProvider;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("productionline.fxml"));
    Parent root = loader.load();

    DatabaseProvider db = new DatabaseProvider();
    ProductionLineController controller = loader.getController();
    controller.setDatabase(db);

    db.allItemsFromTable("PRODUCT");

    primaryStage.setTitle("Production Line Management Software");
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }


  public static void main(String[] args) {
    launch(args);
  }
}
