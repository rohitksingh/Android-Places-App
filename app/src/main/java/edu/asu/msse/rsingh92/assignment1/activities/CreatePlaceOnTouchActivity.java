package edu.asu.msse.rsingh92.assignment1.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import edu.asu.msse.rsingh92.assignment1.R;
import edu.asu.msse.rsingh92.assignment1.callbacks.YesNoCallback;
import edu.asu.msse.rsingh92.assignment1.models.PlaceDescription;
import edu.asu.msse.rsingh92.assignment1.utilities.AppUtility;

public class CreatePlaceOnTouchActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText name, desc, category, elevation, latitude, longitude, streetTitle, streetAddress;
    private Button save, cancel;
    private PlaceDescription place;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_place);
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

        place = (PlaceDescription)getIntent().getSerializableExtra(AppUtility.MODIFY_PLACE);

        setValues();
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);


    }

    public void setValues(){
        latitude.setText(place.getLatitude().toString());
        longitude.setText(place.getLongitude().toString());
    }

    private PlaceDescription getPlaceFromView(){
        place.setName(name.getText().toString());
        place.setDescription(desc.getText().toString());
        place.setCategory(category.getText().toString());
        place.setElevation(elevation.getText().toString());
        place.setAddressStreet(streetAddress.getText().toString());
        place.setAddressTitle(streetTitle.getText().toString());
        return place;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.save:
                sendPlaceData();
                break;

            case R.id.cancel:
                dontSendPlaceData();
                break;

            default:
                break;

        }
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
}
