package edu.asu.msse.rsingh92.assignment1.RPC;

import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.asu.msse.rsingh92.assignment1.callbacks.RPCCallback;
import edu.asu.msse.rsingh92.assignment1.models.PlaceDescription;
import edu.asu.msse.rsingh92.assignment1.utilities.AppUtility;
import edu.asu.msse.rsingh92.assignment1.utilities.PlaceLibrary;

public class AsyncCollectionConnect extends AsyncTask<RPCMethodMetadata, Integer, RPCMethodMetadata> {

    private static List<PlaceDescription> allPlaces;
    private static int list_size;
    private static RPCCallback rpcCallback;

    @Override
    protected void onPreExecute(){
        allPlaces = AppUtility.getAllPlacesFromMemory();
    }

    @Override
    protected RPCMethodMetadata doInBackground(RPCMethodMetadata... aRequest){
        // array of methods to be called. Assume exactly one input, a single MethodInformation object
        android.util.Log.d(this.getClass().getSimpleName(),"in doInBackground on "+
                (Looper.myLooper() == Looper.getMainLooper()?"Main thread":"Async Thread"));
        try {
            JSONArray ja = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                ja = new JSONArray(aRequest[0].params);
            }
            android.util.Log.d(this.getClass().getSimpleName(),"params: "+ja.toString());
            String requestData = "{ \"jsonrpc\":\"2.0\", \"method\":\""+aRequest[0].method+"\", \"params\":"+ja.toString()+
                    ",\"id\":3}";
            android.util.Log.d("ENDPOINT","requestData: "+requestData+" url: "+aRequest[0].urlString);
            JsonRPCRequestViaHttp conn = new JsonRPCRequestViaHttp((new URL(aRequest[0].urlString)));
            aRequest[0].resultAsJson = conn.call(requestData);


        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"exception in remote call "+
                    ex.getMessage());
        }
        return aRequest[0];
    }

    @Override
    protected void onPostExecute(RPCMethodMetadata res){
        /*
         * Using AsyncTask is constraining in the following sense: Either you create a separate Class
         * extending AsyncTask for each call (or method being called), or you must determine which
         * (and possibly where) the method was called in onPostExecute. Thus, the if-then-else below.
         * In iOS we use a different approach by passing the block of code to be executed upon completion
         * of the asynchronous call. While Java has lambda's, they are constrained such that doing so is
         * not a viable option at this time.
         * Another approach would be to define an interface and have the calling class implement the
         * methods of the interface. This would abbreviate the code below while requiring the caller to
         * define what are commonly called delegate methods, or call-backs which could be called by
         * onPostExecute.
         * If you are thinking: Why didn't the designers of Android define the View Objects to be shared
         * (thread-safe) objects, thus allowing multiple threads to access them, each getting the "key"
         * before reading or making changes. The problem with this approach is that the view objects would
         * have to relinquish control to threads other than the UI Thread. This could cause the user to
         * experience non-responsiveness of the UI/App if misused by the App Developer.
         */
        android.util.Log.d(this.getClass().getSimpleName(), "in onPostExecute on " +
                (Looper.myLooper() == Looper.getMainLooper() ? "Main thread" : "Async Thread"));
        android.util.Log.d(this.getClass().getSimpleName(), " resulting is: " + res.resultAsJson);
        try {
            if (res.method.equals("getNames")) {
                JSONObject jo = new JSONObject(res.resultAsJson);
                JSONArray ja = jo.getJSONArray("result");
                ArrayList<String> al = new ArrayList<String>();
                for (int i = 0; i < ja.length(); i++) {
                    al.add(ja.getString(i));
                }
                String[] names = al.toArray(new String[0]);

                list_size = names.length;

                allPlaces = AppUtility.getAllPlacesFromMemory();

//                for (int i = 0; i < names.length; i++) {
//
//                    Log.d("WOAH", "onPostExecute: "+ names[i]);
//
//                    PlaceDescription place = new PlaceDescription();
//                    place.setName(names[i]);
//                    places.add(place);
//                }



//                res.callback.resultLoaded(places);

                for(int i=0;i<names.length;i++){

                    Log.d("WOAH", "onPostExecute: "+ names.length);

//                    Log.d("WOAH", "onPostExecute: "+names[i]);
//
                    RPCMethodMetadata mi = new RPCMethodMetadata(res.callback, res.urlString, "get", new String[]{names[i]});
                    AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
                }



//                res.parent.adapter.clear();
//                for (int i = 0; i < names.length; i++) {
//                    res.parent.adapter.add(names[i]);
//                }
//                res.parent.adapter.notifyDataSetChanged();
//                if (names.length > 0){
//                    try{
//                        // got the list of student names from the server, so now create a new async task
//                        // to get the student information about the first student and populate the UI with
//                        // that student's information.
//                        MethodInformation mi = new MethodInformation(res.parent, res.urlString, "get",
//                                new String[]{names[0]});
//                        AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
//                    } catch (Exception ex){
//                        android.util.Log.w(this.getClass().getSimpleName(),"Exception processing spinner selection: "+
//                                ex.getMessage());
//                    }
//                }
            } else if (res.method.equals("get")) {

                Log.d("WOAH", "inside");

                JSONObject jo = new JSONObject(res.resultAsJson);

                PlaceDescription place = PlaceLibrary.getPlaceHolderFromJsonObject(jo.getJSONObject("result"));
                Log.d("PLACENAME", place.toString());

                allPlaces.add(place);


                if(allPlaces.size()==list_size){
                    Log.d("SUCCESS", ""+(res.callback==null));
                    res.callback.resultLoaded(allPlaces);
                }





//               Student aStud = new Student(jo.getJSONObject("result"));
//                res.parent.studentidET.setText((new Integer(aStud.studentid)).toString());
//                res.parent.nameET.setText(aStud.name);
            } else if (res.method.equals("add")){
//                try{
//                    // finished adding a student. refresh the list of students by going back to the server for names
//                    MethodInformation mi = new MethodInformation(res.parent, res.urlString, "getNames", new Object[]{ });
//                    AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
//                } catch (Exception ex){
//                    android.util.Log.w(this.getClass().getSimpleName(),"Exception processing getNames: "+
//                            ex.getMessage());
//                }
            }
        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"Exception: "+ex.getMessage());
        }
    }

}