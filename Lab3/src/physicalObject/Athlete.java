package physicalObject;

import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class Athlete implements PhysicalObject {

  private final String name;
  private final int number;
  private final String nationality;
  private final int age;
  private final BigDecimal bestScore;

  // Abstraction function:
  // AF(name) = the name of this athlete
  // AF(number) = the number of this athlete
  // AF(nationality) = the nationality of this athlete
  // AF(age) = the age of this athlete
  // AF(bestScore) = the bestScore of this athlete
  // Representation invariant:
  // the len of nationality should be 3;
  // the format of bestScore should be A.BC or AA.BC(A,B,C is digit)
  // Safety from rep exposure:
  // all fields are final and immutable.

  Athlete(String name, int number, String nationality, int age, BigDecimal bestScore) {
    this.name = name;
    this.number = number;
    this.nationality = nationality;
    this.age = age;
    this.bestScore = bestScore;
    checkRep();
  }


  private void checkRep() {
    assert nationality.length() == 3;
    assert (int) bestScore.doubleValue() <= 99;
    assert bestScore.scale() == 2;
  }
}
