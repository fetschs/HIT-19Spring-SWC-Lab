package relationTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import org.junit.Test;
import relation.Relation;


public class RelationTest {

  @Test
  // just test
  public void test() {
    String s1 = "Song";
    String s2 = "Chen";
    BigDecimal len = BigDecimal.ONE;
    Relation<String, String> relation = new Relation<>(len, s1, s2);
    assertNotNull(relation);
    assertEquals("same len", relation.getLength(), len);
    assertEquals("same object", relation.getFrom(), s1);
    assertEquals("same object", relation.getTo(), s2);
  }
}