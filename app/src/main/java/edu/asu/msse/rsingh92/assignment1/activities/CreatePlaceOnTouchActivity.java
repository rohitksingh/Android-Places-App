package edu.asu.msse.rsingh92.assignment1.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import edu.asu.msse.rsingh92.assignment1.R;
import edu.asu.msse.rsingh92.assignment1.models.PlaceDescription;
import edu.asu.msse.rsingh92.assignment1.utilities.AppUtility;

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
 * @version April 2016
 */
public class CreatePlaceOnTouchActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText name, desc, category, elevation, latitude, longitude, streetTitle, streetAddress;
    private Button save, cancel;

    private PlaceDescription place;

    /***********************************************************************************************
     *                                  Lifecycle methods
     ***********************************************************************************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_place);
        initView();
        getDataFromIntent();
        setViewData();
    }


    /***********************************************************************************************
     *                                  Callback methods
     ***********************************************************************************************/
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.save:
                if(ifValadationPassed()){
                    sendPlaceData();
                }
                break;

            case R.id.cancel:
                dontSendPlaceData();
                break;

            default:
                break;

        }
    }

    /***********************************************************************************************
     *                                   Private methods
     ***********************************************************************************************/

    private PlaceDescription getPlaceFromView(){
        place.setName(name.getText().toString());
        place.setDescription(desc.getText().toString());
        place.setCategory(category.getText().toString());
        place.setElevation(elevation.getText().toString());
        place.setAddressStreet(streetAddress.getText().toString());
        place.setAddressTitle(streetTitle.getText().toString());
        return place;
    }

    private void sendPlaceData(){
        place = getPlaceFromView();
        Intent data = new Intent();
        data.putExtra(AppUtility.MODIFY_PLACE, place);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    private void dontSendPlaceData(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    private boolean ifValadationPassed(){

        String vName = name.getText().toString().trim();
        String vDesc = desc.getText().toString().trim();
        String vAddressStreet = streetAddress.getText().toString().trim();
        String vAddressTitle = streetTitle.getText().toString().trim();
        String vCategory = category.getText().toString().trim();
        String vElevation = elevation.getText().toString().trim();
        String vLongitude = longitude.getText().toString().trim();
        String vLatitude = latitude.getText().toString().trim();


        boolean validationPassed = true;

        if(TextUtils.isEmpty(vName)){
            name.setError("Name is empty");
            validationPassed = false;
        }

        if(TextUtils.isEmpty(vDesc)){
            desc.setError("Description is empty");
            validationPassed = false;
        }

        if(TextUtils.isEmpty(vCategory)){
            category.setError("Category is empty");
            validationPassed = false;
        }

        if(TextUtils.isEmpty(vAddressStreet)){
            streetAddress.setError("Address street is empty");
            validationPassed = false;
        }

        if(TextUtils.isEmpty(vAddressTitle)){
            streetTitle.setError("Address title is empty");
            validationPassed = false;
        }

        if(TextUtils.isEmpty(vElevation)){
            elevation.setError("Elevation is empty");
            validationPassed = false;
        }

        if(TextUtils.isEmpty(vLatitude)){
            latitude.setError("Latitude is empty");
            validationPassed = false;
        }

        if(TextUtils.isEmpty(vLongitude)){
            longitude.setError("Longitude is empty");
            validationPassed = false;
        }

        return validationPassed;
    }

    private void setViewData(){
        latitude.setText(place.getLatitude().toString());
        longitude.setText(place.getLongitude().toString());
    }

    private void getDataFromIntent(){
        place = (PlaceDescription)getIntent().getSerializableExtra(AppUtility.MODIFY_PLACE);
    }

    private void setListeners(){
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    private void initView(){
        name = findViewById(R.id.name);
        desc = findViewById(R.id.desc);
        elevation = findViewById(R.id.elevation);
        category = findViewById(R.id.category);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);
        streetTitle = findViewById(R.id.streetTitle);
        streetAddress = findViewById(R.id.streetAddress);
        setListeners();
    }

}
