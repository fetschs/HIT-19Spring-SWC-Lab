package apistest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import centralpoint.CentralPoint;
import circularorbit.AtomStructure;
import circularorbit.CircularOrbit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import physicalobject.Electron;
import physicalobject.ElectronFactory;
import physicalobject.Nucleus;
import physicalobject.PhysicalObjectFactory;

public class AtomStructureAPIsTest extends CircularOrbitApisTest<Nucleus, Electron> {

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