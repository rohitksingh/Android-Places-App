package edu.asu.msse.rsingh92.assignment1.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.asu.msse.rsingh92.assignment1.RPC.JsonRPCRequestViaHttp;
import edu.asu.msse.rsingh92.assignment1.RPC.RPCMethodMetadata;
import edu.asu.msse.rsingh92.assignment1.callbacks.RPCCallback;
import edu.asu.msse.rsingh92.assignment1.models.PlaceDescription;
import edu.asu.msse.rsingh92.assignment1.utilities.AppUtility;

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
public class FetchPlaceAsyncTask extends AsyncTask<RPCMethodMetadata, Integer, RPCMethodMetadata> {

    private static List<PlaceDescription> allPlaces;
    private static int list_size;
    private Context context;

    public FetchPlaceAsyncTask(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute(){
        allPlaces = AppUtility.getAllPlacesFromMemory();
    }

    @Override
    protected RPCMethodMetadata doInBackground(RPCMethodMetadata... aRequest){

        try {
            JSONArray ja = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                ja = new JSONArray(aRequest[0].params);
            }

            String requestData = "{ \"jsonrpc\":\"2.0\", \"method\":\""+aRequest[0].method+"\", \"params\":"+ja.toString()+
                    ",\"id\":3}";

            JsonRPCRequestViaHttp conn = new JsonRPCRequestViaHttp((new URL(aRequest[0].urlString)));
            aRequest[0].resultAsJson = conn.call(requestData);

        }catch (Exception ex){
            Log.d(this.getClass().getSimpleName(),"Exception in RPC"+
                    ex.getMessage());
        }
        return aRequest[0];
    }

    @Override
    protected void onPostExecute(RPCMethodMetadata res){

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

                for(int i=0;i<names.length;i++){


                    AppUtility.getItem(context, names[i]);
//                    RPCMethodMetadata mi = new RPCMethodMetadata(res.callback, res.urlString, "get", new String[]{names[i]});
//                    AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
                }

            } else if (res.method.equals("get")) {


                JSONObject jo = new JSONObject(res.resultAsJson);

                PlaceDescription place = AppUtility.getPlaceHolderFromJsonObject(jo.getJSONObject("result"));

                AppUtility.getAllPlacesFromMemory().add(place);

                if(allPlaces.size()==list_size){
                    res.callback.resultLoaded(allPlaces);
                }


            }
        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"Exception: "+ex.getMessage());
        }
    }

}