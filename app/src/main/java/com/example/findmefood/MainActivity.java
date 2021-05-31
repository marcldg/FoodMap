package com.example.findmefood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // Function to Handle adding a new retaurant.
    public void addNewRestaurant(View view)
    {
        Intent addRestaurant = new Intent(this, AddRestaurant.class);
        startActivity(addRestaurant);
    }

    // Declaring variables.
    Button addNewPlace, showAllOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialising Variables.
        addNewPlace = findViewById(R.id.addNewPlace);
        showAllOnMap = findViewById(R.id.showAllOnMap);

        showAllOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showRestaurants = new Intent(getApplicationContext(), ShowAllMap.class);
                startActivity(showRestaurants);
            }
        });
    }
}