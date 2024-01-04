package com.example.pregnancyguideapp.breastfeeding.birthpreparedness.birthplan;

import android.app.Activity;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.HashMap;
import com.example.pregnancyguideapp.R;

public class LaborHelper {

    private HashMap<String, String> LaborMap = new HashMap<>();

    public LaborHelper() {}

    // Update values of data fields inside Labor.
    public void updateLaborMap(String key, String value) {
        LaborMap.put(key, value);
    }

    // Getter
    public HashMap<String, String> getLaborMap() {
        return LaborMap;
    }

    // Setter
    public void setLaborMap(HashMap<String, String> LMap) {
        this.LaborMap = LMap;
    }

    // Helper method for RadioGroup(s) to set radioButtons according to the data provided.
    public void RadioGroupHelper(Activity activity,HashMap<String, String> DataMap ) {

        // ----------------------------Labor Preferences Radio group--------------------------------
        String getLaborPref = DataMap.get("LaborPreferences");
        int LaborPrefRadioButtonID;
        if (getLaborPref.equals(activity.getResources().getString(R.string.labor_pref_1))) {
            LaborPrefRadioButtonID = R.id.LaborPref_1;
        } else if (getLaborPref.equals(activity.getResources().getString(R.string.labor_pref_2))) {
            LaborPrefRadioButtonID = R.id.LaborPref_2;
        } else if (getLaborPref.equals(activity.getResources().getString(R.string.labor_pref_3))) {
            LaborPrefRadioButtonID = R.id.LaborPref_3;
        } else {
            LaborPrefRadioButtonID = R.id.LaborPref_4;
            EditText LaborOthers = activity.findViewById(R.id.editTextLaborPrefOthers);
            LaborOthers.setText(getLaborPref);
        }
        RadioButton selectedButtonLP = activity.findViewById(LaborPrefRadioButtonID);
        selectedButtonLP.setChecked(true);

        // ---------------------------Pain Management Radio group-----------------------------------
        String getPainMgmt = DataMap.get("PainManagement");
        int PainMgmtRadioButtonID;
        if (getPainMgmt.equals(activity.getResources().getString(R.string.pain_mgmt_1))) {
            PainMgmtRadioButtonID = R.id.LaborPain_1;
        } else if (getPainMgmt.equals(activity.getResources().getString(R.string.pain_mgmt_2))) {
            PainMgmtRadioButtonID = R.id.LaborPain_2;
        } else if (getPainMgmt.equals(activity.getResources().getString(R.string.pain_mgmt_3))) {
            PainMgmtRadioButtonID = R.id.LaborPain_3;
        } else {
            PainMgmtRadioButtonID = R.id.LaborPain_4;
            EditText PainMgmtOthers = activity.findViewById(R.id.editTextPainManagementOthers);
            PainMgmtOthers.setText(getPainMgmt);
        }
        RadioButton selectedButtonPM = activity.findViewById(PainMgmtRadioButtonID);
        selectedButtonPM.setChecked(true);
    }

}
