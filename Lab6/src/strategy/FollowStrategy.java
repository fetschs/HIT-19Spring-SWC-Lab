package strategy;

import entity.Ladder;
import entity.Monkey;
import java.util.List;

public class FollowStrategy extends ladderStrategy {

  public FollowStrategy() {
  }

  @Override
  public Ladder getLadder(List<Ladder> ladders, Monkey monkey) {
    int minn = Integer.MAX_VALUE;
    Ladder ret = null;
    for (Ladder ladder : ladders) {
      int monkeyNumber = ladder.getMonkeyNumber();
      if (monkeyNumber < minn) {
        if (ladder.getDirection().equals(monkey.getDirection()) || ladder.getDirection()
            .equals("Empty")) {
          minn = monkeyNumber;
          ret = ladder;
        }
      }
    }
    return ret;
  }
}
