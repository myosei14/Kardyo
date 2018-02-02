package com.example.maxwell.kardyo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import  android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity implements OnClickListener {

    private Spinner statusSp, genderSp;
    private Button signupBtn;
    private TextView linkToSignin, errTv;
    private EditText signupNameEt, signupEmailEt, signupContactEt, signupPassEt, signupConPass;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference user_name, user_email, user_password, user_status, user_contact,user_gender;
    private CheckBox agreementCb;
    private  String errMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //data posted to database
        user_name = firebaseDatabase.getReference("username");
        user_email = firebaseDatabase.getReference("email");
        user_contact = firebaseDatabase.getReference("contact");
        user_gender = firebaseDatabase.getReference("gender");
        user_status = firebaseDatabase.getReference("status");
        user_password = firebaseDatabase.getReference("password");

        signupNameEt = (EditText)findViewById(R.id.signupNameEt);
        signupEmailEt = (EditText)findViewById(R.id.signupEmailEt);
        signupContactEt = (EditText)findViewById(R.id.signupContactEt);
        signupPassEt = (EditText)findViewById(R.id.signupPassEt);
        signupConPass = (EditText)findViewById(R.id.signupConPass);
        errTv = (TextView)findViewById(R.id.errTv);
        agreementCb = (CheckBox)findViewById(R.id.agreementCb);
        signupBtn = (Button)findViewById(R.id.signupBtn);
        statusSp = (Spinner)findViewById(R.id.statusSp);
        genderSp = (Spinner)findViewById(R.id.genderSp);
        linkToSignin = (TextView)findViewById(R.id.linkToSignin);
        progressDialog = new ProgressDialog(this);

        linkToSignin.setOnClickListener(this);
        signupBtn.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.signupBtn){
            registerUser();
        }
        else if(view.getId() == R.id.linkToSignin){
            Intent signin_activity = new Intent(getApplicationContext(), SigninActivity.class);
            startActivity(signin_activity);
        }


    }//end of onclick method

    public void registerUser(){

        String userName = signupNameEt.getText().toString().trim();
        String userEmail = signupEmailEt.getText().toString().trim();
        String userPassword = signupPassEt.getText().toString().trim();
        String userContact = signupContactEt.getText().toString().trim();
        String userStatus = statusSp.getSelectedItem().toString().trim();
        String userGender = genderSp.getSelectedItem().toString().trim();
        String conPass = signupConPass.getText().toString().trim();

        if (TextUtils.isEmpty(userName)|| TextUtils.isEmpty(userEmail)||TextUtils.isEmpty(userContact)|| TextUtils.isEmpty(userPassword)||TextUtils.isEmpty(conPass) || !agreementCb.isChecked() || userContact.length() != 10 || !(userPassword.equals(conPass))){

            if (TextUtils.isEmpty(userName)|| TextUtils.isEmpty(userEmail)||TextUtils.isEmpty(userContact)|| TextUtils.isEmpty(userPassword)||TextUtils.isEmpty(conPass)){
               errMessage = "* All fields must be filled ";
               errTv.setText(errMessage);
                return;
           }

            if (userContact.length() != 10){
                errMessage ="* Contact must be 10 ";
                errTv.setText(errMessage);
                return;
            }
            if (!(userPassword.equals(conPass))){
                errMessage = "* Passwords donot match ";
                errTv.setText(errMessage);
                return;
            }
            if (!agreementCb.isChecked()){
                errMessage = "* Please agree ";
                errTv.setText(errMessage);
                return;
            }
            return;
        }

        else {
            errMessage = "";
            errTv.setText(errMessage);
            progressDialog.setMessage("Registering user...");
            progressDialog.show();

            user_name.setValue(userName);
            user_email.setValue(userEmail);
            user_contact.setValue(userContact);
            user_gender.setValue(userGender);
            user_status.setValue(userStatus);
            user_password.setValue(userPassword);


            //authentication user for sigin
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                progressDialog.hide();
                                directToProfile();
                            } else {
                                Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                                progressDialog.hide();
                            }
                        }
                    });

        }
    }//end registerUser

    public void directToProfile(){
        Intent main_activity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(main_activity);
    }
}
