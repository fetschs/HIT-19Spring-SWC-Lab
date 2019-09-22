package P3;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class BoardTest {

    /*
     * Testing strategy
     * 
     * Partition the inputs as follows: Game : go or chess name : EMPTY or empty
     * string Player : offensive or defensive Location : in the boadr or not in the
     * board.
     */
    String kindgo = "go";
    String kindchess = "chess";
    String name1 = "EMPTY";
    String name2 = "";
    Game gamego;
    Game gamechess;
    Player goplayer1;
    Player goplayer2;
    Player chessplayer1;
    Player chessplayer2;
    Board goboard;
    Board chessboard;
    int x1 = 0;
    int y1 = 0;
    int x2 = 0;
    int y2 = 0;
    int gon = 10;
    int chessn = 8;

    @Before
    public void setUp() throws Exception {
        gamego = new Game(kindgo);
        gamechess = new Game(kindchess);
        goplayer1 = new Player(gamego, name1, "offensive");
        goplayer2 = new Player(gamego, name1, "defensive");
        chessplayer1 = new Player(gamechess, name1, "offensive");
        chessplayer2 = new Player(gamechess, name2, "defensive");
        goboard = new Board(gamego, gon);
        chessboard = new Board(gamechess, chessn);
    }

    /**
     * covers test if the board size as we expected
     */
    @Test
    public void testBoard() {
        assertEquals("expected size", goboard.getSize(), gon + 1);
        assertEquals("expected size", chessboard.getSize(), chessn);
    }

    @Test
    /**
     * covers: location: in the board or not position: empty or not empty. test if
     * we can update by this method.
     */
    public void testUpdate() {
        x1 = -1;
        y1 = -1;
        Location location = new Location(x1, y1);
        Position position = new Position(goplayer1.getPieces().get(0));
        assertFalse("expected unsuccessfully update", goboard.updatePositions(location, position, goplayer1));
        x1 = 0;
        y1 = 0;
        location = new Location(x1, y1);
        ;
        position = new Position(goplayer2.getPieces().get(0));
        assertTrue("expected successfully update", goboard.updatePositions(location, position, goplayer1));
    }

    /**
     * covers: location: in the board or not position: empty or not empty test if we
     * can get true Owner by this method. bottom up test by testUpdate.
     */
    @Test
    public void testGetOwner() {
        testUpdate();
        x1 = 0;
        y1 = 0;
        Location location = new Location(x1, y1);
        Position position = new Position(goplayer1.getPieces().get(0));
        goboard.updatePositions(location, position, goplayer1);
        assertEquals("expected offensive", "offensive", goboard.getOwner(location));
        x1 = -1;
        y1 = -1;
        location = new Location(x1, y1);
        assertEquals("expected INVALID", "INVALID", goboard.getOwner(location));
        x1 = 1;
        y1 = 1;
        location = new Location(x1, y1);
        position = new Position(goplayer2.getPieces().get(0));
        goboard.updatePositions(location, position, goplayer2);
        assertEquals("expected defensive", "defensive", goboard.getOwner(location));
    }

    // covers:chess or go board offensive or defensive player
    // update then getPositions
    @Test
    public void testSomeget() {
        assertTrue("expected the same size", goboard.getSize() == gon + 1);
        assertTrue("expected the same size", chessboard.getSize() == chessn);
        assertTrue("expected the same sum of pieces",
                chessboard.getSumofpieces(chessplayer1) == chessboard.getSumofpieces(chessplayer2));
        assertTrue("expected the same sum of pieces",
                chessboard.getSumofpieces(goplayer1) == chessboard.getSumofpieces(goplayer2));
        Map<Location, Position> chessPositions = chessboard.getPositions();
        Map<Location, Position> goPositions = goboard.getPositions();
        for(Position position : goPositions.values()) {
            assertEquals("expected empty piece", Game.empty,position.getPiece());
        }
        assertEquals("expected n*n in board", chessn * chessn,chessPositions.size());
        assertEquals("expected n*n in board", (gon+1) * (gon+1),goPositions.size());
    }
}
