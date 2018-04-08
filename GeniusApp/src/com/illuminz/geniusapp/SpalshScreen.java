package com.illuminz.geniusapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.illuminz.utilities.UtilityClass;

@SuppressLint("WorldWriteableFiles")
public class SpalshScreen extends Activity 
{
	String ScreenName = "SpalshScreen";
	Activity activity = SpalshScreen.this;;
	String PREFS_NAME = "InstallDatabase";
	String DB_PATH;
	String DBNAME = "quizgamedb";
	String username, password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		getActionBar().hide();
		Log.d(ScreenName, "onCreate");
		username = UtilityClass.getQuizUserNameFromSharedPreference(activity);
		password = UtilityClass.getPasswordFromSharedPreference(activity);

		copyDatabase();
		int SecondDelayed = 1;
		new Handler().postDelayed(new Runnable() {
			public void run() {
				System.out.println(">>>username>>>"+username+">>>>password>>>>"+password);	
				if (username != "" || password != "") {

					Intent intent = new Intent(activity, RoomActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					//overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left);
					activity.finish();

				} else {
					Intent intent = new Intent(activity, MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					//overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left);
					activity.finish();

				}

			}
		}, SecondDelayed * 2000);
	}

	@Override
	public void onBackPressed() 
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
		return;
	}

	@SuppressLint("SdCardPath")
	private void copyDatabase() 
	{
		// TODO Auto-generated method stub
		@SuppressWarnings("deprecation")
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,MODE_WORLD_WRITEABLE);
		boolean silent = settings.getBoolean("install", false);
		System.out.println("SplashScreen.copyDatabase()..." + silent);
		if (!silent) 
		{
			DB_PATH = "/data/data/" + getApplicationContext().getPackageName()+ "/databases/";
			boolean dbexist = checkdatabase();
			if (dbexist) 
			{
				System.out.println("Database exists");
			} else 
			{
				copyAssets();
			}
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("install", true);
			editor.commit();
			System.out.println("Copy Complete...........");
		}
	}

	@SuppressLint("SdCardPath")
	private void copyAssets()
	{
		// TODO Auto-generated method stub
		String DB_PATH = "/data/data/"+ getApplicationContext().getPackageName() + "/databases/";
		AssetManager assetManager = getAssets();
		String[] files = null;
		try 
		{
			files = assetManager.list("");
		} catch (IOException e) 
		{
			Log.e("tag", e.getMessage());
		}
		for (String filename : files)
		{
			if (filename.equals(DBNAME))
			{
				DB_PATH = "/data/data/"	+ getApplicationContext().getPackageName()+ "/databases/";
				InputStream in = null;
				OutputStream out = null;
				try 
				{
					in = assetManager.open(filename);
					File dir = new File(DB_PATH);
					if (!dir.exists())
						dir.mkdir();
					out = new FileOutputStream(DB_PATH + filename);
					copyFile(in, out);
					in.close();
					in = null;
					out.flush();
					out.close();
					out = null;
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		}
	}

	private void copyFile(InputStream in, OutputStream out)
	{
		// TODO Auto-generated method stub
		byte[] buffer = new byte[1024];
		int read;
		try 
		{
			while ((read = in.read(buffer)) != -1) 
			{
				out.write(buffer, 0, read);
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private boolean checkdatabase() 
	{
		// TODO Auto-generated method stub
		boolean checkdb = false;
		try 
		{
			String myPath = DB_PATH + DBNAME;
			System.out.println("my path>>>>>>>>>>>>" + myPath);
			File dbfile = new File(myPath);
			checkdb = dbfile.exists();
		} catch (SQLiteException e) 
		{
			System.out.println("Database doesn't exist...");
		}
		return checkdb;
	}
}
