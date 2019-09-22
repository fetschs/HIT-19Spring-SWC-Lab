package physicalobject;

import java.util.Objects;
import lombok.Getter;

@Getter
public class Electron implements PhysicalObject {

  private final String name;

  // Abstraction function:
  // AF(name) the name of electron
  // RI: no
  // All fields are private and immutable.
  Electron() {
    this.name = "Electron";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    return !(o == null || getClass() != o.getClass());
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
  // AF(name) = the name of this nucleus
  // Representation invariant:
  // no
  // Safety from rep exposure:
  // all fields are final and immutable.
}
