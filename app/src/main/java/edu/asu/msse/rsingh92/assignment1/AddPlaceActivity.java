package edu.asu.msse.rsingh92.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddPlaceActivity extends AppCompatActivity{

    private EditText name, description, category, addressTitle, addressStreet, elevation, latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        category = findViewById(R.id.category);
        addressTitle = findViewById(R.id.addressTitle);
        addressStreet = findViewById(R.id.addressStreet);
        elevation = findViewById(R.id.elevation);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
    }

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


//    private void opendialog() {
//
//        ConfirmationDialog confirmationDialog=new ConfirmationDialog("Do you want to save this place");
//        confirmationDialog.show(getSupportFragmentManager(),"example dialog");
//
//    }


}
