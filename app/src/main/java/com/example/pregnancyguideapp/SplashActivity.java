package com.example.pregnancyguideapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(3000); // Display the splash screen for 3 seconds.
                }catch(Exception ex) {
                    ex.printStackTrace();
                }finally {
                    // Get the user's phone number from the cache if it has been previously stored.
                    SharedPreferences sp = getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
                    String phoneN = sp.getString("User_PhoneN", "");

                    // Checks if user's phone number is stored in the cache. If the user has not been logged out yet, direct it to the Home Activity, otherwise, open up the Login Activity.
                    if (phoneN == "") {
                        // Direct the user to login activity if no user is kept logged in.
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    } else {
                        //Direct the user to home activity if an user is still logged in.
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));

                        // To retrieve all the details of the user from the database and store it in the disk cache at the start of the app.
                        // This gives enough time for the app to get connected to the database early enough to do read/write operations smoothly.
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pregnancy-guide-app-841c4-default-rtdb.firebaseio.com/");
                        //databaseReference.child("Users").child(phoneN).child("Mother_name").keepSynced(true); - child("..") has to be a ref value (string)- not an exact data - keepSynced() only good for non user related data.
                        databaseReference.child("Users").child(phoneN).keepSynced(true);
                        //Direct the user to home activity if an user is still logged in.
                    }

                    // Ensures the splash screen is not accessible if a 'back' button is pressed by an user.
                    finish();
                }
            }
        };thread.start();
    }
}