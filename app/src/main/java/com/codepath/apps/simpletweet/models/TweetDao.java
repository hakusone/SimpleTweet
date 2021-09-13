package com.codepath.apps.simpletweet.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TweetDao {

    // @Query annotation requires knowing SQL syntax
    // See http://www.sqltutorial.org/

    @Query("SELECT * FROM Tweet WHERE id = :id")
    Tweet byId(long id);

    @Query("SELECT * FROM Tweet ORDER BY ID DESC LIMIT 25")
    List<Tweet> recentItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertTweet(Tweet tweet);
}