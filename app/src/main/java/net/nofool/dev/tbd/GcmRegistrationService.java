package net.nofool.dev.tbd;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class GcmRegistrationService extends IntentService{
    private static final String TAG = "RegService";
    final String PREFS="dev.nofool.net.tbd";
    public GcmRegistrationService(){
        super(TAG);
    }
    @Override
    public void onHandleIntent(Intent intent){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String id=intent.getStringExtra("id");
        String uID=intent.getStringExtra("uid");
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderID), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "GCM Registration Token: " + token);
            sendRegistrationToServer(token,uID,id);
            settings.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER,true).apply();
        }catch(Exception e) {
            Log.d(TAG, "Failed to complete token refresh",e);
            settings.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER,false).apply();
        }
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
    private void sendRegistrationToServer(String token,String uID,String id){
        SharedPreferences s = getSharedPreferences(PREFS,0);
        s.edit().putString("thisGCM",token).commit();
        String url = "http://dev.nofool.net/app/updateGCM.php";
        final OkHttpClient client = new OkHttpClient();
        final RequestBody requestBody = new FormBody.Builder()
                .add("uid",uID)
                .add("id",id)
                .add("google",token)
                .build();
        final Request request = new Request.Builder().url(url).get().post(requestBody).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String jsonData = response.body().string();
                    Log.v(TAG, jsonData);
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(jsonData);
                        String x = jsonObject.getString("message");
                        if (x.equalsIgnoreCase("Success")) {

                        } else {

                        }
                    }
                    }catch(Exception e){
                    Log.v(TAG, "Exception: " + e);
                }
            }
        });


    }
}
