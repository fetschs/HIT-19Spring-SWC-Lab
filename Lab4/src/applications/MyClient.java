package applications;

import apis.CircularOrbitApis;
import apis.CircularOrbitHelper;
import circularorbit.AtomStructure;
import circularorbit.CircularOrbit;
import circularorbit.SocialNetworkCircle;
import circularorbit.TrackGame;
import circularorbit.trackgameother.BestStrategy;
import circularorbit.trackgameother.RandomStrategy;
import exception.DependencyException;
import exception.GrammarException;
import exception.InvalidInputException;
import exception.SameLabelException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import physicalobject.Athlete;
import physicalobject.AthleteFactory;
import physicalobject.Electron;
import physicalobject.ElectronFactory;
import physicalobject.Friend;
import physicalobject.FriendFactory;
import physicalobject.PhysicalObjectFactory;
import relation.Relation;
import track.Track;

public class MyClient {

  private static Logger exceptionLog = Logger.getLogger("exceptionLogger");
  private static Logger actionLog = Logger.getLogger("actionLogger");

  /**
   * Client code.
   *
   * @param args no use.
   */
  public static void main(String[] args) {
    boolean endFlag = false;
    FileHandler exceptionFileHandler;
    FileHandler actionFileHandler;
    try {
      exceptionFileHandler = new FileHandler("./log/exception.log");
      //exceptionFileHandler.setFormatter(new SimpleFormatter());
      actionFileHandler = new FileHandler("./log/action.log");
      //actionFileHandler.setFormatter(new SimpleFormatter());
    } catch (IOException e) {
      System.out.println("Please creat your log file");
      return;
    }
    exceptionLog.addHandler(exceptionFileHandler);
    actionLog.addHandler(actionFileHandler);
    System.out.println("Please input the circular orbit you want to create.");
    Scanner sc = new Scanner(System.in, "utf-8");
    while (true) {
      String kindStr = sc.nextLine().trim();
      switch (kindStr) {
        case "TrackGame": {
          TrackGame trackGame;
          while (true) {
            System.out.println("Please input the filename to build the orbit.");
            String fileName = sc.nextLine().trim();
            System.out.println("Please input the strategy");
            String strategyName = sc.nextLine().trim();
            try {
              if (strategyName.equals("Random")) {
                trackGame = TrackGame.creatInstanceFromClient(fileName, new RandomStrategy());
              } else if (strategyName.equals("Best")) {
                trackGame = TrackGame.creatInstanceFromClient(fileName, new BestStrategy());
              } else {
                System.out.println("Please input the strategy again");
                continue;
              }
            } catch (SameLabelException | GrammarException | FileNotFoundException e) {
              System.out.println("Please input filename again.");
              exceptionLog.info(
                  e.getClass() + " IN " + kindStr + "." + "creatInstanceFromClient\n" + e
                      .getMessage());
              continue;
            }
            break;
          }
          System.out.println("Please input your action as these format:\n"
              + "1.\"newTrack radius \"\n"
              + "2.\"newNode patternLine track \"\n"
              + "3.\"removeTrack radius \"\n"
              + "4.\"removeNode patternLine \"\n"
              + "5.\"calcEntropy \"\n"
              + "6.\"change athlete1 track1 athlete2 track2 \"\n"
              + "7.\"GUI \"\n"
              + "8.\"end \"\n");
          while (!endFlag) {
            String tempLine = sc.nextLine().trim();
            String[] actions = tempLine.split("\\s+");
            Athlete newAthlete;
            PhysicalObjectFactory factory = new AthleteFactory();
            tempLine = sc.nextLine().trim();
            actions = tempLine.split("\\s+");
            try {
              switch (actions[0]) {
                case "addTrack": {
                  trackGame.addTrack(new Track(new BigDecimal(actions[1])));
                  break;
                }
                case "newNode": {
                  newAthlete = (Athlete) factory.createPhysicalObject(actions[1]);
                  trackGame
                      .addPhysicalObjectInTrack(newAthlete, new Track(new BigDecimal(actions[2])));
                  break;
                }
                case "removeTrack": {
                  trackGame.removeTrack(new Track(new BigDecimal(actions[1])));
                  break;
                }
                case "removeNode": {
                  newAthlete = (Athlete) factory.createPhysicalObject(actions[1]);
                  for (CircularOrbit circularOrbit : trackGame.getGroups()) {
                    if (circularOrbit.getObjects().contains(newAthlete)) {
                      circularOrbit.removeTrack(new Track(new BigDecimal(actions[2])));
                    }
                  }
                  break;
                }
                case "calcEntropy": {
                  CircularOrbitApis apis = new CircularOrbitApis();
                  System.out.println(apis.getObjectDistributionEntropy(trackGame));
                  break;
                }
                case "change": {
                  Athlete newAthlete1 = (Athlete) factory.createPhysicalObject(actions[1]);
                  Track track1 = new Track(new BigDecimal(actions[2]));
                  Athlete newAthlete2 = (Athlete) factory.createPhysicalObject(actions[3]);
                  Track track2 = new Track(new BigDecimal(actions[4]));
                  for (CircularOrbit<Athlete, Athlete> circularOrbit : trackGame.getGroups()) {
                    if (circularOrbit.getObjects().contains(newAthlete1)) {
                      circularOrbit.removePhysicalObjectInTrack(track1, newAthlete1);
                      circularOrbit.addPhysicalObjectInTrack(newAthlete2, track2);
                    } else if (circularOrbit.getObjects().contains(newAthlete2)) {
                      circularOrbit.removePhysicalObjectInTrack(track2, newAthlete2);
                      circularOrbit.addPhysicalObjectInTrack(newAthlete1, track1);
                    }
                  }
                  break;
                }
                case "GUI": {
                  CircularOrbitHelper.draw(trackGame);
                  break;
                }
                case "end": {
                  endFlag = true;
                  break;
                }
                default: {
                  System.out.println("Invalid action,Please input your action again");
                  continue;
                }
              }
            } catch (InvalidInputException e) {
              System.out.println("Invalid Input, Please input your action again");
              exceptionLog.info(
                  e.getClass() + " IN " + kindStr + "." + e.getMethod() + '\n' + e.getMessage());
            }
            actionLog.info(actions[0] + " IN " + kindStr + ".");
          }
          break;
        }
        case "AtomStructure": {
          System.out.println("Please input the filename to build the orbit.");
          AtomStructure atomStructure;
          while (true) {
            String fileName = sc.nextLine().trim();
            try {
              atomStructure = AtomStructure.createInstanceFromClient(fileName);
            } catch (GrammarException | FileNotFoundException
                | SameLabelException | DependencyException e) {
              System.out.println("Please input filename again.");
              exceptionLog.info(
                  e.getClass() + " IN " + kindStr + "." + "creatInstanceFromClient\n" + e
                      .getMessage());
              continue;
            }
            break;
          }
          System.out.println("Please input your action as these format:\n"
              + "1.\"newTrack radius \"\n"
              + "2.\"newNode patternLine track \"\n"
              + "3.\"removeTrack radius \""
              + "4.\"removeNode patternLine \"\n"
              + "5.\"calcEntropy \"\n"
              + "6.\"Jump track1 track2 \"\n"
              + "7.\"GUI \"\n"
              + "8.\"end \"\n");
          while (!endFlag) {
            String tempLine = sc.nextLine().trim();
            String[] actions = tempLine.split("\\s+");
            Electron newElectron;
            try {
              switch (actions[0]) {
                case "newTrack": {
                  atomStructure
                      .addTrack(new Track(BigDecimal.valueOf(Double.parseDouble(actions[1]))));
                  break;
                }
                case "newNode": {
                  newElectron = (Electron) new ElectronFactory().createPhysicalObject("");
                  atomStructure
                      .addPhysicalObjectInTrack(newElectron,
                          new Track(new BigDecimal(actions[1])));//radius
                  break;
                }
                case "removeTrack": {
                  atomStructure.removeTrack(new Track(new BigDecimal(actions[1])));
                  break;
                }
                case "removeNode": {
                  newElectron = (Electron) new ElectronFactory().createPhysicalObject("");
                  atomStructure
                      .removePhysicalObjectInTrack(new Track(new BigDecimal(actions[2])),
                          newElectron);
                  break;
                }
                case "calcEntropy": {
                  CircularOrbitApis apis = new CircularOrbitApis();
                  System.out.println(apis.getObjectDistributionEntropy(atomStructure));
                  break;
                }
                case "Jump": {
                  newElectron = (Electron) new ElectronFactory().createPhysicalObject("");
                  atomStructure.jump(new Track(new BigDecimal(actions[1])),
                      new Track(new BigDecimal(actions[2])), newElectron);
                  break;
                }
                case "GUI": {
                  CircularOrbitHelper.draw(atomStructure);
                  break;
                }
                case "end": {
                  endFlag = true;
                  break;
                }
                default: {
                  System.out.println("Invalid action,Please input your action again");
                  continue;
                }
              }
            } catch (InvalidInputException e) {
              System.out.println("Invalid Input, Please input your action again");
              exceptionLog.info(e.getClass() + " IN " + kindStr + "." + e.getMethod());
            }
            actionLog.info(actions[0] + " IN " + kindStr + ".");
          }
          break;
        }
        case "SocialNetworkCircle": {
          System.out.println("Please input the filename to build the orbit.");
          SocialNetworkCircle socialNetworkCircle;
          while (true) {
            String fileName = sc.nextLine().trim();
            try {
              socialNetworkCircle = SocialNetworkCircle
                  .createInstanceFromClient(fileName);
            } catch (GrammarException | SameLabelException
                | FileNotFoundException | DependencyException e) {
              System.out.println("Please input filename again.");
              exceptionLog.info(
                  e.getClass() + " IN " + kindStr + "." + "creatInstanceFromClient\n" + e
                      .getMessage());
              continue;
            }
            break;
          }
          System.out.println("Please input your action as these format:\n"
              + "1.\"newTrack radius \"\n"
              + "2.\"newNode patternLine track \"\n"
              + "3.\"removeTrack radius \"\n"
              + "4.\"removeNode patternLine \"\n"
              + "5.\"calcEntropy \"\n"
              + "6.\"change athlete1 track1 athlete2 track2 \"\n"
              + "7.\"addRelation friend1 friend2 len \"\n"
              + "8.\"calcLogicalDistance friend1 friend2 \"\n"
              + "9.\"GUI \"\n"
              + "9.\"calcSpread friend\"\n"
              + "10.\"end \"\n");
          while (!endFlag) {
            String tempLine = sc.nextLine().trim();
            String[] actions = tempLine.split("\\s+");
            try {
              Friend newFriend;
              PhysicalObjectFactory factory = new FriendFactory();
              switch (actions[0]) {
                case "newTrack": {
                  socialNetworkCircle.addTrack(new Track(new BigDecimal(actions[1])));
                  break;
                }
                case "newNode": {
                  newFriend = (Friend) factory
                      .createPhysicalObject(actions[1]);
                  socialNetworkCircle
                      .addPhysicalObjectInTrack(newFriend, new Track(new BigDecimal(actions[2])));
                  break;
                }
                case "removeTrack": {
                  socialNetworkCircle.removeTrack(new Track(new BigDecimal(actions[1])));
                  break;
                }
                case "removeNode": {
                  newFriend = (Friend) factory.createPhysicalObject(actions[1]);
                  socialNetworkCircle
                      .removePhysicalObjectInTrack(new Track(new BigDecimal(actions[2])),
                          newFriend);
                  break;
                }
                case "calcEntropy": {
                  CircularOrbitApis apis = new CircularOrbitApis();
                  System.out.println(apis.getObjectDistributionEntropy(socialNetworkCircle));
                  break;
                }
                case "GUI": {
                  CircularOrbitHelper.draw(socialNetworkCircle);
                  break;
                }
                case "calcSpread": {
                  newFriend = (Friend) factory.createPhysicalObject(actions[1]);
                  socialNetworkCircle.calSpreadInFirstTrack(newFriend.getName());
                  break;
                }
                case "addRelation": {
                  Friend newFriend1 = (Friend) factory.createPhysicalObject(actions[1]);
                  Friend newFriend2 = (Friend) factory.createPhysicalObject(actions[2]);
                  socialNetworkCircle.addT2tRelation(
                      new Relation<>(new BigDecimal(actions[3]), newFriend1,
                          newFriend2));
                  break;
                }
                case "calcLogicalDistance": {
                  Friend newFriend1 = (Friend) factory.createPhysicalObject(actions[1]);
                  Friend newFriend2 = (Friend) factory.createPhysicalObject(actions[2]);
                  CircularOrbitApis apis = new CircularOrbitApis();
                  System.out.println(
                      apis.getLogicalDistance(socialNetworkCircle, newFriend1, newFriend2));
                  break;
                }
                case "end": {
                  endFlag = true;
                  break;
                }
                default: {
                  System.out.println("Invalid action,Please input your action again");
                }
              }
            } catch (InvalidInputException e) {
              System.out.println("Invalid Input, Please input your action again");
              exceptionLog.info(
                  e.getClass() + " IN " + kindStr + "." + e.getMethod() + '\n' + e.getMessage());
            }
            actionLog.info(actions[0] + " IN " + kindStr);
          }
          break;
        }
        default: {
          System.out.println("Please input your orbit kind again");
          continue;
        }
      }
      sc.close();
      break;
    }
  }
}
