package applications;

import APIs.CircularOrbitAPIs;
import APIs.CircularOrbitHelper;
import circularOrbit.AtomStructure;
import circularOrbit.CircularOrbit;
import circularOrbit.SocialNetworkCircle;
import circularOrbit.TrackGame;
import circularOrbit.TrackGameOther.BestStrategy;
import circularOrbit.TrackGameOther.RandomStrategy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import physicalObject.Athlete;
import physicalObject.AthleteFactory;
import physicalObject.Electron;
import physicalObject.ElectronFactory;
import physicalObject.Friend;
import physicalObject.FriendFactory;
import physicalObject.PhysicalObjectFactory;
import relation.Relation;
import track.Track;

public class myClient {

  public static void main(String[] args) throws IOException {
    System.out.println("Please enter the circular object you want to create.");
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String kindStr = br.readLine();
    if (kindStr.equals("TrackGame")) {
      System.out.println("Please enter the file to build the object.");
      String fileName = br.readLine();
      String strategyName = br.readLine();
      TrackGame nowTrackGame = TrackGame.creatInstanceFromClient(fileName, new RandomStrategy());
      if (strategyName.equals("Random")) {
        nowTrackGame = TrackGame.creatInstanceFromClient(fileName, new RandomStrategy());
      } else if (strategyName.equals("Best")) {
        nowTrackGame = TrackGame.creatInstanceFromClient(fileName, new BestStrategy());
      }
      String tempLine = br.readLine();
      String[] actions = tempLine.split(" ");

      while (!actions[0].equals("end")) {
        tempLine = br.readLine();
        actions = tempLine.split(" ");
        Athlete newAthlete;
        PhysicalObjectFactory factory = new AthleteFactory();
        if (actions[0].equals("newTrack")) {
          nowTrackGame.addTrack(new Track(new BigDecimal(actions[1])));
        } else if (actions[0].equals("newNode")) {
          newAthlete = (Athlete) factory.createPhysicalObject(actions[1]);
          nowTrackGame.addPhysicalObjectInTrack(newAthlete, new Track(new BigDecimal(actions[2])));
        } else if (actions[0].equals("removeTrack")) {
          nowTrackGame.removeTrack(new Track(new BigDecimal(actions[1])));
        } else if (actions[0].equals("removeNode")) {
          newAthlete = (Athlete) factory.createPhysicalObject(actions[1]);
          for (CircularOrbit circularOrbit : nowTrackGame.getGroups()) {
            if (circularOrbit.getObjects().contains(newAthlete)) {
              circularOrbit.removeTrack(new Track(new BigDecimal(actions[2])));
            }
          }
        } else if (actions[0].equals("calcEntropy")) {
          CircularOrbitAPIs apis = new CircularOrbitAPIs();
          System.out.println(apis.getObjectDistributionEntropy(nowTrackGame));
        } else if (actions[0].equals("change")) {
          Athlete newAthlete1 = (Athlete) factory.createPhysicalObject(actions[1]);
          Athlete newAthlete2 = (Athlete) factory.createPhysicalObject(actions[3]);
          for (CircularOrbit circularOrbit : nowTrackGame.getGroups()) {
            if (circularOrbit.getObjects().contains(newAthlete1)) {
              circularOrbit.removeTrack(new Track(new BigDecimal(actions[2])));
            } else if (circularOrbit.getObjects().contains(newAthlete2)) {
              circularOrbit.removeTrack(new Track(new BigDecimal(actions[4])));
            }
          }
        }
      }
    } else if (kindStr.equals("AtomStructure")) {
      System.out.println("Please enter the file to build the object.");
      String fileName = br.readLine();
      AtomStructure atomStructure = AtomStructure.createInstanceFromClient(fileName);
      String tempLine = br.readLine();
      String[] actions = tempLine.split(" ");
      while (!actions[0].equals("end")) {
        tempLine = br.readLine();
        actions = tempLine.split(" ");
        Electron newElectron;
        if (actions[0].equals("newTrack")) {
          atomStructure.addTrack(new Track(BigDecimal.valueOf(Double.parseDouble(actions[2]))));
        } else if (actions[0].equals("newNode")) {
          newElectron = (Electron) new ElectronFactory().createPhysicalObject("");
          atomStructure
              .addPhysicalObjectInTrack(newElectron, new Track(new BigDecimal(actions[1])));//radius
        } else if (actions[0].equals("removeTrack")) {
          atomStructure.removeTrack(new Track(new BigDecimal(actions[1])));
        } else if (actions[0].equals("removeNode")) {
          newElectron = (Electron) new ElectronFactory().createPhysicalObject("");
          atomStructure
              .removePhysicalObjectInTrack(new Track(new BigDecimal(actions[2])), newElectron);
        } else if (actions[0].equals("calcEntropy")) {
          CircularOrbitAPIs apis = new CircularOrbitAPIs();
          System.out.println(apis.getObjectDistributionEntropy(atomStructure));
        } else if (actions[0].equals("Jump")) {
          newElectron = (Electron) new ElectronFactory().createPhysicalObject("");
          atomStructure.jump(new Track(new BigDecimal(actions[1])),
              new Track(new BigDecimal(actions[2])), newElectron);
        }
      }
    } else if (kindStr.equals("SocialNetworkCircle")) {
      System.out.println("Please enter the file to build the object.");
      String fileName = br.readLine();
      SocialNetworkCircle socialNetworkCircle = SocialNetworkCircle
          .createInstanceFromClient(fileName);
      String tempLine = br.readLine();
      String[] actions = tempLine.split(" ");
      while (!actions[0].equals("end")) {
        tempLine = br.readLine();
        actions = tempLine.split(" ");
        Friend newFriend;
        PhysicalObjectFactory factory = new FriendFactory();
        if (actions[0].equals("newTrack")) {
          socialNetworkCircle.addTrack(new Track(new BigDecimal(actions[1])));
        } else if (actions[0].equals("newNode")) {
          newFriend = (Friend) factory
              .createPhysicalObject(actions[1]);
          socialNetworkCircle
              .addPhysicalObjectInTrack(newFriend, new Track(new BigDecimal(actions[2])));
        } else if (actions[0].equals("removeTrack")) {
          socialNetworkCircle.removeTrack(new Track(new BigDecimal(actions[1])));
        } else if (actions[0].equals("removeNode")) {
          newFriend = (Friend) factory.createPhysicalObject(actions[1]);
          socialNetworkCircle
              .removePhysicalObjectInTrack(new Track(new BigDecimal(actions[2])), newFriend);
        } else if (actions[0].equals("calcEntropy")) {
          CircularOrbitAPIs apis = new CircularOrbitAPIs();
          System.out.println(apis.getObjectDistributionEntropy(socialNetworkCircle));
        } else if (actions[0].equals("GUI")) {
          CircularOrbitHelper.draw(socialNetworkCircle);
        } else if (actions[0].equals("calcspread")) {
          newFriend = (Friend) factory.createPhysicalObject(actions[1]);
          socialNetworkCircle.calSpreadInFirstTrack(newFriend.getName());
        } else if (actions[0].equals("addRelation")) {
          Friend newFriend1 = (Friend) factory.createPhysicalObject(actions[1]);
          Friend newFriend2 = (Friend) factory.createPhysicalObject(actions[2]);
          socialNetworkCircle.addT2tRelation(
              new Relation<Friend, Friend>(new BigDecimal(actions[3]), newFriend1, newFriend2));
        } else if (actions[0].equals("calcLogicalDistance")) {
          Friend newFriend1 = (Friend) factory.createPhysicalObject(actions[1]);
          Friend newFriend2 = (Friend) factory.createPhysicalObject(actions[2]);
          CircularOrbitAPIs apis = new CircularOrbitAPIs();
          System.out
              .println(apis.getLogicalDistance(socialNetworkCircle, newFriend1, newFriend2));
        }
      }
    }
  }
}
