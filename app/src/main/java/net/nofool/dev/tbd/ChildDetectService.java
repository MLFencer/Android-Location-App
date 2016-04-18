package net.nofool.dev.tbd;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

public class ChildDetectService extends Service {
    Intent intent;
    LocationManager locationManager;
    MyLocationListener listener;
    private String TAG = ChildDetectService.class.getSimpleName();
    private static final String BROADCAST_ACTION = "string";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {return null;}

    @Override
    public void onCreate(){
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG,"Service Started");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        if (Build.VERSION.SDK_INT>=23&& ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){}
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,4000,0,listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,4000,0,listener);

        return START_STICKY;
    }


    public class MyLocationListener implements LocationListener {
        Location previousBestLoc = null;
        Intent intent= new Intent(BROADCAST_ACTION);
        private static final int TWO_MINUTES = 1000 * 60 * 2;

        public void onLocationChanged(final Location loc){
            if (isBetterLocation(loc,previousBestLoc)){
                loc.getLatitude();
                loc.getLongitude();
                intent.putExtra("Latitude",loc.getLatitude());
                intent.putExtra("Longitude",loc.getLongitude());
                intent.putExtra("Provider",loc.getProvider());
                sendBroadcast(intent);
            }
        }

        public void onProviderDisabled(String provider){
            Toast.makeText(getApplicationContext(),"Gps Disabled",Toast.LENGTH_SHORT).show();
        }

        public void onProviderEnabled(String provider){
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }

        public void onStatusChanged(String provider, int status, Bundle extras){}

        private boolean isSameProvider(String prov1, String prov2){
            if (prov1==null){
                return prov2==null;
            }
            return prov1.equals(prov2);
        }

        protected boolean isBetterLocation(Location location, Location current){
            if(current==null){
                return true;
            }
            long timeC = location.getTime() - current.getTime();
            boolean isVN = timeC>TWO_MINUTES;
            boolean isVO = timeC<-TWO_MINUTES;
            boolean isN = timeC>0;

            if (isVN){
                return true;
            } else if (isVO){
                return false;
            }
            int accC=(int)(location.getAccuracy()-current.getAccuracy());
            boolean isLA = accC>0;
            boolean isMA = accC<0;
            boolean isVLA = accC>200;

            boolean isSP = isSameProvider(location.getProvider(),current.getProvider());

            if (isMA){
                return true;
            } else if(isN && !isLA){
                return true;
            } else if (isN && !isVLA && isSP){
                return true;
            }
            return false;
        }

    }
}
