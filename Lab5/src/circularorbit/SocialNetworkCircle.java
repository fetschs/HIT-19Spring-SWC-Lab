package circularorbit;

import centralpoint.CentralPoint;
import circularorbit.iostrategy.IoStrategy;
import circularorbit.socialnetworkother.SocialNetworkIterator;
import exception.DependencyException;
import exception.GrammarException;
import exception.SameLabelException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import physicalobject.CentralUser;
import physicalobject.CentralUserFactory;
import physicalobject.Friend;
import physicalobject.FriendFactory;
import physicalobject.PhysicalObjectFactory;
import relation.Relation;
import track.Track;

@ToString
@Getter
public class SocialNetworkCircle extends CircularOrbit<CentralUser, Friend> {

  public static final String centralUserPatternString =
      "CentralUser\\s*::=\\s*<\\s*([a-zA-Z0-9]+),\\s*([-]?\\d+(?:\\.\\d+)?),\\s*([a-zA-Z0-9]+)>";
  public static final String friendPatternString =
      "Friend\\s*::=\\s*<\\s*([a-zA-Z0-9]+),\\s*([-]?\\d+(?:\\.\\d+)?),\\s*([a-zA-Z0-9]+)>";
  public static final String socialTiePatternString =
      "SocialTie\\s*::=\\s*<\\s*([a-zA-Z0-9]+),\\s*([a-zA-Z0-9]+),\\s*([-]?\\d+(?:\\.\\d+)?)>";
  public static final Pattern centralUserPattern = Pattern.compile(centralUserPatternString);
  public static final Pattern friendPattern = Pattern.compile(friendPatternString);
  public static final Pattern socialTiePattern = Pattern.compile(socialTiePatternString);

  //RI: friend in track(radius is n) should be logic distance from central to it is n.
  //all the friend can access central
  @Builder(toBuilder = true)
  public SocialNetworkCircle(TreeSet<Track> tracks,
      List<Friend> objects,
      Map<Track, List<Friend>> trackContent,
      CentralPoint<CentralUser> centralPoint,
      List<Relation<CentralUser, Friend>> c2tRelations,
      Map<Friend, List<Relation<Friend, Friend>>> t2tRelations) {
    super(tracks, objects, trackContent, centralPoint, c2tRelations, t2tRelations);
    checkRep(this);
  }


  private static Map<Friend, BigDecimal> getDistance(List<Friend> objects,
      List<Relation<CentralUser, Friend>> c2tRelations,
      Map<Friend, List<Relation<Friend, Friend>>> t2tRelations) {

    Map<Friend, BigDecimal> distance = new HashMap<>();
    for (Friend friend : objects) {
      distance.put(friend, BigDecimal.valueOf(Integer.MAX_VALUE));
    }
    Queue<Friend> queue = new ConcurrentLinkedQueue<>();
    Set<Friend> inqueue = new HashSet<>();
    for (Relation<CentralUser, Friend> relation : c2tRelations) {
      distance.put(relation.getTo(), relation.getLength());
      queue.add(relation.getTo());
      inqueue.add(relation.getTo());
    }
    while (!queue.isEmpty()) {
      Friend now = queue.poll();
      for (Relation<Friend, Friend> relation : t2tRelations.getOrDefault(now, new ArrayList<>())) {
        if (distance.get(relation.getTo())
            .compareTo(distance.get(relation.getFrom()).add(relation.getLength())) > 0) {
          distance
              .put(relation.getTo(), distance.get(relation.getFrom()).add(relation.getLength()));
          if (!inqueue.contains(relation.getTo())) {
            queue.add(relation.getTo());
            inqueue.add(relation.getTo());
          }
        }
      }
      inqueue.remove(now);
    }
    return distance;
  }

