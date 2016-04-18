package net.nofool.dev.tbd;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

public class NotificationsDialogFragment  extends DialogFragment{
    AlertDialog.Builder builder;
    Context context;
    String alertType;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        alertType = getArguments().getString("error_type");
        context= getActivity();
        builder= new AlertDialog.Builder(context);

        if (alertType.equalsIgnoreCase("network")){
            builder.setTitle("No Network Connection");
            builder.setMessage("For this Application to function as designed an Active Internet Connection is Required.");
            builder.setNegativeButton("Retry", null);
            builder.setPositiveButton("Ok",null);
        } else if (alertType.equalsIgnoreCase("email")){
            builder.setTitle("Error");
            builder.setMessage("Email Address is incorrect.");
            builder.setPositiveButton("Ok",null);
        } else if (alertType.equalsIgnoreCase("duplicate")){
            builder.setTitle("Error");
            builder.setMessage("This Email Address is already Registered.");
            builder.setPositiveButton("Ok",null);
        } else if (alertType.equalsIgnoreCase("password")){
            builder.setTitle("Error");
            builder.setMessage("The entered password is incorrect.");
            builder.setPositiveButton("Ok",null);
        } else if (alertType.equalsIgnoreCase("Other")){
            builder.setTitle("Confirm");
            builder.setMessage("This Device is already registered under another account");
            builder.setNegativeButton("Leave It", null);
            builder.setPositiveButton("Change Account",null);
        }else if (alertType.equalsIgnoreCase("first")) {
            builder.setTitle("Confirm");
            builder.setMessage("You must login once to activate this device!");
            builder.setNeutralButton("Ok", null);
        }

        AlertDialog dialog = builder.create();
        return dialog;

    }
}
