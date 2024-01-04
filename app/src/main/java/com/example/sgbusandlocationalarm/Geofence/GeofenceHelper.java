package com.example.sgbusandlocationalarm.Geofence;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.UUID;

public class GeofenceHelper extends ContextWrapper {

    // VARIABLES
    private PendingIntent pendingIntent;

    // CONSTRUCTORS
    public GeofenceHelper(Context base) {
        super(base);
    }

    // FUNCTIONS
    /** Creates a Geofence with Geofence.Builder subclass */
    public Geofence createGeofence(String id, LatLng latLng, float radius, int transitionTypes) {
        return new Geofence.Builder()
                .setRequestId(id)
                .setCircularRegion(latLng.latitude, latLng.longitude, radius)
                .setTransitionTypes(transitionTypes)
                 .setLoiteringDelay(0)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                // .setNotificationResponsiveness(0)
                .build();
    }

    /** Creates a list of Geofences with Geofence.Builder subclass */
    public ArrayList<Geofence> createGeofences(ArrayList<LatLng> arrayListLatLng) {

        // instantiate list for Geofences
        ArrayList<Geofence> arrayListGeofences = new ArrayList<>();

        // loop through list of coordinates and create Geofences accordingly
        for(int i = 0; i < arrayListLatLng.size(); i++) {
            arrayListGeofences.add(createGeofence(UUID.randomUUID().toString(), arrayListLatLng.get(i), 500, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT));
        }

        return arrayListGeofences;
    }

    /** Specifies the geofences to monitor and to set how related geofence events are triggered */
    public GeofencingRequest getGeofencingRequest(ArrayList<Geofence> arrayListGeofence) {
        return new GeofencingRequest.Builder()
                // geofencing service trigger a GEOFENCE_TRANSITION_ENTER notification when the geofence is
                // added and if the device is already inside that geofence.
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                // add the geofences to be monitored by geofencing service using the list we created.
                .addGeofences(arrayListGeofence)
                .build();
    }

    /** PendingIntent that starts a BroadcastReceiver */
    public PendingIntent getGeofencePendingIntent() {
        // reuse the PendingIntent if we already have it.
        if (pendingIntent != null) {
            return pendingIntent;
        }

        // create intent.
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);

        // start BroadcastReceiver with PendingIntent.
        // use FLAG_UPDATE_CURRENT to get the same pending intent back when calling addGeofences() and removeGeofences().
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(this, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(this, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        return pendingIntent;
    }
}

