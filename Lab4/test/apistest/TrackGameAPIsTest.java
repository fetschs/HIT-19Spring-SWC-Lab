package apistest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import centralpoint.CentralPoint;
import circularorbit.CircularOrbit;
import circularorbit.CircularOrbitIterator;
import circularorbit.TrackGame;
import circularorbit.trackgameother.BestStrategy;
import circularorbit.trackgameother.RandomStrategy;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.junit.Test;
import physicalobject.Athlete;
import physicalobject.AthleteFactory;
import physicalobject.PhysicalObjectFactory;
import track.Track;

public class TrackGameAPIsTest extends CircularOrbitApisTest<Athlete, Athlete> {

  @Override
  Athlete createEInstance(String tempName) {
    PhysicalObjectFactory factory = new AthleteFactory();
    return (Athlete) factory
        .createPhysicalObject("Athlete ::= " + "<" + tempName + ",23,USA,24,9.12>");
  }


  @Override
  CircularOrbit<Athlete, Athlete> createEmptyOrbitInstance() {
    TreeSet<Track> tracks = new TreeSet<>();
    Map<Track, List<Athlete>> content = new HashMap<>();
    for (int i = 1; i <= 4; i++) {
      Track track = new Track(BigDecimal.valueOf(i));
      tracks.add(track);
      content.put(track, new ArrayList<>());
    }
    return TrackGame.builder().objects(new ArrayList<>()).tracks(tracks).game("100")
        .trackContent(content).c2tRelations(new ArrayList<>()).t2tRelations(new HashMap<>())
        .centralPoint(new CentralPoint<>(new ArrayList<>())).groups(new ArrayList<>()).build();
  }

  /**
   * test the trackGame iterator because  it is special.
   */
  @Test
  public void testTrackGameIterator() throws Exception{
    try {
      CircularOrbit<Athlete, Athlete> temp = TrackGame
          .creatInstanceFromClient("data/TrackGame.txt", new BestStrategy());
      CircularOrbitIterator<Athlete> trackGameIterator = temp.iterator();
      assertEquals("Ronaldo", trackGameIterator.next().getName());
      temp = TrackGame.creatInstanceFromClient("data/TrackGame.txt", new RandomStrategy());
      temp.iterator();
    } catch (Exception e) {
      fail();
      throw e;
    }
  }
}