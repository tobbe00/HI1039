package com.example.cpr;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * BaseActivity is an abstract class that provides common functionality for all activities in the application.
 * It sets up the options menu and handles common menu item clicks such as navigating to settings and logging out.
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * Initializes the activity.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Sets up the options menu.
     * @param menu The options menu in which you place your items.
     * @return true for the menu to be displayed; false to suppress showing it.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        adjustMenu(menu);
        return true;
    }

    /**
     * Handles option menu item clicks.
     * @param item The menu item that was selected.
     * @return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(BaseActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout:
                logout();
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Method to adjust the menu items. This method can be overridden in subclasses to customize the menu.
     * @param menu The options menu in which you place your items.
     */
    protected void adjustMenu(Menu menu) {
        // Default implementation does nothing
        // Override in subclasses to customize the menu
    }

    /**
     * Logs the user out and navigates to the login activity.
     */
    private void logout(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
