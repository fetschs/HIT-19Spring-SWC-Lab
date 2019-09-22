package circularorbit;

import centralpoint.CentralPoint;
import circularorbit.iostrategy.IoStrategy;
import circularorbit.trackgameother.TrackGameIterator;
import circularorbit.trackgameother.TrackGameStrategy;
import exception.GrammarException;
import exception.SameLabelException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import physicalobject.Athlete;
import physicalobject.AthleteFactory;
import physicalobject.PhysicalObjectFactory;
import relation.Relation;
import track.Track;

@ToString
@Getter
public class TrackGame extends CircularOrbit<Athlete, Athlete> {

  public static final String positivePatternString = "[1-9]\\d*";
  public static final Pattern positivePattern = Pattern.compile(positivePatternString);
  public static final String gamePatternString =
      "Game\\s*::=\\s*([-]?\\d+(?:\\.\\d+)?)";
  public static final String numbOfTracksPatternString =
      "NumOfTracks\\s*::=\\s*([-]?\\d+(?:\\.\\d+)?)";
  public static final String athletePatternString
      = "Athlete\\s*::=\\s*<([a-zA-Z]+),([-]?\\d+(?:\\.\\d+)?)"
      + ",([a-zA-Z]+),([-]?\\d+(?:\\.\\d+)?),([-]?\\d+(?:\\.\\d+)?)>";
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
  // the name of game will be 100/200/400
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
  private TrackGame(TreeSet<Track> tracks, List<Athlete> objects,
      Map<Track, List<Athlete>> trackContent,
      CentralPoint<Athlete> centralPoint,
      List<Relation<Athlete, Athlete>> c2tRelations,
      Map<Athlete, List<Relation<Athlete, Athlete>>> t2tRelations, String game,
      List<CircularOrbit<Athlete, Athlete>> groups) {
    super(tracks, objects, trackContent, centralPoint, c2tRelations, t2tRelations);
    this.game = game;
    this.groups = groups;
    checkRep(this);
  }

  /**
   * check if the trackGame's RI Ok.
   *
   * @param trackGame the trackGame will be check
   */
  public static void checkRep(TrackGame trackGame) {
    assert trackGame.getGame().equals("100") || trackGame.getGame().equals("200") || trackGame
        .getGame().equals("400") || trackGame.getGame().equals("SubTrackGame");
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
   * create an Instance by client.
   *
   * @param filename the file to read.
   * @param strategy the strategy the client choose.
   */
  public static TrackGame creatInstanceFromClient(String filename, TrackGameStrategy strategy,
      IoStrategy ioStrategy)
      throws GrammarException, SameLabelException {
    String tempGame = "";
    TreeSet<Track> tracks = new TreeSet<>();
    List<Athlete> objects = new ArrayList<>();
    Set<String> labels = new HashSet<>();
    String line;
    while ((line = ioStrategy.nextLine()) != null) {
      if (line.isEmpty()) {
        continue;
      }
      Matcher matcher = gamePattern.matcher(line);
      if (matcher.find()) {
        if (!tempGame.isEmpty()) {
          throw new SameLabelException();
        }
        tempGame = matcher.group(1);
        if ((!tempGame.equals("100")) && (!tempGame.equals("200")) && (!tempGame.equals("400"))) {
          throw new GrammarException("the kind of TrackGame is invalid");
        }
        continue;
      }
      matcher = numOfTracksPattern.matcher(line);
      if (matcher.find()) {
        if (!positivePattern.matcher(matcher.group(1)).matches()) {
          throw new GrammarException("the number of tracks is invalid");
        }
        if (!tracks.isEmpty()) {
          throw new SameLabelException();
        }
        int number = Integer.parseInt(matcher.group(1));
        for (int i = 0; i < number; i++) {
          tracks.add(new Track(BigDecimal.valueOf(i)));
        }
        continue;
      }
      matcher = athletePattern.matcher(line);
      if (matcher.find()) {
        if (!TrackGame.positivePattern.matcher(matcher.group(2)).matches()
            && !matcher.group(2).equals("0")) {
          throw new GrammarException("the number of athlete is invalid");
        }
        Pattern pattern = Pattern.compile("[A-Z]{3}");
        if (!pattern.matcher(matcher.group(3)).matches()) {
          throw new GrammarException("the nation of athlete is invalid");
        }
        PhysicalObjectFactory factory = new AthleteFactory();
        Athlete athlete = (Athlete) factory.createPhysicalObject(line);
        objects.add(athlete);
        if (labels.contains(athlete.getName())) {
          throw new SameLabelException();
        }
        labels.add(athlete.getName());
        continue;
      }
      throw new GrammarException("line can't match");
    }
    ioStrategy.closeIo();
    List<CircularOrbit<Athlete, Athlete>> groups = strategy.arrange(objects, tracks);
    CentralPoint<Athlete> centralPoint = new CentralPoint<>(new ArrayList<>());
    checkRep(TrackGame.builder().centralPoint(centralPoint).groups(groups).objects(objects)
        .tracks(tracks).game(tempGame).t2tRelations(new HashMap<>())
        .c2tRelations(new ArrayList<>()).trackContent(new HashMap<>())
        .build());
    return TrackGame.builder().centralPoint(centralPoint).groups(groups).objects(objects)
        .tracks(tracks).game(tempGame).t2tRelations(new HashMap<>())
        .c2tRelations(new ArrayList<>()).trackContent(new HashMap<>())
        .build();
  }

  @Override
  public void outputInstanceInFile(String filename, IoStrategy ioStrategy) throws IOException {
    List<String> writeContent = new ArrayList<>();
    StringBuffer tempLine = new StringBuffer("Game ::= ");
    tempLine.append(this.getGame());
    writeContent.add(tempLine.toString());
    tempLine = new StringBuffer("NumOfTracks ::= ");
    tempLine.append(this.getTracks().size());
    writeContent.add(tempLine.toString());
    for (Athlete athlete : this.getObjects()) {
      tempLine = new StringBuffer("Athlete ::= <");
      tempLine.append(athlete.getName()).append(',').append(athlete.getAge()).append(',')
          .append(athlete.getNationality()).append(',').append(athlete.getNumber()).append(',')
          .append(athlete.getBestScore()).append(',').append('>');
      writeContent.add(tempLine.toString());
    }
    try {
      ioStrategy.writes(filename, writeContent);
    } catch (IOException e) {
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public CircularOrbitIterator<Athlete> iterator() {
    return new TrackGameIterator(this);
  }
}
