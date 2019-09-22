package physicalobject;

import circularorbit.AtomStructure;
import java.util.regex.Matcher;

public class NucleusFactory implements PhysicalObjectFactory {

  @Override
  public PhysicalObject createPhysicalObject(String fileLine) {
    Matcher matcher = AtomStructure.elementPattern.matcher(fileLine);
    String name = "";
    if (matcher.find()) {
      name = matcher.group(1);
    } else {
      assert false;
    }
    return new Nucleus(name);
  }
}
