package entity;

import lombok.EqualsAndHashCode;
import strategy.FollowStrategy;
import strategy.LazyStrategy;
import strategy.ladderStrategy;
import work.MonkeyGenerator;

@EqualsAndHashCode
public class Monkey implements Runnable {

  public static String L2R = "L->R";
  public static String R2L = "R->L";
  private final int id;
  private final String direction;
  private final int v;
  private long bornTime;
  private long endTime;

  // Abstraction function:
  // AF(id) = the id of this Monkey
  // AF(direction) = the direction of this Monkey
  // AF(v) = the speed of this Monkey
  // AF(bornTime) = the time of this Monkey born
  // AF(endTime) = the time of this Monkey arrive
  // Representation invariant:
  // direction should be "L->R" or "R->L"
  // endTime should be after bornTime
  // Safety from rep exposure:
  // all fields are private and immutable.
  // RI:direction should be L->R or R->L
  // thread safe:
  // won't share data.
  private void checkRep() {
    assert (this.getDirection().equals(L2R) || this.getDirection().equals(R2L));
  }

  public Monkey(int id, String direction, int v) {
    this.id = id;
    this.direction = direction;
    this.v = v;
    checkRep();
  }

  public long getBornTime() {
    return bornTime;
  }

  public long getEndTime() {
    return endTime;
  }



  @Override
  public void run() {

    this.bornTime = 0;
    Ladder ladder = null;
    int position = -1;
    int target = -1;
    ladderStrategy strategy = new FollowStrategy();
    while (true) {
      synchronized (Monkey.class) {
        ladder = strategy.getLadder(MonkeyGenerator.ladders, this);
        if (ladder != null) {
          if (this.getDirection().equals(L2R)) {
            ladder.jumpMonkey(this, 0);
            position = 0;
            target = ladder.getRungs().size() - 1;
          } else if (this.getDirection().equals(R2L)) {
            ladder.jumpMonkey(this, ladder.getRungs().size() - 1);
            position = ladder.getRungs().size() - 1;
            target = 0;
          }
          String info =
              "Monkey " + this.id + " start " + " in Ladder " + ladder.getId() + " rung "
                  + position + " at " + (0)
                  / MonkeyGenerator.oneSecond;
          MonkeyGenerator.logger.info(info);
          break;
        }
      }
      String info =
          "Monkey " + this.id + " wait " + " at " + (this.getDirection().equals(L2R)
              ? "Left"
              : "Right") + " at "
              + (System.currentTimeMillis() - MonkeyGenerator.begin) / MonkeyGenerator.oneSecond;
      MonkeyGenerator.logger.info(info);
      try {
        Thread.sleep(MonkeyGenerator.oneSecond);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    while (position != target) {
      synchronized (Monkey.class) {
        int nextPosition = position;
        if (this.direction.equals(L2R)) {
          for (int i = position + 1; i <= Math.min(position + this.v, target); i++) {
            if (ladder.getRungs().get(i).getMonkey() != null) {
              break;
            }
            nextPosition = i;
          }
        } else {
          for (int i = position - 1; i >= Math.max(position - this.v, 0); i--) {
            if (ladder.getRungs().get(i).getMonkey() != null) {
              break;
            }
            nextPosition = i;
          }
        }
        if (position != nextPosition) {
          ladder.jumpMonkey(this, position, nextPosition);
          position = nextPosition;
        }
        String info =
            "Monkey " + this.id + " run " + " in Ladder " + ladder.getId() + " rung "
                + position + " at "
                + (System.currentTimeMillis() - MonkeyGenerator.begin) / MonkeyGenerator.oneSecond;
        MonkeyGenerator.logger.info(info);
      }
      try {
        Thread.sleep(MonkeyGenerator.oneSecond);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    synchronized (Monkey.class) {
      ladder.downMonkey();
      String info =
          "Monkey " + this.id + " arrive " + " by Ladder " + ladder.getId() + " at "
              + (System.currentTimeMillis() - MonkeyGenerator.begin) / MonkeyGenerator.oneSecond;
      MonkeyGenerator.logger.info(info);
      this.endTime = 0;
    }
  }

  public String getDirection() {
    return direction;
  }
}
