package edu.asu.msse.rsingh92.assignment1.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import edu.asu.msse.rsingh92.assignment1.callbacks.ConfirmationDialogCallback;
import edu.asu.msse.rsingh92.assignment1.callbacks.RPCCallback;
import edu.asu.msse.rsingh92.assignment1.utilities.AppUtility;
import edu.asu.msse.rsingh92.assignment1.models.PlaceDescription;
import edu.asu.msse.rsingh92.assignment1.R;

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
 * @version February 2016
 */
public class AddPlaceActivity extends AppCompatActivity implements ConfirmationDialogCallback, RPCCallback {

    private EditText name, description, category, addressTitle, addressStreet, elevation, latitude, longitude;
    private PlaceDescription currentPlace;
    private int INDEX;

    /***********************************************************************************************
     *                                  Lifecycle methods
     ***********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        initViews();
        getDataFromPreviousActivity();
    }


    /***********************************************************************************************
     *                                  Menu Related methods
     ***********************************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_place_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {

            case R.id.save:
                AppUtility.openConfirmationDialog(this, "Do you want to save this place");
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }


    /***********************************************************************************************
     *                                   Private methods
     ***********************************************************************************************/
    private void getDataFromPreviousActivity(){
        Intent intent = getIntent();
        if(intent.getAction()!= null && intent.getAction().equals(AppUtility.MODIFY_PLACE)){
            disableNameField();
            currentPlace = (PlaceDescription) intent.getSerializableExtra(AppUtility.CURRENT_PLACE);
            INDEX = intent.getIntExtra(AppUtility.INDEX,0);
            setData();
        }
    }

    private void setData(){
        name.setText(currentPlace.getName());
        description.setText(currentPlace.getDescription());
        category.setText(currentPlace.getCategory());
        addressTitle.setText(currentPlace.getAddressTitle());
        addressStreet.setText(currentPlace.getAddressStreet());
        elevation.setText(currentPlace.getElevation());
        latitude.setText(currentPlace.getLatitude().toString());
        longitude.setText(currentPlace.getLongitude().toString());
    }

    private void initViews(){
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        category = findViewById(R.id.category);
        addressTitle = findViewById(R.id.addressTitle);
        addressStreet = findViewById(R.id.addressStreet);
        elevation = findViewById(R.id.elevation);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
    }

    private void savePlace(){

        List<PlaceDescription> allPlace = AppUtility.getAllPlacesFromMemory();

        if(getIntent().getAction()!=null && getIntent().getAction().equals(AppUtility.MODIFY_PLACE)){
            allPlace.set(INDEX, getPlaceFromView());
        }else {
            allPlace.add(allPlace.size(), getPlaceFromView());
        }

        setResult(Activity.RESULT_OK);
        finish();
    }


    private void savePlaceOnServer(){
        AppUtility.addPlaceOnServer(this, getPlaceFromView());
    }


    private PlaceDescription getPlaceFromView(){

        PlaceDescription newPlace = new PlaceDescription();
        newPlace.setName(name.getText().toString());
        newPlace.setDescription(description.getText().toString());
        newPlace.setAddressStreet(addressStreet.getText().toString());
        newPlace.setAddressTitle(addressTitle.getText().toString());
        newPlace.setCategory(category.getText().toString());
        newPlace.setElevation(elevation.getText().toString());
        newPlace.setLatitude(Double.valueOf(latitude.getText().toString()));
        newPlace.setLongitude(Double.valueOf(longitude.getText().toString()));

        return newPlace;
    }

    private void disableNameField(){
        name.setEnabled(false);
    }

    @Override
    public void okButtonClicked() {
        savePlaceOnServer();
        savePlace();
    }

    @Override
    public void cancelButtonClicked() {

    }

    @Override
    public void resultLoaded(Object object) {

    }
}