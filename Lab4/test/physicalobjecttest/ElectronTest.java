package physicalobjecttest;

import physicalobject.ElectronFactory;
import physicalobject.PhysicalObject;
import physicalobject.PhysicalObjectFactory;

public class ElectronTest extends PhysicalObjectTest {

  @Override
  PhysicalObject createInstance(String tName) {
    PhysicalObjectFactory physicalObjectFactory = new ElectronFactory();
    return physicalObjectFactory.createPhysicalObject("");
  }
}
