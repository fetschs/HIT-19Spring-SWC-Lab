package circularOrbit;

import centralPoint.CentralPoint;
import circularOrbit.AtomStructureOther.AtomStructureIterator;
import circularOrbit.AtomStructureOther.AtomStructureMemento;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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
import physicalObject.Electron;
import physicalObject.ElectronFactory;
import physicalObject.Nucleus;
import physicalObject.NucleusFactory;
import physicalObject.PhysicalObjectFactory;
import relation.Relation;
import track.Track;

@ToString
@Getter
public class AtomStructure extends CircularOrbit<Nucleus, Electron> {

  public static final String elementNameString = "ElementName\\s*::=\\s*([A-Z][a-z]?)";
  public static final String numbOfTracksPatternString = "NumberOfTracks\\s*::=\\s*(\\d+)";
  public static final String numbOfElectronPatternString
      = "NumberOfElectron\\s*::=\\s*((?:(?:\\d+\\/\\d+)|;)+)";
  public static final Pattern elementPattern = Pattern.compile(elementNameString);
  public static final Pattern numOfTracksPattern = Pattern.compile(numbOfTracksPatternString);
  public static final Pattern numOfElectronPattern = Pattern.compile(numbOfElectronPatternString);
  private final String elementName;

  @Builder(toBuilder = true)
  public AtomStructure(TreeSet<Track> tracks,
      List<Electron> objects,
      Map<Track, List<Electron>> trackContent,
      CentralPoint<Nucleus> centralPoint,
      List<Relation<Nucleus, Electron>> c2tRelations,
      Map<Electron, List<Relation<Electron, Electron>>> t2tRelations,
      String elementName) {
    super(tracks, objects, trackContent, centralPoint, c2tRelations, t2tRelations);
    this.elementName = elementName;
  }

  /**
   * create an AtomStructure Instance from Client.
   *
   * @param fileName the datafile's Name.
   * @return the instance
   */
  public static AtomStructure createInstanceFromClient(String fileName) {
    String tempName = "";
    File file = new File(fileName);
    PhysicalObjectFactory factory = new NucleusFactory();
    Scanner sc = new Scanner(System.in, "utf-8");
    try {
      sc = new Scanner(file, "utf-8");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    TreeSet<Track> tracks = new TreeSet<>();
    List<Electron> objects = new ArrayList<>();
    List<List<Electron>> electrons = new ArrayList<>();
    Map<Track, List<Electron>> content = new HashMap<>();
    Nucleus nucleus = (Nucleus) factory.createPhysicalObject("ElementName ::= Tw");
    while (sc.hasNext()) {
      String line = sc.nextLine();
      Matcher matcher = elementPattern.matcher(line);
      if (matcher.find()) {
        tempName = matcher.group(1);
        factory = new NucleusFactory();
        nucleus = (Nucleus) factory.createPhysicalObject(line);
      }
      matcher = numOfTracksPattern.matcher(line);
      if (matcher.find()) {
        int number = Integer.parseInt(matcher.group(1));
        for (int i = 1; i <= number; i++) {
          tracks.add(new Track(BigDecimal.valueOf(i)));
        }
      }
      matcher = numOfElectronPattern.matcher(line);
      if (matcher.find()) {
        System.out.println(matcher.group(1));
        String[] trackContents = matcher.group(1).split(";");
        for (int i = 0; i < trackContents.length; i++) {
          String[] temp = trackContents[i].split("/");
          List<Electron> tempList = new ArrayList<>();
          for (int j = 0; j < Integer.parseInt(temp[1]); j++) {
            factory = new ElectronFactory();
            tempList.add((Electron) factory.createPhysicalObject(line));
          }
          objects.addAll(tempList);
          electrons.add(tempList);
        }
      }
    }
    sc.close();
    List<Track> tempTracks = new ArrayList<>(tracks);
    for (int i = 0; i < tracks.size(); i++) {
      content.put(tempTracks.get(i), electrons.get(i));
    }
    CentralPoint<Nucleus> centralPoint = new CentralPoint<Nucleus>(
        Collections.singletonList(nucleus));
    return AtomStructure.builder().centralPoint(centralPoint).elementName(tempName).objects(objects)
        .tracks(tracks).trackContent(content).c2tRelations(new ArrayList<>())
        .t2tRelations(new HashMap<>()).build();
  }

  /**
   * make the electron from an Track to another.
   *
   * @param from from Track
   * @param to to Track
   * @param electron the electron will jump
   */
  public void jump(Track from, Track to, Electron electron) {
    assert this.getTrackContent().get(from).contains(electron);
    this.removePhysicalObjectInTrack(from, electron);
    this.addPhysicalObjectInTrack(electron, to);
  }

  /**
   * create the Memento.
   *
   * @return the Memento.
   */
  public AtomStructureMemento createMemento() {
    return new AtomStructureMemento(this.getTrackContent());
  }

  /**
   * restore the Memento.
   *
   * @param memento the Memento will restore.
   */
  public void restoreMemento(AtomStructureMemento memento) {
    this.trackContent = memento.getContents();
  }

  @Override
  public CircularOrbitIterator iterator() {
    return new AtomStructureIterator(this);
  }
}
