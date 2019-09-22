package circularOrbitTest;

import static org.junit.Assert.assertEquals;

import centralPoint.CentralPoint;
import circularOrbit.CircularOrbit;
import circularOrbit.TrackGame;
import circularOrbit.TrackGameOther.BestStrategy;
import circularOrbit.TrackGameOther.RandomStrategy;
import circularOrbit.TrackGameOther.TrackGameStrategy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import org.junit.Test;
import physicalObject.Athlete;
import physicalObject.AthleteFactory;
import physicalObject.PhysicalObjectFactory;

public class TrackGameTest extends CircularOrbitTest<Athlete, Athlete> {

  @Override
  CircularOrbit<Athlete, Athlete> createEmptyInstance() {
    return TrackGame.builder().objects(new ArrayList<>()).tracks(new TreeSet<>())
        .trackContent(new HashMap<>()).c2tRelations(new ArrayList<>()).t2tRelations(new HashMap<>())
        .centralPoint(new CentralPoint<>(new ArrayList<>())).build();
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

  @Test
  public void creatInstanceFromClient() {
    TrackGameStrategy strategy = new RandomStrategy();
    TrackGame trackGame = TrackGame.creatInstanceFromClient("data/TrackGame.txt", strategy);
    assertEquals("expected Game", "100", trackGame.getGame());
    assertEquals("expected Size", 3, trackGame.getGroups().size());
    trackGame = TrackGame.creatInstanceFromClient("data/TrackGame_Medium.txt", strategy);
    assertEquals("expected Game", "100", trackGame.getGame());
    assertEquals("expected Size", 7, trackGame.getTracks().size());
    strategy = new BestStrategy();
    trackGame = TrackGame.creatInstanceFromClient("data/TrackGame_Larger.txt", strategy);
    assertEquals("expected Game", "100", trackGame.getGame());
    assertEquals("expected Size", 8, trackGame.getTracks().size());
  }
}