package com.isproject.ptps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.isproject.ptps.fragments.passenger.ViewVehicleFareChartFragment;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class FMS extends FirebaseMessagingService {

    private LocalBroadcastManager broadcaster;
    public static final String TAG = "FMS";
    public static final String CHANNEL_ID = "NOT_CH_ID_001";

    @Override
    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if(remoteMessage.getNotification() != null) {
            Log.d(TAG, "Notification Title: " + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "Notification Message: " + remoteMessage.getNotification().getBody());

            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            Intent pushNotification = new Intent("PushNotification");
            pushNotification.putExtra("title", title);
            pushNotification.putExtra("body", body);
            pushNotification.putExtra("code", 0);

            broadcaster.sendBroadcast(pushNotification);
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        SharedPreferences sharedPreferences = getSharedPreferences(ViewVehicleFareChartFragment.SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("InstanceID", s);
        editor.apply();
    }
}
