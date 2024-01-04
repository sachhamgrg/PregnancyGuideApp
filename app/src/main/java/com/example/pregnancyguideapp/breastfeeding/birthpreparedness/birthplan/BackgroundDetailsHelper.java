package com.example.pregnancyguideapp.breastfeeding.birthpreparedness.birthplan;

import java.util.HashMap;

public class BackgroundDetailsHelper {

    private HashMap<String, String> BackgroundDetailsMap = new HashMap<>();

    public BackgroundDetailsHelper() {}

    // Update values of data fields inside Background Details.
    public void updateBDMap(String key, String value) {
        BackgroundDetailsMap.put(key, value);
    }

    // Getter
    public HashMap<String, String> getBDMap() {
        return BackgroundDetailsMap;
    }

    // Setter
    public void setBDMap(HashMap<String, String> BDMap) {
        this.BackgroundDetailsMap = BDMap;
    }
}
