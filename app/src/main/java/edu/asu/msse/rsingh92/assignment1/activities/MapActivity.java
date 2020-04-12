package edu.asu.msse.rsingh92.assignment1.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import edu.asu.msse.rsingh92.assignment1.R;
import edu.asu.msse.rsingh92.assignment1.callbacks.ConfirmationDialogCallback;
import edu.asu.msse.rsingh92.assignment1.callbacks.RPCCallback;
import edu.asu.msse.rsingh92.assignment1.callbacks.YesNoCallback;
import edu.asu.msse.rsingh92.assignment1.dialogs.AddPlaceDialog;
import edu.asu.msse.rsingh92.assignment1.models.PlaceDescription;
import edu.asu.msse.rsingh92.assignment1.utilities.AppUtility;
import edu.asu.msse.rsingh92.assignment1.utilities.DBUtility;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, YesNoCallback, RPCCallback {

    private GoogleMap mMap;
    private Marker marker;
    private MarkerOptions markerOptions;
    private CameraPosition cameraPosition;
    private static final String TAG = "MapsActivity";

    private PlaceDescription fromLocation;
    private PlaceDescription toLocation;

    private SupportMapFragment mapFragment;

    private AddPlaceDialog addPlaceDialog;

    private boolean ifNewPlaceAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getDataFromIntent();
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        markerOptions = new MarkerOptions();
        markerOptions.draggable(true);
        addMarker();

    }

    /***********************************************************************************************
     *
     *                          Private Helper method
     *
     ***********************************************************************************************/

    private void addMarker(){

        LatLng toLatLng = new LatLng(toLocation.getLatitude(), toLocation.getLongitude());
        markerOptions.position(toLatLng).title(toLocation.getName());
        marker = mMap.addMarker(markerOptions);

        LatLng fromLatLng = new LatLng(fromLocation.getLatitude(), fromLocation.getLongitude());
        markerOptions.position(fromLatLng).title(fromLocation.getName());
        marker = mMap.addMarker(markerOptions);

        drawLine(fromLatLng, toLatLng);

        cameraPosition = new CameraPosition.Builder()
                .target(getMiddleLatLng(fromLatLng, toLatLng))
                .zoom(getZoomLevel(AppUtility.getDistance(fromLocation, toLocation)))
                .build();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }


    private int getZoomLevel(Double distance){
        if(distance<100){
            return 10;
        }else{
            return 2;
        }
    }

    private LatLng getMiddleLatLng(LatLng fromLatLng, LatLng toLatLng){
        double midLat = (fromLatLng.latitude+toLatLng.latitude)/2;
        double midLng = (fromLatLng.longitude+toLatLng.longitude)/2;
        return new LatLng(midLat, midLng);
    }


    private void getDataFromIntent(){
        fromLocation = (PlaceDescription)getIntent().getSerializableExtra(AppUtility.FROM_LOCATION);
        toLocation = (PlaceDescription)getIntent().getSerializableExtra(AppUtility.TO_LOCATION);
    }

    private void drawLine(LatLng fromLatLng, LatLng toLatLng){

        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(fromLatLng, toLatLng)
                .width(5)
                .color(Color.RED));

    }

    @Override
    public void onMapClick(LatLng latLng) {


        PlaceDescription placeDescription = new PlaceDescription();
        placeDescription.setLatitude(latLng.latitude);
        placeDescription.setLongitude(latLng.longitude);
//
//        drawMarker(latLng);

//        drawMarkeronMap(latLng);

        addPlaceDialog = new AddPlaceDialog(this, placeDescription);
        addPlaceDialog.setCancelable(false);
        addPlaceDialog.show();

    }

    private void drawMarkeronMap(LatLng latLng){
        Log.d("DONE", latLng.toString());
        markerOptions.position(latLng).title("New Place");
        marker = mMap.addMarker(markerOptions);
        Toast.makeText(this, "Place Added", Toast.LENGTH_SHORT).show();
//        addPlaceDialog.dismiss();
    }

    @Override
    public void yesClicked(Object object) {
        PlaceDescription place = (PlaceDescription)object;
        LatLng latLng = new LatLng(place.getLatitude(), place.getLatitude());

        drawMarkeronMap(latLng);
        addPlace(place);

        addPlaceDialog.dismiss();

    }

    @Override
    public void noClicked(Object object) {
        addPlaceDialog.dismiss();
    }

    private void addPlace(PlaceDescription place){
        addPlaceOnList(place);
        addPlaceOnServer(place);
        addPlaceOnDataBase(place);
        ifNewPlaceAdded = true;
    }

    private void addPlaceOnList(PlaceDescription place){
        List<PlaceDescription>  places = AppUtility.getAllPlacesFromMemory();
        places.add(places.size(), place);
    }

    private void addPlaceOnDataBase(PlaceDescription place){
        AppUtility.addPlaceOnServer(this, place);
    }

    private void addPlaceOnServer(PlaceDescription place){
        DBUtility.addPlaceToDatabase(place);
    }

    @Override
    public void resultLoaded(Object object) {

    }

    @Override
    public void onBackPressed(){
        if(ifNewPlaceAdded){
            setResult(Activity.RESULT_OK);
        }
        super.onBackPressed();
    }
}
