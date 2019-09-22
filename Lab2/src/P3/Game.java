package P3;

import java.util.LinkedList;
import java.util.List;

public class Game {
    private final String kind;
    private final List<Piece> offensive;
    private final List<Piece> defensive;
    public static Piece king = new Piece("king");
    public static Piece queen = new Piece("queen");
    public static Piece bishop = new Piece("bishop");
    public static Piece knight = new Piece("knight");
    public static Piece rook = new Piece("rook");
    public static Piece pawn = new Piece("pawn");
    public static Piece black = new Piece("black");
    public static Piece white = new Piece("white");
    public static Piece empty = new Piece("EMPTY");
    // Abstraction function:
    // AF(kind) = kind of this game
    // AF(offensive) = offensive pieces in this game
    // AF(defensive) = defensive pieces in this game
    // Representation invariant:
    // kind is go || kind is chess
    // Safety from rep exposure:
    // all files are private
    // defensive copying;
    /**
     * start a game, generate the pieces list. generate a board.
     * 
     * @param game    the kind of this game.
     * @param player1 the first player.
     * @param player2 the second player.
     */
    private void checkRep() {
        assert (kind.equals("go") || kind.equals("chess"));
    }
    /**
     * Game construct function, generate offensive and defensive pieces.
     * @param tkind the kind of this game.
     */
    
    public Game(String tkind) {
        kind = tkind;
        offensive = new LinkedList<>();
        defensive = new LinkedList<>();
        if (kind.equals("go")) {
            for (int i = 1; i <= 181; i++) {
                Piece piece = black;
                offensive.add(piece);
            }
            for (int i = 1; i <= 180; i++) {
                Piece piece = white;
                defensive.add(piece);
            }
        } else {
            for (int i = 1; i <= 1; i++) {
                defensive.add(king);
                offensive.add(king);
                defensive.add(queen);
                offensive.add(queen);
            }
            for (int i = 1; i <= 2; i++) {
                offensive.add(bishop);
                defensive.add(bishop);
                offensive.add(knight);
                defensive.add(knight);
                offensive.add(rook);
                defensive.add(rook);
            }
            for (int i = 1; i <= 8; i++) {
                offensive.add(pawn);
                defensive.add(pawn);
            }
        }
        checkRep();
    }

    public List<Piece> getOffensive() {
        return new LinkedList<Piece>(offensive);
    }

    public List<Piece> getDefensive() {
        return new LinkedList<Piece>(defensive);
    }

    public String getKind() {
        return kind;
    }
}
