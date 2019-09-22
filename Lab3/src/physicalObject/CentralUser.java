package physicalObject;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class CentralUser implements PhysicalObject {

  private final String name;
  private final int age;
  private final String sex;
  // Abstraction function:
  // AF(name) = the name of this centralUser
  // AF(age) = the age of this centralUser
  // AF(sex) = the bestScore of this centralUser
  // Representation invariant:
  // the sex of this centralUser
  // Safety from rep exposure:
  // all fields are final and immutable.

  CentralUser(String name, int age, String sex) {
    this.name = name;
    this.age = age;
    this.sex = sex;
    checkRep();
  }

  private void checkRep() {
    assert this.getSex().equals("M") || this.getSex().equals("F");
  }
}
