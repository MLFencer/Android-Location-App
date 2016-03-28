package net.nofool.dev.tbd;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Registration extends AppCompatActivity {

    private Button signupButton;
    private EditText emailField;
    private EditText passwordField;
    private EditText passwordCheckField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        signupButton = (Button)findViewById(R.id.registerButton);
        emailField = (EditText)findViewById(R.id.emailEditText);
        passwordField = (EditText)findViewById(R.id.passwordEditText);
        passwordCheckField = (EditText)findViewById(R.id.passwordCheckEditText);

        passwordCheckField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (passCheck()){
                    passwordCheckField.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGood));
                }else {
                    passwordCheckField.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWrong));
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
            if (passCheck()) {
                e = emailField.toString();
                p = passwordField.toString();
                //Send to database
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            } else {
                //dialogue fragment saying that the passwords don't match
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
}
