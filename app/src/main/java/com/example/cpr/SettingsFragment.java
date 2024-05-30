package com.example.cpr;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

/**
 * SettingsFragment is used to build an interface for user preferences stored in XML.
 * It allows users to configure settings such as IP addresses for network connectivity.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    /**
     * Loads preferences from an XML resource and sets up change listeners to update UI and settings values.
     */
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {

        // Load the preferences from an XML resource specified in R.xml.settings
        setPreferencesFromResource(R.xml.settings, rootKey);

        // Find the EditTextPreference for IP address settings
        EditTextPreference ipaddressPreference = findPreference("url");
        if (ipaddressPreference != null) {
            if(LoginActivity.getUrl()!=null){
                // Set the initial summary with the current URL from LoginActivity
                ipaddressPreference.setSummary((LoginActivity.getUrl()));
            }
            // Define response to user changes in the IP address setting
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
