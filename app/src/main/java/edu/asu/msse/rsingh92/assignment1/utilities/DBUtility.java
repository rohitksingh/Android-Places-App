package edu.asu.msse.rsingh92.assignment1.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.asu.msse.rsingh92.assignment1.database.PlaceDataBase;
import edu.asu.msse.rsingh92.assignment1.models.PlaceDescription;

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
public class DBUtility {

    private static PlaceDataBase placeDataBase;
    private static SQLiteDatabase sqLiteDatabase;
    private static Context context;

    public static void initDatabase(Context _context){

        Log.d("AAA", "inside cons");

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

        String select_all_query = "SELECT * FROM place";
        Cursor c= sqLiteDatabase.rawQuery(select_all_query, null);
        c.moveToFirst();

        int count = c.getCount();

        for(int i=0;i<count;i++){

            PlaceDescription place = new PlaceDescription();

            place.setName(c.getString(c.getColumnIndex("name")));
            place.setDescription(c.getString(c.getColumnIndex("description")));
            place.setCategory(c.getString(c.getColumnIndex("category")));
            place.setAddressStreet(c.getString(c.getColumnIndex("address_street")));
            place.setAddressTitle(c.getString(c.getColumnIndex("address_title")));
            place.setLongitude((c.getDouble(c.getColumnIndex("longitude"))));
            place.setLatitude((c.getDouble(c.getColumnIndex("latitude"))));
            place.setElevation(c.getString(c.getColumnIndex("elevation")));

            allPlaces.add(place);
            c.moveToNext();
        }

        return allPlaces;

    }

    public static void addAllPlacesToDatabase(List<PlaceDescription> places){
        for(PlaceDescription place:places){
            addPlaceToDatabase(place);
        }
    }

    public static void addPlaceToDatabase(PlaceDescription place){

        String name = place.getName();
        String description = place.getDescription();
        String category = place.getCategory();
        String addresstitle = place.getAddressTitle();
        String addressstreet = place.getAddressStreet();
        Double elevation = Double.parseDouble(place.getElevation());
        Double latitude = place.getLatitude();
        Double longitude = place.getLongitude();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("description",description);
        contentValues.put("category",category);
        contentValues.put("address_title",addresstitle);
        contentValues.put("address_street",addressstreet);
        contentValues.put("elevation",elevation);
        contentValues.put("latitude",latitude);
        contentValues.put("longitude",longitude);

        long rownum = sqLiteDatabase.insert("place",null,contentValues);
    }


    public static void updatePlaceToDatabase(PlaceDescription placeDescription){

        String name = placeDescription.getName();
        String description = placeDescription.getDescription();
        String category = placeDescription.getCategory();
        String addresstitle = placeDescription.getAddressTitle();
        String addressstreet = placeDescription.getAddressStreet();
        Double elevation = Double.parseDouble(placeDescription.getElevation());
        Double latitude = placeDescription.getLatitude();
        Double longitude = placeDescription.getLongitude();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("description",description);
        contentValues.put("category",category);
        contentValues.put("address_title",addresstitle);
        contentValues.put("address_street",addressstreet);
        contentValues.put("elevation",elevation);
        contentValues.put("latitude",latitude);
        contentValues.put("longitude",longitude);

        sqLiteDatabase.update("place", contentValues, "name = ?", new String[]{name});

    }

    public static int deleteAllPlacesOnDatabase(){
        return sqLiteDatabase.delete("place", "1", null);
    }

    public static void deletePlaceOnDataBase(String placeName){
        String table = "place";
        String whereClause = "name=?";
        String[] whereArgs = new String[] { String.valueOf(placeName) };
        sqLiteDatabase.delete(table, whereClause, whereArgs);
    }

    /**
     *   Methods which deal with places that could not be pushed to Server
     */

}
