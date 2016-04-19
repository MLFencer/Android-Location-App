package net.nofool.dev.tbd;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

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
    @Override
    public void onMessageReceived(String from, Bundle data){
        String message = data.getString("message");
        Log.d(TAG,"From: "+from);
        Log.d(TAG,"Message: "+message);

        if (message.equalsIgnoreCase("loc")){
            String returnResultKey = data.getString("senderKey");
            SharedPreferences s = getSharedPreferences(PREFS, 0);
            String myKey = s.getString("thisGCM", null);
            String lon = s.getString("thisLon",null);
            String lat = s.getString("thisLat",null);
            String result = lon+"_"+lat;
            String url = "http://dev.nofool.net/app/gcm.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("mess", result)
                    .add("targetKey",returnResultKey)
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
        } else if(message.startsWith("m",0)){
            //do something with message
        } else {

            DeviceListFrag r = new DeviceListFrag();
            r.changeItem(data.getString("senderKey"), message);
        }
    }
    private boolean thisDevice(String r){
        SharedPreferences settings = getSharedPreferences(PREFS, 0);
        String y =settings.getString("UID",null);
        if(y.equalsIgnoreCase(r)){
            return true;
        }
        return false;
    }
}
