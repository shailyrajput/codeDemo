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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.illuminz.adapter.RequestFriendListAdapter;
import com.illuminz.utilities.Constants;
import com.illuminz.utilities.MyApplication;
import com.illuminz.utilities.UtilityClass;

public class FriendsRequestListActivity extends BaseActivity {
	
	Activity activity = FriendsRequestListActivity.this;
	Context context;
	ListView myListView;
	String quiz_userid;
	ProgressDialog mProgressDialog;
	ArrayList<HashMap<String, String>> recievedRequestList;
	LayoutInflater inflater;
	String user_id, user_name, user_email, devideID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout baseView = (LinearLayout) findViewById(R.id.base_layout);
		RelativeLayout inflatedLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.friend_request_screen, null, false);
		baseView.addView(inflatedLayout);
		Title.setText("Friends Requests");
		getActionBar().hide();
		context = getApplicationContext();
		quiz_userid = UtilityClass.getQuizUserIdFromSharedPreference(activity);
		devideID = UtilityClass.getDeviceIdFromSharedPreference(activity);
		initView();

	}

	private void initView() {
		myListView = (ListView) findViewById(R.id.myListView);
		recievedRequest();
	}

	private void recievedRequest() {
		
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
			new jsonData(activity, jsonObject).execute();
		} else {
			showPopup("Internet not available.");
		}
	}

	String MODE = "";
	class jsonData extends AsyncTask<Void, Void, Void> {
		JSONObject jsonObj;

		jsonData(Activity activity, JSONObject jsonObject) {
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
				keyMap.put("functionName", "recieved_friend_request_list");
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
						connection.setRequestProperty("ENCTYPE", "multipart/form-data");
						connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
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
						recievedRequestList = new ArrayList<HashMap<String, String>>();
						// get Data data here___________________________
						try {

							JSONObject jsonObject = new JSONObject(output);
							MODE = jsonObject.getString("mode");

							JSONArray jsonArray = new JSONArray();
							jsonArray = jsonObject.getJSONArray("data");
							if (jsonArray != null) {
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject arrayObj = jsonArray.getJSONObject(i);
									String user_id = arrayObj.getString("id");
									String user_name = arrayObj.getString("name");
									String profile_photo = arrayObj.getString("profile_photo");
									HashMap<String, String> map = new HashMap<String, String>();
									map.put("user_id", user_id);
									map.put("user_name", user_name);
									map.put("profile_photo", profile_photo);
									recievedRequestList.add(map);
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
			if (MODE.equals("")) {
				showPopup("Oops ! Connection timeout !");
				mProgressDialog.dismiss();
			} else {
				if (MODE.equals("success")) {
					myListView.setAdapter(new RequestFriendListAdapter(activity, recievedRequestList));
					mProgressDialog.dismiss();
				} else {
					showPopup("Something is wrong !");
					mProgressDialog.dismiss();
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(activity, RoomActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		activity.finish();
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
