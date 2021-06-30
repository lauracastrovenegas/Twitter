package com.codepath.apps.restclienttemplate.models;

import com.facebook.stetho.inspector.jsonrpc.JsonRpcException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Entities {

    public List<Media> media;

    // Empty constructor for Parcel Library
    public Entities(){}

    public static Entities fromJson(JSONObject jsonObject) throws JSONException {
        Entities entities = new Entities();
        try {
            entities.media = Media.fromJsonArray(jsonObject.getJSONArray("media"));
        } catch (JSONException e){
            entities.media = new ArrayList<>();
            entities.media.add(new Media());
        }

        return entities;
    }

}
