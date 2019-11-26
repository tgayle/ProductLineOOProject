package model.employee;

public class Employee {

  private int id;
  private StringBuilder name;
  private String firstName;
  private String lastName;
  private String username;
  private String password;
  private String email;

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
    final String pattern = "Employee Details%n"
        + "Name : %s%n"
        + "Username : %s%n"
        + "Email : %s%n"
        + "Initial Password : %s";

    return String.format(pattern, name.toString(), username, email, password);
  }
}