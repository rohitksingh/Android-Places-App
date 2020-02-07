package edu.asu.msse.rsingh92.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PlaceListActivity extends AppCompatActivity implements ListClickListener{

    private RecyclerView placeRecyclerView;
    private LinearLayoutManager llm;
    private PlaceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placelibrary);
        placeRecyclerView = findViewById(R.id.placeRV);
        llm = new LinearLayoutManager(this);
        adapter = new PlaceAdapter(this, PlaceLibrary.getAllPlacesFronJson(this));
        placeRecyclerView.setLayoutManager(llm);
        placeRecyclerView.setAdapter(adapter);
    }

    @Override
    public void itemClicked(PlaceDescription placeDescription) {
        Intent intent = new Intent(this, PlaceDetailActivity.class);
        intent.putExtra("Place", placeDescription);
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
                Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

}
