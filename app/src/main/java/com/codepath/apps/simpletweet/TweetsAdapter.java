package com.codepath.apps.simpletweet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.apps.simpletweet.databinding.ItemTweetBinding;
import com.codepath.apps.simpletweet.models.Tweet;

import org.parceler.Parcels;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = "TweetsAdapter";

    Context context;
    List<Tweet> tweets;

    public interface AdapterListener {
        void onReplyClick(int position);
    }

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);

        ItemTweetBinding binding =
                ItemTweetBinding.inflate(inflater, parent, false);
        viewHolder = new ViewHolder(binding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);

        ViewHolder vh = (ViewHolder) holder;

        vh.binding.setTweet(tweet);

        vh.binding.ibRetweet.setSelected(tweet.retweeted);
        vh.binding.tvRetweet.setSelected(tweet.retweeted);
        vh.binding.ibFavorite.setSelected(tweet.favorited);
        vh.binding.tvFavorite.setSelected(tweet.favorited);

        vh.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ItemTweetBinding binding;
        ImageButton ibReply;

        public ViewHolder(ItemTweetBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            ibReply = (ImageButton) itemView.findViewById(R.id.ibReply);

            ibReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AdapterListener listener = (AdapterListener) context;
                    listener.onReplyClick(getBindingAdapterPosition());
                }
            });
        }
    }
}
