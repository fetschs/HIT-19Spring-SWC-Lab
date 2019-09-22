package strategy;

import entity.Ladder;
import entity.Monkey;
import java.util.List;

public class LazyStrategy extends ladderStrategy {

  public LazyStrategy() {
  }

  @Override
  public Ladder getLadder(List<Ladder> ladders, Monkey monkey) {
    for (Ladder ladder : ladders) {
        if (ladder.getMonkeyNumber() == 0) {
          return ladder;
        }
    }
    return null;
  }
}
