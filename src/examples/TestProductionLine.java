package examples;

import model.employee.Employee;

public class TestProductionLine {

  public static void main(String[] args) {
    Employee employee = new Employee();

    System.out.println("Employee: " + employee.getName().toString());
    System.out.println("Employee Code: " + employee.getCode());
  }
}
