/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */

package P1.graph;

import static org.junit.Assert.*;

import P1.graph.ConcreteEdgesGraph;
import P1.graph.Graph;

import org.junit.Before;
import org.junit.Test;



/**
 * Tests for ConcreteEdgesGraph. 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph<String>();
    }
    
    String string1 = "FW";
    String string2 = "T";
    String string3 = "FETS";
    String string4 = "NO EDEG IN THE GRAPH";
    Edge<String> edge = new Edge<String>(string2, string1, 1);
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
    /*
     * Testing ConcreteEdgesGraph...
     */
    
    // Testing strategy for ConcreteEdgesGraph.toString()
    // println the string return by toString()
    // test if it can work as we assume
    @Test
    public void testTostring() {
        System.out.println(graph.toString());
        assertFalse("expected not empty",graph.toString().isEmpty());
    }
    

    
    /*
     * Testing Edge...
     */
    
    // Testing strategy for Edge
    // just test toString()    
    @Test
    public void testEdgetoString() {
        assertFalse("expected not empty",edge.toString().isEmpty());
    }
}
