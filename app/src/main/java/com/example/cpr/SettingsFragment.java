package com.example.cpr;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;


public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);


        EditTextPreference ipaddressPreference = findPreference("ipaddress");
        if (ipaddressPreference != null) {

            if(LoginActivity.getUrl()!=null){
                ipaddressPreference.setSummary((LoginActivity.getUrl()));
            }

            ipaddressPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                String newIpAddress = (String) newValue;
                if(newIpAddress.isEmpty()){
                    preference.setSummary("No IP address set");
                }else{
                    LoginActivity.setUrl(newIpAddress);
                    preference.setSummary(LoginActivity.getUrl());
                }
                return true;
            });
        }
    }


}
