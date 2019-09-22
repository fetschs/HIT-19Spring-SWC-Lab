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
public class ConcreteVerticesGraph<L> implements Graph<L> {

    private final List<Vertex<L>> vertices = new ArrayList<>();

    // Abstraction function:
    // AF(vertices) = {vertices[i] in the graph | 0 <= i < vertices.size()}
    // Representation invariant:
    // vertices must has unique Vertex<L>
    // Safety from rep exposure:
    // all fields are private
    // vertices is a mutable List, but we don't use it in dangerous situation.
    /**
     * construct method.
     */
    public ConcreteVerticesGraph() {
    }

    /**
     * check if the RI change.
     */
    private void checkRep() {
        Set<Vertex<L>> setofvertices = new HashSet<>();
        setofvertices.addAll(vertices);
        assert setofvertices.size() == vertices.size();
    }

    @Override
    public boolean add(L vertex) {
        Vertex<L> temp = new Vertex<L>(vertex);
        if (vertices.contains(temp) == true) {
            return false;
        }
        vertices.add(temp);
        checkRep();
        return true;
    }

    @Override
    public int set(L source, L target, int weight) {
        if (weight < 0) {
            throw new Error("Valid param!");
        }
        Vertex<L> begin = new Vertex<L>(source);
        Vertex<L> end = new Vertex<L>(target);
        if (weight == 0) {
            if (vertices.contains(begin) == false || vertices.contains(end) == false) {
                return 0;
            }
        }
        if (vertices.contains(begin) == false) {
            vertices.add(begin);
        }
        if (vertices.contains(end) == false) {
            vertices.add(end);
        }
        Vertex<L> begininlist = vertices.get(vertices.indexOf(begin));
        Vertex<L> endinlist = vertices.get(vertices.indexOf(end));
        return begininlist.updateEdges(endinlist, weight);
    }

    @Override
    public boolean remove(L vertex) {
        Vertex<L> temp = new Vertex<L>(vertex);
        if (vertices.contains(temp) == false) {
            return false;
        }
        vertices.remove(temp);
        checkRep();
        return true;
    }

    @Override
    public Set<L> vertices() {
        Set<L> ret = new HashSet<>();
        for (Vertex<L> vertex : vertices) {
            ret.add(vertex.getValue());
        }
        return ret;
    }

    @Override
    public Map<L, Integer> sources(L target) {
        Vertex<L> temp = new Vertex<L>(target);
        Map<L, Integer> ret = new HashMap<>();
        if(vertices.contains(temp) == false) {
            return ret;
        }
        int index = vertices.indexOf(temp);
        Map<Vertex<L>, Integer> map = vertices.get(index).gettoEdges();
        for (Vertex<L> key : map.keySet()) {
            ret.put(key.getValue(), map.get(key));
        }
        return ret;
    }

    @Override
    public Map<L, Integer> targets(L source) {
        Vertex<L> temp = new Vertex<L>(source);
        Map<L, Integer> ret = new HashMap<>();
        if(vertices.contains(temp) == false) {
            return ret;
        }
        int index = vertices.indexOf(temp);
        Map<Vertex<L>, Integer> map = vertices.get(index).getfromEdges();
        for (Vertex<L> key : map.keySet()) {
            ret.put(key.getValue(), map.get(key));
        }
        return ret;
    }

    @Override
    public String toString() {
        StringBuffer retString = new StringBuffer("Contains Vertices: ");
        for (Vertex<L> vertex : vertices) {
            retString.append("\"" + vertex.getValue() + "\" ");
        }
        retString.append("\n");
        for (Vertex<L> vertex : vertices) {
            retString.append(vertex.toString());
        }
        return retString.toString();
    }

}

/**
 * Mutable. This class is internal to the rep of ConcreteVerticesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is up to
 * you.
 */
class Vertex<L> {

    private final L value;
    private final Map<Vertex<L>, Integer> fromEdges;
    private final Map<Vertex<L>, Integer> toEdges;

