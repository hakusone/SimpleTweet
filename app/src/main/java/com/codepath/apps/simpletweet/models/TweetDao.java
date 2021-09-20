package com.codepath.apps.simpletweet.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TweetDao {
    @Query("SELECT Tweet.id AS tweet_id, Tweet.body AS tweet_body, Tweet.createdAt AS tweet_createdAt, " +
            "Tweet.retweeted AS tweet_retweeted, Tweet.favorited AS tweet_favorited," +
            "Tweet.retweetCount AS tweet_retweetCount, Tweet.favoriteCount AS tweet_favoriteCount," +
            "User.* FROM Tweet INNER JOIN User ON Tweet.userId = User.id " +
            "ORDER BY Tweet.id DESC LIMIT 300")
    List<TweetWithUser> recentItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(Tweet... tweets);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertModel(User... users);
}
