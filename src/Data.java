import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Data {

  private List<Table> tables;

  public Data(List<Table> tables) {
    this.tables = tables;
  }

  public List<Table> getTables() {
    return tables;
  }

  public String relationType(String[] tableNames) {

    List<Table> tablesToDisplay = new ArrayList<>();

    if (tableNames.length == 1) {
      return "visualise " + tableNames[0] + " as " + EntityType.Basic;
    }

    for (String name : tableNames) {
      for (Table t : getTables()) {
        if (t.getName().equals(name)) {
          tablesToDisplay.add(t);
        }
      }
    }

    boolean eOneTakes = false;

    Table t1 = tablesToDisplay.get(0);
    Table t2 = tablesToDisplay.get(1);
    ForeignKey checkFk = null;

    for (ForeignKey fk : t1.getForeignKeys()) {
      for (ForeignKeyElement fke : fk.getKeyAttributes()) {
        if (fke.getParentTable().equals(tableNames[1])) {
          eOneTakes = true;
          checkFk = fk;
        }
      }
    }

    if (eOneTakes) {
      if (checkWeak(t1, checkFk)) {
        return "visualise " + tableNames[0] + " as " + EntityType.Weak + " to " + tableNames[1];
      }
      if (checkOneMany(t1, checkFk)) {
        return "visualise " + tableNames[0] + " as " + EntityType.OneMany + " to " + tableNames[1];
      }
    }

    if (!eOneTakes) {

      for (ForeignKey fk : t2.getForeignKeys()) {
        for (ForeignKeyElement fke : fk.getKeyAttributes()) {
          if (fke.getParentTable().equals(tableNames[0])) {
            checkFk = fk;
          }
        }
      }

      if (checkWeak(t2, checkFk)) {
        return "visualise " + tableNames[1] + " as " + EntityType.Weak + " to " + tableNames[0];
      }
      if (checkOneMany(t2, checkFk)) {
        return "visualise " + tableNames[1] + " as " + EntityType.OneMany + " to " + tableNames[0];
      }

    }

    return "not possible";
  }

  public boolean checkWeak(Table table, ForeignKey fk) {

    List<String> pks = table.getPrimaryKeys();
    List<String> fkes = fk.getKeyAttributes()
                          .stream()
                          .map(x -> x.getChildColumn())
                          .collect(Collectors.toList());

    for (String fke : fkes) {
      if (!pks.contains(fke)) {
        return false;
      }
    }

    return true;
  }

  public boolean checkOneMany(Table table, ForeignKey fk) {

    List<String> clmnsWithoutPk = new ArrayList<>(table.getColumns());
    clmnsWithoutPk.removeAll(table.getPrimaryKeys());

    List<String> fkes = fk.getKeyAttributes()
        .stream()
        .map(x -> x.getChildColumn())
        .collect(Collectors.toList());

    for (String fke : fkes) {
      if (!clmnsWithoutPk.contains(fke)) {
        return false;
      }
    }

    return true;
  }

  public enum EntityType {
    Basic,
    Weak,
    OneMany,
    ManyMany
  }




}
