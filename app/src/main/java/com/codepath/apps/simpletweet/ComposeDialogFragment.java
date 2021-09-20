package com.codepath.apps.simpletweet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.simpletweet.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;

import okhttp3.Headers;

public class ComposeDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final int MAX_TWEET_LENGTH = 280;
    public static final String TAG = "ComposeActivity";

    EditText etCompose;
    Button btnTweet;
    TextView tvCounter;
    TextView tvReplyTo;
    TwitterClient client;
    ImageView ivReply;
    ImageButton btnClose;
    String tweetId;

    public interface ComposeTweetDialogListener {
        void onFinishComposeTweetDialog(Tweet tweet);
    }

    public ComposeDialogFragment() {

    }

    public static ComposeDialogFragment newInstance(String replyTo, String tweetId) {
        ComposeDialogFragment fragment = new ComposeDialogFragment();
        Bundle args = new Bundle();
        args.putString("replyTo", replyTo);
        args.putString("tweetId", tweetId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.activity_compose, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String replyTo = getArguments().getString("replyTo", "");
        tweetId = getArguments().getString("tweetId", "");

        etCompose = view.findViewById(R.id.etCompose);
        btnTweet = view.findViewById(R.id.btnTweet);
        tvCounter = view.findViewById(R.id.tvCounter);
        ivReply = view.findViewById(R.id.ivReply);
        tvReplyTo = view.findViewById(R.id.tvReplyTo);
        btnClose = view.findViewById(R.id.btnClose);

        if (!replyTo.equals("")) {
            replyTo = "@" + replyTo;
            tvReplyTo.setText(replyTo);
            etCompose.setText(replyTo);
        }
        else {
            tvReplyTo.setVisibility(View.INVISIBLE);
            ivReply.setVisibility(View.INVISIBLE);
        }

        etCompose.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        client = TwitterApp.getRestClient(view.getContext());

        btnTweet.setOnClickListener(this);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int textCount = charSequence.length();
                String display = String.valueOf(textCount) + "/" + String.valueOf(MAX_TWEET_LENGTH);
                tvCounter.setText(display);
                if (textCount > MAX_TWEET_LENGTH) {
                    btnTweet.setEnabled(false);
                    tvCounter.setTextColor(getResources().getColor(R.color.medium_red));
                }
                else {
                    btnTweet.setEnabled(true);
                    tvCounter.setTextColor(getResources().getColor(R.color.light_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        String tweetContent = etCompose.getText().toString();
        if (tweetContent.isEmpty()) {
            Toast.makeText(getActivity(), "Sorry, your tweet cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        if (tweetContent.length() > MAX_TWEET_LENGTH) {
            Toast.makeText(getActivity(), "Sorry, your tweet is too long", Toast.LENGTH_LONG).show();
            return;
        }

        if (!isNetworkAvailable()) {
            Toast.makeText(getActivity(), "No network connection available", Toast.LENGTH_LONG).show();
            return;
        }

        // make API call to twitter API to publish tweet
        client.publishTweet(tweetContent, tweetId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess to publish tweet");
                try {
                    Tweet newTweet = Tweet.fromJson(json.jsonObject);
                    Log.i(TAG, "Published tweet says: " + tweetContent);

//                    Intent data = new Intent();
//                    data.putExtra("tweet", Parcels.wrap(newTweet));

                    ComposeTweetDialogListener listener = (ComposeTweetDialogListener) getActivity();
                    listener.onFinishComposeTweetDialog(newTweet);
                    dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure to publish tweet", throwable);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}