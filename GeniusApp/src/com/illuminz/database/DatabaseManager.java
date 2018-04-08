package com.illuminz.database;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseManager {
	DatabaseHelper databasehelper;
	String DatabasePath;
	static int DATABASE_VERSION = 1;
	Activity activity;
	Context mContext;
	private static SQLiteDatabase sqLiteDatabase;
	String Tag = "DatabaseManager";
	static String DatabaseName = AllConstants.DatabaseName;

	/** ..................................tables.............................. */
	
	static String TABLE_QUEANS = AllConstants.questionAnswerTable;
	static String TABLE_AVERAGE = AllConstants.averageTable;
	static String TABLE_USER_INFO = AllConstants.userTable;

	/** ...............DATABASE HELPER.................................. */
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DatabaseName, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

	/** ..................CREATE CONSTRUCTOR FOR CONTEXT............... */
	public DatabaseManager(Context context) {
		this.mContext = context;
		databasehelper = new DatabaseHelper(context);
	}

	/** ..................CREATE CONSTRUCTOR FOR ACTIVITY............... */
	public DatabaseManager(Activity activity) {
		this.activity = activity;
		databasehelper = new DatabaseHelper(activity);
	}

	public DatabaseManager open() {
		sqLiteDatabase = databasehelper.getWritableDatabase();
		return this;
	}

	public void close() {
		databasehelper.close();
	}

	
	public void SaveQuestionData(String id, String question,String option1, String option2, String option3, String option4, String answer) {
		ContentValues cv = new ContentValues();
		cv.put("id", id);
		cv.put("question", question);
		cv.put("option1", option1);
		cv.put("option2", option2);
		cv.put("option3", option3);
		cv.put("option4", option4);
		cv.put("answer", answer);
		sqLiteDatabase.insert(TABLE_QUEANS, null, cv);
	}

	public void updateQuestionData(String id, String question,String option1, String option2, String option3, String option4, String answer) {
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put("id", id);
		cvUpdate.put("question", question);
		cvUpdate.put("option1", option1);
		cvUpdate.put("option2", option2);
		cvUpdate.put("option3", option3);
		cvUpdate.put("option4", option4);
		cvUpdate.put("answer", answer);
		sqLiteDatabase.update(TABLE_QUEANS, cvUpdate, "id" + "=" + id,	null);

	}
	
	
	public ArrayList<HashMap<String, String>> ReteriveTableData() {
		HashMap<String, String> myHashMap = new HashMap<String, String>();
		ArrayList<HashMap<String, String>> myArrayList = new ArrayList<HashMap<String, String>>();
		Cursor mCursor = null;
		String[] field = new String[] { "id", "question", "option1", "option2","option3","option4", "answer" };
		mCursor = sqLiteDatabase.query(TABLE_QUEANS, field, null, null, null,
				null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		if (mCursor.getCount() > 0) {
			do {
				myHashMap = new HashMap<String, String>();
				myHashMap.put("id", mCursor.getString(0));
				myHashMap.put("question", mCursor.getString(1));
				myHashMap.put("option1", mCursor.getString(2));
				myHashMap.put("option2", mCursor.getString(3));
				myHashMap.put("option3", mCursor.getString(4));
				myHashMap.put("option4", mCursor.getString(5));
				myHashMap.put("answer", mCursor.getString(6));
				myArrayList.add(myHashMap);
			} while (mCursor.moveToNext());
		}
	
		return myArrayList;
	}
	
	/**..........................USer data................................*/
	
	
	public void SaveUserInfoData(String username , String eamil, String password) {
		ContentValues cv = new ContentValues();
		//cv.put("id", id);
		cv.put("username", username);
		cv.put("eamil", eamil);
		cv.put("password", password);
		sqLiteDatabase.insert(TABLE_USER_INFO, null, cv);
		//System.out.println(">>>>>>>>>>>>>>>data inserted<<<<<<<<<<<<<<<<<<<<");
	}

	public void updateUserInfoData(String id,String username , String eamil, String password) {
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put("id", id);
		cvUpdate.put("username", username);
		cvUpdate.put("eamil", eamil);
		cvUpdate.put("password", password);
		sqLiteDatabase.update(TABLE_USER_INFO, cvUpdate, "id" + "=" + id,	null);

	}
	
	public ArrayList<HashMap<String, String>> ReteriveUserInfoData() {
		HashMap<String, String> myHashMap = new HashMap<String, String>();
		ArrayList<HashMap<String, String>> myArrayList = new ArrayList<HashMap<String, String>>();
		Cursor mCursor = null;
		String[] field = new String[] { "id", "username","eamil","password"};
		mCursor = sqLiteDatabase.query(TABLE_USER_INFO, field, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		if (mCursor.getCount() > 0) {
			do {
				myHashMap = new HashMap<String, String>();
				myHashMap.put("id", mCursor.getString(0));
				myHashMap.put("username", mCursor.getString(1));
				myHashMap.put("eamil", mCursor.getString(2));
				myHashMap.put("password", mCursor.getString(3));
				myArrayList.add(myHashMap);
			} while (mCursor.moveToNext());
		}
		return myArrayList;
	}
	
	public void deleteUserInfoDataRecord() {
		
		sqLiteDatabase.delete(TABLE_USER_INFO, null, null) ;
		
		//System.out.println(">>>>>>>>>>>>>>>data deleted<<<<<<<<<<<<<<<<<<<<");
		
	}
	
	
	/**..........................AVERAGE................................*/
	public void SaveTableData(String score) {
		ContentValues cv = new ContentValues();
		//cv.put("id", id);
		cv.put("score", score);
		sqLiteDatabase.insert(TABLE_AVERAGE, null, cv);
	}

	public void updateTableData(String id, String score ) {
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put("id", id);
		cvUpdate.put("score", score);
		sqLiteDatabase.update(TABLE_AVERAGE, cvUpdate, "id" + "=" + id,	null);

	}
	
	public ArrayList<HashMap<String, String>> ReteriveAverageTableData() {
		HashMap<String, String> myHashMap = new HashMap<String, String>();
		ArrayList<HashMap<String, String>> myArrayList = new ArrayList<HashMap<String, String>>();
		Cursor mCursor = null;
		String[] field = new String[] { "id", "score"};
		mCursor = sqLiteDatabase.query(TABLE_AVERAGE, field, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		if (mCursor.getCount() > 0) {
			do {
				myHashMap = new HashMap<String, String>();
				myHashMap.put("id", mCursor.getString(0));
				myHashMap.put("score", mCursor.getString(1));
				myArrayList.add(myHashMap);
			} while (mCursor.moveToNext());
		}
		return myArrayList;
	}
	
	
	public boolean deleteTableRecord() {
		try {
			return sqLiteDatabase.delete(TABLE_AVERAGE, null, null) > 0;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	

}
