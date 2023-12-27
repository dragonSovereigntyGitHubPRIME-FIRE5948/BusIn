package com.example.sgbusandlocationalarm.Utils;

import android.app.Activity;

import com.example.sgbusandlocationalarm.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class PlayServicesUtils {
    public static boolean checkGooglePlayServices(Activity context, int requestCode) {
        GoogleApiAvailability googleApi = GoogleApiAvailability.getInstance();
        int errorCode = googleApi.isGooglePlayServicesAvailable(context);
        // Google Service not available
        if (errorCode != ConnectionResult.SUCCESS) {
            if (googleApi.isUserResolvableError(errorCode)) {
                // context, error code, request code, cancelListener
                googleApi.showErrorDialogFragment(context, errorCode, requestCode);
            }
        }
        return true;
    }
}
