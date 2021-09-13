package com.codepath.apps.simpletweet.models;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.codepath.apps.simpletweet.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
@Entity(foreignKeys = @ForeignKey(entity=User.class, parentColumns="id", childColumns="user_id"))
public class Tweet {
    @ColumnInfo
    @PrimaryKey
    public long id;
    @ColumnInfo
    public String body;
    @ColumnInfo
    public String createdAt;
    @ColumnInfo
    public boolean retweeted;
    @ColumnInfo
    public boolean favorited;
    @ColumnInfo
    public Integer retweetCount;
    @ColumnInfo
    public Integer favoriteCount;

    @Embedded(prefix="user_")
    public User user;

    public long getId() {
        return id;
    }

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.id = jsonObject.getLong("id");
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.retweeted = jsonObject.getBoolean("retweeted");
        tweet.retweetCount = jsonObject.getInt("retweet_count");
        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.favoriteCount = jsonObject.getInt("favorite_count");

        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }

        return tweets;
    }

    public String getFormattedTimeDifference() {
        return TimeFormatter.getTimeDifference(this.createdAt);
    }
}
