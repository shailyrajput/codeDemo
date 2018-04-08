package com.illuminz.geniusapp;


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.illuminz.adapter.HorizontalAdapter;
import com.illuminz.database.DatabaseManager;
import com.illuminz.utilities.Constants;
import com.illuminz.utilities.DateDisplay;
import com.illuminz.utilities.MyUIApplication;
import com.illuminz.utilities.UtilityClass;

public class PlayScreenActivtiy extends Activity implements OnClickListener{
	Activity activity = PlayScreenActivtiy.this;
	Context context;
	int deviceHeight,deviceWidth;
	private RelativeLayout rlayout_main, rlayout_listView, rlayout_bottomView, rLayot_quest, rLayot_ans, rLayout1,
	rLayout2, rLayout3, rLayout4;
	private ImageView imgView_back, imgView_logo;
	private TextView textView_players, TextView_question1, TextView_question2, txt_option_A, txt_option_1, txt_option_B, txt_option_2,
	txt_option_C, txt_option_3, txt_option_D, txt_option_4, txt_timer;
	HorizontalListView myHzntListView;
	int[] imageId = { R.drawable.dp, R.drawable.dp1,R.drawable.dp2, R.drawable.dp, R.drawable.dp1,R.drawable.dp2, R.drawable.dp, R.drawable.dp1, R.drawable.dp2, R.drawable.mask_copy };
	HorizontalAdapter horizontalAdapter;
	
