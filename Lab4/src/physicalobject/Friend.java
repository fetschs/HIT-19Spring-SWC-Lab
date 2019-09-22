package physicalobject;

import java.util.Objects;
import lombok.Getter;

@Getter
public class Friend implements PhysicalObject {

  private final String name;
  private final int age;
  private final String sex;

  // Abstraction function:
  // AF(name) = the name of this friend
  // AF(age) = the age of this friend
  // AF(sex) = the bestScore of this friend
  // Representation invariant:
  // the sex of this friend
  // Safety from rep exposure:
  // all fields are final and immutable.

  Friend(String name, int age, String sex) {
    this.name = name;
    this.age = age;
    this.sex = sex;
    checkRep();
  }

  private void checkRep() {
    assert this.getSex().equals("M") || this.getSex().equals("F");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Friend friend = (Friend) o;
    return getName().equals(friend.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getAge(), getSex());
  }
}
