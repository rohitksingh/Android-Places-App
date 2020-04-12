package edu.asu.msse.rsingh92.assignment1.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import edu.asu.msse.rsingh92.assignment1.R;
import edu.asu.msse.rsingh92.assignment1.models.PlaceDescription;

public class AddPlaceDialog extends Dialog {


    private EditText name, desc, category, latitude, longitude;
    private Button save, cancel;
    private PlaceDescription place;

    public AddPlaceDialog(Context context, PlaceDescription place) {
        super(context);
        this.place = place;
    }

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_place);
        name = findViewById(R.id.name);
        desc = findViewById(R.id.desc);
        category = findViewById(R.id.category);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);

        setValues();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public void setValues(){
        latitude.setText(place.getLatitude().toString());
        longitude.setText(place.getLongitude().toString());
    }

}
