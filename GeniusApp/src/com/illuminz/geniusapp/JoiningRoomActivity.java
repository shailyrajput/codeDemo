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
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.illuminz.adapter.JoiningRoomAdapter;
import com.illuminz.utilities.Constants;
import com.illuminz.utilities.MyApplication;
import com.illuminz.utilities.MyUIApplication;
import com.illuminz.utilities.UtilityClass;

public class JoiningRoomActivity extends Activity implements OnClickListener {
	Activity activity = JoiningRoomActivity.this;
	Context context;
	RelativeLayout rlayout_main ,rlayout_middleView,rlayout_gridView,rlayout_bottombView;
	ImageView imgView_back, imgView_logo ,image_g,img_start;
	TextView textView_players,txt_please_wait,txt_joining_player;
	GridView gridView;
	int deviceHeight,deviceWidth;
	JoiningRoomAdapter  joiningRoomAdapter;
	int[] imageId = { R.drawable.dp, R.drawable.dp1,R.drawable.dp2, R.drawable.dp, R.drawable.dp1,
			R.drawable.dp2, R.drawable.dp, R.drawable.dp1, R.drawable.dp2, R.drawable.mask_copy };
	
	String quiz_userid; 
	String devideID = "";
	String waitingFlag;
	BroadcastReceiver myBroadcast;
	android.os.CountDownTimer Count;
	
	boolean isBackGame = false;
	ProgressDialog mProgressDialog;
	LayoutInflater inflater;
	//int numberOfPlayers;
	
