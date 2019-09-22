package strategy;

import entity.Ladder;
import entity.Monkey;
import java.util.List;
import java.util.Random;

public abstract class ladderStrategy {

  /**
   * get the ladder the monkey will use.
   *
   * @param monkey the monkey
   * @param ladders the ladder
   * @return the ladder will use. if ladder is null, means the monkey should wait.
   */
  public abstract Ladder getLadder(List<Ladder> ladders, Monkey monkey);

  public static ladderStrategy getRandomStrategy() {
    Random random = new Random();
    int number = random.nextInt(2);
    if (number == 0) {
      return new LazyStrategy();
    } else {
      return new FollowStrategy();
    }
  }
}