package backend;

public enum EntityType {
  PRODUCT("Product"),
  PROD_RECORD("ProductionRecord");

  private final String tableName;

  EntityType(String tableName) {
    this.tableName = tableName;
  }

  public String getTableName() {
    return tableName;
  }
}
