package physicalobject;

import java.math.BigDecimal;
import java.util.Objects;
import lombok.Getter;
import position.Position;

@Getter
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PositionPhysicalObject that = (PositionPhysicalObject) o;
    return getName().equals(that.getName())
        && getPosition().equals(that.getPosition());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getPosition());
  }
}
