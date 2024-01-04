package com.example.pregnancyguideapp;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    Button b_login;
    TextView tv_signUp;
    EditText et_name, et_phoneN;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pregnancy-guide-app-841c4-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // The following code is done to support Android devices with back gestures animations.
        OnBackPressedDispatcher onBackPressedDispatcher = this.getOnBackPressedDispatcher();
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("Are you sure you want to exit?");
                builder.setCancelable(true);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Close the application.
                        finishAffinity();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        };
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback);
        
        // Login system
        b_login = findViewById(R.id.btnLogin);
        et_name = findViewById(R.id.editText_name);
        et_phoneN = findViewById(R.id.editText_phone_num);

        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_user = et_name.getText().toString();
                String phoneN_user = et_phoneN.getText().toString();

                // Error handling for login
                if (name_user.isEmpty() && phoneN_user.isEmpty() )
                {
                    Toast.makeText(LoginActivity.this,"Please enter your name and phone number!", Toast.LENGTH_SHORT).show();
                }else if ( name_user.isEmpty() )
                {
                    Toast.makeText(LoginActivity.this, "Name cannot be empty!", Toast.LENGTH_SHORT).show();
                }else if (phoneN_user.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Phone number cannot be empty!", Toast.LENGTH_SHORT).show();
                }else {
                    databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(phoneN_user)) {
                                final String getName = snapshot.child(phoneN_user).child("AccountDetails").child("Mother_name").getValue(String.class);
                                if (getName.equals(name_user)) {
                                    // To keep the user logged in even after closing the app. The user will have to manually logout in order switch to a different user.
                                    // This will also let user to stay logged in and be able to use some of the offline functionalities of the app.
                                    SharedPreferences sp = getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("User_PhoneN", phoneN_user);
                                    editor.apply();

                                    // Open up the home activity with a welcome message after the login process is successful.
                                    Toast.makeText(LoginActivity.this, "Welcome, " + name_user, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    finish();

                                } else {
                                    Toast.makeText(LoginActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Phone number does not exist!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        // Open up a register activity if the user wishes to make a new account.
        tv_signUp = findViewById(R.id.textView_SignUp);
        tv_signUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

}


