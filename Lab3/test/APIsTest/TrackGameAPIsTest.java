package APIsTest;

import centralPoint.CentralPoint;
import circularOrbit.CircularOrbit;
import circularOrbit.TrackGame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import physicalObject.Athlete;
import physicalObject.AthleteFactory;
import physicalObject.PhysicalObjectFactory;

public class TrackGameAPIsTest extends CircularOrbitAPIsTest<Athlete, Athlete> {

  @Override
  Athlete createEInstance(String tempName) {
    PhysicalObjectFactory factory = new AthleteFactory();
    return (Athlete) factory
        .createPhysicalObject("Athlete ::= " + "<" + tempName + ",23,USA,24,9.12>");
  }


  @Override
  CircularOrbit<Athlete, Athlete> createEmptyOrbitInstance() {
    return TrackGame.builder().objects(new ArrayList<>()).tracks(new TreeSet<>())
        .trackContent(new HashMap<>()).c2tRelations(new ArrayList<>()).t2tRelations(new HashMap<>())
        .centralPoint(new CentralPoint<>(new ArrayList<>())).build();
  }
}