package edu.asu.msse.rsingh92.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PlaceLibraryActivity extends AppCompatActivity implements ListClickListener{

    private RecyclerView placeRecyclerView;
    private LinearLayoutManager llm;
    private PlaceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placelibrary);
        placeRecyclerView = findViewById(R.id.placeRV);
        llm = new LinearLayoutManager(this);
        adapter = new PlaceAdapter(this, AppUtility.getAllPlacesFronJson(this));
        placeRecyclerView.setLayoutManager(llm);
        placeRecyclerView.setAdapter(adapter);
    }

    @Override
    public void itemClicked(PlaceDescription placeDescription) {
        Intent intent = new Intent(this, PlaceDetailActivity.class);
        intent.putExtra("Place", placeDescription);
        startActivity(intent);

    }
}
