package P3;

import java.util.HashMap;
import java.util.Map;


public class Action {

    /**
     * place a new piece in (x,y).
     * 
     * @param player the player who place piece
     * @param x      the X-axis 
     * @param y      the Y-axis 
     * @param board  current board.
     * @return true means we place it ,otherwise we can't place it.
     */
    public static Boolean placePiece(Player player, Piece piece, int x, int y, Board board) {
        Location location = new Location(x, y);
        Position position = new Position(piece);
        if ((player.getPieces().contains(piece) == false)
                || (board.getPositions().values().contains(position) == true)) {
            return false;
        }// player don't have this chess or this chess if in the board
        String situation = checkLocation(x, y, board);
        if ((situation.equals("EMPTY") == false) || (situation.equals("INVALID") == true)) {
            return false;
        }

        return board.updatePositions(location, position, player);

    }

    /**
     * remove the piece in (x,y).
     * 
     * @param player the player who remove piece, only can remove the other
     * @param x      the X-axis 
     * @param y      the Y-axis 
     * @param board  current board
     * @return true means we remove it ,otherwise we can't.
     */
    public static Boolean removePiece(Player player, int x, int y, Board board) {
        String situation = checkLocation(x, y, board);
        if ((situation.equals("EMPTY") == true) || (situation.equals(player.getOrder()) == true)
                || (situation.equals("INVALID"))) {
            return false;
        }
        Location location = new Location(x, y);
        Position position = new Position(Game.empty);
        return board.updatePositions(location, position, player);
    }

    /**
     * move the piece in (x1,y1) to (x2,y2).
     * 
     * @param player the player who move piece
     * @param x1     the X-axis 
     * @param y1     the Y-axis
     * @param x2     the X-axis 
     * @param y2     the Y-axis 
     * @param board  the board of this game
     * @return true means we move it, otherwise we can't
     */
    public static Boolean movePiece(Player player, int x1, int y1, int x2, int y2, Board board) {
        if (x1 == x2 && y1 == y2) {
            return false;
        }
        String situation = checkLocation(x1, y1, board);
        if ((situation.equals("EMPTY") == true) || (situation.equals(player.getOrder()) == false)
                || (situation.equals("INVALID") == true)) {
            return false;
        }
        situation = checkLocation(x2, y2, board);
        if ((situation.equals("EMPTY") == false) || (situation.equals("INVALID") == true)) {
            return false;
        }
        Location location = new Location(x1, y1);
        Position position = new Position(board.getPositions().get(location).getPiece());
        Boolean check = false;
        location = new Location(x2, y2);
        check = board.updatePositions(location, position, player);
        if (check == false) {
            return false;
        }
        location = new Location(x1, y1);
        position = new Position(Game.empty);
        check = board.updatePositions(location, position, player);
        return check;
    }

    /**
     * move the piece in (x1,y1) to (x2,y2), and remove (x2,y2).
     * 
     * @param player the player who move piece
     * @param x1     the X-axis
     * @param y1     the Y-axis
     * @param x2     the X-axis 
     * @param y2     the Y-axis
     * @param board  the game's board
     * @return true means we caputre it, otherwise we can't
     */
    public static Boolean capturePiece(Player player, int x1, int y1, int x2, int y2, Board board) {
        if (x1 == x2 && y1 == y2) {
            return false;
        }
        String situation = checkLocation(x1, y1, board);
        if ((situation.equals("EMPTY") == true) || (situation.equals(player.getOrder()) == false)
                || (situation.equals("INVALID") == true)) {
            return false;
        }
        situation = checkLocation(x2, y2, board);
        if (situation.equals("EMPTY") == true || (situation.equals(player.getOrder()) == true)
                || (situation.equals("INVALID") == true)) {
            return false;
        }
        Location location = new Location(x1, y1);
        Position position = new Position(board.getPositions().get(location).getPiece());
        Boolean check = false;
        location = new Location(x2, y2);
        check = board.updatePositions(location, position, player);
        if (check == false) {
            return false;
        }
        location = new Location(x1, y1);
        position = new Position(Game.empty);
        check = board.updatePositions(location, position, player);
        return check;
    }

    /**
     * calculate the piece of each player.
     * 
     * @param player1 as above
     * @param player2 as above
     * @return a map.entry, key for player1, value for player2
     */
    public static Map.Entry<Integer, Integer> 
    calculatePiece(Player player1, Player player2, Board board) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(board.getSumofpieces(player1), board.getSumofpieces(player2));
        Map.Entry<Integer, Integer> reEntry = null;
        for(Map.Entry<Integer, Integer> entry : map.entrySet()) {
            reEntry = entry;
        }
        return reEntry;
    }

    /**
     * check the situation of (x,y).
     * 
     * @param x     the X-axis
     * @param y     the Y-axis
     * @param board the current board
     * @return the order of the owner of this situation, if return EMPTY means no
     *         owner, if return INVALID means invalid (x,y).
     */
    public static String checkLocation(int x, int y, Board board) {
        if (x < 0 || x >= board.getSize() || y < 0 || y >= board.getSize()) {
            return "INVALID";
        }
        Location location = new Location(x, y);
        return board.getOwner(location);
    }
}
