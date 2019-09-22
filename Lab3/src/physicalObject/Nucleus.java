package physicalObject;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
public class Nucleus implements PhysicalObject {

  private final String name;

  // Abstraction function:
  // AF(name) = the name of this nucleus
  // Representation invariant:
  // no
  // Safety from rep exposure:
  // all fields are final and immutable.
}
