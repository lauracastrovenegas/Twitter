package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.net.IDN;
import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {

    private static final String TEXT = "text";
    private static final String FULL_TEXT = "full_text";
    private static final String DATE = "created_at";
    private static final String USER = "user";
    private static final String ENTITIES = "entities";
    private static final String RETWEET_COUNT = "retweet_count";
    private static final String LIKE_COUNT = "favorite_count";
    private static final String TWEET_ID = "id";

    public String body;
    public String createdAt;
    public User user;
    public Entities entities;
    public long id;
    public int retweet_count;
    public int favorite_count;

    // Empty constructor for Parcel Library
    public Tweet(){}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        if(jsonObject.has(FULL_TEXT)) {
            tweet.body = jsonObject.getString(FULL_TEXT);
        } else {
            tweet.body = jsonObject.getString(TEXT);
        }
        tweet.createdAt = jsonObject.getString(DATE);
        tweet.user = User.fromJson(jsonObject.getJSONObject(USER));
        tweet.entities = Entities.fromJson(jsonObject.getJSONObject(ENTITIES));
        tweet.retweet_count = jsonObject.getInt(RETWEET_COUNT);
        tweet.favorite_count = jsonObject.getInt(LIKE_COUNT);
        tweet.id = jsonObject.getLong(TWEET_ID);
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }

        return tweets;
    }
}
