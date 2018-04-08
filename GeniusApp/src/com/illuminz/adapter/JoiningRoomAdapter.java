package com.illuminz.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.illuminz.geniusapp.R;

public class JoiningRoomAdapter extends BaseAdapter {
	public int deviceWidth, deviceHeight;
    Activity mActivity;
	int[] value;
	private LayoutInflater inflater;
	public JoiningRoomAdapter(Activity activity ,int[] imageId) {
		this.mActivity = activity;
		this.value = imageId;
			
		inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DisplayMetrics displaymetrics = mActivity.getResources().getDisplayMetrics();
		deviceHeight = displaymetrics.heightPixels;
		deviceWidth = displaymetrics.widthPixels;
	}

	@Override
	public int getCount() {
	
		return value.length;
	}

	@Override
	public Object getItem(int arg0) {
		
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		
		return 0;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		if (convertview == null) {
			convertview = inflater.inflate(R.layout.joining_room_item, null);
			RelativeLayout rLayout_main = (RelativeLayout) convertview.findViewById(R.id.rLayout_main);
			ImageView  imageview = (ImageView)convertview.findViewById(R.id.image_profile);
			
			rLayout_main.setLayoutParams(new RelativeLayout.LayoutParams((int) ((deviceWidth / 5)), (int) ((deviceWidth / 5))));
			imageview.setLayoutParams(new RelativeLayout.LayoutParams((int) ((deviceWidth )), (int) ((deviceWidth))));
			imageview.setImageResource(value[position]);
		}

		return convertview;
	}

}
