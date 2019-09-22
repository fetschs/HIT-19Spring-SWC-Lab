package applications;

import circularorbit.SocialNetworkCircle;
import circularorbit.TrackGame;
import circularorbit.iostrategy.ScannerAndFilesIo;
import circularorbit.trackgameother.RandomStrategy;
import exception.DependencyException;
import exception.GrammarException;
import exception.InvalidInputException;
import exception.SameLabelException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import physicalobject.Athlete;
import physicalobject.AthleteFactory;
import physicalobject.PhysicalObjectFactory;
import track.Track;

public class GcTest {

  /**
   * test gc.
   *
   * @param args no use.
   * @throws InterruptedException just throw.
   */
  public static void main(String[] args) throws InterruptedException {
    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    String filename = "data/big/TrackGame.txt";
    TrackGame trackGame;
    SocialNetworkCircle socialNetworkCircle;
    try {
      trackGame = TrackGame.creatInstanceFromClient(filename, new RandomStrategy(),
          new ScannerAndFilesIo(new File(filename)));
      System.err.println("TrackGameOver");
      Track track = new Track(new BigDecimal(1145141919));
      trackGame.addTrack(track);
      trackGame.removeTrack(track);
      track = new Track(new BigDecimal(1145141920));
      trackGame.addTrack(track);
      PhysicalObjectFactory physicalObjectFactory = new AthleteFactory();
      Athlete athlete = (Athlete) physicalObjectFactory
          .createPhysicalObject("Athlete ::= <Bolt,1,JAM,38,9.94>");
      trackGame.addPhysicalObjectInTrack(athlete, track);
      trackGame.removePhysicalObjectInTrack(track, athlete);
      filename = "data/big/SocialNetworkCircle.txt";
      socialNetworkCircle = SocialNetworkCircle.createInstanceFromClient(filename,
          new ScannerAndFilesIo(new File(filename)));
      System.err.println("test finish!");
    } catch (InvalidInputException | GrammarException
        | IOException | SameLabelException | DependencyException e) {
      e.printStackTrace();
    }
  }
}
