package physicalObjectTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import physicalObject.PhysicalObject;

public abstract class PhysicalObjectTest {

  /**
   * get an instance.
   *
   * @param tName the name of this physicalObject
   * @return an instance
   */
  abstract PhysicalObject createInstance(String tName);

  @Test
  public void test() {
    String name = "Tom";
    PhysicalObject p1 = createInstance(name);
    PhysicalObject p2 = createInstance(name);
    PhysicalObject p3 = createInstance("");
    assertEquals("Equal object", p1, p2);
    assertNotEquals("Not Equal object", p1, p3);
  }
}
