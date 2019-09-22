/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import P1.graph.ConcreteVerticesGraph;
import P1.graph.Graph;

/**
 * Tests for ConcreteVerticesGraph.
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {

    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override
    public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph<String>();
    }

    /*
     * Testing ConcreteVerticesGraph...
     */
    String string1 = "";
    String string2 = "T";
    String string3 = "FETS";
    String string4 = "NO EDEG IN THE GRAPH";
    Vertex<String> vertex1 = new Vertex<String>(string1);
    Vertex<String> vertex2 = new Vertex<String>(string2);
    Vertex<String> vertex3 = new Vertex<String>(string3);
    Vertex<String> vertex4 = new Vertex<String>(string4);
    Graph<String> graph = emptyInstance();

    @Before
    public void setUp() {
        testAdd();
        testSet();
        graph.add(string1);
        graph.add(string2);
        graph.add(string3);
        graph.add(string4);
        graph.set(string2, string1, 1);
        graph.set(string3, string1, 2);
        graph.set(string1, string2, 3);
        graph.set(string1, string3, 4);
    }

    // Testing strategy for ConcreteVerticesGraph.toString()
    // println the string return by toString()
    // test if it can work as we assume
    @Test
    public void testTostring() {
        System.out.println(graph.toString());
        assertFalse("expected not empty",graph.toString().isEmpty());
    }
    /*
     * Testing Vertex...
     */

    // Testing strategy for Vertex
    // Partition the inputs as follows: source/target/vertex: in the graph or not
    // weight:0, >0

    /**
     * covers: begin/end :in the graph or not weight : 0, >0.
     */
    @Test
    public void testUpdateEdges() {
        int weight = 1;
        assertEquals("expected sccessfully add edge", 0, vertex1.updateEdges(vertex4, weight));
        weight = 2;
        assertEquals("expected sccessfully update edge", 1, vertex1.updateEdges(vertex4, weight));
        weight = 0;
        assertEquals("expected sccessfully remove edge", 2, vertex1.updateEdges(vertex4, weight));
        assertEquals("expected unsuccessfully remove edge because it is not in the graph", 0,
                vertex1.updateEdges(vertex4, weight));
    }

    /**
     * print the string return by toString() check vertex.toString()
     */
    @Test
    public void testVertexTostring() {
        assertFalse("expected not empty",vertex1.toString().isEmpty());
    }

    /**
     * test if equals work bottom up test.
     */
    @Test
    public void testEquals() {
        assertFalse("expected not equal",vertex1.equals(vertex2));
        assertTrue("expected equal",vertex1.equals(vertex1));
    }
}
