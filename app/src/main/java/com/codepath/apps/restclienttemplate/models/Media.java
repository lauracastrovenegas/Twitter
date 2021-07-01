package com.codepath.apps.restclienttemplate.models;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.facebook.stetho.inspector.jsonrpc.JsonRpcException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Media {

    private static final String IMAGE_URL_REQUEST = "media_url_https";
    public String mediaUrl;

    public Media(){
        mediaUrl = "";
    }

    public static Media fromJson(JSONObject jsonObject) throws JSONException {
        Media media = new Media();
        media.mediaUrl = jsonObject.getString(IMAGE_URL_REQUEST);

        return media;
    }

    public static List<Media> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Media> media = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            media.add(fromJson(jsonArray.getJSONObject(i)));
        }

        return media;
    }

    public String getMediaUrl(){
        return mediaUrl;
    }
}
