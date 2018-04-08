package com.illuminz.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.view.WindowManager;

@SuppressLint("SimpleDateFormat")
public class MyApplication extends Application {

	// public static ArrayList<GalleryWrapper> _objGalleryList;
	private static MyApplication instance;

	public String _objString;
	public long _objLong;
	
	public MyApplication() {
		instance = this;
	}

	public static boolean isFIrstTymLogger;


	public static Context getContext() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

	public String getDate_Time(long milis) {

		DateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");

		// Create a calendar object that will convert the date and time
		// value in
		// milliseconds to date.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milis);
		return formatter.format(calendar.getTime());

	}

	public long getCurrentTimeInMillis() {
		return (System.currentTimeMillis() / 1000);
	}

	public boolean isInternetAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// test for connection
		if (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			// Log.v("APP_ROOT", "Internet Connection Not Present");
			return false;
		}
	}

	/* for getting device height and width */
	@SuppressWarnings("deprecation")
	public int[] getDeviceSize(Context context) {

		WindowManager mWinMgr = (WindowManager) context	.getSystemService(Context.WINDOW_SERVICE);
		int device[] = { mWinMgr.getDefaultDisplay().getHeight() , mWinMgr.getDefaultDisplay().getWidth() };

		return device;

	}

	/* for alert dialog */

	@SuppressWarnings("deprecation")
	public void AlertPopUp(Context context, String txt, String title) {

		{
			AlertDialog alertDialog = new AlertDialog.Builder(context).create();
			alertDialog.setTitle(title);
			alertDialog.setMessage(txt);

			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// here you can add functions
					return;
				}
			});

			alertDialog.show();
		}
	}

	@SuppressWarnings("deprecation")
	public void AlertPopUp(Context context, String txt) {

		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		alertDialog.setMessage(txt);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// here you can add functions

			}
		});

		alertDialog.show();
		return;
	}

	
	public boolean isInternetAvailable() {
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// test for connection
		if (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			// Log.v("APP_ROOT", "Internet Connection Not Present");
			return false;
		}
	}

	public void AlertForInternet() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage("Internet not available");
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		alert.show();
	}

	public static float calculateHeight(FontMetrics fm) {
		return fm.bottom - fm.top;
	}

	// determineTextSize method
	public static int determineTextSize(Typeface font, float allowableHeight) {

		Paint p = new Paint();
		p.setTypeface(font);

		int size = (int) allowableHeight;
		p.setTextSize(size);

		float currentHeight = calculateHeight(p.getFontMetrics());

		while (size != 0 && (currentHeight) > allowableHeight) {
			p.setTextSize(size--);
			currentHeight = calculateHeight(p.getFontMetrics());
		}

		if (size == 0) {
			System.out.print("Using Allowable Height!!");
			return (int) allowableHeight;
		}

		System.out.print("Using size " + size);
		return size;
	}
	public void setNewsCountContentParserData(String allNewsCounts) {
		if (allNewsCounts != null) {
			_objString = new String();
			_objString = allNewsCounts;
		}
	}


}
