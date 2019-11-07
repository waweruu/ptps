package com.isproject.ptps.activities;

import android.content.Context;
import android.content.DialogInterface;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;

public class Utilities {
    public static void showWarningDialog(Context context) {
        final AlertDialog.Builder warningBuilder = new AlertDialog.Builder(context);
        warningBuilder.setTitle("Warning!");
        warningBuilder.setMessage("Please fill in the details requested! You cannot stop the registration" +
                " process once you have begun.");
        warningBuilder.setCancelable(false);
        warningBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog warningDialog = warningBuilder.create();
        warningDialog.show();
    }

    public static Long getArrayListSum(ArrayList<Long> arrayList) {
        long sum = 0;

        for(long i : arrayList) {
            sum += i;
        }

        return sum;
    }
}
