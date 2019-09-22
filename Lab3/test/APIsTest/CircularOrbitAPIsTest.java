package APIsTest;

import static org.junit.Assert.assertEquals;

import APIs.CircularOrbitAPIs;
import circularOrbit.CircularOrbit;
import circularOrbit.Difference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.Before;
import org.junit.Test;
import relation.Relation;
import track.Track;

/**
 * circularOrbitAPIs Tester.
 *
 * @author FETS
 */
public abstract class CircularOrbitAPIsTest<L, E> {

  Track t1;
  Track t2;
  E a1;
  E a2;
  E b1;
  E b2;
  private CircularOrbitAPIs circularOrbitAPIs = new CircularOrbitAPIs();
  private CircularOrbit<L, E> c1 = createEmptyOrbitInstance();
  private CircularOrbit<L, E> c2 = createEmptyOrbitInstance();

  /**
   * create empty orbit instance.
   *
   * @return the empty orbit instance.
   */
  abstract CircularOrbit<L, E> createEmptyOrbitInstance();

  /**
   * create an E instance name is tempName.
   *
   * @param tempName the name of instance
   * @return the instance
   */
  abstract E createEInstance(String tempName);
  /*
   * Testing strategy
   * test apis by normal things.
   * not test physical because i didn't create concrete Class about Position.
   * add more orbit to test in further work. TODO
   */

  /**
   * setUp before every test.
   */
  @Before
  public void setUp() {
    t1 = new Track(BigDecimal.ONE);
    t2 = new Track(BigDecimal.TEN);
    a1 = createEInstance("Ao");
    a2 = createEInstance("At");
    b1 = createEInstance("Bo");
    b2 = createEInstance("Bt");
    c1.addTrack(t1);
    c2.addTrack(t1);
    c1.addTrack(t2);
    c2.addTrack(t2);
    c1.addPhysicalObjectInTrack(a1, t1);
    c1.addPhysicalObjectInTrack(a2, t1);
    c2.addPhysicalObjectInTrack(b1, t1);
    c2.addPhysicalObjectInTrack(b2, t2);
  }

  /**
   * use the super class as <L,E> to test. Method: getObjectDistributionEntropy(CircularOrbit<L, E>
   * c)
   */
  @Test
  public void testGetObjectDistributionEntropy() {
    assertEquals("expected entropy", circularOrbitAPIs.getObjectDistributionEntropy(c1),
        BigInteger.ONE);
    assertEquals("expected entropy", circularOrbitAPIs.getObjectDistributionEntropy(c2),
        BigInteger.ONE.add(BigInteger.ONE));
  }

  /**
   * test Method: getLogicalDistance(CircularOrbit c, E e1, E e2).
   */
  @Test
  public void testGetLogicalDistance() throws Exception {
    c1.addT2tRelation(new Relation<>(BigDecimal.ONE, a1, a2));
    assertEquals("expected logic distance", 1
        , circularOrbitAPIs.getLogicalDistance(c1, a1, a2));
    assertEquals("expected inf logic distance", Integer.MAX_VALUE,
        circularOrbitAPIs.getLogicalDistance(c2, b1, b2));
  }

  /**
   * num will be 0,1,3. Method: recursion(BigInteger num).
   */
  @Test
  public void testRecursion() {
    try {
      Method method = circularOrbitAPIs.getClass().getDeclaredMethod("recursion", BigInteger.class);
      method.setAccessible(true);
      assertEquals("get 1 by 0", BigInteger.ONE, method.invoke(circularOrbitAPIs, BigInteger.ZERO));
      assertEquals("get 1 by 1", BigInteger.ONE, method.invoke(circularOrbitAPIs, BigInteger.ONE));
      assertEquals("get 6 by 3", BigInteger.valueOf(6),
          method.invoke(circularOrbitAPIs, BigInteger.valueOf(3)));

    } catch (NoSuchMethodException | IllegalAccessException | IllegalAccessError | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  /**
   * test the len and Exception test Method: getPhysicalDistance(CircularOrbit c, E e1, E e2)
   */
//  @Test
//  public void getPhysicalDistance() {
//    Position pos1 = new Position(BigDecimal.valueOf(3), BigDecimal.valueOf(0));
//    Position pos2 = new Position(BigDecimal.valueOf(4), BigDecimal.valueOf(90));
//    PositionPhysicalObject pp1 = new PositionPhysicalObject("pp1", pos1);
//    PositionPhysicalObject pp2 = new PositionPhysicalObject("pp2", pos2);
//    CircularOrbit<L, PositionPhysicalObject> c3 = createEmptyOrbitInstance();
//    BigDecimal validLen = BigDecimal.ZERO;
//    boolean flag = false;
//    try {
//      circularOrbitAPIs.getPhysicalDistance(c1, a1, a2);
//    } catch (Exception e) {
//      flag = true;
//    }
//    assertTrue("expected catch invalid call", flag);
//    try {
//      validLen = circularOrbitAPIs.getPhysicalDistance(c3, pp1, pp2);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    assertEquals("expected distance is 5", 0, validLen.compareTo(
//        BigDecimal.valueOf(5)));
//
//  }
  @Test
  public void testGetDifference() {
    Difference difference = circularOrbitAPIs.getDifference(c1, c2);
    assertEquals("same number", difference.getTrackNumber(), 0);
    assertEquals("expected difference number in track", difference.getObjectNumber()[0], 1);
    assertEquals("expected difference number in track", difference.getObjectNumber()[1], -1);
  }
}
