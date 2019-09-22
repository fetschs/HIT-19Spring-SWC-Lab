package circularorbit.atomstructureother;

import circularorbit.AtomStructure;
import circularorbit.CircularOrbitIterator;
import java.util.HashMap;
import java.util.Map;
import physicalobject.Electron;
import track.Track;

public class AtomStructureIterator implements CircularOrbitIterator<Electron> {

  private final Map<Integer, Electron> rank;
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
   * @param atomStructure the orbit will use iterator.
   */
  public AtomStructureIterator(AtomStructure atomStructure) {
    this.rank = new HashMap<>();
    this.index = 0;
    int tempNumber = 0;
    for (Track track : atomStructure.getTracks()) {
      for (Electron electron : atomStructure.getTrackContent().get(track)) {
        rank.put(++tempNumber, electron);
      }
    }
    rankNumber = tempNumber;
  }

  @Override
  public boolean hasNext() {
    return index <= rankNumber;
  }

  @Override
  public Electron next() {
    return rank.get(++index);
  }

}
