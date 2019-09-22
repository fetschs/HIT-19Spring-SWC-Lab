package physicalobjecttest;

import physicalobject.CentralUserFactory;
import physicalobject.PhysicalObject;
import physicalobject.PhysicalObjectFactory;

public class CentralUserTest extends PhysicalObjectTest {

  @Override
  PhysicalObject createInstance(String tName) {
    PhysicalObjectFactory factory = new CentralUserFactory();
    return (factory
        .createPhysicalObject("CentralUser ::= <" + tName + ",30,M>"));
  }
}
