package com.example.pregnancyguideapp.breastfeeding.log;

import java.util.HashMap;

public class BreastfeedingLogHelper {
    private HashMap<String, Boolean> checkedTVMap = new HashMap<>();

    public BreastfeedingLogHelper() {}

    // Update boolean values of time stamps.
    public void putLogMap(String hour, boolean isChecked) {
        checkedTVMap.put(hour, isChecked);
    }

    // Getter
    public HashMap<String, Boolean> getLogMap() {
        return checkedTVMap;
    }

    // Setter
    public void setLogMap(HashMap<String, Boolean> checkedTVMap) {
        this.checkedTVMap = checkedTVMap;
    }
}
