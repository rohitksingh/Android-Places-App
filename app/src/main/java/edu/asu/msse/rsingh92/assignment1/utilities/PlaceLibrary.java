package edu.asu.msse.rsingh92.assignment1.utilities;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

public class PlaceLibrary {

    public static PlaceDescription getPlaceHolderFromJsonObject(JSONObject obj){

        PlaceDescription place = new PlaceDescription();

        try {

            place.setName(obj.getString("name"));
            place.setDescription(obj.getString("description"));
            place.setCategory(obj.getString("category"));
            place.setAddressTitle(obj.getString("address-title"));
            place.setAddressStreet(obj.getString("address-street"));
            place.setElevation(obj.getString("elevation"));
            place.setLatitude(obj.getDouble("latitude"));
            place.setLongitude(obj.getDouble("longitude"));

        }catch (JSONException e){

        }

        return place;
    }

    private static String getJsonString(Context context){
        String json = null;
        try {
            InputStream is = context.getAssets().open("location.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}