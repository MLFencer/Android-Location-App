package net.nofool.dev.tbd;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

public class MessageNotification extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        String sub = intent.getStringExtra("msg");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(sub);
        builder.setPositiveButton("Ok",null);
        final AlertDialog alert = builder.create();
        alert.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alert.show();
    }
}
