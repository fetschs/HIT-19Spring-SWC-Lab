package work;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Gui {

  public static void main(String[] args) {
    JFrame jFrame = new JFrame();
    JTextArea textArea = new JTextArea(500,500);
    try {
      Scanner scanner = new Scanner(new File("log/monkey.log"));
      while (scanner.hasNext()){
        scanner.nextLine();
        textArea.append(scanner.nextLine().split(":")[1]+'\n');
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    JScrollPane jScrollPane = new JScrollPane(textArea);
    jScrollPane.setPreferredSize(new Dimension(150, 150));
    jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jFrame.add(jScrollPane);
    jFrame.setSize(new Dimension(900, 900));
    jFrame.setVisible(true);
  }
}
