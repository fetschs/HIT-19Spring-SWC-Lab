package physicalObject;

import circularOrbit.SocialNetworkCircle;
import java.util.regex.Matcher;

public class CentralUserFactory implements PhysicalObjectFactory {

  @Override
  public PhysicalObject createPhysicalObject(String fileLine) {
    Matcher matcher = SocialNetworkCircle.centralUserPattern.matcher(fileLine);
    String name = "";
    int age = 0;
    String sex = "";
    if (matcher.find()) {
      name = matcher.group(1);
      age = Integer.parseInt(matcher.group(2));
      sex = matcher.group(3);
    } else {
      assert false;
    }
    return new CentralUser(name, age, sex);
  }
}
