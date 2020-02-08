package edu.asu.msse.rsingh92.assignment1;

import androidx.appcompat.app.AppCompatActivity;

public class AppUtility {

    public static String MODIFY_PLACE="edu.asu.msse.rsingh92.assignment1.AppUtility.MODIFY_PLACE";
    public static String CURRENT_PLACE="edu.asu.msse.rsingh92.assignment1.AppUtility.CURRENT_PLACE";

    public static void openConfirmationDialog(AppCompatActivity activity, String msg){
        ConfirmationDialog confirmationDialog=new ConfirmationDialog(msg);
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

        double longDiff= Math.toRadians(lon1-lon2);
        double y= Math.sin(longDiff)*Math.cos(lat2);
        double x=Math.cos(lat1)*Math.sin(lat2)-Math.sin(lat1)*Math.cos(lat2)*Math.cos(longDiff);

        return (Math.toDegrees(Math.atan2(y, x))+360)%360;
    }

    public static String getKmString(Double distance){
        String value = String.format("%.2f",distance);
        return value = value+" KM";
    }

}
