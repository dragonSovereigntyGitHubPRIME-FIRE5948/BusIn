package com.example.sgbusandlocationalarm.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.sgbusandlocationalarm.MainActivity;
import com.example.sgbusandlocationalarm.Helpers.UIBuilderHelper;

public class PermissionUtils extends MainActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;

    public static final String permissionCoarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String permissionFineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String permissionBackgroundLocation = Manifest.permission.ACCESS_BACKGROUND_LOCATION;

    public static final String permissionNoti = Manifest.permission.ACCESS_NOTIFICATION_POLICY;

//    public static final String permissionNotification = Manifest.permission.ACCNO;

    private static final String[] permissions =
            new String[] {
                    permissionCoarseLocation,
                    permissionFineLocation,
                    permissionBackgroundLocation,
                    permissionNoti
            };

    public PermissionUtils() {}

    /** Check if permission is allowed */
    public static Boolean hasPermission(Activity context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
        Show rationale for needing permission if user rejects permission the first time. Start activity of app setting.

        Request permission if it is first time.
     */
    public static void requestPermission(Activity context, String[] permissions) {
//        if (context.shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            UIBuilderHelper.showPermissionRationaleDialog(context, permissions, PERMISSION_REQUEST_CODE);
//        } else {
            ActivityCompat.requestPermissions(context, permissions, PERMISSION_REQUEST_CODE);
//        }
    }


    //todo display ui show permissions and avaialble features
    // todo button request precise location
//    ActivityResultLauncher<String[]> locationPermissionRequest =
//            registerForActivityResult(new ActivityResultContracts
//                            .RequestMultiplePermissions(), result -> {
//                        Boolean fineLocationGranted = result.getOrDefault(
//                                Manifest.permission.ACCESS_FINE_LOCATION, false);
//                        Boolean coarseLocationGranted = result.getOrDefault(
//                                Manifest.permission.ACCESS_COARSE_LOCATION,false);
//                        if (fineLocationGranted != null && fineLocationGranted) {
//                            // Precise location access granted.
//                        } else if (coarseLocationGranted != null && coarseLocationGranted) {
//                            // Only approximate location access granted.
//                        } else {
//                            // No location access granted.
//                        }
//                    }
//            );
//
//// ...
//
//// Before you perform the actual permission request, check whether your app
//// already has the permissions, and whether your app needs to show a permission
//// rationale dialog. For more details, see Request permissions.
//locationPermissionRequest.launch(new String[] {
//        Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//    });

    //todo request permission
    // context.requestPermissions(permissions, PERMISSION_REQUEST_CODE);

    /** Handle on permission request result  */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
//                    fusedLocationClient.getLastLocation()
//                            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                                @Override
//                                public void onSuccess(Location location) {
//                                    // Got last known location. In some rare situations this can be null.
//                                    if (location != null) {
//                                        double wayLatitude = location.getLatitude();
//                                        double wayLongitude = location.getLongitude();
//
//                                        tv1.setText(wayLongitude+"");
//                                        tv2.setText(wayLatitude+"");
//                                    }
//                                }
//                            });
                } else {
                }
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }

    /** Open App settings */
    public static void startAppDetailsActivity(final Activity context) {
        if (context == null) {return;}

        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }


//    public void mmm() {
//        ActivityResultLauncher<String[]> locationPermissionRequest =
//                registerForActivityResult(new ActivityResultContracts
//                                .RequestMultiplePermissions(), result -> {
////                            Boolean fineLocationGranted = result.getOrDefault(
////                                    android.Manifest.permission.ACCESS_FINE_LOCATION, false);
//                            Boolean coarseLocationGranted = null;
//
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                coarseLocationGranted = result.getOrDefault(
//                                        Manifest.permission.ACCESS_COARSE_LOCATION, false);
//                            }
//
////                            if (fineLocationGranted != null && fineLocationGranted) {
////                                // Precise location access granted.
////                            } else
//                            if (coarseLocationGranted != null && coarseLocationGranted) {
//                                // Only approximate location access granted.
//                            } else {
//                                showPermissionRationaleDialog();
//                            }
//                        }
//                );
//
//        locationPermissionRequest.launch(new String[]{
//                android.Manifest.permission.ACCESS_FINE_LOCATION,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION
//        });
//    }
}
