package edu.asu.msse.rsingh92.assignment1.utilities;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import edu.asu.msse.rsingh92.assignment1.callbacks.ConfirmationDialogCallback;
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
    private static List<PlaceDescription> allplaces = new ArrayList<>();

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

    public static void loadAllPlacesInMemory(Context context){
        allplaces = PlaceLibrary.getAllPlacesFronJson(context);
    }

    public static List<PlaceDescription> getAllPlacesFromMemory(){
        return allplaces;
    }


}
