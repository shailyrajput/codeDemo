package com.facebook.androidLogin;

import com.facebook.android.Facebook;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionStore {
    
    private static final String TOKEN = "access_token";
    private static final String EXPIRES = "expires_in";
    private static final String FORCE_DIALOG = "facebook-force-dialog";
    private static final String FORCE_DIALOG_KEY = "facebook-force-dialog-key";
    private static final String KEY = "facebook-session";
    
    @SuppressWarnings("static-access")
	public static boolean save(Facebook session, Context context) {
    	System.out.println("SessionStore.save()");
        Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.putString(TOKEN, session.getAccessToken());
        editor.putLong(EXPIRES, session.getAccessExpires());
        return editor.commit();
    }
    
    @SuppressWarnings("static-access")
	public static boolean restore(Facebook session, Context context) {
    	System.out.println("SessionStore.restore()");
        SharedPreferences savedSession =  context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        session.setAccessToken(savedSession.getString(TOKEN, null));
        session.setAccessExpires(savedSession.getLong(EXPIRES, 0));
        return session.isSessionValid();
    }

    public static void clear(Context context) {
    	System.out.println("SessionStore.clear()");
        Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
   
    public static boolean saveForceDialog(Context context, boolean force) {
    	System.out.println("SessionStore.saveForceDialog()");
        Editor editor = context.getSharedPreferences(FORCE_DIALOG_KEY, Context.MODE_PRIVATE).edit();
        editor.putBoolean(FORCE_DIALOG, force);
        return editor.commit();
    }
    public static boolean isForceDialog(Context context) {
    	System.out.println("SessionStore.isForceDialog()");
        SharedPreferences savedSession = context.getSharedPreferences(FORCE_DIALOG_KEY, Context.MODE_PRIVATE);
        return savedSession.getBoolean(FORCE_DIALOG, false);
    }
}
