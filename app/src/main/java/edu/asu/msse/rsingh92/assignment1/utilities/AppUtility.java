package edu.asu.msse.rsingh92.assignment1.utilities;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import edu.asu.msse.rsingh92.assignment1.R;
import edu.asu.msse.rsingh92.assignment1.asynctasks.FetchPlaceAsyncTask;
import edu.asu.msse.rsingh92.assignment1.asynctasks.ModifyPlaceAsyncTask;
import edu.asu.msse.rsingh92.assignment1.rpc.RPCMethodMetadata;
import edu.asu.msse.rsingh92.assignment1.callbacks.ConfirmationDialogCallback;
import edu.asu.msse.rsingh92.assignment1.callbacks.RPCCallback;
import edu.asu.msse.rsingh92.assignment1.dialogs.ConfirmationDialog;
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

public class AppUtility {

    public static String MODIFY_PLACE="AppUtility.MODIFY_PLACE";
    public static String CURRENT_PLACE="AppUtility.CURRENT_PLACE";
    public static String INDEX = "AppUtility.INDEX";
    public static String FROM_LOCATION = "AppUtility.FROM_LOCATION";
    public static String TO_LOCATION = "AppUtility.TO_LOCATION";

    private static List<PlaceDescription> allplaces = new ArrayList<>();

    private static final String TAG = "AppUtility";

    public static void openConfirmationDialog(AppCompatActivity activity, String msg){
        ConfirmationDialog confirmationDialog=new ConfirmationDialog((ConfirmationDialogCallback)activity, msg);
        confirmationDialog.show(activity.getSupportFragmentManager(),"example dialog");
    }

    public static Double getDistance(PlaceDescription currentPlace, PlaceDescription place) {

        Double lat1 = 0.0;
        Double lat2 = 0.0;
        Double lon1 = 0.0;
        Double lon2 = 0.0;

        lat1 = currentPlace.getLatitude();
        lon1 = currentPlace.getLongitude();

        lat2 = place.getLatitude();
        lon2 = place.getLongitude();

        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0.0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515 * 1.609344;
            return (dist);
        }
    }

    public static Double getBearing(PlaceDescription currentPlace, PlaceDescription place) {

        Double lat1 = 0.0;
        Double lat2 = 0.0;
        Double lon1 = 0.0;
        Double lon2 = 0.0;

        lat1 = currentPlace.getLatitude();
        lat1 = Math.toRadians(lat1);

        lon1 = currentPlace.getLongitude();

        lat2 = place.getLatitude();
        lat2 = Math.toRadians(lat2);

        lon2 = place.getLongitude();

        double longDiff= Math.toRadians(lon2-lon1);
        double y= Math.sin(longDiff)*Math.cos(lat2);
        double x=Math.cos(lat1)*Math.sin(lat2)-Math.sin(lat1)*Math.cos(lat2)*Math.cos(longDiff);

        return (Math.toDegrees(Math.atan2(y, x))+360)%360;
    }

    public static String getKmString(Double distance){
        String value = String.format("%.2f",distance);
        return value+" KM";
    }

    public static String getDegreeString(Double distance){
        String value = String.format("%.2f",distance);
        return value+" Degree";
    }


    public static List<PlaceDescription> getAllPlacesFromMemory(){
        return allplaces;
    }

    public static void setAllPlacesOnMemory(List<PlaceDescription> _allplaces){
        allplaces = _allplaces;
    }


    public static void getAllPlacesFromServer(Context context){
        try{
            RPCMethodMetadata mi = new RPCMethodMetadata((RPCCallback)context, context.getString(R.string.defaulturl),"getNames",
                    new Object[]{});
            FetchPlaceAsyncTask ac = new FetchPlaceAsyncTask(context);
            ac.execute(mi);

            cancelTaskAfter(context, 5000, ac);

        } catch (Exception ex) {
            Log.d(TAG, "loadAllPlaces: ");
        }
    }

    public static void getPlaceFromServer(Context context, String placeName){
        RPCMethodMetadata mi = new RPCMethodMetadata((RPCCallback)context, context.getString(R.string.defaulturl), "get", new String[]{placeName});
        FetchPlaceAsyncTask ac = (FetchPlaceAsyncTask) new FetchPlaceAsyncTask(context).execute(mi);
    }

    public static void addPlaceOnServer(Context context, PlaceDescription placeDescription){

        JSONObject jsonObject = getJsonFromPlaceDesc(placeDescription);

        RPCMethodMetadata mi = new RPCMethodMetadata((RPCCallback) context, context.getString(R.string.defaulturl),"add",
                new Object[]{jsonObject});
        ModifyPlaceAsyncTask deletePlaceAsyncTask = new ModifyPlaceAsyncTask();
        deletePlaceAsyncTask.execute(mi);
    }

    public static void deletePlaceOnServer(Context context, String placeName){

        RPCMethodMetadata mi = new RPCMethodMetadata((RPCCallback) context, context.getString(R.string.defaulturl),"remove",
                new String[]{placeName});
        ModifyPlaceAsyncTask deletePlaceAsyncTask = new ModifyPlaceAsyncTask();
        deletePlaceAsyncTask.execute(mi);

    }


    public static JSONObject getJsonFromPlaceDesc(PlaceDescription placeDescription){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("address-title",placeDescription.getAddressTitle());
            jsonObject.put("address-street",placeDescription.getAddressStreet());
            jsonObject.put("elevation",Double.parseDouble(placeDescription.getElevation()));
            jsonObject.put("latitude",placeDescription.getLatitude());
            jsonObject.put("longitude",placeDescription.getLongitude());
            jsonObject.put("image","Image");
            jsonObject.put("name",placeDescription.getName());
            jsonObject.put("image",placeDescription.getName());
            jsonObject.put("description",placeDescription.getDescription());
            jsonObject.put("category",placeDescription.getCategory());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        return jsonObject;
        
    }

    public static PlaceDescription getPlaceDescFromJson(JSONObject obj){

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

    public static PlaceDescription getDummyPlace(){

        PlaceDescription placeDescription = new PlaceDescription();
        placeDescription.setName("Delhi");
        placeDescription.setDescription("Delhi is a place");
        placeDescription.setCategory("Capital");
        placeDescription.setAddressTitle("Delhi Title");
        placeDescription.setAddressStreet("Delhi Address street");
        placeDescription.setElevation(4500+"");
        placeDescription.setLatitude(273.33);
        placeDescription.setLongitude(273.11);
        return placeDescription;

    }

    public static void cancelTaskAfter(final Context context, final int milis, final AsyncTask asyncTask){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(milis);

                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Server is offline", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

}