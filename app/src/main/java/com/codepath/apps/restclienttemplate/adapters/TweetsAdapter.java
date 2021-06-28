package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweets;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        try {
            holder.bind(tweet);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        TextView tvScreenName;
        TextView tvBody;
        TextView tvCreatedAt;
        TextView tvName;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            tvName = itemView.findViewById(R.id.tvName);
        }

        public void bind(Tweet tweet) throws ParseException {
            tvBody.setText(tweet.body);
            tvScreenName.setText("@" + tweet.user.screenName);
            //tvCreatedAt.setText(tweet.createdAt);
            Calendar calendar = Calendar.getInstance();
            String today = new SimpleDateFormat("MM/dd/yy HH:mm aa").format(calendar.getTime());
            Log.i("Date and Time:", today);
            String tweetDate = (String) new SimpleDateFormat("MM/dd/yy HH:mm aa").format(new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy").parse(tweet.createdAt));
            tvCreatedAt.setText("· " + setDate(today, tweetDate));
            tvName.setText(tweet.user.name);
            Glide.with(context)
                    .load(tweet.user.profileImageUrl)
                    .circleCrop()
                    .into(ivProfileImage);
        }

        public String setDate(String today, String date) {
            if (today.substring(0,8).equals(date.substring(0,8))){
                return (String) date.substring(10);
            } else {
                return date.substring(0,8);
            }
        }
    }
}
