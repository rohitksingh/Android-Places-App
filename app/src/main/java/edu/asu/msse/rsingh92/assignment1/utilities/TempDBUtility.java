package edu.asu.msse.rsingh92.assignment1.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.asu.msse.rsingh92.assignment1.models.PlaceDescription;

public class TempDBUtility {

    private static Context context;
    public static String BACKUP="BACKUP";

    public static void init(Context _context){
        context = _context;
        if(get(BACKUP)==null){
            initBackUp();
        }
    }

    public static void save(Set<String> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    public static Set<String> get(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<Set<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static void initBackUp(){
        save(new HashSet<String>(), BACKUP);
    }

    public static void saveData(String name){
        Set<String> data = get(BACKUP);
        data.add(name);
        save(data, BACKUP);
    }

    public static void pushDataToServer(Context context){

        Set<String> data = get(BACKUP);

        for(String name: data){
            PlaceDescription place = getPlace(name);
            if(place ==null){
                AppUtility.deletePlaceOnServer(context, name);
            }else {
                AppUtility.addPlaceOnServer(context, place);
            }
        }

    }

    public static PlaceDescription getPlace(String placeName){

        List<PlaceDescription> placeList = AppUtility.getAllPlacesFromMemory();
        for(PlaceDescription place: placeList){
            if(place.getName().equals(placeName))
                return place;
        }

        return null;
    }

}
