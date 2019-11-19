package backend;

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
import model.ItemType;
import model.Product;
import model.Widget;
import model.production.Production;
import model.production.ProductionWithProduct;

public class DatabaseProvider {

  /**
   * The path to our database. When run from IntelliJ, the starting directory is the root of the
   * project.
   */
  private static final String DB_PATH = "jdbc:h2:" + "./src/main/resources/db/ProductionLineDB";
  private static DatabaseProvider dbInstance;
  private Connection connection;

  private DatabaseProvider() throws SQLException, ClassNotFoundException {
    Class.forName("org.h2.Driver");
    connection = DriverManager.getConnection(DB_PATH);
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
    try (ResultSet rows = connection.createStatement().executeQuery(allQuery)) {
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
      connection.setAutoCommit(false);
      String insertionQuery =
          "INSERT INTO PRODUCTIONRECORD "
              + "(PRODUCT_ID, QUANTITY, DATE_PRODUCED) VALUES (?, ?, ?)";

      try (PreparedStatement stmnt = connection.prepareStatement(insertionQuery)) {
        for (Production production : productions) {
          stmnt.setInt(1, production.getProductId());
          stmnt.setInt(2, production.getQuantity());
          stmnt.setTimestamp(3, Timestamp.valueOf(production.getManufacturedOn()));
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

  /**
   * Returns a list of all productions from the databse.
   *
   * @return a list of productions
   * @throws SQLException if there is an error communicating with the database.
   */
  public List<Production> getAllProductions() throws SQLException {
    String allQuery = "SELECT * FROM PRODUCTIONRECORD";
    List<Production> productions = new ArrayList<>();
    try (ResultSet rows = connection.createStatement().executeQuery(allQuery)) {

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
            + " P.NAME, P.MANUFACTURER, P.TYPE, P.SERIALNUM\n"
            + "FROM PRODUCTIONRECORD PR\n"
            + "JOIN PRODUCT P on (PR.PRODUCT_ID=P.ID)";

    List<ProductionWithProduct> productions = new ArrayList<>();
    ResultSet rows = connection.createStatement().executeQuery(getProductsWithProductQuery);
    while (rows.next()) {
      int productionId = rows.getInt(1);
      int productId = rows.getInt(2);
      int quantity = rows.getInt(3);
      Timestamp dateProduced = rows.getTimestamp(4);
      String productName = rows.getString(5);
      String manufacturer = rows.getString(6);
      String productType = rows.getString(7);
      String serialNumber = rows.getString(8);

      Product productProduced = new Widget(productId, productName, ItemType.fromCode(productType),
          manufacturer);

      productProduced.setSerialNumber(serialNumber);

      productions.add(
          new ProductionWithProduct(productionId, quantity, serialNumber,
              dateProduced.toLocalDateTime(),
              productProduced));
    }
    rows.close();
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
    return resultSet.getInt(1);
  }

  public void recordProduction(Production... productions) {
    recordProductions(Arrays.asList(productions));
  }
}
