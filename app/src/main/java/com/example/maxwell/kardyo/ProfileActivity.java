package com.example.maxwell.kardyo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "viewDatabase";
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference dbRef, searchRef;
    private String userId, userStatus;
    private MenuItem managePatient, manageDoctor, viewPatients, viewRecords, sendPin;
    private TextView userNameTv, usernameTv, userEmailTv, defaultSearchTitle;
    private Menu nav_menu;
    private NavigationView navigationView;
    private View header;
    private Button cancelChanges, saveChanges;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText searchTerm;
    private RecyclerView profileList;
    private ProgressDialog progressDialog;
     ArrayList<String> signedUpUsersList;
     ArrayList<String> signedUpUsersStatusList;
     SearchAdapter searchAdapter;
     Dialog searchDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbRef = firebaseDatabase.getReference();
        searchRef = firebaseDatabase.getReference();
        userNameTv = (TextView)findViewById(R.id.userNameTv);
        signedUpUsersList = new ArrayList<>();
        signedUpUsersStatusList = new ArrayList<>();



        //gets refrences to menu items
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        header = navigationView.getHeaderView(0);
        nav_menu = navigationView.getMenu();
        usernameTv = (TextView)header.findViewById(R.id.usernameTv);
        userEmailTv = (TextView)header.findViewById(R.id.userEmailTv);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.profileRefresh);
        progressDialog = new ProgressDialog(this);



        //authenticates the signed in user
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    userId = user.getUid();
                    progressDialog.setMessage("Setting up profile");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    Toast.makeText(getApplicationContext(), "Signed in with: " + user.getUid(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "User is signed out ", Toast.LENGTH_SHORT).show();
                }
            }
        };

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },5000);
            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //creates navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dbRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    getUserData(dataSnapshot);
                }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
    }


    //retrieves user's info from firebase
    public void getUserData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds: dataSnapshot.getChildren()){
            for(DataSnapshot dsChild : ds.getChildren()){
                if (dsChild.getKey().equals(userId)){
                    patientDetails pd = dsChild.getValue(patientDetails.class);
                    userNameTv.setText(pd.getName().toUpperCase());
                    userEmailTv.setText(pd.getEmail());
                    usernameTv.setText(pd.getName().toUpperCase());

                    userStatus = pd.getStatus();
                    showProfileContent();
//                    setupProfile();

                }
            }
        }
    }

//    private void setupProfile() {
//
//    }


    //callback function when the back is pressed
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            logout();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.profile, menu);
//        return true;
//    }

    //   @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.logout) {
            logout();
       } else if (id == R.id.editProfile) {
            showProfileDialog();
        }

       else if (id == R.id.viewPatients) {
            searchList();
       } //else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //shows dialog to edit your profile
    private void showProfileDialog() {
        final Dialog d = new Dialog(this);
        d.setTitle("Edit Profile");
        d.setContentView(R.layout.edit_profile_dialog);
        cancelChanges = (Button)d.findViewById(R.id.cancelChanges);
        cancelChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.hide();
            }
        });

        d.show();
    }

    private void showSearchDialog(){
        searchDialog = new Dialog(this);
        searchDialog.setTitle("Search");
        searchDialog.setContentView(R.layout.general_search_dialog);
        //cancelChanges = (Button)searchDialog.findViewById(R.id.cancelChanges);
//        cancelChanges.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                searchDialog.hide();
//            }
//        });
        searchDialog.show();

    }

    public void searchList(){
        final Dialog searchDialog = new Dialog(this);
        searchDialog.setTitle("Search");
        searchDialog.setContentView(R.layout.general_search_dialog);
        //cancelChanges = (Button)searchDialog.findViewById(R.id.cancelChanges);
//        cancelChanges.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                searchDialog.hide();
//            }
//        });
        searchDialog.show();
        //setting recycler properties
        searchTerm = (EditText)searchDialog.findViewById(R.id.searchTerm);
        profileList = (RecyclerView)searchDialog.findViewById(R.id.profileList);

        profileList.setHasFixedSize(true);
        profileList.setLayoutManager(new LinearLayoutManager(this));
        profileList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        searchTerm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()){
                    setMyAdapter(editable.toString());
                }else{

                    //clears the list for a new list
                    signedUpUsersList.clear();
                    signedUpUsersStatusList.clear();
                    profileList.removeAllViews();
                }
            }
        });

    } //end of searchList()

    private void setMyAdapter(final String searchItem) {

        searchRef.child("patient").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clears the list for a new list
//                signedUpUsersList.clear();
//                signedUpUsersStatusList.clear();
//                profileList.removeAllViews();
                int counter = 0;
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                        patientDetails pd = snapshot.getValue(patientDetails.class);
                        String full_name = pd.getName();
                        String status_name = pd.getStatus();

                        if (full_name.toLowerCase(). contains(searchItem.toLowerCase())){
                            signedUpUsersList.add(full_name);
                            signedUpUsersStatusList.add(status_name);
                            Toast.makeText(getApplicationContext(),searchItem,Toast.LENGTH_SHORT).show();
                            counter++;
                        }


                    //String full_name = snapshot.child("name").getValue-(String.class);
                   // String status_name = snapshot.child("status").getValue(String.class);

//                    if (counter == 15)
//                        break;
                }
                searchAdapter = new SearchAdapter(ProfileActivity.this,signedUpUsersList,signedUpUsersStatusList);
                profileList.setAdapter(searchAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    } //end of searchAdapter()


    //logs a user out
    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure, you want to logout?")
                .setTitle("Logout?")
                .setPositiveButton("Ok",  new DialogInterface.OnClickListener(){

                    @Override
                    public  void onClick(DialogInterface dialog, int which){
                        firebaseAuth.signOut();
                        Intent signin_intent = new Intent(getApplicationContext(),SigninActivity.class);
                        startActivity(signin_intent);
                        finish();
                    }
                })

                .setNegativeButton("Cancel", null);
        AlertDialog alert = builder.create();
        alert.show();
    }


    //displayes appropriate profile content per user's status
    public void showProfileContent(){

        if (userStatus.equals("Doctor")){
           nav_menu.findItem(R.id.viewRecords).setVisible(false);
           nav_menu.findItem(R.id.manageDoctor).setVisible(false);
        }else if(userStatus.equals("Patient")){
            nav_menu.findItem(R.id.managePatient).setVisible(false);
        }else if(userStatus.equals("Relative")){
            nav_menu.findItem(R.id.managePatient).setVisible(false);
            nav_menu.findItem(R.id.manageDoctor).setVisible(false);
            nav_menu.findItem(R.id.sendPin).setVisible(false);
        }
        progressDialog.dismiss();
    }

    @Override
    public  void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public  void onStop(){
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
