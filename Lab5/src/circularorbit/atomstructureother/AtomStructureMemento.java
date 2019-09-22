package circularorbit.atomstructureother;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import physicalobject.Electron;
import track.Track;

public class AtomStructureMemento {

  private final Map<Track, List<Electron>> contents;
  // Abstraction function:
  // AF(contents) = trackContents
  // Representation invariant:
  // no
  // Safety from rep exposure:
  // all fields are private
  // deep copy and defensive copy

  public AtomStructureMemento(AtomStructureMemento memento) {
    this.contents = memento.getContents();
  }

  public AtomStructureMemento(Map<Track, List<Electron>> tempContents) {
    this.contents = new HashMap<>(tempContents);
  }

  public Map<Track, List<Electron>> getContents() {
    return new HashMap<>(contents);
  }
}
