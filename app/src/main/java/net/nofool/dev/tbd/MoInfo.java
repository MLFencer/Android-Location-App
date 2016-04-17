package net.nofool.dev.tbd;

import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MoInfo extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mmap;
    private TextView dName, app1, app2, app3;
    private Switch track;
    private ImageButton refresh;
    private double x,y;
    private String n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mo_info);

        dName = (TextView)findViewById(R.id.deviceNameTV);
        app1 = (TextView)findViewById(R.id.app1TV);
        app2 = (TextView)findViewById(R.id.app2TV);
        app3 = (TextView)findViewById(R.id.app3TV);
        track = (Switch)findViewById(R.id.switch1);
        refresh = (ImageButton)findViewById(R.id.imageButton);

       /* initMap();

        MarkerOptions mark = new MarkerOptions().position(new LatLng(x,y)).title(n);
        mmap.addMarker(mark);
        mmap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(x,y)));*/



     SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        mmap = googleMap;
        x=34.526147;
        y=-83.984395;
        n="name";
        LatLng location = new LatLng(x,y);
        mmap.addMarker(new MarkerOptions().position(location).title(n));
        mmap.moveCamera(CameraUpdateFactory.newLatLng(location));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            mmap.setMyLocationEnabled(true);
        } else {
            Toast.makeText(MoInfo.this, "To see yourself on the map allow this app access to location data!", Toast.LENGTH_SHORT).show();
        }


    }
    /*
    private void initMap(){
        if (mmap == null){
            mmap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
            if (mmap==null){

            }
        }
    }
@Override
    protected void onResume(){
    super.onResume();
    initMap();
}*/
}
