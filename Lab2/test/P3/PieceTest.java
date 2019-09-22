package P3;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PieceTest {
    //test the get and equals
    Piece piece;
    @Before
    public void setUp() throws Exception {
        piece = new Piece("EMPTY");
    }
    // just test equals and hashcode as normal
    @Test
    public void testEqualsandHashCode() {
        assertTrue("expected equal",piece.equals(Game.empty));
        assertEquals("expected equal hashcode",Game.empty.hashCode(),piece.hashCode());
    }
    // test get method
    @Test
    public void testGetKind() {
        assertEquals("expected the kind", "EMPTY",piece.getKind());
    }

}
