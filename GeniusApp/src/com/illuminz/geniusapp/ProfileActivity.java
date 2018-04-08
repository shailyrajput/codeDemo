package com.illuminz.geniusapp;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
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
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.illuminz.adapter.ProfileAdapter;
import com.illuminz.geniusapp.lazyadapter.ImageLoader;
import com.illuminz.utilities.Constants;
import com.illuminz.utilities.MyApplication;
import com.illuminz.utilities.MyUIApplication;
import com.illuminz.utilities.UtilityClass;

public class ProfileActivity extends BaseActivity {
	
	Activity activity = ProfileActivity.this;
	Context context;
	private int deviceHeight;
	RelativeLayout mainLayout;
    ListView listView;
    ProfileAdapter profileadapter;
	TextView name ,email,playedGame ,dob_txt ,gender_txt;
	ImageView imgView_profilePic;
	String quiz_userid , quiz_email , mode = "";
	ProgressDialog mProgressDialog;
	String prifileUserName, profileUserEmail,gender, dob, profilePhoto;
	ArrayList<HashMap<String, String>> profileData;
	ExpandableListView expListView;
	LinkedHashSet<String> linkedHashSet;
	List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
	List<String> single_Player, OneonOne_Player, five_Player, tan_Player, twentyFive_Player;
	LayoutInflater inflater;
	String devideID = "";
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout baseView = (LinearLayout) findViewById(R.id.base_layout);
		RelativeLayout inflatedLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.profile_screen, null, false);
		baseView.addView(inflatedLayout);
		
		Title.setText("PROFILE");
		quiz_userid = UtilityClass.getQuizUserIdFromSharedPreference(activity);
		devideID = UtilityClass.getDeviceIdFromSharedPreference(activity);
		getActionBar().hide();
		context = getApplicationContext();
		getDeviceParams();
		initView();
		setViews();
		getProfileData();
	}

	private void getDeviceParams() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		deviceHeight = displayMetrics.heightPixels;
	}

	private void initView() {
		mainLayout  = (RelativeLayout)findViewById(R.id.mainLayout);
		name = (TextView)findViewById(R.id.name_txt);
		email = (TextView)findViewById(R.id.email_txt);
		playedGame = (TextView)findViewById(R.id.played_txt);
		expListView = (ExpandableListView)findViewById(R.id.myListView);
		dob_txt = (TextView)findViewById(R.id.dob_txt);
		gender_txt = (TextView)findViewById(R.id.gender_txt);
		imgView_profilePic = (ImageView) findViewById(R.id.imgView_profilePic);
		
		single_Player = new ArrayList<String>();
		OneonOne_Player = new ArrayList<String>();
		five_Player = new ArrayList<String>();
		tan_Player = new ArrayList<String>();
		twentyFive_Player = new ArrayList<String>();
	}

	private void setViews() {
		
		name.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(name.getTypeface(), (int)(0.40f*.1f*deviceHeight)));
		email.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(email.getTypeface(), (int)(0.40f*.1f*deviceHeight)));
		dob_txt.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(dob_txt.getTypeface(), (int)(0.40f*.1f*deviceHeight)));
		gender_txt.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(gender_txt.getTypeface(), (int)(0.40f*.1f*deviceHeight)));
		playedGame.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(playedGame.getTypeface(), (int)(0.45f*.1f*deviceHeight)));
		
		RelativeLayout.LayoutParams imgView_profilePicParams = (RelativeLayout.LayoutParams) imgView_profilePic.getLayoutParams();
		imgView_profilePicParams.width = (int) (deviceHeight * .13f);
		imgView_profilePicParams.height = (int) (deviceHeight * .13f);
		imgView_profilePic.setLayoutParams(imgView_profilePicParams);
				
		playedGame.setPadding(0, (int) (deviceWidth * 0.02f), 0, 0);
		mainLayout.setPadding((int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.02f));
	}
	
	private void getProfileData() {
		JSONObject jsonObject = new JSONObject();
		try {
			
			System.out.println("user id>>>: "+quiz_userid);
           	jsonObject.put("user_id", quiz_userid);
           	jsonObject.put("device_id", devideID);
			jsonObject.put("device_type", "android");
	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MyApplication app = new MyApplication();
		if (app.isInternetAvailable(activity.getApplicationContext())) {
			new jsonProfile(activity, jsonObject).execute();
		} else {
			showPopup("Internet not available.");
		}
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(activity,RoomActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
		activity.finish();
	}

	class jsonProfile extends AsyncTask< Void, Void, Void >
	{
		JSONObject jsonObj;
		jsonProfile(Activity activity , JSONObject jsonObject)
		{
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
				keyMap.put("functionName", "get_profile_info");
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
				
							profileData = new ArrayList<HashMap<String, String>>();
							JSONObject jsonObject = new JSONObject(output);
							mode = jsonObject.getString("mode");
							prifileUserName= jsonObject.getString("user_name");
							profileUserEmail= jsonObject.getString("user_email_id");
							gender = jsonObject.getString("gender");
							dob = jsonObject.getString("dob");
							profilePhoto = jsonObject.getString("profile_photo");
							
							JSONArray jsonMainNode = jsonObject.optJSONArray("data");
							if (jsonMainNode != null) {
								for (int i = 0; i < jsonMainNode.length(); i++) {
									JSONObject arrayObj = jsonMainNode.getJSONObject(i);
									
									String id = arrayObj.getString("id");
									String gameId = arrayObj.getString("gameId");
									String room_player_number = arrayObj.getString("room_player_number");
									String created = arrayObj.getString("created");
									
									HashMap<String, String> jsonData = new HashMap<String, String>();
									// adding each child node to HashMap key => value
									jsonData.put("id", id);
									jsonData.put("gameId", gameId);
									jsonData.put("room_player_number", room_player_number);
									jsonData.put("created", created);	
									// adding question data to questionList 
									profileData.add(jsonData);
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
				setDataInAdapter(profileData, prifileUserName, profileUserEmail,gender, dob,profilePhoto);
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
	
	private void setDataInAdapter(
			ArrayList<HashMap<String, String>> profileData2,
			String prifileUserName2, String profileUserEmail2, String gender2,
			String dob2, String profilePhoto2) {
		// TODO Auto-generated method stub
		name.setText("Name : " + prifileUserName2);
		email.setText("Email Id : " + profileUserEmail2);
		dob_txt.setText("Dob : " + dob2);
		if (gender2.equals("1")) {
			gender_txt.setText("Gender : Male");
		} else {
			gender_txt.setText("Gender : Female" );
		}

		new ImageLoader(activity).DisplayImageRoundRact(profilePhoto2,
				imgView_profilePic, (int) (deviceHeight * .13f),
				(int) (deviceHeight * .13f));

	if (!profileData2.isEmpty()) {
			playedGame.setVisibility(View.VISIBLE);
			linkedHashSet = new LinkedHashSet<String>();
			listDataHeader = new ArrayList<String>();
			listDataChild = new HashMap<String, List<String>>();

			for (int i = 0; i < profileData2.size(); i++) {
				if (profileData2.get(i).get("room_player_number").equals("1")) {
					linkedHashSet.add("Single Player");
				} else if (profileData2.get(i).get("room_player_number").equals("2")) {
					linkedHashSet.add("One-on-One Player");
				} else if (profileData2.get(i).get("room_player_number")
						.equals("5")) {
					linkedHashSet.add("Five Player");
				} else if (profileData2.get(i).get("room_player_number")
						.equals("10")) {
					linkedHashSet.add("Tan Player");
				} else if (profileData2.get(i).get("room_player_number")
						.equals("25")) {
					linkedHashSet.add("Twenty Five Player");
				}
			}
		
		// To get the Iterator use the iterator() operation
		Iterator <String> it = linkedHashSet.iterator();
		while(it.hasNext())
		listDataHeader.add(it.next());
		
		for (int i = 0; i < profileData2.size(); i++) {
			
			if (profileData2.get(i).get("room_player_number").equals("1")) {
				String combineData = profileData2.get(i).get("room_player_number")+"_"+profileData2.get(i).get("gameId")+"_"+profileData2.get(i).get("created");
				single_Player.add(combineData);
			} else if (profileData2.get(i).get("room_player_number").equals("2")) {
				String combineData = profileData2.get(i).get("room_player_number")+"_"+profileData2.get(i).get("gameId")+"_"+profileData2.get(i).get("created");
				OneonOne_Player.add(combineData);
			} else if (profileData2.get(i).get("room_player_number").equals("5")) {
				String combineData = profileData2.get(i).get("room_player_number")+"_"+profileData2.get(i).get("gameId")+"_"+profileData2.get(i).get("created");
				five_Player.add(combineData);
			} else if (profileData2.get(i).get("room_player_number").equals("10")) {
				String combineData = profileData2.get(i).get("room_player_number")+"_"+profileData2.get(i).get("gameId")+"_"+profileData2.get(i).get("created");
				tan_Player.add(combineData);
			} else if (profileData2.get(i).get("room_player_number").equals("25")) {
				String combineData = profileData2.get(i).get("room_player_number")+"_"+profileData2.get(i).get("gameId")+"_"+profileData2.get(i).get("created");
				twentyFive_Player.add(combineData);
			}
			for (int j = 0; j < listDataHeader.size(); j++) {
					if (listDataHeader.get(j).equals("Single Player")) {
						listDataChild.put(listDataHeader.get(j), single_Player);
					} else if (listDataHeader.get(j).equals("One-on-One Player")) {
						listDataChild.put(listDataHeader.get(j), OneonOne_Player);
					} else if (listDataHeader.get(j).equals("Five Player")) {
						listDataChild.put(listDataHeader.get(j), five_Player);
					} else if (listDataHeader.get(j).equals("Tan Player")) {
						listDataChild.put(listDataHeader.get(j), tan_Player);
					} else if (listDataHeader.get(j).equals("Twenty Five Player")) {
						listDataChild.put(listDataHeader.get(j), twentyFive_Player);
					}
				}
			}
		
	    //set data in adapter using expandable list view.............................
		profileadapter = new ProfileAdapter(activity, listDataHeader,listDataChild);
		expListView.setAdapter(profileadapter);
		expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,	int groupPosition, int childPosition, long id) {
				String getText = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
				String numberOfPlayer = getText.substring(0,getText.indexOf("_"));
				String gameId = getText.substring(getText.indexOf("_")	+ 1, getText.lastIndexOf("_"));
			    Intent intent = new Intent(activity, ScoreActivity.class);
				intent.putExtra("gameId", gameId);
				intent.putExtra("numberOfPlayer", numberOfPlayer);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
				return false;
			}
		});
		}
	}
}
