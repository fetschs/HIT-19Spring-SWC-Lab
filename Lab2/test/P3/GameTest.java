package P3;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class GameTest {
    Game gogame;
    Game chessgame;
    @Before
    public void setUp() {
        gogame = new Game("go");
        chessgame = new Game("chess");
    }
    //just test all the function by normal input(go or chess)
    @Test
    public void testGame() {
        assertFalse("expected not the same size",
                gogame.getOffensive().size() == gogame.getDefensive().size());
        assertTrue("expected the same size",
                chessgame.getOffensive().size() == chessgame.getDefensive().size());
        assertTrue("expected the same kind",chessgame.getKind().equals("chess"));
        assertTrue("expected the same kind",gogame.getKind().equals("go"));
    }
}
