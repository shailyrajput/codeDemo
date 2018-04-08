package com.illuminz.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.illuminz.geniusapp.R;
import com.illuminz.utilities.DateDisplay;
import com.illuminz.utilities.MyUIApplication;

public class ProfileAdapter extends BaseExpandableListAdapter {
	
	Activity mActivity;
	View v;
    static LayoutInflater inflater = null;
	ArrayList<HashMap<String, String>> profileData = null;
	private List<String> _listDataHeader;
	private HashMap<String, List<String>> _listDataChild;
	@SuppressWarnings("unused")
	private int deviceHeight, deviceWidth;

	public ProfileAdapter(Activity activity, List<String> _listDataHeader,HashMap<String, List<String>> _listDataChild) {
		
		this.mActivity = activity;
		this._listDataHeader = _listDataHeader;
		this._listDataChild = _listDataChild;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DisplayMetrics displayMetrics = activity.getResources()	.getDisplayMetrics();
		deviceHeight = displayMetrics.heightPixels;
		deviceWidth = displayMetrics.widthPixels;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
	}
	
	@Override
	public long getChildId(int groupPosition, int childPosition) {
  		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
		final String childText = (String) getChild(groupPosition, childPosition);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item, null);
		}
		TextView txtView_gameName = (TextView) convertView.findViewById(R.id.txtView_gameName);
		TextView txtView_Date = (TextView) convertView.findViewById(R.id.txtView_Date);
		txtView_gameName.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txtView_gameName.getTypeface(), (int)(0.40f*.1f*deviceHeight)));
		txtView_Date.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(txtView_Date.getTypeface(), (int)(0.30f*.1f*deviceHeight)));
		
		String numberOfPlayer = childText.substring(0,childText.indexOf("_"));
		//String gameId = childText.substring(childText.indexOf("_")	+ 1, childText.lastIndexOf("_"));
		String DateString = childText.substring(childText.lastIndexOf("_")	+ 1, childText.length());
		String Date = DateDisplay.getDataChangeFormate(DateString);
		
		if (numberOfPlayer.equals("1")) {
			txtView_gameName.setText("Single Player");
		} else if (numberOfPlayer.equals("2")) {
			txtView_gameName.setText("One on One Player");
		}else if (numberOfPlayer.equals("5")){
			txtView_gameName.setText("Five Player");
		}else if (numberOfPlayer.equals("10")){
			txtView_gameName.setText("Ten Player");
		}else if (numberOfPlayer.equals("25")){
			txtView_gameName.setText("Twenty Five Player");
		}
			
		txtView_Date.setText(Date);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
    	return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) 
		{
			LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_group, null);
		}

		TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
		lblListHeader.setTextSize(TypedValue.COMPLEX_UNIT_PX, MyUIApplication.determineTextSize(lblListHeader.getTypeface(), (int)(0.45f*.1f*deviceHeight)));
		lblListHeader.setTypeface(null, Typeface.ITALIC);
		lblListHeader.setText(headerTitle);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
