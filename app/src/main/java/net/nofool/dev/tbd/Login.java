package net.nofool.dev.tbd;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    private Button loginB;
    private EditText emailET;
    private EditText passwordET;
    private Button newB;

    private String TAG = Login.class.getSimpleName();
    final String PREFS="dev.nofool.net.tbd";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getIntent().getBooleanExtra("first", false)==true){
            alertUser("first");
        }
        SharedPreferences settings = getSharedPreferences(PREFS,0);
        if (settings.getBoolean("first_time",true)){
            settings.edit().putBoolean("first_time",false).commit();
            Random random = new Random();
            int num = random.nextInt(10000)+1;
            String name = android.os.Build.MODEL;
            String user = Build.USER;
            String unique = name+user+num;
            settings.edit().putString("UID",unique).commit();
        }
        loginB = (Button)findViewById(R.id.loginButton);
        emailET = (EditText)findViewById(R.id.emailEditText);
        passwordET = (EditText)findViewById(R.id.passwordEditText);
        newB = (Button)findViewById(R.id.registerButton);

        loginB.setOnClickListener(loginClick);
        newB.setOnClickListener(newClick);

    }
    private View.OnClickListener loginClick = new View.OnClickListener() {
        String email;
        String password;
        @Override
        public void onClick(View v) {
            email = emailET.getText().toString();
            password = passwordET.getText().toString();
            Login(email,password);
        }
    };
    private View.OnClickListener newClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(),Registration.class);
            startActivity(i);
        }
    };

    private void Login(String email, String password) {
        final String e = email;
        final String p = password;
        final String urlRequest = "http://dev.nofool.net/app/getPassword.php";
        final RequestBody bodyRequest = new FormBody.Builder()
                .add("email", email)
                .build();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(urlRequest).get().post(bodyRequest).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String jsonData = response.body().string();
                    Log.v(TAG, jsonData);
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(jsonData);
                        String x =jsonObject.getString("message");
                        if (x.equalsIgnoreCase("Success")){
                            String pw = jsonObject.getString("password");
                            String id = ""+jsonObject.getInt("id");
                            if (pw.equals(p)){
                                registerDevice(id);


                            }
                        }
                        else {
                            Log.v(TAG,"Register1.onResoponse,else: "+x+" "+e+" "+ p);
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Exception", e);
                } catch (JSONException e) {
                    Log.e(TAG, "JSON Exception", e);
                }
            }
        });
    }


    private void alertUser(String s){
        NotificationsDialogFragment alert = new NotificationsDialogFragment();
        Bundle args = new Bundle();
        args.putString("error_type",s);
        alert.setArguments(args);
        alert.show(getFragmentManager(), s);
    }

    private void registerDevice(String id2){
        final String id4 = id2;
        SharedPreferences settings = getSharedPreferences(PREFS,0);
        final String uID = settings.getString("UID",null);
        final OkHttpClient client = new OkHttpClient();
        final String urlRequest = "http://dev.nofool.net/app/selectDevice.php";
        final RequestBody bodyRequest = new FormBody.Builder()
                .add("uid",uID)
                .add("id",id4)
                .build();
        final Request request = new Request.Builder().url(urlRequest).get().post(bodyRequest).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String jsonData = response.body().string();
                    Log.v(TAG, "Register Device"+jsonData);
                    Log.v(TAG, "Request UID: "+uID);
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(jsonData);
                        String x = jsonObject.getString("message");
                        switch (x){
                            case "Success":
                                registerDevice2(id4);
                                break;
                            case "Already":
                                Intent s = new Intent(getApplicationContext(),ChildDetectService.class);
                                startService(s);
                                Intent i = new Intent(getApplicationContext(),Devices.class);
                                i.putExtra("id", id4);
                                startActivity(i);
                                break;
                            case "Other":
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder b=new AlertDialog.Builder(Login.this)
                                                .setTitle("Warning")
                                                .setMessage("This Device is ALREADY Connected to Another Account.\n Do You Want to Overwrite?")
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        changeDevice(id4);
                                                    }
                                                })
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        emailET.setText("");
                                                        passwordET.setText("");
                                                    }
                                                });
                                        AlertDialog d = b.create();
                                        d.show();

                                    }
                                });
                                break;
                        }
                    }

                } catch (IOException e){}
                catch (JSONException e){}
            }
        });
    }
    private void registerDevice2(String id){
        String name = Build.MODEL;
        final String id2 = id;
        SharedPreferences settings = getSharedPreferences(PREFS,0);
        final String uID = settings.getString("UID",null);
        final OkHttpClient client = new OkHttpClient();
        final String urlRequest = "http://dev.nofool.net/app/addDevice.php";
        final RequestBody bodyRequest = new FormBody.Builder()
                .add("pName", name)
                .add("uid",uID)
                .add("id",id)
                .build();
        final Request request = new Request.Builder().url(urlRequest).get().post(bodyRequest).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.v(TAG, "ONFAIL : "+e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String jsonData = response.body().string();
                    Log.v(TAG, "Register Device 2 :"+jsonData);
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(jsonData);
                        String x = jsonObject.getString("message");
                        if (x.equalsIgnoreCase("Success")) {
                            Intent r = new Intent(getApplicationContext(), GcmRegistrationService.class);
                            r.putExtra("id",id2);
                            r.putExtra("uid",uID);
                            startService(r);
                        } else {
                        Log.v(TAG, "Error: x: " + x + " JsonData: " + jsonData);
                    }
                }
                    Intent s = new Intent(getApplicationContext(),ChildDetectService.class);
                    startService(s);
                    Intent i = new Intent(getApplicationContext(),Devices.class);
                    i.putExtra("id", id2);
                    startActivity(i);

                }catch(Exception e){
                    Log.v(TAG, "Some Exception at register 2"+e);
                }}
        });
    }

    private void changeDevice(String id){
        String name = Build.MODEL;
        final String id2 = id;
        SharedPreferences settings = getSharedPreferences(PREFS,0);
        final String uID = settings.getString("UID",null);
        final OkHttpClient client = new OkHttpClient();
        final String urlRequest = "http://dev.nofool.net/app/changeDevice.php";
        final RequestBody bodyRequest = new FormBody.Builder()
                .add("pName", name)
                .add("uid",uID)
                .add("id",id)
                .build();
        final Request request = new Request.Builder().url(urlRequest).get().post(bodyRequest).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.v(TAG, "ONFAIL : "+e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String jsonData = response.body().string();
                    Log.v(TAG, "Register Device 2 :"+jsonData);
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(jsonData);
                        String x = jsonObject.getString("message");
                        if (x.equalsIgnoreCase("Success")) {
                            Intent r = new Intent(getApplicationContext(), GcmRegistrationService.class);
                            r.putExtra("id",id2);
                            r.putExtra("uid",uID);
                            startService(r);
                        } else {
                            Log.v(TAG, "Error: x: " + x + " JsonData: " + jsonData);
                        }
                    }
                    Intent s = new Intent(getApplicationContext(),ChildDetectService.class);
                    startService(s);
                    Intent i = new Intent(getApplicationContext(),Devices.class);
                    i.putExtra("id", id2);
                    startActivity(i);

                }catch(Exception e){
                    Log.v(TAG, "Some Exception at register 2"+e);
                }}
        });
    }
}
