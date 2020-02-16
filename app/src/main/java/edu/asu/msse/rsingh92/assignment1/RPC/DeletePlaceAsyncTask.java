package edu.asu.msse.rsingh92.assignment1.RPC;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;

public class DeletePlaceAsyncTask extends AsyncTask<RPCMethodMetadata, Integer, RPCMethodMetadata> {

    private static final String TAG = "DeletePlaceAsyncTask";

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected RPCMethodMetadata doInBackground(RPCMethodMetadata... methodInformations) {


        JSONArray ja = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            try {
                ja = new JSONArray(methodInformations[0].params);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        String requestData = "{ \"jsonrpc\":\"2.0\", \"method\":\""+methodInformations[0].method+"\", \"params\":"+ja.toString()+
                ",\"id\":3}";

        Log.d("DeletePlaceAsyncTask",requestData);

        JsonRPCRequestViaHttp conn = null;
        try {
            conn = new JsonRPCRequestViaHttp((new URL(methodInformations[0].urlString)));
        } catch (MalformedURLException e) {
            Log.d(TAG, "MalformedURLException: ");
            e.printStackTrace();
        }
        try {
            methodInformations[0].resultAsJson = conn.call(requestData);
            Log.d(TAG, "doInBackground: conn"+methodInformations[0].resultAsJson);
        } catch (Exception e) {
            Log.d(TAG, "doInBackground: Exception");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(RPCMethodMetadata methodInformation) {

    }
}
