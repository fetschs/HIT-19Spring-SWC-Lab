package circularOrbit;

import centralPoint.CentralPoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import lombok.NonNull;
import lombok.ToString;
import relation.Relation;
import track.Track;

@ToString
public abstract class CircularOrbit<L, E> {

  @NonNull
  protected TreeSet<Track> tracks;
  @NonNull
  protected List<E> objects;
  @NonNull
  protected Map<Track, List<E>> trackContent;
  protected CentralPoint<L> centralPoint;
  protected List<Relation<L, E>> c2tRelations;
  protected Map<E, List<Relation<E, E>>> t2tRelations;

  // Abstraction function:
  // AF(tracks) = the tracks in this orbit
  // AF(objects) = the objects in this orbit
  // AF(trackContent) = map to store objects in the track
  // AF(centralPoint) = the centralPoint in this orbit
  // AF(c2tRelations) = the relation list between central to trackObject
  // AF(t2tRelations) = like the adj list in a graph,
  // key is the object in orbit,and value a list of the key's relations
  // Representation invariant:
  // all the values in map should be in objects and has the same length
  // all the keys in map should be in tracks and has the same length
  // all the track key should have list value instead of null
  // all the object key should have list value instead of null
  // all the objects in relations should in this orbit
  // Safety from rep exposure:
  // all fields are private
  // defensive copy

  /**
   * All construction method.
   *
   * @param tracks as above.
   * @param objects as above.
   * @param trackContent as above.
   * @param centralPoint as above.
   * @param c2tRelations as above.
   * @param t2tRelations as above.
   */
  public CircularOrbit(TreeSet<Track> tracks, List<E> objects,
      Map<Track, List<E>> trackContent, CentralPoint<L> centralPoint,
      List<Relation<L, E>> c2tRelations,
      Map<E, List<Relation<E, E>>> t2tRelations) {
    this.tracks = tracks;
    this.objects = objects;
    this.trackContent = trackContent;
    this.centralPoint = centralPoint;
    this.c2tRelations = c2tRelations;
    this.t2tRelations = t2tRelations;
  }

  private void checkRep() {
    for (Track track : tracks) {
      assert trackContent.containsKey(track);
    }
    assert tracks.size() == trackContent.keySet().size();
    for (List<E> object : trackContent.values()) {
      assert objects.containsAll(object);
    }
    for (E object : objects) {
      t2tRelations.keySet().contains(object);
    }
    for (E object : t2tRelations.keySet()) {
      assert objects.contains(object);
    }
    for (Relation<L, E> relation : c2tRelations) {
      assert centralPoint.getCentralObjects().contains(relation.getFrom());
      assert objects.contains(relation.getTo());
    }
    for (List<Relation<E, E>> relations : t2tRelations.values()) {
      for (Relation<E, E> relation : relations) {
        assert objects.contains(relation.getFrom());
        assert objects.contains(relation.getTo());
      }
    }
  }

  /**
   * design a iterator.
   *
   * @return an iterator.
   */
  public abstract CircularOrbitIterator iterator();

  /**
   * add a track in this orbit.
   *
   * @param track will be added
   */
  public void addTrack(Track track) {
    tracks.add(track);
    if (!trackContent.containsKey(track)) {
      trackContent.put(track, new ArrayList<>());
    }
    checkRep();
  }

  /**
   * set a object in the central.
   *
   * @param tempCentralObject will be set
   */
  public void addCentralPoint(L tempCentralObject) {
    centralPoint.addCentralObject(tempCentralObject);
    checkRep();
  }

  /**
   * add a E in the track.
   *
   * @param trackObject will be added
   * @param track will update
   */
  public void addPhysicalObjectInTrack(E trackObject, Track track) {
    objects.add(trackObject);
    List<E> tempList = trackContent.get(track);
    tempList.add(trackObject);
    trackContent.put(track, tempList);
    t2tRelations.put(trackObject, new ArrayList<>());
    checkRep();
  }

  /**
   * remove a track in this orbit.
   *
   * @param track will be removed
   */
  public void removeTrack(Track track) {

    List<E> tempList = trackContent.get(track);
    for (E physicalObject : tempList) {
      objects.remove(physicalObject);
      removeRelation(physicalObject);
    }
    tracks.remove(track);
    trackContent.remove(track);
    checkRep();
  }

  /**
   * remove a physicalObject in a track.
   *
   * @param track the track which the physicalObject in
   * @param physicalObject the physicalObject will be removed
   */
  public void removePhysicalObjectInTrack(Track track, E physicalObject) {
    removeRelation(physicalObject);
    objects.remove(physicalObject);
    List<E> tempList = trackContent.get(track);
    tempList.remove(physicalObject);
    trackContent.put(track, tempList);
    checkRep();
  }

  /**
   * set the relation between the L object and the E object.
   *
   * @param relation the relation between the objects
   */
  public void addC2tRelation(Relation<L, E> relation) {
    c2tRelations.add(relation);
    checkRep();
  }

  /**
   * get this tracks.
   *
   * @return this tracks
   */
  public TreeSet<Track> getTracks() {
    return new TreeSet<>(tracks);
  }

  /**
   * get this objects.
   *
   * @return this objects.
   */
  public List<E> getObjects() {
    return new ArrayList<E>(objects);
  }

  /**
   * get this trackContent.
   *
   * @return this trackContent.
   */
  public Map<Track, List<E>> getTrackContent() {
    return new HashMap<Track, List<E>>(trackContent);
  }

  /**
   * get this centralPoint.
   *
   * @return this centralPoint.
   */
  public CentralPoint<L> getCentralPoint() {
    return new CentralPoint<>(centralPoint.getCentralObjects());
  }

  /**
   * get this c2tRelations.
   *
   * @return this c2tRelations.
   */
  public List<Relation<L, E>> getC2tRelations() {

    return new ArrayList<>(c2tRelations);
  }

  /**
   * get this t2tRelations.
   *
   * @return this t2tRelations.
   */
  public Map<E, List<Relation<E, E>>> getT2tRelations() {
    return new HashMap<>(t2tRelations);
  }

  /**
   * set the relation between the E object and the E object.
   *
   * @param relation the relation between the objects
   */
  public void addT2tRelation(Relation<E, E> relation) {
    E from;
    E to;
    try {
      from = relation.getFrom();
      to = relation.getTo();
    } catch (NullPointerException e) {
      e.printStackTrace();
      return;
    }
    List<Relation<E, E>> fromList = t2tRelations.get(from);
    List<Relation<E, E>> toList = t2tRelations.get(to);
    fromList.add(relation);
    toList.add(relation);
    t2tRelations.put(from, fromList);
    t2tRelations.put(to, toList);
    checkRep();
  }

  /**
   * remove the object's relation in this orbit.
   *
   * @param object as above
   */
  private void removeRelation(E object) {
    c2tRelations.removeIf(relation -> relation.getTo().equals(object));
    for (List<Relation<E, E>> relations : t2tRelations.values()) {
      relations.removeIf(relation -> relation.getFrom().equals(object));
      relations.removeIf(relation -> relation.getTo().equals(object));
    }
    t2tRelations.remove(object);
  }


}
