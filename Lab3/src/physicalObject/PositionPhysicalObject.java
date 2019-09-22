package physicalObject;

import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import position.Position;

@Getter
@EqualsAndHashCode
public class PositionPhysicalObject implements PhysicalObject {

  private static Position defPosition = new Position(BigDecimal.ONE, BigDecimal.ZERO);
  private final String name;
  private final Position position;

  // Abstraction function:
  // AF(position) = the position of this object
  // Representation invariant:
  // radius should be more than zero
  // Safety from rep exposure:
  // all fields are protected
  // return immutable field.
  public PositionPhysicalObject(String tempName) {
    name = tempName;
    position = defPosition;
  }

  public PositionPhysicalObject(String tempName, Position tempPosition) {
    name = tempName;
    position = tempPosition;
  }
}
