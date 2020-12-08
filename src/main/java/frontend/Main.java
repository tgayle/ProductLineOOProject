package frontend;

import backend.DatabaseProvider;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Primary launcher and entry point into this product line management application.
 */
public class Main extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {	
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getClassLoader().getResource("ui/productionline.fxml"));
    final Parent root = loader.load(); // Load view early so that Controller is instantiated

    DatabaseProvider db = DatabaseProvider.get();
    db.allItemsFromTable("PRODUCT");

    primaryStage.setTitle("Production Line Management Software");
    Scene scene = new Scene(root);
    scene.getStylesheets()
        .add(getClass().getClassLoader().getResource("ui/ProductionLine.css").toExternalForm());

    primaryStage.setScene(scene);
    primaryStage.show();

  }
}
