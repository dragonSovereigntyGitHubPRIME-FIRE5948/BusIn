package com.example.sgbusandlocationalarm;

import android.content.Context;
import android.content.Intent;

import com.example.sgbusandlocationalarm.Bus.BusArrivalFragment;
import com.example.sgbusandlocationalarm.Notifier.NotifierFormActivity;
import com.google.android.material.navigation.NavigationBarView;

// TODO
public class NavListeners {
    public static void navListener(NavigationBarView nav, Context context) {
       nav.setOnItemSelectedListener( item -> {
           navigateToScreen(item.getItemId(), context); return true;
        });
    }

    private static void navigateToScreen(int screen, Context context) {
        Intent intent;

        // Screen
        if (screen == R.id.homeNav) {intent = new Intent(context, MainActivity.class);}
        else if (screen == R.id.arrivalNav) {intent = new Intent(context, BusArrivalFragment.class);}
        else if (screen == R.id.geofenceNav) {intent = new Intent(context, NotifierFormActivity.class);}
        // Default
        else {intent = new Intent(context, MainActivity.class);}

        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
    }
}
