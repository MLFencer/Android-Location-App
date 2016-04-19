package net.nofool.dev.tbd;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyGcmListenerService extends GcmListenerService{
    private static final String TAG = "MyGcmListenerService";
    final String PREFS="dev.nofool.net.tbd";
    public final static String LOCATION_BROADCAST = "net.nofool.dev.tbd";
    private String theSenderKey, theRecieverKey,info;
    @Override
    public void onMessageReceived(String from, Bundle data){
        try {
            JSONObject jsonObject = new JSONObject(data.getString("message"));
            theSenderKey = jsonObject.getString("senderKey");
            theRecieverKey = jsonObject.getString("recKey");
            info = jsonObject.getString("info");
        }catch (Exception e){
            Log.v(TAG,e.getMessage());
        }
        Log.v(TAG,"From: "+from);
        Log.v(TAG,"Message: "+info);

        if (info.equalsIgnoreCase("loc")){
            SharedPreferences s = getSharedPreferences(PREFS, 0);
            String myKey = s.getString("thisGCM", null);
            String lon = s.getString("thisLon",null);
            String lat = s.getString("thisLat",null);
            String result = lon+"_"+lat;
            String url = "http://dev.nofool.net/app/gcm.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("mess", result)
                    .add("targetKey",theSenderKey)
                    .add("senderKey", myKey)
                    .build();
            Request request = new Request.Builder().url(url).get().post(body).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {}
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try{
                        final String jsonData = response.body().string();
                        Log.v(TAG, "getLoc"+jsonData);
                        if (response.isSuccessful()){
                            JSONObject jsonObject = new JSONObject(jsonData);
                            String o = jsonObject.getString("message");
                            if (o.equalsIgnoreCase("TRUE")){}
                        }

                    } catch (Exception e){
                    }
                }
            });
        } else if(info.startsWith("m",0)){
            Log.v(TAG, "Message Recieved: "+info);
            String sub = info.substring(1);
            Log.v(TAG, "SubString: "+sub);
            Intent y = new Intent();
            y.setAction("net.nofool.dev.tbd.NOTIFY_USER");
            y.putExtra("msg",sub);
            sendBroadcast(y);


        } else {
            Intent intent = new Intent(LOCATION_BROADCAST);
            int x = info.indexOf('_');
            String longitude = info.substring(0, x - 1);
            String latitude = info.substring(x+1,info.length()-1);
            intent.putExtra("lon",longitude);
            intent.putExtra("lat",latitude);
            sendBroadcast(intent);
        }
    }
}
