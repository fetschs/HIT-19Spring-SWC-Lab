package track;

import java.math.BigDecimal;
import java.util.Objects;
import lombok.Getter;

@Getter
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

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Track track = (Track) o;
    return getRadius().compareTo(track.getRadius()) == 0;
  }

  @Override
  public int hashCode() {
    return Double.valueOf(getRadius().doubleValue()).hashCode();
  }
}
