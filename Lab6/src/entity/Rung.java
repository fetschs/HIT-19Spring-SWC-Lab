package entity;

class Rung {

  private Monkey monkey;

  // a rung save a monkey
  // Ladder will access Rung
  // null means no, otherwise has a monkey
  Rung() {
    monkey = null;
  }

  Monkey getMonkey() {
    return this.monkey;
  }

  void setMonkey(Monkey monkey) {
    this.monkey = monkey;
  }

}