  /**
   * create Instance from client.
   *
   * @param fileName the filename.
   */
  public static SocialNetworkCircle createInstanceFromClient(String fileName, IoStrategy ioStrategy)
      throws SameLabelException, DependencyException,
      GrammarException {
    TreeSet<Track> tracks = new TreeSet<>();
    List<Friend> objects = new ArrayList<>();
    Set<Relation<CentralUser, Friend>> c2tRelations = new HashSet<>();
    Map<Friend, List<Relation<Friend, Friend>>> t2tRelations = new HashMap<>();
    Map<Track, List<Friend>> trackCt = new HashMap<>();
    CentralPoint<CentralUser> centralPoint = new CentralPoint<>(new ArrayList<>());
    Map<String, Friend> friendMap = new HashMap<>();
    Map<String, CentralUser> centralUserMap = new HashMap<>();
    List<String> froms = new ArrayList<>();
    List<String> tos = new ArrayList<>();
    List<BigDecimal> lens = new ArrayList<>();
    Set<String> labels = new HashSet<>();
    String centralName = "";
    PhysicalObjectFactory centralUserFactory = new CentralUserFactory();
    PhysicalObjectFactory friendFactory = new FriendFactory();
    String line = "";
    while ((line = ioStrategy.nextLine()) != null) {
      if (line.isEmpty()) {
        continue;
      }
      Matcher matcher = centralUserPattern.matcher(line);
      if (matcher.find()) {
        if (!centralName.isEmpty()) {
          throw new SameLabelException();
        }
        centralName = matcher.group(1);
        labels.add(centralName);
        if (!TrackGame.positivePattern.matcher(matcher.group(2)).matches()) {
          throw new GrammarException("the age of centralUser is invalid");
        }
        if (!matcher.group(3).equals("M") && !matcher.group(3).equals("F")) {
          throw new GrammarException("the sex of centralUser is invalid");
        }
        centralUserMap
            .put(centralName, (CentralUser) centralUserFactory.createPhysicalObject(line));
        centralPoint.addCentralObject((CentralUser) centralUserFactory.createPhysicalObject(line));
        continue;
      }
      matcher = friendPattern.matcher(line);
      if (matcher.find()) {
        if (labels.contains(matcher.group(1))) {
          throw new SameLabelException();
        }
        if (!TrackGame.positivePattern.matcher(matcher.group(2)).matches()) {
          throw new GrammarException("the age of friend is invalid");
        }
        if (!matcher.group(3).equals("M") && !matcher.group(3).equals("F")) {
          throw new GrammarException("the sex of friend is invalid");
        }
        labels.add(matcher.group(1));
        friendMap.put(matcher.group(1), (Friend) friendFactory.createPhysicalObject(line));
        objects.add((Friend) friendFactory.createPhysicalObject(line));
        continue;
      }
      matcher = socialTiePattern.matcher(line);
      if (matcher.find()) {
        froms.add(matcher.group(1));
        tos.add(matcher.group(2));
        BigDecimal len = BigDecimal.valueOf(Double.parseDouble(matcher.group(3)));
        if (len.compareTo(BigDecimal.ONE) > 0 || len.compareTo(BigDecimal.ZERO) <= 0) {
          throw new GrammarException("the number of intimacy is invalid");
        }
        lens.add(BigDecimal.valueOf(Double.parseDouble(matcher.group(3))));
        continue;
      }
      throw new GrammarException("line can't match");
    }
    ioStrategy.closeIo();
    for (Friend friend : objects) {
      t2tRelations.put(friend, new ArrayList<>());
    }
    for (int i = 0; i < froms.size(); i++) {
      if ((!labels.contains(froms.get(i))) || (!labels.contains(tos.get(i)))) {
        throw new DependencyException();
      }
      if (froms.get(i).equals(tos.get(i))) {
        throw new DependencyException();
      }
      if (froms.get(i).equals(centralName)) {
        Relation<CentralUser, Friend> relation = (new Relation<>(lens.get(i),
            centralUserMap.get(froms.get(i)),
            friendMap.get(tos.get(i))));
        if (!c2tRelations.contains(relation)) {
          c2tRelations.add(relation);
        } else {
          throw new DependencyException();
        }
        continue;
      }
      Friend from = friendMap.get(froms.get(i));
      Friend to = friendMap.get(tos.get(i));
      Relation<Friend, Friend> relation1 = new Relation<>(lens.get(i), from, to);
      Relation<Friend, Friend> relation2 = new Relation<>(lens.get(i), to, from);
      List<Relation<Friend, Friend>> relations = t2tRelations.getOrDefault(from, new ArrayList<>());
      Set<Relation<Friend, Friend>> checkRelations = new HashSet<>(relations);
      if (checkRelations.contains(relation1) || checkRelations.contains(relation2)) {
        throw new DependencyException();
      }
      relations.add(relation1);
      t2tRelations.put(from, relations);
      relations = t2tRelations.getOrDefault(to, new ArrayList<>());
      relations.add(relation2);
      t2tRelations.put(to, relations);
    }
    Map<Friend, BigDecimal> distance = getDistance(objects, new ArrayList<>(c2tRelations),
        t2tRelations);
    for (Friend friend : objects) {
      Track track = new Track(distance.get(friend));
      tracks.add(track);
    }
    for (Track track : tracks) {
      trackCt.put(track, new ArrayList<>());
    }
    for (Friend friend : objects) {
      Track track = new Track(distance.get(friend));
      List<Friend> list = trackCt.get(track);
      list.add(friend);
      trackCt.put(track, list);
    }
    checkRep(SocialNetworkCircle.builder().c2tRelations(new ArrayList<>(c2tRelations))
        .t2tRelations(t2tRelations)
        .objects(objects).tracks(tracks).trackContent(trackCt).centralPoint(centralPoint).build());
    return SocialNetworkCircle.builder().c2tRelations(new ArrayList<>(c2tRelations))
        .t2tRelations(t2tRelations)
        .objects(objects).tracks(tracks).trackContent(trackCt).centralPoint(centralPoint).build();
  }

