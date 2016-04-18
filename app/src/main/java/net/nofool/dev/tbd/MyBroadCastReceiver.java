package net.nofool.dev.tbd;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadCastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent){
        context.startService(new Intent(context, ChildDetectService.class));
    }
}
