package P1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MagicSquare {
    /**
     * read the file, and judge if magic square.
     * 
     * @param fileName the name of file
     * @return
     */
    public static boolean isLegalMagicSquare(String fileName) {
        BufferedReader reader = null;
        int row = 0;
        int column = 0;
        ArrayList<int[]> square = new ArrayList<int[]>();
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                String[] tempStrings = tempString.split("\\t");
                if (column != tempStrings.length && column != 0) {
                    System.out.println("not a square!");
                    return false;
                } // check if square
                column = tempStrings.length;
                int[] tempValues = new int[column];
                for (int i = 0; i < column; i++) {
                    Pattern pattern = Pattern.compile("([^\\d])(\\d+)");
                    Matcher matcher = pattern.matcher(tempStrings[i]);
                    Integer value = null;
                    try {
                        value = Integer.valueOf(tempStrings[i]);
                        if (value < 0) {
                            System.out.println("not positive integer!");
                            return false;
                        }
                    } catch (Exception e) {
                        System.out.println("not split by \\t!");
                        return false;
                    }

                    if (matcher.find()) {
                        System.out.println("not split by \\t!");
                        System.out.println(tempStrings[i]);
                        return false;
                    }
                    tempValues[i] = value;

                }
                square.add(tempValues);
                row++;
            } // check value of this square
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (row != column) {
            System.out.println("row not equal to column");
            return false;
        } // check row and column
        int lastsum = -1;
        for (int i = 0; i < row; i++) {
            int nowsum = 0;
            for (int j = 0; j < column; j++) {
                nowsum += square.get(i)[j];
            }
            if (nowsum != lastsum && lastsum != -1) {
                return false;
            }
        }
        for (int j = 0; j < column; j++) {
            int nowsum = 0;
            for (int i = 0; i < row; i++) {
                nowsum += square.get(i)[j];
            }
            if (nowsum != lastsum && lastsum != -1) {
                return false;
            }
        }
        for (int i = 0; i < column; i++) {
            int nowsum = 0;
            nowsum += square.get(i)[i];
            if (nowsum != lastsum && lastsum != -1) {
                return false;
            }
        } // check sum if suit for magic
        return true;
    }

    /**
     * generate a n*n magic square.
     * 
     * @param n the size of magic square
     * @return
     */

    public static boolean generateMagicSquare(int n) {
        if (n % 2 == 0 || n <= 0) {
            return false;
        }
        int[][] magic = new int[n][n]; // the square array
        int row = 0, col = n / 2, i, j, square = n * n;

        for (i = 1; i <= square; i++) {
            magic[row][col] = i;// fill the number
            if (i % n == 0) {
                row++;
            } else {
                if (row == 0) {
                    row = n - 1;
                } else {
                    row--;
                }
                if (col == (n - 1)) {
                    col = 0;
                } else {
                    col++;
                }
            } // update the current location
        }
        FileWriter writeName = null;
        BufferedWriter writer = null;
        try {
            writeName = new FileWriter("src/P1/6.txt");
            writer = new BufferedWriter(writeName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                try {
                    writer.write(magic[i][j] + "\t");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                writer.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            if(writer!=null)
                writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        } // print the magic square
        return true;
    }

    public static void main(String[] args) {

        System.out.println(MagicSquare.isLegalMagicSquare("src/P1/1.txt"));
        System.out.println(MagicSquare.isLegalMagicSquare("src/P1/2.txt"));
        System.out.println(MagicSquare.isLegalMagicSquare("src/P1/3.txt"));
        System.out.println(MagicSquare.isLegalMagicSquare("src/P1/4.txt"));
        System.out.println(MagicSquare.isLegalMagicSquare("src/P1/5.txt"));

        MagicSquare.generateMagicSquare(3);
        System.out.println(MagicSquare.isLegalMagicSquare("src/P1/6.txt"));
    }

}
