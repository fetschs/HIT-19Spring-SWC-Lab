/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P4.twitter;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of every
     *         tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
        if (tweets.size() == 0) {
            System.out.println("ERROR:empty tweets list");
            return null;
        } // handle the empty tweet list
        Instant start = tweets.get(0).getTimestamp();
        Instant end = tweets.get(0).getTimestamp();
        for (Tweet tweet : tweets) {
            Instant tempTimestamp = tweet.getTimestamp();
            if (tempTimestamp.isBefore(start))
                start = tempTimestamp;
            if (tempTimestamp.isAfter(end))
                end = tempTimestamp;
        } // find the start timestamp and end timestamp of these tweets
          // means the earliest and latest
        Timespan retTimespan = new Timespan(start, end);
        return retTimespan;
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets. A
     *         username-mention is "@" followed by a Twitter username (as defined by
     *         Tweet.getAuthor()'s spec). The username-mention cannot be immediately
     *         preceded or followed by any character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT
     *         contain a mention of the username mit. Twitter usernames are
     *         case-insensitive, and the returned set may include a username at most
     *         once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        if (tweets.size() == 0) {
            System.out.println("ERROR:empty tweets list");
            return null;
        } // handle the empty tweet set
        Set<String> retSet = new HashSet<>();
        Set<String> checkSet = new HashSet<>();// avoid repeat name
        for (Tweet tweet : tweets) {
            String content = tweet.getText();
            String regex = "([^\\w|-])@([\\w|-]+)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(content);
            while (matcher.find() == true) {
                String username = matcher.group(2);
                String factusername = username.toLowerCase();
                if (checkSet.contains(factusername) == false) {
                    checkSet.add(factusername);
                    retSet.add(username);
                }
            }
            regex = "^@([\\w|-]+)";// special design
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(content);
            while (matcher.find() == true) {
                String username = matcher.group(1);
                String factusername = username.toLowerCase();
                if (checkSet.contains(factusername) == false) {
                    checkSet.add(factusername);
                    retSet.add(username);
                }
            }
        } // get the username
        return retSet;
    }

    /**
     * Get hashtags mentioned in a list of tweets.
     * 
     * @param tweets list of tweets with distinct ids, not modified by this method.
     * @return the set of hashtags who are mentioned in the text of the tweets. Here
     *         assume that a hashtag-mention is "#" followed by a string like
     *         Twitter username (as defined by Tweet.getAuthor()'s spec). The
     *         hashtag-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter hashtag. Twitter hashtags are
     *         case-insensitive, and the returned set may include a hashtage at most
     *         once.
     */
    public static Set<String> getHashtags(List<Tweet> tweets) {
        if (tweets.size() == 0) {
            System.out.println("ERROR:empty tweets list");
            return null;
        }
        Set<String> retSet = new HashSet<>();
        Set<String> checkSet = new HashSet<>();
        for (Tweet tweet : tweets) {
            String content = tweet.getText();
            String regex = "([^\\w|]+)#([\\w|]+)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(content);
            while (matcher.find() == true) {
                String hashtag = matcher.group(2);
                String facthashtag = hashtag.toLowerCase();
                if (checkSet.contains(facthashtag) == false) {
                    checkSet.add(facthashtag);
                    retSet.add(hashtag);
                }
            }
        }
        return retSet;
    }
}
