package circularOrbit.TrackGameOther;

import centralPoint.CentralPoint;
import circularOrbit.CircularOrbit;
import circularOrbit.TrackGame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import physicalObject.Athlete;
import track.Track;

public class BestStrategy implements TrackGameStrategy {

  @Override
  public List<CircularOrbit<Athlete, Athlete>> arrange(List<Athlete> athletes,
      TreeSet<Track> tracks) {
    List<Athlete> newAthletes = new ArrayList<>(athletes);
    newAthletes.sort(new Comparator<Athlete>() {
      public int compare(Athlete a1, Athlete a2) {
        return a1.getBestScore().compareTo(a2.getBestScore());
      }
    });
    List<CircularOrbit<Athlete, Athlete>> groups = new ArrayList<>();
    Map<Track, List<Athlete>> content = new HashMap<>();
    int n = athletes.size() / tracks.size();
    if (athletes.size() % tracks.size() != 0) {
      n = n + 1;
    }
    for (int i = 0; i < n; i++) {
      List<Athlete> tempAthletes = new ArrayList<>();
      for (Track track : tracks) {
        if (newAthletes.isEmpty()) {
          break;
        }
        Athlete tempAthlete = newAthletes.get(newAthletes.size() - 1);
        tempAthletes.add(tempAthlete);
        content.put(track, Collections.singletonList(tempAthlete));
        newAthletes.remove(tempAthlete);
      }
      CentralPoint<Athlete> empty = new CentralPoint<>(new ArrayList<>());
      groups.add(TrackGame.<Athlete, Athlete>builder().objects(tempAthletes).centralPoint(empty)
          .tracks(tracks)
          .trackContent(content).build());
    }
    return groups;
  }
}
