package entity;

import static org.junit.Assert.*;
import org.junit.Test;

public class laddertest {
  @Test
  public void testLadder() {
    Monkey m1= new Monkey(1, Monkey.L2R, 1);
    Ladder l1 = new Ladder(1, 3);
    assertEquals(l1.getDirection(), "Empty");
    assertEquals(l1.getMonkeyNumber(), 0);
    assertEquals(l1.getId(), 1);
    l1.jumpMonkey(m1, 0);
    l1.jumpMonkey(m1, 0, 1);
    assertEquals(l1.getMonkeyNumber(),1);
    assertEquals(l1.getMonkeyNumber(),1);
    l1.downMonkey();
    assertEquals(l1.getMonkeyNumber(), 0);
    assertEquals(l1.getDirection(), "Empty");
  }
}
