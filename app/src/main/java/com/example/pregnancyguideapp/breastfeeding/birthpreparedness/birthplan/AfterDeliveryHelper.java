package com.example.pregnancyguideapp.breastfeeding.birthpreparedness.birthplan;

import android.app.Activity;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.pregnancyguideapp.R;

import java.util.HashMap;

public class AfterDeliveryHelper {

    private HashMap<String, String> AfterDeliveryMap = new HashMap<>();

    public AfterDeliveryHelper() {}

    // Update values of data fields inside After Delivery.
    public void updateAfterDeliveryMap(String key, String value) {
        AfterDeliveryMap.put(key, value);
    }

    // Getter
    public HashMap<String, String> getAfterDeliveryMap() {
        return AfterDeliveryMap;
    }

    // Setter
    public void setAfterDeliveryMap(HashMap<String, String> ADMap) {
        this.AfterDeliveryMap = ADMap;
    }

    // Helper method for RadioGroup(s) to set radioButtons according to the data provided.
    public void RadioGroupHelper(Activity activity, HashMap<String, String> DataMap ) {

        // ---------------------------Hold Baby Radio group-------------------------------------
        String getHB = DataMap.get("HoldBaby");
        int HBRadioButtonID = 0;
        if (getHB.equals(activity.getResources().getString(R.string.yes))) {
            HBRadioButtonID = R.id.holdBaby_1;
        } else if (getHB.equals(activity.getResources().getString(R.string.no))) {
            HBRadioButtonID = R.id.holdBaby_2;
        }
        RadioButton selectedButtonHB = activity.findViewById(HBRadioButtonID);
        selectedButtonHB.setChecked(true);

        // -------------------------Delay New Born Procedures Radio group---------------------------
        String getProcedures = DataMap.get("delayNewBornProcedures");
        int ProceduresRadioButtonID = 0;
        if (getProcedures.equals(activity.getResources().getString(R.string.yes))) {
            ProceduresRadioButtonID = R.id.delayP_1;
        } else if (getProcedures.equals(activity.getResources().getString(R.string.no))) {
            ProceduresRadioButtonID = R.id.delayP_2;
        }
        RadioButton selectedButtonProcedures = activity.findViewById(ProceduresRadioButtonID);
        selectedButtonProcedures.setChecked(true);

        // ----------------------------Medical Explanation Radio group------------------------------
        String getME = DataMap.get("MedicalExplanation");
        int MERadioButtonID = 0;
        if (getME.equals(activity.getResources().getString(R.string.yes))) {
            MERadioButtonID = R.id.Explanation_1;
        } else if (getME.equals(activity.getResources().getString(R.string.no))) {
            MERadioButtonID = R.id.Explanation_2;
        }
        RadioButton selectedButtonME = activity.findViewById(MERadioButtonID);
        selectedButtonME.setChecked(true);

        // ------------------------Keeping Baby Preferences Radio group-----------------------------
        String getKeepPref = DataMap.get("KeepBabyPreferences");
        int KBPrefRadioButtonID;
        if (getKeepPref.equals(activity.getResources().getString(R.string.AD_keep_baby_pref_1))) {
            KBPrefRadioButtonID = R.id.keepBabyPref_1;
        } else if (getKeepPref.equals(activity.getResources().getString(R.string.AD_keep_baby_pref_2))) {
            KBPrefRadioButtonID = R.id.keepBabyPref_2;
        } else if (getKeepPref.equals(activity.getResources().getString(R.string.AD_keep_baby_pref_3))) {
            KBPrefRadioButtonID = R.id.keepBabyPref_3;
        } else {
            KBPrefRadioButtonID = R.id.keepBabyPref_4;
            EditText KBPrefOthers = activity.findViewById(R.id.editTextKeepBabyPrefOthers);
            KBPrefOthers.setText(getKeepPref);
        }
        RadioButton selectedButtonKBPref = activity.findViewById(KBPrefRadioButtonID);
        selectedButtonKBPref.setChecked(true);

        // ------------------------Feeding Baby Preferences Radio group-----------------------------
        String getFBPref = DataMap.get("FeedBabyPreferences");
        int FBPrefRadioButtonID = 0;
        if (getFBPref.equals(activity.getResources().getString(R.string.AD_feed_baby_pref_1))) {
            FBPrefRadioButtonID = R.id.feedBabyPref_1;
        } else if (getFBPref.equals(activity.getResources().getString(R.string.AD_feed_baby_pref_2))) {
            FBPrefRadioButtonID = R.id.feedBabyPref_2;
        } else if (getFBPref.equals(activity.getResources().getString(R.string.AD_feed_baby_pref_3))) {
            FBPrefRadioButtonID = R.id.feedBabyPref_3;
        }

        RadioButton selectedButtonFBPref = activity.findViewById(FBPrefRadioButtonID);
        selectedButtonFBPref.setChecked(true);

    }
}
