package edu.asu.msse.rsingh92.assignment1.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import edu.asu.msse.rsingh92.assignment1.callbacks.ListClickListener;
import edu.asu.msse.rsingh92.assignment1.adapters.PlaceAdapter;
import edu.asu.msse.rsingh92.assignment1.callbacks.RPCSyncCallback;
import edu.asu.msse.rsingh92.assignment1.models.PlaceDescription;
import edu.asu.msse.rsingh92.assignment1.utilities.AppUtility;
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

public class PlaceListActivity extends AppCompatActivity implements ListClickListener, RPCSyncCallback, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView placeRecyclerView;
    private LinearLayoutManager llm;
    private PlaceAdapter adapter;
    private List<PlaceDescription> allPlaces;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int unpushedDataSize=0;
    private int dataPushed=0;

    /***********************************************************************************************
     *                                  Lifecycle methods
     ***********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placelibrary);
        initView();
        setViewData();





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
     *                                  Menu Related methods
     ***********************************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.place_list_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.add:
                openAddPlaceActivity();
                return true;

            case R.id.menu_refresh:

                swipeRefreshLayout.setRefreshing(true);
                syncWithServer(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /***********************************************************************************************
     *                                  Callback methods
     ***********************************************************************************************/
    @Override
    public void itemClicked(int index) {
        openDetailActivity(index);
    }

    @Override
    public void onSuccess(Object object) {
        checkAndPullData();
    }

    @Override
    public void onFail(String methodname) {
        checkAndPullData();
    }

    @Override
    public void resultLoaded(Object object) {

        if(object!=null){

            Toast.makeText(this, "Synced", Toast.LENGTH_SHORT).show();

            allPlaces = AppUtility.getAllPlacesFromMemory();
            DBUtility.deleteAllPlacesOnDatabase();
            DBUtility.addAllPlacesToDatabase(allPlaces);

            adapter = new PlaceAdapter(this,allPlaces);
            placeRecyclerView.setAdapter(adapter);
            TempDBUtility.initBackUp();
        }else {
            Toast.makeText(PlaceListActivity.this, "Server is offline", Toast.LENGTH_SHORT).show();
        }

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        syncWithServer(this);
    }


    /***********************************************************************************************
     *                                  Private methods
     ***********************************************************************************************/
    private void openAddPlaceActivity(){
        Intent intent = new Intent(this, ModifyPlaceActivity.class);
        startActivityForResult(intent, 8090);
    }

    private void openDetailActivity(int index){
        Intent intent = new Intent(this, PlaceDetailActivity.class);
        intent.putExtra("Place", allPlaces.get(index));
        intent.putExtra("OTHER_PLACES", (ArrayList<PlaceDescription>)allPlaces);
        intent.putExtra("INDEX",index);
        startActivityForResult(intent,8090);
    }

    private void syncWithServer(Context context){
        dataPushed = 0;
        unpushedDataSize = TempDBUtility.get(TempDBUtility.BACKUP).size();
        if(unpushedDataSize!=0){
            TempDBUtility.pushDataToServer(this);
        }else {
            loadDataFromServer();
        }
    }

    private void loadDataFromServer(){
        AppUtility.getAllPlacesFromServer(this);
    }

    private void checkAndPullData(){
        dataPushed++;
        if(dataPushed==unpushedDataSize){
            loadDataFromServer();
        }
    }

    private void initView(){
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        placeRecyclerView = findViewById(R.id.placeRV);
    }

    private void setViewData(){
        setupSwipeTorefesh();
        setRV();
    }

    private void setRV(){
        llm = new LinearLayoutManager(this);
        allPlaces = AppUtility.getAllPlacesFromMemory();
        adapter = new PlaceAdapter(this, allPlaces);
        placeRecyclerView.setLayoutManager(llm);
        placeRecyclerView.setAdapter(adapter);
    }

    private void setupSwipeTorefesh(){

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );
    }

}
