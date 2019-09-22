package APIsTest;

import centralPoint.CentralPoint;
import circularOrbit.AtomStructure;
import circularOrbit.CircularOrbit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import physicalObject.Electron;
import physicalObject.ElectronFactory;
import physicalObject.Nucleus;
import physicalObject.PhysicalObjectFactory;

public class AtomStructureAPIsTest extends CircularOrbitAPIsTest<Nucleus, Electron> {

  @Override
  Electron createEInstance(String tempName) {
    PhysicalObjectFactory factory = new ElectronFactory();
    return (Electron) factory.createPhysicalObject(tempName);
  }

  @Override
  CircularOrbit<Nucleus, Electron> createEmptyOrbitInstance() {
    return AtomStructure.builder().objects(new ArrayList<>()).tracks(new TreeSet<>())
        .trackContent(new HashMap<>()).c2tRelations(new ArrayList<>()).t2tRelations(new HashMap<>())
        .centralPoint(new CentralPoint<>(new ArrayList<>())).build();
  }
}