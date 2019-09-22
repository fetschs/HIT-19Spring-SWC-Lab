package circularorbit.trackgameother;

import centralpoint.CentralPoint;
import circularorbit.CircularOrbit;
import circularorbit.TrackGame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;
import physicalobject.Athlete;
import track.Track;

public class RandomStrategy implements TrackGameStrategy {

  @Override
  public List<CircularOrbit<Athlete, Athlete>> arrange(List<Athlete> athletes,
      TreeSet<Track> tracks) {
    List<CircularOrbit<Athlete, Athlete>> groups = new ArrayList<>();
    int n = athletes.size() / tracks.size();
    if (athletes.size() % tracks.size() != 0) {
      n++;
    }
    for (int i = 0; i < n; i++) {
      Map<Track, List<Athlete>> content = new HashMap<>();
      for (Track track : tracks) {
        content.put(track, new ArrayList<>());
      }
      List<Athlete> tempAthletes = new ArrayList<>();
      Random random = new Random();
      for (Track track : tracks) {
        if (athletes.isEmpty()) {
          break;
        }
        Athlete athlete = athletes.get(random.nextInt(athletes.size()));
        athletes.remove(athlete);
        tempAthletes.add(athlete);
        content.put(track, Collections.singletonList(athlete));
      }
      CentralPoint<Athlete> empty = new CentralPoint<>(new ArrayList<>());
      groups.add(TrackGame.builder().centralPoint(empty).objects(tempAthletes)
          .tracks(tracks).t2tRelations(new HashMap<>()).c2tRelations(new ArrayList<>())
          .groups(new ArrayList<>())
          .trackContent(content).game("SubTrackGame").build());
    }
    return groups;
  }
}
