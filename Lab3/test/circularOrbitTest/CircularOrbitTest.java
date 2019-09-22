package circularOrbitTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import centralPoint.CentralPoint;
import circularOrbit.CircularOrbit;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.junit.Before;
import org.junit.Test;
import relation.Relation;
import track.Track;

/**
 * @author FETS
 */
public abstract class CircularOrbitTest<L, E> {

  private CircularOrbit<L, E> circularOrbit;
  private TreeSet<Track> tracks;
  private List<E> objects;
  private CentralPoint<L> centralPoint;
  private List<Relation<L, E>> c2tRelations;
  private Map<E, List<Relation<E, E>>> t2tRelations;
  private Map<Track, List<E>> trackContent;


  /**
   * get a L instance.
   *
   * @param tempName the name of this instance.
   * @return the instance
   */
  abstract L createLInstance(String tempName);

  /**
   * get a E instance.
   *
   * @param tempName the name of this instance.
   * @return the instance.
   */
  abstract E createEInstance(String tempName);

  abstract CircularOrbit<L, E> createEmptyInstance();

  /*
   * Testing strategy
   * test some normal get/set method.
   * test the builder.
   * Partition the inputs as follows: test some valid input
   * remove  object which is not in this orbit
   *
   */
  @Before
  public void setUp() {
    circularOrbit = createEmptyInstance();
  }

  private void updateStatus() {
    tracks = circularOrbit.getTracks();
    objects = circularOrbit.getObjects();
    centralPoint = circularOrbit.getCentralPoint();
    c2tRelations = circularOrbit.getC2tRelations();
    t2tRelations = circularOrbit.getT2tRelations();
    trackContent = circularOrbit.getTrackContent();
  }

  /**
   * test the InterfaceMethod, mutator or observer.
   */
  @Test
  public void testInterfaceMethod() {
    L newCentral = createLInstance("Cn");
    E newObject = createEInstance("On");
    Track newTrack = new Track(BigDecimal.valueOf(123.321));
    circularOrbit.addTrack(newTrack);
    circularOrbit.addPhysicalObjectInTrack(newObject, newTrack);
    circularOrbit.addCentralPoint(newCentral);
    Relation<L, E> leRelation = new Relation<>(BigDecimal.ONE, newCentral, newObject);
    circularOrbit.addC2tRelation(leRelation);
    updateStatus();
    assertTrue("expected track add", tracks.contains(newTrack));
    assertTrue("expected object add", objects.contains(newObject));
    assertTrue("expected central add", centralPoint.getCentralObjects().contains(newCentral));
    assertTrue("expected track content", trackContent.get(newTrack).contains(newObject));
    assertTrue("expected c2tRelation add", c2tRelations.contains(leRelation));
    E anotherObject = createEInstance("anotherObject");
    Track anotherTrack = new Track(BigDecimal.valueOf(132.231));
    circularOrbit.addTrack(anotherTrack);
    circularOrbit.addPhysicalObjectInTrack(anotherObject, anotherTrack);
    Relation<E, E> eeRelation = new Relation<>(BigDecimal.ONE, newObject, anotherObject);
    circularOrbit.addT2tRelation(eeRelation);
    updateStatus();
    assertTrue("expected t2tRelation add", t2tRelations.get(newObject).contains(eeRelation));
    circularOrbit.removeTrack(anotherTrack);
    circularOrbit.removePhysicalObjectInTrack(newTrack, newObject);
    updateStatus();
    assertFalse("expected remove track", tracks.contains(anotherTrack));
    assertFalse("expected remove physicalObject", objects.contains(newObject));
  }

}
