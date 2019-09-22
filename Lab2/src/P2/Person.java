package P2;

public class Person {
    private final String name;
    // Abstraction function:
    // AF(name) = the person's name
    // Representation invariant:
    // no repeat person in graph
    // Safety from rep exposure:
    // all fields are private.
    public String getName() { 
        return this.name;
    }

    public Person(String name) {
        this.name = name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            return (((Person) (obj)).getName().equals(name)) ;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
