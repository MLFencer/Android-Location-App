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
        savedInstanceState = getArguments();
        alertType = savedInstanceState.getString("alert_type");
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
        } else if (alertType.equalsIgnoreCase("sure")){
            builder.setTitle("Confirm");
            builder.setMessage("Are You Sure?");
            builder.setNegativeButton("No", null);
            builder.setPositiveButton("Yes",null);
        }

        AlertDialog dialog = builder.create();
        return dialog;

    }
}
