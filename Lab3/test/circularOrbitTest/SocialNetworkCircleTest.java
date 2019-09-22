package circularOrbitTest;

import static org.junit.Assert.assertEquals;

import centralPoint.CentralPoint;
import circularOrbit.CircularOrbit;
import circularOrbit.SocialNetworkCircle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import org.junit.Test;
import physicalObject.CentralUser;
import physicalObject.CentralUserFactory;
import physicalObject.Friend;
import physicalObject.FriendFactory;
import physicalObject.PhysicalObjectFactory;

public class SocialNetworkCircleTest extends CircularOrbitTest<CentralUser, Friend> {

  @Override
  Friend createEInstance(String tempName) {
    PhysicalObjectFactory factory = new FriendFactory();
    return (Friend) factory.createPhysicalObject("Friend ::= <" + tempName + ",24,F>");
  }

  @Override
  CentralUser createLInstance(String tempName) {
    PhysicalObjectFactory factory = new CentralUserFactory();
    return (CentralUser) factory.createPhysicalObject("CentralUser ::= <" + tempName + ",24,F>");
  }

  @Override
  CircularOrbit<CentralUser, Friend> createEmptyInstance() {
    return SocialNetworkCircle.builder().objects(new ArrayList<>()).tracks(new TreeSet<>())
        .trackContent(new HashMap<>()).c2tRelations(new ArrayList<>()).t2tRelations(new HashMap<>())
        .centralPoint(new CentralPoint<>(new ArrayList<>())).build();
  }

  /**
   * just test the normal input file.
   */
  @Test
  public void testRead() {
    SocialNetworkCircle socialNetworkCircle = SocialNetworkCircle
        .createInstanceFromClient("data/SocialNetworkCircle_my.txt");
    System.out.println(socialNetworkCircle.getTracks());
    assertEquals("expected size", 1, socialNetworkCircle.calSpreadInFirstTrack("TomWong"));
    assertEquals("expected track size", 2, socialNetworkCircle.getTracks().size());
    //has some wrong TODO
  }
}