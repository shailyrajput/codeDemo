package com.illuminz.geniusapp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;
import com.illuminz.notification.GcmConstants;
import com.illuminz.utilities.Constants;
import com.illuminz.utilities.MyApplication;
import com.illuminz.utilities.MyUIApplication;
import com.illuminz.utilities.UtilityClass;

public class MainActivity extends Activity implements OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {
	Activity activity = MainActivity.this;
	Context context;
	private int deviceHeight;
	private int deviceWidth;
	private RelativeLayout rlayout_main;
	private ImageView imgView_logo, imgView_signup, imgView_facebook, imgView_googleplus, imgView_twitter, imgView_login;
	private TextView txtView_signWith, txtView_haveAccount;
	GoogleCloudMessaging gcm;
	String message;
	String devideID = "";
	LayoutInflater inflater;
    ProgressDialog mProgressDialog;
	private String loginSource = "";
	String disp_id, disp_name, disp_email;
	String id, name;
	
	//Facebook login varaiables
	@SuppressWarnings("unused")
	private UiLifecycleHelper uiHelper;
	@SuppressWarnings("unused")
	private Session currentSession;
	public static final String APP_ID = "769698609818018";
	Facebook facebook;
	AsyncFacebookRunner mAsyncRunner;
	
	 //Google plus login variables
    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
    private ProgressDialog mConnectionProgressDialog;
    private PlusClient mPlusClient;
    private ConnectionResult mConnectionResult;
 	String profile_picture;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().hide();
		context = getApplicationContext();
		getDeviceParams();
		initializeView();
		setViews();
		
		//get gcm id for notication======================================
		gcm = GoogleCloudMessaging.getInstance(activity);
		registerInBackground();
		//Google plus====================================================
		mPlusClient = new PlusClient.Builder(this, this, this).setActions("http://schemas.google.com/AddActivity","http://schemas.google.com/BuyActivity").build();
		// to catch Exception NetworkONMainThreadException this exception is getting in honeycomb and its hiegher operation syatem
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
			
