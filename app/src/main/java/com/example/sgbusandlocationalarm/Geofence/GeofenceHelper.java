package com.example.sgbusandlocationalarm.Geofence;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import com.example.sgbusandlocationalarm.Constants;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class GeofenceHelper extends ContextWrapper {

    // VARIABLES.
    private PendingIntent pendingIntent;

    /** Constructor */
    public GeofenceHelper(Context base) {
        super(base);
    }

    //https://developer.android.com/training/location/geofencing#specify-geofences-and-initial-triggers

    /**
        Listens to geofence & takes necessary actions when it is fired.

        Uses the GeofencingRequest class and its nested GeofencingRequestBuilder class to
        specify the geofences to monitor and to set how related geofence events are triggered


        Best results: set a minimum radius of 100 meters. It helps account for the location accuracy of
        typical Wi-Fi networks, & also helps reduce device power consumption.
     */

    public GeofencingRequest getGeofencingRequests(ArrayList<LatLng> latLngList) {

        ArrayList<Geofence> geofenceList = new ArrayList<>();

        for(LatLng coordinates: latLngList) {
            geofenceList.add(createGeofence("d", coordinates, 500, 2));
        }




        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service using the list we created.
        builder.addGeofences(geofenceList);

        return builder.build();
    }

    public GeofencingRequest getGeofencingRequest(Geofence geofence) {

        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service using the list we created.
        builder.addGeofence(geofence);

        return builder.build();
    }

    //https://developer.android.com/training/location/geofencing#create-geofence-objects

    /**
        Build Geofence
     */
    public Geofence createGeofence(String id, LatLng latLng, float radius, int transitionTypes) {
        return new Geofence.Builder()
                .setRequestId(id)
                .setCircularRegion(latLng.latitude, latLng.longitude, radius)
                .setTransitionTypes(transitionTypes) // enter, dwell, exit
                .setLoiteringDelay(0)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                //todo
                .setNotificationResponsiveness(0)
                .build();
    }

    //https://developer.android.com/training/location/geofencing#define-a-broadcast-receiver-for-geofence-transitions

    /**
        PendingIntent that starts a BroadcastReceiver
     */
    public PendingIntent getPendingIntent() {
        // An Intent sent from Location Services can trigger various actions in your app,
        // but you should not have it start an activity or fragment,
        // because components should only become visible in response to a user action.
        // In many cases, a BroadcastReceiver is a good way to handle a geofence transition.
        // A BroadcastReceiver gets updates when an event occurs,
        // such as a transition into or out of a geofence, and can start long-running background work

        // Reuse the PendingIntent if we already have it.
        if (pendingIntent != null) {
            return pendingIntent;
        }

        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
//        intent.setAction(intent.ACTION_GEOFENCE_EVENT)
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling addGeofences() and removeGeofences().

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

