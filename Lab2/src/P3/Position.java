package P3;

public class Position {
    private final Piece piece;

    // Abstraction function:
    // AF(piece) = the piece in this position
    // Representation invariant:
    // piece shouldn't be null
    // Safety from rep exposure:
    // all fields are private and immutable
    // immutable
    public Position(Piece tpiece) {
        piece = tpiece;
        checkRep();
    }
    private void checkRep() {
        assert piece != null;
    }
    public Piece getPiece() {
        return piece;
    }

}
