package edu.asu.msse.rsingh92.assignment1;

import android.app.Activity;
import android.content.Context;

import java.security.PublicKey;

import androidx.appcompat.app.AppCompatActivity;

public class AppUtility {

    public static String MODIFY_PLACE="edu.asu.msse.rsingh92.assignment1.AppUtility.MODIFY_PLACE";
    public static String CURRENT_PLACE="edu.asu.msse.rsingh92.assignment1.AppUtility.CURRENT_PLACE";

    public static void openConfirmationDialog(AppCompatActivity activity, String msg){
        ConfirmationDialog confirmationDialog=new ConfirmationDialog(msg);
        confirmationDialog.show(activity.getSupportFragmentManager(),"example dialog");
    }
}
