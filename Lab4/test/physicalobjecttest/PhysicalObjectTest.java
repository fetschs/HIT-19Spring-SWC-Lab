package physicalobjecttest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import physicalobject.Electron;
import physicalobject.PhysicalObject;

public abstract class PhysicalObjectTest {

  /**
   * get an instance. test factory method.
   *
   * @param tName the name of this physicalobject
   * @return an instance
   */
  abstract PhysicalObject createInstance(String tName);
  // test some normal method,use same or not same input for equals

  @Test
  public void test() {
    String name = "Tom";
    PhysicalObject p1 = createInstance(name);
    PhysicalObject p2 = createInstance(name);
    PhysicalObject p3 = createInstance("Emp");
    assertEquals("Equal object", p1, p2);
    assertEquals("Equal object", p1, p1);
    if (!(p1 instanceof Electron)) {
      assertNotEquals("Not Equal object", p1, p3);
    }
    assertNotEquals("Not Equal object", p1, null);
    assertNotEquals("Not Equal object", p1, 0);
  }
}
