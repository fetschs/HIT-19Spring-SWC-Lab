package circularorbit.trackgameother;

import circularorbit.CircularOrbit;
import java.util.List;
import java.util.TreeSet;
import physicalobject.Athlete;
import track.Track;

public interface TrackGameStrategy {

  /**
   * get the groups by athletes and tracks.
   *
   * @param athletes the atheletes.
   * @param tracks the tracks.
   * @return the groups.
   */
  public List<CircularOrbit<Athlete, Athlete>> arrange(List<Athlete> athletes,
      TreeSet<Track> tracks);
}
