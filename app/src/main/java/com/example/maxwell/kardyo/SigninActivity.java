package com.example.maxwell.kardyo;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {

        private EditText mEmail, mPassword;
        private Button mSigninBtn;
        private TextView linkToSignup, emailPass;
        private FirebaseAuth firebaseAuth;
        private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        mEmail = (EditText)findViewById(R.id.siginEmailEt);
        mPassword = (EditText)findViewById(R.id.siginPassEt);
        mSigninBtn = (Button)findViewById(R.id.siginBtn);
        linkToSignup = (TextView)findViewById(R.id.linkToSignup);
        emailPass = (TextView)findViewById(R.id.emailPass);



        mSigninBtn.setOnClickListener(this);
        linkToSignup.setOnClickListener(this);
    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    @Override
    public void onClick(View view) {
            if (view.getId() == R.id.linkToSignup){
                Intent signup_activity = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(signup_activity);
            }

            else if (view.getId() == R.id.siginBtn){
                userSignin();
            }
    }


    public void userSignin(){

                String myEmail = mEmail.getText().toString();
                String myPassword = mPassword.getText().toString();

                if (TextUtils.isEmpty(myEmail) || TextUtils.isEmpty(myPassword)){
                    emailPass.setText("* All fields must be filled");
                    return;
                }

                else {
                    emailPass.setText("");
                    progressDialog.setMessage("Sigining in ...");
                    progressDialog.show();
                    firebaseAuth.signInWithEmailAndPassword(myEmail, myPassword)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.hide();
                                        Toast.makeText(getApplicationContext(), "Sigin Successful", Toast.LENGTH_SHORT).show();
                                        directToProfile();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Credentials Invalid, Sigin failed !", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }//end of outer else


    }//end userSignin method

    public void directToProfile(){
        Intent main_activity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(main_activity);
    }

}
