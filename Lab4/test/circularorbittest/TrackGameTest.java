package circularorbittest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import centralpoint.CentralPoint;
import circularorbit.CircularOrbit;
import circularorbit.TrackGame;
import circularorbit.trackgameother.BestStrategy;
import circularorbit.trackgameother.RandomStrategy;
import circularorbit.trackgameother.TrackGameStrategy;
import exception.GrammarException;
import exception.SameLabelException;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import org.junit.Test;
import physicalobject.Athlete;
import physicalobject.AthleteFactory;
import physicalobject.PhysicalObjectFactory;
import track.Track;

public class TrackGameTest extends CircularOrbitTest<Athlete, Athlete> {

  @Override
  CircularOrbit<Athlete, Athlete> createEmptyInstance() {
    TreeSet<Track> tracks = new TreeSet<>();
    for (int i = 1; i <= 4; i++) {
      tracks.add(new Track(BigDecimal.valueOf(i)));
    }
    return TrackGame.builder().objects(new ArrayList<>()).tracks(tracks).game("100")
        .trackContent(new HashMap<>()).c2tRelations(new ArrayList<>()).t2tRelations(new HashMap<>())
        .centralPoint(new CentralPoint<>(new ArrayList<>())).groups(new ArrayList<>()).build();
  }

  @Override
  Athlete createLInstance(String tempName) {
    PhysicalObjectFactory factory = new AthleteFactory();
    return (Athlete) factory
        .createPhysicalObject("Athlete ::= " + "<" + tempName + ",23,USA,24,8.12>");
  }

  @Override
  Athlete createEInstance(String tempName) {
    PhysicalObjectFactory factory = new AthleteFactory();
    return (Athlete) factory
        .createPhysicalObject("Athlete ::= " + "<" + tempName + ",23,USA,24,9.12>");
  }

  //test createInstance from file, use normal file or invalid file ,try to get Exception, check the message.
  @Test
  public void creatInstanceFromClient() {
    TrackGame trackGame1 = null;
    TrackGame trackGame2 = null;
    TrackGameStrategy strategy = new RandomStrategy();
    try {
      trackGame1 = TrackGame.creatInstanceFromClient("data/TrackGameForStrategy.txt", strategy);
      trackGame1 = TrackGame.creatInstanceFromClient("data/TrackGame.txt", strategy);
      trackGame2 = TrackGame.creatInstanceFromClient("data/TrackGame_Medium.txt", strategy);
      strategy = new BestStrategy();
      trackGame1 = TrackGame.creatInstanceFromClient("data/TrackGameForStrategy.txt", strategy);
      trackGame1 = TrackGame.creatInstanceFromClient("data/TrackGame.txt", strategy);
      trackGame2 = TrackGame.creatInstanceFromClient("data/TrackGame_Medium.txt", strategy);
    } catch (GrammarException | FileNotFoundException | SameLabelException e) {
      e.printStackTrace();
      fail();
    }
    assertEquals("expected Game", "100", trackGame1.getGame());
    assertEquals("expected Size", 3, trackGame1.getGroups().size());
    assertEquals("expected Game", "100", trackGame2.getGame());
    assertEquals("expected Size", 7, trackGame2.getTracks().size());
    Set<String> messages = new HashSet<>();
    for (int i = 1; i <= 11; i++) {
      try {
        TrackGame errorTrackGame = TrackGame
            .creatInstanceFromClient("errdata/Track_err_" + i +".txt", strategy);
      } catch (GrammarException | FileNotFoundException | SameLabelException e) {
        messages.add(e.getMessage());
      }
    }
    assertEquals("6 exceptions input", 6, messages.size());
  }

}