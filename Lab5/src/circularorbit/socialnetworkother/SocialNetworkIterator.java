package circularorbit.socialnetworkother;

import circularorbit.CircularOrbitIterator;
import circularorbit.SocialNetworkCircle;
import java.util.HashMap;
import java.util.Map;
import physicalobject.Friend;
import track.Track;

public class SocialNetworkIterator implements CircularOrbitIterator<Friend> {

  private final Map<Integer, Friend> rank;
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
   * @param socialNetworkCircle the orbit will use iterator.
   */
  public SocialNetworkIterator(SocialNetworkCircle socialNetworkCircle) {
    this.rank = new HashMap<>();
    this.index = 0;
    int tempNumber = 0;
    for (Track track : socialNetworkCircle.getTracks()) {
      for (Friend friend : socialNetworkCircle.getTrackContent().get(track)) {
        rank.put(++tempNumber, friend);
      }
    }
    rankNumber = tempNumber;
  }

  @Override
  public boolean hasNext() {
    return index <= rankNumber;
  }

  @Override
  public Friend next() {
    return rank.get(++index);
  }
}
