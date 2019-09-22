package physicalobjecttest;

import physicalobject.PhysicalObject;
import physicalobject.PositionPhysicalObjectFactory;

/**
 * PositionPhysicalObject Tester.
 *
 * @author FETS
 */
public class PositionPhysicalObjectTest extends PhysicalObjectTest {

  private PositionPhysicalObjectFactory factory = new PositionPhysicalObjectFactory();

  @Override
  PhysicalObject createInstance(String tName) {
    return factory.createPhysicalObject(tName);
  }


} 
