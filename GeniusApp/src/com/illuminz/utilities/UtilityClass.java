package com.illuminz.utilities;

import android.app.Activity;
import android.content.SharedPreferences;

public class UtilityClass {

	public static void setNameInSharedPreference(Activity context, String name) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		share.edit().putString(Constants.NAME, name).commit();
		System.out.println("name set>>>>>" + name);
	}

	public static String getNameFromSharedPreference(Activity context) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		return share.getString(Constants.NAME, "");
	}

	public static void setEmailInSharedPreference(Activity context, String email) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		share.edit().putString(Constants.EMAIL, email).commit();
		System.out.println("email id set>>>>>" + email);
	}

	public static String getEmailFromSharedPreference(Activity context) {
		SharedPreferences share = context.getSharedPreferences(	Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		return share.getString(Constants.EMAIL, "");
	}

	public static void setPasswordInSharedPreference(Activity context,String password) {
		SharedPreferences share = context.getSharedPreferences(	Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		share.edit().putString(Constants.PASSWORD, password).commit();
		System.out.println("password set>>>>>" + password);
	}

	public static String getPasswordFromSharedPreference(Activity context) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		return share.getString(Constants.PASSWORD, "");
	}

	public static void setVerificationCodeInSharedPreference(Activity context,String verificationCode) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		share.edit().putString(Constants.VERIFICATION_CODE, verificationCode).commit();
		System.out.println("verification code is set>>>>>" + verificationCode);
	}

	public static String getVerificationFromSharedPreference(Activity context) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		return share.getString(Constants.VERIFICATION_CODE, "");
	}

	public static void setLayoutChangeInSharedPreference(Activity context,String layoutchange) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		share.edit().putString(Constants.LAYOUT_CHANGE, layoutchange).commit();
		System.out.println("layout change set>>>>>" + layoutchange);
	}

	public static String getLayoutChangeFromSharedPreference(Activity context) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		return share.getString(Constants.LAYOUT_CHANGE, "");
	}

	public static void setQuizUserIdInSharedPreference(Activity context,String quiz_userid) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		share.edit().putString(Constants.QUIZ_USERID, quiz_userid).commit();
		System.out.println("id set>>>>>" + quiz_userid);
	}

	public static String getQuizUserIdFromSharedPreference(Activity context) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		return share.getString(Constants.QUIZ_USERID, "");
	}

	public static void setQuizUserNameInSharedPreference(Activity context,String quiz_username) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		share.edit().putString(Constants.QUIZ_USERNAME, quiz_username).commit();
		System.out.println("name set>>>>>" + quiz_username);
	}

	public static String getQuizUserNameFromSharedPreference(Activity context) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		return share.getString(Constants.QUIZ_USERNAME, "");
	}

	public static void setQuizGameIDInSharedPreference(Activity context,String game_id) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		share.edit().putString(Constants.QUIZ_GAME_ID, game_id).commit();
		System.out.println("game id>>>>>" + game_id);
	}

	public static String getQuizGameIDFromSharedPreference(Activity context) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		return share.getString(Constants.QUIZ_GAME_ID, "");
	}

	public static void setFlagSharedPreference(Activity context, String flag) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		share.edit().putString(Constants.FLAG, flag).commit();
		System.out.println("number of player>>>>>" + flag);
	}

	public static String getFlagFromSharedPreference(Activity context) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		return share.getString(Constants.FLAG, "");
	}

	public static void removeQuizUserNameFromSharedPreference(Activity context) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		share.edit().remove(Constants.QUIZ_USERNAME).commit();
		System.out.println("Constants.QUIZ_USERNAME>>>>>" + Constants.QUIZ_USERNAME);
	}

	public static void removeEmailFromSharedPreference(Activity context) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		share.edit().remove(Constants.EMAIL).commit();
		System.out.println("Constants.QUIZ_USERNAME>>>>>" + Constants.QUIZ_USERNAME);
	}

	public static void removePasswordFromSharedPreference(Activity context) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		share.edit().remove(Constants.PASSWORD).commit();
		System.out.println("Constants.PASSWORD>>>>>"+ Constants.PASSWORD);
	}
	
	public static void setDeviceIDInSharedPreference(Activity context, String deviceid) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		share.edit().putString(Constants.DEVICE_ID, deviceid).commit();
		System.out.println("name set>>>>>" + deviceid);
	}

	public static String getDeviceIdFromSharedPreference(Activity context) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		return share.getString(Constants.DEVICE_ID, "");
	}

	
	
	public static void setLastUpdatedTimeInSharedPreference(Activity context, String deviceid) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		share.edit().putString(Constants.DEVICE_ID, deviceid).commit();
		System.out.println("name set>>>>>" + deviceid);
	}

	public static String getLastUpdatedTimeFromSharedPreference(Activity context) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		return share.getString(Constants.DEVICE_ID, "");
	}
	
	
	
	public static void setWaitingFlagInSharedPreference(Activity context, String waitingflag) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		share.edit().putString(Constants.WAITING_FLAG, waitingflag).commit();
		System.out.println("name set>>>>>" + waitingflag);
	}

	public static String getWaitingFlagFromSharedPreference(Activity context) {
		SharedPreferences share = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		return share.getString(Constants.WAITING_FLAG, "");
	}

}
