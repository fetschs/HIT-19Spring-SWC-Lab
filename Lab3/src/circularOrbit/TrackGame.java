package circularOrbit;

import centralPoint.CentralPoint;
import circularOrbit.TrackGameOther.TrackGameIterator;
import circularOrbit.TrackGameOther.TrackGameStrategy;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import physicalObject.Athlete;
import physicalObject.AthleteFactory;
import physicalObject.PhysicalObjectFactory;
import relation.Relation;
import track.Track;

@ToString
@Getter
public class TrackGame extends CircularOrbit<Athlete, Athlete> {

  public static final String gamePatternString =
      "Game\\s*::=\\s*(100|200|400)";
  public static final String numbOfTracksPatternString =
      "NumOfTracks\\s*::=\\s*(4|5|6|7|8|9|10)";
  public static final String athletePatternString
      = "Athlete\\s*::=\\s*<([a-zA-Z]+),(\\d+),([A-Z]{3}),(\\d+),(\\d{1,2}\\.\\d{2})>";
  public static final Pattern gamePattern = Pattern.compile(gamePatternString);
  public static final Pattern numOfTracksPattern = Pattern.compile(numbOfTracksPatternString);
  public static final Pattern athletePattern = Pattern.compile(athletePatternString);

  //some Patterns for parser
  private final String game;
  private final List<CircularOrbit<Athlete, Athlete>> groups;
  // Abstraction function:
  // AF(game) = the kind of this game.
  // AF(groups) = each group will be a circularOrbit;
  // Representation invariant:
  // the name of game will be
  // no object in the central
  // 4-10 tracks, and one athlete will be most in one track.
  // 0 - n-1 group, has the same number of people,(or 0-n)
  // Safety from rep exposure:
  // all fields are private
  // defensive copy

  /**
   * All construction method.
   *
   * @param tracks super.tracks
   * @param objects super.objects.
   * @param trackContent super.trackContent
   * @param centralPoint super.centralPoint
   * @param c2tRelations super.c2tRelations
   * @param t2tRelations super.t2tRelations
   * @param game this.game
   * @param groups this.groups
   */
  @Builder(toBuilder = true)
  public TrackGame(TreeSet<Track> tracks, List<Athlete> objects,
      Map<Track, List<Athlete>> trackContent,
      CentralPoint<Athlete> centralPoint,
      List<Relation<Athlete, Athlete>> c2tRelations,
      Map<Athlete, List<Relation<Athlete, Athlete>>> t2tRelations, String game,
      List<CircularOrbit<Athlete, Athlete>> groups) {
    super(tracks, objects, trackContent, centralPoint, c2tRelations, t2tRelations);
    this.game = game;
    this.groups = groups;
  }

  /**
   * check if the trackGame's RI Ok.
   *
   * @param trackGame the trackGame will be check
   */
  public static void checkRep(TrackGame trackGame) {
    assert trackGame.getCentralPoint().getCentralObjects().size() == 0;
    assert trackGame.getTracks().size() >= 4 && trackGame.getTracks().size() <= 10;
    for (CircularOrbit<Athlete, Athlete> c : trackGame.getGroups()) {
      assert c.getCentralPoint().getCentralObjects().size() == 0;
      Map<Track, List<Athlete>> content = c.getTrackContent();
      for (List<Athlete> as : content.values()) {
        assert as.size() <= 1;
      }
      if (trackGame.getGroups().indexOf(c) != trackGame.getGroups().size() - 1) {
        assert
            c.getObjects().size() == trackGame.getTracks().size();
      } else {
        assert c.getObjects().size() <= trackGame.getTracks().size();
      }
    }
  }

  /**
   * creant an Instance by client.
   *
   * @param filename the file to read.
   * @param strategy the strategy the client choose.
   */
  public static TrackGame creatInstanceFromClient(String filename, TrackGameStrategy strategy) {
    String tempGame = "";
    TreeSet<Track> tracks = new TreeSet<>();
    List<Athlete> objects = new ArrayList<>();
    File file = new File(filename);
    Scanner sc = new Scanner(System.in, "utf-8");
    try {
      sc = new Scanner(file, "utf-8");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    while (sc.hasNext()) {
      String line = sc.nextLine();
      Matcher matcher = gamePattern.matcher(line);
      if (matcher.find()) {
        tempGame = matcher.group(1);
        continue;
      }
      matcher = numOfTracksPattern.matcher(line);
      if (matcher.find()) {
        int number = Integer.parseInt(matcher.group(1));
        for (int i = 0; i < number; i++) {
          tracks.add(new Track(BigDecimal.valueOf(i)));
        }
        continue;
      }
      matcher = athletePattern.matcher(line);
      if (matcher.find()) {
        PhysicalObjectFactory factory = new AthleteFactory();
        Athlete athlete = (Athlete) factory.createPhysicalObject(line);
        objects.add(athlete);
      }
    }
    sc.close();
    List<CircularOrbit<Athlete, Athlete>> groups = strategy.arrange(objects, tracks);
    CentralPoint<Athlete> centralPoint = new CentralPoint<>(new ArrayList<>());
    checkRep(TrackGame.builder().centralPoint(centralPoint).groups(groups).objects(objects)
        .tracks(tracks).game(tempGame)
        .build());//track game ri is not the same as his father TODO
    return TrackGame.builder().centralPoint(centralPoint).groups(groups).objects(objects)
        .tracks(tracks).game(tempGame).t2tRelations(new HashMap<>())
        .c2tRelations(new ArrayList<>()).trackContent(new HashMap<>())
        .build();
  }

  @Override
  public CircularOrbitIterator iterator() {
    return new TrackGameIterator(this);
  }
}
