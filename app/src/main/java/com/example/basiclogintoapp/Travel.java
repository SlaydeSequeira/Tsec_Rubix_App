package com.example.basiclogintoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Travel extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap googleMap;
    private LatLng origin = null; // Initialize origin as null
    private LatLng destination = null; // Initialize destination as null

    private static final double START_LATITUDE = 37.7749;
    private static final double START_LONGITUDE = -122.4194;
    float[] results = new float[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        FloatingActionButton centerButton = findViewById(R.id.centerButton);


        centerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Center the map on the user's location or do something similar
                // For simplicity, I'm just animating to the origin coordinates here
                    // Assuming you have a LatLng object for the target location
                    LatLng targetLocation = new LatLng(19.051114, 72.824993);

// Create a CameraPosition for the target location with a desired zoom level
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(targetLocation)
                            .zoom(15.0f) // Adjust the zoom level as needed (higher values for closer zoom)
                            .build();

// Create a CameraUpdate using the CameraPosition
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);

// Move the camera to the specified position and zoom level
                    googleMap.animateCamera(cameraUpdate);                }
            
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        // Set OnMapClickListener to capture tap events
        googleMap.setOnMapClickListener(this);

        // Set OnMarkerClickListener to capture marker click events
        googleMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // Check if origin is not set, set it
        if (origin == null) {
            origin = latLng;
            googleMap.addMarker(new MarkerOptions().position(origin).title("Origin"));
        } else {
            // If origin is set, set the destination
            destination = latLng;

            // Clear previous markers and polyline
            googleMap.clear();

            // Add marker for origin
            googleMap.addMarker(new MarkerOptions().position(origin).title("Origin"));

            // Add marker for new destination
            googleMap.addMarker(new MarkerOptions().position(destination).title("Destination"));

            // Draw route between origin and new destination
            PolylineOptions polylineOptions = new PolylineOptions()
                    .add(origin)
                    .add(destination)
                    .color(R.color.polylineColor); // Set your desired color
            Polyline polyline = googleMap.addPolyline(polylineOptions);

            // Calculate distance (in meters) between origin and new destination
            android.location.Location.distanceBetween(
                    origin.latitude, origin.longitude,
                    destination.latitude, destination.longitude,
                    results);

            // Display distance in a toast message
            Toast.makeText(this, "Distance: " + results[0]/1000 + "kms"+" Cost: "+ results[0]/50, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Switch to a new activity when a marker is clicked
        Intent intent = new Intent(this, Payment.class);
        int cost = (int) (results[0] / 50); // Assuming you want to cast the float to an integer
        intent.putExtra("Cost",cost);
        startActivity(intent);
        return true; // Consume the event to prevent default behavior
    }
}
