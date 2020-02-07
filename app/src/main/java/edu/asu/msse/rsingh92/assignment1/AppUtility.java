package edu.asu.msse.rsingh92.assignment1;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AppUtility {

    public static List<PlaceDescription> getAllPlacesFronJson(Context context){

        List<PlaceDescription> places = new ArrayList<PlaceDescription>();

        try {

            JSONObject jsonObject = new JSONObject(getJsonString(context));
            PlaceDescription place1= getPlaceHolderFromJsonObject(jsonObject.getJSONObject("ASU-West"));
            PlaceDescription place2= getPlaceHolderFromJsonObject(jsonObject.getJSONObject("UAK-Anchorage"));
            PlaceDescription place3= getPlaceHolderFromJsonObject(jsonObject.getJSONObject("Toreros"));
            PlaceDescription place4= getPlaceHolderFromJsonObject(jsonObject.getJSONObject("Barrow-Alaska"));
            PlaceDescription place5= getPlaceHolderFromJsonObject(jsonObject.getJSONObject("Calgary-Alberta"));
            PlaceDescription place6= getPlaceHolderFromJsonObject(jsonObject.getJSONObject("London-England"));
            PlaceDescription place7= getPlaceHolderFromJsonObject(jsonObject.getJSONObject("Moscow-Russia"));
            PlaceDescription place8= getPlaceHolderFromJsonObject(jsonObject.getJSONObject("New-York-NY"));
            PlaceDescription place9= getPlaceHolderFromJsonObject(jsonObject.getJSONObject("Notre-Dame-Paris"));
            PlaceDescription place10= getPlaceHolderFromJsonObject(jsonObject.getJSONObject("Reavis-Ranch"));
            PlaceDescription place11= getPlaceHolderFromJsonObject(jsonObject.getJSONObject("Rogers-Trailhead"));
            PlaceDescription place12= getPlaceHolderFromJsonObject(jsonObject.getJSONObject("Reavis-Grave"));
            PlaceDescription place13= getPlaceHolderFromJsonObject(jsonObject.getJSONObject("Muir-Woods"));
            PlaceDescription place14= getPlaceHolderFromJsonObject(jsonObject.getJSONObject("Windcave-Peak"));

            places.add(place1);
            places.add(place2);
            places.add(place3);
            places.add(place4);
            places.add(place5);
            places.add(place6);
            places.add(place7);
            places.add(place8);
            places.add(place9);
            places.add(place10);
            places.add(place11);
            places.add(place12);
            places.add(place13);
            places.add(place14);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSON", "exception");
        }

        return places;

    }

    private static PlaceDescription getPlaceHolderFromJsonObject(JSONObject obj){

        PlaceDescription place = new PlaceDescription();

        try {

            place.setName(obj.getString("name"));
            place.setDescription(obj.getString("description"));
            place.setCategory(obj.getString("category"));
            place.setAddressTitle(obj.getString("address-title"));
            place.setAddressStreet(obj.getString("address-street"));
            place.setElevation(obj.getString("elevation"));
            place.setLatitude(obj.getString("latitude"));
            place.setLongitude(obj.getString("longitude"));

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
