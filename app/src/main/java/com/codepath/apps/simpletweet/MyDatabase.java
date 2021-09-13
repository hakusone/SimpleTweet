package com.codepath.apps.simpletweet;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.codepath.apps.simpletweet.models.Tweet;
import com.codepath.apps.simpletweet.models.TweetDao;
import com.codepath.apps.simpletweet.models.User;
import com.codepath.apps.simpletweet.models.UserDao;

@Database(entities={Tweet.class, User.class}, version=10)
public abstract class MyDatabase extends RoomDatabase {
    public abstract TweetDao tweetDao();
    public abstract UserDao userDao();

    // Database name to be used
    public static final String NAME = "MyDataBase";
}
