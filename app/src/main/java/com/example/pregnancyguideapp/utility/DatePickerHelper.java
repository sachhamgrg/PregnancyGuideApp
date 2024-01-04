package com.example.pregnancyguideapp.utility;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;

// Prompts the user to choose a date using DatePickerHelper dialog box.
public class DatePickerHelper implements View.OnClickListener{

    private final EditText editText;
    private final Context context;

    public DatePickerHelper(Context context, EditText editText) {
        this.context = context;
        this.editText = editText;
    }

    @Override
    public void onClick(View v) {

        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        // Date picker dialog pop up
        DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                editText.setText(dayOfMonth + "/" + (month+1) + "/" + year);
            }
        }, year, month, day);
        datePicker.show();
    }
}
