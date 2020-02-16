package edu.asu.msse.rsingh92.assignment1.activities;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;

import androidx.appcompat.app.AppCompatActivity;
import edu.asu.msse.rsingh92.assignment1.R;
import edu.asu.msse.rsingh92.assignment1.RPC.AsyncCollectionConnect;
import edu.asu.msse.rsingh92.assignment1.RPC.RPCMethodMetadata;
import edu.asu.msse.rsingh92.assignment1.callbacks.RPCCallback;

public class SplashActivity extends AppCompatActivity implements RPCCallback {

    LottieAnimationView lottieAnimationView;

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

        try{
            RPCMethodMetadata mi = new RPCMethodMetadata(this, getString(R.string.defaulturl),"getNames",
                    new Object[]{});
            AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "Exception creating adapter: " +
                    ex.getMessage());
        }

    }
}
