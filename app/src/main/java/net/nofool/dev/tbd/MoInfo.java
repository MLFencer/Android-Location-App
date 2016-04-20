package net.nofool.dev.tbd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MoInfo extends FragmentActivity implements OnMapReadyCallback{

    InterstitialAd mInterstitialAd;

    private Button submit;
    private EditText msgET;
    private GoogleMap mmap;
    private TextView dName;
    private ImageButton refresh;
    private double x,y;
    private String n, goog, msg;
    private double newLat;
    private double newLon;
    private String TAG = MoInfo.class.getSimpleName();
    final String PREFS="dev.nofool.net.tbd";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mo_info);
        findViewById(R.id.moinfoLayout).requestFocus();
        dName = (TextView)findViewById(R.id.deviceNameTV);
        refresh = (ImageButton)findViewById(R.id.imageButton);
        msgET = (EditText)findViewById(R.id.messageET);
        submit = (Button)findViewById(R.id.submitButton);
        registerReceiver(infoReciever, new IntentFilter(MyGcmListenerService.LOCATION_BROADCAST));

        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId(getString(R.string.between_login_devicelist));
        requestNewInterstitial();
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params){
                while (mInterstitialAd.isLoading()){
                    try {
                        wait(1);
                    } catch (Exception e){}
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mInterstitialAd.isLoaded()){
                                mInterstitialAd.show();
                            }
                        }
                    });
                }
                return null;
            }

        }.execute();

        goog = getIntent().getStringExtra("google");
        n = getIntent().getStringExtra("name");
        getLocation(goog);
        x =newLat;
        y =newLon;
        Log.v(TAG, "Longitude: "+y);
        Log.v(TAG, "Latitude: "+x);

        dName.setText(n);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        submit.setOnClickListener(submitClick);
        refresh.setOnClickListener(refreshClick);

    }

    private void requestNewInterstitial(){
        Bundle extras = new Bundle();
        extras.putBoolean("is_designed_for_families", true);
        AdRequest adRequest = new AdRequest.Builder()
                //.addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .addTestDevice("D7BD0063D715185D4D51E12685DA4610")
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    private View.OnClickListener submitClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            msg = msgET.getText().toString();
            msg = "m"+msg;
            Log.v(TAG, "Message Sent");
            String url = "http://dev.nofool.net/app/gcm.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("mess", msg)
                    .add("targetKey",goog)
                    .add("senderKey", n)
                    .build();
            Request request = new Request.Builder().url(url).get().post(body).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        final String jsonData = response.body().string();
                        Log.v(TAG, "getLoc" + jsonData);
                        if (response.isSuccessful()) {
                            JSONObject jsonObject = new JSONObject(jsonData);
                            String o = jsonObject.getString("message");
                            if (o.equalsIgnoreCase("TRUE")) {
                            }
                        }

                    } catch (Exception e) {
                    }
                }
            });
        }
    };

    private View.OnClickListener refreshClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getLocation(goog);
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap){
        mmap = googleMap;
        //x=34.526147;
        //y=-83.984395;
        LatLng location = new LatLng(x,y);
        mmap.addMarker(new MarkerOptions().position(location).title(n));
        mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
    }

    public void getLocation(String id){
        Log.v(TAG, "getLocation called");
        SharedPreferences s = getSharedPreferences(PREFS, 0);
        String perp=s.getString("thisGCM",null);
        String url = "http://dev.nofool.net/app/gcm.php";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("mess", "loc")
                .add("targetKey",id)
                .add("senderKey", perp)
                .build();
        Request request = new Request.Builder().url(url).get().post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String jsonData = response.body().string();
                    Log.v(TAG, "getLoc" + jsonData);
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(jsonData);
                        String o = jsonObject.getString("message");
                        if (o.equalsIgnoreCase("TRUE")) {
                        }
                    }

                } catch (Exception e) {
                }
            }
        });
    }
    private BroadcastReceiver infoReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateMap(intent);

        }
    };

    private void updateMap(Intent i){
        newLat = Double.parseDouble(i.getStringExtra("lat"));
        newLon = Double.parseDouble(i.getStringExtra("lon"));
        mmap.clear();
        LatLng ll=new LatLng(newLat,newLon);
        mmap.addMarker(new MarkerOptions().position(ll).title(n));
        mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll,15));
    }


}
