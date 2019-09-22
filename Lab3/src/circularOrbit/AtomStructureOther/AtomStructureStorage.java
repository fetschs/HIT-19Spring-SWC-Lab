package circularOrbit.AtomStructureOther;

public class AtomStructureStorage {

  private AtomStructureMemento memento;
  //AF(memento) = save the memento

  /**
   * construction method.
   *
   * @param tempMemento the memento.
   */
  public AtomStructureStorage(AtomStructureMemento tempMemento) {
    this.memento = new AtomStructureMemento(tempMemento);
  }

  public AtomStructureMemento getMemento() {
    return new AtomStructureMemento(memento);
  }
  // Abstraction function:
  // AF(memento) = memento of atomStruct
  // Representation invariant:
  // no
  // Safety from rep exposure:
  // all fields are private
  // no return and mutable

}
