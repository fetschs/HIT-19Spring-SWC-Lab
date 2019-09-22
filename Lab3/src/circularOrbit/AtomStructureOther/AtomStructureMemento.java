package circularOrbit.AtomStructureOther;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import physicalObject.Electron;
import track.Track;

@AllArgsConstructor
@EqualsAndHashCode
public class AtomStructureMemento {

  private final Map<Track, List<Electron>> contents;
  // Abstraction function:
  // AF(contents) = trackContents
  // Representation invariant:
  // no
  // Safety from rep exposure:
  // all fields are private
  // no return

  public AtomStructureMemento(AtomStructureMemento memento) {
    this.contents = memento.getContents();
  }

  public Map<Track, List<Electron>> getContents() {
    return new HashMap<>(contents);
  }
}
