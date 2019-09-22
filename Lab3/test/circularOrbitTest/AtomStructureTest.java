package circularOrbitTest;

import static org.junit.Assert.assertEquals;

import centralPoint.CentralPoint;
import circularOrbit.AtomStructure;
import circularOrbit.AtomStructureOther.AtomStructureStorage;
import circularOrbit.CircularOrbit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import org.junit.Test;
import physicalObject.Electron;
import physicalObject.ElectronFactory;
import physicalObject.Nucleus;
import physicalObject.NucleusFactory;
import physicalObject.PhysicalObjectFactory;
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
  public void jumpAndMemento() {
    AtomStructure atomStructure = AtomStructure
        .createInstanceFromClient("data/AtomicStructure.txt");
    List<Track> tracks = new ArrayList<>(atomStructure.getTracks());
    AtomStructureStorage storage = new AtomStructureStorage(atomStructure.createMemento());
    atomStructure.jump(tracks.get(1), tracks.get(2), createEInstance(""));
    atomStructure.restoreMemento(storage.getMemento());
    //has some wrong TODO
    assertEquals("expected content", storage.getMemento(),
        atomStructure.createMemento());
  }


}