package physicalObject;

public class ElectronFactory implements PhysicalObjectFactory {

  @Override
  public PhysicalObject createPhysicalObject(String fileLine) {
    return new Electron();
  }
}
