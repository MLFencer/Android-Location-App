package net.nofool.dev.tbd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Devices extends AppCompatActivity {
    static int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        String x = getIntent().getStringExtra("id");
        int i = Integer.parseInt(x);
        id = i;
    }
    public static int getId(){
        return id;
    }
}
