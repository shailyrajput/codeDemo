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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
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

public class LoginActivity extends Activity implements OnClickListener,	ConnectionCallbacks, OnConnectionFailedListener {
	Activity activity = LoginActivity.this;
	Context context;
	View view;
	private int deviceHeight;
	private int deviceWidth;
	private RelativeLayout rlayout_main, rlayout_edittxt, rlayout_password;
	private ImageView imgView_back, imgView_logo, imgView_login,imgView_facebook, imgView_googleplus, imgView_twitter, imgView_signup;
	private TextView txtView_login, txtView_show, txtView_forgotPassword, txtView_haveAccount;
	private EditText editTxt_email, editTxt_password;
	boolean isTextColorWhite = false;
	LayoutInflater inflater;
	
	private String loginSource = "";
	ProgressDialog mProgressDialog;
	String disp_name, disp_email, disp_id;
	GoogleCloudMessaging gcm;
	String regId, message;
	String devideID = "";
	String username, password, email;
	String id, name;

	Dialog selectionDialog;
	RelativeLayout main_layout, rLayout_main1, rlayout_forgotpassword;
	EditText editTxt_forgotpassword;
	ImageView imgView_Ok;

	// Facebook login varaiables
	@SuppressWarnings("unused")
	private UiLifecycleHelper uiHelper;
	@SuppressWarnings("unused")
	private Session currentSession;
	public static final String APP_ID = "769698609818018";
	Facebook facebook;
	AsyncFacebookRunner mAsyncRunner;

