package com.example.sgbusandlocationalarm;

import android.Manifest;

public abstract class Constants {

    // Bus
    public static final String SEATS_AVAILABLE = "SEA";
    public static final String STANDING_AVAILABLE = "SDA";
    public static final String STANDING_LIMITED = "LSD";

    public static final String WHEELCHAIR_ACCESSIBLE = "WAB";

    public static final String SINGLE_DECK = "SD";
    public static final String DOUBLE_DECK = "DD";
    public static final String BENDY = "BD";

    // Permissions
    public static final String ACCESS_COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String ACCESS_FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String ACCESS_BACKGROUND_LOCATION = Manifest.permission.ACCESS_BACKGROUND_LOCATION;

    // Firebase
    public static final String COLLECTION_ACCOUNTS = "accounts";
    public static final String COLLECTION_NOTIFIERS = "notifiers";

    public static final String GEOFENCE_NAME_BUNDLE_KEY = "GeofenceNameBundleKey";
    public static final String GEOFENCE_NAME_REQUEST_KEY = "GeofenceNameRequestKey";
    public static final String GEOFENCE_LAT_LNG_BUNDLE_KEY = "GeofenceLatLngBundleKey";
    public static final String GEOFENCE_LAT_LNG_REQUEST_KEY = "GeofenceLatLngRequestKey";
    public static final String GEOFENCE_RADIUS_BUNDLE_KEY = "GeofenceRadiusBundleKey";
    public static final String GEOFENCE_RADIUS_REQUEST_KEY = "GeofenceRadiusRequestKey";

    public static final String SIGN_UP_EMAIL_BUNDLE_KEY = "SignUpEmailBundleKey";
    public static final String SIGN_UP_EMAIL_REQUEST_KEY = "SignUpEmailRequestKey";
    public static final String SIGN_UP_PASSWORD_BUNDLE_KEY = "SignUpPasswordBundleKey";
    public static final String SIGN_UP_PASSWORD_REQUEST_KEY = "SignUpPasswordRequestKey";

    public static final String LAT_LNG_BUNDLE_KEY = "LatLngBundleKey";
    public static final String LAT_LNG_REQUEST_KEY = "LatLngRequestKey";

    //
    public static final int GET_STRING = 1;
    public static final int GET_INT = 2;
    public static final int GET_LAT_LNG = 3;

    //    enum mhi {
    //        LOW,
    //        HIGH,
    //    }
}
