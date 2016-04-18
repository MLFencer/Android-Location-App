package net.nofool.dev.tbd;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MoInfo extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mmap;
    private TextView dName, app1, app2, app3;
    private ImageButton refresh;
    private double x,y;
    private String n, goog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mo_info);

        dName = (TextView)findViewById(R.id.deviceNameTV);
        app1 = (TextView)findViewById(R.id.app1TV);
        app2 = (TextView)findViewById(R.id.app2TV);
        app3 = (TextView)findViewById(R.id.app3TV);
        refresh = (ImageButton)findViewById(R.id.imageButton);

        goog = getIntent().getStringExtra("google");
        n = getIntent().getStringExtra("name");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        mmap = googleMap;
        x=34.526147;
        y=-83.984395;
        LatLng location = new LatLng(x,y);
        mmap.addMarker(new MarkerOptions().position(location).title(n));
        mmap.moveCamera(CameraUpdateFactory.newLatLng(location));



    }
}
