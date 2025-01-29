package com.example.smartspine;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchNotifications;
    private Button buttonCalibrate;
    private EditText edittextUpdateInterval;
    private Button buttonSaveSettings;
    private Button buttonChangeBluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchNotifications = findViewById(R.id.switch_notifications);
        buttonCalibrate = findViewById(R.id.button_calibrate);
        edittextUpdateInterval = findViewById(R.id.edittext_update_interval);
        buttonSaveSettings = findViewById(R.id.button_save_settings);
        buttonChangeBluetooth = findViewById(R.id.button_change_bluetooth);

        loadSettings();

        buttonCalibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement calibration logic
                Toast.makeText(SettingsActivity.this, "Calibrating sensor...", Toast.LENGTH_SHORT).show();
            }
        });

        buttonSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });

        buttonChangeBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement Bluetooth device change logic
                Toast.makeText(SettingsActivity.this, "Change Bluetooth device...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSettings() {
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        boolean notificationsEnabled = preferences.getBoolean("notifications", false);
        int updateInterval = preferences.getInt("update_interval", 1000);

        switchNotifications.setChecked(notificationsEnabled);
        edittextUpdateInterval.setText(String.valueOf(updateInterval));
    }

    private void saveSettings() {
        boolean notificationsEnabled = switchNotifications.isChecked();
        int updateInterval;
        try {
            updateInterval = Integer.parseInt(edittextUpdateInterval.getText().toString());
        } catch (NumberFormatException e) {
            updateInterval = 1000; // Default to 1000ms if input is invalid
        }

        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("notifications", notificationsEnabled);
        editor.putInt("update_interval", updateInterval);
        editor.apply();

        Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
    }
}
