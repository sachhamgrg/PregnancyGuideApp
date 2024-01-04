package com.example.pregnancyguideapp.breastfeeding.log;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.pregnancyguideapp.HomeActivity;
import com.example.pregnancyguideapp.R;

import java.util.HashMap;

public class BreastfeedingLogAlarmReceiver extends BroadcastReceiver {

    private static HashMap<String, Boolean> myHashMap = new HashMap<>();
    final String channel_ID = "ch1";

    @Override
    public void onReceive(Context context, Intent intent) {

        /*
        // Display the hashmap in logcat
        for (Map.Entry<String, Boolean> entry : myHashMap.entrySet()) {
            Log.d("myTag", ( entry.getKey()+" : " + entry.getValue() ) );
        }
         */

        int count = 0;
        // Get all the boolean values from the hashmap
        for (Boolean value : myHashMap.values()) {
            // Check if the condition is met
            if (value) {count++;}
        }

        // Checks if the log for that day satisfies the condition. (condition: Feed more than 4 times by noon)
        if (count < 4) {

            // Set the intent to open the BreastfeedingLogFragment() fragment inside fragment container of HomeActivity.
            Intent intent_log = new Intent(context, HomeActivity.class);
            intent_log.putExtra("FRAGMENT_TO_OPEN", "BreastfeedingLogFragment");
            // Set the pending intent of the notification banner to open direct to the intent_log.
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent_log, PendingIntent.FLAG_IMMUTABLE);
            // Build the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channel_ID);
            builder.setSmallIcon(R.drawable.ic_notification);
            builder.setContentTitle("Breastfeeding Log");
            builder.setContentText("Feed your baby enough throughout the day.");
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
            builder.setContentIntent(pendingIntent);
            builder.setAutoCancel(true);
            // Send notification
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(1, builder.build());
        }
    }

    // Gets the hashmap of Breastfeeding log checkedTextView states.
    public static void setMyHashMap(HashMap<String, Boolean> hashMap) {
        myHashMap = hashMap;
    }


}
