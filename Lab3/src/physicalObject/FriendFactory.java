package physicalObject;

import circularOrbit.SocialNetworkCircle;
import java.util.regex.Matcher;

public class FriendFactory implements PhysicalObjectFactory {

  @Override
  public PhysicalObject createPhysicalObject(String fileLine) {
    Matcher matcher = SocialNetworkCircle.friendPattern.matcher(fileLine);
    String name = "";
    String sex = "";
    int age = 0;
    if (matcher.find()) {
      name = matcher.group(1);
      sex = matcher.group(3);
      age = Integer.parseInt(matcher.group(2));
    } else {
      assert false;
    }
    return new Friend(name, age, sex);
  }
}
