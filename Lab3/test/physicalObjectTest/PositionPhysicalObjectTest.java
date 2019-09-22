package physicalObjectTest;

import physicalObject.PhysicalObject;
import physicalObject.PositionPhysicalObjectFactory;

/**
 * PositionPhysicalObject Tester.
 *
 * @author FETS
 */
public class PositionPhysicalObjectTest extends PhysicalObjectTest {

  protected PositionPhysicalObjectFactory factory = new PositionPhysicalObjectFactory();

  @Override
  PhysicalObject createInstance(String tName) {
    return factory.createPhysicalObject(tName);
  }


} 
