package net.nofool.dev.tbd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


public class LocationBroadCastReciever extends BroadcastReceiver {

    final String PREFS="dev.nofool.net.tbd";
    @Override
    public void onReceive(Context context, Intent intent) {
        double lon = intent.getDoubleExtra("Longitude",0.0);
        double lat=intent.getDoubleExtra("Latitude",0.0);

        SharedPreferences s = context.getSharedPreferences(PREFS,0);
        s.edit().putString("thisLon",lon+"").commit();
        s.edit().putString("thisLat",lat+"").commit();

    }

}
