package net.nofool.dev.tbd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChoiceActivity extends AppCompatActivity {

    private Button loginButton;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        loginButton = (Button)findViewById(R.id.loginChoiceButton);
        signupButton = (Button)findViewById(R.id.signupChoiceButton);

        loginButton.setOnClickListener(login);
        signupButton.setOnClickListener(regis);

    }


    private View.OnClickListener login = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Intent i = new Intent(getApplicationContext(), Login.class);
            startActivity(i);
        }
    };

   private View.OnClickListener regis = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Intent i = new Intent(getApplicationContext(), Registration.class);
            startActivity(i);
        }
    };
}