	// Google plus login variables
	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
	private ProgressDialog mConnectionProgressDialog;
	private PlusClient mPlusClient;
	private ConnectionResult mConnectionResult;
	String profile_picture;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);
		getActionBar().hide();
		context = getApplicationContext();
		username = UtilityClass.getEmailFromSharedPreference(activity);
		password = UtilityClass.getPasswordFromSharedPreference(activity);
		devideID = UtilityClass.getDeviceIdFromSharedPreference(activity);
		getDeviceParams();
		initializeView();
		setViews();

		// get gcm id for notication======================================
		gcm = GoogleCloudMessaging.getInstance(activity);
		registerInBackground();
		// Google plus====================================================
		mPlusClient = new PlusClient.Builder(this, this, this).setActions("http://schemas.google.com/AddActivity","http://schemas.google.com/BuyActivity").build();
		// to catch Exception NetworkONMainThreadException this exception is
		// getting in honeycomb and its hiegher operation syatem
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		assignClicks();
		mConnectionProgressDialog = new ProgressDialog(this);
		mConnectionProgressDialog.setMessage("Signing in...");
	}

	@Override
	protected void onStart() {
		super.onStart();
		System.out.println("LoginActivity.onStart()");
	}

	@Override
	protected void onStop() {
		super.onStop();
		mPlusClient.disconnect();
		System.out.println("LoginActivity.onStop()");
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
					regId = gcm.register(GcmConstants.GOOGLE_PROJECT_ID);
					Log.d("RegisterActivity", "registerInBackground - regId: "	+ regId);
					msg = "Device registered, registration ID=" + regId;
					devideID = regId;
					UtilityClass.setDeviceIDInSharedPreference(activity,devideID);

				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					Log.d("RegisterActivity", "Error: " + msg);
				}
				Log.d("RegisterActivity", "AsyncTask completed: " + msg);
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				//Toast.makeText(getApplicationContext(),	"Registered with GCM Server." + msg, Toast.LENGTH_LONG)	.show();
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
		// TODO Auto-generated method stub
		rlayout_main = (RelativeLayout) findViewById(R.id.rlayout_main);  
		rlayout_edittxt = (RelativeLayout) findViewById(R.id.rlayout_edittxt);
		rlayout_password = (RelativeLayout) findViewById(R.id.rlayout_password);
		imgView_back = (ImageView) findViewById(R.id.imgView_back);
		imgView_logo = (ImageView) findViewById(R.id.imgView_logo);
		imgView_signup = (ImageView) findViewById(R.id.imgView_signup);
		imgView_facebook = (ImageView) findViewById(R.id.imgView_facebook);
		imgView_googleplus = (ImageView) findViewById(R.id.imgView_googleplus);
		imgView_twitter = (ImageView) findViewById(R.id.imgView_twitter);
		imgView_login = (ImageView) findViewById(R.id.imgView_login);
		txtView_login = (TextView) findViewById(R.id.txtView_login);
		txtView_show = (TextView) findViewById(R.id.txtView_show);
		txtView_forgotPassword = (TextView) findViewById(R.id.txtView_forgotPassword);
		txtView_haveAccount = (TextView) findViewById(R.id.txtView_haveAccount);
		editTxt_email = (EditText) findViewById(R.id.editTxt_email);
		editTxt_password = (EditText) findViewById(R.id.editTxt_password);

		editTxt_email.setText(username);
		editTxt_password.setText(password);
	}

	/** Set the GUI objects **/
	private void setViews() {
		/** Set text size of GUI objects **/
		txtView_login.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txtView_login.getTypeface(),	(int) (0.40f * .1f * deviceHeight)));
		txtView_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txtView_show.getTypeface(),(int) (0.40f * .1f * deviceHeight)));
		txtView_forgotPassword.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txtView_forgotPassword.getTypeface(),(int) (0.40f * .1f * deviceHeight)));
		txtView_haveAccount.setTextSize(TypedValue.COMPLEX_UNIT_PX,	MyUIApplication.determineTextSize(txtView_haveAccount.getTypeface(),(int) (0.40f * .1f * deviceHeight)));
		editTxt_email.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(editTxt_email.getTypeface(),(int) (0.40f * .1f * deviceHeight)));
		editTxt_password.setTextSize(TypedValue.COMPLEX_UNIT_PX,MyUIApplication.determineTextSize(editTxt_password.getTypeface(),(int) (0.40f * .1f * deviceHeight)));

		/** Set height,width and margin of GUI objects **/
		RelativeLayout.LayoutParams imgView_backParams = (RelativeLayout.LayoutParams) imgView_back.getLayoutParams();
		imgView_backParams.width = (int) (deviceHeight * .08f);
		imgView_backParams.height = (int) (deviceHeight * .08f);
		imgView_back.setLayoutParams(imgView_backParams);

		RelativeLayout.LayoutParams imgView_logoParams = (RelativeLayout.LayoutParams) imgView_logo.getLayoutParams();
		imgView_logoParams.width = (int) (deviceHeight * .15f);
		imgView_logoParams.height = (int) (deviceHeight * .15f);
		imgView_logo.setLayoutParams(imgView_logoParams);

		RelativeLayout.LayoutParams txtView_loginParams = (RelativeLayout.LayoutParams) txtView_login.getLayoutParams();
		txtView_loginParams.setMargins(0, (int) (deviceWidth * 0.07f), 0, 0);
		txtView_login.setLayoutParams(txtView_loginParams);

		RelativeLayout.LayoutParams rlayout_edittxtParams = (RelativeLayout.LayoutParams) rlayout_edittxt.getLayoutParams();
		rlayout_edittxtParams.width = (int) (deviceWidth);
		rlayout_edittxtParams.height = (int) (deviceHeight * .18f);
		rlayout_edittxtParams.setMargins(0, (int) (deviceWidth * 0.04f), 0, 0);
		rlayout_edittxt.setLayoutParams(rlayout_edittxtParams);

		RelativeLayout.LayoutParams editTxt_emailParams = (RelativeLayout.LayoutParams) editTxt_email.getLayoutParams();
		editTxt_emailParams.width = (int) (deviceWidth);
		editTxt_emailParams.height = (int) (deviceHeight * .08f);
		editTxt_emailParams.setMargins(0, (int) (deviceHeight * 0.01f), 0, 0);
		editTxt_email.setLayoutParams(editTxt_emailParams);

		RelativeLayout.LayoutParams rlayout_passwordParams = (RelativeLayout.LayoutParams) rlayout_password.getLayoutParams();
		rlayout_passwordParams.width = (int) (deviceWidth);
		rlayout_passwordParams.height = (int) (deviceHeight * .08f);
		rlayout_password.setLayoutParams(rlayout_passwordParams);

		RelativeLayout.LayoutParams editTxt_passwordParams = (RelativeLayout.LayoutParams) editTxt_password.getLayoutParams();
		editTxt_passwordParams.height = (int) (deviceHeight * .08f);
		editTxt_password.setLayoutParams(editTxt_passwordParams);

		RelativeLayout.LayoutParams imgView_loginParams = (RelativeLayout.LayoutParams) imgView_login.getLayoutParams();
		imgView_loginParams.width = (int) (deviceWidth * .40f);
		imgView_loginParams.height = (int) (deviceHeight * .10f);
		imgView_loginParams.setMargins(0, (int) (deviceWidth * 0.05f), 0, 0);
		imgView_login.setLayoutParams(imgView_loginParams);

		RelativeLayout.LayoutParams txtView_forgotPasswordParams = (RelativeLayout.LayoutParams) txtView_forgotPassword.getLayoutParams();
		txtView_forgotPasswordParams.setMargins(0, (int) (deviceWidth * 0.05f),	0, 0);
		txtView_forgotPassword.setLayoutParams(txtView_forgotPasswordParams);

		LinearLayout.LayoutParams imgView_facebookParams = (LinearLayout.LayoutParams) imgView_facebook.getLayoutParams();
		imgView_facebookParams.width = (int) (deviceHeight * .15f);
		imgView_facebookParams.height = (int) (deviceHeight * .10f);
		imgView_facebookParams.setMargins(0, (int) (deviceWidth * 0.06f), 0, 0);
		imgView_facebook.setLayoutParams(imgView_facebookParams);

		LinearLayout.LayoutParams imgView_googleplusParams = (LinearLayout.LayoutParams) imgView_googleplus.getLayoutParams();
		imgView_googleplusParams.width = (int) (deviceHeight * .15f);
		imgView_googleplusParams.height = (int) (deviceHeight * .10f);
		imgView_googleplusParams.setMargins(0, (int) (deviceWidth * 0.06f), 0,0);
		imgView_googleplus.setLayoutParams(imgView_googleplusParams);

		LinearLayout.LayoutParams imgView_twitterParams = (LinearLayout.LayoutParams) imgView_twitter.getLayoutParams();
		imgView_twitterParams.width = (int) (deviceHeight * .15f);
		imgView_twitterParams.height = (int) (deviceHeight * .10f);
		imgView_twitterParams.setMargins(0, (int) (deviceWidth * 0.06f), 0, 0);
		imgView_twitter.setLayoutParams(imgView_twitterParams);

		RelativeLayout.LayoutParams txtView_haveAccountParams = (RelativeLayout.LayoutParams) txtView_haveAccount.getLayoutParams();
		txtView_haveAccountParams.setMargins(0, (int) (deviceWidth * 0.05f), 0,	0);
		txtView_haveAccount.setLayoutParams(txtView_haveAccountParams);

		RelativeLayout.LayoutParams imgView_signupParams = (RelativeLayout.LayoutParams) imgView_signup.getLayoutParams();
		imgView_signupParams.width = (int) (deviceWidth * .35f);
		imgView_signupParams.height = (int) (deviceHeight * .07f);
		imgView_signupParams.setMargins(0, (int) (deviceWidth * 0.04f), 0, 0);
		imgView_signup.setLayoutParams(imgView_signupParams);

		/** Set padding within GUI objects **/
		rlayout_main.setPadding((int) (deviceWidth * 0.02f),(int) (deviceWidth * 0.03f), (int) (deviceWidth * 0.02f),(int) (deviceWidth * 0.02f));
		editTxt_email.setPadding((int) (deviceWidth * 0.05f), 0,(int) (deviceWidth * 0.05f), 0);
		rlayout_password.setPadding((int) (deviceWidth * 0.05f), 0,	(int) (deviceWidth * 0.05f), 0);
		editTxt_password.setPadding(0, 0, (int) (deviceWidth * 0.05f), 0);
		txtView_show.setPadding(0, (int) (deviceWidth * 0.02f), 0,(int) (deviceWidth * 0.02f));
	}

	/** Set button's onClick listener object **/
	private void assignClicks() {
		imgView_back.setOnClickListener(this);
		imgView_signup.setOnClickListener(this);
		imgView_facebook.setOnClickListener(this);
		imgView_googleplus.setOnClickListener(this);
		imgView_twitter.setOnClickListener(this);
		imgView_login.setOnClickListener(this);
		txtView_show.setOnClickListener(this);
		txtView_forgotPassword.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgView_back:
			onBackPressed();
			break;
		case R.id.imgView_login:
			loginClick();
			break;
		case R.id.imgView_facebook:
			Constants.isBackOnActivity = 1;
			loginSource = "facebook";
			signFacebook();
			break;
		case R.id.imgView_googleplus:
			Constants.isBackOnActivity = 1;
			loginSource = "google plus";
			mPlusClient.connect();
			signGooglePlus();
			break;
		case R.id.imgView_twitter:
			Constants.isBackOnActivity = 1;
			loginSource = "twitter";
			signTwitterClick();
			break;
		case R.id.txtView_show:
			showPasswordClick();
			break;
		case R.id.txtView_forgotPassword:
			forgotPasswordClick();
			break;
		case R.id.imgView_signup:
			signupClick();
			break;
		case R.id.imgView_Ok:
			resetPasswordClick();
			break;
		default:
			break;
		}
	}

	
	// Login button click listner============================================
	private void loginClick() {
		if (!(editTxt_email.getText().toString().length() > 0)) {
			showPopup("Please enter email address.");
		} else if (!(editTxt_password.getText().toString().length() > 0)) {
			showPopup("Please enter password.");
		} else {
			String Mailaddress = editTxt_email.getText().toString();
			if (Mailaddress.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
					&& Mailaddress.length() > 0) {
				doActionForLogin("", "", editTxt_email.getText().toString());

			} else {
				showPopup("invalid email");
			}
		}
	}

	// Facebook button click listner============================================
	private void signFacebook() {
		facebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(facebook);
		if (!Facebook.isSessionValid()) {
			facebook.authorize(this, new String[] { "email", "publish_actions", "read_stream" }, new LoginDialogListener1());
		} else {
			getProfileInformation();
		}
	}

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

	class LoginDialogListener1 implements
			com.facebook.android.Facebook.DialogListener {

		public void onComplete(Bundle values) {
			try {

				getProfileInformation();

			} catch (Exception error) {
				Toast.makeText(LoginActivity.this, error.toString(),
						Toast.LENGTH_SHORT).show();
			}
		}

		public void onFacebookError(FacebookError error) {
			Toast.makeText(LoginActivity.this,
					"Something went wrong. Please try again.",
					Toast.LENGTH_LONG).show();
		}

		public void onError(DialogError error) {
			Toast.makeText(LoginActivity.this,
					"Something went wrong. Please try again.",
					Toast.LENGTH_LONG).show();
		}

		public void onCancel() {
			Toast.makeText(LoginActivity.this,
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
					mConnectionResult.startResolutionForResult(this,REQUEST_CODE_RESOLVE_ERR);
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
					result.startResolutionForResult(this,REQUEST_CODE_RESOLVE_ERR);
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

	// onActivityResult for Facebook, Google Plus, Twitter ===================
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

	// Register button click listner=========================================
	private void signupClick() {
		Intent intent = new Intent(activity, RegisterActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		activity.finish();
	}

	//Show Password click listner============================================
	private void showPasswordClick() {
		if (txtView_show.getText().toString().equals("Show")) {
			// show password
			txtView_show.setText("Hide");
			editTxt_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			editTxt_password.setSelection(editTxt_password.length());
		} else {
			// hide password
			txtView_show.setText("Show");
			editTxt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			editTxt_password.setSelection(editTxt_password.length());
		}
	}

	// Forgot button click listner===========================================
	private void forgotPasswordClick() {
		if (isTextColorWhite == true) { 
			txtView_forgotPassword.setTextColor(getResources().getColor(R.color.white));
			isTextColorWhite = false;
		} else {
			txtView_forgotPassword.setTextColor(getResources().getColor(R.color.black));
			isTextColorWhite = true;
		}
		forgotPassword();

	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(activity, MainActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
			jsonObject.put("password", editTxt_password.getText().toString());
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
								
								System.out.println("ID ---> "+id + "NAME -----"+name);
								UtilityClass.setQuizUserIdInSharedPreference(activity, jsonObject2.getString("id"));
								UtilityClass.setQuizUserNameInSharedPreference(activity,jsonObject2.getString("name"));
								UtilityClass.setEmailInSharedPreference(activity,(editTxt_email.getText().toString()));
								UtilityClass.setPasswordInSharedPreference(activity, (editTxt_password.getText().toString()));

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

	private void forgotPassword() {
		selectionDialog = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
		selectionDialog.getWindow().getAttributes().dimAmount = 0.7f;
		selectionDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		selectionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		selectionDialog.setCancelable(true);
		selectionDialog.setContentView(R.layout.forgot_password_dialog);
		selectionDialog.setCanceledOnTouchOutside(false);
		selectionDialog.show();
		// setting view
		main_layout = (RelativeLayout) selectionDialog.findViewById(R.id.main_layout);
		rLayout_main1 = (RelativeLayout) selectionDialog.findViewById(R.id.rLayout_main1);
		rlayout_forgotpassword = (RelativeLayout) selectionDialog.findViewById(R.id.rlayout_forgotpassword);
		editTxt_forgotpassword = (EditText) selectionDialog.findViewById(R.id.editTxt_forgotpassword);
		imgView_Ok = (ImageView) selectionDialog.findViewById(R.id.imgView_Ok);
		editTxt_forgotpassword.setTextSize(TypedValue.COMPLEX_UNIT_PX,MyUIApplication.determineTextSize(editTxt_forgotpassword.getTypeface(),(int) (0.40f * .1f * deviceHeight)));

		RelativeLayout.LayoutParams rlayout_forgotpasswordParams = (RelativeLayout.LayoutParams) rlayout_forgotpassword.getLayoutParams();
		rlayout_forgotpasswordParams.width = (int) (deviceWidth);
		rlayout_forgotpasswordParams.height = (int) (deviceHeight * .09f);
		rlayout_forgotpassword.setLayoutParams(rlayout_forgotpasswordParams);

		RelativeLayout.LayoutParams editTxt_forgotpasswordParams = (RelativeLayout.LayoutParams) editTxt_forgotpassword.getLayoutParams();
		editTxt_forgotpasswordParams.width = (int) (deviceWidth * .80f);
		editTxt_forgotpasswordParams.height = (int) (deviceHeight * .08f);
		editTxt_forgotpassword.setLayoutParams(editTxt_forgotpasswordParams);

		RelativeLayout.LayoutParams imgView_OkParams = (RelativeLayout.LayoutParams) imgView_Ok.getLayoutParams();
		imgView_OkParams.width = (int) (deviceWidth * .40f);
		imgView_OkParams.height = (int) (deviceHeight * .10f);
		imgView_OkParams.setMargins(0, (int) (deviceWidth * 0.05f), 0, 0);
		imgView_Ok.setLayoutParams(imgView_OkParams);

		rLayout_main1.setPadding((int) (deviceHeight * .08f),(int) (deviceHeight * .04f), (int) (deviceHeight * .08f),(int) (deviceHeight * .01f));
		main_layout.setPadding((int) (deviceHeight * .02f), 0,(int) (deviceHeight * .02f), 0);
		editTxt_forgotpassword.setPadding((int) (deviceWidth * 0.02f), 0,(int) (deviceWidth * 0.02f), 0);

		main_layout.setOnClickListener(this);
		imgView_Ok.setOnClickListener(this);

	}

	private void resetPasswordClick() {
		selectionDialog.cancel();
	}
	
}
