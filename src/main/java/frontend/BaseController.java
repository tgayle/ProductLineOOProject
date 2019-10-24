package frontend;

import backend.DatabaseProvider;

public abstract class BaseController implements Actionable {

  protected String id;
  protected DatabaseProvider database = DatabaseProvider.get();

  public BaseController(String id) {
    this.id = id;
  }

  public void initialize() {
    prepare();
    update();
  }

  public abstract void prepare();

  public abstract void update();
}
