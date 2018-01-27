package com.example.maxwell.kardyo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //sends user to signup page
        final Button Bsignup = (Button) findViewById(R.id.Bsignup);

//        Bsignup.setOnClickListener(
//                new View.OnClickListener()
//                {
//                    public void onClick(View v){
//                        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
//                        startActivity(intent);
//                    }
//                }//OnClickListener
//        );

        Bsignup.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View v){
                        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                        startActivity(intent);
                    }
                }//OnClickListener
        );
        //sends user to login page
       final Button BlogIn = (Button)findViewById(R.id.BlogIn);
       BlogIn.setOnClickListener(
               new View.OnClickListener(){
                   @Override
                   public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                    startActivity(intent);
                    //startActivity(new Intent(Homepage.this, LoginActivity.class));
                   }
               }
       );
    }
}
