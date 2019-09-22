package physicalobject;

import java.util.Objects;
import lombok.Getter;

@Getter
public class Nucleus implements PhysicalObject {

  private final String name;

  // Abstraction function:
  // AF(name) = the name of this nucleus
  // Representation invariant:
  // no
  // Safety from rep exposure:
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Nucleus nucleus = (Nucleus) o;
    return getName().equals(nucleus.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName());
  }


  /**
   * construction field.
   *
   * @param name the name of this nucleus
   */
  Nucleus(String name) {
    this.name = name;
  }
  // all fields are final and immutable.
}
