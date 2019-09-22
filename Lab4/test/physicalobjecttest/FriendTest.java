package physicalobjecttest;

import physicalobject.FriendFactory;
import physicalobject.PhysicalObject;
import physicalobject.PhysicalObjectFactory;

public class FriendTest extends PhysicalObjectTest {

  @Override
  PhysicalObject createInstance(String tName) {
    PhysicalObjectFactory physicalObjectFactory = new FriendFactory();
    return physicalObjectFactory.createPhysicalObject("Friend::= <" + tName + ", 25, F>");
  }
}
