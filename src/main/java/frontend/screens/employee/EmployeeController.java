package frontend.screens.employee;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXSnackbarLayout;
import com.jfoenix.controls.JFXTextField;
import frontend.BaseController;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import misc.SuppressFBWarnings;
import model.employee.Employee;

public class EmployeeController extends BaseController {

  private static EmployeeController controller;
  @FXML
  private VBox loginContainer;
  @FXML
  private JFXTextField loginUsername;
  @FXML
  private JFXPasswordField loginPassword;
  @FXML
  private JFXButton loginSignInBtn;
  @FXML
  private JFXButton loginSignUpBtn;
  @FXML
  private JFXButton loginDefaultUser;

  @FXML
  private VBox welcomeUserContainer;
  @FXML
  private Text loggedInAsText;
  @FXML
  private Text welcomeUsername;
  @FXML
  private Text welcomeUserPassword;
  @FXML
  private Text welcomeEmail;
  @FXML
  private JFXButton welcomeLogoutButton;
  @FXML
  private ListView<Employee> usersListView;

  @FXML
  private VBox signupContainer;
  @FXML
  private JFXPasswordField signupPassword;
  @FXML
  private JFXButton signupBackBtn;
  @FXML
  private JFXButton signupSignUpBtn;
  @FXML
  private HBox employeeRoot;
  @FXML
  private StackPane employeeStackRoot;
  @FXML
  private JFXTextField signupFullName;

  private JFXSnackbar snackbar;


  @SuppressFBWarnings(
      value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD",
      justification = STATIC_REFERENCE_JUSTIFICATION)
  public EmployeeController() {
    controller = this;
  }

  public static EmployeeController get() {
    return controller;
  }

  @Override
  public void prepare() {
    snackbar = new JFXSnackbar(employeeRoot);
    employeeStackRoot.getChildren().remove(signupContainer);
    employeeStackRoot.getChildren().remove(welcomeUserContainer);

    loginSignInBtn.setOnAction(e -> signInUser());
    signupSignUpBtn.setOnAction(e -> signupUser());

    loginSignUpBtn.setOnAction(e -> {
      crossfadeViews(signupContainer, loginContainer, employeeStackRoot, 300);
    });

    loginDefaultUser.setOnAction(e -> {
      database.switchToDefaultUser();
      populateWelcomeView(database.getCurrentEmployee());
      crossfadeViews(welcomeUserContainer, loginContainer, employeeStackRoot, 300);
    });

    signupBackBtn.setOnAction(e -> {
      crossfadeViews(loginContainer, signupContainer, employeeStackRoot, 300);
    });

    welcomeLogoutButton.setOnAction(e -> {
      database.logoutEmployee();
      crossfadeViews(loginContainer, welcomeUserContainer, employeeStackRoot, 300);
    });
  }

  /**
   * Attempts to register a new user with the information provided in text fields. If the
   * information provided to create a new user results in the current user being the default user,
   * then we display a failure message and continue onwards.
   */
  public void signupUser() {
    String password = signupPassword.getText();
    String fullName = signupFullName.getText();

    String errorMessage = null;

    if (!fullName.contains(" ")) {
      errorMessage = "Your name must contain a space!";
    } else {
      errorMessage = Employee.checkPassword(password);
    }

    if (errorMessage != null) {
      snackbar.enqueue(new SnackbarEvent(new JFXSnackbarLayout(errorMessage)));
      return;
    }

    Employee registeredEmployee = database.registerEmployee(new Employee(fullName, password));
    String successfulSignupMessage =
        "Hello " + registeredEmployee.getFirstName() + " " + registeredEmployee.getLastName() + "!";
    snackbar.enqueue(new SnackbarEvent(new JFXSnackbarLayout(successfulSignupMessage)));

    crossfadeViews(welcomeUserContainer, signupContainer, employeeStackRoot, 300);
    populateWelcomeView(registeredEmployee);
  }

  /**
   * Attempts to sign in as a user given the entered username and password. If the username and
   * password aren't valid, we proceed as the default user.
   */
  public void signInUser() {
    String username = loginUsername.getText();
    String password = loginPassword.getText();

    Employee currentEmployee = database.loginEmployee(username, password);

    if (currentEmployee != null) {
      // Navigate elsewhere.
      crossfadeViews(welcomeUserContainer, loginContainer, employeeStackRoot, 150);
      populateWelcomeView(currentEmployee);

    } else {
      snackbar.enqueue(new SnackbarEvent(new JFXSnackbarLayout("Invalid username or password!")));
    }
  }

  /**
   * Populates the welcome window with information about the given employee and clears the input
   * fields from previous windows.
   *
   * @param employee The employee that the view should be populated with.
   */
  void populateWelcomeView(Employee employee) {
    signupPassword.clear();
    signupFullName.clear();
    loginUsername.clear();
    loginPassword.clear();

    if (employee == null) {
      return;
    }

    loggedInAsText.setText(
        "Currently logged in as " + employee.getFirstName() + " " + employee.getLastName());
    welcomeUsername.setText(employee.getUsername());
    welcomeUserPassword.setText(employee.getPassword());
    welcomeEmail.setText(employee.getEmail());
  }

  /**
   * Animates between two windows, fading away the first in fading in the second.
   *
   * @param in The window that should be displayed
   * @param out The window to be removed
   * @param parent The parent pane for both of these nodes
   * @param duration The length of time, in milliseconds that should be spent on animating the in
   *     and out nodes individually.
   */
  public static void crossfadeViews(Node in, Node out, Pane parent, double duration) {
    FadeTransition fadeOut = new FadeTransition(Duration.millis(duration), out);
    fadeOut.setFromValue(1);
    fadeOut.setToValue(0);
    fadeOut.setOnFinished(e -> {
      parent.getChildren().remove(out);
      in.setOpacity(0);
      parent.getChildren().add(in);
      FadeTransition fadeIn = new FadeTransition(Duration.millis(duration), in);
      fadeIn.setFromValue(0);
      fadeIn.setToValue(1);
      fadeIn.play();
    });
    fadeOut.play();
  }

  @Override
  public void update() {
    populateWelcomeView(database.getCurrentEmployee());
    usersListView.setItems(FXCollections.observableList(database.getAllUsers()));
  }

  @FXML
  private void loginUsernameKeyReleased(KeyEvent keyEvent) {
    if (keyEvent.getCode() == KeyCode.ENTER) {
      loginPassword.requestFocus();
    }
  }

  @FXML
  private void loginPasswordKeyReleased(KeyEvent keyEvent) {
    if (keyEvent.getCode() == KeyCode.ENTER) {
      signInUser();
    }
  }

  @FXML
  private void signupUsernameKeyReleased(KeyEvent keyEvent) {
    if (keyEvent.getCode() == KeyCode.ENTER) {
      signupPassword.requestFocus();
    }
  }

  @FXML
  private void signupPasswordKeyReleased(KeyEvent keyEvent) {
    if (keyEvent.getCode() == KeyCode.ENTER) {
      signupUser();
    }
  }
}
