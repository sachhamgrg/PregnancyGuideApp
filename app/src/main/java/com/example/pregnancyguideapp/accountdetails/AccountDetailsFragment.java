package com.example.pregnancyguideapp.accountdetails;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.pregnancyguideapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountDetailsFragment extends Fragment{

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pregnancy-guide-app-841c4-default-rtdb.firebaseio.com/");
    CircleImageView profilePicture;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the title of toolbar for Home activity.
        TextView title_toolbar = getActivity().findViewById(R.id.toolbar_title);
        title_toolbar.setText(R.string.account_details);

        // Check the pregnancy item on the navigation menu.
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_account_details);

        // Get the current user's phone number from Shared Preferences and store it in a variable phoneN. This variable will be used later to extract all the other user details from the database.
        SharedPreferences sp = getActivity().getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        String phoneN = sp.getString("User_PhoneN", "");

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

                // (Mother's information)
                // Set mother's profile picture if a profile photo exists inside the database.
                if (getProfileImage != null) {
                    profilePicture = getActivity().findViewById(R.id.circleImageView_profile_picture);
                    Glide.with(requireContext()).load(getProfileImage).into(profilePicture);
                }
                // Get name from the list and set it to the textview to display mother's name.
                TextView name_AD = getActivity().findViewById(R.id.textView_mother_name_assigned);
                name_AD.setText(": " + getM_name);
                // Get phone number from the list and set it to the textview to display mother's phone number.
                TextView phone_AD = getActivity().findViewById(R.id.textView_mother_phone_assigned);
                phone_AD.setText(": " + phoneN);
                // Get email from the list and set it to the textview to display mother's email.
                TextView email_AD = getActivity().findViewById(R.id.textView_mother_email_assigned);
                email_AD.setText(": " + getM_email);
                // Get address from the list and set it to the textview to display mother's address.
                TextView address_AD = getActivity().findViewById(R.id.textView_mother_address_assigned);
                address_AD.setText(": " + getM_address);
                // Get address from the list and set it to the textview to display mother's address.
                TextView dob_AD = getActivity().findViewById(R.id.textView_mother_DOB_assigned);
                dob_AD.setText(": " + getM_dob);

                // (Baby's information)
                // Get EDD from the list and set it to the textview to display baby's EDD.
                TextView baby_EDD = getActivity().findViewById(R.id.textView_baby_EDD_assigned);
                baby_EDD.setText(": " + getB_edd);
                // Get phone number from the list and set it to the textview to display mother's phone number.
                TextView gender_AD = getActivity().findViewById(R.id.textView_baby_gender_assigned);
                gender_AD.setText(": " + getB_gender);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        if(view != null) {
            view.findViewById(R.id.btnUpdate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UpdateAccountDetailsFragment(), "UpdateAccountDetails_Frag").addToBackStack(null).commit();
                }
            });
        }
    }
}
