package edu.asu.msse.rsingh92.assignment1;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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

public class PlaceDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    private EditText name, description, category, addressTitle, addressStreet, elevation, latitude, longitude;
    private TextView distance;
    private Spinner placePicker;
    private PlaceDescription currentPlace;
    private List<PlaceDescription> otherPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        category = findViewById(R.id.category);
        addressTitle = findViewById(R.id.addressTitle);
        addressStreet = findViewById(R.id.addressStreet);
        elevation = findViewById(R.id.elevation);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        distance = findViewById(R.id.distance);
        placePicker = findViewById(R.id.placeSelector);

        currentPlace = getCurrentPlace();
        otherPlaces = getOtherPlaces();
        populateSpinner();

        name.setText(currentPlace.getName());
        description.setText(currentPlace.getDescription());
        category.setText(currentPlace.getCategory());
        addressTitle.setText(currentPlace.getAddressTitle());
        addressStreet.setText(currentPlace.getAddressStreet());
        elevation.setText(currentPlace.getElevation());
        latitude.setText(currentPlace.getLatitude());
        longitude.setText(currentPlace.getLongitude());

    }

    private PlaceDescription getCurrentPlace(){
        PlaceDescription place = (PlaceDescription)getIntent().getSerializableExtra("Place");
        return place;
    }

    private List<PlaceDescription> getOtherPlaces(){

        List<PlaceDescription> places = (ArrayList<PlaceDescription>) getIntent().getSerializableExtra(
                "OTHER_PLACES");
        return places;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.place_detail_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {

            case R.id.modify:
                openAddActivity();
                return true;

            case R.id.remove:
                AppUtility.openConfirmationDialog(this, "Do you want to remove this place");
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void populateSpinner(){

        List<String> spinnerArray = new ArrayList<String>();

        for(int i=0;i<otherPlaces.size();i++){
            spinnerArray.add(otherPlaces.get(i).getName());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        placePicker.setAdapter(spinnerArrayAdapter);
        placePicker.setOnItemSelectedListener(this);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, currentPlace.getName()+" "+otherPlaces.get(position).getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void openAddActivity(){
        Intent intent = new Intent(this, AddPlaceActivity.class);
        intent.setAction(AppUtility.MODIFY_PLACE);
        intent.putExtra(AppUtility.CURRENT_PLACE, currentPlace);
        startActivity(intent);
    }

}
