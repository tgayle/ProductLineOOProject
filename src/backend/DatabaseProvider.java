package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseProvider implements IProductionDatabaseProvider {

  private Connection connection;
  /**
   * The path to our database. When run from IntelliJ, the starting directory is the root of the
   * project.
   */
  private static final String DB_PATH = "jdbc:h2:" + "./res/ProductionLineDB";

  public DatabaseProvider() throws SQLException, ClassNotFoundException {
    Class.forName("org.h2.Driver");
    connection = DriverManager.getConnection(DB_PATH);
  }

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
}
