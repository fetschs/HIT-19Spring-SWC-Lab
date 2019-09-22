/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */

package P1.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph<L> implements Graph<L> {

    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();

    // Abstraction function:
    // AF(vertices) = the vertices of this graph
    // AF(edges) = the edges of this graph
    // Representation invariant:
    // a edge in edges must both the source and target in the vertices
    // Safety from rep exposure:
    // all fields are private, but List and set are mutable,
    // make defensive copies in vertices
    // to avoid sharing the rep's List object with clients.

    public ConcreteEdgesGraph() {
    }

    private void checkRep() {
        for (Edge<L> edge : edges) {
            assert vertices.contains(edge.getSource());
            assert vertices.contains(edge.getTarget());
        }
    }

    @Override
    public boolean add(L vertex) {
        if (vertices.contains(vertex)) {
            return false;
        }
        vertices.add(vertex);
        checkRep();
        return true;
    }

    @Override
    public int set(L source, L target, int weight) {
        if (weight < 0) {
            throw new Error("Valid param!");
        }
        Edge<L> edge = new Edge<L>(source, target, weight);
        int ret = 0;
        if (edges.contains(edge)) {
            Edge<L> edgeinlist = edges.get(edges.indexOf(edge));
            ret = edgeinlist.getWeight();
            if (edgeinlist.getWeight() != weight) {
                edges.remove(edgeinlist);
                edges.add(edge);
            }
        } else {
            if (weight == 0) {
                return ret;
            } else {
                if (vertices.contains(source) == false) {
                    vertices.add(source);
                }
                if (vertices.contains(target) == false) {
                    vertices.add(target);
                }
                edges.add(edge);
            }
        }
        checkRep();
        return ret;
    }

    @Override
    public boolean remove(L vertex) {
        if (vertices.contains(vertex) == false) {
            return false;
        } else {
            vertices.remove(vertex);
            List<Edge<L>> temp = new ArrayList<Edge<L>>(edges);
            for (Edge<L> edge : temp) {
                if ((edge.getSource().equals(vertex)) || (edge.getTarget().equals(vertex))) {
                    edges.remove(edge);
                }
            }
            checkRep();
            return true;
        }
    }

    @Override
    public Set<L> vertices() {
        return (new HashSet<L>(vertices));
    }

    @Override
    public Map<L, Integer> sources(L target) {
        Map<L, Integer> ret = new HashMap<>();
        for (Edge<L> edge : edges) {
            if (edge.getTarget().equals(target) == true) {
                ret.put(edge.getSource(), edge.getWeight());
            }
        }
        return ret;
    }

    @Override
    public Map<L, Integer> targets(L source) {
        Map<L, Integer> ret = new HashMap<>();
        for (Edge<L> edge : edges) {
            if (edge.getSource().equals(source) == true) {
                ret.put(edge.getTarget(), edge.getWeight());
            }
        }
        return ret;
    }

    @Override
    public String toString() {
        StringBuffer ret = new StringBuffer("Contains Vertices:");
        for (L vertex : vertices) {
            ret.append(" \"" + vertex.toString() + "\"");
        }
        ret.append("\n");
        for (Edge<L> edge : edges) {
            ret.append(edge.toString() + "\n");
        }
        return ret.toString();
    }
}

/**
 * Immutable. This class is internal to the rep of ConcreteEdgesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is up to
 * you.
 */
class Edge<L> {

    private final L source;
    private final L target;
    private final int weight;
    // Abstraction function:
    // AF(source) = the source vertex of this edge
    // AF(target) = the target vertex of this edge
    // AF(weight) = the weight of this edge
    // Representation invariant:
    // weight must >=0 (0 means this edge wouldn't in the graph)
    // Safety from rep exposure:
    // all fields are private.
    
    /**
     * construction method.
     * 
     * @param begin the source vertex
     * @param end   the target vertex
     * @param value the weight of edge
     */
    public Edge(L begin, L end, int value) {
        source = begin;
        target = end;
        weight = value;
        checkRep();
    }

    /**
     * check the rep invariant
     */
    private void checkRep() {
        assert (weight >= 0);
    }

    public L getSource() {
        return source;
    }

    public L getTarget() {
        return target;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Edge:" + " From:\"" + source.toString() 
        + "\" To:\"" + target.toString() + "\" Weight:" + weight;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Edge) {
            return (((Edge<?>) (obj)).getSource().equals(source)) 
                    && (((Edge<?>) (obj)).getTarget().equals(target));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (source.toString() 
                + target.toString()).hashCode();
    }
}