    // Abstraction function:
    // AF(L) = the value of this Vertex<L>
    // AF(fromEdges) = the edges begin at this Vertex<L>
    // key means the end Vertex<L>, value means the weight of edge
    // AF(toEdges) = the edges end at this Vertex<L>
    // key means the begin Vertex<L>, value means the weight of edge
    // Representation invariant:
    // from/toEdges values should be >0
    // if vertex1 fromEdges has a edge from vertex1 to vertex2(as the key),
    // vertex2 toEdges MUST has a edge from vertex1(as the key) to vertex2 with the
    // same weight.
    // Safety from rep exposure:
    // mutable class
    /**
     * construct method.
     * 
     * @param L the value of this Vertex<L>
     */
    public Vertex (L vertex) {
        this.value = vertex;
        this.fromEdges = new HashMap<>();
        this.toEdges = new HashMap<>();
        checkRep();
    }


    /**
     * update the edges of this Vertex<L>, like the graph.set(), just change the param
     * type.
     * 
     * @param end    target Vertex<L>
     * @param weight nonnegative weight of the edge
     * @return the previous weight of the edge, or zero if there was no such edge.
     */
    
    public int updateEdges(Vertex<L> end, int weight) {
        if (weight < 0) {
            throw new Error("Valid param!");
        }
        int ret;
        if (this.fromEdges.containsKey(end) == false) {
            ret = 0;
        } else {
            ret = this.fromEdges.get(end);
        }
        if (weight == 0) {
            if (this.fromEdges.containsKey(end)) {
                this.fromEdges.remove(end);
                end.gettoEdges().remove(this);
            }
            checkRep();
            return ret;
        }
        this.fromEdges.put(end, weight);
        end.toEdges.put(this, weight);
        checkRep();
        return ret;
    }

    /**
     * check if the RI change.
     */
    private void checkRep() {
        for (Integer weight : this.fromEdges.values()) {
            assert weight > 0;
        }
        for (Integer weight : this.toEdges.values()) {
            assert weight > 0;
        }
        for (Vertex<L> key : this.fromEdges.keySet()) {
            assert key.gettoEdges().get(this).equals(this.fromEdges.get(key));
        }
        assert this.value != null;
    }

    /**
     * get the value of this Vertex<L>.
     * 
     * @return the value of this Vertex<L>
     */
    public L getValue() {
        return this.value;
    }

    /**
     * get the fromEdges of this Vertex<L>.
     * 
     * @return the fromEdges of this Vertex<L>
     */
    public Map<Vertex<L>, Integer> getfromEdges() {
        return new HashMap<Vertex<L>,Integer>(this.fromEdges);
    }

    /**
     * get the toEdges of this Vertex<L>.
     * 
     * @retutn the toEdges of this Vertex<L>
     */
    public Map<Vertex<L>, Integer> gettoEdges() {
        return this.toEdges;
    }

    @Override
    public String toString() {
        StringBuffer retstring = new StringBuffer();
        retstring.append("vertex \"" + this.value + "\":\n");
        if (this.fromEdges.isEmpty()) {
            retstring.append("No Edges begin at this vertex!\n");
        } else {
            retstring.append("Edges begin at this vertex:\n");
        }
        for (Vertex<L> key : this.fromEdges.keySet()) {
            L thatvertex = key.getValue();
            String edgeweigh = this.fromEdges.get(key).toString();
            retstring.append("From \"" + this.value 
                    + "\" To \"" + thatvertex + "\" Weight: " + edgeweigh + '\n');
        }
        if (this.fromEdges.isEmpty()) {
            retstring.append("No Edges end at this vertex!\n");
        } else {
            retstring.append("Edges end at this vertex:\n");
        }
        for (Vertex<L> key : this.toEdges.keySet()) {
            L thatvertex = key.getValue();
            String edgeweigh = this.toEdges.get(key).toString();
            retstring.append("From \"" + thatvertex 
                    + "\" To \"" + this.value + "\" Weight: " + edgeweigh + '\n');
        }
        return retstring.toString();
    }

    @Override
    /**
     * check if this.vlaue is the same as the obj.
     * 
     * @param obj the obj will be compare
     */
    public boolean equals(Object obj) {
        if (obj instanceof Vertex<?>) {
            return ((Vertex<?>) (obj)).getValue().equals(this.value);
        }
        return false;
    }

    @Override
    /**
     * make the hashcode fit the equals()
     */
    public int hashCode() {
        return this.value.hashCode();
    }
}
