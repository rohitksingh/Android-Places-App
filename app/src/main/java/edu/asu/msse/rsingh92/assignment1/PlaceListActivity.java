package edu.asu.msse.rsingh92.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
 * @version January 2016
 */

public class PlaceListActivity extends AppCompatActivity implements ListClickListener{

    private RecyclerView placeRecyclerView;
    private LinearLayoutManager llm;
    private PlaceAdapter adapter;
    private List<PlaceDescription> allPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placelibrary);
        placeRecyclerView = findViewById(R.id.placeRV);
        llm = new LinearLayoutManager(this);
        allPlaces = PlaceLibrary.getAllPlacesFronJson(this);
        adapter = new PlaceAdapter(this, allPlaces);
        placeRecyclerView.setLayoutManager(llm);
        placeRecyclerView.setAdapter(adapter);
    }

    @Override
    public void itemClicked(int index) {

        Intent intent = new Intent(this, PlaceDetailActivity.class);
        intent.putExtra("Place", allPlaces.get(index));
        intent.putExtra("OTHER_PLACES", (ArrayList<PlaceDescription>)allPlaces);
        startActivity(intent);
    }

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


    private void openAddPlaceActivity(){
        Intent intent = new Intent(this, AddPlaceActivity.class);
        startActivity(intent);
    }


}
