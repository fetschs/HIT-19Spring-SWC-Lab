package position;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@Value
@AllArgsConstructor
public class Position {

  @NonNull
  private final BigDecimal rho;
  @NonNull
  private final BigDecimal theta;
  // Abstraction function:
  // AF(rho) = the length
  // AF(theta) = the angle
  // Representation invariant:
  // the angle should be 0-360,length>=0
  // Safety from rep exposure:
  // all fields are private

  /**
   * check the angle and length.
   */
  private void checkRep() {
    assert rho.compareTo(BigDecimal.ZERO) >= 0;
    assert theta.compareTo(BigDecimal.ZERO) >= 0;
    assert theta.compareTo(BigDecimal.valueOf(360)) <= 0;
  }
}