package com.codepath.apps.simpletweet.models;

import android.util.Log;

import androidx.room.Embedded;

import java.util.ArrayList;
import java.util.List;

public class TweetWithUser {
    @Embedded
    User user;

    @Embedded(prefix="tweet_")
    Tweet tweet;

    public static List<Tweet> getTweetList(List<TweetWithUser> tweetsWithUser) {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < tweetsWithUser.size(); i++) {
            Tweet tweet = tweetsWithUser.get(i).tweet;
            tweet.user = tweetsWithUser.get(i).user;
            tweets.add(tweet);
        }

        return tweets;
    }
}
