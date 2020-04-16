package edu.asu.msse.rsingh92.assignment1.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

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
import edu.asu.msse.rsingh92.assignment1.callbacks.RPCSyncCallback;
import edu.asu.msse.rsingh92.assignment1.models.PlaceDescription;
import edu.asu.msse.rsingh92.assignment1.utilities.AppUtility;
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
 * @version April 2016
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, RPCSyncCallback {

    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    private Marker marker;
    private MarkerOptions markerOptions;
    private CameraPosition cameraPosition;


    private PlaceDescription fromLocation;
    private PlaceDescription toLocation;
    private PlaceDescription newAddedPlace;

    private boolean ifNewPlaceAdded = false;
    public static final int MODIFY_PLACE_REQ = 9099;

    /***********************************************************************************************
     *                                  Lifecycle methods
     ***********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initView();
        getDataFromIntent();
        setListeners();

    }

    /***********************************************************************************************
     *                                  Callback methods
     ***********************************************************************************************/

    @Override
    public void onActivityResult(int req, int res, Intent data){

        if(req == MODIFY_PLACE_REQ){
            if(res == Activity.RESULT_OK){

                PlaceDescription placeAdded = (PlaceDescription)data.getSerializableExtra(AppUtility.MODIFY_PLACE);
                newAddedPlace = placeAdded;
                LatLng latLng = new LatLng(placeAdded.getLatitude(), placeAdded.getLongitude());
                drawMarkeronMap(placeAdded);
                addPlace(placeAdded);

            }else {

            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        markerOptions = new MarkerOptions();
        markerOptions.draggable(true);
        addMarker();

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

    @Override
    public void onMapLongClick(LatLng latLng) {

        PlaceDescription placeDescription = new PlaceDescription();
        placeDescription.setLatitude(latLng.latitude);
        placeDescription.setLongitude(latLng.longitude);

        Intent fillPlaceInfoIntent = new Intent(this, CreatePlaceOnTouchActivity.class);
        fillPlaceInfoIntent.putExtra(AppUtility.MODIFY_PLACE, placeDescription);
        startActivityForResult(fillPlaceInfoIntent, MODIFY_PLACE_REQ);
    }

    @Override
    public void onSuccess(Object object) {

    }

    @Override
    public void onFail(String methodname) {
        Toast.makeText(this, "Failed to Update on server", Toast.LENGTH_SHORT).show();
        TempDBUtility.saveData(newAddedPlace.getName());
    }

    /***********************************************************************************************
     *                                   Private methods
     ***********************************************************************************************/

    private void addPlace(PlaceDescription place){
        addPlaceOnMemory(place);
        addPlaceOnServer(place);
        addPlaceOnDataBase(place);
        ifNewPlaceAdded = true;
    }

    private void addPlaceOnMemory(PlaceDescription place){
        List<PlaceDescription>  places = AppUtility.getAllPlacesFromMemory();
        places.add(places.size(), place);
    }

    private void addPlaceOnDataBase(PlaceDescription place){
        AppUtility.addPlaceOnServer(this, place);
    }

    private void addPlaceOnServer(PlaceDescription place){
        DBUtility.addPlaceToDatabase(place);
    }

    private void drawMarkeronMap(PlaceDescription placeAdded){
        LatLng latLng = new LatLng(placeAdded.getLatitude(), placeAdded.getLongitude());
        markerOptions.position(latLng).title(placeAdded.getName());
        marker = mMap.addMarker(markerOptions);
        Toast.makeText(this, "Place Added", Toast.LENGTH_SHORT).show();
    }

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
                .zoom((float) getFactor(fromLocation, toLocation))
                .build();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }


    public double getWidthInDP(){

        double ret = 0.0;
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        double dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        ret = dpWidth;
        return ret;

    }

    public double distanceRhumbTo(PlaceDescription wp, PlaceDescription to){
        double ret = 0.0;
        double dsi, q;
        double dlatRad = Math.toRadians(wp.getLatitude() - to.getLatitude());
        double dlonRad = Math.toRadians(wp.getLongitude() - to.getLongitude());
        double latOrgRad = Math.toRadians(to.getLatitude());
        double lonOrgRad = Math.toRadians(to.getLongitude());
        dsi = Math.log(Math.tan(Math.PI/4.0 + Math.toRadians(wp.getLatitude())/2.0)/
                Math.tan(Math.PI/4.0+latOrgRad/2.0));
        q = Math.abs(dlatRad) > 10e-12 ? dlatRad/dsi :
                Math.cos(latOrgRad);
        ret = Math.sqrt(dlatRad*dlatRad + q * q * dlonRad * dlonRad) * 6371.0;

        return ret = ret * 0.62137119;

    }

    public double getFactor(PlaceDescription from, PlaceDescription to){

        double increment = 0.25;
        double ret = -0.25;
        double dist = distanceRhumbTo(to, from);

        double latCntr = (from.getLatitude() + to.getLatitude())/2.0;
        double circumferenceAtLat = 24901.0 * Math.cos(Math.toRadians(latCntr));

        double dpWide = getWidthInDP();
        double twoToPwrRet = 1.0;
        double dpForDist = 0.0;
        do {
            ret = ret + increment;
            twoToPwrRet = Math.pow(2.0, ret);
            double dpFor24000Miles = dpWide * twoToPwrRet;
            dpForDist = dpFor24000Miles / circumferenceAtLat * dist;
        } while (dpWide*0.9 > dpForDist);
        return ret - increment;
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

    private void setListeners(){
        mapFragment.getMapAsync(this);
    }

    private void initView(){
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    }

}
