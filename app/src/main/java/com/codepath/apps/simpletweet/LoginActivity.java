package com.codepath.apps.simpletweet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.codepath.apps.simpletweet.models.SampleModel;
import com.codepath.apps.simpletweet.models.SampleModelDao;
import com.codepath.oauth.OAuthLoginActionBarActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

	SampleModelDao sampleModelDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		final SampleModel sampleModel = new SampleModel();
		sampleModel.setName("CodePath");

		sampleModelDao = ((TwitterApp) getApplicationContext()).getMyDatabase().sampleModelDao();

		ExecutorService executor = Executors.newSingleThreadExecutor();
		Handler handler = new Handler(Looper.getMainLooper());

		executor.execute(new Runnable() {
			@Override
			public void run() {
				sampleModelDao.insertModel(sampleModel);

				handler.post(new Runnable() {
					@Override
					public void run() {
						//UI Thread work here
					}
				});
			}
		});
	}


	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
		Log.d("LoginActivity", "login success");
		Intent i = new Intent(this, TimelineActivity.class);
		startActivity(i);
	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
		getClient().connect();
	}

}