	ProgressDialog mProgressDialog;
	String quiz_userid, game_id, current_time; 
	LayoutInflater inflater;
	String devideID = "";
	DatabaseManager db;
	private int a = 0;
	private int b = 0;
	private int c = 0;
	int scroeCount = 0;
	private ArrayList<HashMap<String, String>> TableData;
	android.os.CountDownTimer Count;
	private String ans = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_screen);
		getActionBar().hide();
		getDeviceParams();
		quiz_userid = UtilityClass.getQuizUserIdFromSharedPreference(activity);
		game_id = UtilityClass.getQuizGameIDFromSharedPreference(activity);
		devideID = UtilityClass.getDeviceIdFromSharedPreference(activity);
		current_time = DateDisplay.getdDate();
	
		initializeView();
		setViews();
		assignClicks();
		db = new DatabaseManager(activity);
		getDataFromDataBase();
	    setAdapter();
	
	}

	private void getDeviceParams() {

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		deviceHeight = displaymetrics.heightPixels;
		deviceWidth = displaymetrics.widthPixels;
	}
	
	private void initializeView() 
	
	{
		rlayout_main = (RelativeLayout) findViewById(R.id.rlayout_main); 
		rlayout_listView = (RelativeLayout) findViewById(R.id.rlayout_listView); 
		rlayout_bottomView = (RelativeLayout) findViewById(R.id.rlayout_bottomView); 
		rLayot_quest = (RelativeLayout) findViewById(R.id.rLayot_quest); 
		rLayot_ans = (RelativeLayout) findViewById(R.id.rLayot_ans); 
		rLayout1 = (RelativeLayout) findViewById(R.id.rLayout1); 
		rLayout2 = (RelativeLayout) findViewById(R.id.rLayout2); 
		rLayout3 = (RelativeLayout) findViewById(R.id.rLayout3); 
		rLayout4 = (RelativeLayout) findViewById(R.id.rLayout4); 
		imgView_back = (ImageView) findViewById(R.id.imgView_back);
		imgView_logo = (ImageView) findViewById(R.id.imgView_logo);
		textView_players = (TextView) findViewById(R.id.textView_players);
		TextView_question1 = (TextView) findViewById(R.id.TextView_question1);
		TextView_question2 = (TextView) findViewById(R.id.TextView_question2);
		txt_option_A = (TextView) findViewById(R.id.txt_option_A);
		txt_option_B = (TextView) findViewById(R.id.txt_option_B);
		txt_option_C = (TextView) findViewById(R.id.txt_option_C);
		txt_option_D = (TextView) findViewById(R.id.txt_option_D);
		txt_option_1 = (TextView) findViewById(R.id.txt_option_1);
		txt_option_2 = (TextView) findViewById(R.id.txt_option_2);
		txt_option_3 = (TextView) findViewById(R.id.txt_option_3);
		txt_option_4 = (TextView) findViewById(R.id.txt_option_4);
		myHzntListView =(HorizontalListView) findViewById(R.id.myHzntListView);
		txt_timer = (TextView) findViewById(R.id.txt_timer);
		
		if (Constants.numberOfPlayers == 1) {
			textView_players.setText(Constants.numberOfPlayers+" Player");
			rlayout_listView.setVisibility(View.INVISIBLE);
			
			RelativeLayout.LayoutParams myHzntListViewParam = (RelativeLayout.LayoutParams) myHzntListView.getLayoutParams();
			myHzntListViewParam.height = (int) (deviceHeight * .08f);
			myHzntListView.setLayoutParams(myHzntListViewParam);
					
			RelativeLayout.LayoutParams rlayout_bottomViewParam = (RelativeLayout.LayoutParams) rlayout_bottomView.getLayoutParams();
			rlayout_bottomViewParam.setMargins((int) (deviceHeight * .05f), (int) (deviceHeight * .03f),(int) (deviceHeight * .05f),(int) (deviceHeight * .05f));
			rlayout_bottomView.setLayoutParams(rlayout_bottomViewParam);
			
		} else {
			textView_players.setText(Constants.numberOfPlayers+" Players");
			rlayout_listView.setVisibility(View.VISIBLE);
			
			RelativeLayout.LayoutParams myHzntListViewParam = (RelativeLayout.LayoutParams) myHzntListView.getLayoutParams();
			myHzntListViewParam.height = (int) (deviceHeight * .13f);
			myHzntListView.setLayoutParams(myHzntListViewParam);
					
			RelativeLayout.LayoutParams rlayout_bottomViewParam = (RelativeLayout.LayoutParams) rlayout_bottomView.getLayoutParams();
			rlayout_bottomViewParam.setMargins((int) (deviceHeight * .05f), (int) (deviceHeight * .03f),(int) (deviceHeight * .05f), 0);
			rlayout_bottomView.setLayoutParams(rlayout_bottomViewParam);
		}
		
		
		
		Count = new CountDownTimer(12000, 1000) {
			public void onTick(long millisUntilFinished) {
				txt_timer.setText("Seconds remaining: " + ((millisUntilFinished / 1000) - 1));
				if ((millisUntilFinished / 1000) == 1) {
					// Inserting delay here
					try {
						Thread.sleep(1);
						txt_timer.setText("Seconds remaining: " + "0");
						clickWon();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			public void onFinish() {
			}
		}.start();
	}
	
	private void getDataFromDataBase() {
		db.open();
		TableData = db.ReteriveTableData();
		db.close();
		if (TableData.size() > 0) {
			
			if (c == 0) {
				TextView_question2.setText(TableData.get(0).get("question"));
				txt_option_1.setText(TableData.get(0).get("option1").trim());
				txt_option_2.setText(TableData.get(0).get("option2").trim());
				txt_option_3.setText(TableData.get(0).get("option3").trim());
				txt_option_4.setText(TableData.get(0).get("option4").trim());
			}
		}
	}
	
	
	private void setViews() {
		textView_players.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(textView_players.getTypeface(),(int) (0.55f * .1f * deviceHeight)));
		TextView_question1.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(TextView_question1.getTypeface(),(int) (0.55f * .1f * deviceHeight)));
		TextView_question2.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(TextView_question2.getTypeface(),(int) (0.35f * .1f * deviceHeight)));
		txt_option_A.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txt_option_A.getTypeface(),(int) (0.55f * .1f * deviceHeight)));
		txt_option_B.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txt_option_B.getTypeface(),(int) (0.55f * .1f * deviceHeight)));
		txt_option_C.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txt_option_C.getTypeface(),(int) (0.55f * .1f * deviceHeight)));
		txt_option_D.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txt_option_D.getTypeface(),(int) (0.55f * .1f * deviceHeight)));
		txt_option_1.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txt_option_1.getTypeface(),(int) (0.35f * .1f * deviceHeight)));
		txt_option_2.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txt_option_2.getTypeface(),(int) (0.35f * .1f * deviceHeight)));
		txt_option_3.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txt_option_3.getTypeface(),(int) (0.35f * .1f * deviceHeight)));
		txt_option_4.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txt_option_4.getTypeface(),(int) (0.35f * .1f * deviceHeight)));
		txt_timer.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txt_timer.getTypeface(),(int) (0.35f * .1f * deviceHeight)));
			
		RelativeLayout.LayoutParams imgView_backParams = (RelativeLayout.LayoutParams) imgView_back.getLayoutParams();
		imgView_backParams.width = (int) (deviceHeight * .08f);
		imgView_backParams.height = (int) (deviceHeight * .08f);
		imgView_back.setLayoutParams(imgView_backParams);
		
		RelativeLayout.LayoutParams imgView_logoParams = (RelativeLayout.LayoutParams) imgView_logo.getLayoutParams();
		imgView_logoParams.width = (int) (deviceHeight * .08f);
		imgView_logoParams.height = (int) (deviceHeight * .08f);
		imgView_logo.setLayoutParams(imgView_logoParams);
				
		RelativeLayout.LayoutParams rlayout_listViewParam = (RelativeLayout.LayoutParams) rlayout_listView.getLayoutParams();
		rlayout_listViewParam.setMargins(0, (int) (deviceHeight * .04f), 0, 0);
		rlayout_listView.setLayoutParams(rlayout_listViewParam);
				
		RelativeLayout.LayoutParams rLayot_questParams = (RelativeLayout.LayoutParams) rLayot_quest.getLayoutParams();
		rLayot_questParams.height = (int) (deviceHeight * .17f);
		rLayot_quest.setLayoutParams(rLayot_questParams);
				
		RelativeLayout.LayoutParams rLayot_ansParam = (RelativeLayout.LayoutParams) rLayot_ans.getLayoutParams();
		rLayot_ansParam.setMargins(0, (int) (deviceHeight * .02f),0, 0);
		rLayot_ans.setLayoutParams(rLayot_ansParam);
					
		RelativeLayout.LayoutParams rLayout1Param = (RelativeLayout.LayoutParams) rLayout1.getLayoutParams();
		rLayout1Param.setMargins(0, (int) (deviceHeight * .01f),0, 0);
		rLayout1.setLayoutParams(rLayout1Param);
				
		rlayout_main.setPadding((int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.03f), (int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.05f));
		rlayout_bottomView.setPadding((int) (deviceWidth * 0.05f), (int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.03f), (int) (deviceWidth * 0.03f));
		TextView_question2.setPadding((int) (deviceWidth * 0.07f), 0, 0, 0);
				
		rLayout1.setPadding(0, (int) (deviceWidth * 0.02f), 0, (int) (deviceWidth * 0.02f));
		rLayout2.setPadding(0, (int) (deviceWidth * 0.02f), 0, (int) (deviceWidth * 0.02f));
		rLayout3.setPadding(0, (int) (deviceWidth * 0.02f), 0, (int) (deviceWidth * 0.02f));
		rLayout4.setPadding(0, (int) (deviceWidth * 0.02f), 0, (int) (deviceWidth * 0.02f));
				
		txt_option_1.setPadding((int) (deviceWidth * 0.07f), 0, 0, 0);
		txt_option_2.setPadding((int) (deviceWidth * 0.07f), 0, 0, 0);
		txt_option_3.setPadding((int) (deviceWidth * 0.07f), 0, 0, 0);
		txt_option_4.setPadding((int) (deviceWidth * 0.07f), 0, 0, 0);
			
	}

	private void assignClicks() {

		imgView_back.setOnClickListener(this);
		rLayout1.setOnClickListener(this);
		rLayout2.setOnClickListener(this);
		rLayout3.setOnClickListener(this);
		rLayout4.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgView_back:
			onBackPressed();
			break;
		case R.id.rLayout1:
			a = a + 1;
			c = 1;
			Count.start();
			ans = txt_option_1.getText().toString().trim();
			setQuestions();
			break;
		case R.id.rLayout2:
			a = a + 1;
			c = 2;
			Count.start();
			ans = txt_option_2.getText().toString().trim();
			setQuestions();
			break;
		case R.id.rLayout3:
			a = a + 1;
			c = 3;
			Count.start();
			ans = txt_option_3.getText().toString().trim();
			setQuestions();
			break;
		case R.id.rLayout4:
			a = a + 1;
			c = 4;
			Count.start();
			ans = txt_option_4.getText().toString().trim();
			setQuestions();
			break;
		default:
			break;
		}
	}
	
	private void setQuestions() {

		if (a < TableData.size()) {
			if (ans.equals(TableData.get(a - 1).get("answer"))) {
				scroeCount = scroeCount + 1;
				TextView_question2.setText(TableData.get(a).get("question"));
				if (c == 1) {
					txt_option_1.setText(TableData.get(a).get("option2").trim());
					txt_option_2.setText(TableData.get(a).get("option3").trim());
					txt_option_3.setText(TableData.get(a).get("option4").trim());
					txt_option_4.setText(TableData.get(a).get("option1").trim());

				} else if (c == 2) {
					txt_option_1.setText(TableData.get(a).get("option3").trim());
					txt_option_2.setText(TableData.get(a).get("option4").trim());
					txt_option_3.setText(TableData.get(a).get("option1").trim());
					txt_option_4.setText(TableData.get(a).get("option2").trim());

				} else if (c == 3) {
					txt_option_1.setText(TableData.get(a).get("option4").trim());
					txt_option_2.setText(TableData.get(a).get("option3").trim());
					txt_option_3.setText(TableData.get(a).get("option2").trim());
					txt_option_4.setText(TableData.get(a).get("option1").trim());

				} else {
					txt_option_1.setText(TableData.get(a).get("option3").trim());
					txt_option_2.setText(TableData.get(a).get("option1").trim());
					txt_option_3.setText(TableData.get(a).get("option4").trim());
					txt_option_4.setText(TableData.get(a).get("option2").trim());
				}

			} else {
				b = 1;
				Count.cancel();
				clickLost();
			}

		} else if (a == TableData.size()) {
			if (ans.equals(TableData.get(a - 1).get("answer"))) {
				scroeCount = scroeCount + 1;
				Count.cancel();
				clickWon();
			} else {
				b = 1;
				Count.cancel();
				clickLost();
			}
		}
	}

	private void clickLost() {
		Intent intent = new Intent(activity, LostScoreActivity.class);
		intent.putExtra("scroeCount", scroeCount);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		activity.finish();
	}

	private void clickWon() {
		Intent intent = new Intent(activity, WinScoreActivity.class);
		intent.putExtra("scroeCount", scroeCount);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		activity.finish();
	}

	private void setAdapter() {
		// Make an array adapter using the built in android layout to render a list of strings
		horizontalAdapter = new HorizontalAdapter(activity, imageId);
		// Assign adapter to HorizontalListView
		myHzntListView.setAdapter(horizontalAdapter);
	}
	
	@Override
	public void onBackPressed() {
	    	Count.cancel();
			Intent intent = new Intent(activity, JoiningRoomActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			activity.finish();
	}
}
