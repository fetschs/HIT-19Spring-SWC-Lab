package circularorbittest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import centralpoint.CentralPoint;
import circularorbit.AtomStructure;
import circularorbit.CircularOrbit;
import circularorbit.atomstructureother.AtomStructureStorage;
import circularorbit.iostrategy.ScannerAndFilesIo;
import exception.DependencyException;
import exception.GrammarException;
import exception.InvalidInputException;
import exception.SameLabelException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.junit.Test;
import physicalobject.Electron;
import physicalobject.ElectronFactory;
import physicalobject.Nucleus;
import physicalobject.NucleusFactory;
import physicalobject.PhysicalObjectFactory;
import track.Track;

public class AtomStructureTest extends CircularOrbitTest<Nucleus, Electron> {

  @Override
  Electron createEInstance(String tempName) {
    PhysicalObjectFactory factory = new ElectronFactory();
    return (Electron) factory.createPhysicalObject("");
  }

  @Override
  Nucleus createLInstance(String tempName) {
    PhysicalObjectFactory factory = new NucleusFactory();
    return (Nucleus) factory.createPhysicalObject("ElementName ::= " + tempName);
  }

  @Override
  CircularOrbit<Nucleus, Electron> createEmptyInstance() {
    return AtomStructure.builder().objects(new ArrayList<>()).tracks(new TreeSet<>())
        .trackContent(new HashMap<>()).c2tRelations(new ArrayList<>()).t2tRelations(new HashMap<>())
        .centralPoint(new CentralPoint<>(new ArrayList<>())).build();
  }

  @Test
  public void testMethod() {
    AtomStructure atomStructure = null;
    try {
      String filename = "data/AtomicStructure.txt";
      atomStructure = AtomStructure
          .createInstanceFromClient(filename, new ScannerAndFilesIo(new File(filename)));
    } catch (Exception e) {
      fail();
    }
    List<Track> tracks = new ArrayList<>(atomStructure.getTracks());
    Set<String> messages = new HashSet<>();
    for (int i = 1; i <= 11; i++) {
      try {
        String filename = "errdata/Atom_err_" + i + ".txt";
        AtomStructure errorAtom = AtomStructure
            .createInstanceFromClient(filename, new ScannerAndFilesIo(new File(filename)));
      } catch (GrammarException | IOException | SameLabelException | DependencyException e) {
        messages.add(e.getMessage());
      }
    }
    assertEquals("8 exceptions input", 8, messages.size());
    AtomStructureStorage storage = new AtomStructureStorage(atomStructure.createMemento());
    try {
      atomStructure.jump(tracks.get(1), tracks.get(2), createEInstance(""));
    } catch (InvalidInputException e) {
      assertTrue("test Invalid", true);
    }
    atomStructure.restoreMemento(storage.getMemento());
    assertEquals("expected content", storage.getMemento().getContents(),
        atomStructure.createMemento().getContents());
  }


}