package net.nofool.dev.tbd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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

    private String TAG = Registration.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                                Intent i = new Intent(getApplicationContext(),Devices.class);
                                i.putExtra("id", id);
                                startActivity(i);
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

}
