package P3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MyChessAndGoGame {

    /**
     * client code.
     * 
     * @param args unused
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please input your choice: ");
        System.out.println("1.\"chess\": the international chess");
        System.out.println("2.\"go\": the weiqi");
        String kind = "";
        for(;;) {
            kind = scanner.nextLine();
            if(kind.equals("chess") == false && kind.equals("go") == false) {
                System.out.println("INPUT GAMEKIND AGAIN");
                continue;
            }
            break;
        }
        System.out.println("Please input the player1 name!");
        String name1 = scanner.nextLine();
        System.out.println("Please input the player2 name!");
        String name2 = scanner.nextLine();
        System.out.println("OK," + name1 + " VS " + name2 + " with " + kind + "!");
        System.out.println("Please input the size of the board!");
        int size = 8;
        if (kind == "go") {
            for (;;) {
                try {
                    size = Integer.valueOf(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("NOT A NUMBER, INPUT AGAIN.");
                }
                if (size <= 0) {
                    System.out.println("SIZE LESS THAN ZERO, INPUT AGAIN");
                }
            }
        }
        Game game = new Game(kind);
        Board board = new Board(game, size);
        Player player1 = new Player(game, name1, "offensive");
        Player player2 = new Player(game, name2, "defensive");
        System.out.println("Game Start!");
        System.out.println("Take turns to action.");
        Boolean over = false;
        List<String> actionlist = new ArrayList<String>();
        for (int i = 1;;) {
            Boolean valid = false;
            Player player;
            String name;
            if (i == 1) {
                player = player1;
                name = name1;
            } else {
                player = player2;
                name = name2;
            }
            System.out.println("Please " + name + 
                    " input your action as follows instruction in quotes");
            if (kind.equals("go")) {
                System.out.println("1.\"place x y piecekind\":place a new chess in (x,y)");
                System.out.println("2.\"remove x y\":remove the chess in (x,y)");
            } else {
                System.out.println("1.\"move x1 y1 x2 y2\":move a chess from (x1,y1) to (x2,y2)");
                System.out.println("2.\"capture x1 y1 x2 y2\":move a chess from (x1,y1) to (x2,y2)"
                        + "and capture the chess in (x2,y2)");
            }
            System.out.println("3.\"find x y\":find the chess in (x,y)");
            System.out.println("4.\"calculate\":calculate the sum of each player's chess");
            System.out.println("5.\"skip\":skip your turn");
            System.out.println("6.\"end\":end this game");
            String input = scanner.nextLine();
            String[] words = input.split(" ");
            String piecekind = "";
            String instruction = words[0];
            int x = 0;
            int y = 0;
            int x1 = 0;
            int y1 = 0;
            int x2 = 0;
            int y2 = 0;
            switch (instruction) {
            case "place":
                if (words.length != 4) {
                    break;
                }
                x = Integer.valueOf(words[1]);
                y = Integer.valueOf(words[2]);
                piecekind = words[3];
                Piece piece = new Piece(piecekind);
                valid = Action.placePiece(player, piece, x, y, board);
                break;
            case "remove":
                if (words.length != 3) {
                    break;
                }
                x = Integer.valueOf(words[1]);
                y = Integer.valueOf(words[2]);
                valid = Action.removePiece(player, x, y, board);
                break;
            case "move":
                if (words.length != 5) {
                    break;
                }
                x1 = Integer.valueOf(words[1]);
                y1 = Integer.valueOf(words[2]);
                x2 = Integer.valueOf(words[3]);
                y2 = Integer.valueOf(words[4]);
                valid = Action.movePiece(player, x1, y1, x2, y2, board);
                break;
            case "capture":
                if (words.length != 5) {
                    break;
                }
                x1 = Integer.valueOf(words[1]);
                y1 = Integer.valueOf(words[2]);
                x2 = Integer.valueOf(words[3]);
                y2 = Integer.valueOf(words[4]);
                valid = Action.capturePiece(player, x1, y1, x2, y2, board);
                break;
            case "find":
                if (words.length != 3) {
                    break;
                }
                x = Integer.valueOf(words[1]);
                y = Integer.valueOf(words[2]);
                String answer = Action.checkLocation(x, y, board);
                if (answer.equals("INVALID")) {
                    valid = false;
                    break;
                }
                valid = true;
                System.out.println(answer);
                break;
            case "calculate":
                if (words.length != 1) {
                    break;
                }
                Map.Entry<Integer, Integer> entry = Action.calculatePiece(player1, player2, board);
                System.out.println(player1.getName() + ":" + entry.getKey());
                System.out.println(player2.getName() + ":" + entry.getValue());
                valid = true;
                break;
            case "skip":
                if (words.length != 1) {
                    break;
                }
                valid = true;
                break;
            case "end":
                if (words.length != 1) {
                    break;
                }
                over = true;
                break;
            default:
                break;
            }
            if (over == true) {
                for(String action : actionlist) {
                    System.out.println(action);
                }
                System.out.println("Game Over!");
                scanner.close();
                break;
            }
            if (valid == false) {
                System.out.println("INVALID INSTRUCT!");
                System.out.println("INPUT YOUR ACTION AGAIN!");
                continue;
            } else {
                actionlist.add(player.getOrder()+": "+input);
                i = (i % 2) + 1;
            }
        }
    }

}
