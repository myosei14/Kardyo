package com.example.maxwell.kardyo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import  android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignupActivity extends AppCompatActivity implements OnClickListener {

    private Toolbar mToolbar;

    private Spinner statusSp, genderSp;
    private Button signupBtn, verifyBtn, resendBtn;
    private TextView linkToSignin, errTv, textView9,textView3,textView2,textView;
    private EditText signupNameEt, signupEmailEt, signupContactEt, signupPassEt, signupConPass, codeEt;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference  firebaseRef,childRef,nextchildRef;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private  PhoneAuthProvider.ForceResendingToken resendToken;
    private CheckBox agreementCb;
    private  String errMessage, userName,userEmail,userPassword,userContact,userGender,conPass,uid;
    private  static  String userStatus, phoneVerificationId, userPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        mToolbar = (Toolbar)findViewById(R.id.signup_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        codeEt = (EditText)findViewById(R.id.codeEt);
        verifyBtn = (Button)findViewById(R.id.verifyBtn);
        resendBtn = (Button)findViewById(R.id.resendBtn);

        codeEt.setVisibility(View.GONE);
        verifyBtn.setVisibility(View.GONE);
        resendBtn.setVisibility(View.GONE);

        verifyBtn.setOnClickListener(this);
        resendBtn.setOnClickListener(this);


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
        textView9 = (TextView)findViewById(R.id.textView9);
        textView3 = (TextView)findViewById(R.id.textView3);
        textView2 = (TextView)findViewById(R.id.textView3);
        textView = (TextView)findViewById(R.id.textView3);

        linkToSignin.setOnClickListener(this);
        signupBtn.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.signupBtn){
            registerUser();
        }
         if(view.getId() == R.id.linkToSignin){
            Intent signin_activity = new Intent(getApplicationContext(), SigninActivity.class);
            startActivity(signin_activity);
        }

        if (view.getId() == R.id.verifyBtn) {
            verifyCode(view);
        }
        if (view.getId() == R.id.resendBtn){
            resendCode(view);
        }


    }//end of onclick method

    public void registerUser(){

         userName = signupNameEt.getText().toString().trim();
         userEmail = signupEmailEt.getText().toString().trim();
         userPassword = signupPassEt.getText().toString().trim();
         userContact = signupContactEt.getText().toString().trim();
         userPhone = userContact;
         userStatus = statusSp.getSelectedItem().toString().trim();
         userGender = genderSp.getSelectedItem().toString().trim();
         conPass = signupConPass.getText().toString().trim();

        if (TextUtils.isEmpty(userName)|| TextUtils.isEmpty(userEmail)||TextUtils.isEmpty(userContact)|| TextUtils.isEmpty(userPassword)||TextUtils.isEmpty(conPass) || !agreementCb.isChecked() || userContact.length() != 10 ||userPassword.length() < 6 || !(userPassword.equals(conPass))){

            if (TextUtils.isEmpty(userName)|| TextUtils.isEmpty(userEmail)||TextUtils.isEmpty(userContact)|| TextUtils.isEmpty(userPassword)||TextUtils.isEmpty(conPass)){
               errMessage = "* All fields must be filled ";
               errTv.setText(errMessage);
                return;
           }


           if(!emailValidator(userEmail)){
               errMessage = "* A valid email is required ";
               errTv.setText(errMessage);
               return;
           }

            if (userContact.length() != 10){
                errMessage ="* Contact must be 10 ";
                errTv.setText(errMessage);
                return;
            }
            if (!(userPassword.equals(conPass))){
                errMessage = "* Passwords do not match ";
                errTv.setText(errMessage);
                return;
            }

            if ( userPassword.length() < 6){
                errMessage = "* Password must be at least 6 characters ";
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

            if (userStatus.equals("Doctor") ){
                firebaseRef = firebaseDatabase.getReference("doctor");
            }
            if (userStatus.equals("Patient")){
                firebaseRef = firebaseDatabase.getReference("patient");
            }
            if (userStatus.equals("Relative")){
                firebaseRef = firebaseDatabase.getReference("relative");
            }



            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                uid = user.getUid();
                                sendUserDetails();
                                progressDialog.hide();
                                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                verifyPhone();
                                showVerifyPage();
                            } else {
                                // If sign in fails, display a message to the user.
                                errMessage = "* A valid email is required ";
                                errTv.setText(errMessage);
                                Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                                progressDialog.hide();

                            }

                            // ...
                        }
                    });

        }
    }//end registerUser

    //sends verification code to user's contact
    public void verifyPhone(){
        setUpVerificationCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                userContact,
                60,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks
        );
    } //end of verify code

    public void showVerifyPage(){
        signupNameEt.setVisibility(View.GONE);
        signupEmailEt.setVisibility(View.GONE);
        signupContactEt.setVisibility(View.GONE);
        signupPassEt.setVisibility(View.GONE);
        signupConPass.setVisibility(View.GONE);
        errTv.setVisibility(View.GONE);
        agreementCb.setVisibility(View.GONE);
        signupBtn.setVisibility(View.GONE);
        statusSp.setVisibility(View.GONE);
        genderSp.setVisibility(View.GONE);
        linkToSignin.setVisibility(View.GONE);
        textView9.setVisibility(View.GONE);
        textView3.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        textView2.setVisibility(View.GONE);

        codeEt.setVisibility(View.VISIBLE);
        verifyBtn.setVisibility(View.VISIBLE);
        resendBtn.setVisibility(View.VISIBLE);
    }
    private void verifyCode(View view) {
        String code = codeEt.getText().toString().trim();
        if (TextUtils.isEmpty(code)){
            Toast.makeText(getApplicationContext(),"Please enter verification code", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Verifying user");
        progressDialog.show();
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(phoneVerificationId, code);
        signInWithPhoneAuthCredential(phoneAuthCredential);
    }

    public void resendCode(View view){
        progressDialog.setMessage("Resending code...");
        progressDialog.show();
        setUpVerificationCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                userContact,
                60,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks,
                resendToken
        );
        progressDialog.hide();
    } //end of resend code

    private  void  setUpVerificationCallbacks() {
       verificationCallbacks =
               new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                   @Override
                   public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                     signInWithPhoneAuthCredential(phoneAuthCredential);
                   }

                   @Override
                   public void onVerificationFailed(FirebaseException e) {
                        if (e instanceof FirebaseAuthInvalidCredentialsException){
                            Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        }
                        else if(e instanceof FirebaseTooManyRequestsException){
                            Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        }
                   }

                   @Override
                  public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token){
                       phoneVerificationId = verificationId;
                       resendToken = token;
                   }
               };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Contact verified successfully", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = task.getResult().getUser();
                            progressDialog.hide();
                            directToProfile();
                        }

                        else{
                            Toast.makeText(getApplicationContext(), "Contact verification failed", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
    };

    public void directToProfile(){
        Intent signin_activity = new Intent(getApplicationContext(), SigninActivity.class);
        startActivity(signin_activity);
    }

    public void sendUserDetails(){
            childRef = firebaseRef.child(uid);

        for (int i = 0; i < 6; i++){
            if (i == 0){
                nextchildRef = childRef.child("name");
                nextchildRef.setValue(userName);
            }
            if (i == 1){
                nextchildRef = childRef.child("email");
                nextchildRef.setValue(userEmail);
            }
            if (i == 2){
                nextchildRef = childRef.child("contact");
                nextchildRef.setValue(userContact);
            }
            if (i == 3){
                nextchildRef = childRef.child("gender");
                nextchildRef.setValue(userGender);
            }
            if (i == 4){
                nextchildRef = childRef.child("status");
                nextchildRef.setValue(userStatus);
            }
            if (i == 5){
                nextchildRef = childRef.child("password");
                nextchildRef.setValue(userPassword);
            }
        }
    } //end of sendUserDetails()


    //validates user's email
    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //makes user status accessible in other activities
    public static String getUserStatus(){
        return userStatus;
    }
    public static String getUserPhone(){return userPhone;}
    public static String getPhoneVerificationId(){return phoneVerificationId;}

}


