package circularorbittest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import centralpoint.CentralPoint;
import circularorbit.CircularOrbit;
import circularorbit.SocialNetworkCircle;
import exception.DependencyException;
import exception.GrammarException;
import exception.SameLabelException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import org.junit.Test;
import physicalobject.CentralUser;
import physicalobject.CentralUserFactory;
import physicalobject.Friend;
import physicalobject.FriendFactory;
import physicalobject.PhysicalObjectFactory;

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
    SocialNetworkCircle socialNetworkCircle = null;
    try {
      socialNetworkCircle = SocialNetworkCircle.createInstanceFromClient("data/SocialNetworkCircle_my.txt");
      assertEquals("expected size", 1, socialNetworkCircle.calSpreadInFirstTrack("TomWong"));
      assertEquals("expected track size", 4, socialNetworkCircle.getTracks().size());
    } catch (GrammarException | FileNotFoundException | SameLabelException | DependencyException e) {
      fail();
    }
    Set<String> messages = new HashSet<>();
    for (int i = 1; i <= 12; i++) {
      try {
        SocialNetworkCircle errorAtom = SocialNetworkCircle.createInstanceFromClient("errdata/Social_err_" + i + ".txt");
      } catch (GrammarException | FileNotFoundException | SameLabelException | DependencyException e ) {
        messages.add(e.getMessage());
      }
    }
    assertEquals("8 exceptions input", 8, messages.size());
  }
}