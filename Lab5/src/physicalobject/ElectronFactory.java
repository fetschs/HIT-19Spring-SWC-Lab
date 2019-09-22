package physicalobject;

public class ElectronFactory implements PhysicalObjectFactory {

  private static Electron instance = new Electron();

  @Override
  public PhysicalObject createPhysicalObject(String fileLine) {
    return instance;
  }
}
