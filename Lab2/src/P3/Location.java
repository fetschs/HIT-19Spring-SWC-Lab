package P3;

public class Location {

    private final int xaxis;
    private final int yaxis;

    // Abstraction function:
    // AF(xaxis) = X-axis
    // AF(yaxis) = Y-axis
    // Representation invariant:
    // no
    // Safety from rep exposure:
    // all fields are private and they are immutable
    // immutable
    public Location(int tx, int ty) {
        xaxis = tx;
        yaxis = ty;
    }

    public int getX() {
        return xaxis;
    }

    public int getY() {
        return yaxis;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            return ((((Location) (obj)).getX() == xaxis) 
                    && (((Location) (obj)).getY() == yaxis));
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        String ret = xaxis + " " + yaxis;
        return ret.hashCode();
    }
}
