package circularOrbit;

import centralPoint.CentralPoint;
import circularOrbit.SocialNetworkOther.SocialNetworkIterator;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import physicalObject.CentralUser;
import physicalObject.CentralUserFactory;
import physicalObject.Friend;
import physicalObject.FriendFactory;
import physicalObject.PhysicalObjectFactory;
import relation.Relation;
import track.Track;

@ToString
@Getter
public class SocialNetworkCircle extends CircularOrbit<CentralUser, Friend> {

  public static final String centralUserPatternString =
      "CentralUser\\s*::=\\s*<\\s*([a-zA-Z0-9]+),\\s*([\\d]+),\\s*([MF])>";
  public static final String friendPatternString =
      "Friend\\s*::=\\s*<\\s*([a-zA-Z0-9]+),\\s*([\\d]+),\\s*([MF])>";
  public static final String socialTiePatternString =
      "SocialTie\\s*::=\\s*<\\s*([a-zA-Z0-9]+),\\s*([a-zA-Z0-9]+),\\s*([\\d]\\.[\\d]{1,3})>";
  public static final Pattern centralUserPattern = Pattern.compile(centralUserPatternString);
  public static final Pattern friendPattern = Pattern.compile(friendPatternString);
  public static final Pattern socialTiePattern = Pattern.compile(socialTiePatternString);

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

  //RI: friend in track(radius is n) should be logic distance from central to it is n.
  //all the friend can access central
  private static Map<Friend, BigDecimal> getDistance(List<Friend> objects,
      List<Relation<CentralUser, Friend>> c2tRelations,
      Map<Friend, List<Relation<Friend, Friend>>> t2tRelations) {
    Map<Friend, BigDecimal> distance = new HashMap<>();
    for (Friend friend : objects) {
      distance.put(friend, BigDecimal.valueOf(Integer.MAX_VALUE));
    }
    Queue<Friend> queue = new ConcurrentLinkedQueue<>();
    for (Relation<CentralUser, Friend> relation : c2tRelations) {
      distance.put(relation.getTo(), BigDecimal.ONE);
      queue.add(relation.getTo());
    }
    Map<Friend, List<Relation<Friend, Friend>>> content = t2tRelations;
    while (!queue.isEmpty()) {
      Friend now = queue.poll();
      for (Relation<Friend, Friend> relation : content.get(now)) {
        if (distance.get(relation.getTo())
            .compareTo(distance.get(relation.getFrom()).add(BigDecimal.ONE)) > 0) {
          distance.put(relation.getTo(), distance.get(relation.getFrom()).add(BigDecimal.ONE));
          if (!queue.contains(relation.getTo())) {
            queue.add(relation.getTo());
          }
        }
      }
    }
    return distance;
  }

  /**
   * create Instance from client.
   *
   * @param fileName the filename.
   */
  public static SocialNetworkCircle createInstanceFromClient(String fileName) {
    TreeSet<Track> tracks = new TreeSet<>();
    List<Friend> objects = new ArrayList<>();
    List<Relation<CentralUser, Friend>> c2tRelations = new ArrayList<>();
    Map<Friend, List<Relation<Friend, Friend>>> t2tRelations = new HashMap<>();
    Map<Track, List<Friend>> trackCt = new HashMap<>();
    CentralPoint<CentralUser> centralPoint = new CentralPoint<>(new ArrayList<>());
    Map<String, Friend> friendMap = new HashMap<>();
    Map<String, CentralUser> centralUserMap = new HashMap<>();
    List<String> froms = new ArrayList<>();
    List<String> tos = new ArrayList<>();
    List<BigDecimal> lens = new ArrayList<>();
    String centralName = "";
    File file = new File(fileName);
    Scanner sc = new Scanner(System.in, "utf-8");
    try {
      sc = new Scanner(file, "utf-8");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    PhysicalObjectFactory centralUserFactory = new CentralUserFactory();
    PhysicalObjectFactory friendFactory = new FriendFactory();
    while (sc.hasNextLine()) {
      String line = sc.nextLine();
      Matcher matcher = centralUserPattern.matcher(line);
      if (matcher.find()) {
        centralName = matcher.group(1);
        centralUserMap
            .put(centralName, (CentralUser) centralUserFactory.createPhysicalObject(line));
        centralPoint.addCentralObject((CentralUser) centralUserFactory.createPhysicalObject(line));
      }
      matcher = friendPattern.matcher(line);
      if (matcher.find()) {
        friendMap.put(matcher.group(1), (Friend) friendFactory.createPhysicalObject(line));
        objects.add((Friend) friendFactory.createPhysicalObject(line));
      }
      matcher = socialTiePattern.matcher(line);
      if (matcher.find()) {
        froms.add(matcher.group(1));
        tos.add(matcher.group(2));
        lens.add(BigDecimal.valueOf(Double.parseDouble(matcher.group(3))));
      }
    }
    sc.close();
    for (Friend friend : objects) {
      t2tRelations.put(friend, new ArrayList<>());
    }
    for (int i = 0; i < froms.size(); i++) {
      if (froms.get(i).equals(centralName)) {
        c2tRelations.add(new Relation<>(lens.get(i), centralUserMap.get(froms.get(i)),
            friendMap.get(tos.get(i))));
        continue;
      }
      Friend from = friendMap.get(froms.get(i));
      Friend to = friendMap.get(tos.get(i));
      Relation<Friend, Friend> relation1 = new Relation<>(lens.get(i), from, to);
      Relation<Friend, Friend> relation2 = new Relation<>(lens.get(i), to, from);
      List<Relation<Friend, Friend>> relations = t2tRelations.get(from);
      relations.add(relation1);
      t2tRelations.put(from, relations);
      relations = t2tRelations.get(to);
      relations.add(relation2);
      t2tRelations.put(to, relations);
    }
    Map<Friend, BigDecimal> distance = getDistance(objects, c2tRelations, t2tRelations);
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
    return SocialNetworkCircle.builder().c2tRelations(c2tRelations).t2tRelations(t2tRelations)
        .objects(objects
        ).tracks(tracks).trackContent(trackCt).centralPoint(centralPoint).build();
  }

  public void checkRep(SocialNetworkCircle sc) {
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
  public CircularOrbitIterator iterator() {
    return new SocialNetworkIterator(this);
  }

  /**
   * cal the spread friend by friend in first track
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
}

