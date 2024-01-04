package com.example.pregnancyguideapp;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pregnancyguideapp.accountdetails.AccountDetailsFragment;
import com.example.pregnancyguideapp.breastfeeding.birthpreparedness.BirthPreparednessFragment;
import com.example.pregnancyguideapp.breastfeeding.BreastfeedingMainFragment;
import com.example.pregnancyguideapp.dashboard.DashboardFragmentMain;
import com.example.pregnancyguideapp.familyplanning.FamilyPlanningFragment;
import com.example.pregnancyguideapp.maleinvolvement.MaleInvolvementFragment;
import com.example.pregnancyguideapp.nutrition.NutritionFragment;
import com.example.pregnancyguideapp.postnatalcare.PostnatalCareFragment;
import com.example.pregnancyguideapp.pregnancy.PregnancyFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView btnHome;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pregnancy-guide-app-841c4-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Load the Dashboard Fragment at the launch of the activity inside the fragment_container.
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragmentMain(),"Dashboard_Frag_Main").commit();

        // Check for extra indicating which Fragment to open (Notification events)
        String fragmentToOpen = getIntent().getStringExtra("FRAGMENT_TO_OPEN");

        if (fragmentToOpen != null && fragmentToOpen.equals("BreastfeedingMainFragment")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new BreastfeedingMainFragment(),"BreastfeedingFrag").commit();
        }

        // The following code is done to support Android devices with back gestures animations.
        // OnBackPressed is deprecated on Android 13 and above, therefore, OnBackPressedDispatcher with OnBackPressedCallback is used.
        OnBackPressedDispatcher onBackPressedDispatcher = this.getOnBackPressedDispatcher();
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                int count = getSupportFragmentManager().getBackStackEntryCount();
                // For the navigation menu
                if ( drawerLayout.isDrawerOpen(GravityCompat.START) ) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                // Prompts the user to exit the app.
                else if (count == 0) {
                    promptExitApp();
                }
                else {
                    // Go back to the previous fragment.
                    getSupportFragmentManager().popBackStack();
                    // Show title of the fragment on the toolbar.
                    TextView title_toolbar = findViewById(R.id.toolbar_title);
                    title_toolbar.setText("Dashboard");
                    // Uncheck all the items from the navigation menu.
                    navigationView.setCheckedItem(R.id.menu_item_id_to_hide);
                }
            }
        };
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback);

        // Get the current user's phone number from Shared Preferences and store it in a variable phoneN. This variable will be used later to extract all the other user details from the database.
        SharedPreferences sp = getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        String phoneN = sp.getString("User_PhoneN", "");

        // Toolbar
        // The default action bar for this has application has been turned off. (Check themes.xml)
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Disable showing the title of the app on the toolbar.
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Drawer Layout
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}
            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                // Display the profile image and name of the user inside the header of the DrawerLayout.
                // The aim of this method is to ensure the contents of the user are refreshed.
                databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Since (header.xml) is the header layout of the parent's layout (activity_home), the following code is done to get a reference to the header layout.
                        TextView header_username = navigationView.getHeaderView(0).findViewById(R.id.textView_header_username);
                        CircleImageView profilePic = navigationView.getHeaderView(0).findViewById(R.id.profile_picture);

                        String getProfileImage = snapshot.child(phoneN).child("AccountDetails").child("profilePicture").getValue(String.class);
                        String getUserName = snapshot.child(phoneN).child("AccountDetails").child("Mother_name").getValue(String.class);

                        // Set mother's name
                        header_username.setText(getUserName);
                        // Set mother's profile picture if a profile photo exists inside the database.
                        if (getProfileImage != null) {
                            Glide.with(getApplicationContext()).load(getProfileImage).into(profilePic);
                        }


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(HomeActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onDrawerClosed(@NonNull View drawerView) {}
            @Override
            public void onDrawerStateChanged(int newState) {}
        });

        // Redirect the user to Dashboard Fragment on click to the home button.
        btnHome = findViewById(R.id.btn_home);
        btnHome.setOnClickListener(v -> {
            // Clear all the back stacks from the fragment manager and return to Dashboard fragment.
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            // Show title of the fragment on the toolbar.
            TextView title_toolbar = findViewById(R.id.toolbar_title);
            title_toolbar.setText("Dashboard");

            // Uncheck all the items from the navigation menu.
            navigationView.setCheckedItem(R.id.menu_item_id_to_hide);
        });

        // Navigation Menu
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        // Prompts user with an option to either enable/disable notification service for the app. (only for Android version >= 13 ) or (SDK >= 33)
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) != PermissionChecker.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(POST_NOTIFICATIONS);
            }
        }

        // For Android version >= 8.0 (Oreo), a notification channel needs to be created.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Instantiate the notification manager.
            NotificationManager manager = getSystemService(NotificationManager.class);
            //Make a notification channel
            NotificationChannel channel = new NotificationChannel("ch1", "Breastfeeding Log", NotificationManager.IMPORTANCE_HIGH);
            // Add the notification channel
            manager.createNotificationChannel(channel);
        }
    }

    // Requests user for notification permission (Pop up dialog box). (only for Android version >= 13 ) or (SDK >= 33)
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (isGranted) {
                            Toast.makeText(HomeActivity.this, "Permission granted for notification!!", Toast.LENGTH_SHORT).show();
                        } else {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HomeActivity.this);
                            builder.setMessage("Notification disabled! You can always turn this back on from the device's system settings.");
                            builder.setNegativeButton("OK", (DialogInterface.OnClickListener) (dialog, which) -> {
                                dialog.cancel();
                            });
                            android.app.AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Gets the name of the current fragment inside the container.
        Fragment myFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        String name = myFragment.getClass().getSimpleName();  // Gets the name of the fragment.

        // On each case, there is an if-statement to check if a fragment started from Dashboard fragment. If this is not true, then the backstack is all cleared.
        // Furthermore, another if-statement is implemented on each case to ensure if a fragment is on the container, then that same fragment cannot be replaced over.
        switch ( item.getItemId() ) {

            case R.id.nav_pregnancy:
                if ( name.equals("PregnancyFragment") ){
                    assert true; //do nothing
                } else if ( !name.equals("DashboardFragmentMain") ) {
                    getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                // Ensures the same fragment to not be added on backStack multiple times.
                if ( !navigationView.getMenu().findItem(R.id.nav_pregnancy).isChecked() ) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PregnancyFragment(), "Pregnancy_Frag").addToBackStack(null).commit();
                }
                break;

            case R.id.nav_nutrition:
                if ( name.equals("NutritionFragment") ||name.equals("NutritionAdviceFragment") || name.equals("ExerciseFragment") ){
                    assert true; //do nothing
                } else if ( !name.equals("DashboardFragmentMain") ) {
                    getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                // Ensures the same fragment to not be added on backStack multiple times.
                if ( !navigationView.getMenu().findItem(R.id.nav_nutrition).isChecked() ) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NutritionFragment(), "Nutrition_Frag").addToBackStack(null).commit();
                }
                break;

            case R.id.nav_birth_preparedness:
                if ( name.equals("BirthPlanFragment") || name.equals("BirthPreparednessFragment")){
                    assert true; //do nothing
                }
                else if ( !name.equals("DashboardFragmentMain") ) {
                    getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                // Ensures the same fragment to not be added on backStack multiple times.
                if ( !navigationView.getMenu().findItem(R.id.nav_birth_preparedness).isChecked() ) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BirthPreparednessFragment(), "BirthPreparedness_Frag").addToBackStack(null).commit();
                }
                break;

            case R.id.nav_postnatal_care:
                if ( name.equals("PostnatalCareFragment") ){
                    assert true; //do nothing
                } else if ( !name.equals("DashboardFragmentMain") ) {
                    getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                // Ensures the same fragment to not be added on backStack multiple times.
                if ( !navigationView.getMenu().findItem(R.id.nav_postnatal_care).isChecked() ) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PostnatalCareFragment(), "PostnatalCare_Frag").addToBackStack(null).commit();
                }
                break;

            case R.id.nav_breastfeeding:
                if ( name.equals("BreastfeedingMainFragment") || name.equals("BreastfeedingLogFragment") || name.equals("BreastfeedingVideoFragment") ){
                    assert true; //do nothing
                } else if ( !name.equals("DashboardFragmentMain") ) {
                    getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                // Ensures the same fragment to not be added on backStack multiple times.
                if ( !navigationView.getMenu().findItem(R.id.nav_breastfeeding).isChecked() ) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BreastfeedingMainFragment(), "BreastfeedingMain_Frag").addToBackStack(null).commit();
                }
                break;

            case R.id.nav_family_planning:
                if ( name.equals("FamilyPlanningFragment") || name.equals("MedicalOptionsFragment") || name.equals("NaturalOptionsFragment") || name.equals("MOBarrierMethodsFragment") || name.equals("MOHormonalFragment") || name.equals("MOSterilizationFragment") ){
                    assert true; //do nothing
                } else if ( !name.equals("DashboardFragmentMain") ) {
                    getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                // Ensures the same fragment to not be added on backStack multiple times.
                if ( !navigationView.getMenu().findItem(R.id.nav_family_planning).isChecked() ) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FamilyPlanningFragment(), "FamilyPlanning_Frag").addToBackStack(null).commit();
                }
                break;

            case R.id.nav_male_involvement:
                if ( name.equals("MaleInvolvementFragment") ){
                    assert true; //do nothing
                } else if ( !name.equals("DashboardFragmentMain") ) {
                    getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                // Ensures the same fragment to not be added on backStack multiple times.
                if ( !navigationView.getMenu().findItem(R.id.nav_male_involvement).isChecked() ) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MaleInvolvementFragment(), "MaleInvolvement_Frag").addToBackStack(null).commit();
                }
                break;

            case R.id.nav_account_details:
                // Ensures Account Details activities can be added to backStack.
                if (name.equals("AccountDetailsFragment") || name.equals("UpdateAccountDetailsFragment") ){
                    assert true; //do nothing
                } else if ( !name.equals("DashboardFragmentMain") ) {
                    getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                // Ensures the same fragment to not be added on backStack multiple times.
                if ( !navigationView.getMenu().findItem(R.id.nav_account_details).isChecked() ) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccountDetailsFragment(), "AccountDetails_Frag").addToBackStack(null).commit();
                }
                break;

            case R.id.nav_logout:
                final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("Are you sure you want to logout?");
                builder.setCancelable(true);

                builder.setPositiveButton("Yes", (dialog, which) -> {
                    // Clear the user's details from shared preferences cache.
                    SharedPreferences sp = getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.apply();

                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();
                });

                builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
        }
        // Close drawer upon selecting a menu item.
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // Method to prompt user for closing the application with a dialog box.
    private void promptExitApp() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setMessage("Are you sure you want to exit?");
        builder.setCancelable(true);

        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Close the application.
            finishAffinity();
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}