package com.example.findmefood;

import androidx.fragment.app.FragmentActivity;

import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShowAllMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DBManager dbManager;

    ArrayList<String> restaurantLocations = new ArrayList<String>( );
    ArrayList<LatLng> coordinates = new ArrayList<LatLng>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        dbManager = new DBManager(this);

        dbManager.open();
        Cursor cursor = dbManager.fetch();

        // Get all the locations from the database.
        while (cursor.moveToNext())
        {
            //Toast.makeText(this, cursor.getString(2), Toast.LENGTH_LONG).show();
            restaurantLocations.add(cursor.getString(2));
        }

        // Loop through an array of coordinates and get the corresponding lat and lon values.
        for (int i = 0; i < restaurantLocations.size(); i++)
        {
            Toast.makeText(this, restaurantLocations.get(i), Toast.LENGTH_SHORT).show();
            String[] latlng = restaurantLocations.get(i).split(",");

            Double lat = Double.parseDouble(latlng[0]);
            Double lon = Double.parseDouble(latlng[1]);
            coordinates.add(new LatLng(lat, lon));
        }

        // Loop through an array of lat and lon values and create markers.
        for(LatLng cor : coordinates)
        {
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(cor.latitude, cor.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            mMap.addMarker(new MarkerOptions().position(cor).title(address));
        }

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("DEFAULT"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 5));
    }
}