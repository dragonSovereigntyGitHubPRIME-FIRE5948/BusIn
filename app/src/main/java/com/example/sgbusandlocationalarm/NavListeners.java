package com.example.sgbusandlocationalarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.sgbusandlocationalarm.BusArrival.BusArrivalActivity;
import com.example.sgbusandlocationalarm.Geofence.NotifierMapsActivity;
import com.example.sgbusandlocationalarm.Notifier.NotifierFormActivity;
import com.example.sgbusandlocationalarm.Notifier.NotifiersActivity;
import com.google.android.material.navigation.NavigationBarView;

public class NavListeners {
    public static void navListener(NavigationBarView nav, Context context) {
       nav.setOnItemSelectedListener( item -> {
            if (item.getItemId() == R.id.homeNav) {
                context.startActivity(new Intent(context, NotifiersActivity.class));
                ((Activity) context).finish();
            }
            else if (item.getItemId() == R.id.arrivalNav) {
                context.startActivity(new Intent(context, BusArrivalActivity.class));
                ((Activity) context).finish();
            }
            else if (item.getItemId() == R.id.geofenceNav) {
                context.startActivity(new Intent(context, NotifierFormActivity.class));
                ((Activity) context).finish();
            }
            return true;
        });
    }
}
