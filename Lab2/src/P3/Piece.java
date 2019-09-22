package P3;

public class Piece {
    private final String kind;

    // Abstraction function:
    // AF(kind) = kind of this piece
    // Representation invariant:
    // piece shouldn't be null
    // Safety from rep exposure:
    // all fields are private and immutable
    // immutable
    public Piece(String piece) {
        kind = piece;
        checkRep();
    }
    private void checkRep() {
        assert (kind != null);
    }
    public String getKind() {
        return kind;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Piece) {
            return (((Piece) (obj)).getKind().equals(kind));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.kind.hashCode();
    }
}
