public class ForeignKeyElement {

  private String parentTable;
  private String parentColumn;
  private String childTable;
  private String childColumn;

  public ForeignKeyElement(String parentTable, String parentColumn, String childTable, String childColumn) {
    this.parentTable = parentTable;
    this.parentColumn = parentColumn;
    this.childTable = childTable;
    this.childColumn = childColumn;
  }

  public String getParentTable() {
    return parentTable;
  }

  public String getParentColumn() {
    return parentColumn;
  }

  public String getChildColumn() {
    return childColumn;
  }

  public String getChildTable() {
    return childTable;
  }
}
