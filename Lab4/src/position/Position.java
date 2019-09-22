package position;

import java.math.BigDecimal;
import java.util.Objects;
import lombok.Getter;

@Getter
public class Position {

  private final BigDecimal rho;

  private final BigDecimal theta;
  // Abstraction function:
  // AF(rho) = the length
  // AF(theta) = the angle
  // Representation invariant:
  // the angle should be 0-360,length>=0
  // Safety from rep exposure:
  // all fields are private

  /**
   * construct field.
   *
   * @param rho this rho
   * @param theta this theta
   */
  public Position(BigDecimal rho, BigDecimal theta) {
    this.rho = rho;
    this.theta = theta;
  }

  /**
   * check the angle and length.
   */
  private void checkRep() {
    assert rho.compareTo(BigDecimal.ZERO) >= 0;
    assert theta.compareTo(BigDecimal.ZERO) >= 0;
    assert theta.compareTo(BigDecimal.valueOf(360)) <= 0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Position position = (Position) o;
    return getRho().compareTo(position.getRho()) == 0
        && getTheta().compareTo(position.getTheta()) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getRho(), getTheta());
  }
}