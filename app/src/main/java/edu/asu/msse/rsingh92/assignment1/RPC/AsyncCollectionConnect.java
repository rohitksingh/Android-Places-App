package edu.asu.msse.rsingh92.assignment1.RPC;

import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.asu.msse.rsingh92.assignment1.models.PlaceDescription;

public class AsyncCollectionConnect extends AsyncTask<MethodInformation, Integer, MethodInformation> {

    @Override
    protected void onPreExecute(){
        android.util.Log.d(this.getClass().getSimpleName(),"in onPreExecute on "+
                (Looper.myLooper() == Looper.getMainLooper()?"Main thread":"Async Thread"));
    }

    @Override
    protected MethodInformation doInBackground(MethodInformation... aRequest){
        // array of methods to be called. Assume exactly one input, a single MethodInformation object
        android.util.Log.d(this.getClass().getSimpleName(),"in doInBackground on "+
                (Looper.myLooper() == Looper.getMainLooper()?"Main thread":"Async Thread"));
        try {
            JSONArray ja = new JSONArray(aRequest[0].params);
            android.util.Log.d(this.getClass().getSimpleName(),"params: "+ja.toString());
            String requestData = "{ \"jsonrpc\":\"2.0\", \"method\":\""+aRequest[0].method+"\", \"params\":"+ja.toString()+
                    ",\"id\":3}";
            android.util.Log.d(this.getClass().getSimpleName(),"requestData: "+requestData+" url: "+aRequest[0].urlString);
            JsonRPCRequestViaHttp conn = new JsonRPCRequestViaHttp((new URL(aRequest[0].urlString)));
            aRequest[0].resultAsJson = conn.call(requestData);


        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"exception in remote call "+
                    ex.getMessage());
        }
        return aRequest[0];
    }

    @Override
    protected void onPostExecute(MethodInformation res){
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

                List<PlaceDescription> places = new ArrayList<>();

                for (int i = 0; i < names.length; i++) {
                    Log.d("SARVANSH",names[i]);

                    PlaceDescription place = new PlaceDescription();
                    place.setName(names[i]);
                    places.add(place);
                }


                res.callback.resultLoaded(places);



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
//                JSONObject jo = new JSONObject(res.resultAsJson);
//
//                Log.d("SARVANSH",jo.toString());
//
////                Student aStud = new Student(jo.getJSONObject("result"));
////                res.parent.studentidET.setText((new Integer(aStud.studentid)).toString());
////                res.parent.nameET.setText(aStud.name);
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