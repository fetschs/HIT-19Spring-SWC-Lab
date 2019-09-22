package relation;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Relation<L, E> {

  private final BigDecimal length;
  private final L from;
  private final E to;
  // Abstraction function:
  // AF(length) = the length of this relation
  // AF(Object1) = one object
  // AF(Object2) = another object
  // Representation invariant:
  // no
  // Safety from rep exposure:
  // all fields are private and immutable.

}
