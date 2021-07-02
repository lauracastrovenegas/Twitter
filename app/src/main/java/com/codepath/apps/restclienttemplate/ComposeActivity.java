package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.databinding.ActivityComposeBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final String FAILURE_MSG = "Failed!";

    public static final int MAX_TWEET_LENGTH = 280;
    public static final String TAG = "Compose Activity";
    public static final String EMPTY_TWEET_MSG = "Sorry, your tweet cannot be empty";
    public static final String LONG_TWEET_MSG = "Sorry, your tweet is too long";
    public static final String KEY = "tweet";

    EditText etCompose;
    Button btnTweet;

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityComposeBinding binding = ActivityComposeBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        client = TwitterApp.getRestClient(this);

        etCompose = binding.etCompose;
        btnTweet = binding.btnTweet;

        // Set Click listener on button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, EMPTY_TWEET_MSG, Toast.LENGTH_LONG).show();
                    return;
                }
                if (tweetContent.length() > MAX_TWEET_LENGTH){
                    Toast.makeText(ComposeActivity.this, LONG_TWEET_MSG, Toast.LENGTH_LONG).show();
                    return;
                }

                // Make API call to twitter to publish tweet
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Intent intent = new Intent();
                            intent.putExtra(KEY, Parcels.wrap(tweet));
                            // set result code and bundle data for response
                            setResult(RESULT_OK, intent);
                            // close the activity, pass data to the parent
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, FAILURE_MSG, throwable);
                    }
                });
            }
        });

    }

    // Method used by X button to cancel composing of new tweet
    public void onClose(View v){
        finish();
    }
}