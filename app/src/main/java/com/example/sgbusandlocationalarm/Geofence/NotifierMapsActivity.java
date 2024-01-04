package com.example.sgbusandlocationalarm.Geofence;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.example.sgbusandlocationalarm.NavListeners;
import com.example.sgbusandlocationalarm.R;
import com.example.sgbusandlocationalarm.databinding.ActivityNotifierMapsBinding;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class NotifierMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    // Variables
    // views
    private ActivityNotifierMapsBinding binding;
    // maps
    private GoogleMap googleMap;
    // intent
    private ArrayList<String> arrayListCoordinateNames;
    private ArrayList<LatLng> arrayListCoordinates;
    // geofence
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;
    // constants
    private final float GEOFENCE_RADIUS = 150;
    private final int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private final int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;
    public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

//    TODO CHECK PERMISSIONS
    public static final String ACCESS_BACKGROUND_LOCATION = Manifest.permission.ACCESS_BACKGROUND_LOCATION;
    public static final String POST_NOTIFICATIONS = Manifest.permission.POST_NOTIFICATIONS;

    private static final String[] permissions =
            new String[] {
                    ACCESS_COARSE_LOCATION,
                    ACCESS_FINE_LOCATION,
//                    permissionBackgroundLocation,
//                    permissionNoti
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotifierMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initialisations.
        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceHelper = new GeofenceHelper(this);

        // get data from Intent.
        arrayListCoordinates = getIntentCoordinates();
        arrayListCoordinateNames = getIntentNames();

        // obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(binding.map.getId());
        mapFragment.getMapAsync(this);

        //TODO CHANGE
        NavListeners.navListener(binding.nav, this);
        this.requestPermissions(permissions, 555);
    }

    /** Manipulates the map once available. This callback is triggered when the map is ready to be used. */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        //TODO LONG CLICK
        //googleMap.setOnMapLongClickListener(this);

        // reference
        this.googleMap = googleMap;

        // set up visualisation for Geofences.
        for (int i = 0; i< arrayListCoordinates.size(); i++) {
            // add marker of coordinates.
            this.googleMap.addMarker(new MarkerOptions()
                    .position(arrayListCoordinates.get(i))
                    .title(arrayListCoordinateNames.get(i))
            );
            // add circle of Geofence.
            addCircle(arrayListCoordinates.get(i), GEOFENCE_RADIUS);
            // add Geofence.
            addGeofencesForTracking();
        }

        // move camera & zoom to first coordinate
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arrayListCoordinates.get(0), 15.0f));
        this.googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        googleMap.setMyLocationEnabled(true);

        // Set zoom levels
//        map.setMinZoomPreference(6.0f);
//        map.setMaxZoomPreference(14.0f);

        // Set Boundary
//        LatLngBounds australiaBounds = new LatLngBounds(
//                new LatLng(-44, 113), // SW bounds
//                new LatLng(-10, 154)  // NE bounds
//        );
//        map.moveCamera(CameraUpdateFactory.newLatLngBounds(australiaBounds, 0));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            enableUserLocation();
//        }

//        this.googleMap.addMarker(new MarkerOptions().position(geofence2));
//        this.googleMap.addMarker(new MarkerOptions().position(new LatLng(1.4256, 103.8350)));
//        this.googleMap.addMarker(new MarkerOptions().position(new LatLng(1.4197, 103.8343)));
//        this.googleMap.addMarker(new MarkerOptions().position(new LatLng(1.4163, 103.8326)));
//        this.googleMap.addMarker(new MarkerOptions().position(new LatLng(1.4128, 103.8308)));
//
//        // Add polylines to the map.
//        // Polylines are useful to show a route or some other connection between points.
//        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        geofence2,
//                        new LatLng(1.4256, 103.8350),
//                        new LatLng(1.4197, 103.8343),
//                        new LatLng(1.4163, 103.8326),
//                        new LatLng(1.4128, 103.8308)
//                ));
    }

//    @Override
//    public void onMapLongClick(LatLng latLng) {
//        if (Build.VERSION.SDK_INT >= 29) {
//            //We need background permission
//            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                handleMapLongClick(latLng);
//            } else {
//                if (this.shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
//                    //We show a dialog and ask for permission
//                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
//                } else {
//                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
//                }
//            }
//        } else {
//            handleMapLongClick(latLng);
//        }
//    }

//    private void handleMapLongClick(LatLng latLng) {
//        googleMap.clear();
//        addMarker(latLng);
//        addCircle(latLng, GEOFENCE_RADIUS);
////        addGeofence();
//    }

    // TODO BRING GEOFENCE TO NOTIFIER FORM ACTIVITY
    /** Add Geofences for tracking */
    @SuppressLint("MissingPermission")
    private void addGeofencesForTracking() {
        // create Geofences with coordinates from NotifierForm Intent.
        ArrayList<Geofence> arrayListGeofences = geofenceHelper.createGeofences(arrayListCoordinates);
        // GeofencingRequest settings.
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(arrayListGeofences);
        // PendingIntent for BroadcastReceiver.
        PendingIntent pendingIntent = geofenceHelper.getGeofencePendingIntent();

        // FINAL: add Geofences for tracking.
        // handle success and failure.
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(NotifierMapsActivity.this, "Geofence successfully add", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onSuccess: Geofence Added...");
                })
                .addOnFailureListener(e -> {
                    //String errorMessage = geofenceHelper.getErrorString(e);
                    Toast.makeText(NotifierMapsActivity.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: ");
                });
    }

    /** Map UI Manipulation */

    /** Adds marker based on geo coordinates */
    private void addMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        googleMap.addMarker(markerOptions);
    }

    /** Creates circle for geofence */
    private void addCircle(LatLng latLng, float radius) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(R.color.navy);
        //todo change transparency
        circleOptions.fillColor(R.color.g1);
        circleOptions.strokeWidth(5);
        googleMap.addCircle(circleOptions);
    }

    // Permissions

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    // ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);
            } else {
                //We do not have the permission..
            }
        }

        if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have the permission
                Toast.makeText(this, "You can add geofences...", Toast.LENGTH_SHORT).show();
            } else {
                //We do not have the permission..
                Toast.makeText(this, "Background location access is neccessary for geofences to trigger...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ArrayList<String> getIntentNames() {
        ArrayList<String> arrayListNames = getIntent().getStringArrayListExtra("NamesFromNotifierForm");
        // checks if ArrayList is empty.
        // checks if ArrayList contains null values.
        if (arrayListNames.isEmpty() || arrayListNames.contains(null)) {
            finish();
        }
        return arrayListNames;
    }

    private ArrayList<LatLng> getIntentCoordinates() {
        ArrayList<LatLng> arrayListGeoCoordinates = getIntent().getParcelableArrayListExtra("CoordinatesFromNotifierForm");
        // checks if ArrayList is empty.
        // checks if ArrayList contains null values.
        if (arrayListGeoCoordinates.isEmpty() || arrayListGeoCoordinates.contains(null)) {
            finish();
        }
        return arrayListGeoCoordinates;
    }



}