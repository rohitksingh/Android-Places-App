package edu.asu.msse.rsingh92.assignment1.utilities;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.asu.msse.rsingh92.assignment1.database.PlaceDataBase;
import edu.asu.msse.rsingh92.assignment1.models.PlaceDescription;

public class DBUtility {

    private static PlaceDataBase placeDataBase;
    private static SQLiteDatabase sqLiteDatabase;
    private static Context context;

    public static void initDatabase(Context _context){
        placeDataBase = new PlaceDataBase(_context);
        context = _context;
        try {
            sqLiteDatabase = placeDataBase.openDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<PlaceDescription> getAllPlacesFromDB(){

        List<PlaceDescription> allPlaces = new ArrayList<>();

        Cursor c= sqLiteDatabase.rawQuery("SELECT * FROM place", null);
        c.moveToFirst();

        int count = c.getCount();

        for(int i=0;i<count;i++){

            PlaceDescription place = new PlaceDescription();

            place.setName(c.getString(c.getColumnIndex("name")));
            place.setDescription(c.getString(c.getColumnIndex("description")));
            place.setCategory(c.getString(c.getColumnIndex("category")));
            place.setAddressStreet(c.getString(c.getColumnIndex("address_street")));
            place.setAddressTitle(c.getString(c.getColumnIndex("address_title")));
            place.setLongitude(Double.parseDouble(c.getString(c.getColumnIndex("longitude"))));
            place.setLatitude(Double.parseDouble(c.getString(c.getColumnIndex("latitude"))));
            place.setElevation(c.getString(c.getColumnIndex("elevation")));

            Log.d("AAA", "getAllPlacesFromDB: "+place.toString());

            allPlaces.add(place);

            c.moveToNext();
        }

        return allPlaces;

    }

}
