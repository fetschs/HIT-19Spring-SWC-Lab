/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P4.twitter;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import P4.twitter.SocialNetwork;

public class SocialNetworkTest {

    public static List<Tweet> tweets = null;
    public static final URL SAMPLE_SERVER = makeURLAssertWellFormatted(
            "https://raw.githubusercontent.com/rainywang/Spring2019_HITCS_SC_Lab1/master/P4/src/tweetPoll.py");

    private static URL makeURLAssertWellFormatted(String urlString) {
        try {
            return new URL(urlString);
        } catch (MalformedURLException murle) {
            throw new AssertionError(murle);
        }
    }
    
    @BeforeClass
    public static void setUp() {
        try {
            SocialNetworkTest.tweets = TweetReader.readTweetsFromWeb(SAMPLE_SERVER);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }
    
    @Test
    public void testGuessFollowsGraph( ) {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        List<String> users = SocialNetwork.influencers(followsGraph);
        assertTrue("expected 1st popular users", users.contains("RealJamesWoods"));
        assertTrue("expected 2nd popular users", users.contains("realDonaldTrump"));        
        assertTrue("expected number of graph nodes", users.size() == 2023);
    }
    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */

}
