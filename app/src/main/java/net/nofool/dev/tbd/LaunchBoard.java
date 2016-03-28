package net.nofool.dev.tbd;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//This Activity is the first activity that loads when the app is launched.
//It takes data from previous launches to determin how it launches this time.
//It is done in this way because one cannot change the launcher activity after the app is created.

public class LaunchBoard extends AppCompatActivity {

    private String email=null;
    private String pWord=null;
    private int save=0;
    private SQLiteDatabase parentLoginDB;
    private Cursor c = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_board);

        parentLoginDB = openOrCreateDatabase("ParentLogin", MODE_PRIVATE, null);

        if (!Check()){
            Intent i = new Intent(getApplicationContext(),ChoiceActivity.class);
            startActivity(i);
        } else {
            c=parentLoginDB.rawQuery("SELECT * from user", null);
            email=c.getString(1);
            pWord=c.getString(2);
            save=c.getInt(3);
        }


    }

    private Boolean Check(){
        Cursor c = null;
        boolean exist;
// get cursor on it
        try
        {
            c = parentLoginDB.query("email", null,null, null, null, null, null);
            exist = true;
        }
        catch (Exception e) {
    // fail
            exist=false;
        }

        if (exist = false){
            parentLoginDB.execSQL("CREATE TABLE login(email VARCHAR, password VARCHAR, remember INT);");
            return exist;
        } else {
            return exist;
        }
    }

}
