package positionTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import org.junit.Test;
import position.Position;

/**
 * @author fets
 */
public class PositionTest {

  @Test
  public void test() {
    int rho = -1;
    int theta = 350;
    try {
      new Position(BigDecimal.valueOf(rho), BigDecimal.valueOf(theta));
    } catch (AssertionError e) {
      assertTrue("checkRep success", true);
    }
    theta = 361;
    try {
      new Position(BigDecimal.valueOf(rho), BigDecimal.valueOf(theta));
    } catch (AssertionError e) {
      assertTrue("checkRep success", true);
    }
    rho = 1;
    theta = 5;
    Position position = new Position(BigDecimal.valueOf(rho), BigDecimal.valueOf(theta));
    assertEquals("the same rho", position.getRho().compareTo(BigDecimal.valueOf(rho)), 0);
    assertEquals("the same theta", position.getTheta().compareTo(BigDecimal.valueOf(theta)), 0);
  }

}
