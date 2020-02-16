package edu.asu.msse.rsingh92.assignment1.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.asu.msse.rsingh92.assignment1.RPC.AsyncCollectionConnect;
import edu.asu.msse.rsingh92.assignment1.RPC.RPCMethodMetadata;
import edu.asu.msse.rsingh92.assignment1.callbacks.ListClickListener;
import edu.asu.msse.rsingh92.assignment1.adapters.PlaceAdapter;
import edu.asu.msse.rsingh92.assignment1.callbacks.RPCCallback;
import edu.asu.msse.rsingh92.assignment1.models.PlaceDescription;
import edu.asu.msse.rsingh92.assignment1.utilities.AppUtility;
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

public class PlaceListActivity extends AppCompatActivity implements ListClickListener, RPCCallback {

    private RecyclerView placeRecyclerView;
    private LinearLayoutManager llm;
    private PlaceAdapter adapter;
    private List<PlaceDescription> allPlaces;

    /***********************************************************************************************
     *                                  Lifecycle methods
     ***********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placelibrary);

        loadListFromRPC();

        // initiate request to server to get the names of all students to be placed in the spinner

//        AppUtility.loadAllPlacesInMemory(this);
        placeRecyclerView = findViewById(R.id.placeRV);
        llm = new LinearLayoutManager(this);
        allPlaces = new ArrayList<PlaceDescription>();//PlaceLibrary.getAllPlacesFronJson(this);
        adapter = new PlaceAdapter(this, allPlaces);
        placeRecyclerView.setLayoutManager(llm);
        placeRecyclerView.setAdapter(adapter);
    }


    @Override
    public void onActivityResult(int req, int res, Intent intent){
        if(req==8090){
            if(res== Activity.RESULT_OK){
                Log.d("Refresh","refresh");
                allPlaces = AppUtility.getAllPlacesFromMemory();
                adapter = new PlaceAdapter(this,allPlaces);
                placeRecyclerView.setAdapter(adapter);
            }
        }

    }

    /***********************************************************************************************
     *                                  Callback methods
     ***********************************************************************************************/
    @Override
    public void itemClicked(int index) {
        openDetailActivity(index);
    }


    /***********************************************************************************************
     *                                  Menu Related methods
     ***********************************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.place_list_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {

            case R.id.add:
                openAddPlaceActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    /***********************************************************************************************
     *                                  Private methods
     ***********************************************************************************************/
    private void openAddPlaceActivity(){
        Intent intent = new Intent(this, AddPlaceActivity.class);
        startActivityForResult(intent, 8090);
    }

    private void openDetailActivity(int index){
        Intent intent = new Intent(this, PlaceDetailActivity.class);
        intent.putExtra("Place", allPlaces.get(index));
        intent.putExtra("OTHER_PLACES", (ArrayList<PlaceDescription>)allPlaces);
        intent.putExtra("INDEX",index);
        startActivityForResult(intent,8090);
    }


    private void loadListFromRPC(){

        try{
            RPCMethodMetadata mi = new RPCMethodMetadata(this, getString(R.string.defaulturl),"getNames",
                    new Object[]{});
            AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "Exception creating adapter: " +
                    ex.getMessage());
        }

    }

    @Override
    public void resultLoaded(Object object) {

        allPlaces = (List<PlaceDescription>)object;
        adapter = new PlaceAdapter(this, allPlaces);
        placeRecyclerView.setAdapter(adapter);

    }
}
