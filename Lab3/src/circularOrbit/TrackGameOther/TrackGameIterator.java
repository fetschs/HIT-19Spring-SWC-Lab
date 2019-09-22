package circularOrbit.TrackGameOther;

import circularOrbit.CircularOrbit;
import circularOrbit.CircularOrbitIterator;
import circularOrbit.TrackGame;
import java.util.HashMap;
import java.util.Map;
import physicalObject.Athlete;
import track.Track;

public class TrackGameIterator implements CircularOrbitIterator<Athlete> {

  private final Map<Integer, Athlete> rank;
  private final int rankNumber;
  private int index;
  // Abstraction function:
  // AF(rank) = each athlete's rank by order.
  // AF(rankNumber) = the max rank.
  // AF(index) = the current index
  // Representation invariant:
  // no
  // Safety from rep exposure:
  // all fields are private
  // no return

  /**
   * construction method.
   *
   * @param temp this trackGame.
   */
  public TrackGameIterator(TrackGame temp) {
    this.rank = new HashMap<>();
    this.index = 0;
    int tempNumber = 0;
    for (CircularOrbit<Athlete, Athlete> c : temp.getGroups()) {
      for (Track track : c.getTracks()) {
        for (Athlete athlete : c.getTrackContent().get(track)) {
          rank.put(++tempNumber, athlete);
        }
      }
    }
    rankNumber = tempNumber;
  }

  @Override
  public boolean hasNext() {
    return index <= rankNumber;
  }


  @Override
  public Athlete next() {
    return rank.get(++this.index);
  }
}
