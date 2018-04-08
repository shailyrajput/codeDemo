package com.illuminz.adapter;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.illuminz.geniusapp.MainActivity;
import com.illuminz.geniusapp.R;
import com.illuminz.geniusapp.imagemgnt.AppController;
import com.illuminz.utilities.Constants;
import com.illuminz.utilities.MyApplication;
import com.illuminz.utilities.MyUIApplication;
import com.illuminz.utilities.SyncQuestionService;
import com.illuminz.utilities.UtilityClass;

public class FriendListAdapter extends BaseAdapter {
	Activity activity;
	ArrayList<HashMap<String, String>> list;
	ViewHolder holder;
	LayoutInflater inflater = null;
	String quiz_userid;
	ProgressDialog mProgressDialog;
	String devideID = "";
	ArrayList<HashMap<String, String>> questionList;
	String waitingFlag;
	private int deviceHeight;
	ImageLoader imageLoader;
	
	
	public FriendListAdapter(Activity _activity,ArrayList<HashMap<String, String>> list) {
		activity = _activity;
		this.list = list;
		imageLoader = AppController.getInstance().getImageLoader();
	    inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		quiz_userid = UtilityClass.getQuizUserIdFromSharedPreference(activity);
		devideID = UtilityClass.getDeviceIdFromSharedPreference(activity);

		DisplayMetrics displayMetrics = activity.getResources()	.getDisplayMetrics();
		deviceHeight = displayMetrics.heightPixels;
		
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.friend_list_row, null);
			holder.mainLayout = (RelativeLayout) convertView.findViewById(R.id.mainLayout);
			holder.image_profile = (ImageView)convertView.findViewById(R.id.image_profile);
			holder.netimage = (NetworkImageView) convertView .findViewById(R.id.imgNetwork);	
			holder.user_txt = (TextView) convertView.findViewById(R.id.user_txt);
			holder.invite_btn = (Button) convertView.findViewById(R.id.invite_btn);
			
			holder.user_txt.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(holder.user_txt.getTypeface(), (int)(0.40f*.1f*deviceHeight)));
			holder.image_profile.setLayoutParams(new RelativeLayout.LayoutParams((int) (deviceHeight * .08f), (int) (deviceHeight * .08f)));
			holder.mainLayout.setPadding((int) (deviceHeight * 0.01f), (int) (deviceHeight * 0.01f), (int) (deviceHeight * 0.01f), (int) (deviceHeight * 0.01f));	
			holder.user_txt.setPadding((int) (deviceHeight * 0.01f), 0, (int) (deviceHeight * 0.01f), 0);
			
			
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		String str = list.get(position).get("user_name");
		holder.user_txt.setText(str);
		
		if ((list.get(position).get("profile_photo")).equals("")) {
			holder.image_profile.setImageResource(R.drawable.user1);
		} else {
			// If you are using NetworkImageView
			holder.netimage.setImageUrl(list.get(position).get("profile_photo"), imageLoader);
			// Loading image with placeholder and error image
			imageLoader.get(list.get(position).get("profile_photo"),ImageLoader.getImageListener(holder.image_profile,R.drawable.user1, R.drawable.user1));
			Cache cache = AppController.getInstance().getRequestQueue().getCache();
			Entry entry = cache.get(list.get(position).get("profile_photo"));
			if (entry != null) {
				try {
					@SuppressWarnings("unused")
					String data = new String(entry.data, "UTF-8");
					// netimage.setVisibility(1);
					// handle data, like converting it to xml, json, bitmap
					// etc.,
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				// cached response doesn't exists. Make a network call here
			}

		}
		
		
		holder.invite_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String userId = list.get(position).get("user_id");
				
				
				JSONObject jsonObject = new JSONObject();
				try {
		
					//System.out.println("user id>>>:"+userId+"<<<<<device id>>"+devideID);
					jsonObject.put("user_id", userId);
					jsonObject.put("numof_players", "");
					jsonObject.put("device_id", devideID);
					jsonObject.put("device_type", "android");
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				MyApplication app = new MyApplication();
				if (app.isInternetAvailable(activity.getApplicationContext())) {
					//new jsonPlayGameWithFriends(activity, jsonObject).execute();
				} else {
					showPopup("Internet not available.");
				}
			}
		});
		
		
		return convertView;
	}

	protected void getInviteData() {
	}
	class ViewHolder {
		RelativeLayout mainLayout;
		ImageView image_profile;
		TextView user_txt;
		Button invite_btn;
		NetworkImageView netimage;
	}
	

	String mode = "";
	public class jsonPlayGameWithFriends extends AsyncTask<Void, Void, Void> {
		JSONObject jsonObj;

		public jsonPlayGameWithFriends(Activity activity, JSONObject jsonObject) {
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
							waitingFlag = jsonObject1.getString("waitingFlag");
							System.out.println("RoomActivity.jsonRegister.doInBackground()>>> "+waitingFlag);
							UtilityClass.setQuizUserIdInSharedPreference(activity, jsonObject1.getString("user_id"));
							UtilityClass.setQuizGameIDInSharedPreference(activity, jsonObject1.getString("game_id"));
							//UtilityClass.setFlagSharedPreference(activity, String.valueOf(numberOfPlayers));
							@SuppressWarnings("unused")
							String flag=jsonObject1.getString("flag");
							
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
				Intent intent = new Intent(activity, MainActivity.class);
				intent.putExtra("waitingFlag", waitingFlag);
				System.out.println("RoomActivity.jsonRegister.onPostExecute()>>"+waitingFlag);
				activity.startActivity(intent);
				activity.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
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
		inflater=activity.getLayoutInflater();
		View customToastroot =inflater.inflate(R.layout.mycustom_toast, null);
		Toast customtoast=new Toast(activity);
		TextView textView = (TextView)customToastroot.findViewById(R.id.textView1);
		textView.setText(msg);
		customtoast.setView(customToastroot);
		customtoast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0, 0);
		customtoast.setDuration(Toast.LENGTH_LONG);
		customtoast.show();
	}

	
	
}

