package work;

import entity.Ladder;
import entity.Monkey;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MonkeyGenerator {

  public static final Logger logger = Logger.getLogger("Monkey action");
  private static int nowMonkey = 0;//next monkey id
  private static int nowLadder = 0;//next ladder id
  public static int oneSecond = 10;// one second in fact
  public static final List<Ladder> ladders = Collections
      .synchronizedList(new ArrayList<>());//ladders for share
  private static int n;//the number of ladders
  private static int h;//the number of rungs in a ladder
  private static int t;
  private static int N;
  private static int k;
  private static int MV;
  //each t second generate k monkey which speed in [1,MV],
  //the number of all the monkeys is N.
  private static List<Thread> monkeyThreads = Collections
      .synchronizedList(new ArrayList<>());// thread list this time
  private static List<Monkey> generateMonkeys = Collections
      .synchronizedList(new ArrayList<>());// monkey list this time
  private static Map<Integer, Integer> time2k = new HashMap<>();// each time monkeys
  public static long begin;// the begin time
  private static long end;// the end time
  private static double Th;// in and out rate
  private static double fairness = 0;// the fairness

  // a static class ,use to start work for simulate.
  // some static input.

  public static void readDataFromFile(String fileName) throws IOException {
    Scanner scanner = new Scanner(new File(fileName), "utf-8");
    while (scanner.hasNext()) {
      String tempLine = scanner.nextLine();
      int equalsIndex = tempLine.indexOf('=');
      if (equalsIndex != -1) {
        String left = tempLine.substring(0, equalsIndex);
        String right = tempLine.substring(equalsIndex + 1, tempLine.length());
        switch (left) {
          case "n": {
            n = Integer.parseInt(right);
            break;
          }
          case "h": {
            h = Integer.parseInt(right);
            break;
          }
          case "t": {
            t = Integer.parseInt(right);
            break;
          }
          case "N": {
            N = Integer.parseInt(right);
            break;
          }
          case "k": {
            k = Integer.parseInt(right);
            break;
          }
          case "MV": {
            MV = Integer.parseInt(right);
            break;
          }
          default:
            throw new IOException("Invalid File");
        }
      }
    }
  }

  public static void generate() {
    begin = System.currentTimeMillis();
    long delay = 0;
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        Random random = new Random(System.currentTimeMillis());
        for (int i = 1; i <= k; i++) {
          Monkey monkey = new Monkey(++nowMonkey,
              random.nextBoolean() ? Monkey.L2R : Monkey.R2L, random.nextInt(MV) + 1);
          generateMonkeys.add(monkey);
          Thread monkeyThread = new Thread(monkey);
          monkeyThreads.add(monkeyThread);
          monkeyThread.start();
          if (nowMonkey == N) {
            for (Thread thread : monkeyThreads) {
              try {
                thread.join();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
            evaluate();
            System.out.println(Th);
            System.out.println(fairness);
            timer.cancel();
            break;
          }
        }
      }
    }, delay, oneSecond * t);
  }

  public static void generateV3(String fileName) {
    Scanner scanner = null;
    try {
      scanner = new Scanner(new File(fileName), "utf-8");
    } catch (IOException e) {
      e.printStackTrace();
    }
    String tempLine = scanner.nextLine();
    String right = tempLine.substring(2);
    int maxTime = -1;
    n = Integer.parseInt(right);
    tempLine = scanner.nextLine();
    right = tempLine.substring(2, tempLine.length());
    h = Integer.parseInt(right);
    for (int i = 1; i <= n; i++) {
      ladders.add(new Ladder(++nowLadder, h));
    }
    while (scanner.hasNext()) {
      tempLine = scanner.nextLine();
      tempLine = tempLine.split("=")[1];
      String[] tempLines = tempLine.split(",");
      tempLines[0] = tempLines[0].substring(1);
      tempLines[3] = tempLines[3].substring(0, tempLines[3].length() - 1);
      Monkey monkey = new Monkey(++nowMonkey, tempLines[2], Integer.parseInt(tempLines[3]));
      generateMonkeys.add(monkey);
      System.out.println(tempLines[0]);
      maxTime = Math.max(maxTime, Integer.parseInt(tempLines[0]));
      time2k.put(Integer.parseInt(tempLines[0]),
          time2k.getOrDefault(Integer.parseInt(tempLines[0]), 0) + 1);
    }
    int nowMonkey = 0;
    begin = System.currentTimeMillis();
    for (int i = 1; i <= maxTime; i++) {
      for (int j = 1; j <= time2k.getOrDefault(i, 0); j++) {
        Monkey monkey = generateMonkeys.get(++nowMonkey);
        Thread thread = new Thread(monkey);
        monkeyThreads.add(thread);
        thread.start();
      }
      try {
        Thread.sleep(MonkeyGenerator.oneSecond);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    for (Thread thread : monkeyThreads) {
      try {
        thread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    evaluate();
    System.out.println(Th);
    System.out.println(fairness);
  }

  public static void evaluate() {
    end = System.currentTimeMillis();
    double cost = (end - begin) / oneSecond;
    Th = generateMonkeys.size() / cost;
    for (int i = 0; i < generateMonkeys.size(); i++) {
      for (int j = i + 1; j < generateMonkeys.size(); j++) {
        Monkey monkey1 = generateMonkeys.get(i);
        Monkey monkey2 = generateMonkeys.get(j);
        if (monkey1.equals(monkey2)) {
          continue;
        }
        long temp = (monkey2.getEndTime() - monkey1.getEndTime()) * (monkey2.getBornTime() - monkey1
            .getBornTime());
        if (temp >= 0) {
          fairness++;
        } else {
          fairness--;
        }
      }
    }
    fairness = fairness / (generateMonkeys.size() * (generateMonkeys.size() - 1) / 2);
  }

  public static void main(String[] args) {
    try {
      FileHandler fileHandler = new FileHandler("log/monkey.log");
      fileHandler.setFormatter(new SimpleFormatter());
      logger.addHandler(fileHandler);
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
//    try {
//      readDataFromFile("data/v1data1.txt");
//    } catch (IOException e) {
//      System.err.println("ERROR INPUT FILE!");
//      return;
//    }
//    for (int i = 1; i <= n; i++) {
//      ladders.add(new Ladder(++nowLadder, h));
//    }
//    generate();
    generateV3("data/Competition_3.txt");
  }
}
