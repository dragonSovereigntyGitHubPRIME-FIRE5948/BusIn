package com.example.sgbusandlocationalarm.Geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.sgbusandlocationalarm.Notifier.NotificationHelper;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    // Variables
    private GeofencingEvent geofencingEvent;
    private NotificationHelper notificationHelper;

    // Debugging
    // private static final String TAG = "GeofenceBroadcast";

    // Functions
    /** Handles when BroadcastReceiver receives data (in this case it is the transition event) from the PendingIntent */
    @Override
    public void onReceive(Context context, Intent intent) {

        // receive PendingIntent.
        geofencingEvent = GeofencingEvent.fromIntent(intent);
        // instantiate NotificationHelper.
        notificationHelper = new NotificationHelper(context);
        // instantiate Geofence List that contains Geofences that were triggered in transition event.
        // a single-event can trigger multiple Geofences.
        // for example, if you had added same location in 2 separate Geofences.
        List<Geofence> triggeredGeofences = geofencingEvent.getTriggeringGeofences();
        // get transition type.
        int transitionType = geofencingEvent.getGeofenceTransition();

        // handle transition event types
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                Toast.makeText(context, "GEOFENCE_TRANSITION_ENTER", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification(triggeredGeofences.get(0).getLatitude()+"", "ENTER", NotifierMapsActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                Toast.makeText(context, "GEOFENCE_TRANSITION_DWELL", Toast.LENGTH_SHORT).show();
                //todo
                notificationHelper.sendHighPriorityNotification(triggeredGeofences.get(0).getLatitude()+"", "DWELL", NotifierMapsActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Toast.makeText(context, "This is exit", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification(triggeredGeofences.get(0).getLatitude()+"", "EXIT   ", NotifierMapsActivity.class);
                break;
        }

        // Error handling
//        if (geofencingEvent.hasError()) {
//            if (geofencingEvent.hasError()) {
//                String errorMessage = GeofenceStatusCodes
//                        .getStatusCodeString(geofencingEvent.getErrorCode());
//                Log.e(TAG, errorMessage);
//                return;
//            }
//
//            for (Geofence geofence : triggeredGeofences) {
////            Log.d(TAG, "onReceive: " + geofence.getRequestId());
//            }
//
//        }
    }
}
