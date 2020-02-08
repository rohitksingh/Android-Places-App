package edu.asu.msse.rsingh92.assignment1;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

public class AppUtility {

    public static void openConfirmationDialog(AppCompatActivity activity, String msg){
        ConfirmationDialog confirmationDialog=new ConfirmationDialog(msg);
        confirmationDialog.show(activity.getSupportFragmentManager(),"example dialog");
    }
}
