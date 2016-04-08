package net.nofool.dev.tbd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    private Button loginB;
    private EditText emailET;
    private EditText passwordET;
    private Button newB;

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

        }
    };
    private View.OnClickListener newClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(),Registration.class);
            startActivity(i);
        }
    };

}
