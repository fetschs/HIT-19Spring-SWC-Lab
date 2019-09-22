package trackTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import org.junit.Test;
import track.Track;

/**
 * @author FETS
 */
public class TrackTest {

  @Test
  // test some normal method,use valid or invalid radius
  public void test() {
    Track t1 = new Track(BigDecimal.valueOf(1));
    Track t2 = new Track(BigDecimal.valueOf(2));
    Track t3 = new Track(BigDecimal.valueOf(1));
    try {
      new Track(BigDecimal.valueOf(-1));
    } catch (AssertionError e) {
      assertTrue("expected error", true);
    }
    assertEquals("test compare", -1, t1.compareTo(t2));
    assertEquals("test compare", 0, t1.compareTo(t3));
    assertEquals("test compare", 1, t2.compareTo(t1));
  }

}
