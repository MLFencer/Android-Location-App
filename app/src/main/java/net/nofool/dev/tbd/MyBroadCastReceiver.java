package net.nofool.dev.tbd;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadCastReceiver extends BroadcastReceiver{

    String data;

    @Override
    public void onReceive(Context context, Intent intent){
       //Get Data From SQL Lite
        data = null;
        if (data.equalsIgnoreCase("child")) {
            context.startService(new Intent(context, ChildDetectService.class));
        } else {
            context.startService(new Intent(context, ParentDetectService.class));
        }
    }
}
