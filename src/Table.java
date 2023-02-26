import java.util.ArrayList;
import java.util.List;

public class Table {

  private String name;
  private List<String> primaryKeys = new ArrayList<>();
  private List<ForeignKey> foreignKeys = new ArrayList<>();
  private List<String> columns;

  public Table(String name, List<String> columns) {
    this.name = name;
    this.columns = columns;
  }

  public String getName() {
    return this.name;
  }

  public void AddPrimaryKeys(String primaryKey) {
    this.primaryKeys.add(primaryKey);
  }

  public List<String> getPrimaryKeys() {
    return this.primaryKeys;
  }

  public void AddForeignKeys(ForeignKey foreignKey) {
    this.foreignKeys.add(foreignKey);
  }

  public List<ForeignKey> getForeignKeys() {
    return this.foreignKeys;
  }

  public List<String> getColumns() {
    return columns;
  }

  public void showPk() {

    if (getPrimaryKeys().isEmpty()) {
      return;
    }

    StringBuilder pks = new StringBuilder();
    pks.append(getName() + "(");
    pks.append(String.join(",", getPrimaryKeys()));
    pks.append(")");
    System.out.println(pks);

  }

  public void showFkConstraints() {

    if (getForeignKeys().isEmpty()) {
      return;
    }

    for (ForeignKey fk : getForeignKeys()) {
      System.out.println(fk.showFk());
    }
    System.out.println();

  }


}
