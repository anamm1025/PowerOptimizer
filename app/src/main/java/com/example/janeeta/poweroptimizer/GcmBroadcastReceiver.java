package com.example.janeeta.poweroptimizer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onReceive(Context context, Intent intent) {


//		Toast.makeText(context,"GCM Broadcast received" , Toast.LENGTH_LONG).show();


		final NotificationManager mgr=
				(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		//Notification note=new Notification(android.R.drawable.alert_dark_frame,

		//      "Android Example Status message!",

		//    System.currentTimeMillis());
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
		// This pending intent will open after notification click
		PendingIntent i=PendingIntent.getActivity(context, 0,
				new Intent(context, NotifyMessage.class),
				0);

		mBuilder.setContentTitle("Notification Alert, Click Me!");
		mBuilder.setContentText("Hi, This is Android Notification Detail!");
		//After uncomment this line you will see number of notification arrived
		//note.number=2;
		mgr.notify(1337, mBuilder.build());



		// Explicitly specify that GcmMessageHandler will handle the intent.
		ComponentName comp = new ComponentName(context.getPackageName(),
				GcmMessageHandler.class.getName());

		// Start the service, keeping the device awake while it is launching.
		startWakefulService(context, (intent.setComponent(comp)));
		setResultCode(Activity.RESULT_OK);
	}
}