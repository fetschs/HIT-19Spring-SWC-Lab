package physicalObjectTest;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import physicalObject.Athlete;
import physicalObject.AthleteFactory;
import physicalObject.PhysicalObjectFactory;

public class AthleteTest extends PhysicalObjectTest {


  @Override
  Athlete createInstance(String tempName) {
    PhysicalObjectFactory factory = new AthleteFactory();
    return (Athlete) factory
        .createPhysicalObject("Athlete ::= " + "<" + tempName + ",23,USA,24,9.12>");
  }

  /**
   * just test factory by valid or invalid input.
   */
  @Test
  public void test() {
    PhysicalObjectFactory factory = new AthleteFactory();
    String tempData = "Athlete ::= <Bolt,1,JAM,38,9.88>";
    factory.createPhysicalObject(tempData);
    tempData = "Athlete ::= <Bolt,1,JAM,38,9.8>";
    try {
      factory.createPhysicalObject(tempData);
    } catch (AssertionError e) {
      assertTrue("expected checkRep", true);
    }
    tempData = "Athlete ::= <Bolt,1,JA,38,9.81>";
    try {
      factory.createPhysicalObject(tempData);
    } catch (AssertionError e) {
      assertTrue("expected checkRep", true);
    }
    tempData = "Athlete ::= <Bolt,1,JAM,38,109.8>";
    try {
      factory.createPhysicalObject(tempData);
    } catch (AssertionError e) {
      assertTrue("expected checkRep", true);
    }
  }
}