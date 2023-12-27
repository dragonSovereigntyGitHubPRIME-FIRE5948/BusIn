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
    private static final String TAG = "GeofenceBroadcast";

    /**
        After detecting the transition event via the PendingIntent, the BroadcastReceiver gets the geofence transition type and
        tests whether it is one of the events the app uses to trigger notifications (enter, dwell, etc).
        The service then sends a notification.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Toast.makeText(context, "Geofence triggered...", Toast.LENGTH_SHORT).show();

        // Receive Geofence pending intent
        geofencingEvent = GeofencingEvent.fromIntent(intent);
        // Instantiate NotificationHelper
        notificationHelper = new NotificationHelper(context);

        // Instantiate Geofence List (Geofences that were triggered)
        // Get the Geofences that were triggered. A single event can trigger multiple geofences.

        List<Geofence> triggeredGeofences = geofencingEvent.getTriggeringGeofences();

        // Get Geofence transition type
        int transitionType = geofencingEvent.getGeofenceTransition();

        // Inform user

        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                Toast.makeText(context, "GEOFENCE_TRANSITION_ENTER", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification(triggeredGeofences.get(0).getRequestId(), "ENTER", NotifierMapsActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                Toast.makeText(context, "GEOFENCE_TRANSITION_DWELL", Toast.LENGTH_SHORT).show();
                //todo
                notificationHelper.sendHighPriorityNotification(triggeredGeofences.get(0).getRequestId(), "DWELL", NotifierMapsActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Toast.makeText(context, "GEOFENCE_TRANSITION_EXIT", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_EXIT", "EXIT   ", NotifierMapsActivity.class);
                break;
        }

        // Error handling
        if (geofencingEvent.hasError()) {
            if (geofencingEvent.hasError()) {
                String errorMessage = GeofenceStatusCodes
                        .getStatusCodeString(geofencingEvent.getErrorCode());
                Log.e(TAG, errorMessage);
                return;
            }

            for (Geofence geofence : triggeredGeofences) {
//            Log.d(TAG, "onReceive: " + geofence.getRequestId());
            }

        }
    }
}
