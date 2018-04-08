/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.facebook.androidLogin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.facebook.androidLogin.SessionEvents.AuthListener;
import com.facebook.androidLogin.SessionEvents.LogoutListener;

public class LoginFunction  {

	private Facebook mFb;
	@SuppressWarnings("unused")
	private Handler mHandler;
	private SessionListener mSessionListener = new SessionListener();
	private String[] mPermissions;
	private Activity mActivity;
	String TAG = "LoginButton";
	private SharedPreferences sharedPrefs;
	String fbId, fbName, fbEmail;
	
	public void init(final Activity activity, final int activityCode, final Facebook fb,
            final String[] permissions) {
		 System.out.println("LoginButton work start");
		mActivity = activity;
		mFb = fb;
		mPermissions = permissions;
		mHandler = new Handler();
		SessionEvents.addAuthListener(mSessionListener);
		
		new ButtonOnClickListener();
		
	}

	public class ButtonOnClickListener  {

		@SuppressWarnings({ "static-access", "unused" })
		public void onClick(View arg0) {
			if (mFb.isSessionValid()) {
				AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFb);
			} else {
				mFb.authorize(mActivity, mPermissions,
						new LoginDialogListener());
			}
		}
	}

	private final class LoginDialogListener implements DialogListener {
		@SuppressWarnings("static-access")
		public void onComplete(Bundle values) {
			SessionEvents.onLoginSuccess();
			Log.d(TAG, "LoginONComplete");
			String token = mFb.getAccessToken();
			long token_expires = mFb.getAccessExpires();
			Log.d(TAG, "AccessToken: " + token);
			Log.d(TAG, "AccessExpires: " + token_expires);
			sharedPrefs = PreferenceManager
					.getDefaultSharedPreferences(mActivity);
			sharedPrefs.edit().putLong("access_expires", token_expires)
					.commit();
			sharedPrefs.edit().putString("access_token", token).commit();
			AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFb);
			asyncRunner.request("me", new IDRequestListener());

		}

		public void onFacebookError(FacebookError error) {
			SessionEvents.onLoginError(error.getMessage());
		}

		public void onError(DialogError error) {
			SessionEvents.onLoginError(error.getMessage());
		}

		public void onCancel() {
			SessionEvents.onLoginError("Action Canceled");
		}
	}

	private class IDRequestListener implements RequestListener {

		public void onComplete(String response, Object state) {
			try {
				Log.d(TAG, "IDRequestONComplete");
				Log.d(TAG, "Response: " + response.toString());
				JSONObject json = Util.parseJson(response);
				fbId = json.getString("id");
				fbName = json.getString("name");
			} catch (JSONException e) {
				Log.d(TAG, "JSONException: " + e.getMessage());
			} catch (FacebookError e) {
				Log.d(TAG, "FacebookError: " + e.getMessage());
			}
		}

		public void onIOException(IOException e, Object state) {
			Log.d(TAG, "IOException: " + e.getMessage());
		}

		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
			Log.d(TAG, "FileNotFoundException: " + e.getMessage());
		}

		public void onMalformedURLException(MalformedURLException e,
				Object state) {
			Log.d(TAG, "MalformedURLException: " + e.getMessage());
		}

		public void onFacebookError(FacebookError e, Object state) {
			Log.d(TAG, "FacebookError: " + e.getMessage());
		}

	}

	private class SessionListener implements AuthListener, LogoutListener {

		public void onAuthSucceed() {
		}

		public void onAuthFail(String error) {
		}

		public void onLogoutBegin() {
			// TODO Auto-generated method stub
			
		}

		public void onLogoutFinish() {
			// TODO Auto-generated method stub
			
		}

	}
}
