package com.isproject.ptps;

import android.content.SharedPreferences;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.isproject.ptps.activities.LandingTwoActivity;
import com.isproject.ptps.fragments.passenger.ViewVehicleFareChartFragment;

import androidx.annotation.NonNull;

public class FMS extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        String newToken = s;
        SharedPreferences sharedPreferences = getSharedPreferences(ViewVehicleFareChartFragment.SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("InstanceID", newToken);
        editor.commit();
    }
}
