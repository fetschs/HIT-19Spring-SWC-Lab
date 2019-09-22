package physicalObject;

public class PositionPhysicalObjectFactory implements PhysicalObjectFactory {

  /**
   * factory method.
   *
   * @param fileLine the line of input(in this class for test, just his name)
   * @return the product
   */
  @Override
  public PhysicalObject createPhysicalObject(String fileLine) {
    return new PositionPhysicalObject(fileLine);
  }
}
