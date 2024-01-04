package com.example.pregnancyguideapp.breastfeeding.birthpreparedness.birthplan;

import android.app.Activity;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.pregnancyguideapp.R;

import java.util.HashMap;

public class DeliveryHelper {

    private HashMap<String, String> DeliveryMap = new HashMap<>();

    public DeliveryHelper() {}

    // Update values of data fields inside Delivery.
    public void updateDeliveryMap(String key, String value) {
        DeliveryMap.put(key, value);
    }

    // Getter
    public HashMap<String, String> getDeliveryMap() {
        return DeliveryMap;
    }

    // Setter
    public void setDeliveryMap(HashMap<String, String> DMap) {
        this.DeliveryMap = DMap;
    }

    // Helper method for RadioGroup(s) to set radioButtons according to the data provided.
    public void RadioGroupHelper(Activity activity,HashMap<String, String> DataMap ) {

        // ------------------------Delivery Preferences Radio group---------------------------------
        String getDP = DataMap.get("DeliveryPreferences");
        int DPRadioButtonID;
        if (getDP.equals(activity.getResources().getString(R.string.delivery_pref_1))) {
            DPRadioButtonID = R.id.DeliveryPref_1;
        } else if (getDP.equals(activity.getResources().getString(R.string.delivery_pref_2))) {
            DPRadioButtonID = R.id.DeliveryPref_2;
        } else if (getDP.equals(activity.getResources().getString(R.string.delivery_pref_3))) {
            DPRadioButtonID = R.id.DeliveryPref_3;
        } else {
            DPRadioButtonID = R.id.DeliveryPref_4;
            EditText DPOthers = activity.findViewById(R.id.editTextDeliveryPrefOthers);
            DPOthers.setText(getDP);
        }
        RadioButton selectedButtonDP = activity.findViewById(DPRadioButtonID);
        selectedButtonDP.setChecked(true);

        // ---------------------------Birth Partner Radio group-------------------------------------
        String getBP = DataMap.get("BirthPartner");
        int BPRadioButtonID = 0;
        if (getBP.equals(activity.getResources().getString(R.string.yes))) {
            BPRadioButtonID = R.id.BirthPartner_1;
        } else if (getBP.equals(activity.getResources().getString(R.string.no))) {
            BPRadioButtonID = R.id.BirthPartner_2;
        }
        RadioButton selectedButtonBP = activity.findViewById(BPRadioButtonID);
        selectedButtonBP.setChecked(true);

        // ------------------------------------Episiotomy Radio group-------------------------------
        String getEpisiotomy = DataMap.get("Episiotomy");
        int EpisiotomyRadioButtonID;
        if (getEpisiotomy.equals(activity.getResources().getString(R.string.delivery_episiotomy_1))) {
            EpisiotomyRadioButtonID = R.id.Episiotomy_1;
        } else if (getEpisiotomy.equals(activity.getResources().getString(R.string.delivery_episiotomy_2))) {
            EpisiotomyRadioButtonID = R.id.Episiotomy_2;
        } else if (getEpisiotomy.equals(activity.getResources().getString(R.string.delivery_episiotomy_3))) {
            EpisiotomyRadioButtonID = R.id.Episiotomy_3;
        } else {
            EpisiotomyRadioButtonID = R.id.Episiotomy_4;
            EditText EpisiotomyOthers = activity.findViewById(R.id.editTextEpisiotomyOthers);
            EpisiotomyOthers.setText(getEpisiotomy);
        }
        RadioButton selectedButtonE = activity.findViewById(EpisiotomyRadioButtonID);
        selectedButtonE.setChecked(true);

        // ------------------------------------Cesarean Radio group---------------------------------
        String getCesarean = DataMap.get("Cesarean");
        int CesareanButtonID;
        if (getCesarean.equals(activity.getResources().getString(R.string.delivery_cesarean_1))) {
            CesareanButtonID = R.id.Cesarean_1;
        } else if (getCesarean.equals(activity.getResources().getString(R.string.delivery_cesarean_2))) {
            CesareanButtonID = R.id.Cesarean_2;
        } else if (getCesarean.equals(activity.getResources().getString(R.string.delivery_cesarean_3))) {
            CesareanButtonID = R.id.Cesarean_3;
        } else {
            CesareanButtonID = R.id.Cesarean_4;
            EditText CesareanOthers = activity.findViewById(R.id.editTextCesareanOthers);
            CesareanOthers.setText(getCesarean);
        }
        RadioButton selectedButtonC = activity.findViewById(CesareanButtonID);
        selectedButtonC.setChecked(true);

    }
}
