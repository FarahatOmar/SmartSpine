package com.example.smartspine;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private static final String DEVICE_ADDRESS = "20:20:08:14:25:87"; // HC-06 address
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 1;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private boolean isConnected = false;
    private ImageView postureImageView;


    private TextView statusTextView;
    private Button connectButton;

    private Handler handler = new Handler();
    private InputStream inputStream;

    private String lastPosture = "";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTextView = findViewById(R.id.statusTextView);
        connectButton = findViewById(R.id.connectButton);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnected) {
                    requestBluetoothPermissions();
                } else {
                    disconnectBluetooth();
                }
            }
        });


        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void requestBluetoothPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_PERMISSIONS);
        } else {
            connectBluetooth();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                connectBluetooth();
            } else {
                Toast.makeText(this, "Bluetooth permission is required to connect", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void connectBluetooth() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported on this device", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Please enable Bluetooth", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                requestBluetoothPermissions();
                return;
            }

            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(DEVICE_ADDRESS);
            bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            bluetoothSocket.connect();
            inputStream = bluetoothSocket.getInputStream();
            isConnected = true;
            statusTextView.setText("Connected");
            connectButton.setText("Disconnect");
            startListeningForData();
        } catch (IOException e) {
            Log.e(TAG, "Error connecting to Bluetooth device", e);
            Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
            isConnected = false;
        } catch (SecurityException e) {
            Log.e(TAG, "Bluetooth permission not granted", e);
            Toast.makeText(this, "Bluetooth permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void disconnectBluetooth() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
            }
            isConnected = false;
            statusTextView.setText("Disconnected");
            connectButton.setText("Connect");
        } catch (IOException e) {
            Log.e(TAG, "Error disconnecting from Bluetooth device", e);
            Toast.makeText(this, "Disconnection failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void startListeningForData() {
        final byte[] buffer = new byte[1024];
        final int[] bytes = new int[1];

        Thread thread = new Thread(new Runnable() {
            public void run() {
                while (isConnected) {
                    try {
                        bytes[0] = inputStream.read(buffer);
                        handler.post(new Runnable() {
                            public void run() {
                                String receivedData = new String(buffer, 0, bytes[0]).trim();
                                updatePostureData(receivedData);
                            }
                        });
                        Thread.sleep(500); // Add a delay to reduce rapid updates

                    } catch (IOException e) {
                        Log.e(TAG, "Error reading from input stream", e);
                        isConnected = false;
                    } catch (InterruptedException e) {
                        Log.e(TAG, "Thread sleep interrupted", e);
                    }
                }
            }
        });

        thread.start();
    }

    private void updatePostureData(String receivedData) {
        if (!receivedData.equals(lastPosture)) {
            lastPosture = receivedData;
            statusTextView.setText(receivedData);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main:
                // Already on main screen, do nothing
                break;
            case R.id.nav_history:
                startActivity(new Intent(this, PostureHistory.class));
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.nav_tips:
                startActivity(new Intent(this, PostureTipsActivity.class));
                break;
        }
        drawerLayout.closeDrawers();
        return true;
    }


    private void applySettings() {
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        int updateInterval = preferences.getInt("update_interval", 1000);

        // Use updateInterval for your logic
    }

    // Launch Settings Activity
    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}

