package net.nofool.dev.tbd;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService{
    private static final String TAG = "MyGcmListenerService";
    @Override
    public void onMessageReceived(String from, Bundle data){
        String message = data.getString("message");
        Log.d(TAG,"From: "+from);
        Log.d(TAG,"Message: "+message);

        if (message.equalsIgnoreCase("loc")){

        }
    }
}
