package track;

import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Track implements Comparable<Track> {

  private final BigDecimal radius;

  // Abstraction function:
  // AF(radius) = the radius of this track
  // Representation invariant:
  // radius should be more than zero
  // Safety from rep exposure:
  // all fields are private and immutable

  /**
   * construction method.
   */
  public Track(BigDecimal tempRadius) {
    radius = tempRadius;
    checkRep();
  }


  private void checkRep() {
    assert radius.compareTo(BigDecimal.ZERO) >= 0;
  }

  @Override
  public int compareTo(Track o) {
    BigDecimal tempRadius = o.getRadius();
    return this.radius.compareTo(tempRadius);
  }
}
