package frontend.screens.employee;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXSnackbarLayout;
import com.jfoenix.controls.JFXTextField;
import frontend.BaseController;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

    Employee registeredEmployee = database.registerEmployee(new Employee(fullName, password));
    String successfulSignupMessage =
        "Hello " + registeredEmployee.getFirstName() + " " + registeredEmployee.getLastName() + "!";
    String failureMessage = "Invalid signup input. Proceeding as default user.";

    snackbar.enqueue(
        new SnackbarEvent(
            new JFXSnackbarLayout(
                registeredEmployee.isDefaultUser() ? failureMessage : successfulSignupMessage)
        )
    );

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
   * and out nodes individually.
   */
  public static void crossfadeViews(Node in, Node out, Pane parent, double duration) {
    FadeTransition t = new FadeTransition(Duration.millis(duration), out);
    t.setFromValue(1);
    t.setToValue(0);
    t.setOnFinished(e -> {
      parent.getChildren().remove(out);
      in.setOpacity(0);
      parent.getChildren().add(in);
      FadeTransition fade = new FadeTransition(Duration.millis(duration), in);
      fade.setFromValue(0);
      fade.setToValue(1);
      fade.play();
    });
    t.play();
  }

  @Override
  public void update() {
    populateWelcomeView(database.getCurrentEmployee());
  }
}
