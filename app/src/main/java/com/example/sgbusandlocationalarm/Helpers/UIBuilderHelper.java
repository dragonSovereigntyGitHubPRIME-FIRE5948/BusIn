package com.example.sgbusandlocationalarm.Helpers;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

public class UIBuilderHelper extends FragmentActivity  {

    /** Alert Dialog - Permission Rationale */
    public static void showPermissionRationaleDialog(Activity context, String[] permissions, int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("This app needs access to your location to function properly.")
                .setTitle("Location Permission Required")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Request the permission again
//                        context.requestPermissions(permissions, 555);
//                        PermissionUtils.startAppDetailsActivity(context);

                    }
                });
        builder.create().show();
    }

//    public static void showNotifierForm(FragmentActivity context) {
//        NotifierFormDialogFragment dialogFragment = new NotifierFormDialogFragment();
//        dialogFragment.show(context.getSupportFragmentManager(),"Notifier Form Dialog Fragment");
//    }


}
