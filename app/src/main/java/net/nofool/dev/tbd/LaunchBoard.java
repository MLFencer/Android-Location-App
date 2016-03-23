package net.nofool.dev.tbd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//This Activity is the first activity that loads when the app is launched.
//It takes data from previous launches to determin how it launches this time.
//It is done in this way because one cannot change the launcher activity after the app is created.

public class LaunchBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_board);
    }
}
