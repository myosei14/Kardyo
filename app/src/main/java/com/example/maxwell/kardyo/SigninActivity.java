package com.example.maxwell.kardyo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SigninActivity extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private Button mSigninBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mEmail = (EditText)findViewById(R.id.usernameEt);
        mPassword = (EditText)findViewById(R.id.passwordEt);
        mSigninBtn = (Button)findViewById(R.id.siginBtn);

        final String Email = mEmail.getText().toString().trim();
        final String Password = mPassword.getText().toString().trim();

        mSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SignIn_User(Email, Password);
                if(Email=="youcan@gmail.com" && Password=="youcan"){
                    Intent main_activity_intent = new Intent(SigninActivity.this, MainActivity.class);
                    startActivity(main_activity_intent);
                }
            }
        });
    }

    /*private void SignIn_User(String email, String password) {

        if(email==getString(R.string.signinemail)&&password==getString(R.string.signinpassword)){
            Intent main_activity_intent = new Intent(SigninActivity.this, MainActivity.class);
            startActivity(main_activity_intent);
        }

    }*/

}
