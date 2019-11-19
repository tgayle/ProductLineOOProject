package examples;

import model.employee.Employee;

/**
 * A driver class for testing Employees.
 *
 * @author Travis Gayle
 */
public class TestEmployee {

  /**
   * The main entry point for this class.
   * @param args Optional string arguments -- no use for this example.
   */
  public static void main(String[] args) {
    Employee employee = new Employee();

    System.out.println("Employee: " + employee.getName().toString());
    System.out.println("Employee Code: " + employee.getCode());
  }
}
