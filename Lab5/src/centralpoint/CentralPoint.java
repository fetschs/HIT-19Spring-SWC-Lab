package centralpoint;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CentralPoint<L> {

  private final List<L> centralObjects;

  // Abstraction function:
  // AF(centralObjects) = the objects in this orbit
  // Representation invariant:
  // no
  // Safety from rep exposure:
  // all fields are private.
  // mutable


  /**
   * add a centralObject in list.
   *
   * @param centralObject object will be add.
   */
  public void addCentralObject(L centralObject) {
    centralObjects.add(centralObject);
  }

  public List<L> getCentralObjects() {
    return new ArrayList<>(centralObjects);
  }
}
