package model.employee;

public class Employee {

  private int id;
  private StringBuilder name;
  private String firstName;
  private String lastName;
  private String username;
  private String password;
  private String email;

  /**
   * Creates an employee from a full name and password. The full name is parsed and a username and
   * email are generated from it. If the full name isn't valid (doesn't contain a space), then the
   * username and email will be set to defaults. The password is also checked for validity and
   * assigned to a default value if not valid.
   *
   * @param fullName The employee's full name.
   * @param password The employee's desired password.
   */
  public Employee(String fullName, String password) {
    this.name = new StringBuilder(fullName);

    if (checkName(fullName)) {
      firstName = fullName.split(" ")[0];
      lastName = fullName.split(" ")[1];

      setUsername(fullName);
      setEmail(fullName);
    } else {
      username = "default";
      email = "user@oracleacademy.Test";
      firstName = "Default";
      lastName = "User";
    }

    isValidPassword(password);
  }

  /**
   * Generates an employee with an id and all fields pre-determined. This should only be used with
   * data retrieved from a database where this employee was assigned an id and has already received
   * a username and email.
   * @param id The employee's id
   * @param firstName The employee's first name
   * @param lastName The employee's last name
   * @param username The employee's username
   * @param password The employee's password
   * @param email The employee's email
   */
  public Employee(int id, String firstName, String lastName,
      String username, String password, String email) {
    this.id = id;
    this.name = new StringBuilder(firstName + " " + lastName);
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.password = password;
    this.email = email;
  }

  private void setUsername(String fullName) {
    String[] firstAndLastName = fullName.toLowerCase().split(" ");
    username = firstAndLastName[0].charAt(0) + firstAndLastName[1];
  }

  private boolean checkName(String name) {
    return name.contains(" ");
  }

  private void setEmail(String fullName) {
    String[] firstAndLastName = fullName.toLowerCase().split(" ");
    email = firstAndLastName[0] + "." + firstAndLastName[1] + "@oracleacademy.Test";
  }

  private boolean isValidPassword(String password) {
    if (password.matches(".*[A-Z].*[a-z].*[!@#$%^&*()_+-=<>?]")) {
      this.password = password;
      return true;
    }

    this.password = "pw";
    return false;
  }

  public String getEmail() {
    return email;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public int getId() {
    return id;
  }

  public boolean isDefaultUser() {
    return username.equals("default");
  }

  @Override
  public String toString() {
    final String pattern = "Name : %s%n"
        + "Username : %s%n"
        + "Email : %s%n"
        + "Password : %s";

    return String.format(pattern, name.toString(), username, email, password);
  }
}