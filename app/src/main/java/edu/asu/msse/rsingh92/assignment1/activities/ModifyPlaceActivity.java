package edu.asu.msse.rsingh92.assignment1.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import edu.asu.msse.rsingh92.assignment1.callbacks.ConfirmationDialogCallback;
import edu.asu.msse.rsingh92.assignment1.callbacks.RPCCallback;
import edu.asu.msse.rsingh92.assignment1.callbacks.RPCErrorCallback;
import edu.asu.msse.rsingh92.assignment1.callbacks.RPCSyncCallback;
import edu.asu.msse.rsingh92.assignment1.utilities.AppUtility;
import edu.asu.msse.rsingh92.assignment1.models.PlaceDescription;
import edu.asu.msse.rsingh92.assignment1.R;
import edu.asu.msse.rsingh92.assignment1.utilities.DBUtility;
import edu.asu.msse.rsingh92.assignment1.utilities.TempDBUtility;

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
public class ModifyPlaceActivity extends AppCompatActivity implements ConfirmationDialogCallback, RPCSyncCallback {

    private EditText name, description, category, addressTitle, addressStreet, elevation, latitude, longitude;
    private PlaceDescription currentPlace;
    private int INDEX;
    private static final int ADD_NEW_PLACE = 9087;
    private static final int EDIT_PLACE = 9833;
    private int MODIFY_MODE=ADD_NEW_PLACE;

    /***********************************************************************************************
     *                                  Lifecycle methods
     ***********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        getModifyMode();
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

                if(ifValadationPassed()){
                    if(MODIFY_MODE==EDIT_PLACE){
                        AppUtility.openConfirmationDialog(this, "Do you want to modify this place");
                    }else{
                        AppUtility.openConfirmationDialog(this, "Do you want to save this place");
                    }
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }


    /***********************************************************************************************
     *                                   Private methods
     ***********************************************************************************************/
    private void getDataFromPreviousActivity(){

        if(MODIFY_MODE==EDIT_PLACE){
            disableNameField();
            Intent intent = getIntent();
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
        setAppBarTitle();
    }

    private void savePlace(){

        List<PlaceDescription> allPlace = AppUtility.getAllPlacesFromMemory();

        if(MODIFY_MODE==EDIT_PLACE){
            allPlace.set(INDEX, getPlaceFromView());
        }else {
            allPlace.add(allPlace.size(), getPlaceFromView());
        }

        setResult(Activity.RESULT_OK);
        finish();
    }


    private void savePlaceOnDataBase(){

        if(MODIFY_MODE==EDIT_PLACE){
           DBUtility.updatePlaceToDatabase(getPlaceFromView());
        }else {
            DBUtility.addPlaceToDatabase(getPlaceFromView());
        }

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
        name.setClickable(true);
        name.setCursorVisible(false);
        name.setFocusable(false);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ModifyPlaceActivity.this, "Name field is non editable.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getModifyMode(){
        if(getIntent().getAction()!= null && getIntent().getAction().equals(AppUtility.MODIFY_PLACE)){
            MODIFY_MODE = EDIT_PLACE;
        }
    }

    private void setAppBarTitle(){

        if(MODIFY_MODE==EDIT_PLACE){
            getSupportActionBar().setTitle("Edit Place");
        }
    }

    @Override
    public void okButtonClicked() {
        savePlaceOnServer();
        savePlaceOnDataBase();
        savePlace();
    }

    @Override
    public void cancelButtonClicked() {

    }

    @Override
    public void resultLoaded(Object object) {

    }


    private boolean ifValadationPassed(){

        String vName = name.getText().toString().trim();
        String vDesc = description.getText().toString().trim();
        String vAddressStreet = addressStreet.getText().toString().trim();
        String vAddressTitle = addressTitle.getText().toString().trim();
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
            description.setError("Description is empty");
            validationPassed = false;
        }

        if(TextUtils.isEmpty(vCategory)){
            category.setError("Category is empty");
            validationPassed = false;
        }

        if(TextUtils.isEmpty(vAddressStreet)){
            addressStreet.setError("Address street is empty");
            validationPassed = false;
        }

        if(TextUtils.isEmpty(vAddressTitle)){
            addressTitle.setError("Address title is empty");
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

    @Override
    public void onFail(String methodname) {
        Toast.makeText(this, "Failed to Update on server", Toast.LENGTH_SHORT).show();
        TempDBUtility.saveData(getPlaceFromView().getName());
    }

    @Override
    public void onSuccess(Object object) {

    }
}