	Dialog selectionDialog;
	RelativeLayout main_layout, rLayout_main1, rLayout_line;
	LinearLayout rLayout_Button;
	TextView textView_displayMsg;
	Button btn_No, btn_Yes;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.joining_room_screen);
		quiz_userid = UtilityClass.getQuizUserIdFromSharedPreference(activity);
		devideID = UtilityClass.getDeviceIdFromSharedPreference(activity);
		waitingFlag = UtilityClass.getWaitingFlagFromSharedPreference(activity);
		getActionBar().hide();
		getDeviceParams();
		initializeView();
		setViews();
		assignClicks();
		setAdapter();
	
		if (Constants.ChangeLayout==1) {
			//db = new DatabaseManager(activity);
			//getDataFromDataBase();
		} else {
			System.out.println("MainActivity.onCreate()"+waitingFlag);
			if (waitingFlag.equals("0")) {
				CreateBroadcast1();
			} else {
				CreateBroadcast();
				activity.registerReceiver(myBroadcast,new IntentFilter("com.illuminz.quizdemo.CHAT_DATA"));
			}
		
		}
		
		
	 /* new Thread(new Runnable() {
			public void run() {

				try {
					for (int i = 500; i > 0; i--) {
						// Let the thread sleep for a while.
						Thread.sleep(1000);
						System.out.println("aaaaaaaaaaaaaaaaaa-----11111---------->");
					}
				} catch (InterruptedException e) {
					System.out.println("Thread " + " interrupted.");
				}
			
			}
		}).start();*/
	}

	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (waitingFlag.equals("1")) {
		if (Constants.ChangeLayout==2) {
			activity.registerReceiver(myBroadcast,new IntentFilter("com.illuminz.quizdemo.CHAT_DATA"));
			System.out.println("MainActivity.onResume()");
		}
		}
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (waitingFlag.equals("1")) {
		if (Constants.ChangeLayout==2) {
		activity.unregisterReceiver(myBroadcast);
		System.out.println("MainActivity.onPause()");
		}
		}
	}
		
	public void CreateBroadcast() {
		myBroadcast = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				final String strVal = intent.getExtras().getString("msg_data");
				System.out.println("Notification data received :" + strVal );
				isBackGame = true;
				Count = new CountDownTimer(7000, 1000) {
					public void onTick(long millisUntilFinished) {
						txt_please_wait.setText(""+((millisUntilFinished / 1000) - 1));
						if ((millisUntilFinished / 1000) == 1) {
							// Inserting delay here
							try {
								Thread.sleep(1);
								txt_please_wait.setText("0");
								Intent intent = new Intent(activity, PlayScreenActivtiy.class);
								startActivity(intent);
								clearNotification();
								overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
								activity.finish();
								Count.cancel();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					public void onFinish() {

					}
				}.start();
			}

		};

	}
	
	public void CreateBroadcast1() {

		isBackGame = true;
		Count = new CountDownTimer(7000, 1000) {
			public void onTick(long millisUntilFinished) {
				txt_please_wait.setText("" + ((millisUntilFinished / 1000) - 1));
				if ((millisUntilFinished / 1000) == 1) {
					// Inserting delay here
					try {
						Thread.sleep(1);
						txt_please_wait.setText("0");
						Intent intent = new Intent(activity,PlayScreenActivtiy.class);
						startActivity(intent);
						clearNotification();
						overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
						activity.finish();
						Count.cancel();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			public void onFinish() {

			}
		}.start();

	}

	public void clearNotification() {
		NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(1);
	}
	
	

	private void getDeviceParams() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		deviceHeight = displaymetrics.heightPixels;
		deviceWidth = displaymetrics.widthPixels;

	}

	private void initializeView() {
		rlayout_main = (RelativeLayout) findViewById(R.id.rlayout_main);
		rlayout_middleView =(RelativeLayout) findViewById(R.id.rlayout_middleView);
		rlayout_gridView = (RelativeLayout) findViewById(R.id.rlayout_gridView);
		imgView_back = (ImageView) findViewById(R.id.imgView_back);
		imgView_logo = (ImageView) findViewById(R.id.imgView_logo);
		image_g = (ImageView) findViewById(R.id.image_g);
		img_start = (ImageView) findViewById(R.id.img_start);

		textView_players = (TextView) findViewById(R.id.textView_players);
		txt_please_wait = (TextView) findViewById(R.id.txt_please_wait);
		txt_joining_player = (TextView) findViewById(R.id.txt_joining_player);
		gridView = (GridView) findViewById(R.id.gridView);
		
		
		if (Constants.numberOfPlayers == 1) {
			txt_please_wait.setVisibility(View.GONE);
			txt_joining_player.setText("Ready to Play");
			rlayout_gridView.setVisibility(View.GONE);
			textView_players.setText(Constants.numberOfPlayers+" Player");
			
			
			RelativeLayout.LayoutParams image_gParams = (RelativeLayout.LayoutParams) image_g.getLayoutParams();
			image_gParams.width = (int) (deviceHeight * .22f);
			image_gParams.height = (int) (deviceHeight * .22f);
			image_gParams.setMargins(0, (int) (deviceHeight * 0.13f), 0, 0); 
			image_g.setLayoutParams(image_gParams);
			
			RelativeLayout.LayoutParams img_startParams = (RelativeLayout.LayoutParams) img_start.getLayoutParams();
			img_startParams.width = (int) (deviceHeight * .15f);
			img_startParams.height = (int) (deviceHeight * .15f);
			img_startParams.setMargins(0, 0, 0, (int) (deviceHeight * 0.13f)); 
			img_start.setLayoutParams(img_startParams);
			
		} else {
			txt_please_wait.setVisibility(View.VISIBLE);
			txt_joining_player.setText("Players are joining");
			rlayout_gridView.setVisibility(View.VISIBLE);
			textView_players.setText(Constants.numberOfPlayers+" Players");
			
			RelativeLayout.LayoutParams image_gParams = (RelativeLayout.LayoutParams) image_g.getLayoutParams();
			image_gParams.width = (int) (deviceHeight * .15f);
			image_gParams.height = (int) (deviceHeight * .15f);
			image_gParams.setMargins(0, (int) (deviceWidth * 0.05f), 0, 0); 
			image_g.setLayoutParams(image_gParams);
			
			RelativeLayout.LayoutParams img_startParams = (RelativeLayout.LayoutParams) img_start.getLayoutParams();
			img_startParams.width = (int) (deviceHeight * .12f);
			img_startParams.height = (int) (deviceHeight * .12f);
			img_startParams.setMargins(0, 0, 0, (int) (deviceWidth * 0.09f)); 
			img_start.setLayoutParams(img_startParams);
		}
	}

	
	private void setViews() {
		/** Set text size of GUI objects **/
		textView_players.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(textView_players.getTypeface(),(int) (0.55f * .1f * deviceHeight)));
		txt_please_wait.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txt_please_wait.getTypeface(),(int) (0.35f * .1f * deviceHeight)));
		txt_joining_player.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txt_joining_player.getTypeface(),(int) (0.65f * .1f * deviceHeight)));
		
		/** Set height,width and margin of GUI objects **/
		RelativeLayout.LayoutParams imgView_backParams = (RelativeLayout.LayoutParams) imgView_back.getLayoutParams();
		imgView_backParams.width = (int) (deviceHeight * .08f);
		imgView_backParams.height = (int) (deviceHeight * .08f);
		imgView_back.setLayoutParams(imgView_backParams);
		
		RelativeLayout.LayoutParams imgView_logoParams = (RelativeLayout.LayoutParams) imgView_logo.getLayoutParams();
		imgView_logoParams.width = (int) (deviceHeight * .08f);
		imgView_logoParams.height = (int) (deviceHeight * .08f);
		imgView_logo.setLayoutParams(imgView_logoParams);
			
		RelativeLayout.LayoutParams rlayout_gridViewParams = (RelativeLayout.LayoutParams) rlayout_gridView.getLayoutParams();
		rlayout_gridViewParams.height = (int) (deviceHeight * .27f);
		rlayout_gridView.setLayoutParams(rlayout_gridViewParams);
				
		gridView.setColumnWidth((int) ((deviceWidth / 5)));
		gridView.setVerticalSpacing((int) (deviceWidth * 0.005f));
		gridView.setHorizontalSpacing((int) (deviceWidth * 0.005f));
										
		/** Set padding within GUI objects **/
		rlayout_main.setPadding((int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.03f), (int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.02f));
		rlayout_middleView.setPadding((int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.06f), (int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.02f));
		
		
	}

	/** Set button's onClick listener object **/
	private void assignClicks() {
		imgView_back.setOnClickListener(this);
		img_start.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgView_back:
			onBackPressed();
			break;
		case R.id.img_start:
			startplay();
			break;
		case R.id.btn_No:
			onCancelDialogClick();
			break;
		case R.id.btn_Yes:
			getQuitGame();
			break;
		default:
			break;
		}
	}
	
	
	private void setAdapter() {
		joiningRoomAdapter = new JoiningRoomAdapter(activity, imageId);
		gridView.setAdapter(joiningRoomAdapter);
	}

	@Override
	public void onBackPressed() {
		forgotPassword();
	}

	private void startplay() {
		Intent intent = new Intent(activity, PlayScreenActivtiy.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		activity.finish();
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
		
		textView_displayMsg.setText("Are you sure, you want to quit the game?");
		btn_No.setText("Resume");		
		btn_Yes.setText("  Quit  ");	
		
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
	
		
	private void getQuitGame() {
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
			new jsonRegister(activity, jsonObject).execute();
		} else {
			showPopup("Internet not available.");
		}
	}

	String mode = "";
	public class jsonRegister extends AsyncTask<Void, Void, Void> {
		JSONObject jsonObj;

		public jsonRegister(Activity activity, JSONObject jsonObject) {
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
				keyMap.put("functionName", "quit_game");
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
						//System.out.println("out String is :-" + output);
						// get Data data here___________________________
						try {
						
							JSONObject jsonObject = new JSONObject(output);
							mode = jsonObject.getString("mode");
													
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
					Intent intent = new Intent(activity, RoomActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_left,	R.anim.slide_out_right);
					activity.finish();
					mProgressDialog.dismiss();
					selectionDialog.dismiss();
				} else {
					if (isBackGame == true) {
						showPopup("Sorry ! Your game is start now.");

					} else {
						Intent intent = new Intent(activity, RoomActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_left,	R.anim.slide_out_right);
						activity.finish();
						selectionDialog.dismiss();
					}
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
