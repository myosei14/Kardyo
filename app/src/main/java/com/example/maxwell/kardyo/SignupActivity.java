package com.example.maxwell.kardyo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SignupActivity extends AppCompatActivity {

    private TextView TV_signupToHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        TV_signupToHome = (TextView)findViewById(R.id.TV_signupToHome);

        TV_signupToHome.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View V)
                    {
                        Intent intent = new Intent(getApplicationContext(),HomePage.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
