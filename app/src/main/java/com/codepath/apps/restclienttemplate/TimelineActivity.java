package com.codepath.apps.restclienttemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.apps.restclienttemplate.adapters.TweetsAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    public static final String SUCCESS_MSG = "Success!";
    public static final String FAILURE_MSG = "Failed!";

    public static final String TAG = "TimelineActivity";
    private final int REQUEST_CODE = 20;
    public static final String KEY = "tweet";

    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;

    TwitterClient client;
    RecyclerView rvTweets;
    TweetsAdapter adapter;
    List<Tweet> tweets;
    long max_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);

        // Set up swipe container for pull to refresh feature
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateHomeTimeline();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Initialize list of tweets
        tweets = new ArrayList<>();

        // Set up RecyclerView, Layout, and Adapter
        adapter = new TweetsAdapter(this, tweets);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rvTweets = (RecyclerView) findViewById(R.id.rvTweets);
        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.setAdapter(adapter);

        // Set scroll listener for endless scroll feature
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi();
            }
        };

        rvTweets.addOnScrollListener(scrollListener);

        // Set custom toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        populateHomeTimeline();
    }

    // Initial request for tweets; add to empty tweet list
    // Also used in case of refresh with swipeContainer
    private void populateHomeTimeline() {
        client.getHomeTimeline(-1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, SUCCESS_MSG + json.toString());
                try {
                    // Clear list and adapter in case of refresh
                    adapter.clear();
                    tweets.clear();

                    // populate tweet list
                    tweets.addAll(Tweet.fromJsonArray(json.jsonArray));
                    max_id = tweets.get(tweets.size() - 1).id;
                    adapter.notifyDataSetChanged();

                    // In case of refresh
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    Log.e(TAG, "Json exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, FAILURE_MSG + response, throwable);
            }
        });
    }

    // Request more tweets from API and add to current tweet list
    private void loadNextDataFromApi() {
        client.getHomeTimeline(max_id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    tweets.addAll(Tweet.fromJsonArray(json.jsonArray));
                    adapter.notifyDataSetChanged();
                    max_id = tweets.get(tweets.size() - 1).id;
                } catch (JSONException e) {
                    Log.e(TAG, "Json exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, FAILURE_MSG + response, throwable);
            }
        });

    }

    // Manually add new tweet composed to list of tweets; scroll to top
    // Used after coming back from Compose Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // Get data from the intent (tweet)
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra(KEY));
            // Update the RV with the tweet
            tweets.add(0, tweet);
            adapter.notifyItemInserted(0);

            // scroll to top of rv
            rvTweets.smoothScrollToPosition(0);
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    // Method used by Sign Out button -> Log out and go to Log in Activity
    public void onLogoutButton(View view) {
        client.clearAccessToken(); // forget who's logged in
        startActivity(new Intent(TimelineActivity.this, LoginActivity.class));
        finish(); // navigate backwards to Login screen
    }

    // Method used by Compose button -> Go to Compose Activity
    public void onComposeButton(View view) {
        Intent intent = new Intent(this, ComposeActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }
}