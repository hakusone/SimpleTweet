package com.codepath.apps.simpletweet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.codepath.apps.simpletweet.databinding.ActivityTweetDetailBinding;
import com.codepath.apps.simpletweet.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailActivity extends AppCompatActivity implements ComposeDialogFragment.ComposeTweetDialogListener {

    public static final String TAG = "TweetDetailActivity";
    private ActivityTweetDetailBinding binding;
    ImageButton ibReply;
    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Tweet");

        binding = DataBindingUtil.setContentView(this, R.layout.activity_tweet_detail);
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        binding.setTweet(tweet);
        Log.d(TAG, "Showing details for tweet");

        ibReply = (ImageButton) findViewById(R.id.ibReply);

        ibReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                ComposeDialogFragment composeDialogFragment = ComposeDialogFragment.newInstance(tweet.user.screenName, String.valueOf(tweet.getId()));
                composeDialogFragment.show(fm, "activity_compose");
            }
        });
    }

    public void onFinishComposeTweetDialog(Tweet tweet) {
        Intent data = new Intent();
        data.putExtra("tweet", Parcels.wrap(tweet));

        setResult(RESULT_OK, data);
        finish();
    }
}