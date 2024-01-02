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
import android.os.Parcelable;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class NotifierMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    // Views
    private ActivityNotifierMapsBinding binding;

    // Maps
    private GoogleMap googleMap;

    // Geofence
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;

//    private LatLng geoCoordinates;
    private static LatLng geofence;
    private ArrayList<LatLng> geoCoordinates;
//    private LatLng geofence;

    // For best results, the minimum radius of the geofence should be set between 100 - 150 meters.
    private float GEOFENCE_RADIUS = 150;
    // todo make random
    private String GEOFENCE_ID = "gf-10293";
    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;

    public static final String permissionCoarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String permissionFineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String permissionBackgroundLocation = Manifest.permission.ACCESS_BACKGROUND_LOCATION;
    public static final String permissionNoti = Manifest.permission.POST_NOTIFICATIONS;

    private static final String[] permissions =
            new String[] {
                    permissionCoarseLocation,
                    permissionFineLocation,
//                    permissionBackgroundLocation,
//                    permissionNoti
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotifierMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavListeners.navListener(binding.nav, this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(binding.map.getId());
        mapFragment.getMapAsync(this);


        this.requestPermissions(permissions, 555);


        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceHelper = new GeofenceHelper(this);
    }

    /**
        Manipulates the map once available. This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setOnMapLongClickListener(this);

        this.googleMap = googleMap;

        // gets geo coordinates form intent, create new LatLng object with coordinates

        // TODO NULL CHECK
        geoCoordinates = getIntent().getParcelableArrayListExtra("GeoCoordinates");

        for (int i =0; i<geoCoordinates.size(); i++) {
            geofence = new LatLng(geoCoordinates.get(i).latitude, geoCoordinates.get(i).longitude);
        }

        //TODO CHECK ARRAYLIST EMPTY, ETC
        geofence = geoCoordinates.get(0);

        LatLng geofence2 = new LatLng(1.4293, 103.8352);

        this.googleMap.addMarker(new MarkerOptions().position(geofence2));
        this.googleMap.addMarker(new MarkerOptions().position(new LatLng(1.4256, 103.8350)));
        this.googleMap.addMarker(new MarkerOptions().position(new LatLng(1.4197, 103.8343)));
        this.googleMap.addMarker(new MarkerOptions().position(new LatLng(1.4163, 103.8326)));
        this.googleMap.addMarker(new MarkerOptions().position(new LatLng(1.4128, 103.8308)));

        // Add polylines to the map.
        // Polylines are useful to show a route or some other connection between points.
        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        geofence2,
                        new LatLng(1.4256, 103.8350),
                        new LatLng(1.4197, 103.8343),
                        new LatLng(1.4163, 103.8326),
                        new LatLng(1.4128, 103.8308)
                ));

        // Adds markers with coordinates.
        this.googleMap.addMarker(new MarkerOptions()
                .position(geofence)
                .title("Geofence")
        );
        // Adds circle of Geofence.
        addCircle(geofence, GEOFENCE_RADIUS);

        // Move camera & zoom
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(geofence, 15.0f));
        //animate camera
        this.googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        addGeofence(geofence, GEOFENCE_RADIUS);

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
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (Build.VERSION.SDK_INT >= 29) {
            //We need background permission
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                handleMapLongClick(latLng);
            } else {
                if (this.shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    //We show a dialog and ask for permission
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                }
            }
        } else {
            handleMapLongClick(latLng);
        }
    }

    private void handleMapLongClick(LatLng latLng) {
        googleMap.clear();
        addMarker(latLng);
        addCircle(latLng, GEOFENCE_RADIUS);
        addGeofence(latLng, GEOFENCE_RADIUS);
    }

//    /** If user long clicks map, add Geofence */
    @SuppressLint("MissingPermission")
    private void addGeofence(LatLng latLng, float radius) {
        Geofence geofence = geofenceHelper.createGeofence(GEOFENCE_ID, latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();

        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(NotifierMapsActivity.this, "Geofence successfully add", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onSuccess: Geofence Added...");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        String errorMessage = geofenceHelper.getErrorString(e);
                        Toast.makeText(NotifierMapsActivity.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: ");
                    }
                });
    }

    /** Map UI Manipulation */

    //TODO CREATE HELPER CLASS

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

}