package com.illuminz.adapter;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
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
import com.illuminz.geniusapp.R;
import com.illuminz.geniusapp.imagemgnt.AppController;
import com.illuminz.utilities.Constants;
import com.illuminz.utilities.MyApplication;
import com.illuminz.utilities.MyUIApplication;
import com.illuminz.utilities.UtilityClass;

public class SearchFriendAdapter extends BaseAdapter {
	Activity activity;
	ArrayList<HashMap<String, String>> list;
	ViewHolder holder;
	LayoutInflater inflater = null;
	String UserName;
	String edittext;
	int start, end;
	SpannableString spannable;
	Pattern pattern;
	Matcher matcher;
	String quiz_userid;
	ProgressDialog mProgressDialog;
	String devideID = "";
	private int deviceHeight;
	ImageLoader imageLoader;
	
	public SearchFriendAdapter(Activity _activity,ArrayList<HashMap<String, String>> list, String edittext) {
		activity = _activity;
		this.list = list;
		this.edittext = edittext;
		imageLoader = AppController.getInstance().getImageLoader();
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		UserName = UtilityClass.getQuizUserNameFromSharedPreference(activity);
		pattern = Pattern.compile(edittext, Pattern.CASE_INSENSITIVE);
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
			convertView = inflater.inflate(R.layout.friend_item, null);
			holder.mainLayout = (RelativeLayout) convertView.findViewById(R.id.mainLayout);
			holder.image_profile = (ImageView)convertView.findViewById(R.id.image_profile);
			holder.netimage = (NetworkImageView) convertView .findViewById(R.id.imgNetwork);	
			holder.user_txt = (TextView) convertView.findViewById(R.id.user_txt);
			holder.inviter_btn = (Button) convertView.findViewById(R.id.invite_btn);
			
			holder.user_txt.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(holder.user_txt.getTypeface(), (int)(0.40f*.1f*deviceHeight)));
			
			holder.image_profile.setLayoutParams(new RelativeLayout.LayoutParams((int) (deviceHeight * .08f), (int) (deviceHeight * .08f)));
			RelativeLayout.LayoutParams inviter_btnParams = (RelativeLayout.LayoutParams) holder.inviter_btn.getLayoutParams();
			inviter_btnParams.width = (int) (deviceHeight * .05f);
			inviter_btnParams.height = (int) (deviceHeight * .05f);
		    holder.inviter_btn.setLayoutParams(inviter_btnParams);
			
			holder.mainLayout.setPadding((int) (deviceHeight * 0.01f), (int) (deviceHeight * 0.01f), (int) (deviceHeight * 0.01f), (int) (deviceHeight * 0.01f));	
			holder.user_txt.setPadding((int) (deviceHeight * 0.01f), 0, (int) (deviceHeight * 0.01f), 0);
			
			
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		String str = list.get(position).get("user_name");
		matcher = pattern.matcher(list.get(position).get("user_name"));
		spannable = new SpannableString(str);

		while (matcher.find()) {
			start = matcher.start();
			end = matcher.end();
			break;
		}
		spannable.setSpan(new ForegroundColorSpan(R.color.white), start, end, 0);
		spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, 0);
		
		holder.user_txt.setText(spannable);

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
	
        holder.inviter_btn.setOnClickListener(new OnClickListener() {
						
			@Override
			public void onClick(View v) {

				String user_id = list.get(position).get("user_id");
				inviteFriend(user_id, quiz_userid,v);
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
		Button inviter_btn;
		NetworkImageView netimage;
	}
	
	private void inviteFriend(String user_id, String quiz_userid,View view) {
	
		JSONObject jsonObject = new JSONObject();
		try {
		
		    jsonObject.put("sender_id", quiz_userid);
		    jsonObject.put("reciver_id", user_id);
		    jsonObject.put("device_id", devideID);
			jsonObject.put("device_type", "android");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		MyApplication app = new MyApplication();
		if (app.isInternetAvailable(activity.getApplicationContext())) {
			new jsonFriendList(activity, jsonObject,view).execute();
		} else {
			showToast("Internet not available.");
		}
	}

	String mode = "";
	class jsonFriendList extends AsyncTask<Void, Void, Void> {
		JSONObject jsonObj;
		View view;

		jsonFriendList(Activity activity, JSONObject jsonObject,View view) {
			jsonObj = jsonObject;
			this.view=view;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(activity, "", "Please wait...");
			mProgressDialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				String jsonText = jsonObj.toString();
				final HashMap<String, String> keyMap = new HashMap<String, String>();
				keyMap.put("functionName", "send_friend_request");
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
				showToast("Oops ! Connection timeout !");
				mProgressDialog.dismiss();
			} else {
			if (mode.equals("success")) {
				setteextChanged(view);
				mProgressDialog.dismiss();
			} else {
				showToast("Something is wrong !");
				mProgressDialog.dismiss();
			}
			}
		}
	}
	
	private void showToast(String msg) {
		Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();

	}

	public void setteextChanged(View view)
	{
		RelativeLayout vwParentRow = (RelativeLayout) view.getParent();
        holder.inviter_btn = (Button) vwParentRow.getChildAt(1);
		//holder.inviter_btn.setText("Invited");
        int c = R.color.red_blur;
        vwParentRow.setBackgroundColor(c);
		vwParentRow.refreshDrawableState();
		holder.inviter_btn.setEnabled(false);
		
	}
}
