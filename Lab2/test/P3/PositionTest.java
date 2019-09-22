package P3;

import static org.junit.Assert.*;
import org.junit.Test;

public class PositionTest {

    //just in case
    @Test
    public void testPosition() {
        Position position = new Position(Game.empty);
        assertEquals("expected the same piece",Game.empty,position.getPiece());
    }

}
