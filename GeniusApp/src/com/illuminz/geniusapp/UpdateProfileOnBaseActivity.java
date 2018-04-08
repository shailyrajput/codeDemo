package com.illuminz.geniusapp;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.illuminz.geniusapp.imagemgnt.AppController;
import com.illuminz.utilities.Constants;
import com.illuminz.utilities.MyApplication;
import com.illuminz.utilities.MyUIApplication;
import com.illuminz.utilities.UtilityClass;

public class UpdateProfileOnBaseActivity extends BaseActivity implements OnClickListener {
	Activity activity = UpdateProfileOnBaseActivity.this;
	Context context;
	private int deviceHeight;
	private int deviceWidth;
	private RelativeLayout rlayout_main, rlayout_imageView, rlayout_dropdown, onOffLayout;
	private ImageView imgView_back, imgView_logo, imgView_plus, imgView_Next;
	private TextView txtView_photo, txtView_age, textView_ageHolder, txtView_gender, offText, onText;
	private ToggleButton toggleButton;
	String quiz_userid, imageUrl= "";
	String imageFilePath = "";
	String dateofBirth = "0";
	String gender = "2";
	Dialog selectionDialog;
	RelativeLayout rLayout_main, selectFromGallery, takeFromCamera, cancel;
	ImageView imageView_camera, imageView_gallary;
	private static final int PICK_IMAGE = 1;
	final int CAMERA_CAPTURE = 2;
	final int PIC_CROP = 3;
	private Uri picUri;
	ProgressDialog mProgressDialog;
	LayoutInflater inflater;
	private int mYear, mMonth, mDay;
	NetworkImageView netimage;
	ImageLoader imageLoader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout baseView = (LinearLayout) findViewById(R.id.base_layout);
		RelativeLayout inflatedLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.update_profile_screen, null, false);
		baseView.addView(inflatedLayout);
		imageLoader = AppController.getInstance().getImageLoader();
		Title.setText("UPDATE PROFILE");
		context=getApplicationContext();
		quiz_userid = UtilityClass.getQuizUserIdFromSharedPreference(activity);
		
		getActionBar().hide();
		getDeviceParams();
		initializeView();
		setViews();
		assignClicks();
		
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

		rlayout_main = (RelativeLayout) findViewById(R.id.rlayout_main);
		rlayout_dropdown = (RelativeLayout) findViewById(R.id.rlayout_dropdown);
		onOffLayout = (RelativeLayout) findViewById(R.id.onOffLayout);
		rlayout_imageView = (RelativeLayout) findViewById(R.id.rlayout_imageView);
		imgView_back = (ImageView) findViewById(R.id.imgView_back);
		imgView_logo = (ImageView) findViewById(R.id.imgView_logo);
		imgView_plus = (ImageView) findViewById(R.id.imgView_plus);
		netimage = (NetworkImageView) findViewById(R.id.imgNetwork);
		imgView_Next = (ImageView) findViewById(R.id.imgView_Next);
		txtView_photo = (TextView) findViewById(R.id.txtView_photo);
		txtView_age = (TextView) findViewById(R.id.txtView_age);
		textView_ageHolder = (TextView) findViewById(R.id.textView_ageHolder);
		txtView_gender = (TextView) findViewById(R.id.txtView_gender);
		offText = (TextView) findViewById(R.id.offText);
		onText = (TextView) findViewById(R.id.onText);
		toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
		
		offText.setTextColor(getResources().getColor(R.color.white));
		onText.setTextColor(getResources().getColor(R.color.white));
		
        System.out.println("imageUrl------------->"+imageUrl);
	
		// If you are using NetworkImageView
		netimage.setImageUrl(imageUrl, imageLoader);
		// Loading image with placeholder and error image
		imageLoader.get(imageUrl, ImageLoader.getImageListener(imgView_plus, R.drawable.user1, R.drawable.user1));
		Cache cache = AppController.getInstance().getRequestQueue().getCache();
		Entry entry = cache.get(imageUrl);
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
			
		toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				
				if (isChecked) 
				{
					offText.setTextColor(getResources().getColor(R.color.white));
					onText.setTextColor(getResources().getColor(R.color.background_color));
					gender = "1";	
					System.out.println(">male>>> : "+gender);
					toggleButton.setBackground(getResources().getDrawable(R.drawable.toggle_button_selector));
				} else 
				{
					offText.setTextColor(getResources().getColor(R.color.background_color));
					onText.setTextColor(getResources().getColor(R.color.white));
					gender = "2";	
					System.out.println(">female>>> : "+gender);
					toggleButton.setBackground(getResources().getDrawable(R.drawable.toggle_button_selector));
				}
			}
		});
			
	}
	
		
	/** Set the GUI objects **/
	private void setViews() {
		/** Set text size of GUI objects **/
		txtView_photo.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txtView_photo.getTypeface(),(int) (0.40f * .1f * deviceHeight)));
		txtView_age.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txtView_age.getTypeface(),(int) (0.40f * .1f * deviceHeight)));
		textView_ageHolder.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(textView_ageHolder.getTypeface(),(int) (0.40f * .1f * deviceHeight)));
		txtView_gender.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txtView_gender.getTypeface(),(int) (0.40f * .1f * deviceHeight)));
		offText.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(offText.getTypeface(),(int) (0.40f * .1f * deviceHeight)));
		onText.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(onText.getTypeface(),(int) (0.40f * .1f * deviceHeight)));
		
		/** Set height,width and margin of GUI objects **/
		RelativeLayout.LayoutParams imgView_backParams = (RelativeLayout.LayoutParams) imgView_back.getLayoutParams();
		imgView_backParams.width = (int) (deviceHeight * .08f);
		imgView_backParams.height = (int) (deviceHeight * .08f);
		imgView_back.setLayoutParams(imgView_backParams);
		
		RelativeLayout.LayoutParams imgView_logoParams = (RelativeLayout.LayoutParams) imgView_logo.getLayoutParams();
		imgView_logoParams.width = (int) (deviceHeight * .15f);
		imgView_logoParams.height = (int) (deviceHeight * .15f);
		imgView_logo.setLayoutParams(imgView_logoParams);
								
		RelativeLayout.LayoutParams rlayout_imageViewParams = (RelativeLayout.LayoutParams) rlayout_imageView.getLayoutParams();
		rlayout_imageViewParams.setMargins(0, (int) (deviceWidth * 0.06f), 0,(int) (deviceWidth * 0.02f)); 
		rlayout_imageView.setLayoutParams(rlayout_imageViewParams);
		
		RelativeLayout.LayoutParams imgView_plusParams = (RelativeLayout.LayoutParams) imgView_plus.getLayoutParams();
		imgView_plusParams.width = (int) (deviceHeight * .15f);
		imgView_plusParams.height = (int) (deviceHeight * .15f);
		imgView_plus.setLayoutParams(imgView_plusParams);
				
		RelativeLayout.LayoutParams txtView_ageParams = (RelativeLayout.LayoutParams) txtView_age.getLayoutParams();
		txtView_ageParams.setMargins(0, (int) (deviceWidth * 0.08f), 0, 0);
		txtView_age.setLayoutParams(txtView_ageParams);
		
		RelativeLayout.LayoutParams rlayout_dropdownParams = (RelativeLayout.LayoutParams) rlayout_dropdown.getLayoutParams();
		rlayout_dropdownParams.width = (int) (deviceWidth * .60f );
		rlayout_dropdownParams.height = (int) (deviceHeight * .09f);
		rlayout_dropdownParams.setMargins(0, (int) (deviceWidth * 0.02f), 0, 0); 
		rlayout_dropdown.setLayoutParams(rlayout_dropdownParams);
			
		RelativeLayout.LayoutParams txtView_genderParams = (RelativeLayout.LayoutParams) txtView_gender.getLayoutParams();
		txtView_genderParams.setMargins(0, (int) (deviceWidth * 0.05f), 0, 0);
		txtView_gender.setLayoutParams(txtView_genderParams);
			
		RelativeLayout.LayoutParams onOffLayoutParams = (RelativeLayout.LayoutParams) onOffLayout.getLayoutParams();
		onOffLayoutParams.width = (int) (deviceWidth * .60f );
		onOffLayoutParams.height = (int) (deviceHeight * .09f);
		onOffLayoutParams.setMargins(0, (int) (deviceWidth * 0.02f), 0, 0); 
		onOffLayout.setLayoutParams(onOffLayoutParams);
		
		
		RelativeLayout.LayoutParams toggleButtonParams = (RelativeLayout.LayoutParams) toggleButton.getLayoutParams();
		toggleButtonParams.width = (int) (deviceWidth * .60f );
		toggleButtonParams.height = (int) (deviceHeight * .09f);
		toggleButton.setLayoutParams(toggleButtonParams);
				
		RelativeLayout.LayoutParams offTextParams = (RelativeLayout.LayoutParams) offText.getLayoutParams();
		offTextParams.setMargins((int) (deviceWidth * 0.06f), (int) (deviceWidth * 0.025f), 0, 0);
		offText.setLayoutParams(offTextParams);
		
		RelativeLayout.LayoutParams onTextParams = (RelativeLayout.LayoutParams) onText.getLayoutParams();
		onTextParams.setMargins(0, (int) (deviceWidth * 0.025f), (int) (deviceWidth * 0.12f), 0);
		onText.setLayoutParams(onTextParams);
		
		RelativeLayout.LayoutParams imgView_NextParams = (RelativeLayout.LayoutParams) imgView_Next.getLayoutParams();
		imgView_NextParams.width = (int) (deviceWidth * .35f );
		imgView_NextParams.height = (int) (deviceHeight * .07f);
		imgView_Next.setLayoutParams(imgView_NextParams);
				
		/** Set padding within GUI objects **/
		rlayout_main.setPadding((int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.03f), (int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.08f));
		rlayout_dropdown.setPadding((int) (deviceWidth * 0.06f), (int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.08f), 0);
		
		
	}
	
	private void onImageClick() {
		// TODO Auto-generated method stub
		selectionDialog = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
		selectionDialog.getWindow().getAttributes().dimAmount = 0.7f;
		selectionDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		selectionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		selectionDialog.setCancelable(true);
		selectionDialog.setContentView(R.layout.imagepic_or_take_layout);
		selectionDialog.setCanceledOnTouchOutside(false);
		selectionDialog.show();
		// setting view
		cancel = (RelativeLayout) selectionDialog.findViewById(R.id.cancel);
		rLayout_main = (RelativeLayout) selectionDialog.findViewById(R.id.rLayout_main);
		selectFromGallery = (RelativeLayout) selectionDialog.findViewById(R.id.useExisting);
		takeFromCamera = (RelativeLayout) selectionDialog.findViewById(R.id.takePic);
		imageView_camera = (ImageView) selectionDialog.findViewById(R.id.imageView_camera);
		imageView_gallary = (ImageView) selectionDialog.findViewById(R.id.imageView_gallary);

		RelativeLayout.LayoutParams imageView_cameraParams = (RelativeLayout.LayoutParams) imageView_camera.getLayoutParams();
		imageView_cameraParams.width = (int) (deviceHeight * .08f);
		imageView_cameraParams.height = (int) (deviceHeight * .06f);
		imageView_cameraParams.setMargins(0, (int) (deviceHeight * 0.04f), 0, 0);
		imageView_camera.setLayoutParams(imageView_cameraParams);

		RelativeLayout.LayoutParams imageView_gallaryParams = (RelativeLayout.LayoutParams) imageView_gallary.getLayoutParams();
		imageView_gallaryParams.width = (int) (deviceHeight * .06f);
		imageView_gallaryParams.height = (int) (deviceHeight * .06f);
		imageView_gallaryParams.setMargins(0, (int) (deviceHeight * 0.05f), 0, 0);
		imageView_gallary.setLayoutParams(imageView_gallaryParams);

		rLayout_main.setPadding((int) (deviceHeight * .02f),(int) (deviceHeight * .03f), (int) (deviceHeight * .02f), (int) (deviceHeight * .03f));

		// setting clicks
		selectFromGallery.setOnClickListener(this);
		takeFromCamera.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	/** Set button's onClick listener object **/
	private void assignClicks() {
		imgView_back.setOnClickListener(this);
		imgView_Next.setOnClickListener(this);
		imgView_plus.setOnClickListener(this);
		rlayout_dropdown.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgView_back:
			onBackPressed();
			break;
		case R.id.imgView_Next:
			doActionForUpdateProfile();
			break;
		case R.id.imgView_plus:
			onImageClick();
			break;
		case R.id.rlayout_dropdown:
			goToSetDateTime();
			break;	
			
		case R.id.useExisting:
			UploadFromGallery();
			break;
		case R.id.takePic:
			openCamera();
			break;
		case R.id.cancel:
			cancel();	
			
		default:
			break;
		}
	}
	
	
	private void cancel() {
		selectionDialog.cancel();
	}
	
	private void openCamera() {
		selectionDialog.cancel();
		try {
			// use standard intent to capture an image
			Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// we will handle the returned data in onActivityResult
			startActivityForResult(captureIntent, CAMERA_CAPTURE);
		} catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support capturing images!";
			Toast toast = Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void UploadFromGallery() {
		selectionDialog.cancel();
		try {
			// use standard intent to capture an image
			Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			// we will handle the returned data in onActivityResult
			startActivityForResult(i, PICK_IMAGE);
		} catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support capturing images!";
			Toast toast = Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}


	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			// user is returning from capturing an image using the camera
			if (requestCode == PICK_IMAGE && data != null && data.getData() != null) {

				picUri = data.getData();
				Cursor cursor = activity.getContentResolver().query(picUri,new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },null, null, null);
				cursor.moveToFirst();
				cursor.close();
				// carry out the crop operation
				performCrop();
				System.gc();

			}

			if (requestCode == CAMERA_CAPTURE) {
				// get the Uri for the captured image
				picUri = data.getData();
				// carry out the crop operation
				performCrop();
				System.gc();
			}

			// user is returning from cropping the image
			else if (requestCode == PIC_CROP) {

				String filePath = Environment.getExternalStorageDirectory()	+ "/temporary_holder.jpg";
				Bitmap thumbnail = BitmapFactory.decodeFile(filePath);
				imgView_plus.setImageBitmap(thumbnail);
				
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
			    thumbnail.compress(Bitmap.CompressFormat.JPEG, 35, stream);
			    byte[] imageInByte = stream.toByteArray();
		        imageFilePath = Base64.encodeToString(imageInByte,Base64.DEFAULT);
		        	               		        
		   	}
		}
	}

	
	private void performCrop() {
		// take care of exceptions
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(picUri, "image/*");
			// set crop properties
			cropIntent.putExtra("crop", "true");
			// indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			// indicate output X and Y
			cropIntent.putExtra("outputX", 512);
			cropIntent.putExtra("outputY", 512);
			//cropIntent.putExtra("scale", true);
			cropIntent.putExtra("scaleUpIfNeeded", true);

			File f = new File(Environment.getExternalStorageDirectory(),"/temporary_holder.jpg");
			try {
				f.createNewFile();
			} catch (IOException ex) {
				Log.e("io", ex.getMessage());
			}

			Uri uri = Uri.fromFile(f);
			cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			startActivityForResult(cropIntent, PIC_CROP);
		}
		// respond to users whose devices do not support the crop action
		catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			Toast toast = Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	
	
   //=======================================================================================================================
	
	
	private void goToSetDateTime() {
		// Process to get Current Date
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// Launch Date Picker Dialog
		DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						// Display Selected date in textbox
						textView_ageHolder.setText(year + "-"	+ (monthOfYear + 1) + "-" + dayOfMonth);
						dateofBirth = textView_ageHolder.getText().toString();
					}
				}, mYear, mMonth, mDay);
		dpd.show();
	}
	
		
	private void doActionForUpdateProfile() {
		JSONObject jsonObject = new JSONObject();
		try {

			jsonObject.put("user_id", quiz_userid);
			jsonObject.put("profile_photo", imageFilePath);
			jsonObject.put("dob", dateofBirth);
			jsonObject.put("gender", gender);
		

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
				keyMap.put("functionName", "update_profile");

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
						connection.setRequestMethod("POST");
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
					overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
					activity.finish();

					mProgressDialog.dismiss();
				} else {

					showPopup("Email ID already exist.");
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

	@Override
	public void onBackPressed() {

		Intent intent = new Intent(activity,RoomActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
		activity.finish();
	}

	public void showToast(String msg) {
		Toast.makeText(activity.getApplicationContext(), msg,	Toast.LENGTH_SHORT).show();
	}
}