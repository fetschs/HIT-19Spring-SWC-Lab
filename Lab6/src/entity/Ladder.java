package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ladder {

  private final int id;
  private final List<Rung> rungs;
  private int monkeyNumber;
  private String direction = "Empty";

  // Abstraction function:
  // AF(id) = the id of this Ladder
  // AF(monkeyNumber) = the number of monkeyNumber
  // AF(direction) = the direction of Monkeys in this Ladder
  // AF(rungs) = some rungs to save monkey
  // Representation invariant:
  // direction should be "L->R" or "R->L" or "Empty"(where no monkey)
  // Safety from rep exposure:
  // all fields are private.
  // defensive copy for get
  // thread safe:
  // won't share data.
  public Ladder(int id, int numberOfRungs) {
    this.monkeyNumber = 0;
    this.id = id;
    this.rungs = Collections.synchronizedList(new ArrayList<>());
    for (int i = 1; i <= numberOfRungs; i++) {
      this.rungs.add(new Rung());
    }
  }

  /**
   * get the monkeyNumber
   *
   * @return this.monkeyNumber
   */
  public int getMonkeyNumber() {
    return this.monkeyNumber;
  }

  /**
   * get the rungs by defensive copy.
   *
   * @return this.rungs.
   */
  public List<Rung> getRungs() {
    return Collections.synchronizedList(new ArrayList<>(this.rungs));
  }

  /**
   * get the First Monkey.
   *
   * @return the First Monkey.
   */
  public Monkey getFirstMonkey() {
    if (this.getDirection().equals(Monkey.L2R)) {
      return rungs.get(0).getMonkey();
    } else {
      return rungs.get(rungs.size() - 1).getMonkey();
    }
  }

  /**
   * make a monkey jump up to the ladder.
   *
   * @param monkey the monkey will jump.
   * @param target the target rung's index. should be 0 or rungs.size()-1
   */
  public void jumpMonkey(Monkey monkey, int target) {
    Rung targetRung = rungs.get(target);
    targetRung.setMonkey(monkey);
    rungs.set(target, targetRung);
    this.monkeyNumber++;
    this.direction = monkey.getDirection();
  }

  /**
   * make a monkey jump from a rung to another.
   *
   * @param monkey the monkey will jump.
   * @param from from rung's index.
   * @param to to rung's index.
   */
  public void jumpMonkey(Monkey monkey, int from, int to) {
    Rung fromRung = rungs.get(from);
    Rung targetRung = rungs.get(to);
    fromRung.setMonkey(null);
    rungs.set(from, fromRung);
    targetRung.setMonkey(monkey);
    rungs.set(to, targetRung);
  }

  /**
   * down a monkey from the Ladder.
   *
   */
  public void downMonkey() {
    if (this.getDirection().equals(Monkey.L2R)) {
      rungs.set(rungs.size() - 1, new Rung());
    } else {
      rungs.set(0, new Rung());
    }
    this.monkeyNumber--;
    if(this.monkeyNumber == 0)
      this.direction = "Empty";
  }

  public int getId() {
    return id;
  }

  public String getDirection() {
    return direction;
  }
}
