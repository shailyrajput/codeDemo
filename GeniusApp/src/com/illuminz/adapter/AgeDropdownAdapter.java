package com.illuminz.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.illuminz.geniusapp.R;
import com.illuminz.utilities.MyUIApplication;

public class AgeDropdownAdapter extends ArrayAdapter<String>{
	private Activity activity;
	int deviceHeight,deviceWidth;
	ArrayList<String> _objlist;
	private int selectedPosition;

	public AgeDropdownAdapter(Activity a, int textViewResourceId,ArrayList<String> _objList,int selectedPosition,boolean checkbox ) {
		super(a, textViewResourceId, _objList);
		activity = a;
		this._objlist = _objList;
		this.selectedPosition= selectedPosition;
		DisplayMetrics displaymetrics = activity.getResources().getDisplayMetrics();
		deviceHeight = displaymetrics.heightPixels;
		deviceWidth = displaymetrics.widthPixels;

	}

	public static class ViewHolder{
		public TextView Text;
		public ImageView checkBox;

		//ADDED
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;

		try
		{
			final ViewHolder holder;
			if (v == null)
			{		
				LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.drop_down_list, null);

				v.setMinimumHeight((int)(.035f*deviceHeight));

				holder = new ViewHolder();
				holder.Text = (TextView) v.findViewById(R.id.Text);
				holder.checkBox = (ImageView) v.findViewById(R.id.checkBox);

				RelativeLayout.LayoutParams Params_Text = (RelativeLayout.LayoutParams)holder.Text.getLayoutParams();
				Params_Text.height = (int)(deviceHeight*.035f);
				Params_Text.width = (int)(deviceWidth*.35f);
				Params_Text.leftMargin = (int)(deviceWidth*.015f);
				holder.Text.setLayoutParams(Params_Text);

				holder.Text.setTextSize(TypedValue.COMPLEX_UNIT_PX,MyUIApplication.determineTextSize(holder.Text.getTypeface(),(0.35f*.1f*deviceHeight)));
				holder.Text.setPadding((int)(0.02f*deviceWidth),0,(int)(0.02f*deviceWidth) ,0);

				v.setTag(holder);
			}
			else
				holder=(ViewHolder)v.getTag();

			// v.setBackgroundResource(R.drawable.listviewclick);

			if (_objlist.get(position) != null) {

				holder.Text.setText(_objlist.get(position));
				RelativeLayout.LayoutParams params_Img = (RelativeLayout.LayoutParams)holder.checkBox.getLayoutParams();
				params_Img.height=(int)(deviceHeight*.04f);
				params_Img.width=(int)(deviceHeight*.04f);
				params_Img.rightMargin=(int)(deviceWidth*.01f);
				holder.checkBox.setLayoutParams(params_Img);
				
				if(selectedPosition == position)
				{
					//ImageUtil.setBackgroundBgFromAssetsNotCache(holder.checkBox, activity,"Home/check_sel.png",(deviceHeight*.04f),(deviceHeight*.04f));
				}
				else 
				{
					//ImageUtil.setBackgroundBgFromAssetsNotCache(holder.checkBox, activity,"Home/check_nonsel.png",(deviceHeight*.04f),(deviceHeight*.04f));
				}
			}
		}catch (Exception e) {
			System.out.println("exception "+e);
		}
		return v;
	}
}

