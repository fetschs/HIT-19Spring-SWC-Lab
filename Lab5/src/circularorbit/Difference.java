package circularorbit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import lombok.ToString;
import track.Track;


@ToString
public class Difference<L, E> {

  private final CircularOrbit<L, E> c1;
  private final CircularOrbit<L, E> c2;
  private final int trackNumber;
  private final int[] objectNumber;
  private final List<List<E>> firstUnique;
  private final List<List<E>> secondUnique;
  // Abstraction function:
  // AF(c1/c2) two circularOrbit
  // AF(trackNumber) the difference of tracks
  // AF(objectNumber) the objectNumber
  // AF(firstUnique) the unique object in c1
  // AF(secondUnique) the unique object in c2
  // Representation invariant:
  // c1 instance of c2,c1.tracks.size()>=c2
  // Safety from rep exposure:
  // all fields are private and final
  // defensive copy

  /**
   * construction Difference.
   *
   * @param tempC1 one orbit
   * @param tempC2 another orbit
   */
  public Difference(CircularOrbit<L, E> tempC1, CircularOrbit<L, E> tempC2) {
    List<List<E>> tempFirstUnique = new ArrayList<>();
    List<List<E>> tempSecondUnique = new ArrayList<>();
    boolean reverse = false;
    trackNumber = tempC1.getTracks().size() - tempC2.getTracks().size();
    if (tempC1.getTracks().size() < tempC2.getTracks().size()) {
      CircularOrbit<L, E> temp = tempC1;
      tempC1 = tempC2;
      tempC2 = temp;
      reverse = true;
    }
    c1 = tempC1;
    c2 = tempC2;
    objectNumber = new int[c1.getTracks().size()];
    Map<Track, List<E>> content1 = c1.getTrackContent();
    Map<Track, List<E>> content2 = c2.getTrackContent();
    TreeSet<Track> tracks1 = c1.getTracks();
    TreeSet<Track> tracks2 = c2.getTracks();
    int t1Len = tracks1.size();
    int t2Len = tracks2.size();
    for (int i = 0; i < t2Len; i++) {
      Track track1 = tracks1.pollFirst();
      Track track2 = tracks2.pollFirst();
      List<E> objects1 = content1.get(track1);
      List<E> objects2 = content2.get(track2);
      objectNumber[i] = objects1.size() - objects2.size();
      List<E> tempList = new ArrayList<>();
      for (E object : objects1) {
        if (!objects2.contains(object)) {
          tempList.add(object);
        }
      }
      tempFirstUnique.add(tempList);
      tempList.clear();
      for (E object : objects2) {
        if (!objects1.contains(object)) {
          tempList.add(object);
        }
      }
      tempSecondUnique.add(tempList);
    }
    for (int i = t2Len; i < t1Len; i++) {
      Track track1 = tracks1.pollFirst();
      List<E> objects1 = content1.get(track1);
      objectNumber[i] = objects1.size();
      tempFirstUnique.add(objects1);
    }
    if (reverse) {
      for (int i = 0; i < t1Len; i++) {
        objectNumber[i] = -objectNumber[i];
      }
      firstUnique = tempSecondUnique;
      secondUnique = tempFirstUnique;
    } else {
      firstUnique = tempFirstUnique;
      secondUnique = tempSecondUnique;
    }
  }

  public int getTrackNumber() {
    return trackNumber;
  }


  public int[] getObjectNumber() {
    return objectNumber.clone();
  }


}
