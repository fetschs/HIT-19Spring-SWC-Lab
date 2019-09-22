/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.poet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import P1.graph.Graph;

/**
 * A graph-based poetry generator.
 * 
 * <p>
 * GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph. Vertices in the graph are words. Words are defined as
 * non-empty case-insensitive strings of non-space non-newline characters. They
 * are delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>
 * For example, given this corpus:
 * 
 * <pre>
 *     Hello, HELLO, hello, goodbye!
 * </pre>
 * <p>
 * the graph would contain two edges:
 * <ul>
 * <li>("hello,") -> ("hello,") with weight 2
 * <li>("hello,") -> ("goodbye!") with weight 1
 * </ul>
 * <p>
 * where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>
 * Given an input string, GraphPoet generates a poem by attempting to insert a
 * bridge word between every adjacent pair of words in the input. The bridge
 * word between input words "w1" and "w2" will be some "b" such that w1 -> b ->
 * w2 is a two-edge-long path with maximum-weight weight among all the
 * two-edge-long paths from w1 to w2 in the affinity graph. If there are no such
 * paths, no bridge word is inserted. In the output poem, input words retain
 * their original case, while bridge words are lower case. The whitespace
 * between every word in the poem is a single space.
 * 
 * <p>
 * For example, given this corpus:
 * 
 * <pre>
 *     This is a test of the Mugar Omni Theater sound system.
 * </pre>
 * <p>
 * on this input:
 * 
 * <pre>
 *     Test the system.
 * </pre>
 * <p>
 * the output poem would be:
 * 
 * <pre>
 *     Test of the system.
 * </pre>
 * 
 * <p>
 * PS2 instructions: this is a required ADT class, and you MUST NOT weaken the
 * required specifications. However, you MAY strengthen the specifications and
 * you MAY add additional methods. You MUST use Graph in your rep, but otherwise
 * the implementation of this class is up to you.
 */
public class GraphPoet {

    private final Graph<String> graph = Graph.empty();

    // Abstraction function:
    // AF(graph) = the poet graph as above,
    // the vertex is the word,and the edge means they are adjacent.
    // Representation invariant:
    // all the vertex should be lower-case.
    // Safety from rep exposure:
    // all fields are private, we also won't graph directly
    /**
     * handle sentence in a line.
     * 
     * @param line
     * @return words list after handle to lower case word
     */
    public static List<String> handleLine(String line) {
        String[] words = line.split(" |,|!|-|_|\\.|\\?|\\'");
        String regex = "([A-Za-z]+)";
        Pattern pattern = Pattern.compile(regex);
        List<String> newwords = new ArrayList<>();
        for (int index = 0; index < words.length; index++) {
            Matcher matcher = pattern.matcher(words[index]);
            if (matcher.find()) {
                if (matcher.group(1).isEmpty() == false) {
                    newwords.add(matcher.group(1).toLowerCase());
                }
            }
        }
        return newwords;
    }

    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        Scanner scanner = new Scanner(corpus);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            List<String> newwords = handleLine(line);
            for (int index = 1; index < newwords.size(); index++) {
                int weigh = graph.set(newwords.get(index - 1), newwords.get(index), 0);
                graph.set(newwords.get(index - 1), newwords.get(index), weigh + 1);
            }
        }
        scanner.close();
        checkRep();
    }


    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above) '.' in the end
     */
    public String poem(String input) {
        List<String> newwords = handleLine(input);
        String[] words = input.split(" |,|!|-|_|\\.|\\?|\\'");
        List<String> retwords = new LinkedList<>(Arrays.asList(words));
        for (int index = 1; index < newwords.size(); index++) {
            Map<String, Integer> targets = graph.targets(newwords.get(index - 1));
            Map<String, Integer> sources = graph.sources(newwords.get(index));
            Set<String> begins = targets.keySet();
            Set<String> ends = sources.keySet();
            Set<String> bridges = new HashSet<>();
            bridges.addAll(begins);
            bridges.retainAll(ends);
            int weight = -1;
            String insert = "";
            for (String bridge : bridges) {
                int temp = targets.get(bridge) + sources.get(bridge);
                if (weight < temp) {
                    weight = temp;
                    insert = bridge;
                }
            }
            if (bridges.isEmpty() == false)
                retwords.add(index, insert);
        }
        StringBuffer ret = new StringBuffer();
        for (String string : retwords) {
            ret.append(string + " ");
        }
        ret.append(".");
        return ret.toString();
    }
    /**
     * check the Representation invariant,
     * if we just have lowercase.
     */
    private void checkRep() {
        Set<String> vertices = graph.vertices();
        for (String vertex : vertices) {
            char[] chars = vertex.toCharArray();
            for (char ch : chars) {
                assert (Character.isLowerCase(ch));
            }
        }
    }

    @Override
    public String toString() {
        StringBuffer ret = new StringBuffer("Graph:\n");
        ret.append(graph.toString());
        return ret.toString();
    }
}
