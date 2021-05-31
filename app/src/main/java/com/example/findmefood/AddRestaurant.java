package com.example.findmefood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AddRestaurant extends AppCompatActivity implements View.OnClickListener
{
    // Declaring variables.
    Button getCurrentLocation, showOnMap, saveRestaurant;
    EditText placeName, locationField;
    String coordinates;
    private DBManager dbManager;

    // Function for handling getting the location
    public void getLocation(View view)
    {
        // Ensure that the field is not empty
        if(placeName.length() == 0)
        {
            Toast.makeText(this, "Please enter a valid restaurant name", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent getLocation = new Intent(this, GetLocation.class);
            startActivity(getLocation);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);

        // Initialising variables.
        getCurrentLocation = findViewById(R.id.getCurrentLocation);
        showOnMap = findViewById(R.id.showOnMap);
        saveRestaurant = findViewById(R.id.saveRestaurant);
        placeName = findViewById(R.id.placeName);
        locationField = findViewById(R.id.locationField);

        dbManager = new DBManager(this);
        dbManager.open();

        Intent intent = getIntent();
        String latLong = intent.getStringExtra("latLong");
        String name =  intent.getStringExtra("name");

        locationField.setText(latLong);
        placeName.setText(name);

        // Handle show on map button click.
        showOnMap.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Check that the fields are not empty.
                if(placeName.length() != 0 && locationField.length() != 0)
                {
                    String address = locationField.getText().toString();
                    // Check for valid coordinates.
                    if (isCoordinates(address) == true)
                    {
                        // Create the intent to display location on map.
                        Intent locateOnMap = new Intent(getApplicationContext(), Map.class);
                        locateOnMap.putExtra("latLong", locationField.getText().toString());
                        locateOnMap.putExtra("name", placeName.getText().toString());
                        startActivity(locateOnMap);
                    }
                    else
                    {
                        // If coordinates are not valid, attempt to get them through geocoding.
                        String location = locationField.getText().toString();
                        GeocodingLocation locationAddress = new GeocodingLocation();
                        locationAddress.getAddressFromLocation(location, getApplicationContext(), new GeocoderHandler());
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please enter a valid restaurant name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        saveRestaurant.setOnClickListener(this);
    }

    // Handle save location button click.
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.saveRestaurant:
                String loc = locationField.getText().toString();

                // Ensure that the coordinate are valid.
                if (isCoordinates(loc) == true)
                {
                    dbManager.insert(placeName.getText().toString(),loc);
                    Toast.makeText(this, "Restaurant saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    // If coordinates are not valid, attempt to get them through geocoding.
                    Geocoder geocoder = new Geocoder(this.getApplicationContext(), Locale.getDefault());
                    String result = null;

                    try {
                        List addressList = geocoder.getFromLocationName(loc, 1);

                        if (addressList != null && addressList.size() > 0) {
                            Address address = (Address) addressList.get(0);
                            StringBuilder sb = new StringBuilder();
                            sb.append(address.getLatitude()).append(",");
                            sb.append(address.getLongitude()).append("");
                            result = sb.toString();
                            dbManager.insert(placeName.getText().toString(), result);
                            Toast.makeText(this, "Restaurant saved successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Location coordinates can't be found please enter a valid address. ", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(this, "Unable to connect to Geocoder", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private class GeocoderHandler extends Handler
    {
        // Function for getting the location and showing it on the map in the case that the coordinates are not valid.
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }

            locationField.setText(locationAddress.toString());
            coordinates = locationAddress.toString();

            if (isCoordinates(coordinates)){
                Intent showOnMap = new Intent(getApplicationContext(), Map.class);
                showOnMap.putExtra("latLong", coordinates);
                showOnMap.putExtra("name", placeName.getText().toString());
                startActivity(showOnMap);
            }
            else
            {
                Toast.makeText(AddRestaurant.this, "Location could not be found.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Method to check if the coordinates are valid.
    private Boolean isCoordinates(String address)
    {
        try {
            String[] coordinates = address.split(",");
            String lat = coordinates[1];

        }
        catch (Exception e)
        {
            //Toast.makeText(AddRestaurant.this, "Yo", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}