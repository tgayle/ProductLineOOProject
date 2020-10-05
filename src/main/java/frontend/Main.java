package frontend;

import backend.DatabaseProvider;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.MediaProduct;
import model.audio.AudioPlayer;
import model.mediabehavior.audiobehavior.AudioPlay;
import model.mediabehavior.audiobehavior.AudioStop;
import model.mediabehavior.moviebehavior.MovieStop;
import model.video.MonitorType;
import model.video.MoviePlayer;

/**
 * Primary launcher and entry point into this product line management application.
 */
public class Main extends Application {
	
  public static boolean DEBUG_MODE = true;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
	if(DEBUG_MODE) {
		MediaProduct testProd = new MoviePlayer("david", "davidcorp", null, MonitorType.LCD);
		testProd.play();
		//we want the play behavior of testProd to change
		testProd.setPlay(new AudioPlay());
		testProd.play();
		MediaProduct testProd2 = new AudioPlayer("aplayer", "corp");
		testProd2.setStop(new MovieStop());
		testProd2.stop();
		testProd2.setStop(new AudioStop());
		testProd2.stop();
	}
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
