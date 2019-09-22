package P3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import java.util.Queue;

public class FriendshipGraph {

    private HashSet<String> nameSet;
    // set of person's name in this graph,used to avoid repeat name
    private ArrayList<Person> vertexList;// list of Vertex
    private ArrayList<Edge> edgeList;// list of Edge
    private Integer numofVertexs;// the number of Vertex
    private Integer numofEdges;// the number of Edge
    private HashMap<Person, Edge> linkList;// adjacency list for save graph

    public HashSet<String> getNameSet() {
        return nameSet;
    }

    public void setNameSet(HashSet<String> nameSet) {
        this.nameSet = nameSet;
    }

    public ArrayList<Person> getVertexList() {
        return vertexList;
    }

    public void setVertexList(ArrayList<Person> vertexList) {
        this.vertexList = vertexList;
    }

    public ArrayList<Edge> getEdgeList() {
        return edgeList;
    }

    public void setEdgeList(ArrayList<Edge> edgeList) {
        this.edgeList = edgeList;
    }

    public Integer getNumofVertexs() {
        return numofVertexs;
    }

    public void setNumofVertexs(Integer numofVertexs) {
        this.numofVertexs = numofVertexs;
    }

    public Integer getNumofEdges() {
        return numofEdges;
    }

    public void setNumofEdges(Integer numofEdges) {
        this.numofEdges = numofEdges;
    }

    public HashMap<Person, Edge> getLinkList() {
        return linkList;
    }

    public void setLinkList(HashMap<Person, Edge> linkList) {
        this.linkList = linkList;
    }

    /**
     * Inner class , describe the edge.
     * 
     * @author FETS
     * 
     */

    private class Edge {
        private Person start;// the start point of this edge
        private Person end;// the end point of this edge
        private Edge next;// next edge adjacent to this edge

        /**
         * constructed function for Edge.
         * 
         * @param start mentioned above
         * @param end   mentioned above
         */
        public Edge(Person start, Person end) {
            this.start = start;
            this.end = end;
            this.next = FriendshipGraph.this.linkList.get(start);
            FriendshipGraph.this.linkList.put(start, this);// update linklist
        }
    }

    /**
     * constructed function for Graph, init this attributes.
     */
    public FriendshipGraph() {
        this.numofVertexs = 0;
        this.numofEdges = 0;
        this.nameSet = new HashSet<String>();
        this.vertexList = new ArrayList<>();
        this.linkList = new HashMap<>();
        this.edgeList = new ArrayList<>();
    }

    /**
     * add a vertex to this graph, i.e. add a person to this social net
     * 
     * @param newperson the vertex will be added
     */
    private void addVertex(Person newperson) {
        String newpersonName = newperson.getName();
        if (this.nameSet.contains(newpersonName)) {
            throw new Error("ERROR : Repeat Name!");
        } // avoid repeat name, warning and exit
        this.nameSet.add(newperson.getName());
        this.vertexList.add(newperson);
        this.numofVertexs++;
    }

    /**
     * add a edge to this graph, i.e. add a connection to this social net
     * 
     * @param p1 one person will be added
     * @param p2 another person
     */
    private void addEdge(Person p1, Person p2) {
        if (this.nameSet.contains(p1.getName()) == false 
                || this.nameSet.contains(p2.getName()) == false) {
            throw new Error("ERROR: A point is not in this graph");
        }
        this.numofEdges++;
        Edge edge = new Edge(p1, p2);
        edgeList.add(edge);
    }

    /**
     * calculate the distance between person p1 and person p2 in this graph.
     * 
     * @param p1 one person
     * @param p2 another person
     * @return the distance between person p1 and person p2 in this graph, if can't
     *         reach p2 ,return -1
     */
    private int getDistance(Person p1, Person p2) {
        if (p1.equals(p2)) {
            return 0;
        }
        HashMap<Person, Integer> distanceMap = new HashMap<>();
        // Use a HashMap to save p1 to other persons distance
        for (Person tmp : vertexList) {
            distanceMap.put(tmp, -1);
        }
        // At the first, distance is -1, means not reach there
        Queue<Person> queue = new LinkedList<>();
        // Use a queue to save vertex for BFS algorithm
        queue.add(p1);
        distanceMap.put(p1, 0);
        // the distance of p1 will be 0, means start from here
        while (queue.isEmpty() == false) {
            Person nowPerson = queue.poll();// get a vertex from queue's front
            for (Edge nowEdge = linkList.get(nowPerson); nowEdge != null; nowEdge = nowEdge.next) {
                Person end = nowEdge.end;
                Person start = nowEdge.start;
                if (distanceMap.get(end) == -1) {
                    distanceMap.put(end, distanceMap.get(start) + 1);// update distance
                    if (end.equals(p2)) {
                        return distanceMap.get(p2);// success
                    }
                    queue.add(end);// update queue
                }
            }
        } // the process of BFS algorithm
        return -1;// if not found p2 ,return -1
    }

    /**
     * client code.
     * 
     * @param args useless
     */

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
