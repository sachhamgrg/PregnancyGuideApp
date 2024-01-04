package com.example.pregnancyguideapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.pregnancyguideapp.utility.DatePickerHelper;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText et_M_name, et_M_phoneN, et_M_email, et_B_edd;
    RadioGroup rg_gender;
    RadioButton selectedRadioButton;
    Button b_submit;
    String calculatedEDD;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Mother's information
        et_M_name = findViewById(R.id.editText_mother_name);
        et_M_phoneN = findViewById(R.id.editText_mother_phone_num);
        et_M_email = findViewById(R.id.editText_mother_email);

        // Baby's information
        et_B_edd = findViewById(R.id.editText_baby_EDD);
        rg_gender = findViewById(R.id.radioGroup_baby_gender);
        // Call DatePickerHelper to display a calendar view to choose a date.
        DatePickerHelper datePickerHelper = new DatePickerHelper(this, et_B_edd);
        et_B_edd.setOnClickListener(datePickerHelper);
        // Baby's EDD calculator
        findViewById(R.id.textView_click_ask_EDD).setOnClickListener(v -> {
            calculateEDD();
                });

        // Registration system
        b_submit = findViewById(R.id.btnSubmit);
        b_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String M_name = et_M_name.getText().toString().trim();
                String M_phoneN = et_M_phoneN.getText().toString();
                String M_email = et_M_email.getText().toString().trim();
                String B_edd = et_B_edd.getText().toString();

                // State value for RadioGroup if a button is selected.
                int stateRadioButton = rg_gender.getCheckedRadioButtonId();

                //Error handling for registration process
                if (TextUtils.isEmpty(M_name) || TextUtils.isEmpty(M_phoneN) || TextUtils.isEmpty(M_email) ||  TextUtils.isEmpty(B_edd) || stateRadioButton == -1) {

                    // Mother's details
                    if (M_name.length()== 0) {et_M_name.setError("Required");}
                    if (M_phoneN.length()== 0) {et_M_phoneN.setError("Required");}
                    if (M_email.length()== 0) {et_M_email.setError("Required");}

                    // Baby's details
                    if (B_edd.length()== 0){ et_B_edd.setError("Required");}
                    if (stateRadioButton == -1) {
                        TextInputLayout genderTextInputLayout = findViewById(R.id.B_gender_textInputLayout);
                        genderTextInputLayout.setError("Please select a gender.");
                        // Clear the error message when a RadioButton is selected
                        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                genderTextInputLayout.setError(null);
                            }
                        });
                    }

                    Toast.makeText(RegisterActivity.this,"Please fill in all the necessary details!", Toast.LENGTH_SHORT).show();

                }else{
                    //Ensure the mother's phone number is 10 digits.
                    if (M_phoneN.length() < 10 || M_phoneN.length() > 11 ) {
                        et_M_phoneN.setError("Not a valid phone number!");
                        et_M_phoneN.requestFocus();
                    }else {
                        //Ensure the mother's email is a valid email address.
                        if (!validateEmail(M_email)) {
                            et_M_email.setError("Not a valid e-mail address!");
                            et_M_email.requestFocus();
                        }else {
                            // Get the radioButton text value.
                            selectedRadioButton  = findViewById(rg_gender.getCheckedRadioButtonId());
                            String B_gender = selectedRadioButton.getText().toString();
                            // Call the method to register the user with the given details.
                            doRegistration(M_name, M_phoneN, M_email, B_edd, B_gender);
                        }
                    }
                }
            }
        });
    }

    public void doRegistration(String M_name,String M_phoneN,String M_email,String B_edd,String B_gender){

        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // To check if the phone number already exists in the database.
                if (snapshot.hasChild(M_phoneN)) {
                    Toast.makeText(RegisterActivity.this, "User already exist on this phone number!", Toast.LENGTH_SHORT).show();
                } else {

                    // All the user's details will be stored inside 'AccountDetails' under the user's phone number.
                    DatabaseReference ref = databaseReference.child("Users").child(M_phoneN).child("AccountDetails");
                    // Send data to firebase realtime database.
                    ref.child("Baby_gender").setValue(B_gender);
                    ref.child("Baby_EDD").setValue(B_edd);
                    ref.child("Mother_DOB").setValue("");
                    ref.child("Mother_address").setValue("");
                    ref.child("Mother_email").setValue(M_email);
                    ref.child("Mother_name").setValue(M_name);

                    // Give a toast message saying the registration was successful and go to Login Activity.
                    Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisterActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Checks if the input is a valid e-mail address.
    public boolean validateEmail(String email) {
        //Regular Expression
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
        //Compile regular expression to get the pattern
        Pattern pattern = Pattern.compile(regex);

        //Create instance of matcher
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Method to expand an image on a PhotoView.
    private void calculateEDD() {
        // Build an AlertDialog box to inflate the child layout.
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(RegisterActivity.this);
        // Inflate the child layout to be shown on that AlertDialog box.
        View mView = getLayoutInflater().inflate(R.layout.calc_edd_layout, null);
        // Set the view on the AlertDialog box.
        mBuilder.setView(mView);
        // Create and show the AlertDialog box.
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

        // Open up a DatePickerHelper upon click on the EditText
        EditText et_date_menstrual = mView.findViewById(R.id.editText_date_menstrual);
        // Call DatePickerHelper to display a calendar view to choose a date.
        DatePickerHelper datePickerHelper = new DatePickerHelper(this, et_date_menstrual);
        et_date_menstrual.setOnClickListener(datePickerHelper);

        // Calculate the EDD upon clicking the button - error handling.
        Button btnGetDate = mView.findViewById(R.id.btn_get_calcDate);
        btnGetDate.setOnClickListener(v -> {
            String menstrual_date = et_date_menstrual.getText().toString().trim();
            // If the date is selected then calculate the EDD.
            if (!TextUtils.isEmpty(menstrual_date)) {
                // Get the date format
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date lmpDate = null;
                try {
                    // Parse the string as a date.
                    lmpDate = dateFormat.parse(menstrual_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //---------------------------Calculate the EDD----------------------------------
                Calendar cal = Calendar.getInstance();
                cal.setTime(lmpDate);
                // Add 280 days to the current date to calculate the EDD
                cal.add(Calendar.DATE, 280);
                // Get the new date (EDD)
                Date dueDate = cal.getTime();
                // Get the date in a string format.
                calculatedEDD = dateFormat.format(dueDate);
                // Set the EDD to the EditText of the parent layout.
                et_B_edd.setText(calculatedEDD);
                // Dismiss the dialog.
                mDialog.dismiss();

            // If no date is selected, show an error using AlertDialog.
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setMessage("No date was selected!");
                builder.setTitle("Pregnancy Due Date Calculator");
                builder.setNegativeButton("OK", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        });
    }

}