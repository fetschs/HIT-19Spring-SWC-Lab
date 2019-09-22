package physicalobjecttest;

import physicalobject.AthleteFactory;
import physicalobject.PhysicalObject;
import physicalobject.PhysicalObjectFactory;

public class AthleteTest extends PhysicalObjectTest {


  @Override
  PhysicalObject createInstance(String tempName) {
    PhysicalObjectFactory factory = new AthleteFactory();
    return factory.createPhysicalObject("Athlete ::= " + "<" + tempName + ",23,USA,24,9.12>");
  }
}