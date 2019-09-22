package P2;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import P1.graph.Graph;

public class FriendshipGraph {
    
    private final Graph<Person> graph;
    private final Map<Person, Set<Person>> linklist;
    // Abstraction function:
    // AF(graph) = the grpah
    // AF(linkelist) = the edges in the graph for junit test
    // Representation invariant:
    // no repeat person in graph
    // Safety from rep exposure:
    // all fields are private.
    // make defensive copies in vertices
    // to avoid sharing the rep's List object with clients.
    public Set<Person> getVertices() {
        return graph.vertices();
    }
    public Map<Person, Set<Person>> getLinklist() {
        return (new HashMap<Person, Set<Person>>(linklist));
    }
    public FriendshipGraph() {
        graph = Graph.<Person>empty();
        linklist = new HashMap<>();
    }
    private void addVertex(Person person) {
        graph.add(person);
    }
    private void addEdge(Person p1, Person p2) {
        int weight = 1;
        graph.set(p1, p2, weight);
        linklist.put(p1,graph.targets(p1).keySet());
    }                              
    private int getDistance(Person p1, Person p2) {
        if(p1.equals(p2)) {
            return 0;
        }
        Map<Person, Integer> distance = new HashMap<>();
        for (Person tmp : graph.vertices()) {
            distance.put(tmp, -1);
        }
        Queue<Person> queue = new ConcurrentLinkedQueue<>();
        queue.add(p1);
        distance.put(p1, 0);
        while(queue.isEmpty() == false) {
            Person now = queue.poll();
            Map<Person, Integer> map = graph.targets(now);
            int dis = distance.get(now);
            for(Person person : map.keySet()) {
                if(distance.get(person) == -1) {
                    distance.put(person, dis + 1);
                    if(person.equals(p2)) {
                        return dis + 1;
                    }
                    queue.add(person);
                }
            }
        }
        return -1;
    }
    public static void main(String[] args) {
        FriendshipGraph graph = new FriendshipGraph();
        Person rachel = new Person("Rachel");
        Person ross = new Person("Ross");
        Person ben = new Person("Ben");
        Person kramer = new Person("Kramer");
        graph.addVertex(rachel);
        graph.addVertex(ross);
        graph.addVertex(ben);
        graph.addVertex(kramer);
        graph.addEdge(rachel, ross);
        graph.addEdge(ross, rachel);
        graph.addEdge(ross, ben);
        graph.addEdge(ben, ross);
        System.out.println(graph.getDistance(rachel, ross));
        System.out.println(graph.getDistance(rachel, ben));
        System.out.println(graph.getDistance(rachel, rachel));
        System.out.println(graph.getDistance(rachel, kramer));
    }

}
