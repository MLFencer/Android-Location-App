package net.nofool.dev.tbd;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LaunchBoard extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_board);

        if (isNetworkAvailible()){
            Intent i = new Intent(LaunchBoard.this,Login.class);
            startActivity(i);
        }else{
            networkStateResponce();
        }


    }

    private boolean isNetworkAvailible(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo active = connectivityManager.getActiveNetworkInfo();
        return active != null && active.isConnected();
    }

    private void networkStateResponce(){
        AlertDialog.Builder b=new AlertDialog.Builder(LaunchBoard.this)
                .setTitle("Warning")
                .setMessage("This Application Requires an Internet Connection!")
                .setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isNetworkAvailible()){
                            Intent i = new Intent(LaunchBoard.this,Login.class);
                            startActivity(i);
                        }else{
                            networkStateResponce();
                        }
                    }
                });
        AlertDialog d = b.create();
        d.show();
    };
}
