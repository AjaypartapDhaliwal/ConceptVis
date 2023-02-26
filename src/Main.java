import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Main {

  static final String DB_URL = "jdbc:postgresql://db.doc.ic.ac.uk/mondial";
  static final String USER = "lab";
  static final String PASS = "lab";
  static final String QUERY = "SELECT table_name\n"
      + "FROM information_schema.tables\n"
      + "WHERE table_schema='public'";

  public static void main(String[] args) {
    // Open a connection
    // Load the relevent RDBMS JDBC driver
    System.out.println( "Plain Java, Postgres version\n" );

    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      System.err.println( "Driver not found: " + e + "\n" + e.getMessage() );
    }

    try {
      Connection conn = DriverManager.getConnection (DB_URL, USER, PASS);

      //get metadata and tables
      DatabaseMetaData md = conn.getMetaData();
      List<Table> tables = getTables(md,conn);

      // get pks and fks for all tables
      for (Table table : tables) {
        extractPrimaryKeys(md, table);
        extractForeignKeys(md, table);

        System.out.println(table.getName());
        table.showPk();
        table.showFkConstraints();
        System.out.println("columns = " + table.getColumns());
        System.out.println();
      }

      Data data = new Data(tables);
      String[] tablesToDisplay = new String[] {"province"};
      System.out.println(data.relationType(tablesToDisplay));

    } catch (SQLException e) {
      System.err.println("Something went wrong!");
      e.printStackTrace();
    }
  }

  private static void extractForeignKeys(DatabaseMetaData md, Table table) throws SQLException {

    ResultSet foreignKeys = md.getImportedKeys(null, null, table.getName());
    HashMap<String, List<ForeignKeyElement>> fkNames = new HashMap<>();

    while (foreignKeys.next()) {

      String currfkName = foreignKeys.getString("FK_NAME");
      if (!fkNames.containsKey(currfkName)) {
        fkNames.put(currfkName, new ArrayList<>());
      }

    }

    while (foreignKeys.previous()) {}

    String tableTo = "";
    String tableFrom = "";

    while(foreignKeys.next()){

      tableFrom = foreignKeys.getString("PKTABLE_NAME");
      tableTo = foreignKeys.getString("FKTABLE_NAME");
      String pkColumnName = foreignKeys.getString("PKCOLUMN_NAME");
      String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME");

      ForeignKeyElement fk = new ForeignKeyElement(tableFrom, pkColumnName, tableTo, fkColumnName);
      fkNames.get(foreignKeys.getString("FK_NAME")).add(fk);

    }

    for (Entry<String, List<ForeignKeyElement>> entry : fkNames.entrySet()) {
      tableFrom = entry.getValue().get(0).getParentTable();
      tableTo = entry.getValue().get(0).getChildTable();
      table.AddForeignKeys(new ForeignKey(tableFrom, tableTo, entry.getValue()));
    }
  }

  private static void extractPrimaryKeys(DatabaseMetaData md, Table table) throws SQLException {

    ResultSet primaryKeys = md.getPrimaryKeys(null, null, table.getName());
    while (primaryKeys.next()) {
      String primaryKeyColumnName = primaryKeys.getString("COLUMN_NAME");
      table.AddPrimaryKeys(primaryKeyColumnName);
    }
  }

  private static List<Table> getTables(DatabaseMetaData md, Connection conn) throws SQLException {

    ResultSet rs = md.getTables(null, null, null, new String[]{"TABLE"});
    List<Table> tables = new ArrayList<>();

    while (rs.next()) {

      String tableName = rs.getString(3);
      List<String> columns = new ArrayList<>();

      ResultSet resultSet = md.getColumns(null, null, tableName, null);
      while (resultSet.next()) {
        columns.add(resultSet.getString("COLUMN_NAME"));
      }

      Table table = new Table(tableName, columns);
      tables.add(table);
    }
    return tables;
  }

}
