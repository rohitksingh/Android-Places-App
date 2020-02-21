package edu.asu.msse.rsingh92.assignment1.activities;

import android.content.Intent;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;

import androidx.appcompat.app.AppCompatActivity;
import edu.asu.msse.rsingh92.assignment1.R;
import edu.asu.msse.rsingh92.assignment1.callbacks.RPCCallback;
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
public class SplashActivity extends AppCompatActivity implements RPCCallback {

    private LottieAnimationView lottieAnimationView;

    @Override
    public void onCreate(Bundle savesInstanceState){
        super.onCreate(savesInstanceState);
        setContentView(R.layout.activity_splash);
        lottieAnimationView = findViewById(R.id.splash_anim);
        loadListFromRPC();
    }

    @Override
    public void resultLoaded(Object object) {
        startActivity(new Intent(SplashActivity.this, PlaceListActivity.class));
        finish();
    }


    private void loadListFromRPC(){
        AppUtility.getAllPlacesFromServer(this);
    }

}
