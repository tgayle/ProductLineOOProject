package model.employee;

import java.util.Scanner;

public class Employee implements Comparable {

  private StringBuilder name;
  private String code;

  public Employee() {
    setName();
  }

  public StringBuilder getName() {
    return name;
  }

  public String getCode() {
    return code;
  }

  /**
   * Prepares the user's code from their full name.
   */
  private void setName() {
    String fullName = inputName().trim();
    name = new StringBuilder(fullName);

    if (checkName(name)) {
      createEmployeeCode(name);
    } else {
      code = "guest";
    }
  }

  /**
   * Generates an employee code, which is equal to the user's first initial and their last name.
   *
   * @param name The name a code will be generated from.
   */
  private void createEmployeeCode(StringBuilder name) {
    // Grabs the first character of the string then grabs everything after the first space.
    code = name.charAt(0) + name.substring(name.indexOf(" ") + 1);
  }

  private String inputName() {
    System.out.println("Enter your full name: ");
    return new Scanner(System.in).nextLine();
  }

  /**
   * Returns true if the supplied name contains a space.
   *
   * @param name The name or input string to scan.
   * @return true if a space is in the given string, else false.
   */
  private boolean checkName(StringBuilder name) {
    return name.indexOf(" ") != -1;
  }

  @Override
  public int compareTo(Object o) {
    if (o instanceof Employee) {
      Employee objectAsEmployee = (Employee) o;
      return this.name.toString().compareTo(objectAsEmployee.name.toString());
    }

    return 0;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object instanceof Employee) {
      Employee objAsEmployee = (Employee) object;
      return this.getCode().equals(objAsEmployee.code) &&
          this.getName().equals(objAsEmployee.name);
    }
    return false;
  }
}