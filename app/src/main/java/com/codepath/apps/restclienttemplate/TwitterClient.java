package com.codepath.apps.restclienttemplate;

import android.content.Context;

import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.FlickrApi;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;

/*
 * This is the object responsible for communicating with a REST API.
 * See a full list of supported API classes: 
 *   https://github.com/scribejava/scribejava/tree/master/scribejava-apis/src/main/java/com/github/scribejava/apis
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 */
public class TwitterClient extends OAuthBaseClient {
	public static final BaseApi REST_API_INSTANCE = TwitterApi.instance();
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = BuildConfig.CONSUMER_KEY;       // Change this inside apikey.properties
	public static final String REST_CONSUMER_SECRET = BuildConfig.CONSUMER_SECRET; // Change this inside apikey.properties

	// Landing page to indicate the OAuth flow worked in case Chrome for Android 25+ blocks navigation back to the app.
	public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

	// See https://developer.chrome.com/multidevice/android/intents
	public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

	// API URL fetch strings
	public static final String GET_TIMELINE_URL = "statuses/home_timeline.json";
	public static final String POST_TWEET_URL = "statuses/update.json";

	// Parameters for fetch requests
	public static final String COUNT = "count";
	public static final String SINCE_ID = "since_id";
	public static final String MAX_ID = "max_id";
	public static final String STATUS = "status";
	public static final String TWEET_MODE = "tweet_mode";

	// current tweet mode
	public static final String TWEET_MODE_STRING = "extended";
	public static final int MAX_NUM_OF_TWEETS = 25;

	public TwitterClient(Context context) {
		super(context, REST_API_INSTANCE,
				REST_URL,
				REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET,
				null,  // OAuth2 scope, null for OAuth1
				String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
						context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
	}

	public void getHomeTimeline(long max_id, JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl(GET_TIMELINE_URL);
		RequestParams params = new RequestParams();
		if (max_id > -1){
			params.put(MAX_ID, max_id);
		} else {
			params.put(SINCE_ID, 1);
		}
		params.put(COUNT, MAX_NUM_OF_TWEETS);
		params.put(TWEET_MODE, TWEET_MODE_STRING);
		client.get(apiUrl, params, handler);
	}

	public void publishTweet(String tweetContent, JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl(POST_TWEET_URL);
		RequestParams params = new RequestParams();
		params.put(STATUS, tweetContent);
		client.post(apiUrl, params, "",handler);
	}
}
