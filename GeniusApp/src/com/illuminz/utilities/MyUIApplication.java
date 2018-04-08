package com.illuminz.utilities;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyUIApplication extends Application {

	private static MyUIApplication instance;
	public static int deviceWidth;
	public static int deviceHeight;
	public MyUIApplication() {
		instance = this;
	}

	public static Context getContext() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}
	public boolean isInternetAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// test for connection
		if (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			// Log.v("APP_ROOT", "Internet Connection Not Present");
			return false;
		}
	}
	/* for getting device height and width */
	@SuppressWarnings("deprecation")
	public int[] getDeviceSize(Context context) {
		WindowManager mWinMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int device[] = { mWinMgr.getDefaultDisplay().getHeight(),mWinMgr.getDefaultDisplay().getWidth() };
		return device;

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

	public void CustomAlertPopup(final Context context, String title,String message) {
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			LinearLayout mainLayout = new LinearLayout(context);
			mainLayout.setGravity(Gravity.CENTER_HORIZONTAL);
			mainLayout.setOrientation(LinearLayout.VERTICAL);
			mainLayout.setMinimumHeight(80);
			mainLayout.setPadding(8, 8, 8, 8);
			TextView titleTextView = new TextView(context);
			titleTextView.setText(title);
			titleTextView.setTextSize(18);
			titleTextView.setTextColor(Color.WHITE);
			titleTextView.setGravity(Gravity.CENTER);
			TextView messageTextView = new TextView(context);
			messageTextView.setText(message);
			messageTextView.setTextSize(14);
			messageTextView.setTextColor(Color.WHITE);
			messageTextView.setGravity(Gravity.CENTER);
			mainLayout.addView(titleTextView);
			mainLayout.addView(messageTextView);
			alertDialogBuilder.setView(mainLayout);
			alertDialogBuilder.setCancelable(false).setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}
					});

			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}
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
}
