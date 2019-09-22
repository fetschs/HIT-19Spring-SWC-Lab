/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */

package P4.twitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even
 * exist as a key in the map; this is true even if A is followed by other people
 * in the network. Twitter usernames are not case sensitive, so "ernie" is the
 * same as "ERNie". A username should appear at most once as a key in the map or
 * in any given map[A] set.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */

public class SocialNetwork {
    private static class UserWithInfluence {
        private String name;
        private int influences;

        public UserWithInfluence(String name, int influences) {
            this.name = name;
            this.influences = influences;
        }

        public int getInfluences() {
            return influences;
        }

        public String getName() {
            return name;
        }

        public static final Comparator<UserWithInfluence> sortbyinfluence = new Comparator<SocialNetwork.UserWithInfluence>() {
            public int compare(UserWithInfluence u1, UserWithInfluence u2) {
                Integer inf1 = u1.getInfluences();
                Integer inf2 = u2.getInfluences();
                return inf2.compareTo(inf1);
            }
        };
    }

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets a list of tweets providing the evidence, not modified by this
     *               method.
     * @return a social network (as defined above) in which Ernie follows Bert if
     *         and only if there is evidence for it in the given list of tweets. One
     *         kind of evidence that Ernie follows Bert is if Ernie
     * @-mentions Bert in a tweet. This must be implemented. Other kinds of evidence
     *            may be used at the implementor's discretion. All the Twitter
     *            usernames in the returned social network must be either authors
     *            or @-mentions in the list of tweets.
     */

    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        Map<String, Set<String>> tagsGraph = new HashMap<>(); // use for split people by hashtag
        Set<String> authors = new HashSet<>();
        for (Tweet tweet : tweets) {
            authors.add(tweet.getAuthor());
        }
        for (String author : authors) {
            List<Tweet> currenttwwets = Filter.writtenBy(tweets, author);
            Set<String> mentionedusers = Extract.getMentionedUsers(currenttwwets);
            for (String mentioneduser : mentionedusers) {
                Set<String> tempset = followsGraph.get(mentioneduser);
                if(mentioneduser.equals(author)) {
                    continue;
                }
                try {
                    tempset.add(author);
                } catch (NullPointerException e) {
                    tempset = new HashSet<>();
                    tempset.add(author);
                }
                followsGraph.put(mentioneduser, tempset);
            } // find the author's mentioneduser, and add the author to their followers.
            Set<String> hashtags = Extract.getHashtags(currenttwwets);
            for (String hashtag : hashtags) {
                Set<String> tempset = followsGraph.get(hashtag);
                try {
                    tempset.add(author);
                } catch (NullPointerException e) {
                    tempset = new HashSet<>();
                    tempset.add(author);
                }
                tagsGraph.put(hashtag, tempset);
            } // find the author's hashtags, and add the author to the hashtag's set.
        }
        for (String hashtag : tagsGraph.keySet()) {
            Set<String> users = tagsGraph.get(hashtag);
            for (String user1 : users) {
                for (String user2 : users) {
                    if (user1.equals(user2)) {
                        continue;
                    }
                    Set<String> tempset1 = followsGraph.get(user1);
                    Set<String> tempset2 = followsGraph.get(user2);
                    if (tempset1 != null) {
                        if (tempset1.contains(user2)) {
                            try {
                                tempset2.add(user1);
                            } catch (NullPointerException e) {
                                tempset2 = new HashSet<>();
                                tempset2.add(user1);
                            }
                        }
                    }
                    if (tempset2 != null) {
                        if (tempset2.contains(user1)) {
                            try {
                                tempset1.add(user2);
                            } catch (NullPointerException e) {
                                tempset1 = new HashSet<>();
                                tempset1.add(user2);
                            }
                        }
                    }
                    followsGraph.put(user1, tempset1);
                    followsGraph.put(user2, tempset2);
                }
            }
        }
        return followsGraph;
    }

    /**
     * Find the people in a social network who have the greatest influence, in the
     * sense that they have the most followers.
     * 
     * @param followsGraph a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        List<UserWithInfluence> userWithInfluences = new ArrayList<>();
        List<String> retList = new ArrayList<>();
        for (String user : followsGraph.keySet()) {
            int influences;
            try {
                influences = followsGraph.get(user).size();
            } catch (NullPointerException e) {
                influences = 0;
            }
            UserWithInfluence userWithInfluence = new UserWithInfluence(user, influences);
            userWithInfluences.add(userWithInfluence);
        }
        Collections.sort(userWithInfluences, UserWithInfluence.sortbyinfluence);
        for (UserWithInfluence temp : userWithInfluences) {
            retList.add(temp.getName());
        }

        return retList;
    }

}
