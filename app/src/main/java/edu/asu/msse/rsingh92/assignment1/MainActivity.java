package edu.asu.msse.rsingh92.assignment1;

/*
        * Copyright 2020 Rohit Kumar Singh,
        *
        * Licensed under the Apache License, Version 2.0 (the "License");
        * you may not use this file except in compliance with the License.
        * You may obtain a copy of the License at
        *
        * http://www.apache.org/licenses/LICENSE-2.0
        *
        * Unless required by applicable law or agreed to in writing, software
        * distributed under the License is distributed on an "AS IS" BASIS,
        * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        * See the License for the specific language governing permissions and
        * limitations under the License.
        *
        * @author Rohit Kumar Singh rsingh92@asu.edu
        *
        * @version January 2016
        */

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {


    private TextView name, description, category, addressTitle, addressStreet, elevation, latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        category = findViewById(R.id.category);
        addressTitle = findViewById(R.id.addressTitle);
        addressStreet = findViewById(R.id.addressStreet);
        elevation = findViewById(R.id.elevation);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);

        String jsonString = getJsonString();
        PlaceDescription placeDescription = new PlaceDescription(jsonString);

        name.setText(placeDescription.getName());
        description.setText(placeDescription.getDescription());
        category.setText(placeDescription.getCategory());
        addressTitle.setText(placeDescription.getAddressTitle());
        addressStreet.setText(placeDescription.getAddressStreet());
        elevation.setText(placeDescription.getElevation());
        latitude.setText(placeDescription.getLatitude());
        longitude.setText(placeDescription.getLongitude());
    }

    private String getJsonString(){
        String json = null;
        try {
            InputStream is = this.getAssets().open("location.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
