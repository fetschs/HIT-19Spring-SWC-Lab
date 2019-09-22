package P3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Board {
    private final Map<Location, Position> positions;
    private final List<Position> offensive;
    private final List<Position> defensive;
    private int size;

    // Abstraction function:
    // AF(positions) = the position status in this board
    // AF(size) = the board will be size*size. size should be >0
    // AF(offensive) = the position of offensive player
    // AF(defensive) = the position of defensive player
    // Representation invariant:
    // the positions.size should be the size*size
    // Safety from rep exposure:
    // defensive copy

    private void checkRep() {
        assert positions.size() == size * size;
    }

    /**
     * put the chess in line x and line x+delta.
     * 
     * @param x     as above
     * @param delta as above
     */
    private List<Position> putChess(int x, int delta) {
        List<Position> ret = new LinkedList<>();
        Location location = new Location(x, 0);
        Position position = new Position(Game.rook);
        ret.add(position);
        positions.put(location, position);
        location = new Location(x, size - 1);
        ret.add(position);
        positions.put(location, position);
        location = new Location(x, 1);
        position = new Position(Game.knight);
        ret.add(position);
        positions.put(location, position);
        location = new Location(x, size - 2);
        ret.add(position);
        positions.put(location, position);
        location = new Location(x, 2);
        position = new Position(Game.bishop);
        ret.add(position);
        positions.put(location, position);
        location = new Location(x, size - 3);
        ret.add(position);
        positions.put(location, position);
        location = new Location(x, 3);
        position = new Position(Game.king);
        ret.add(position);
        positions.put(location, position);
        location = new Location(x, 4);
        position = new Position(Game.queen);
        ret.add(position);
        positions.put(location, position);
        position = new Position(Game.pawn);
        for (int y = 0; y < size; y++) {
            location = new Location(x + delta, y);
            ret.add(position);
            positions.put(location, position);
        }
        checkRep();
        return ret;
    }

    /**
     * construction method.
     * 
     * @param game  this game
     * @param nsize the size of this board.
     */
    public Board(Game game, int nsize) {
        offensive = new LinkedList<>();
        defensive = new LinkedList<>();
        positions = new HashMap<>();
        size = nsize;
        if (game.getKind() == "go") {
            size++;
        }
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Location location = new Location(x, y);
                Position position = new Position(Game.empty);
                positions.put(location, position);
            }
        }
        if (game.getKind().equals("chess")) {
            offensive.addAll(putChess(0, 1));
            defensive.addAll(putChess(size - 1, -1));
        }
        checkRep();
    }

    public int getSize() {
        return size;
    }

    public Map<Location, Position> getPositions() {
        return new HashMap<Location, Position>(positions);
    }

    /**
     * get the sum of the player's pieces.
     * 
     * @param player the player who we want to know more
     * @return the sum of pieces
     */

    public int getSumofpieces(Player player) {
        if (player.getOrder().equals("offensive")) {
            return offensive.size();
        } else {
            return defensive.size();
        }
    }

    /**
     * update the Positions List.(means add or remove,depends on the position)/
     * 
     * @param location the location of this position.
     * @param position the new position.
     * @param player   the player who action.
     * @return means success or not
     */

    public Boolean updatePositions(Location location, Position position, Player player) {
        if (positions.containsKey(location) == false) {
            return false;
        }
        Position old = positions.get(location);
        offensive.remove(old);
        defensive.remove(old);
        positions.put(location, position);
        checkRep();
        if(position.getPiece().equals(Game.empty)) {
            return true;
        }
        if (player.getOrder().equals("offensive")) {
            offensive.add(position);
        } else {
            defensive.add(position);
        }
        return true;
    }

    /**
     * get the Owner of this location.
     * 
     * @param location the location which we want to know, should be in the board.
     * @return offensive/defensive means the owner, EMPTY means no owner, INVALID
     *         means invalid location.
     */
    public String getOwner(Location location) {
        if (positions.containsKey(location) == false) {
            return ("INVALID");
        }
        Position position = positions.get(location);
        if (offensive.contains(position)) {
            return "offensive";
        } else if (defensive.contains(position)) {
            return "defensive";
        } else {
            return "EMPTY";
        }
    }
}
