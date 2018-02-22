package com.example.maxwell.kardyo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //sends user to signup page
        final Button Bsignup = (Button) findViewById(R.id.Bsignup);
        mToolbar = (Toolbar)findViewById(R.id.homepage_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Kardyo");


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

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId() == R.id.aboutMenuItem){
            Toast.makeText(this, "Final year project", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
}
