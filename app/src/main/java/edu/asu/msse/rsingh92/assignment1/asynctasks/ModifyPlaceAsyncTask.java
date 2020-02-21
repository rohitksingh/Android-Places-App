package edu.asu.msse.rsingh92.assignment1.asynctasks;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;

import edu.asu.msse.rsingh92.assignment1.rpc.JsonRPCRequestViaHttp;
import edu.asu.msse.rsingh92.assignment1.rpc.RPCMethodMetadata;

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
public class ModifyPlaceAsyncTask extends AsyncTask<RPCMethodMetadata, Integer, RPCMethodMetadata> {

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

        JsonRPCRequestViaHttp conn = null;
        try {
            conn = new JsonRPCRequestViaHttp((new URL(methodInformations[0].urlString)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            methodInformations[0].resultAsJson = conn.call(requestData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
