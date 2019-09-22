package apis;

import circularorbit.CircularOrbit;
import circularorbit.Difference;
import exception.InvalidInputException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import lombok.NoArgsConstructor;
import physicalobject.PositionPhysicalObject;
import position.Position;
import relation.Relation;
import track.Track;

@NoArgsConstructor
public class CircularOrbitApis {


  /**
   * just calculate the recursion num!.
   *
   * @param num the number.
   * @return the recursion num!.
   */
  private static BigInteger recursion(BigInteger num) {
    BigInteger sum = BigInteger.ONE;
    for (BigInteger i = BigInteger.ONE; i.compareTo(num) <= 0; i = i.add(BigInteger.ONE)) {
      sum = sum.multiply(i);
    }
    return sum;
  }

  /**
   * calculate the Entropy of this circularorbit, assume we know the distribution, then we can
   * figure out the number of possible distributions, and the number will be proportional to the
   * Entropy, so we use this number as our Entropy.
   *
   * @param c the circularorbit will be calculate.
   * @param <L> the centralpoint of the Orbit.
   * @param <E> the physicalobject of the Orbit.
   * @return the Entropy.
   */
  public <L, E> BigInteger getObjectDistributionEntropy(CircularOrbit<L, E> c) {
    BigInteger objectSize = BigInteger.valueOf(c.getObjects().size());
    BigInteger factorial = recursion(objectSize);
    Map<Track, List<E>> content = c.getTrackContent();
    for (List<E> objects : content.values()) {
      factorial = factorial.divide(recursion(BigInteger.valueOf(objects.size())));
    }
    return factorial;
  }

  /**
   * get the physicalDistance between e1 and e2.
   *
   * @param c the orbit
   * @param e1 one object
   * @param e2 another object
   * @param <L> the central object.
   * @param <E> the track object.
   * @return the physicalDistance.
   * @throws Exception when the e1 and e2 can't calculate distance.
   */
  public <L, E> BigDecimal getPhysicalDistance(CircularOrbit<L, E> c, E e1, E e2)
      throws InvalidInputException {
    if (e1 instanceof PositionPhysicalObject && e2 instanceof PositionPhysicalObject) {
      Position p1 = ((PositionPhysicalObject) e1).getPosition();
      Position p2 = ((PositionPhysicalObject) e2).getPosition();
      BigDecimal pho1 = p1.getRho();
      BigDecimal theta1 = p1.getTheta();
      BigDecimal pho2 = p2.getRho();
      BigDecimal theta2 = p2.getTheta();
      double delta = Math.toRadians(theta1.subtract(theta2).doubleValue());
      BigDecimal temp1 = pho1.multiply(pho1).add(pho2.multiply(pho2));
      BigDecimal temp2 = BigDecimal.valueOf(2).multiply(pho1).multiply(pho2)
          .multiply(BigDecimal.valueOf(Math.cos(delta)));
      return temp1.subtract(temp2);
    } else {
      throw new InvalidInputException(Thread.currentThread().getStackTrace()[1].getMethodName(),
          "can't get physical distance");
    }
  }

  /**
   * get the logical distance between objects in a orbit, return the max of int if no relations from
   * e1 to e2.
   *
   * @param c the orbit
   * @param e1 one object
   * @param e2 another object
   * @param <E> the class of object
   * @return the logicalDistance.
   */
  public <L, E> int getLogicalDistance(CircularOrbit<L, E> c, E e1, E e2) {
    Map<E, List<Relation<E, E>>> map = c.getT2tRelations();
    Queue<E> queue = new ConcurrentLinkedDeque<>();
    queue.add(e1);
    Map<E, Integer> distance = new HashMap<>();
    Map<E, Boolean> hasRelation2Central = new HashMap<>();
    List<E> objects = c.getObjects();
    for (E object : objects) {
      distance.put(object, -1);
      hasRelation2Central.put(object, false);
    }
    for (Relation<L, E> relation : c.getC2tRelations()) {
      hasRelation2Central.put(relation.getTo(), true);
    }
    distance.put(e1, 0);
    while (!queue.isEmpty()) {
      E now = queue.poll();
      List<Relation<E, E>> relations = map.get(now);
      for (Relation<E, E> relation : relations) {
        E tempObject = relation.getTo();
        if (tempObject.equals(e2)) {
          return distance.get(now) + 1;
        }
        if (distance.get(tempObject) == -1) {
          queue.add(tempObject);
          distance.put(tempObject, distance.get(now) + 1);
        }
      }
      if (hasRelation2Central.get(now)) {
        for (E object : objects) {
          if (hasRelation2Central.get(object)) {
            if (distance.get(object) > distance.get(now) + 2 || distance.get(object) == -1) {
              queue.add(object);
              distance.put(object, distance.get(now) + 2);
              if (object.equals(e2)) {
                return distance.get(object);
              }
            }
          }
        }
      }
    }
    return (int) (Double.POSITIVE_INFINITY);
  }

  /**
   * get the difference between orbits.
   *
   * @param c1 one orbit
   * @param c2 another orbit
   * @param <L> central Object
   * @param <E> track Object
   * @return the difference
   */
  public <L, E> Difference getDifference(CircularOrbit<L, E> c1, CircularOrbit<L, E> c2) {
    return new Difference<>(c1, c2);
  }
}
