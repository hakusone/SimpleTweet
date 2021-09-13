package com.codepath.apps.simpletweet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.simpletweet.models.Tweet;
import com.codepath.apps.simpletweet.models.TweetDao;
import com.codepath.apps.simpletweet.models.UserDao;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {
    public static final String TAG = "TimelineActivity";

    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;
    private long oldestTweetId;
    Context context;
    TweetDao tweetDao;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        client = TwitterApp.getRestClient(this);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "Fetching new data");
                populateHomeTimeline();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        rvTweets = findViewById(R.id.rvTweets);
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreData();
            }
        };

        rvTweets.addOnScrollListener(scrollListener);

        ItemClickSupport.addTo(rvTweets).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Log.d(TAG, "position: " + Integer.toString(position));
                if (position != RecyclerView.NO_POSITION) {
                    Tweet tweet = tweets.get(position);
                    Intent intent = new Intent(context, TweetDetailActivity.class);
                    intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                    context.startActivity(intent);
                }
            }
        });


        populateHomeTimeline();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.miCompose:
                Toast.makeText(this, "Compose", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess" + json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    adapter.clear();
                    List<Tweet> newTweets = Tweet.fromJsonArray(jsonArray);
                    adapter.addAll(newTweets);
                    Tweet oldestTweet = newTweets.get(newTweets.size() - 1); // get last tweet
                    oldestTweetId = oldestTweet.getId();
                    updateDatabase(newTweets);
                    adapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    Log.e(TAG, "json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure" + response, throwable);
                Toast.makeText(context, "No internet", Toast.LENGTH_SHORT).show();
                getFromDatabase();
            }
        });
    }

    public void loadMoreData() {
        Log.i("TimelineActivity", "Loading more data...");
        client.getNextPageOfTweets(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess" + json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    List<Tweet> newTweets = Tweet.fromJsonArray(jsonArray);
                    adapter.addAll(newTweets);
                    Tweet oldestTweet = newTweets.get(newTweets.size() - 1); // get last tweet
                    oldestTweetId = oldestTweet.getId();
                    updateDatabase(newTweets);
                    adapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    Log.e(TAG, "json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure" + response, throwable);

                Toast.makeText(context, "No internet", Toast.LENGTH_SHORT).show();
            }
        }, oldestTweetId - 1);
    }

    public void updateDatabase(List<Tweet> tweets) {
        tweetDao = ((TwitterApp) getApplicationContext()).getMyDatabase().tweetDao();
        userDao = ((TwitterApp) getApplicationContext()).getMyDatabase().userDao();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                for (Tweet tweetModel : tweets) {
                    tweetDao.insertTweet(tweetModel);
                    userDao.insertUser(tweetModel.user);
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                    }
                });
            }
        });
    }

    public void getFromDatabase() {
        tweetDao = ((TwitterApp) getApplicationContext()).getMyDatabase().tweetDao();
        userDao = ((TwitterApp) getApplicationContext()).getMyDatabase().userDao();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Tweet> tweets = tweetDao.recentItems();

                adapter.addAll(tweets);
                adapter.notifyDataSetChanged();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                    }
                });
            }
        });
    }
}