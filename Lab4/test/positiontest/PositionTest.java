package positiontest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import org.junit.Test;
import position.Position;

/**
 * @author fets
 */
public class PositionTest {

  // test some normal method,use valid or invalid theta or rho
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
    Position p1 = new Position(BigDecimal.valueOf(rho), BigDecimal.valueOf(theta));
    assertEquals("the same rho", p1.getRho().compareTo(BigDecimal.valueOf(rho)), 0);
    assertEquals("the same theta", p1.getTheta().compareTo(BigDecimal.valueOf(theta)), 0);
    Position p2 = new Position(BigDecimal.valueOf(rho), BigDecimal.valueOf(theta));
    assertEquals(p1, p2);
    assertNotEquals(p1, null);
    assertEquals(p1, p1);
    rho = 2;
    theta = 6;
    Position p3 = new Position(BigDecimal.valueOf(rho), BigDecimal.valueOf(theta));
    assertNotEquals(p1, p3);
    assertNotEquals(p1, rho);
    rho = 1;
    theta = 6;
    p3 = new Position(BigDecimal.valueOf(rho), BigDecimal.valueOf(theta));
    assertNotEquals(p1, p3);
  }

}
