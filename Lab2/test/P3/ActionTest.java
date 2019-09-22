package P3;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ActionTest {

    /*
     * Testing strategy
     * 
     * Partition the inputs as follows: Game : go or chess Piece : class Game static
     * pieces name : EMPTY or empty string x : x<0,x=0 ,x=n,x>n y : x<0,x=0 ,x=n,x>n
     * Player : offensive or defensive board : size n > 0 source/target/vertex: in
     * the graph or not weight:0, >0
     * 
     */
    String kindgo = "go";
    String kindchess = "chess";
    String name1 = "EMPTY";
    String name2 = "";
    Piece white = new Piece("white");
    Piece black = new Piece("black");
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
     * covers: player : off or def x,y : <0 0 >0 =n+1(because go.size = go +1). test
     * remove empty/my piece. test if we can Place Piece in valid or invalid
     * position by each player.
     */
    @Test
    public void testPlacePiece() {
        testcheckLocation();
        x1 = -1;
        y1 = -1;
        assertFalse("expected unsuccesfully place piece", Action.placePiece(goplayer1, black, x1, y1, goboard));
        x1 = gon + 1;
        y1 = gon + 1;
        assertFalse("expected unsuccesfully place piece", Action.placePiece(goplayer1, black, x1, y1, goboard));
        x1 = 0;
        y1 = 0;
        assertFalse("expected unsuccesfully place the other's piece",
                Action.placePiece(goplayer1, white, x1, y1, goboard));
        x1 = gon;
        y1 = gon;
        assertEquals("expected true owner","EMPTY", Action.checkLocation(x1, y1, goboard));
        assertTrue("expected succesfully place piece", Action.placePiece(goplayer1, black, x1, y1, goboard));
        assertEquals("expected true owner","offensive", Action.checkLocation(x1, y1, goboard));
        x1 = 5;
        y1 = 5;
        assertTrue("expected succesfully place piece", Action.placePiece(goplayer1, black, x1, y1, goboard));
        assertFalse("expected unsuccesfully place piece", Action.placePiece(goplayer1, black, x1, y1, goboard));
        assertEquals("expected true owner","offensive", Action.checkLocation(x1, y1, goboard));
        assertTrue("expected succesfully place piece", Action.placePiece(chessplayer1, Game.king, x1, y1, chessboard));
        assertEquals("expected true owner","offensive", Action.checkLocation(x1, y1, chessboard));
    }

    /**
     * covers: player : off or def x,y : <0 0 >0 =n =n+1(because go.size = go +1)
     * test if we can Remove Piece in valid or invalid position by each player.
     * bottom up test by test Place Piece
     */
    @Test
    public void testRemovePiece() {
        testPlacePiece();
        testcheckLocation();
        x1 = -1;
        y1 = -1;
        assertFalse("expected unsuccesfully remove piece", Action.removePiece(goplayer1, x1, y1, goboard));
        x1 = gon + 1;
        y1 = gon + 1;
        assertFalse("expected unsuccesfully remove empty piece", Action.removePiece(goplayer1, x1, y1, goboard));
        x1 = 0;
        y1 = 0;
        Action.placePiece(goplayer1, black, x1, y1, goboard);
        assertFalse("expected unsuccesfully remove your piece", Action.removePiece(goplayer1, x1, y1, goboard));
        x1 = gon;
        y1 = gon;
        Action.placePiece(goplayer1, black, x1, y1, goboard);
        assertTrue("expected succesfully remove piece", Action.removePiece(goplayer2, x1, y1, goboard));
        assertFalse("expected unsuccesfully repeat remove piece", Action.removePiece(goplayer2, x1, y1, goboard));
        assertEquals("expected true owner","EMPTY", Action.checkLocation(x1, y1, goboard));
    }

    /**
     * covers: player : off or def. test move same/empty/other's pieces. test move
     * to invalid location. test if we can move Piece in valid or invalid position
     * by each player. bottom up test by check.
     */
    @Test
    public void testMovePiece() {
        testcheckLocation();
        x1 = 1;
        y1 = 1;
        x2 = 1;
        y2 = 1;
        assertFalse("expected unsuccesfully move the same piece",
                Action.movePiece(chessplayer1, x1, y1, x2, y2, chessboard));
        x2 = 3;
        y2 = 3;
        assertFalse("expected unsuccesfully move the other's piece",
                Action.movePiece(chessplayer2, x1, y1, x2, y2, chessboard));
        x1 = 2;
        y1 = 2;
        assertFalse("expected unsuccesfully move empty piece",
                Action.movePiece(chessplayer1, x1, y1, x2, y2, chessboard));
        x2 = 0;
        y2 = -1;
        assertFalse("expected unsuccesfully move to invalid place(not in the board)",
                Action.movePiece(chessplayer1, x1, y1, x2, y2, chessboard));
        x2 = 7;
        y2 = 7;
        assertFalse("expected unsuccesfully move to invalid place(has a piece)",
                Action.movePiece(chessplayer1, x1, y1, x2, y2, chessboard));
        x1 = 1;
        y1 = 1;       
        x2 = 5;
        y2 = 5;
        assertTrue("expected successfully move from(1,1) to (5,5)",
                Action.movePiece(chessplayer1, x1, y1, x2, y2, chessboard));
        assertEquals("expected true owner","EMPTY", Action.checkLocation(x1, y1, chessboard));
        assertEquals("expected true owner","offensive", Action.checkLocation(x2, y2, chessboard));
    }

    /**
     * covers: player : off or def. test capture same/empty/yours pieces. test move
     * to invalid location. test if we can move Piece in valid or invalid pos ition
     * by each player. bottom up test by move piece.
     */
    @Test
    public void testCapturePiece() {
        x1 = 1;
        y1 = 1;
        x2 = 1;
        y2 = 1;
        assertFalse("expected unsuccesfully capture the same piece",
                Action.capturePiece(chessplayer1, x1, y1, x2, y2, chessboard));
        x2 = 0;
        y2 = 0;
        assertFalse("expected unsuccesfully capture to your piece",
                Action.capturePiece(chessplayer1, x1, y1, x2, y2, chessboard));
        x1 = 1;
        y1 = 1;
        x2 = 0;
        y2 = 0;
        assertFalse("expected unsuccesfully capture from not your piece",
                Action.capturePiece(chessplayer2, x1, y1, x2, y2, chessboard));
        x1 = 2;
        y1 = 2;
        x2 = chessn - 1;
        y2 = chessn - 1;
        assertFalse("expected unsuccesfully capture from empty piece",
                Action.capturePiece(chessplayer1, x1, y1, x2, y2, chessboard));
        x2 = 0;
        y2 = -1;
        assertFalse("expected unsuccesfully capture to invalid place(not in the board)",
                Action.capturePiece(chessplayer1, x1, y1, x2, y2, chessboard));
        x1 = 1;
        y1 = 1;
        x2 = 4;
        y2 = 4;
        assertFalse("expected unsuccesfully capture to invalid place(withour a piece)",
                Action.capturePiece(chessplayer1, x1, y1, x2, y2, chessboard));
        x1 = 1;
        y1 = 1;
        x2 = chessn - 1;
        y2 = chessn - 1;
        assertTrue("expected successfully capture from p1 to p2",
                Action.capturePiece(chessplayer1, x1, y1, x2, y2, chessboard));
    }

    /**
     * covers: player1 is/not the same as plyaer2.
     */
    @Test
    public void testCalculatePiece() {
        assertEquals("expected same piece at the first",
                Action.calculatePiece(chessplayer1, chessplayer1, chessboard).getValue(),
                Action.calculatePiece(chessplayer1, chessplayer1, chessboard).getKey());
        assertEquals("expected same piece at the first",
                Action.calculatePiece(goplayer1, goplayer1, goboard).getValue(),
                Action.calculatePiece(goplayer1, goplayer1, goboard).getKey());
        assertEquals("expected go zero piece at the first", (Integer) (0),
                Action.calculatePiece(goplayer1, goplayer2, goboard).getKey());
        assertEquals("expected go zero piece at the first", (Integer) (0),
                Action.calculatePiece(goplayer1, goplayer2, goboard).getValue());
        assertEquals("expected chess 16 pieces at the first", (Integer) (16),
                Action.calculatePiece(chessplayer1, chessplayer2, chessboard).getKey());
        assertEquals("expected chess 16 pieces at the first", (Integer) (16),
                Action.calculatePiece(chessplayer1, chessplayer2, chessboard).getValue());
    }

    /**
     * location : in the board or not in the board, test empty test each player.
     */
    @Test
    public void testcheckLocation() {
        x1 = 0;
        y1 = 0;
        String answer = Action.checkLocation(x1, y1, chessboard);
        assertEquals("expected return", "offensive", answer);
        x1 = 7;
        y1 = 7;
        answer = Action.checkLocation(x1, y1, chessboard);
        assertEquals("expected return", "defensive", answer);
        answer = Action.checkLocation(x1, y1, goboard);
        assertEquals("expected return", "EMPTY", answer);
        x1 = 3;
        y1 = 3;
        answer = Action.checkLocation(x1, y1, chessboard);
        assertEquals("expected return", "EMPTY", answer);
    }

}
