package edu.asu.msse.rsingh92.assignment1;

import org.json.JSONException;
import org.json.JSONObject;

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

public class PlaceDescription {

    private String name, description, category, addressTitle, addressStreet, elevation, latitude, longitude;

    public PlaceDescription(String jsonString){

        try {

            JSONObject obj = new JSONObject(jsonString);
            this.name = obj.getString("name");
            this.description = obj.getString("description");
            this.category = obj.getString("category");
            this.addressTitle = obj.getString("address-title");
            this.addressStreet = obj.getString("address-street");
            this.elevation = obj.getString("elevation");
            this.latitude = obj.getString("latitude");
            this.longitude = obj.getString("longitude");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString(){
        return name+" "+description+" "+category+" "+addressTitle+" "+addressStreet+" "+elevation+" "+latitude+" "+longitude;
    }

}