		assignClicks();
		mConnectionProgressDialog = new ProgressDialog(this);
		mConnectionProgressDialog.setMessage("Signing in...");
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		System.out.println("MainActivity.onStart()");
	}

	@Override
	protected void onStop() {
		super.onStop();
		mPlusClient.disconnect();
		System.out.println("MainActivity.onStop()");
	}
	

	private void registerInBackground() {
	    new AsyncTask<Void, Void, String>() {
	      @Override
	      protected String doInBackground(Void... params) {
	        String msg = "";
	        try {
	          if (gcm == null) {
	            gcm = GoogleCloudMessaging.getInstance(activity);
	          }
	          devideID = gcm.register(GcmConstants.GOOGLE_PROJECT_ID);
	          msg = "Device registered, registration ID=" + devideID;
	          UtilityClass.setDeviceIDInSharedPreference(activity, devideID);
	                    
	        } catch (IOException ex) {
	          msg = "Error :" + ex.getMessage();
	          Log.d("RegisterActivity", "Error: " + msg);
	        }
	        Log.d("RegisterActivity", "AsyncTask completed: " + msg);
	        return msg;
	      }
	 
	      @Override
	      protected void onPostExecute(String msg) {
	       // Toast.makeText(getApplicationContext(),"Registered with GCM Server." + msg, Toast.LENGTH_LONG).show();
	      }
	    }.execute(null, null, null);
	  }
	/** Get device's width and height **/
	private void getDeviceParams() {
		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		deviceHeight = displaymetrics.heightPixels;
		deviceWidth = displaymetrics.widthPixels;
	}
	
	/** Find references to the GUI objects **/
	private void initializeView() {
		
		rlayout_main = (RelativeLayout)findViewById(R.id.rlayout_main);
		imgView_logo = (ImageView) findViewById(R.id.imgView_logo);
		imgView_signup = (ImageView) findViewById(R.id.imgView_signup);
		imgView_facebook = (ImageView) findViewById(R.id.imgView_facebook);
	    imgView_googleplus = (ImageView) findViewById(R.id.imgView_googleplus);
		imgView_twitter = (ImageView) findViewById(R.id.imgView_twitter);
		imgView_login = (ImageView) findViewById(R.id.imgView_login);
		txtView_signWith = (TextView) findViewById(R.id.txtView_signWith);
		txtView_haveAccount = (TextView) findViewById(R.id.txtView_haveAccount);
		
	}
	
	/** Set the GUI objects **/
	private void setViews() {
		/** Set text size of GUI objects **/
		txtView_signWith.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txtView_signWith.getTypeface(),(int) (0.40f * .1f * deviceHeight)));
		txtView_haveAccount.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txtView_haveAccount.getTypeface(),(int) (0.40f * .1f * deviceHeight)));

		/** Set height,width and margin of GUI objects **/
		RelativeLayout.LayoutParams imgView_logoParams = (RelativeLayout.LayoutParams) imgView_logo.getLayoutParams();
		imgView_logoParams.height = (int) (deviceHeight * .37f);
	    imgView_logo.setLayoutParams(imgView_logoParams);
		
		RelativeLayout.LayoutParams imgView_signupParams = (RelativeLayout.LayoutParams) imgView_signup.getLayoutParams();
		imgView_signupParams.width = (int) (deviceWidth * .40f );
		imgView_signupParams.height = (int) (deviceHeight * .10f);
		imgView_signupParams.setMargins(0, (int) (deviceWidth * 0.10f), 0, 0); 
		imgView_signup.setLayoutParams(imgView_signupParams);
				
		RelativeLayout.LayoutParams txtView_signWithParams = (RelativeLayout.LayoutParams) txtView_signWith.getLayoutParams();
		txtView_signWithParams.setMargins(0, (int) (deviceWidth * 0.07f), 0, 0);
		txtView_signWith.setLayoutParams(txtView_signWithParams);
		
		LinearLayout.LayoutParams imgView_facebookParams = (LinearLayout.LayoutParams) imgView_facebook.getLayoutParams();
		imgView_facebookParams.width = (int) (deviceHeight * .15f );
		imgView_facebookParams.height = (int) (deviceHeight * .10f);
		imgView_facebookParams.setMargins(0, (int) (deviceWidth * 0.03f), 0, 0);
		imgView_facebook.setLayoutParams(imgView_facebookParams);
				
		LinearLayout.LayoutParams imgView_googleplusParams = (LinearLayout.LayoutParams) imgView_googleplus.getLayoutParams();
		imgView_googleplusParams.width = (int) (deviceHeight * .15f );
		imgView_googleplusParams.height = (int) (deviceHeight * .10f);
		imgView_googleplusParams.setMargins(0, (int) (deviceWidth * 0.03f), 0, 0); 
		imgView_googleplus.setLayoutParams(imgView_googleplusParams);
		
		LinearLayout.LayoutParams imgView_twitterParams = (LinearLayout.LayoutParams) imgView_twitter.getLayoutParams();
		imgView_twitterParams.width = (int) (deviceHeight * .15f );
		imgView_twitterParams.height = (int) (deviceHeight * .10f);
		imgView_twitterParams.setMargins(0, (int) (deviceWidth * 0.03f), 0, 0); 
		imgView_twitter.setLayoutParams(imgView_twitterParams);
				
		RelativeLayout.LayoutParams txtView_haveAccountParams = (RelativeLayout.LayoutParams) txtView_haveAccount.getLayoutParams();
		txtView_haveAccountParams.setMargins(0, (int) (deviceWidth * 0.05f), 0, 0);
		txtView_haveAccount.setLayoutParams(txtView_haveAccountParams);
		
		RelativeLayout.LayoutParams imgView_loginParams = (RelativeLayout.LayoutParams) imgView_login.getLayoutParams();
		imgView_loginParams.width = (int) (deviceWidth * .35f );
		imgView_loginParams.height = (int) (deviceHeight * .07f);
		imgView_loginParams.setMargins(0, (int) (deviceWidth * 0.02f), 0, 0); 
		imgView_login.setLayoutParams(imgView_loginParams);
				
		/** Set padding within GUI objects **/
		rlayout_main.setPadding((int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.08f), (int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.02f));

	}

	/** Set button's onClick listener object **/
	private void assignClicks() {
		imgView_signup.setOnClickListener(this);
		imgView_facebook.setOnClickListener(this);
		imgView_googleplus.setOnClickListener(this);
		imgView_twitter.setOnClickListener(this);
		imgView_login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgView_signup:
			signupClick();
			break;
		case R.id.imgView_facebook:
			Constants.isBackOnActivity = 0;
			loginSource = "facebook";
			signFacebook();
			break;
		case R.id.imgView_googleplus:
			Constants.isBackOnActivity = 0;
			loginSource = "google plus";
			mPlusClient.connect();
			signGooglePlus();
			break;
		case R.id.imgView_twitter:
			Constants.isBackOnActivity = 0;
			loginSource = "twitter";
			signTwitterClick();
			break;
		case R.id.imgView_login:
			Constants.isBackOnActivity = 0;
			loginClick();
			break;
		default:
			break;
		}
	}
	
	// Register button click listner============================================
	private void signupClick() {
		Intent intent = new Intent(activity, RegisterActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		activity.finish();
	}
		
	// Facebook button click listner============================================
	private void signFacebook() {
		facebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(facebook);
		if (!Facebook.isSessionValid()) {
			facebook.authorize(this, new String[] { "email", "publish_actions",
					"read_stream" }, new LoginDialogListener1());
		} else {
			getProfileInformation();
		}
	}
	
	// Get facebook information===================^^^^^^^^^^^^^^^^^^=======================
	public void getProfileInformation() {

		try {

			JSONObject profile = Util.parseJson(facebook.request("me"));
			String mUserId = profile.getString("id");
			String mUserName = profile.getString("name");
			String mUserEmail = profile.getString("email");
			// mUserToken = facebook.getAccessToken();
			String gender = profile.getString("gender");
			System.out.println("gender------------>"+gender);
			try {
			  URL image_path = new URL("https://graph.facebook.com/"+ mUserId+ "/picture?type=large");
			  profile_picture = image_path.toString();
			  System.out.println("image::> " + profile_picture);
			 
			}
			catch (MalformedURLException e) {
			  e.printStackTrace();
			}
			
			doActionForLogin(mUserId, mUserName, mUserEmail);
			
		} catch (FacebookError e) {

			e.printStackTrace();
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (JSONException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (NetworkOnMainThreadException e) {
			e.printStackTrace();
		}

	}
		
	class LoginDialogListener1 implements com.facebook.android.Facebook.DialogListener {

		public void onComplete(Bundle values) {
			try {

				getProfileInformation();

			} catch (Exception error) {
				Toast.makeText(MainActivity.this, error.toString(),
						Toast.LENGTH_SHORT).show();
			}
		}

		public void onFacebookError(FacebookError error) {
			Toast.makeText(MainActivity.this,
					"Something went wrong. Please try again.",
					Toast.LENGTH_LONG).show();
		}

		public void onError(DialogError error) {
			Toast.makeText(MainActivity.this,
					"Something went wrong. Please try again.",
					Toast.LENGTH_LONG).show();
		}

		public void onCancel() {
			Toast.makeText(MainActivity.this,
					"Something went wrong. Please try again.",
					Toast.LENGTH_LONG).show();
		}
	}

	
	// Google plus button click listner============================================
	private void signGooglePlus() {

		if (!mPlusClient.isConnected()) {
			if (mConnectionResult == null) {
				mConnectionProgressDialog.show();
			} else {
				try {
					mConnectionResult.startResolutionForResult(this,
							REQUEST_CODE_RESOLVE_ERR);

				} catch (SendIntentException e) {
					// Try connecting again.
					mConnectionResult = null;
					mPlusClient.connect();

				}
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (mConnectionProgressDialog.isShowing()) {
			// The user clicked the sign-in button already. Start to resolve
			// connection errors. Wait until onConnected() to dismiss the
			// connection dialog.
			System.out.println("LoginActivity.onConnectionFailed()");
			if (result.hasResolution()) {
				try {
					result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
				} catch (SendIntentException e) {
					mPlusClient.connect();
				}
			}
		}
		// Save the result and resolve the connection failure upon a user click.
		mConnectionResult = result;
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		mConnectionProgressDialog.dismiss();
		Person currentPerson = mPlusClient.getCurrentPerson();
		String id = currentPerson.getId();	
		String name = currentPerson.getDisplayName();
		String email = mPlusClient.getAccountName();
		
		profile_picture = currentPerson.getImage().getUrl();
		int gender = currentPerson.getGender();
		//System.out.println("currentPerson>>>>>>>>>>>>>>>>>>>>>"+currentPerson);
		System.out.println("gender>>>>>>>>>>>>>>>>>>>>>"+gender);
		System.out.println("profile_pic>>>>>>>>>>>>>>>>>>>>>"+profile_picture);
	
		doActionForLogin(id, name, email);
	}

	@Override
	public void onDisconnected() {
		System.out.println("MainActivity.onDisconnected()");
		
	}
	
	// Twitter button click listner============================================
	private void signTwitterClick() {

		
	}

	// onActivityResult for Facebook, Google Plus, Twitter ============================================
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (loginSource.equals("facebook")) {
			facebook.authorizeCallback(requestCode, resultCode, data);
			
		} else if (loginSource.equals("google plus")) {
			if (requestCode == REQUEST_CODE_RESOLVE_ERR	&& resultCode == RESULT_OK) {
				mConnectionResult = null;
				mPlusClient.connect();
			}
		} 
	}
	
	// Login button click listner============================================
	private void loginClick() {
		Intent intent = new Intent(activity, LoginActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		activity.finish();

	}
	
	
	//Call Json Service here ============================================
		private void doActionForLogin(String getId, String getName, String getEmail) {
			JSONObject jsonObject = new JSONObject();
			disp_id = getId;
			disp_name = getName;
			disp_email =  getEmail;
			try {
				System.out.println("login source >>>> : " +loginSource);
				System.out.println("disp_id      >>>> : " +disp_id);
				System.out.println("disp_name    >>>> : " +disp_name);
				System.out.println("disp_email   >>>> : " +disp_email);
				System.out.println("devideID     >>>> : " +devideID);
		
				
				jsonObject.put("login_source", loginSource);
				jsonObject.put("source_id", disp_id);
				jsonObject.put("user_name", disp_name);
				jsonObject.put("email_id", disp_email);
				jsonObject.put("password", "");
				jsonObject.put("device_id", devideID);
				jsonObject.put("device_type", "android");
				
				jsonObject.put("profile_photo", profile_picture);
				jsonObject.put("dob", "2015-11-13");
				jsonObject.put("gender", "Male");
					
				Constants.LOGIN_SOURCE = loginSource;

			} catch (JSONException e) {
				e.printStackTrace();
			}
			MyApplication app = new MyApplication();
			if (app.isInternetAvailable(activity.getApplicationContext())) {
				new jsonLogin(activity, jsonObject).execute();

			} else {
				showPopup("Internet not available.");
			}
		}

		String mode = "";

		public class jsonLogin extends AsyncTask<Void, Void, Void> {
			JSONObject jsonObj;

			public jsonLogin(Activity activity, JSONObject jsonObject) {
				jsonObj = jsonObject;
			}

			@Override
			protected void onPreExecute() {
				
				super.onPreExecute();
				mProgressDialog = ProgressDialog.show(activity, "",	"Please wait...");
				mProgressDialog.setCancelable(false);
			}

			@Override
			protected Void doInBackground(Void... params) {
			
				try {
					String jsonText = jsonObj.toString();
					final HashMap<String, String> keyMap = new HashMap<String, String>();
					keyMap.put("functionName", "quiz_login");
					keyMap.put("json", jsonText);
					try {
						HttpURLConnection connection = null;
						DataOutputStream outputStream = null;
						String lineEnd = "\r\n";
						String twoHyphens = "--";
						String boundary = "*****";
						try {

							URL url = new URL(Constants.URL);
							connection = (HttpURLConnection) url.openConnection();

							// Allow Inputs & Outputs
							connection.setDoInput(true);
							connection.setDoOutput(true);
							connection.setUseCaches(false);
							// Enable POST method
							connection.setRequestMethod("POST");
							connection.setConnectTimeout(Constants.milliseconds);
							connection.setReadTimeout(Constants.milliseconds);

							connection.setRequestProperty("Connection",	"Keep-Alive");
							connection.setRequestProperty("ENCTYPE","multipart/form-data");
							connection.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);
							outputStream = new DataOutputStream(connection.getOutputStream());
							outputStream.writeBytes(twoHyphens + boundary + lineEnd);
							Set<String> keyArray = keyMap.keySet();
							for (String keyName : keyArray) {
								String value = keyMap.get(keyName);
								outputStream.writeBytes("Content-Disposition: form-data; name=\""
												+ keyName
												+ "\""
												+ lineEnd
												+ lineEnd);
								outputStream.writeBytes(value);
								outputStream.writeBytes(lineEnd);
								outputStream.writeBytes(twoHyphens + boundary + lineEnd);

							}
							outputStream.writeBytes(lineEnd);
							outputStream.flush();
							InputStream is = connection.getInputStream();
							// retrieve the response from server
							int ch;
							StringBuffer b = new StringBuffer();
							while ((ch = is.read()) != -1) {
								b.append((char) ch);
							}
							String output = b.toString();
							System.out.println("out String is :-" + output);
							// get Data data here___________________________
							try {
								JSONObject jsonObject = new JSONObject(output);
								mode = jsonObject.getString("mode");

								if (mode.equals("success")) {
									String data = jsonObject.getString("data");
									JSONObject jsonObject2 = new JSONObject(data);
									id = jsonObject2.getString("id");
									name = jsonObject2.getString("name");
									
									UtilityClass.setQuizUserIdInSharedPreference(activity, jsonObject2.getString("id"));
									UtilityClass.setQuizUserNameInSharedPreference(activity,jsonObject2.getString("name"));
									UtilityClass.setEmailInSharedPreference(activity,"");
									UtilityClass.setPasswordInSharedPreference(activity, "");

								} else {
									String data = jsonObject.getString("data");
									JSONObject jsonObject2 = new JSONObject(data);
									message = jsonObject2.getString("message");
								}
							} catch (Throwable t) {
								t.printStackTrace();
							}

						} catch (java.net.SocketTimeoutException e) {
							System.out.println("error is occured 1 :-"	+ e.getMessage());

						} catch (java.io.IOException e) {
							System.out.println("error is occured 2 :-"	+ e.getMessage());
						}
						
					} catch (Exception e) {
						System.out.println("error is occured 2 :-" + e.getMessage());
					}
				} catch (Exception e) {
					System.out.println("error is occured in file sending over server :-" + e.getMessage());
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (mode.equals("")) {
					showPopup("Oops ! Connection timeout !");
					mProgressDialog.dismiss();
				} else {
					if (mode.equals("success")) {
						Intent intent = new Intent(activity, RoomActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
						activity.finish();
						UtilityClass.setFlagSharedPreference(activity, "0");
						mProgressDialog.dismiss();
					} else {
						showPopup(message);
						mProgressDialog.dismiss();

					}
				}
			}
		}

		public void showPopup(String msg) {
			inflater = getLayoutInflater();
			View customToastroot = inflater.inflate(R.layout.mycustom_toast, null);
			Toast customtoast = new Toast(context);
			TextView textView = (TextView) customToastroot.findViewById(R.id.textView1);
			textView.setText(msg);
			customtoast.setView(customToastroot);
			customtoast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
			customtoast.setDuration(Toast.LENGTH_LONG);
			customtoast.show();
		}
}
