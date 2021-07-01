package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {

    private static final String NAME = "name";
    private static final String SCREEN_NAME = "screen_name";
    private static final String PROFILE_IMAGE = "profile_image_url_https";

    public String name;
    public String screenName;
    public String profileImageUrl;

    // Empty constructor for Parcel Library
    public User(){}

    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.name = jsonObject.getString(NAME);
        user.screenName = jsonObject.getString(SCREEN_NAME);
        user.profileImageUrl = jsonObject.getString(PROFILE_IMAGE);

        return user;
    }
}
