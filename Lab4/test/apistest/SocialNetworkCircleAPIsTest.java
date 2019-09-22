package apistest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import apis.CircularOrbitApis;
import centralpoint.CentralPoint;
import circularorbit.CircularOrbit;
import circularorbit.SocialNetworkCircle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import org.junit.Test;
import physicalobject.CentralUser;
import physicalobject.Friend;
import physicalobject.FriendFactory;
import physicalobject.PhysicalObjectFactory;

public class SocialNetworkCircleAPIsTest extends CircularOrbitApisTest<CentralUser, Friend> {

  @Override
  Friend createEInstance(String tempName) {
    PhysicalObjectFactory physicalObjectFactory = new FriendFactory();
    return (Friend) physicalObjectFactory
        .createPhysicalObject("Friend ::= <" + tempName + ", 25, F>");
  }

  @Override
  CircularOrbit<CentralUser, Friend> createEmptyOrbitInstance() {
    return SocialNetworkCircle.builder().objects(new ArrayList<>()).tracks(new TreeSet<>())
        .trackContent(new HashMap<>()).c2tRelations(new ArrayList<>()).t2tRelations(new HashMap<>())
        .centralPoint(new CentralPoint<>(new ArrayList<>())).build();
  }

  @Test
  public void testLogic() throws Exception{
    SocialNetworkCircle sc;
    try {
      sc = SocialNetworkCircle.createInstanceFromClient("data/SocialNetworkCircle_my.txt");
      Friend f1 = null;
      Friend f2 = null;
      for (Friend friend : sc.getObjects()) {
        if (friend.getName().equals("FrankLee")) {
          f1 = friend;
        }
        if (friend.getName().equals("LisaWong")) {
          f2 = friend;
        }
      }
      CircularOrbitApis circularOrbitAPIs = new CircularOrbitApis();
      assertEquals(3, circularOrbitAPIs.getLogicalDistance(sc, f1, f2));
      assertEquals(3, circularOrbitAPIs.getLogicalDistance(sc, f2, f1));
    } catch (Exception e) {
      fail();
      throw e;
    }
  }
}
