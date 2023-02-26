import java.util.ArrayList;
import java.util.List;

public class ForeignKey {

  private List<ForeignKeyElement> keyAttributes;
  private String parent;
  private String child;

  public ForeignKey(String parent, String child, List<ForeignKeyElement> keyAttributes) {
    this.keyAttributes = keyAttributes;
    this.parent = parent;
    this.child = child;
  }

  public List<ForeignKeyElement> getKeyAttributes() {
    return this.keyAttributes;
  }

  public String showFk() {

    StringBuilder fkString = new StringBuilder();
    fkString.append(child);
    fkString.append("(");
    List<String> childColumns = new ArrayList<>();
    List<String> parentColumns = new ArrayList<>();
    for (ForeignKeyElement fke : getKeyAttributes()) {
      childColumns.add(fke.getChildColumn());
      parentColumns.add(fke.getParentColumn());
    }
    fkString.append(String.join(",", childColumns));
    fkString.append(") => ");
    fkString.append(parent);
    fkString.append("(");
    fkString.append(String.join(",", parentColumns));
    fkString.append(")");

    return fkString.toString();

  }
}
