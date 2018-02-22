//package com.example.maxwell.kardyo;
//
//import android.support.annotation.NonNull;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import org.w3c.dom.Text;
//
//public class PatientActivity extends AppCompatActivity {
//
//    private static final String TAG = "viewDatabase";
//    private FirebaseDatabase firebaseDatabase;
//    private FirebaseAuth firebaseAuth;
//    private FirebaseAuth.AuthStateListener authStateListener;
//    private DatabaseReference databaseReference, tableRef,keyRef;
//    private String userId, userName,userContact, userEmail;
//    private TextView userNameTv, userContactTv;
//    private Toolbar profileTb;
//    private DrawerLayout drawerLayout;
//    private ActionBarDrawerToggle actionBarDrawerToggle;
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_patient);
//
//        profileTb = (Toolbar)findViewById(R.id.profile_toolbar);
//        setSupportActionBar(profileTb);
//        getSupportActionBar().setTitle("Kardyo");
//
//        drawerLayout = (DrawerLayout)findViewById(R.id.profileDrawer);
//        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();
//        getSupportActionBar().setDisplayShowTitleEnabled(true);
//
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference();
//
//        userNameTv = (TextView)findViewById(R.id.userNameTv);
//        userContactTv = (TextView)findViewById(R.id.userContactTv);
//
//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    userId = user.getUid();
//                    userEmail = user.getEmail();
//                    Toast.makeText(getApplicationContext(), "Signed in with: " + user.getUid(), Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "User is signed out ", Toast.LENGTH_SHORT).show();
//                }
//            }
//        };
//
//        //tableRef = databaseReference.child("doctor");
//
//        userNameTv.setText(userEmail);
////
////        databaseReference.child("doctor").addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(DataSnapshot dataSnapshot) {
////
////                getUserData(dataSnapshot);
////            }
////
////            @Override
////            public void onCancelled(DatabaseError databaseError) {
////
////            }
////        });
//    }
//
//    public void getUserData(DataSnapshot dataSnapshot) {
//        for (DataSnapshot ds: dataSnapshot.getChildren()){
//
//            userNameTv.setText("max");
//            if (ds.equals(userId)){
////                patientDetails pd = new patientDetails();
////                pd.setName(ds.getValue(patientDetails.class).getName());
////                pd.setContact(ds.getValue(patientDetails.class).getContact());
////                userName = pd.getName();
////                userContact = pd.getContact();
//                //userName += userId;
//                userNameTv.setText("after max");
//                for (DataSnapshot dsChild: ds.getChildren()){
//
//                    userName = String.valueOf(dsChild.child("name").getValue());
//                    userContact = String.valueOf(dsChild.child("name").getValue());
//                }
//            }
//        }
//    }
//
//    @Override
//    public  void onStart(){
//        super.onStart();
//        firebaseAuth.addAuthStateListener(authStateListener);
//    }
//
//    public  void onStop(){
//        super.onStop();
//        firebaseAuth.removeAuthStateListener(authStateListener);
//    }
//
////    public boolean onCreateOptionsMenu(Menu menu){
////        MenuInflater inflater = getMenuInflater();
////        inflater.inflate(R.menu.profiledrawermenu, menu);
////
////        return true;
////    }
////
////    public boolean onOptionsItemSelected(MenuItem item){
////
////        if(item.getItemId() == R.id.editProfile){
////            Toast.makeText(getApplicationContext(),"Edit your profile", Toast.LENGTH_SHORT).show();
////        }
////
////        return true;
////    }
//
//    public void showData(){
//        userNameTv.setText(userName);
//        userContactTv.setText(userContact);
//    }
//}
