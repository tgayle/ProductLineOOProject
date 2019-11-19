package frontend;

import backend.DatabaseProvider;

/**
 * An abstract class implemented by all Controllers to simplify the handling and setup of scenes.
 */
public abstract class BaseController implements Actionable {

  protected String id;
  protected DatabaseProvider database = DatabaseProvider.get();

  public BaseController(String id) {
    this.id = id;
  }

  /**
   * The standard initialize function that is directly called from JavaFX when a controller is set.
   * Prepare will be called to give subclasses the opportunity to prepare their views, then update()
   * will be called to retrieve and fill any relevant data for the view.
   */
  public void initialize() {
    prepare();
    update();
  }

  /**
   * Prepares views and fields of a scene. This might include preparing a TableView's columns,
   * adding click listeners, filling ComboBoxes, etc.
   */
  public abstract void prepare();

  /**
   * Updates the data of the scene. The scene/controller should already have been prepared and setup
   * at this point, so the view can be immediately updated with new values, such as fetching
   * information from a database.
   */
  public abstract void update();
}
