package com.example.cpr;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * SettingsActivity manages the display of settings options for the application.
 * It hosts a fragment that contains the settings.
 */
public class SettingsActivity extends AppCompatActivity {

    /**
     * Called when the activity is starting. Sets up the settings UI.
     * If the activity is being re-initialized after previously being shut down,
     * then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     * Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        // Begin a new FragmentTransaction, add the SettingsFragment to the 'settings_container' FrameLayout.
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();
    }
}
