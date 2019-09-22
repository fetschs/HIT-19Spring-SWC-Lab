package P2;

import static org.junit.Assert.*;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class FriendshipGraphTest {

    Person rachel = new Person("Rachel");
    Person ross = new Person("Ross");
    Person ben = new Person("Ben");
    Person kramer = new Person("Kramer");
    FriendshipGraph graph;

    @Before
    public void setUp() throws Exception {

        graph = new FriendshipGraph();
        Method testAddVertex = graph.getClass().getDeclaredMethod("addVertex", Person.class);
        testAddVertex.setAccessible(true);
        testAddVertex.invoke(graph, rachel);
        testAddVertex.invoke(graph, ross);
        testAddVertex.invoke(graph, ben);
        Method testAddEdge = graph.getClass()
                .getDeclaredMethod("addEdge", Person.class, Person.class);
        testAddEdge.setAccessible(true);
        testAddEdge.invoke(graph, rachel, ross);
        testAddEdge.invoke(graph, ross, rachel);
        testAddEdge.invoke(graph, ross, ben);
        testAddEdge.invoke(graph, ben, ross);
    }

    @Test
    public void testAddVertex() {
        Set<Person> vertices = graph.getVertices();
        assertTrue(vertices.size() == 3);
        assertTrue(vertices.contains(rachel));
        assertTrue(vertices.contains(ross));
        assertTrue(vertices.contains(ben));
        assertFalse(vertices.contains(kramer));
    }

    @Test
    public void testAddEdge() {
        Map<Person, Set<Person>> map = graph.getLinklist();
        assertTrue(map.get(rachel).contains(ross));
        assertTrue(map.get(ross).contains(rachel));
        assertTrue(map.get(ross).contains(ben));
        assertTrue(map.get(ben).contains(ross));
    }

    @Test
    public void testGetDistance() throws Exception {
        Method testgetDistance = graph.getClass().getDeclaredMethod("getDistance",
                Person.class, Person.class);
        testgetDistance.setAccessible(true);
        assertEquals(-1, (testgetDistance.invoke(graph, rachel, kramer)));
        assertEquals(0, (testgetDistance.invoke(graph, rachel, rachel)));
        assertEquals(2, (testgetDistance.invoke(graph, rachel, ben)));
        assertEquals(1, (testgetDistance.invoke(graph, rachel, ross)));

    }

}
