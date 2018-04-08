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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.illuminz.adapter.ScoreAdapter;
import com.illuminz.utilities.Constants;
import com.illuminz.utilities.MyApplication;
import com.illuminz.utilities.MyUIApplication;
import com.illuminz.utilities.UtilityClass;

public class ScoreActivity extends Activity {
	
	Activity activity = ScoreActivity.this;
	Context context;
	LinearLayout rlayout_header ;
	RelativeLayout rlayout_headerBack, itemLayout;
	ImageView imageView_backArrow;
	TextView textView_headerText, name, score, ranktxt;
	ListView listView;
	int deviceHeight, deviceWidth;
	ScoreAdapter scoreadapter;
	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> scoreList;
	String gameId, numberOfPlayer;
	LayoutInflater inflater;
	String devideID = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_screen);
		getActionBar().hide();
		context=getApplicationContext();
		devideID = UtilityClass.getDeviceIdFromSharedPreference(activity);
		getDeviceParams();
		getValueFromIntent();
		initView();
		setViews();
		getScoreData();
	}

	private void getValueFromIntent() {
		Intent i = getIntent();
		Bundle mBundle = i.getExtras();
		if (mBundle != null) {
			gameId = mBundle.getString("gameId");
			numberOfPlayer = mBundle.getString("numberOfPlayer");
		}
	}
	
	private void getDeviceParams() {                                                                                                                                                                                                                                                                                                                                                 
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		deviceHeight = displaymetrics.heightPixels;
		deviceWidth = displaymetrics.widthPixels;
	}

	private void initView() {
		rlayout_header = (LinearLayout) findViewById(R.id.rlayout_header);
		rlayout_headerBack = (RelativeLayout) findViewById(R.id.rlayout_headerBack);
		itemLayout = (RelativeLayout) findViewById(R.id.itemLayout);
		imageView_backArrow = (ImageView) findViewById(R.id.imageView_backArrow);
		textView_headerText = (TextView) findViewById(R.id.textView_headerText);
		name = (TextView) findViewById(R.id.nametxt);
		score = (TextView) findViewById(R.id.scoretxt);
		ranktxt = (TextView) findViewById(R.id.ranktxt);
		listView = (ListView) findViewById(R.id.myListView);
		
		if (numberOfPlayer.equals("1")) {
			itemLayout.setVisibility(View.GONE);
		}else{
			itemLayout.setVisibility(View.VISIBLE);
		}
		
        rlayout_headerBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activity.finish();
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
			}
		});
    }

	private void setViews() {
		textView_headerText.setTextSize(TypedValue.COMPLEX_UNIT_PX,MyUIApplication.determineTextSize(textView_headerText.getTypeface(),(int)(0.45f*.1f*deviceHeight)));
		name.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(name.getTypeface(),(int) (0.40f * .1f * deviceHeight)));
		score.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(score.getTypeface(),(int) (0.40f * .1f * deviceHeight)));
		ranktxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(ranktxt.getTypeface(),(int) (0.40f * .1f * deviceHeight)));
		
		/** Set height,width and margin of GUI objects **/	
		rlayout_header.getLayoutParams().height = (int) (deviceHeight * .080f);
		RelativeLayout.LayoutParams imageView_backArrowParams = (RelativeLayout.LayoutParams) imageView_backArrow.getLayoutParams();
		imageView_backArrowParams.height = (int) (deviceHeight * .03f);
		imageView_backArrowParams.width = (int) (deviceHeight * .03f);
		imageView_backArrow.setLayoutParams(imageView_backArrowParams);
		rlayout_header.setPadding((int) (deviceWidth * 0.04f),(int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.02f),(int) (deviceWidth * 0.02f));
	}

	private void getScoreData() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("game_id", gameId);
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
				keyMap.put("functionName", "get_score_info");
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
							scoreList = new ArrayList<HashMap<String, String>>();
							JSONObject jsonObject = new JSONObject(output);
							mode = jsonObject.getString("mode");
							JSONArray jsonMainNode = jsonObject.optJSONArray("data");
							if (jsonMainNode != null) {
								for (int i = 0; i < jsonMainNode.length(); i++) {
									JSONObject arrayObj = jsonMainNode.getJSONObject(i);
									String name = arrayObj.getString("name");
									String score = arrayObj.getString("score");
									HashMap<String, String> jsonData = new HashMap<String, String>();
									// adding each child node to HashMap key => value
									jsonData.put("name", name);
									jsonData.put("score", score);
									// adding question data to questionList 
									scoreList.add(jsonData);
								}
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
				setDataInAdapter(scoreList, numberOfPlayer);
				mProgressDialog.dismiss();
			} else {
				showPopup("Something is wrong !");
				mProgressDialog.dismiss();
			}
			}
		}
	}
	
	private void setDataInAdapter(ArrayList<HashMap<String, String>> scoreList2, String numberOfPlayer2) {
		scoreadapter = new ScoreAdapter(activity, scoreList2, numberOfPlayer2 );
		listView.setAdapter(scoreadapter);
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

	@Override
	public void onBackPressed() {
		activity.finish();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
	}
}
