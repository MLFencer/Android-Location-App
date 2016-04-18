package net.nofool.dev.tbd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class LocationBroadCastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        double lon = intent.getDoubleExtra("Longitude",0.0);
        double lat=intent.getDoubleExtra("Latitude",0.0);

        Device app = new Device(null, null);
        app.setLat(lat);
        app.setLon(lon);

    }
}
