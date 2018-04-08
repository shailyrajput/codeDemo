package com.illuminz.geniusapp;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.illuminz.animation.AnimationLayout;
import com.illuminz.utilities.Constants;
import com.illuminz.utilities.MyApplication;
import com.illuminz.utilities.MyUIApplication;
import com.illuminz.utilities.UtilityClass;

public class BaseActivity extends Activity implements OnClickListener {
	Activity activity = BaseActivity.this;
	Context context;
	public static AnimationLayout menu;
	public RelativeLayout animation_layout_sidebar, TopBar, rowLayout0,
			rowLayout1, rowLayout2, rowLayout3, rowLayout4, rowLayout5;
	public TextView Title, title1, title2, title3, title4, title5;
	public ImageView MenuIcon;
	public static RelativeLayout rowLayout6;
	public int deviceHeight;
	public int deviceWidth;
	private TextView fakeView1;
	Dialog selectionDialog;
	RelativeLayout main_layout, rLayout_main1, rLayout_line;
	LinearLayout rLayout_Button;
	TextView textView_displayMsg;
	Button btn_No, btn_Yes;
	ProgressDialog mProgressDialog;
	LayoutInflater inflater;
	String quiz_userid;
	String devideID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		quiz_userid = UtilityClass.getQuizUserIdFromSharedPreference(activity);
		devideID = UtilityClass.getDeviceIdFromSharedPreference(activity);
		context = getApplicationContext();
		getDeviceParams();
		BaseInitialization();
		setViews();
		assignClicks();
	}

	private void getDeviceParams() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		deviceHeight = displaymetrics.heightPixels;
		deviceWidth = displaymetrics.widthPixels;
	}
	
	public void BaseInitialization() 
	{
		animation_layout_sidebar = (RelativeLayout) findViewById(R.id.animation_layout_sidebar);
		TopBar = (RelativeLayout) findViewById(R.id.TopBar);
		rowLayout6 = (RelativeLayout) findViewById(R.id.rowLayout6);
		Title = (TextView) findViewById(R.id.title);
		MenuIcon = (ImageView) findViewById(R.id.menuIcon);
		rowLayout0  = (RelativeLayout) findViewById(R.id.rowLayout0);
		rowLayout1  = (RelativeLayout) findViewById(R.id.rowLayout1);
		rowLayout2  = (RelativeLayout) findViewById(R.id.rowLayout2);
		rowLayout3  = (RelativeLayout) findViewById(R.id.rowLayout3);
		rowLayout4  = (RelativeLayout) findViewById(R.id.rowLayout4);
		rowLayout5  = (RelativeLayout) findViewById(R.id.rowLayout5);
		title1 = (TextView) findViewById(R.id.title1);
		title2 = (TextView) findViewById(R.id.title2);
		title3 = (TextView) findViewById(R.id.title3);
		title4 = (TextView) findViewById(R.id.title4);
		title5 = (TextView) findViewById(R.id.title5);
		
		fakeView1 = (TextView) findViewById(R.id.fakeView1);
		
		rowLayout1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent profile_Intent = new Intent(activity, ProfileActivity.class);
				startActivity(profile_Intent);
				overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
				activity.finish();
			}
		});
		
		rowLayout2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent profile_Intent = new Intent(activity, UpdateProfileOnBaseActivity.class);
				startActivity(profile_Intent);
				overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
				activity.finish();
			}
		});
		
       rowLayout3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent profile_Intent = new Intent(activity, SearchFriendActivity.class);
				startActivity(profile_Intent);
				overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
				activity.finish();	
			}
		});

       rowLayout4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent friend_request_Intent = new Intent(activity, FriendsRequestListActivity.class);
				startActivity(friend_request_Intent);
				overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
				activity.finish();	
			}
		});
       rowLayout5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent friend_request_Intent = new Intent(activity, FriendsListActivity.class);
				startActivity(friend_request_Intent);
				overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
				activity.finish();	
				
			}
		});
	
	}
	
	private void setViews() {
		
		Title.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(Title.getTypeface(),(int) (0.55f * .1f * deviceHeight)));
		title1.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(title1.getTypeface(),(int) (0.35f * .1f * deviceHeight)));
		title2.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(title2.getTypeface(),(int) (0.35f * .1f * deviceHeight)));
		title3.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(title3.getTypeface(),(int) (0.35f * .1f * deviceHeight)));
		title4.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(title4.getTypeface(),(int) (0.35f * .1f * deviceHeight)));
		title5.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(title5.getTypeface(),(int) (0.35f * .1f * deviceHeight)));
		rowLayout0.getLayoutParams().height = (int) (deviceHeight * .1f);
		RelativeLayout.LayoutParams menuIconParams = (RelativeLayout.LayoutParams) MenuIcon.getLayoutParams();
		menuIconParams.height = (int)(deviceHeight * .08f);
		menuIconParams.width = (int)(deviceHeight * .08f);
		MenuIcon.setLayoutParams(menuIconParams);
		RelativeLayout.LayoutParams fakeView1Param = (RelativeLayout.LayoutParams) fakeView1.getLayoutParams();
		fakeView1Param.height = (int)(deviceHeight * .08f);
		fakeView1.setLayoutParams(fakeView1Param);
		TopBar.setPadding((int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.03f), (int) (deviceWidth * 0.02f),(int) (deviceWidth * 0.0f));
		rowLayout1.setPadding(0, (int) (deviceWidth * 0.03f), (int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.03f));
		rowLayout2.setPadding(0, (int) (deviceWidth * 0.03f), (int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.03f));
		rowLayout3.setPadding(0, (int) (deviceWidth * 0.03f), (int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.03f));
		rowLayout4.setPadding(0, (int) (deviceWidth * 0.03f), (int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.03f));
		rowLayout5.setPadding(0, (int) (deviceWidth * 0.03f), (int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.03f));
		
	}

	public void menuBotton(View view)
	{
		menu = (AnimationLayout) findViewById(R.id.animation_layout);
		menu.toggleSidebar();
	}

	public void menuBotton1(View view)
	{
		menu = (AnimationLayout) findViewById(R.id.animation_layout);
		menu.toggleSidebar();
	}
	
	
	/** Set button's onClick listener object **/
	private void assignClicks() {
		//rowLayout5.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btn_No:
			onCancelDialogClick();
			break;
		case R.id.btn_Yes:
			onLogoutClick();
			break;
		default:
			break;
		}
	}
	
	
	private void forgotPassword() {
		selectionDialog = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
		selectionDialog.getWindow().getAttributes().dimAmount = 0.7f;
		selectionDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		selectionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		selectionDialog.setCancelable(true);
		selectionDialog.setContentView(R.layout.logout_popup);
		selectionDialog.setCanceledOnTouchOutside(false);
		selectionDialog.show();
		// setting view
		
		main_layout = (RelativeLayout) selectionDialog.findViewById(R.id.main_layout);
		rLayout_main1 = (RelativeLayout) selectionDialog.findViewById(R.id.rLayout_main1);
		rLayout_line = (RelativeLayout) selectionDialog.findViewById(R.id.rLayout_line);
		rLayout_Button = (LinearLayout) selectionDialog.findViewById(R.id.rLayout_Button);
		textView_displayMsg = (TextView) selectionDialog.findViewById(R.id.textView_displayMsg);
		btn_No = (Button) selectionDialog.findViewById(R.id.btn_No);
		btn_Yes = (Button) selectionDialog.findViewById(R.id.btn_Yes);
				
		textView_displayMsg.setTextSize(TypedValue.COMPLEX_UNIT_PX,MyUIApplication.determineTextSize(textView_displayMsg.getTypeface(),(int) (0.40f * .1f * deviceHeight)));
		btn_No.setTextSize(TypedValue.COMPLEX_UNIT_PX,MyUIApplication.determineTextSize(btn_No.getTypeface(),(int) (0.40f * .1f * deviceHeight)));
		btn_Yes.setTextSize(TypedValue.COMPLEX_UNIT_PX,MyUIApplication.determineTextSize(btn_Yes.getTypeface(),(int) (0.40f * .1f * deviceHeight)));

		RelativeLayout.LayoutParams rLayout_main1Params = (RelativeLayout.LayoutParams) rLayout_main1.getLayoutParams();
		rLayout_main1Params.width = (int) (deviceWidth * .80f);
		rLayout_main1.setLayoutParams(rLayout_main1Params);

		textView_displayMsg.setPadding((int) (deviceHeight * .05f),(int) (deviceHeight * .03f), (int) (deviceHeight * .05f),(int) (deviceHeight * .03f));
		rLayout_Button.setPadding((int) (deviceHeight * .02f),(int) (deviceHeight * .02f),(int) (deviceHeight * .02f),(int) (deviceHeight * .02f));
		
		btn_No.setOnClickListener(this);
		btn_Yes.setOnClickListener(this);
	}

	private void onCancelDialogClick() {
		selectionDialog.dismiss();
	}

		
    private void onLogoutClick() {
    	//selectionDialog.dismiss();
		JSONObject jsonObject = new JSONObject();
		try {
			
			System.out.println("quiz_userid >>: "+quiz_userid+" devideID >> :"+devideID);
			jsonObject.put("user_id", quiz_userid);
			jsonObject.put("device_id", devideID);
			jsonObject.put("device_type", "android");
		  
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MyApplication app = new MyApplication();
		if (app.isInternetAvailable(activity.getApplicationContext())) {
			new jsonLogout(activity, jsonObject).execute();
		} else {
			showPopup("Internet not available.");
		}
	}
	
	
	String mode1 = "";
	public class jsonLogout extends AsyncTask<Void, Void, Void> {
		JSONObject jsonObj;

		public jsonLogout(Activity activity, JSONObject jsonObject) {
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
				keyMap.put("functionName", "logout");
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
							mode1 = jsonObject.getString("mode");
										
						} catch (Throwable t) {
							t.printStackTrace();
						}

					} catch (java.net.SocketTimeoutException e) {
						System.out.println("error is occured 1 :-" + e.getMessage());
						
					} catch (java.io.IOException e) {
						System.out.println("error is occured 2 :-" + e.getMessage());
					}
				} catch (Exception e) {
					System.out.println("error is occured :-" + e.getMessage());
				}
			} catch (Exception e) {
				System.out.println("error is occured in file sending over server :-" + e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (mode1.equals("")) {
				showPopup("Oops ! Connection timeout !");
				mProgressDialog.dismiss();
			} else {
			if (mode1.equals("success")) {
				UtilityClass.removeEmailFromSharedPreference(activity);
				UtilityClass.removeQuizUserNameFromSharedPreference(activity);
				UtilityClass.removePasswordFromSharedPreference(activity);
				Intent logout_intent = new Intent(activity, LoginActivity.class);
			    startActivity(logout_intent);
			    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_in_left);
			    activity.finish();
			  
				mProgressDialog.dismiss();
			} else {
				showPopup("Something is wrong !");
				mProgressDialog.dismiss();
			}
			}
		}
	}
	
	
   public void showPopup(String msg) {
		
		inflater=getLayoutInflater();
		View customToastroot =inflater.inflate(R.layout.mycustom_toast, null);
		Toast customtoast=new Toast(context);
		TextView textView = (TextView)customToastroot.findViewById(R.id.textView1);
		textView.setText(msg);
		customtoast.setView(customToastroot);
		customtoast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0, 0);
		customtoast.setDuration(Toast.LENGTH_LONG);
		customtoast.show();
	}
		
}
