package com.illuminz.geniusapp;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.illuminz.animation.AnimationLayout;
import com.illuminz.animation.FloatingActionMenu;
import com.illuminz.animation.FloatingActionMenu.MenuStateChangeListener;
import com.illuminz.animation.SubActionButton;
import com.illuminz.database.DatabaseManager;
import com.illuminz.utilities.Constants;
import com.illuminz.utilities.MyApplication;
import com.illuminz.utilities.MyUIApplication;
import com.illuminz.utilities.SyncQuestionService;
import com.illuminz.utilities.UtilityClass;

public class RoomActivity extends BaseActivity implements OnClickListener {
	
	Activity activity = RoomActivity.this;
	static Activity mActivity;
	Context context;
	private int deviceHeight;
	private int deviceWidth;
	private static int deviceH;
	private RelativeLayout rlayout_main ;
	private ImageView imgView_room_bg ;
	Dialog selectionDialog;
	RelativeLayout main_layout, rLayout_main1, rLayout_line;
	LinearLayout rLayout_Button;
	TextView textView_displayMsg;
	Button btn_No, btn_Yes;
	ProgressDialog mProgressDialog;
	LayoutInflater inflater;
	String quiz_userid, quiz_username;
	String devideID = "";
	DatabaseManager db;
	
	ArrayList<HashMap<String, String>> questionList;
	FloatingActionMenu circleMenu;
	Button centerActionButton;
	Boolean open=true;
	static TextView one;
	static TextView two;
	static TextView three;
	static TextView four;
	static TextView five;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LinearLayout baseView = (LinearLayout) findViewById(R.id.base_layout);
		RelativeLayout inflatedLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.room_screen, null, false);
		baseView.addView(inflatedLayout);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new CustomButtonDemoFragment(activity)).commit();
		}
				
		getActionBar().hide();
		quiz_userid = UtilityClass.getQuizUserIdFromSharedPreference(activity);
		quiz_username = UtilityClass.getQuizUserNameFromSharedPreference(activity);
		devideID = UtilityClass.getDeviceIdFromSharedPreference(activity);
		context = getApplicationContext();
		db = new DatabaseManager(activity);
		getDeviceParams();
		initializeView();
		setViews();
		assignClicks();
	}
	
	
	/**
     * A placeholder fragment containing a simple view.
     */
    public class CustomButtonDemoFragment extends Fragment {

        public CustomButtonDemoFragment(Activity activity) {
        	mActivity = activity;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_menu_with_custom_action_button, container, false);

            // Our action button is this time just a regular view!
            centerActionButton = (Button) rootView.findViewById(R.id.centerActionButton);
            RelativeLayout.LayoutParams imgView_playButtonParams = (RelativeLayout.LayoutParams) centerActionButton.getLayoutParams();
    		imgView_playButtonParams.width = (int) (deviceH * .20f);
    		imgView_playButtonParams.height = (int) (deviceH * .20f);
    		centerActionButton.setLayoutParams(imgView_playButtonParams);
    		
    		// Add some items to the menu. They are regular views as well!
            one = new TextView(getActivity()); 
            one.setText("Single Player"); 
            one.setGravity(Gravity.CENTER_HORIZONTAL);
            one.setTextColor(getResources().getColor(R.color.white));
            one.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(one.getTypeface(),(int) (0.30f * .1f * deviceHeight)));
    		one.setBackgroundColor(Color.TRANSPARENT);
            Drawable icon1 = this.getResources().getDrawable(R.drawable.one);
            Bitmap bitmap1 = ((BitmapDrawable) icon1).getBitmap();
            Drawable da = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap1, (int) (deviceH * .13f), (int) (deviceH * .13f), true));
            one.setCompoundDrawablesWithIntrinsicBounds( null, null, null,da );
                        
            two = new TextView(getActivity()); 
            two.setText("One-O-One");
            two.setGravity(Gravity.CENTER_HORIZONTAL);
            two.setTextColor(getResources().getColor(R.color.white));
            two.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(two.getTypeface(),(int) (0.30f * .1f * deviceHeight)));
        	two.setBackgroundResource(android.R.drawable.btn_default_small);
            two.setBackgroundColor(Color.TRANSPARENT);
            Drawable icon2=this.getResources().getDrawable(R.drawable.two);
            Bitmap bitmap2 = ((BitmapDrawable) icon2).getBitmap();
            Drawable db = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap2, (int) (deviceH * .13f), (int) (deviceH * .13f), true));
            two.setCompoundDrawablesWithIntrinsicBounds( null, null, null,db );
         
            three = new TextView(getActivity()); 
            three.setText("Five Players");
            three.setGravity(Gravity.CENTER_HORIZONTAL);
            three.setTextColor(getResources().getColor(R.color.white));
            three.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(three.getTypeface(),(int) (0.30f * .1f * deviceHeight)));
        	three.setBackgroundResource(android.R.drawable.btn_default_small);
            three.setBackgroundColor(Color.TRANSPARENT);
            Drawable icon3=this.getResources().getDrawable(R.drawable.five);
            Bitmap bitmap3 = ((BitmapDrawable) icon3).getBitmap();
            Drawable dc = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap3, (int) (deviceH * .13f), (int) (deviceH * .13f), true));
            three.setCompoundDrawablesWithIntrinsicBounds( null, dc, null, null );
            
            four = new TextView(getActivity()); 
            four.setText("Ten Players"); 
            four.setGravity(Gravity.CENTER_HORIZONTAL);
            four.setTextColor(getResources().getColor(R.color.white));
            four.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(four.getTypeface(),(int) (0.30f * .1f * deviceHeight)));
        	four.setBackgroundResource(android.R.drawable.btn_default_small);
            four.setBackgroundColor(Color.TRANSPARENT);
            Drawable icon4 = this.getResources().getDrawable(R.drawable.ten);
            Bitmap bitmap4 = ((BitmapDrawable) icon4).getBitmap();
            Drawable dd = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap4, (int) (deviceH * .13f), (int) (deviceH * .13f), true));
            four.setCompoundDrawablesWithIntrinsicBounds( null,dd, null, null );
         
            five = new TextView(getActivity());
            five.setText("Twenty five Players");
            five.setGravity(Gravity.CENTER_HORIZONTAL);
            five.setTextColor(getResources().getColor(R.color.white));
            five.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(five.getTypeface(),(int) (0.30f * .1f * deviceHeight)));
        	five.setBackgroundResource(android.R.drawable.btn_default_small);
            five.setBackgroundColor(Color.TRANSPARENT);
            Drawable icon5 = this.getResources().getDrawable(R.drawable.twenty_five);
            Bitmap bitmap5 = ((BitmapDrawable) icon5).getBitmap();
            Drawable de = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap5, (int) (deviceH * .13f), (int) (deviceH * .13f), true));
            five.setCompoundDrawablesWithIntrinsicBounds( null, null, null,de);
                      
            FrameLayout.LayoutParams imgParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
	        one.setLayoutParams(imgParams);
            two.setLayoutParams(imgParams);
            three.setLayoutParams(imgParams);
            four.setLayoutParams(imgParams);
            five.setLayoutParams(imgParams);
                          
            @SuppressWarnings("unused")
			SubActionButton.Builder subBuilder = new SubActionButton.Builder(getActivity());
            circleMenu = new FloatingActionMenu.Builder(getActivity())
					.setStartAngle(271)// A whole circle!
					.setEndAngle(560)
					.setRadius((int) (deviceH * .20f))
					.addSubActionView(one)
					.addSubActionView(two)
					.addSubActionView(three)
					.addSubActionView(four)
					.addSubActionView(five)
					.attachTo(centerActionButton)
					.build();
            			
			MenuIcon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					menu = (AnimationLayout) findViewById(R.id.animation_layout);
					menu.toggleSidebar();
					open = menu.mOpened;

					if (open.equals(false)) {

						//circleMenu.close(true);
						new CountDownTimer(100, 100) {

							public void onTick(long millisUntilFinished) {

							}

							public void onFinish() {
								one.setVisibility(View.GONE);
								two.setVisibility(View.GONE);
								three.setVisibility(View.GONE);
								four.setVisibility(View.GONE);
								five.setVisibility(View.GONE);
							}
						}.start();

					} else if (open.equals(true)) {

						new CountDownTimer(300, 100) {

							public void onTick(long millisUntilFinished) {

							}

							public void onFinish() {
								one.setVisibility(View.VISIBLE);
								two.setVisibility(View.VISIBLE);
								three.setVisibility(View.VISIBLE);
								four.setVisibility(View.VISIBLE);
								five.setVisibility(View.VISIBLE);
							}
						}.start();

					}

				}

				
			});
			
			rowLayout6.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					menu = (AnimationLayout) findViewById(R.id.animation_layout);
					menu.toggleSidebar();
					open = menu.mOpened;

					if (open.equals(false)) {

						new CountDownTimer(100, 100) {

							public void onTick(long millisUntilFinished) {

							}

							public void onFinish() {
								one.setVisibility(View.GONE);
								two.setVisibility(View.GONE);
								three.setVisibility(View.GONE);
								four.setVisibility(View.GONE);
								five.setVisibility(View.GONE);
							}
						}.start();

					} else if (open.equals(true)) {

						new CountDownTimer(300, 100) {

							public void onTick(long millisUntilFinished) {

							}

							public void onFinish() {
								one.setVisibility(View.VISIBLE);
								two.setVisibility(View.VISIBLE);
								three.setVisibility(View.VISIBLE);
								four.setVisibility(View.VISIBLE);
								five.setVisibility(View.VISIBLE);
							}
						}.start();

					}	
				}
			});
			
			

            circleMenu.setStateChangeListener(new MenuStateChangeListener() {
				
				@Override
				public void onMenuOpened(FloatingActionMenu menu) {
					one.setVisibility(View.VISIBLE);
					two.setVisibility(View.VISIBLE);
					three.setVisibility(View.VISIBLE);
					four.setVisibility(View.VISIBLE);
					five.setVisibility(View.VISIBLE);
				    centerActionButton.setBackgroundResource(R.drawable.play_button_enable); 
				    imgView_room_bg.setImageResource(R.color.background_color);  
				}
				
				@Override
				public void onMenuClosed(FloatingActionMenu menu) {
					one.setVisibility(View.VISIBLE);
					two.setVisibility(View.VISIBLE);
					three.setVisibility(View.VISIBLE);
					four.setVisibility(View.VISIBLE);
					five.setVisibility(View.VISIBLE);
				    centerActionButton.setBackgroundResource(R.drawable.play_button); 
				    imgView_room_bg.setImageResource(R.drawable.icon_group);  
				}
			});
            
            
			one.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Constants.numberOfPlayers = 1;
					Constants.ChangeLayout = 1;
					PlayGame();
				}
			});
			two.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Constants.numberOfPlayers = 2;
					Constants.ChangeLayout = 2;
			    	PlayGame();
								
				}
			});
			three.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Constants.numberOfPlayers = 5;
					Constants.ChangeLayout = 2;
			    	PlayGame();
				}
			});
			four.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Constants.numberOfPlayers = 10;
					Constants.ChangeLayout = 2;
			    	PlayGame();
				}
			});
			five.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Constants.numberOfPlayers = 25;
					Constants.ChangeLayout = 2;
			    	PlayGame();
					
				}
			});

            return rootView;
        }
    }
	
	/** Get device's width and height **/
	private void getDeviceParams() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		deviceHeight = displaymetrics.heightPixels;
		deviceWidth = displaymetrics.widthPixels;
		deviceH  = displaymetrics.heightPixels;
	}
	
	/** Find references to the GUI objects **/
	private void initializeView() {
		rlayout_main = (RelativeLayout)findViewById(R.id.rlayout_main);	
		imgView_room_bg = (ImageView) findViewById(R.id.imgView_room_bg);
		imgView_room_bg.setImageResource(R.drawable.icon_group);  
	}
	
	/** Set the GUI objects **/
	private void setViews() {
		/** Set padding within GUI objects **/
		rlayout_main.setPadding((int) (deviceWidth * 0.02f), 0, (int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.02f));
	}

	/** Set button's onClick listener object **/
	private void assignClicks() {
			
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
	
	@Override
	public void onBackPressed() {
		forgotPassword();
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
		
		JSONObject jsonObject = new JSONObject();
		try {
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
			selectionDialog.dismiss();
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
	
	//============================================================================
	private void PlayGame() {
		JSONObject jsonObject = new JSONObject();
		try {

			System.out.println("user id>>>:"+quiz_userid+" number of players>>"+Constants.numberOfPlayers+"device id>>"+devideID);
			jsonObject.put("user_id", quiz_userid);
			jsonObject.put("numof_players", Constants.numberOfPlayers);
			jsonObject.put("device_id", devideID);
			jsonObject.put("device_type", "android");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MyApplication app = new MyApplication();
		if (app.isInternetAvailable(activity.getApplicationContext())) {
			new jsonPlayGame(activity, jsonObject).execute();
		} else {
			showPopup("Internet not available.");
		}
	}

	String mode = "";
	public class jsonPlayGame extends AsyncTask<Void, Void, Void> {
		JSONObject jsonObj;

		public jsonPlayGame(Activity activity, JSONObject jsonObject) {
			jsonObj = jsonObject;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(activity, "",	"Please wait...");
			mProgressDialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				String jsonText = jsonObj.toString();
				final HashMap<String, String> keyMap = new HashMap<String, String>();
				keyMap.put("functionName", "numofplayers");
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
							questionList = new ArrayList<HashMap<String, String>>();
							JSONObject jsonObject = new JSONObject(output);
							mode = jsonObject.getString("mode");
							String data= jsonObject.getString("data");
							JSONObject jsonObject1=new JSONObject(data);
							
							UtilityClass.setWaitingFlagInSharedPreference(activity, jsonObject1.getString("waitingFlag"));
							UtilityClass.setQuizUserIdInSharedPreference(activity, jsonObject1.getString("user_id"));
							UtilityClass.setQuizGameIDInSharedPreference(activity, jsonObject1.getString("game_id"));
							UtilityClass.setFlagSharedPreference(activity, String.valueOf(Constants.numberOfPlayers));
							//String flag=jsonObject1.getString("flag");
							
							JSONArray jsonMainNode = jsonObject.optJSONArray("quiz");
							if (jsonMainNode != null) {
								for (int i = 0; i < jsonMainNode.length(); i++) {
									JSONObject arrayObj = jsonMainNode.getJSONObject(i);
									String id = String.valueOf(i);
									String question = arrayObj.getString("question");
									String option_1 = arrayObj.getString("option_1");
									String option_2 = arrayObj.getString("option_2");
									String option_3 = arrayObj.getString("option_3");
									String option_4 = arrayObj.getString("option_4");
									String answer   = arrayObj.getString("answer");
									
									HashMap<String, String> questiondata = new HashMap<String, String>();
									// adding each child node to HashMap key => value
									questiondata.put("id", id);
									questiondata.put("question", question);
									questiondata.put("option_1", option_1);
									questiondata.put("option_2", option_2);
									questiondata.put("option_3", option_3);
									questiondata.put("option_4", option_4);
									questiondata.put("answer", answer);
																
									// adding question data to questionList 
									questionList.add(questiondata);
								}
							}
							
							if (questionList.size() > 0) {
								SyncQuestionService.syncQuestion(questionList, activity);
							}
												
							
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
			if (mode.equals("")) {
				showPopup("Oops ! Connection timeout !");
				mProgressDialog.dismiss();
			} else {
			if (mode.equals("success")) {
				/*if (Constants.ChangeLayout ==1) {
					Intent intent = new Intent(activity, PlayScreenActivtiy.class);
					intent.putExtra("waitingFlag", waitingFlag);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
					activity.finish();
				} else {*/
					Intent intent = new Intent(activity, JoiningRoomActivity.class);
					intent.putExtra("numberOfPlayers", Constants.numberOfPlayers);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
					activity.finish();
				/*}*/
				
				
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
