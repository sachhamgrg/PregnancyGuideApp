package com.example.pregnancyguideapp.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class UserDetailsCache {

    SharedPreferences sp;

    public UserDetailsCache(Context context){
        sp = context.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
    }

    public UserDetailsCache(Thread thread) {
    }

    public void setPhoneN(String phone) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("User_PhoneN", phone);
        editor.apply();
    }

    public String getPhoneN() {
        return sp.getString("User_PhoneN", "");
    }

}
