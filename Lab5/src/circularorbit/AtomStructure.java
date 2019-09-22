package circularorbit;

import centralpoint.CentralPoint;
import circularorbit.atomstructureother.AtomStructureIterator;
import circularorbit.atomstructureother.AtomStructureMemento;
import circularorbit.iostrategy.IoStrategy;
import exception.DependencyException;
import exception.GrammarException;
import exception.InvalidInputException;
import exception.SameLabelException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import physicalobject.Electron;
import physicalobject.ElectronFactory;
import physicalobject.Nucleus;
import physicalobject.NucleusFactory;
import physicalobject.PhysicalObjectFactory;
import relation.Relation;
import track.Track;

@ToString
@Getter
public class AtomStructure extends CircularOrbit<Nucleus, Electron> {

  public static final String elementNameString = "ElementName\\s*::=\\s*([a-zA-z][a-zA-Z]?)";
  public static final String numbOfTracksPatternString
      = "NumberOfTracks\\s*::=\\s*([-]?\\d+(?:\\.\\d+)?)";
  public static final String numbOfElectronPatternString
      = "NumberOfElectron\\s*::=\\s*((?:(?:(?:[-]?\\d+(?:\\.\\d+)?)/(?:[-]?\\d+(?:\\.\\d+)?)|;)+))";
  public static final Pattern elementPattern = Pattern.compile(elementNameString);
  public static final Pattern numOfTracksPattern = Pattern.compile(numbOfTracksPatternString);
  public static final Pattern numOfElectronPattern = Pattern.compile(numbOfElectronPatternString);

  private final String elementName;

  // Abstraction function:
  // AF(elementName) = the kind of this element.
  // Representation invariant:
  // elementName should be [A-Z][a-z]?
  // Safety from rep exposure:
  // all fields are private
  // defensive copy
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
   * check the RI of atomStructure.
   *
   * @param atomStructure the atomStructure will be check.
   */
  public static void checkRep(AtomStructure atomStructure) {
    Pattern pattern = Pattern.compile("[A-Z][a-z]?");
    assert pattern.matcher(atomStructure.getElementName()).matches();
  }

  /**
   * create an AtomStructure Instance from Client.
   *
   * @param fileName the datafile's Name.
   * @return the instance
   */
  public static AtomStructure createInstanceFromClient(String fileName, IoStrategy ioStrategy)
      throws FileNotFoundException, SameLabelException, GrammarException, DependencyException {
    String tempName = "";
    PhysicalObjectFactory factory = new NucleusFactory();
    TreeSet<Track> tracks = new TreeSet<>();
    List<Electron> objects = new ArrayList<>();
    List<List<Electron>> electrons = new ArrayList<>();
    Map<Track, List<Electron>> content = new HashMap<>();
    Nucleus nucleus = (Nucleus) factory.createPhysicalObject("ElementName ::= Tw");
    String line;
    while ((line = ioStrategy.nextLine()) != null) {
      if (line.isEmpty()) {
        continue;
      }
      Matcher matcher = elementPattern.matcher(line);
      if (matcher.find()) {
        if (!tempName.isEmpty()) {
          throw new SameLabelException();
        }
        tempName = matcher.group(1);
        Pattern pattern = Pattern.compile("[A-Z][a-z]?");
        if (!pattern.matcher(tempName).matches()) {
          throw new GrammarException("the element name is invalid");
        }
        factory = new NucleusFactory();
        nucleus = (Nucleus) factory.createPhysicalObject(line);
        continue;
      }
      matcher = numOfTracksPattern.matcher(line);
      if (matcher.find()) {
        if (!tracks.isEmpty()) {
          throw new SameLabelException();
        }
        if (!TrackGame.positivePattern.matcher(matcher.group(1)).matches()) {
          throw new GrammarException("the number of tracks is invalid");
        }
        int number = Integer.parseInt(matcher.group(1));
        for (int i = 1; i <= number; i++) {
          tracks.add(new Track(BigDecimal.valueOf(i)));
        }
        continue;
      }
      matcher = numOfElectronPattern.matcher(line);
      if (matcher.find()) {
        if (!objects.isEmpty()) {
          throw new SameLabelException();
        }
        String[] trackContents = matcher.group(1).split(";");
        for (String trackContent : trackContents) {
          String[] temp = trackContent.split("/");
          List<Electron> tempList = new ArrayList<>();
          if (!tracks.isEmpty() && tracks.size() != trackContents.length) {
            throw new DependencyException();
          }
          if (!TrackGame.positivePattern.matcher(temp[1]).matches()) {
            throw new GrammarException("the number of electrons in a track is invalid");
          }
          for (int j = 0; j < Integer.parseInt(temp[1]); j++) {
            factory = new ElectronFactory();
            tempList.add((Electron) factory.createPhysicalObject(line));
          }
          objects.addAll(tempList);
          if (electrons.size() + 1 != Integer.parseInt(temp[0])) {
            throw new GrammarException("the track sequence is discontinuous");
          }
          electrons.add(tempList);
        }
        continue;
      }
      throw new GrammarException("line can't match");
    }
    ioStrategy.closeIo();
    List<Track> tempTracks = new ArrayList<>(tracks);
    for (int i = 0; i < tempTracks.size(); i++) {
      content.put(tempTracks.get(i), electrons.get(i));
    }
    CentralPoint<Nucleus> centralPoint = new CentralPoint<>(
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
  public void jump(Track from, Track to, Electron electron) throws InvalidInputException {
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
  public void outputInstanceInFile(String filename, IoStrategy ioStrategy) throws IOException {
    List<String> writeContent = new ArrayList<>();
    StringBuffer tempLine = new StringBuffer("ElementName ::= ");
    tempLine.append(this.getElementName());
    writeContent.add(tempLine.toString());
    tempLine = new StringBuffer("NumOfTracks ::= ");
    tempLine.append(this.getTracks().size());
    writeContent.add(tempLine.toString());
    tempLine = new StringBuffer("NumberOfElectron ::= ");
    Map<Track, List<Electron>> contents = this.getTrackContent();
    for (Track track : tracks) {
      tempLine.append(track.getRadius()).append('/').append(contents.get(track).size());
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
  public CircularOrbitIterator<Electron> iterator() {
    return new AtomStructureIterator(this);
  }
}
