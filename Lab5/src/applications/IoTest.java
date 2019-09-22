package applications;

import circularorbit.SocialNetworkCircle;
import circularorbit.TrackGame;
import circularorbit.iostrategy.ReaderAndWriterIo;
import circularorbit.iostrategy.ScannerAndFilesIo;
import circularorbit.iostrategy.StreamIo;
import circularorbit.trackgameother.RandomStrategy;
import exception.DependencyException;
import exception.GrammarException;
import exception.SameLabelException;
import java.io.File;
import java.io.IOException;

public class IoTest {

  /**
   * test IO.
   * @param args no use.
   */
  public static void main(String[] args) {
    String filename = "data/big/TrackGame.txt";
    TrackGame trackGame;
    SocialNetworkCircle socialNetworkCircle;
    long start = 0;
    long end = 0;
    try {
      start = System.currentTimeMillis();
      trackGame = TrackGame.creatInstanceFromClient(filename, new RandomStrategy(),
          new ScannerAndFilesIo(new File(filename)));
      end = System.currentTimeMillis();
      System.out.println(end - start);
      start = System.currentTimeMillis();
      trackGame = TrackGame.creatInstanceFromClient(filename, new RandomStrategy(),
          new ReaderAndWriterIo(new File(filename)));
      end = System.currentTimeMillis();
      System.out.println(end - start);
      start = System.currentTimeMillis();
      trackGame = TrackGame.creatInstanceFromClient(filename, new RandomStrategy(),
          new StreamIo(new File(filename)));
      end = System.currentTimeMillis();
      System.out.println(end - start);
      filename = "data/big/SocialNetworkCircle.txt";
      start = System.currentTimeMillis();
      socialNetworkCircle = SocialNetworkCircle.createInstanceFromClient(filename,
          new ScannerAndFilesIo(new File(filename)));
      end = System.currentTimeMillis();
      System.out.println(end - start);
      start = System.currentTimeMillis();
      socialNetworkCircle = SocialNetworkCircle
          .createInstanceFromClient(filename, new ReaderAndWriterIo(new File(filename)));
      end = System.currentTimeMillis();
      System.out.println(end - start);
      start = System.currentTimeMillis();
      socialNetworkCircle = SocialNetworkCircle
          .createInstanceFromClient(filename, new StreamIo(new File(filename)));
      end = System.currentTimeMillis();
      System.out.println(end - start);
    } catch (GrammarException | IOException | SameLabelException | DependencyException e) {
      e.printStackTrace();
    }
  }
}
