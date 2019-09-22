package circularorbit.trackgameother;

import centralpoint.CentralPoint;
import circularorbit.CircularOrbit;
import circularorbit.TrackGame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import physicalobject.Athlete;
import track.Track;

public class BestStrategy implements TrackGameStrategy {

  @Override
  public List<CircularOrbit<Athlete, Athlete>> arrange(List<Athlete> athletes,
      TreeSet<Track> tracks) {
    List<Athlete> newAthletes = new ArrayList<>(athletes);
    newAthletes.sort(Comparator.comparing(Athlete::getBestScore).reversed());
    List<CircularOrbit<Athlete, Athlete>> groups = new ArrayList<>();
    int n = athletes.size() / tracks.size();
    if (athletes.size() % tracks.size() != 0) {
      n = n + 1;
    }

    for (int i = 0; i < n; i++) {
      List<Athlete> tempAthletes = new ArrayList<>();
      Map<Track, List<Athlete>> content = new HashMap<>();
      for (Track track : tracks) {
        content.put(track, new ArrayList<>());
      }
      for (Track track : tracks) {
        if (newAthletes.isEmpty()) {
          break;
        }
        Athlete tempAthlete = newAthletes.get(newAthletes.size() - 1);
        tempAthletes.add(tempAthlete);
        content.put(track, Collections.singletonList(tempAthlete));
        newAthletes.remove(newAthletes.size() - 1);
      }
      CentralPoint<Athlete> empty = new CentralPoint<>(new ArrayList<>());
      groups.add(TrackGame.builder().centralPoint(empty).objects(tempAthletes)
          .tracks(tracks).t2tRelations(new HashMap<>()).c2tRelations(new ArrayList<>())
          .groups(new ArrayList<>()).game("SubTrackGame").trackContent(content).build());
    }
    return groups;
  }
}
