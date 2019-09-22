package P3;

import static org.junit.Assert.*;
import java.lang.reflect.Method;
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
        assertTrue(graph.getNumofVertexs() == 3);
        assertTrue(graph.getVertexList().contains(rachel));
        assertTrue(graph.getVertexList().contains(ross));
        assertTrue(graph.getVertexList().contains(ben));
        assertFalse(graph.getVertexList().contains(kramer));
    }

    @Test
    public void testAddEdge() {
        assertTrue(graph.getNumofEdges() == 4);
        assertTrue(graph.getLinkList().get(ross) != null);
        assertTrue(graph.getLinkList().get(rachel) != null);
        assertTrue(graph.getLinkList().get(ben) != null);
        assertFalse(graph.getLinkList().get(kramer) != null);
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
