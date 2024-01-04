package com.example.pregnancyguideapp.breastfeeding.log;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckedTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pregnancyguideapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BreastfeedingLogFragment extends Fragment {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pregnancy-guide-app-841c4-default-rtdb.firebaseio.com/");
    StorageReference StorageBreastfeedingRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://pregnancy-guide-app-841c4.appspot.com").child("Application").child("Breastfeeding");
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    String day;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Set the title of toolbar for Home activity.
        TextView title_toolbar = (TextView) getActivity().findViewById(R.id.toolbar_title);
        title_toolbar.setText(R.string.breastfeeding);

        // Check the pregnancy item on the navigation menu.
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_breastfeeding);

        return inflater.inflate(R.layout.fragment_breastfeedinglog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Makes sure the breastfeeding log layout is not available for the user to access at the start unless the user selects a day of their choice.
        RelativeLayout layout_breastfeedingLog = getActivity().findViewById(R.id.layout_breastfeedingLog);
        layout_breastfeedingLog.setVisibility(View.GONE);

        // Get the current user's phone number from Shared Preferences and store it in a variable phoneN. This variable will be used later to extract all the other user details from the database.
        SharedPreferences sp = getActivity().getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        String phoneN = sp.getString("User_PhoneN", "");

        //Instantiation of 'BreastfeedingLogBuilder' class to call the method 'getCheckedTVMap' to obtain the HashMap of time stamps.
        BreastfeedingLogBuilder breastfeedingLogBuilder = new BreastfeedingLogBuilder(getActivity());
        HashMap<String, CheckedTextView> checkedTVMap = breastfeedingLogBuilder.getCheckedTVMap();

        // Select/Unselect the time stamps(hours).
        // Gets the boolean value of each time stamp from the HashMap and set that value to true if false and vice versa upon user's interaction.
        for (CheckedTextView checkedTV : checkedTVMap.values()) {
            checkedTV.setOnClickListener(v -> {
                boolean isChecked = checkedTV.isChecked();
                checkedTV.setChecked(!isChecked);
            });
        }

        // Dropdown list to select days.
        autoCompleteTextView = getActivity().findViewById(R.id.autoCompleteTextView);
        String[] list_days = getResources().getStringArray(R.array.list_days);
        adapterItems = new ArrayAdapter<>(getActivity(), R.layout.breastfeeding_list_days, list_days);

        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Make the layout visible for the user to use breastfeeding log.
                layout_breastfeedingLog.setVisibility(View.VISIBLE);

                day = parent.getItemAtPosition(position).toString();
                Toast.makeText(getActivity(), day, Toast.LENGTH_SHORT).show();

                DatabaseReference userRef = databaseReference.child("Users").child(phoneN).child("BreastfeedingLog").child(day);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // If a certain day has been logged in, then display the logged data else, set all the checkedTextView value to false.
                        if ( snapshot.exists() ) {
                            // Parse the data into a CheckedTVDataModel object
                            BreastfeedingLogHelper LogData = snapshot.getValue(BreastfeedingLogHelper.class);

                            // Update the UI to display the CheckedTextView data.
                            if (LogData != null) {
                                HashMap<String, Boolean> checkedTVMap = LogData.getLogMap();
                                for (Map.Entry<String, Boolean> entry : checkedTVMap.entrySet()) {
                                    String hour = entry.getKey();
                                    boolean isChecked = entry.getValue();
                                    // Get the resource ID of checkedTextView for each time stamp.
                                    int resID = getActivity().getResources().getIdentifier("checkedTV_" + hour, "id", getActivity().getPackageName());
                                    CheckedTextView checkedTV = getActivity().findViewById(resID);
                                    // Update the CheckedTextView object with the new boolean value
                                    checkedTV.setChecked(isChecked);
                                }

                                // Send checkedTVMap HashMap to BreastfeedingLogAlarmReceiver class.
                                BreastfeedingLogAlarmReceiver.setMyHashMap(checkedTVMap);
                                // Call the method to set the alarm when a specific day is selected.
                                setBreastfeedingLogAlarm();
                            }

                        } else {
                            // If no logs (days) in the database then clear all the checkedTextView from other days.
                            HashMap<String, CheckedTextView> newCheckedTVMap = breastfeedingLogBuilder.getCheckedTVMap();
                            for (Map.Entry<String, CheckedTextView> entry : newCheckedTVMap.entrySet()) {
                                CheckedTextView checkedTV = entry.getValue();
                                checkedTV.setChecked(false);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // When the user wants to submit their log.
        view.findViewById(R.id.btnUpdate_breastfeedingLog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BreastfeedingLogHelper LogHelper = new BreastfeedingLogHelper();
                // Iterate over the Map to get the boolean value of each CheckedTextView hour(time stamp).
                for (Map.Entry<String, CheckedTextView> entry : checkedTVMap.entrySet()) {
                    String hour = entry.getKey();
                    CheckedTextView checkedTV = entry.getValue();
                    boolean isChecked = checkedTV.isChecked();
                    // Store the boolean value of each time stamp.
                    LogHelper.putLogMap(hour, isChecked);
                }
                // Save the data model object to Firebase Realtime Database.
                databaseReference.child("Users").child(phoneN).child("BreastfeedingLog").child(day).setValue(LogHelper);
                Toast.makeText(getActivity(), "Log successfully submitted!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setBreastfeedingLogAlarm() {
        // Set the pending intent for the alarm manager.
        Intent intent = new Intent(getActivity(), BreastfeedingLogAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Set the timer for 12pm (afternoon).
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // Set the alarm for 12pm when the pending intent will be called.
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Call the BreastfeedingLogAlarmReceiver class.
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

}
