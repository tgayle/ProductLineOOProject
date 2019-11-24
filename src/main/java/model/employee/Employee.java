package model.employee;

import java.util.regex.Pattern;

public class Employee {
  private StringBuilder name;
  private String username;
  private String password;
  private String email;

  private static final Pattern lowercasePattern = Pattern.compile("[a-z]");
  private static final Pattern uppercasePattern = Pattern.compile("[A-Z]");
  private static final Pattern specialCharPattern = Pattern.compile("\\W");

  public Employee(String fullName, String password) {
    this.name = new StringBuilder(fullName);

    if (checkName(fullName)) {
      setUsername(fullName);
      setEmail(fullName);
    } else {
      username = "default";
      email = "user@oracleacademy.Test";
    }

    isValidPassword(password);
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
    boolean containsLower = lowercasePattern.matcher(password).find();
    boolean containsUpper = uppercasePattern.matcher(password).find();
    boolean containsSpecial = specialCharPattern.matcher(password).find();

    if (containsLower && containsUpper && containsSpecial) {
      this.password = password;
    }

    this.password = "pw";
    return false;
  }

  @Override
  public String toString() {
    final String pattern = "Employee Details\n"
        + "Name : %s\n"
        + "Username : %s\n"
        + "Email : %s\n"
        + "Initial Password : %s";

    return String.format(pattern, name.toString(), username, email, password);
  }
}