package P3;

import java.util.LinkedList;
import java.util.List;

public class Player {
    private final String name;
    private final List<Piece> pieces;
    private final String order;

    // Abstraction function:
    // AF(name) = name of this player
    // AF(pieces) = the pieces they have, will remove after lost them.
    // AF(order) = offensive or defensive
    // Representation invariant:
    // order = offensive or order = defensive
    // Safety from rep exposure:
    // defensive copy
    public Player(Game game, String id, String torder) {
        name = id;
        order = torder;
        if (torder.equals("offensive")) {
            pieces = game.getOffensive();
        } else {
            pieces = game.getDefensive();
        }
        checkRep();
    }

    private void checkRep() {
        assert order.equals("offensive") || order.equals("defensive");
    }

    public String getName() {
        return name;
    }

    public List<Piece> getPieces() {
        return new LinkedList<Piece>(pieces);
    }

    public String getOrder() {
        return order;
    }

    /**
     * remove a piece.
     * 
     * @param piece the piece which will be removed
     */
    public void updatePieces(Piece piece) {
        pieces.remove(piece);
    }
}
