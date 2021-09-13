package com.codepath.apps.simpletweet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.simpletweet.databinding.ActivityTweetDetailBinding;
import com.codepath.apps.simpletweet.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailActivity extends AppCompatActivity {

    public static final String TAG = "TweetDetailActivity";
    private ActivityTweetDetailBinding binding;
    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        getSupportActionBar().setTitle("Tweet");


        binding = DataBindingUtil.setContentView(this, R.layout.activity_tweet_detail);
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        binding.setTweet(tweet);
        Log.d(TAG, "Showing details for tweet");
    }
}