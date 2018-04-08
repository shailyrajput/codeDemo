package com.illuminz.geniusapp;

import com.illuminz.utilities.Constants;
import com.illuminz.utilities.MyUIApplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WinScoreActivity extends Activity implements OnClickListener{
	
	Activity activity = WinScoreActivity.this;
	Context context;
	int deviceHeight,deviceWidth;
	private RelativeLayout rlayout_main, rlayout_wonView;
	private ImageView imgView_back, imgView_logo, imgView_won;
	private TextView textView_players, txtView_youWon, txtView_yourScore1, txtView_yourScore2;
	private Button imgView_playAgain,imgView_leaderboard;
	private int scroeCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.win_screen);
		getActionBar().hide();
		getDeviceParams();
		initializeView();
		getValueFromIntent();
		setViews();
		assignClicks();
	    
	}
	
	private void getValueFromIntent() 
	{
		Intent i = getIntent();
		Bundle mBundle = i.getExtras();
		if (mBundle != null) 
		{
			scroeCount = mBundle.getInt("scroeCount");
			txtView_yourScore2.setText(""+scroeCount);
			
		}
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
		rlayout_wonView = (RelativeLayout) findViewById(R.id.rlayout_wonView); 
		imgView_back = (ImageView) findViewById(R.id.imgView_back);
		imgView_logo = (ImageView) findViewById(R.id.imgView_logo);
		imgView_won = (ImageView) findViewById(R.id.imgView_won);
		imgView_playAgain = (Button) findViewById(R.id.imgView_playAgain);
		imgView_leaderboard = (Button) findViewById(R.id.imgView_leaderboard);
		textView_players = (TextView) findViewById(R.id.textView_players);
		txtView_youWon = (TextView) findViewById(R.id.txtView_youWon);
		txtView_yourScore1 = (TextView) findViewById(R.id.txtView_yourScore1);
		txtView_yourScore2 = (TextView) findViewById(R.id.txtView_yourScore2);
		
		if (Constants.numberOfPlayers == 1) {
			textView_players.setText(""+Constants.numberOfPlayers+" Player");
		} else {
			textView_players.setText(""+Constants.numberOfPlayers+" Players");
		}
	}
	
	private void setViews() {
		textView_players.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(textView_players.getTypeface(),(int) (0.55f * .1f * deviceHeight)));
		txtView_youWon.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txtView_youWon.getTypeface(),(int) (0.90f * .1f * deviceHeight)));
		txtView_yourScore1.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txtView_yourScore1.getTypeface(),(int) (0.55f * .1f * deviceHeight)));
		txtView_yourScore2.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txtView_yourScore2.getTypeface(),(int) (0.90f * .1f * deviceHeight)));
		imgView_playAgain.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(imgView_playAgain.getTypeface(),(int) (0.55f * .1f * deviceHeight)));
		imgView_leaderboard.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(imgView_leaderboard.getTypeface(),(int) (0.55f * .1f * deviceHeight)));
				
		RelativeLayout.LayoutParams imgView_backParams = (RelativeLayout.LayoutParams) imgView_back.getLayoutParams();
		imgView_backParams.width = (int) (deviceHeight * .08f);
		imgView_backParams.height = (int) (deviceHeight * .08f);
		imgView_back.setLayoutParams(imgView_backParams);
		
		RelativeLayout.LayoutParams imgView_logoParams = (RelativeLayout.LayoutParams) imgView_logo.getLayoutParams();
		imgView_logoParams.width = (int) (deviceHeight * .08f);
		imgView_logoParams.height = (int) (deviceHeight * .08f);
		imgView_logo.setLayoutParams(imgView_logoParams);
							
		RelativeLayout.LayoutParams rlayout_wonViewParam = (RelativeLayout.LayoutParams) rlayout_wonView.getLayoutParams();
		rlayout_wonViewParam.setMargins((int) (deviceHeight * .05f), (int) (deviceHeight * .05f),(int) (deviceHeight * .05f), (int) (deviceHeight * .05f));
		rlayout_wonView.setLayoutParams(rlayout_wonViewParam);
	
		RelativeLayout.LayoutParams imgView_wonParams = (RelativeLayout.LayoutParams) imgView_won.getLayoutParams();
		imgView_wonParams.width = (int) (deviceHeight * .20f);
		imgView_wonParams.height = (int) (deviceHeight * .25f);
		imgView_won.setLayoutParams(imgView_wonParams);
									
		rlayout_main.setPadding((int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.03f), (int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.05f));
		rlayout_wonView.setPadding((int) (deviceWidth * 0.05f), (int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.03f), (int) (deviceWidth * 0.03f));
		imgView_playAgain.setPadding((int) (deviceWidth * 0.03f), (int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.03f), (int) (deviceWidth * 0.02f));
		imgView_leaderboard.setPadding((int) (deviceWidth * 0.03f), (int) (deviceWidth * 0.02f), (int) (deviceWidth * 0.03f), (int) (deviceWidth * 0.02f));
			
	}
	private void assignClicks() {
		
		imgView_back.setOnClickListener(this);
		imgView_playAgain.setOnClickListener(this);
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgView_back:
			onBackPressed();
			break;
		case R.id.imgView_playAgain:
			onBackPressed();
			break;
		
		default:
			break;
		}
	}
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(activity, RoomActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left,	R.anim.slide_out_right);
		activity.finish();

	}
}
