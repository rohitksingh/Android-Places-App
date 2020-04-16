package edu.asu.msse.rsingh92.assignment1.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import edu.asu.msse.rsingh92.assignment1.R;
import edu.asu.msse.rsingh92.assignment1.callbacks.RPCCallback;
import edu.asu.msse.rsingh92.assignment1.callbacks.RPCErrorCallback;
import edu.asu.msse.rsingh92.assignment1.callbacks.RPCSyncCallback;
import edu.asu.msse.rsingh92.assignment1.models.PlaceDescription;
import edu.asu.msse.rsingh92.assignment1.utilities.AppUtility;
import edu.asu.msse.rsingh92.assignment1.utilities.DBUtility;
import edu.asu.msse.rsingh92.assignment1.utilities.TempDBUtility;


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
public class SplashActivity extends AppCompatActivity implements RPCSyncCallback {

    private LottieAnimationView lottieAnimationView;

    @Override
    public void onCreate(Bundle savesInstanceState){
        super.onCreate(savesInstanceState);
        setContentView(R.layout.activity_splash);
        lottieAnimationView = findViewById(R.id.splash_anim);
        DBUtility.initDatabase(this);
        TempDBUtility.init(this);

        Log.d("BACKUP", "onCreate: "+TempDBUtility.get(TempDBUtility.BACKUP).size());

        List<PlaceDescription> allplaces = DBUtility.getAllPlacesFromDB();
        AppUtility.setAllPlacesOnMemory(allplaces);


        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            public void onPostExecute(Void param){
                startActivity(new Intent(SplashActivity.this, PlaceListActivity.class));
                finish();
            }

        }.execute();




//        loadListFromRPC();

    }

    @Override
    public void resultLoaded(Object object) {
        startActivity(new Intent(SplashActivity.this, PlaceListActivity.class));
        finish();
    }


    private void loadListFromRPC(){
        AppUtility.getAllPlacesFromServer(this);
    }

    @Override
    public void onFail(String methodname) {

    }

    @Override
    public void onSuccess(Object object) {

    }
}
