package com.example.attendence;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class exceldialog {
    private Activity activity;
    private AlertDialog dialog;

    exceldialog(Activity myactivity){
        activity = myactivity;
    }

    void startExcelDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_opendialog,null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }
}
