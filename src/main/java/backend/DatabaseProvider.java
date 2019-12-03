package backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.ItemType;
import model.Product;
import model.Widget;
import model.employee.Employee;
import model.production.Production;
import model.production.ProductionWithProduct;

public class DatabaseProvider {

  /**
   * The path to our database. When run from IntelliJ, the starting directory is the root of the
   * project.
   */
  private static final String DB_PATH = "jdbc:h2:" + "./src/main/resources/db/ProductionLineDB";
  private static DatabaseProvider dbInstance;
  private Employee currentEmployee;
  private Connection connection;

  private DatabaseProvider() {
    Alert alertWindow = new Alert(AlertType.ERROR);
    File databaseAuthFile = new File("./src/main/resources/db/DATABASE_LOGIN");
    if (!databaseAuthFile.exists()) {
      String message = "No database password was found (DATABASE_LOGIN)";
      alertWindow.setContentText(message);
      alertWindow.show();
      throw new RuntimeException(message);
    }

    try (Scanner loginReader = new Scanner(databaseAuthFile, "utf-8")) {
      String username = loginReader.nextLine();
      String encryptedPassword = loginReader.nextLine();
      String decryptedPassword = new StringBuilder(encryptedPassword).reverse().toString();

      Class.forName("org.h2.Driver");
      connection = DriverManager.getConnection(DB_PATH, username, decryptedPassword);
      System.out.println("Password successfully retrieved and 'decrypted.'");

    } catch (FileNotFoundException e) {
      alertWindow.setContentText("DATABASE_LOGIN file is missing! "
          + "Please return this file to it's proper location for running this application.");
      alertWindow.show();

      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      alertWindow.setContentText("The H2 Database driver is missing! "
          + "Please return this file to it's proper location for running this application, or "
          + "ensure Maven is properly setup to include it.");
      e.printStackTrace();
    } catch (SQLException e) {
      alertWindow.setContentText("There was an issue connecting to the database!");
      e.printStackTrace();
    }

  }

