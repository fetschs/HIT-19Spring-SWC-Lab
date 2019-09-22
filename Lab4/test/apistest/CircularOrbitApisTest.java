package apistest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import apis.CircularOrbitApis;
import apis.CircularOrbitHelper;
import circularorbit.CircularOrbit;
import circularorbit.CircularOrbitIterator;
import circularorbit.Difference;
import circularorbit.TrackGame;
import exception.InvalidInputException;
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
public abstract class CircularOrbitApisTest<L, E> {

  Track t1;
  Track t2;
  E a1;
  E a2;
  E b1;
  E b2;
  private CircularOrbitApis circularOrbitAPIs = new CircularOrbitApis();
  CircularOrbit<L, E> c1 = createEmptyOrbitInstance();
  CircularOrbit<L, E> c2 = createEmptyOrbitInstance();

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
    t1 = new Track(BigDecimal.valueOf(c1.getTracks().size() + 1));
    t2 = new Track(BigDecimal.valueOf(c2.getTracks().size() + 2));
    a1 = createEInstance("Ao");
    a2 = createEInstance("At");
    b1 = createEInstance("Bo");
    b2 = createEInstance("Bt");
    try {
      c1.addTrack(t1);
      c2.addTrack(t1);
      c1.addTrack(t2);
      c2.addTrack(t2);
      c1.addPhysicalObjectInTrack(a1, t1);
      c1.addPhysicalObjectInTrack(a2, t1);
      c2.addPhysicalObjectInTrack(b1, t1);
      c2.addPhysicalObjectInTrack(b2, t2);
    } catch (InvalidInputException e) {
      fail();
    }
  }

  /**
   * test the calc of distribution Entropy. use the super class as <L,E> to test. Method:
   * getObjectDistributionEntropy(CircularOrbit<L, E> c)
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
      fail();
    }
  }
//
//  /**
//   * test the len and Exception test Method: getPhysicalDistance(CircularOrbit c, E e1, E e2)
//   */
//  @Test
//  public void testGetPhysicalDistance() {
//    Position pos1 = new Position(BigDecimal.valueOf(3), BigDecimal.valueOf(0));
//    Position pos2 = new Position(BigDecimal.valueOf(4), BigDecimal.valueOf(90));
//    PositionPhysicalObject pp1 = new PositionPhysicalObject("pp1", pos1);
//    PositionPhysicalObject pp2 = new PositionPhysicalObject("pp2", pos2);
//    CircularOrbit<PositionPhysicalObject, PositionPhysicalObject> c3 = ;
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

  /**
   * test get Difference method as normal.
   */
  @Test
  public void testGetDifference() {
    Difference difference = circularOrbitAPIs.getDifference(c1, c2);
    assertEquals("same number", difference.getTrackNumber(), 0);
    assertEquals("expected difference number in track",
        difference.getObjectNumber()[c1.getTracks().size() - 2], 1);
    assertEquals("expected difference number in track",
        difference.getObjectNumber()[c2.getTracks().size() - 1], -1);
    try {
      c1.removeTrack(t2);
    }catch (InvalidInputException e) {
      fail();
    }
    difference = circularOrbitAPIs.getDifference(c1,c2);
    assertEquals("not same number", difference.getTrackNumber(), -1);
  }

  /**
   * test the iterator and assert.
   */
  @Test
  public void testIterator() {
    CircularOrbitIterator<E> circularOrbitIterator = c1.iterator();
    while (circularOrbitIterator.hasNext()) {
      circularOrbitIterator.next();
    }
    circularOrbitIterator = c2.iterator();
    if (c1 instanceof TrackGame) {// test the trackGame iterator in his test.
      return;
    }
    assertEquals(b1, circularOrbitIterator.next());
    assertEquals(b2, circularOrbitIterator.next());
  }

  @Test
  public void testGUI() {
    CircularOrbitHelper.main(new String[10]);
  }
}
