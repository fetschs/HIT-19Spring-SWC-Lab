package physicalObject;

import circularOrbit.TrackGame;
import java.math.BigDecimal;
import java.util.regex.Matcher;

public class AthleteFactory implements PhysicalObjectFactory {

  @Override
  public PhysicalObject createPhysicalObject(String fileLine) {
    Matcher matcher = TrackGame.athletePattern.matcher(fileLine);
    String name = "";
    String nation = "";
    int number = 0;
    int age = 0;
    BigDecimal best = BigDecimal.ZERO;
    if (matcher.find()) {
      name = matcher.group(1);
      number = Integer.parseInt(matcher.group(2));
      nation = matcher.group(3);
      age = Integer.parseInt(matcher.group(4));
      best = new BigDecimal(matcher.group(5));
    } else {
      assert false;
    }
    return new Athlete(name, number, nation, age, best);
  }
}