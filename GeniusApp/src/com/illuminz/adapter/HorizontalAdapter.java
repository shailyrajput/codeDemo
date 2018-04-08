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
import android.widget.TextView;

import com.illuminz.geniusapp.R;

public class HorizontalAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    int deviceHeight,deviceWidth;
    Activity mActivity;
	int[] values;

    public HorizontalAdapter(Activity activity, int[] imageId) {
    	this.mActivity = activity;
		this.values = imageId;
	    mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     	DisplayMetrics displayMetrics = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		deviceHeight = displayMetrics.heightPixels;
		deviceWidth = displayMetrics.widthPixels;
    }
    @Override
	public int getCount() {
		
		return values.length;
	}

	@Override
	public String getItem(int arg0) {
		
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		
		return 0;
	}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            // Inflate the view since it does not exist
            convertView = mInflater.inflate(R.layout.myhorizontal_item, parent, false);
            holder = new Holder();
            holder.rLayout_main = (RelativeLayout)convertView.findViewById(R.id.rLayoutData);
            holder.img_mask_copy = (ImageView)convertView.findViewById(R.id.img_mask_copy);
            holder.img_cross = (ImageView)convertView.findViewById(R.id.img_cross);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        
        RelativeLayout.LayoutParams  rLayout_mainParams = (RelativeLayout.LayoutParams)  holder.rLayout_main.getLayoutParams();
        rLayout_mainParams.width = (int) (deviceHeight * .13f);
        rLayout_mainParams.height = (int) (deviceHeight * .13f);
		holder.rLayout_main.setLayoutParams(rLayout_mainParams);
		
        
		RelativeLayout.LayoutParams  img_mask_copyParams = (RelativeLayout.LayoutParams)  holder.img_mask_copy.getLayoutParams();
		img_mask_copyParams.width = (int) (deviceHeight * .11f);
		img_mask_copyParams.height = (int) (deviceHeight * .11f);
		holder.img_mask_copy.setLayoutParams(img_mask_copyParams);
		
		RelativeLayout.LayoutParams  img_crossParams = (RelativeLayout.LayoutParams)  holder.img_cross.getLayoutParams();
		img_crossParams.width = (int) (deviceHeight * .05f);
		img_crossParams.height = (int) (deviceHeight * .05f);
		holder.img_cross.setLayoutParams(img_crossParams);
	
		holder.rLayout_main.setPadding((int) (deviceHeight * .01f), (int) (deviceHeight * .01f), (int) (deviceHeight * .01f), (int) (deviceHeight * .01f));
		
		holder.img_mask_copy.setImageResource(values[position]);
        return convertView;
    }

    /** View holder for the views we need access to */
    private static class Holder {
    	RelativeLayout rLayout_main;
    	ImageView img_mask_copy, img_cross;
    	TextView title;
    	
    }
}
