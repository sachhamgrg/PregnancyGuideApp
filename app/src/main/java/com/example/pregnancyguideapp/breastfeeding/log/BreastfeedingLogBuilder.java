package com.example.pregnancyguideapp.breastfeeding.log;

import android.app.Activity;
import android.widget.CheckedTextView;

import com.example.pregnancyguideapp.R;

import java.util.HashMap;

public class BreastfeedingLogBuilder {

    //Use of HashMap to store CheckedTextView with its corresponding time stamps in an order (AM - PM).
    private final HashMap<String, CheckedTextView> checkedTVMap;

    public BreastfeedingLogBuilder(Activity activity) {
        // Store time stamp(hours) and its corresponding resource IDs of CheckedTextView.
        checkedTVMap = new HashMap<>();
        // Insert all the time for AM.
        // Insert 12am
        checkedTVMap.put("12am", activity.findViewById(R.id.checkedTV_12am));
        // Insert 1am - 11 am using for loop.
        for (int i = 1; i < 12; i++) {
            String key = (i + "am");
            int resID = activity.getResources().getIdentifier("checkedTV_" + i + "am", "id", activity.getPackageName());
            checkedTVMap.put(key, activity.findViewById(resID));

        }
        // Insert all the time for PM.
        // Insert 12pm
        checkedTVMap.put("12pm", activity.findViewById(R.id.checkedTV_12pm));
        // Insert 1pm - 11 pm using for loop.
        for (int i = 1; i < 12; i++) {
            String key = (i + "pm");
            int resID = activity.getResources().getIdentifier("checkedTV_" + i + "pm", "id", activity.getPackageName());
            checkedTVMap.put(key, activity.findViewById(resID));
        }
    }

    // This method returns the HashMap 'checkedTVMap' on call.
    public HashMap<String, CheckedTextView> getCheckedTVMap() {
        return checkedTVMap;
    }

}
