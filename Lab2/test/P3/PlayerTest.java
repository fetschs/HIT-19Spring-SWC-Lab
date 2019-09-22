package P3;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
    Game gamego;
    Game gamechess;
    Player goplayer1;
    Player goplayer2;
    Player chessplayer1;
    Player chessplayer2;
    @Before
    public void setUp() throws Exception {
        gamego = new Game("go");
        gamechess = new Game("chess");
        goplayer1 = new Player(gamego, "a", "offensive");
        goplayer2 = new Player(gamego, "b", "defensive");
        chessplayer1 = new Player(gamechess, "a", "offensive");
        chessplayer2 = new Player(gamechess, "b", "defensive");
    }
    //test the construct function,test the piece list include board chess
    @Test
    public void testPlayer() {
        assertFalse("expected not the same size",
                goplayer1.getPieces().size() == goplayer2.getPieces().size());
        assertTrue("expected the same size",
                chessplayer1.getPieces().size() == chessplayer2.getPieces().size());
    }
    //test if we can remove the piece from player
    @Test
    public void testUpdate() {
        Piece piece = Game.king;
        chessplayer1.updatePieces(piece);
        assertFalse("have remove it",chessplayer1.getPieces().contains(piece));
    }
}
