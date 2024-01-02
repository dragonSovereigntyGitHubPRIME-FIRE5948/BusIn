package com.example.sgbusandlocationalarm.Geofence;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class GeofenceUtils {

    /** If user long clicks map, add Geofence */
//    @SuppressLint("MissingPermission")
//    public static void addGeofence(GeofencingClient geofencingClient, GeofenceHelper geofenceHelper,
//                                   ArrayList<LatLng> latLngArrayList) {
//
//
//        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(latLngArrayList);
//        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();
//
//        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
////                        Toast.makeText(, "Geofence successfully add", Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, "onSuccess: Geofence Added...");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
////                        String errorMessage = geofenceHelper.getErrorString(e);
////                        Toast.makeText(, "Unsuccessful", Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, "onFailure: ");
//                    }
//                });
//    }

}
