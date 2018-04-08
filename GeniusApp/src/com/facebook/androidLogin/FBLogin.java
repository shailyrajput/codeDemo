package com.facebook.androidLogin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

public class FBLogin {

	Activity activity;
	private Facebook mFacebook;
	private String facebookUID, facebookToken;
	private boolean newLogin;
	public FacebookCallback facebookCallback;
	private boolean facebookTaskruning = false;
	private ProgressBar bar;

	public boolean isFacebookTaskruning() {
		return facebookTaskruning;
	}

	public String getFacebookUID() {
		return facebookUID;
	}

	public String getFacebookToken() {
		return facebookToken;
	}

	public boolean isNewLogin() {
		return newLogin;
	}

	public FBLogin(Activity activity, FacebookCallback facebookCallback) {
		System.out.println("FBLogin.FBLogin()");
		this.activity = activity;
		this.facebookCallback = facebookCallback;
		mFacebook = new Facebook(FacebookConstants.mAPP_ID);
	}

	public FBLogin(Activity activity, FacebookCallback facebookCallback,
			ProgressBar bar) {

		this.activity = activity;
		this.facebookCallback = facebookCallback;
		System.out.println("..........hello login...." + facebookCallback);
		this.bar = bar;
		mFacebook = new Facebook(FacebookConstants.mAPP_ID);
	}

	/*
	 * public void loginFacebook(){ newLogin = false;
	 * facebookCallback.onFacebookLogin(); } public void logoutFacebook(){
	 * Toast.makeText( activity, "logged out", Toast.LENGTH_LONG).show(); }
	 */

	@SuppressWarnings("static-access")
	public void loginFacebook() {
		System.out.println("FBLogin.loginFacebook()....");
		facebookTaskruning = true;
		SessionStore.restore(mFacebook, activity);
		if (!mFacebook.isSessionValid()) {
			System.out.println("FBLogin.loginFacebook() if");
			// Toast.makeText(Login.this, "Authorizing",
			// Toast.LENGTH_SHORT).show();
			mFacebook.authorize(activity, new String[] {},
					Facebook.FORCE_DIALOG_AUTH, new LoginDialogListener());
		} else {
			System.out.println("FBLogin.loginFacebook() else ");
			requestMe(false);
		}
	}

	public void logoutFacebook() {
		facebookTaskruning = true;
		// SessionStore.clear(activity);
		logOut();
	}

	public final class LoginDialogListener implements DialogListener {

		public void onComplete(Bundle values) {
			System.out.println("FBLogin.LoginDialogListener.onComplete()");
			SessionStore.save(mFacebook, activity);
			requestMe(true);
		}

		public void onFacebookError(FacebookError error) {
			System.out
					.println("FBLogin.LoginDialogListener.onFacebookError().."
							+ error.getMessage());
			onerror(error.getMessage());
			facebookTaskruning = false;
		}

		public void onError(DialogError error) {
			onerror(error.getMessage());
			System.out.println("FBLogin.LoginDialogListener.onError()..."
					+ error.getMessage());
			facebookTaskruning = false;
		}

		public void onCancel() {
			System.out.println("FBLogin.LoginDialogListener.onCancel()");
			// Toast.makeText( activity,
			// "Something went wrong. Please try again.",
			// Toast.LENGTH_LONG).show();
			facebookTaskruning = false;
		}
	}

	private void requestMe(final boolean newlogin) {
		System.out.println("FBLogin.requestMe()");
		facebookTaskruning = true;
		setBar(View.VISIBLE);
		AsyncFacebookRunner asynFb = new AsyncFacebookRunner(mFacebook);
		asynFb.request("me", new MyRequestListner() {
			
			@SuppressWarnings("static-access")
			@Override
			public void onComplete(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				super.onComplete(arg0, arg1);
				try {
					JSONObject json = Util.parseJson(arg0);
					System.out.println("json>>>>>>>>>>" + json);
					facebookUID = json.getString("id");
					facebookToken = mFacebook.getAccessToken();
					newLogin = newlogin;
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							facebookCallback.onFacebookLogin();
						}
					});
				} catch (FacebookError e) {
					// TODO Auto-generated catch block
					onfacebookError(e);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					onerror("Unable to connect to Facebook, please try again.");
				}
			}
		});

	}

	private void onfacebookError(FacebookError e) {
		System.out
				.println("FBLogin.onfacebookError(...).new Runnable() {...}.run().."
						+ e.getMessage());

		// if(e.getErrorCode()== 0){
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

				SessionStore.clear(activity);
				loginFacebook();
				// mFacebook.authorize(activity, new String[] {},
				// Facebook.FORCE_DIALOG_AUTH, new LoginDialogListener());
			}
		});
		// }
	}

	private void onerror(final String error) {
		System.out.println("FBLogin.onerror().." + error);
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(activity, error, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void setBar(final int visibility) {
		System.out.println("FBLogin.setBar()..." + visibility);
		facebookTaskruning = false;
		if (bar == null) {
			return;
		}
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				bar.setVisibility(visibility);
			}
		});
	}

	private void logOut() {
		System.out.println("FBLogin.logOut()");
		AsyncFacebookRunner asynFb = new AsyncFacebookRunner(mFacebook);
		SessionStore.clear(activity);
		asynFb.logout(activity, new MyRequestListner() {

			@Override
			public void onComplete(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				super.onComplete(arg0, arg1);
				System.out
						.println("FBLogin.logOut().new MyRequestListner() {...}.onComplete()");
				SessionStore.clear(activity);
				if (facebookCallback != null)
					System.out
							.println("FBLogin.logOut().new MyRequestListner() {...}.onComplete()...if...."
									+ facebookCallback);
				facebookCallback.onFacebookLogout();
				facebookTaskruning = false;
				// Intent intent = new Intent(activity,
				// MvelopesLauncherActivity.class);
				// activity.startActivity(intent);
			}
		});

	}

	public class MyRequestListner implements RequestListener {

		@Override
		public void onComplete(String arg0, Object arg1) {
			// TODO Auto-generated method stub
			System.out.println("FBLogin.MyRequestListner.onComplete()");
			setBar(View.INVISIBLE);
		}

		@Override
		public void onFacebookError(FacebookError arg0, Object arg1) {
			System.out.println("FBLogin.MyRequestListner.onFacebookError()");
			// TODO Auto-generated method stub
			if (facebookCallback != null)
				facebookCallback.onFacebookError();
			setBar(View.INVISIBLE);
		}

		@Override
		public void onFileNotFoundException(FileNotFoundException arg0,
				Object arg1) {
			System.out
					.println("FBLogin.MyRequestListner.onFileNotFoundException()");
			// TODO Auto-generated method stub
			if (facebookCallback != null)
				facebookCallback.onFacebookError();
			setBar(View.INVISIBLE);
		}

		@Override
		public void onIOException(IOException arg0, Object arg1) {
			System.out.println("FBLogin.MyRequestListner.onIOException()");
			// TODO Auto-generated method stub
			if (facebookCallback != null)
				facebookCallback.onFacebookError();
			setBar(View.INVISIBLE);
		}

		@Override
		public void onMalformedURLException(MalformedURLException arg0,
				Object arg1) {
			System.out
					.println("FBLogin.MyRequestListner.onMalformedURLException()");
			// TODO Auto-generated method stub
			if (facebookCallback != null)
				facebookCallback.onFacebookError();
			setBar(View.INVISIBLE);
		}

	}

	public interface FacebookCallback {
		void onFacebookLogin();

		void onFacebookLogout();

		void onFacebookError();
	}

}