  /**
   * Returns an instance of the database provider if one already exists, otherwise create one then
   * return it.
   */
  public static synchronized DatabaseProvider get() {
    if (dbInstance == null) {
      try {
        dbInstance = new DatabaseProvider();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return dbInstance;
  }

  /**
   * Returns a list of the names of the columns for a table given a ResultSet.
   *
   * @param rs The ResultSet from which columns should be retrieved.
   * @return A List with the names of each column in the ResultSet/table
   * @throws SQLException when retrieving ResultSet metadata results in an error.
   */
  private static List<String> getColumnNames(ResultSet rs) throws SQLException {
    List<String> names = new ArrayList<>();
    if (rs != null) {
      //create an object based on the Metadata of the result set
      ResultSetMetaData rsMetaData = rs.getMetaData();
      //get and print the column names, column indexes start from 1
      for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
        String columnName = rsMetaData.getColumnName(i);
        names.add(columnName);
      }
    }
    return names;
  }

  /**
   * Prints out the columns of a given table followed by all the items in that table.
   *
   * @param tableName The name of the table whose items should be retrieved
   * @throws SQLException when the executed query results in an error.
   */
  public void allItemsFromTable(String tableName) throws SQLException {
    Statement stmt = connection.createStatement();
    ResultSet result = stmt.executeQuery("SELECT * FROM " + tableName);

    List<String> columnNames = getColumnNames(result);

    for (String col : columnNames) {
      System.out.print(col + "\t");
    }
    System.out.println();

    while (result.next()) {
      for (int i = 1; i <= columnNames.size(); i++) {
        System.out.print(result.getString(i) + "\t");
      }
      System.out.println();
    }

    stmt.close();
  }

  public void close() throws SQLException {
    connection.close();
  }

  /**
   * Adds a product to the database.
   *
   * @param product The product to be inserted
   * @return 1 if a product was updated, 2 if a new product was created, -1 if there was an error.
   */
  public int insertProduct(Product product) {
    String insertProductQuery = "INSERT INTO "
        + "`Product`(type, manufacturer, name, serialnum) VALUES (?, ?, ?, ?)";

    try (PreparedStatement preparedInsert = connection.prepareStatement(insertProductQuery)) {
      int count = getProductItemTypeCount(product.getItemType());
      product.generateSerialNumber(count + 1);
      preparedInsert.setString(1, product.getItemType().getCode());
      preparedInsert.setString(2, product.getManufacturer());
      preparedInsert.setString(3, product.getName());
      preparedInsert.setString(4, product.getSerialNumber());

      return preparedInsert.execute() ? 1 : 2;
    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
    }
  }

  /**
   * Gets a list of all products storied in the database.
   *
   * @return A list of products
   * @throws SQLException if there is an issue with communicating with the database.
   */
  public List<Product> getAllProducts() throws SQLException {
    String allQuery = "SELECT * FROM PRODUCT";
    List<Product> products = new ArrayList<>();
    try (Statement stmt = connection.createStatement()) {
      ResultSet rows = stmt.executeQuery(allQuery);

      while (rows.next()) {
        int id = rows.getInt(1);
        String name = rows.getString(2);
        String type = rows.getString(3);
        String manufacturer = rows.getString(4);
        String serialNumber = rows.getString(5);

        Product thisProduct = new Widget(id, name, ItemType.fromCode(type), manufacturer);
        thisProduct.setSerialNumber(serialNumber);
        products.add(thisProduct);
      }
    }
    return products;
  }

  /**
   * Stores a given list of production records into the database.
   *
   * @param productions A list of production records to be stored.
   */
  public void recordProductions(List<Production> productions) {
    try {
      verifyEmployee();
      connection.setAutoCommit(false);
      String insertionQuery =
          "INSERT INTO PRODUCTIONRECORD "
              + "(PRODUCT_ID, QUANTITY, DATE_PRODUCED, EMPLOYEE_ID) VALUES (?, ?, ?, ?)";

      try (PreparedStatement stmnt = connection.prepareStatement(insertionQuery)) {
        for (Production production : productions) {
          stmnt.setInt(1, production.getProductId());
          stmnt.setInt(2, production.getQuantity());
          stmnt.setTimestamp(3, Timestamp.valueOf(production.getManufacturedOn()));
          stmnt.setInt(4, currentEmployee.getId());
          stmnt.execute();
        }
      }
      connection.commit();
      System.out.println(productions.size() + " productions were recorded.");

    } catch (SQLException e) {
      System.out.println("There was an issue recording productions.");
      e.printStackTrace();
    } finally {
      try {
        connection.setAutoCommit(true);
      } catch (SQLException e) {
        System.out.println("There was an issue reenabling auto-commit.");
        e.printStackTrace();
      }
    }
  }

  private void verifyEmployee() {
    if (currentEmployee == null) {
      currentEmployee = getDefaultEmployee();
    }
  }

  private Employee getDefaultEmployee() {
    try (Statement stmt = connection.createStatement()) {
      ResultSet resultSet = stmt.executeQuery("SELECT * FROM Employee WHERE username='default'");

      resultSet.next();
      return getEmployeeFromResultRow(resultSet);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Returns a list of all productions from the databse.
   *
   * @return a list of productions
   * @throws SQLException if there is an error communicating with the database.
   */
  public List<Production> getAllProductions() throws SQLException {
    String allQuery = "SELECT * FROM PRODUCTIONRECORD";
    List<Production> productions = new ArrayList<>();
    try (Statement stmt = connection.createStatement()) {
      ResultSet rows = stmt.executeQuery(allQuery);

      while (rows.next()) {
        int productionId = rows.getInt(1);
        int productId = rows.getInt(2);
        int quantity = rows.getInt(3);
        Timestamp dateProduced = rows.getTimestamp(4);
        productions
            .add(new Production(productionId, productId, quantity, dateProduced.toLocalDateTime()));
      }
    }
    return productions;
  }

  /**
   * Returns a list of productions with their associated items.
   *
   * @return a list of productions and the items produced.
   * @throws SQLException if there is an issue communicating with the database.
   */
  public List<ProductionWithProduct> getAllProductionsWithItems() throws SQLException {
    final String getProductsWithProductQuery =
        "SELECT PR.PRODUCTION_ID, PR.PRODUCT_ID, PR.QUANTITY, PR.DATE_PRODUCED,"
            + " P.NAME, P.MANUFACTURER, P.TYPE, P.SERIALNUM, PR.EMPLOYEE_ID\n"
            + "FROM PRODUCTIONRECORD PR\n"
            + "JOIN PRODUCT P on (PR.PRODUCT_ID=P.ID)";

    List<ProductionWithProduct> productions = new ArrayList<>();

    Statement stmt = connection.createStatement();
    ResultSet rows = stmt.executeQuery(getProductsWithProductQuery);

    while (rows.next()) {
      int productionId = rows.getInt(1);
      int productId = rows.getInt(2);
      int quantity = rows.getInt(3);
      Timestamp dateProduced = rows.getTimestamp(4);
      String productName = rows.getString(5);
      String manufacturer = rows.getString(6);
      String productType = rows.getString(7);
      String serialNumber = rows.getString(8);
      int employeeId = rows.getInt(9);


      Product productProduced = new Widget(productId, productName, ItemType.fromCode(productType),
          manufacturer);

      productProduced.setSerialNumber(serialNumber);

      productions.add(
          new ProductionWithProduct(productionId, quantity, serialNumber,
              dateProduced.toLocalDateTime(),
              productProduced, getEmployeeById(employeeId))
      );
    }
    rows.close();
    stmt.close();
    return productions;
  }

  /**
   * Gets the number of products currently stored with the given type.
   *
   * @param type The ItemType to count
   * @return the number of items with the given type
   */
  public int getProductItemTypeCount(ItemType type) throws SQLException {
    PreparedStatement query = connection
        .prepareStatement("SELECT COUNT(*) FROM PRODUCT WHERE SERIALNUM LIKE ?");
    query.setString(1, "%" + type.getCode() + "%");
    ResultSet resultSet = query.executeQuery();
    resultSet.next();
    int count = resultSet.getInt(1);

    resultSet.close();
    query.close();
    return count;
  }

  public void recordProduction(Production... productions) {
    recordProductions(Arrays.asList(productions));
  }

  /**
   * Attempts to add a user to the database and log in as that user. If the user's username does not
   * already exists in the database, then a new employee is added to the database, then we are
   * logged in as the user.
   *
   * @param employee The employee to try registering and logging in as.
   * @return The employee we were logged in as.
   */
  public Employee registerEmployee(Employee employee) {
    if (!doesUserExist(employee.getUsername())) {
      insertEmployee(employee);
    }

    // This username didn't previously exist, so we should store this user.
    loginEmployee(employee.getUsername(), employee.getPassword());

    return employee;
  }

  /**
   * Returns an employee given their id.
   * @param id The id of the employee.
   * @return The employee with the given id, or null if this user does not exist.
   */
  public Employee getEmployeeById(int id) {
    try (PreparedStatement stmnt = connection
        .prepareStatement("SELECT * FROM Employee WHERE id=?")) {
      stmnt.setInt(1, id);

      ResultSet result = stmnt.executeQuery();

      if (result.next()) {
        return getEmployeeFromResultRow(result);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Returns true if the given username exists in the database.
   * @param username The username to search the database for.
   * @return True if the username is present in the database, else fa;se/
   */
  public boolean doesUserExist(String username) {
    String userExistsQuery = "SELECT COUNT(*) FROM Employee WHERE username=?";

    try (PreparedStatement stmnt = connection.prepareStatement(userExistsQuery)) {
      stmnt.setString(1, username);
      ResultSet result = stmnt.executeQuery();
      result.next();
      int count = result.getInt(1);
      return count > 0;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Attempts to login as a user with a given username and password. If the given username and
   * password match those for an employee in the database, then the login will be successful and
   * the located employee will be stored as the current employee.
   * @param username The username to attempt logging in with.
   * @param password The password to attempt logging in with.
   * @return The employee that has become the current user if the username and password are valid,
   *         else false.
   */
  public Employee loginEmployee(String username, String password) {
    String fetchUserQuery = "SELECT * FROM Employee WHERE username=? AND password=? LIMIT 1";

    try (PreparedStatement statement = connection.prepareStatement(fetchUserQuery)) {
      statement.setString(1, username);
      statement.setString(2, password);

      ResultSet result = statement.executeQuery();

      // This username and password is valid.
      if (result.next()) {
        currentEmployee = getEmployeeFromResultRow(result);
        return currentEmployee;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Creates an employee from a row in a ResultSet.
   * @param result A result set containing all columns of the employee table.
   * @return The employee from the columns of the current row in the result set.
   * @throws SQLException if there is an issue reading from the database.
   */
  private Employee getEmployeeFromResultRow(ResultSet result) throws SQLException {
    int employeeId = result.getInt(1);
    String employeeFirstName = result.getString(2);
    String employeeLastName = result.getString(3);
    String employeeEmail = result.getString(4);
    String employeeUsername = result.getString(5);
    String employeePassword = result.getString(6);
    Employee employee = new Employee(
        employeeId, employeeFirstName, employeeLastName,
        employeeUsername, employeePassword, employeeEmail);

    return employee;
  }

  /**
   * Adds the given employee to the database.
   * @param employee The employee to insert.
   * @return True if the insertion was successful, else false.
   */
  public boolean insertEmployee(Employee employee) {
    String insertQuery = "INSERT INTO Employee "
        + "(FIRSTNAME, LASTNAME, EMAIL, USERNAME, PASSWORD) VALUES (?, ?, ?, ?, ?) ";

    try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
      statement.setString(1, employee.getFirstName());
      statement.setString(2, employee.getLastName());
      statement.setString(3, employee.getEmail());
      statement.setString(4, employee.getUsername());
      statement.setString(5, employee.getPassword());
      statement.executeUpdate();
      return true;

    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public Employee getCurrentEmployee() {
    return currentEmployee;
  }

  public void logoutEmployee() {
    currentEmployee = getDefaultEmployee();
  }

  public List<Employee> getAllUsers() {
    List<Employee> employees = new ArrayList<>();

    try (ResultSet set = connection.createStatement().executeQuery("SELECT * FROM EMPLOYEE")) {
      while (set.next()) {
        employees.add(getEmployeeFromResultRow(set));
      }
    } catch (SQLException e) {
      System.err.println("There was an issue getting users: " + e.getMessage());
    }

    return employees;
  }
}
