package net.nofool.dev.tbd;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginB = (Button)findViewById(R.id.loginButton);
        emailET = (EditText)findViewById(R.id.emailEditText);
        passwordET = (EditText)findViewById(R.id.passwordEditText);

        loginB.setOnClickListener(loginClick);


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

}
