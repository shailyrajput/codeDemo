package com.illuminz.notification;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.illuminz.geniusapp.JoiningRoomActivity;
import com.illuminz.geniusapp.R;

public class GCMNotificationIntentService extends IntentService 
{
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
    String SenderName,senderId, myMsg;
	public GCMNotificationIntentService()
	{
		super("GcmIntentService");
	}

	public static final String TAG = "GCMNotificationIntentService";

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
				sendNotification("Deleted messages on server: "	+ extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				for (int i = 0; i < 1; i++) 
				{
					Log.i(TAG,"Working... " + (i + 1) + "/5 @ "	+ SystemClock.elapsedRealtime());
					try 
					{
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
				//Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
				Object getMsgStr =  extras.get(GcmConstants.MESSAGE_KEY);
				sendNotification("" + extras.get(GcmConstants.MESSAGE_KEY));
				
				
				//System.out.println("msssssssggg>>>: "+getMsgStr);
				if (getMsgStr == null || getMsgStr == "null") {
					//System.out.println("Msg object is >>>: "+getMsgStr);
				} else {
					//Log.i(TAG, "Received: " + extras.toString());
					myMsg = extras.get("message").toString();
					//Log.i(TAG, "messageeeeeeeee: " + myMsg);

					Intent in = new Intent();
					in.setAction("com.illuminz.quizdemo.CHAT_DATA");
					in.putExtra("msg_data", myMsg);
					getApplicationContext().sendBroadcast(in);
				}
				

			}
		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String msg) {
		//Log.d(TAG, "Preparing to send notification NAME...:"+msg);
	
		if (msg.equals("null") || msg.equals(null)) {
			//Log.d(TAG, "Not broadcast");
		}else {
			//Log.d(TAG, "Preparing to send notification");
			mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,new Intent(this, JoiningRoomActivity.class), 0);
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this).setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle("Quizgame")
					.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
					.setContentText(msg)
					.setAutoCancel(true)
					.setVibrate(new long[]{1000,1000,1000})
					;
			Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			mBuilder.setSound(alarmSound);       
			mBuilder.setContentIntent(contentIntent);
			
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
			//Log.d(TAG, "Notification sent successfully.");
		}
		
	}
}
