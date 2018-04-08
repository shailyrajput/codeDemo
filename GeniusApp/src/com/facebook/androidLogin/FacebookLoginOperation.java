package com.facebook.androidLogin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import com.facebook.android.Util;
import com.facebook.androidLogin.SessionEvents.AuthListener;
import com.facebook.androidLogin.SessionEvents.LogoutListener;

import com.illuminz.geniusapp.R;

public class FacebookLoginOperation {

	static String[] permissions = { "offline_access", "publish_stream", "user_photos", "publish_checkins", "photo_upload" };
	final static int AUTHORIZE_ACTIVITY_RESULT_CODE = 0;
	public static Context context;
	private static SessionListener mSessionListener;
	private static Facebook mFb;
	private Handler mHandler;
	Activity activity;
	String TAG = "FacebookLoginOperation";
	@SuppressWarnings("unused")
	private SharedPreferences sharedPrefs;
	public static String fbId = "id";
	public static String fbName = "name";
	public static String fbEmailID = "link";
	int IsUserAllowForVoting;

	public static ProgressDialog processDialig;

	public static boolean allowForFacebook = true;

	@SuppressWarnings("static-access")
	public void processLogin(Context ctx, final Activity activity) {
		this.context = ctx;
		this.activity = activity;
		mSessionListener = new SessionListener();

		Log.d(TAG, "Facebook App login start >>");

		initFunctionality(activity, AUTHORIZE_ACTIVITY_RESULT_CODE, Utility.mFacebook, permissions);
		Log.d(TAG, "processLogin Start..");
//		Toast toast = Toast.makeText(activity, "Please Wait...", Toast.LENGTH_SHORT);
//		toast.setGravity(Gravity.CENTER, 0, 0);
//		toast.show();
		// activity.runOnUiThread(new Runnable() {
		// public void run() {
		// processDialig = ProgressDialog.show(activity, null, "Loading Data..",
		// true);
		// processDialig.setCancelable(false);
		// System.out.println("Progress dialog start");
		// }
		// });

	}

	private void initFunctionality(Activity activity, final int activityCode, final Facebook fb, final String[] permissions) {
		SessionEvents.addAuthListener(mSessionListener);
		mFb = fb;
		mHandler = new Handler();
		System.out.println("Login Start");
//		new ShareFacebook(activity);
		// if (mFb.isSessionValid()) {
		// SessionEvents.onLogoutBegin();
		// AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFb);
		// asyncRunner.logout(context, new LogoutRequestListener());
		// } else {
		Log.d(TAG, "In else It means login first");
		//mFb.authorize(activity, new String[] {}, Facebook.FORCE_DIALOG_AUTH, new LoginDialogListener());
		// }
	}

	private class SessionListener implements AuthListener, LogoutListener {

		public void onAuthSucceed() {
			SessionStore.save(mFb, context);
			
		}

		public void onAuthFail(String error) {
		}

		public void onLogoutBegin() {
		}

		public void onLogoutFinish() {
			SessionStore.clear(context);
		}
	}

	@SuppressWarnings("unused")
	private class LogoutRequestListener extends BaseRequestListener {
		public void onComplete(String response, final Object state) {
			/*
			 * callback should be run in the original thread, not the background
			 * thread
			 */
			mHandler.post(new Runnable() {
				public void run() {
					SessionEvents.onLogoutFinish();
				}
			});
		}
	}

	@SuppressWarnings("unused")
	private final class LoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {

			Log.d(TAG, "FacebookLogin_Task");
			
	
		/*	SessionEvents.onLoginSuccess();
			Log.d(TAG, "LoginIn Start");
			new ShareFacebook(activity);
			String token = mFb.getAccessToken();
			long token_expires = mFb.getAccessExpires();
			Log.d(TAG, "AccessToken: " + token);
			Log.d(TAG, "AccessExpires: " + token_expires);
			sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
			sharedPrefs.edit().putLong("access_expires", token_expires).commit();
			sharedPrefs.edit().putString("access_token", token).commit();
			AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFb);*/
		//	new ShareFacebook(activity);
			//asyncRunner.request("me", new IDRequestListener());
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

	@SuppressWarnings("unused")
	private class IDRequestListener implements RequestListener {

		public void onComplete(String response, Object state) {

			try {
				Log.d(TAG, "IDRequestONComplete");
				Log.d(TAG, "Response: " + response.toString());
				JSONObject json = Util.parseJson(response);
				fbId = json.getString("id");
				Log.d(TAG, "User Facebook id-" + fbId);
				fbName = json.getString("name");
				Log.d(TAG, "User Facebook name-" + fbName);
				fbEmailID = json.getString("link");
			} catch (JSONException e) {
				Log.d(TAG, "JSONException: " + e.getMessage());
			} catch (FacebookError e) {
				Log.d(TAG, "FacebookError: " + e.getMessage());
			}
	      //  processDialig.dismiss();
			activity.runOnUiThread(new Runnable() {
				public void run() {
					//new ShareFacebook(activity);
				}
			});
		}

		public void onIOException(IOException e, Object state) {
			// TODO Auto-generated method stub

		}

		public void onFileNotFoundException(FileNotFoundException e, Object state) {
			// TODO Auto-generated method stub

		}

		public void onMalformedURLException(MalformedURLException e, Object state) {
			// TODO Auto-generated method stub

		}

		public void onFacebookError(FacebookError e, Object state) {
			// TODO Auto-generated method stub

		}

	}

	@SuppressWarnings("unused")
	private void ConnectServerForCheckCaneUserVote() {
		// TODO Auto-generated method stub
		// CanUserVoteClass.CanUserVote(context, fbName, fbId, fbEmailID);

	}

	public void onIOException(IOException e, Object state) {
		Log.d(TAG, "IOException: " + e.getMessage());
	}

	public void onFileNotFoundException(FileNotFoundException e, Object state) {
		Log.d(TAG, "FileNotFoundException: " + e.getMessage());
	}

	public void onMalformedURLException(MalformedURLException e, Object state) {
		Log.d(TAG, "MalformedURLException: " + e.getMessage());
	}

	public void onFacebookError(FacebookError e, Object state) {
		Log.d(TAG, "FacebookError: " + e.getMessage());
	}

}
