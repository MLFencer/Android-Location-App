package net.nofool.dev.tbd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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

public class Registration extends AppCompatActivity {

    private Button signupButton;
    private EditText emailField;
    private EditText passwordField;
    private EditText passwordCheckField;

    private ProgressDialog prog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        signupButton = (Button)findViewById(R.id.registerButton);
        emailField = (EditText)findViewById(R.id.emailEditText);
        passwordField = (EditText)findViewById(R.id.passwordEditText);
        passwordCheckField = (EditText)findViewById(R.id.passwordCheckEditText);

        signupButton.setClickable(false);

        passwordCheckField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (passCheck()){
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
            e = emailField.toString();
            p = passwordField.toString();
            if(register(e, p)){
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            } else {
                alertUser("duplicate");
            }

        }
    };
    private boolean passCheck(){
        if (passwordField.equals(passwordCheckField)){
            return true;
        }else{
            return false;
        }
    }
    private Boolean register(String email, String pass){


        return false;
    }

    private void alertUser(String s){
        NotificationsDialogFragment alert = new NotificationsDialogFragment();
        alert.show(getFragmentManager(), s);
    }


    class GetUser extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prog = new ProgressDialog(Registration.this);
            prog.setMessage("Creating User..");
            prog.setIndeterminate(false);
            prog.setCancelable(false);
            prog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}