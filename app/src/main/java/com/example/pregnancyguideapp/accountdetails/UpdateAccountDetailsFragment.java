package com.example.pregnancyguideapp.accountdetails;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.pregnancyguideapp.utility.DatePickerHelper;
import com.example.pregnancyguideapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateAccountDetailsFragment extends Fragment{

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pregnancy-guide-app-841c4-default-rtdb.firebaseio.com/");
    FirebaseStorage storage = FirebaseStorage.getInstance();

    CircleImageView profilePicture;
    FloatingActionButton insertImage;
    private Uri uriImage;
    double uploadProgress;
    boolean pickImageState = false;
    String calculatedEDD;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_account_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView title_toolbar = getActivity().findViewById(R.id.toolbar_title);
        title_toolbar.setText(R.string.account_details);

        // Get the current user's phone number from Shared Preferences and store it in a variable phoneN. This variable will be used later to extract all the other user details from the database.
        SharedPreferences sp = getActivity().getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        String phoneN = sp.getString("User_PhoneN", "");

        // CircleImageView - Profile Picture
        profilePicture = getActivity().findViewById(R.id.circleImageView_profile_picture_UAD);

        // Insert Image using FloatingActionButton
        insertImage = getActivity().findViewById(R.id.floatingActionButton_insertImage);
        insertImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Open up a browser to select an image file.
                mGetContent.launch("image/*");
            }
        });

        // Get user's reference using the phone number.
        DatabaseReference userRef = databaseReference.child("Users").child(phoneN).child("AccountDetails");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String getM_name = snapshot.child("Mother_name").getValue(String.class);
                String getM_email = snapshot.child("Mother_email").getValue(String.class);
                String getM_address = snapshot.child("Mother_address").getValue(String.class);
                String getM_dob = snapshot.child("Mother_DOB").getValue(String.class);
                String getB_edd = snapshot.child("Baby_EDD").getValue(String.class);
                String getB_gender = snapshot.child("Baby_gender").getValue(String.class);
                String getProfileImage = snapshot.child("profilePicture").getValue(String.class);

                //                                 (Mother's information)
                // Set mother's profile picture if there is an image stored inside the database.
                if (getProfileImage != null) {
                    Glide.with(requireContext()).load(getProfileImage).into(profilePicture);
                }
                // Get phone number from the list and set it to the textview to display mother's phone number.
                TextView phone = getActivity().findViewById(R.id.textView_mother_phone_UAD);
                phone.setText(": " + phoneN);
                // Get name from the list and set it to the editText of mother's name.
                EditText name = getActivity().findViewById(R.id.editText_mother_name);
                name.setText(getM_name);
                // Get email from the list and set it to the editText of mother's email address.
                EditText email = getActivity().findViewById(R.id.editText_mother_email);
                email.setText(getM_email);
                // Get address from the list and set it to the editText of mother's home address.
                EditText address = getActivity().findViewById(R.id.editText_mother_address);
                address.setText(getM_address);
                // Get address from the list and set it to the editText of mother's date of birth.
                EditText dob = getActivity().findViewById(R.id.editText_mother_DOB);
                dob.setText(getM_dob);
                DatePickerHelper datePicker_dob = new DatePickerHelper(getActivity(), dob);       // Call DatePickerHelper to display a calendar view to choose a date.
                dob.setOnClickListener(datePicker_dob);

                //                                  (Baby's information)
                // Get EDD from the list and set it to the editText of baby's estimated delivery date.
                EditText baby_EDD = getActivity().findViewById(R.id.editText_baby_EDD);
                baby_EDD.setText(getB_edd);
                DatePickerHelper datePicker_B_edd = new DatePickerHelper(getActivity(), baby_EDD);       // Call DatePickerHelper to display a calendar view to choose a date.
                baby_EDD.setOnClickListener(datePicker_B_edd);
                // Baby's EDD calculator
                getActivity().findViewById(R.id.textView_click_ask_EDD).setOnClickListener(v -> {
                    calculateEDD();
                });

                // Get baby's gender and check the correct radio button of the baby's gender RadioGroup.
                int radioButtonID = 0;
                if (getB_gender.equals("Boy")) {
                    radioButtonID = R.id.RB_Male_UAD;
                } else if (getB_gender.equals("Girl")) {
                    radioButtonID = R.id.RB_Female_UAD;
                } else if (getB_gender.equals("Not Sure")) {
                    radioButtonID = R.id.RB_NotSure_UAD;
                }
                RadioButton selectedRB  = getActivity().findViewById(radioButtonID);
                selectedRB.setChecked(true);

                ProgressDialog progressDialog = new ProgressDialog(getActivity());

                view.findViewById(R.id.btnSubmit_updateAD).setOnClickListener(v -> {

                    // Gather all the new details and set it to variables to update the database.
                    String Upd_name = name.getText().toString().trim();
                    String Upd_email = email.getText().toString().trim();
                    String Upd_address = address.getText().toString().trim();
                    String Upd_dob = dob.getText().toString();
                    String Upd_baby_EDD = baby_EDD.getText().toString();
                    RadioGroup rg_gender = getActivity().findViewById(R.id.radioGroup_baby_gender);
                    RadioButton baby_gender  = getActivity().findViewById(rg_gender.getCheckedRadioButtonId());
                    String Upd_baby_gender = baby_gender.getText().toString();

                    // Checks if the user has updated any details before accessing the database.
                    if ( pickImageState || !getM_name.equals(Upd_name) || !getM_email.equals(Upd_email) || !getM_address.equals(Upd_address) || !getM_dob.equals(Upd_dob) || !getB_edd.equals(Upd_baby_EDD) || !getB_gender.equals(Upd_baby_gender) ) {
                        // Insert new data and update the database under that user.
                        DatabaseReference ref = databaseReference.child("Users").child(phoneN).child("AccountDetails");
                        ref.child("Baby_gender").setValue(Upd_baby_gender);
                        ref.child("Baby_EDD").setValue(Upd_baby_EDD);
                        ref.child("Mother_DOB").setValue(Upd_dob);
                        ref.child("Mother_address").setValue(Upd_address);
                        ref.child("Mother_email").setValue(Upd_email);
                        ref.child("Mother_name").setValue(Upd_name);

                        // Check if the user has selected a new image.
                        if (pickImageState) {
                            // Show the progress dialog box for uploading an image.
                            progressDialog.setTitle("Upload in progress");
                            progressDialog.show();

                            // Upload the image and store it inside Firebase Storage under "Users_PP" directory.
                            final StorageReference reference = storage.getReferenceFromUrl("gs://pregnancy-guide-app-841c4.appspot.com").child("Users").child("ProfilePicture").child(phoneN);
                            reference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get the url of the image stored in the Firebase Storage.
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            // Store the url of that image under user's details inside Firebase Realtime Database.
                                            ref.child("profilePicture").setValue(uri.toString());
                                            // Show a message to the user saying the update was successful.
                                            Toast.makeText(getActivity(), "Update successful!", Toast.LENGTH_SHORT).show();
                                            // Close the progress dialog box upon successful upload.
                                            progressDialog.dismiss();
                                        }
                                    });
                                }
                                // Show the progress of the upload in %.
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot1) {
                                    // Show the progress of upload
                                    uploadProgress = 100.0 * snapshot1.getBytesTransferred() / snapshot1.getTotalByteCount();
                                    progressDialog.setMessage("Upload progress" + (int) uploadProgress + "%");
                                }
                            });
                        } else {
                            // Show a message to the user saying the update was successful.
                            Toast.makeText(getActivity(), "Update successful!", Toast.LENGTH_SHORT).show();
                            // Go back to Account Details Fragment only after the update is complete.
                            getActivity().getSupportFragmentManager().popBackStack();
                        }

                        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                // Go back to Account Details Fragment only after the upload is complete.
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        });
                    }
                    else {
                        Toast.makeText(getActivity(), "None of the details were updated, only submit if changes applied!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong while trying to access the database!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        uriImage = result;
                        profilePicture.setImageURI(uriImage);
                        pickImageState = true; // An image was selected.
                    } else {
                        pickImageState = false; // No image was selected.
                        // Display an alert message with AlertDialog if no image file was selected.
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("No image file was selected!");
                        builder.setTitle("Upload Image");
                        builder.setNegativeButton("OK", (DialogInterface.OnClickListener) (dialog, which) -> {
                            dialog.cancel();
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
            });


    // Method to expand an image on a PhotoView.
    private void calculateEDD() {
        // Build an AlertDialog box to inflate the child layout.
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
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
        DatePickerHelper datePickerHelper = new DatePickerHelper(getActivity(), et_date_menstrual);
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
                EditText baby_EDD = getActivity().findViewById(R.id.editText_baby_EDD);
                baby_EDD.setText(calculatedEDD);
                // Dismiss the dialog.
                mDialog.dismiss();

                // If no date is selected, show an error using AlertDialog.
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
