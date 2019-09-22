/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.poet;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {

    // Testing strategy
    // the input of poem() should test lower/upper case
    // and should test insert or not insert
    // test read the file if work
    // test if we can get a poem
    // use print
    File poem = new File("src/P1/poet/poem.txt");

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    /**
     * just test if we can read
     */
    public void testGraphPoet() {
        try {
            GraphPoet graphpoet = new GraphPoet(poem);
            assertFalse("expected not null", graphpoet.toString().isEmpty());
        } catch (Exception e) {
        }
    }
    /**
     * test if only lower case word in wordlist
     */
    @Test
    public void testhandleLine() {
        String line = "^how %many $roads";
        List<String> words = GraphPoet.handleLine(line);
        assertFalse("expected not empty", words.isEmpty());
        assertEquals("expected size", 3, words.size());
        for (String word : words) {
            char[] chars = word.toCharArray();
            for (Character character : chars) {
                assertTrue("expected only lower case word", Character.isLowerCase(character));
            }
        }
    }

    @Test
    /**
     * just test if we can get the poem as we think, then print it.
     */
    public void testPoem() {
        GraphPoet graphpoet;
        try {
            graphpoet = new GraphPoet(poem);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        String input = "how many roads must a man walk down before they call him a man";
        System.out.println(graphpoet.poem(input));
        assertEquals("expected insert you",
                "how many roads must you a man walk down " + "before they call for him a man .", graphpoet.poem(input));
        input = "MUST A MAN";
        assertEquals("expected insert you", "MUST you A MAN .", graphpoet.poem(input));
        input = "HOW MANY";
        assertEquals("expect no insert", "HOW MANY .", graphpoet.poem(input));
        input = "how many";
        assertEquals("expect no insert", "how many .", graphpoet.poem(input));
    }

    @Test
    /**
     * just test toString() bottom up with testGraphPoet()
     */
    public void testToString() {
        testGraphPoet();
        try {
            GraphPoet graphpoet = new GraphPoet(poem);
            assertFalse("expected not empty", graphpoet.toString().isEmpty());
        } catch (Exception e) {
        }
    }
}
