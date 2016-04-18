package net.nofool.dev.tbd;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class Registration extends AppCompatActivity {

    private Button signupButton;
    private EditText emailField;
    private EditText passwordField;
    private EditText passwordCheckField;

    private String TAG = Registration.class.getSimpleName();
    private JSONObject jo;

    private String pass;
    private String passCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        signupButton = (Button)findViewById(R.id.registerButton);
        emailField = (EditText)findViewById(R.id.emailEditText);
        passwordField = (EditText)findViewById(R.id.passwordEditText);
        passwordCheckField = (EditText)findViewById(R.id.passwordCheckEditText);

        pass = passwordField.getText().toString();
        passCheck = passwordCheckField.getText().toString();

        signupButton.setClickable(false);

        passwordCheckField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (passCheck(pass, passCheck)){
                    passwordCheckField.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGood));
                    signupButton.setClickable(true);
                }else {
                    passwordCheckField.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWrong));
                    signupButton.setClickable(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}


        });
        signupButton.setOnClickListener(signupClick);
    }

    private View.OnClickListener signupClick = new View.OnClickListener(){
        private String e;
        private String p;
        @Override
        public void onClick(View v) {
            e = emailField.getText().toString();
            p = passwordField.getText().toString();
            Register(e, p);
            emailField.setText("");
            passwordCheckField.setText("");
            passwordField.setText("");
        }
    };
    private boolean passCheck(String p1, String p2){
        if (p1.equals(p2)){
            return true;
        }else{
            return false;
        }
    }
    private void Register(String email, String password) {
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
                            alertUser("duplicate");
                        }
                        else {
                            Log.v(TAG,"Register1.onResoponse,else: "+x+" "+e+" "+ p);
                            Register2(e, p);
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
    private void Register2(String e, String p){
        final String urlPost = "http://dev.nofool.net/app/registerNewUser.php";
        final RequestBody bodyPost = new FormBody.Builder().add("email", e).add("password", p).build();
        final OkHttpClient client2 = new OkHttpClient();
        Request request2 = new Request.Builder().url(urlPost).post(bodyPost).build();
        Call call2 = client2.newCall(request2);
        call2.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {Log.v(TAG,"Register2.onFailure");}
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String jsonData2 = response.body().string();
                    Log.v(TAG, jsonData2);
                    if (response.isSuccessful()){
                        JSONObject jsonO = new JSONObject(jsonData2);
                        String success = jsonO.getString("message");
                        if (success.equalsIgnoreCase("success")) {
                            Intent i = new Intent(getApplicationContext(), Login.class);
                            i.putExtra("first",true);
                            startActivity(i);
                        }
                    }
                }catch (IOException e){
                    Log.e(TAG,"Exception",e);
                }catch (JSONException e){
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