/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */

package P1.graph;

import P1.graph.Graph;

import static org.junit.Assert.*;

import java.util.Collections;

import java.util.Map;
import java.util.Set;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>
 * PS2 instructions: you MUST NOT add constructors, fields, or non-@Test methods
 * to this class, or change the spec of {@link #emptyInstance()}. Your tests
 * MUST only obtain Graph instances by calling emptyInstance(). Your tests MUST
 * NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {

    /*
     * Testing strategy
     * 
     * Partition the inputs as follows: source/target/vertex.length():0, 1, >1
     * source/target/vertex: in the graph or not weight:0, >0
     * 
     */

    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testInitialVerticesEmpty() {
        assertEquals("expected new graph to have no vertices", Collections.emptySet(), emptyInstance().vertices());
    }

    /**
     * covers: vertex.length():0, 1, >1 vertex:in the graph and not in the graph.
     * test if we can sucessfully add vertex and unsuccessfully add repeat vertex.
     */
    @Test
    public void testAdd() {
        String vertex1 = "";
        String vertex2 = "T";
        String vertex3 = "FETS";
        Graph<String> graph = emptyInstance();
        assertTrue("expected successfully add", graph.add(vertex1));
        assertTrue("expected successfully add", graph.add(vertex2));
        assertTrue("expected successfully add", graph.add(vertex3));
        assertFalse("expected unsuccessfully add", graph.add(vertex1));
    }

    /**
     * covers: source/target.length(): 0, 1, >1. source/target: in the graph and
     * not/. weight: 0, >0 test if we can add,update,remove edge. test if remove
     * edge which is not in the graph, we can get false ret. bottom up unit testing
     * use testAdd.
     */
    @Test
    public void testSet() {
        testAdd();
        String vertex1 = "";
        String vertex2 = "T";
        String vertex3 = "FETS";
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        graph.add(vertex2);
        graph.add(vertex3);
        String vertex4 = "NOT IN THE GRAPH";
        String vertex5 = "ALSO NOT IN THE GRAPH";
        int weight = 0;
        assertEquals("expected not change the graph in weigh = 0," + "vertex4 and vertex5 not in the graph", 0,
                graph.set(vertex4, vertex5, weight));
        assertFalse("expected vertex4 " + "NOT in this graph after set", graph.vertices().contains(vertex4));
        assertFalse("expected vertex5 " + "NOT in this graph after set", graph.vertices().contains(vertex5));
        weight = 1;
        assertEquals("expected sccessfully add edge and points", 0, graph.set(vertex4, vertex5, weight));
        assertTrue("expected vertex4 " + "in this graph after set", graph.vertices().contains(vertex4));
        assertTrue("expected vertex5 " + "in this graph after set", graph.vertices().contains(vertex5));
        assertTrue("expected vertex4 in this graph after set", graph.vertices().contains(vertex4));
        weight = 1;
        assertEquals("expected sccessfully add edge", 0, graph.set(vertex1, vertex4, weight));
        weight = 2;
        assertEquals("expected sccessfully update edge", 1, graph.set(vertex1, vertex4, weight));
        weight = 0;
        assertEquals("expected sccessfully remove edge", 2, graph.set(vertex1, vertex4, weight));
        assertEquals("expected unsuccessfully remove edge because it is not in the graph", 0,
                graph.set(vertex1, vertex4, weight));

    }

    /**
     * covers: vertex.length(): 0, 1, >1 vertex:in the graph and not in the graph.
     * test if we can successfully remove vertex. test if remove vertex and its edge
     * are not in the graph. bottom up unit test by testAdd() and testSet()
     */
    @Test
    public void testRemove() {
        testAdd();
        testSet();
        String vertex1 = "";
        String vertex2 = "T";
        String vertex3 = "FETS";
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        graph.add(vertex2);
        graph.add(vertex3);
        int weight = 1;
        graph.set(vertex1, vertex2, weight);
        graph.set(vertex1, vertex3, weight);
        graph.set(vertex2, vertex3, weight);
        assertTrue("expected sccessfully remove", graph.remove(vertex1));
        assertTrue("expected sccessfully remove", graph.remove(vertex2));
        assertTrue("expected sccessfully remove", graph.remove(vertex3));
        assertFalse("expected unsuccessfully remove", graph.remove(vertex1));
        assertFalse("expected sccessfully remove", graph.vertices().contains(vertex1));
        assertFalse("expected sccessfully remove", graph.vertices().contains(vertex2));
        assertFalse("expected sccessfully remove", graph.vertices().contains(vertex3));
        String vertex4 = "NOT IN THE GRAPH";
        assertFalse("expected unsuccessfully remove", graph.remove(vertex4));
        assertTrue("expected successfully remove edge", graph.targets(vertex1).isEmpty());
        assertTrue("expected successfully remove edge", graph.targets(vertex2).isEmpty());
        assertTrue("expected successfully remove edge", graph.targets(vertex3).isEmpty());

    }

    /**
     * covers: vertex.length():0, 1, >1 vertex:in the graph and not in the graph
     * test if we can get exactly set return by vertices() bottom up unit testing
     * use testAdd.
     */
    @Test
    public void testVertices() {
        testAdd();
        String vertex1 = "";
        String vertex2 = "T";
        String vertex3 = "FETS";
        String vertex4 = "BOOM!";
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        graph.add(vertex2);
        graph.add(vertex3);
        Set<String> vertexs = graph.vertices();
        assertTrue("expected vertex1 in the graph", vertexs.contains(vertex1));
        assertTrue("expected vertex2 in the graph", vertexs.contains(vertex1));
        assertTrue("expected vertex3 in the graph", vertexs.contains(vertex1));
        vertexs.add(vertex4);
        assertFalse("expected vertex4 NOT in the graph" + "for safety from rep exposure",
                graph.vertices().contains(vertex4));
    }

    /**
     * covers: target.length():0, 1, >1 target:in the graph and not in the graph
     * test if we can get exactly set return by sources() test if we can get exactly
     * weigh return by sources() bottom up unit testing use testAdd() and testSet().
     */
    @Test
    public void testSources() {
        testAdd();
        testSet();
        String vertex1 = "";
        String vertex2 = "T";
        String vertex3 = "FETS";
        String vertex4 = "NOT IN THE GRAPH";
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        graph.add(vertex2);
        graph.add(vertex3);
        int weight1 = 1;
        graph.set(vertex2, vertex1, weight1);
        int weight2 = 2;
        graph.set(vertex3, vertex1, weight2);
        assertTrue("expected empty return", graph.sources(vertex4).isEmpty());
        Map<String, Integer> sources = graph.sources(vertex1);
        assertEquals("expected vertex2 in the sources with the adj edge value", weight1,
                sources.get(vertex2).intValue());
        assertEquals("expected vertex2 in the sources with the adj edge value", weight2,
                sources.get(vertex3).intValue());
        assertFalse("expected target not in the sources", sources.containsKey(vertex1));
        assertFalse("expected vertex4 not in the sources", sources.containsKey(vertex4));
    }

    /**
     * covers: target.length():0, 1, >1 target:in the graph and not in the graph
     * test if we can get exactly set return by sources() test if we can get exactly
     * weigh return by sources() bottom up unit testing use testAdd and testSet.
     */
    @Test
    public void testTargets() {
        testAdd();
        testSet();
        String vertex1 = "";
        String vertex2 = "T";
        String vertex3 = "FETS";
        String vertex4 = "NOT IN THE GRAPH";
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        graph.add(vertex2);
        graph.add(vertex3);
        int weight1 = 1;
        graph.set(vertex1, vertex2, weight1);
        int weight2 = 2;
        graph.set(vertex1, vertex3, weight2);
        assertTrue("expected empty return", graph.sources(vertex4).isEmpty());
        Map<String, Integer> targets = graph.targets(vertex1);
        assertEquals("expected vertex2 in the sources with the adj edge value", weight1,
                targets.get(vertex2).intValue());
        assertEquals("expected vertex2 in the sources with the adj edge value", weight2,
                targets.get(vertex3).intValue());
        assertFalse("expected target not in the sources", targets.containsKey(vertex1));
        assertFalse("expected vertex4 not in the sources", targets.containsKey(vertex4));
    }
}