  /**
   * check the rep for orbit.
   *
   * @param sc the orbit will check.
   */
  public static void checkRep(SocialNetworkCircle sc) {
    Map<Friend, BigDecimal> distance = getDistance(sc.getObjects(), sc.getC2tRelations(),
        sc.getT2tRelations());
    for (Track track : sc.getTracks()) {
      Map<Track, List<Friend>> trackContents = sc.getTrackContent();
      for (Friend friend : trackContents.get(track)) {
        assert distance.get(friend).compareTo(track.getRadius()) == 0;
      }
    }
  }

  @Override
  public CircularOrbitIterator<Friend> iterator() {
    return new SocialNetworkIterator(this);
  }

  /**
   * cal the spread friend by friend in first track.
   *
   * @param tempFriend the name of the friend.
   * @return -1 if invalid or as above.
   */
  public int calSpreadInFirstTrack(String tempFriend) {
    for (Relation<CentralUser, Friend> relation : this.getC2tRelations()) {
      if (relation.getTo().getName().equals(tempFriend)) {
        return this.getT2tRelations().get(relation.getTo()).size();
      }
    }
    return -1;
  }

  @Override
  public void outputInstanceInFile(String filename, IoStrategy ioStrategy) throws IOException {
    List<String> writeContent = new ArrayList<>();
    StringBuffer tempLine = new StringBuffer("CentralUser ::= <");
    CentralUser centralUser = this.getCentralPoint().getCentralObjects().get(0);
    tempLine.append(centralUser.getName()).append(',').append(centralUser.getAge()).append(',')
        .append(centralUser.getSex()).append(">");
    writeContent.add(tempLine.toString());
    for (Friend friend : this.getObjects()) {
      tempLine = new StringBuffer("Friend ::= <");
      tempLine.append(friend.getName()).append(',').append(friend.getAge()).append(',')
          .append(friend.getSex()).append(">");
      writeContent.add(tempLine.toString());
    }
    for (Relation relation : this.getC2tRelations()) {
      tempLine = new StringBuffer("SocialTie :: = <");
      tempLine.append(relation.getFrom()).append(',').append(relation.getTo()).append(',')
          .append(relation.getLength()).append('>');
      writeContent.add(tempLine.toString());
    }
    for (List<Relation<Friend, Friend>> relations : this.getT2tRelations().values()) {
      for (Relation relation : relations) {
        tempLine = new StringBuffer("SocialTie :: = <");
        tempLine.append(relation.getFrom()).append(',').append(relation.getTo()).append(',')
            .append(relation.getLength()).append('>');
        writeContent.add(tempLine.toString());
      }
    }
    try {
      ioStrategy.writes(filename, writeContent);
    } catch (IOException e) {
      e.printStackTrace();
      throw e;
    }
  }
}
