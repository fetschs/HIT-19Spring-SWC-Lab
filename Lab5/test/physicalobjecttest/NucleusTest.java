package physicalobjecttest;

import physicalobject.NucleusFactory;
import physicalobject.PhysicalObject;
import physicalobject.PhysicalObjectFactory;

public class NucleusTest extends PhysicalObjectTest{

  @Override
  PhysicalObject createInstance(String tName) {
    PhysicalObjectFactory physicalObjectFactory = new NucleusFactory();
    return physicalObjectFactory.createPhysicalObject("ElementName ::= "+tName);
  }
}
