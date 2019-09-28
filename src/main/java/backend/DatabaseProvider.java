package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseProvider {

  private Connection connection;
  /**
   * The path to our database. When run from IntelliJ, the starting directory is the root of the
   * project.
   */
  private static final String DB_PATH = "jdbc:h2:" + "./src/main/resources/db/ProductionLineDB";

  public DatabaseProvider() throws SQLException, ClassNotFoundException {
    Class.forName("org.h2.Driver");
    connection = DriverManager.getConnection(DB_PATH);
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

  /**
   * Returns a list of the names of the columns for a table given a ResultSet.
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

  public void close() throws SQLException {
    connection.close();
  }

  /**
   * Adds a product to the database.
   *
   * @param type The type of the product
   * @param manufacturer The product manufacturer
   * @param name The product's name
   * @return 1 if a product was updated, 2 if a new product was created, -1 if there was an error.
   */
  public int insertProduct(String type, String manufacturer, String name) {
    String insertProductQuery = "INSERT INTO `Product`(type, manufacturer, name) VALUES (?, ?, ?)";

    try (PreparedStatement preparedInsert = connection.prepareStatement(insertProductQuery)) {
      preparedInsert.setString(1, type);
      preparedInsert.setString(2, manufacturer);
      preparedInsert.setString(3, name);
      return preparedInsert.execute() ? 1 : 2;
    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
    }
  }
}
