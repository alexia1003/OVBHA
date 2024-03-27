package com.example.ovbha;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // 5 seconds delay for SOS signal
    private static final int SOS_DELAY = 5000;
    private List<String> emergencyContacts;
    private LocationManager locationManager;
    // Flag to indicate if emergency mode is active
    private boolean emergencyModeActive = false;
    // Store the last known location when SOS button is clicked
    private Location lastKnownLocation;
    // Add a LocationListener member variable
    private LocationListener locationListener;

    private TextView diagnosticResultTextView;
    private Button diagnoseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize emergency contacts list
        emergencyContacts = new ArrayList<>();
        emergencyContacts.add(" ");
        emergencyContacts.add(" ");
        emergencyContacts.add(" ");

        // Initialize LocationManager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Initialize SOS button
        Button sosButton = findViewById(R.id.sos_button);
        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emergencyModeActive = true; // Set emergency mode active
                sendEmergencyMessage();
            }
        });

        // Initialize the communication button
        Button communicationButton = findViewById(R.id.communication_button);
        communicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                streamlineCommunication(emergencyContacts, "Emergency assistance needed!");
            }
        });

        // Initialize the location listener
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (isEmergencyActive()) {
                    // Resend emergency message with updated coordinates
                    sendEmergencyMessage();
                }
            }
        };

        // Initialize the weather button
        Button weatherButton = findViewById(R.id.weather_button);
        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get current location
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    // Start WeatherActivity and pass latitude and longitude as extras
                    Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Failed to get location!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Initialize the coordinates button
        Button coordButton = findViewById(R.id.coord_button);
        coordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start CoordActivity when the button is clicked
                startActivity(new Intent(MainActivity.this, CoordActivity.class));
            }
        });

        // Initialize button to go to MapActivity
        Button mapButton = findViewById(R.id.map_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapActivity.class));
            }
        });

        // Request location updates
        requestLocationUpdates();

        diagnosticResultTextView = findViewById(R.id.diagnostic_result_text);
        diagnoseButton = findViewById(R.id.diagnose_button);

        final VehicleDiagnostic vehicleDiagnostic = new VehicleDiagnostic();

        // Diagnostics buttons
        diagnoseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String diagnosticResult = vehicleDiagnostic.diagnoseVehicleIssues();
                diagnosticResultTextView.setText(diagnosticResult);
            }
        });

        Button checklistButton = findViewById(R.id.checklistButton);
        checklistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChecklistActivity.class));
            }
        });
    }

    // Method to check if emergency mode is active
    private boolean isEmergencyActive() {
        // Return the flag value
        return emergencyModeActive;
    }

    // Method to request location updates
    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    SOS_DELAY);
            return;
        }

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0, // minimum time interval between updates, in milliseconds
                0, // minimum distance between updates, in meters
                locationListener);
    }

    private void sendEmergencyMessage() {
        // Check for location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    SOS_DELAY);
            return;
        }

        // Get current location
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            // Check if emergency mode is active and the location has changed since SOS button click
            if (isEmergencyActive() && (lastKnownLocation == null || location.distanceTo(lastKnownLocation) > 0)) {
                String message = "Emergency! Current location: Lat " + latitude + ", Long " + longitude;

                // Send emergency message to each contact
                for (String contact : emergencyContacts) {
                    sendSMS(contact, message);
                }

                Toast.makeText(this, "Emergency message sent!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No location change or emergency mode inactive", Toast.LENGTH_SHORT).show();
            }

            // Update last known location
            lastKnownLocation = location;
        } else {
            Toast.makeText(this, "Failed to get location!", Toast.LENGTH_SHORT).show();
        }
    }

    // Define a method to streamline communication and collaboration among contacts
    private void streamlineCommunication(List<String> contacts, String message) {
        for (String contact : contacts) {
            sendSMS(contact, message);
        }
        Toast.makeText(this, "Communication streamlined to expedite help", Toast.LENGTH_SHORT).show();
    }

    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SOS_DELAY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, send emergency message
                sendEmergencyMessage();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove location updates when the activity is destroyed to avoid memory leaks
        locationManager.removeUpdates(locationListener);
    }
}

