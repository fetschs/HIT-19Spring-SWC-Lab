package physicalobject;

public interface PhysicalObjectFactory {

  /**
   * create the PhysicalObject from an line in datafile.
   *
   * @param fileLine an line in datafile, which represents an object.
   * @return the PhysicalObject
   * @throws Exception invalid inpu
   */
  public PhysicalObject createPhysicalObject(String fileLine);
}
