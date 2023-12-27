package com.example.sgbusandlocationalarm;

import android.Manifest;

public class Constants {

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
}
