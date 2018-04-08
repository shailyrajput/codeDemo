package com.illuminz.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.illuminz.geniusapp.R;
import com.illuminz.utilities.MyUIApplication;
import com.illuminz.utilities.UtilityClass;

public class ScoreAdapter extends BaseAdapter {
	Activity mActivity;
	View v;
	int deviceHeight, deviceWidth;
	private static LayoutInflater inflater = null;
	ArrayList<HashMap<String, String>> scoreData =null;
	String UserName, numberOfPlayerStr;
	
	public ScoreAdapter(Activity activity, ArrayList<HashMap<String, String>> scoreList2, String numberOfPlayer2) {
		this.mActivity = activity;
	   	inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.scoreData = scoreList2;
		this.numberOfPlayerStr = numberOfPlayer2;
		DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
		deviceHeight = displayMetrics.heightPixels;
		deviceWidth = displayMetrics.widthPixels;
		UserName = UtilityClass.getQuizUserNameFromSharedPreference(mActivity);
		
	}

	@Override
	public int getCount() {
		return scoreData.size();
	}

	@Override
	public Object getItem(int position) {
		return scoreData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.score_item, parent, false);
		}
	    RelativeLayout itemLayout = (RelativeLayout)view.findViewById(R.id.itemLayout);
		TextView name = (TextView) view.findViewById(R.id.nametxt);
		TextView score = (TextView) view.findViewById(R.id.scoretxt);
		TextView ranktxt = (TextView) view.findViewById(R.id.ranktxt);
		name.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(name.getTypeface(),(int) (0.40f * .1f * deviceHeight)));
		score.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(score.getTypeface(),(int) (0.40f * .1f * deviceHeight)));
		ranktxt.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(ranktxt.getTypeface(),(int) (0.40f * .1f * deviceHeight)));
		
		if (numberOfPlayerStr.equals("1")) {
			name.setText(scoreData.get(position).get("name"));
			ranktxt.setText(scoreData.get(position).get("score"));
		
		} else {
			if (UserName.equals(scoreData.get(position).get("name"))) {
				itemLayout.setBackgroundColor(Color.YELLOW);
			} 
			name.setText(scoreData.get(position).get("name"));
			String scorevalue = scoreData.get(position).get("score");
			if (scorevalue.equals(null) || scorevalue.equals("") || scorevalue.equals("null")) {
				score.setText("Playing...");
			} else {
				score.setText(scorevalue);
			}
			
			ranktxt.setText("" + (position + 1));
		}
	
		return view;
	}
}

