package com.example.sgbusandlocationalarm.BusArrival;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.sgbusandlocationalarm.Geofence.GeofenceHelper;
import com.example.sgbusandlocationalarm.HttpClient;
import com.example.sgbusandlocationalarm.MainActivity;
import com.example.sgbusandlocationalarm.NavListeners;
import com.example.sgbusandlocationalarm.R;
import com.example.sgbusandlocationalarm.Utils.Utils;
import com.example.sgbusandlocationalarm.databinding.ActivityBusArrivalBinding;
import com.example.sgbusandlocationalarm.databinding.ActivityNotifierMapsBinding;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.chip.ChipGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class BusArrivalActivity extends AppCompatActivity implements
        BusStopAdapter.OnItemClickListener, OnMapReadyCallback, GoogleMap.OnMapLongClickListener{

    ////////////////
    // VARIABLES //
    ///////////////
    private ActivityBusArrivalBinding binding;
    private BusStopAdapter busStopAdapter;
    private List<BusStopModel.BusStopDetails> filteredList = new ArrayList<>();
    private List<BusStopModel.BusStopDetails> listBusStops = new ArrayList();
    private GoogleMap googleMap;

    ////////////////
    // LIFECYCLE //
    ///////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBusArrivalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // nav
//        binding.nav.setOnItemSelectedListener( item -> {
//            if (item.getItemId() == R.id.homeNav) {
//                startActivity(new Intent(this, MainActivity.class));
//                finish();
//            }
//            else if (item.getItemId() == R.id.arrivalNav) {
//                startActivity(new Intent(this, BusArrivalActivity.class));
//                finish();
//            }
//            return true;
//        });

        NavListeners.navListener(binding.nav, this);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        if (googleMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        // Functions.

        if (listBusStops.isEmpty()) {
            getBusStops(); // Display all bus stops
        }

        searchBusStop(); // SearchView - search bus stop names or codes
        checkCheckboxState();
        checkboxListener();
    }

    ////////////////
    // FUNCTIONS //
    ///////////////

    /**
        Expand nested RecyclerView for bus arrival details when bus stop item is clicked
     */
    @Override
    public void onItemClick(int position, BusStopModel.BusStopDetails item) {}

    /**
        Retrieve bus stops from LTA DataMall server in new thread.
        Update ui accordingly in main thread.
     */
    private void getBusStops() {
        // Start a new single thread sending a request to LTA Server
        Executors.newSingleThreadExecutor().execute(() -> {
            // Main Thread.
            Handler mainHandler = new Handler(Looper.getMainLooper());
            // Build OkHttp client.
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            // API limits to 500 bus stops per call. There are around 5000 bus stops.
            // Create a list to iterate it through and make multipple calls.
            List<Integer> arr = Arrays.asList(0, 500, 1000, 1500, 2000, 2500, 3000, 3500, 4000, 4500, 5000);

            for (int i = 0; i < arr.size(); i++) {

                // Build HTTP request.
                Request request = new Request.Builder()
                        .url("http://datamall2.mytransport.sg/ltaodataservice/BusStops?$skip="+arr.get(i))
                        .addHeader("AccountKey", Utils.getLtaDataMallApiKey())
                        .build();

                // Try block the HTTP response
                try (Response response = client.newCall(request).execute()) {
                    // Body.
                    ResponseBody body = response.body();
                    // ObjectMapper.
                    ObjectMapper objectMapper = new ObjectMapper();
                    // Map Json string to BusStopmModel class.
                    BusStopModel model = objectMapper.readValue(body.string(), BusStopModel.class);
                    // Pass it into the listBusStops list
                    listBusStops.addAll(model.getBusStopDetails());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            mainHandler.post(() -> {
                // Set Up Adapter
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                binding.rvBusStops.setLayoutManager(linearLayoutManager);
                busStopAdapter = new BusStopAdapter(listBusStops, this);
                binding.rvBusStops.setAdapter(busStopAdapter);
                binding.rvBusStops.setHasFixedSize(true);
            });
        });
    }

    /**
        SearchView
     */

    /** Search bus stop name or codes */
    private void searchBusStop() {
        binding.svSearchBar.clearFocus();
        binding.svSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
    }

    /** SearchView list filtering */
    private void filterList(String searchText) {

        filteredList.clear();

        for (int i=0; i < listBusStops.size(); i++) {
            // road name
            if (listBusStops.get(i).getRoadName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(listBusStops.get(i));
            }
            // bus stop code
            else if (listBusStops.get(i).getBusStopCode().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(listBusStops.get(i));
            }
            // description
            else if (listBusStops.get(i).getDescription().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(listBusStops.get(i));
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            busStopAdapter.setFilteredList(filteredList);
        }
    }

    /**
        Manipulates the map once available. This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setOnMapLongClickListener(this);

        this.googleMap = googleMap;

        LatLng geofence2 = new LatLng(1.4293, 103.8352);

        // Adds markers with coordinates.
        this.googleMap.addMarker(new MarkerOptions()
                .position(geofence2)
                .title("Geofence")
        );
        // Adds circle of Geofence.
        addCircle(geofence2, 150);

        // Move camera & zoom
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(geofence2, 15.0f));
        //animate camera
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
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
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
    }

    private void handleMapLongClick(LatLng latLng) {
        googleMap.clear();
        addMarker(latLng);
        addCircle(latLng, 150);
//        addGeofence(latLng, GEOFENCE_RADIUS);
    }

//    /** If user long clicks map, add Geofence */
//    @SuppressLint("MissingPermission")
//    private void addGeofence(LatLng latLng, float radius) {
//        Geofence geofence = geofenceHelper.createGeofence(GEOFENCE_ID, latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
//        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
//        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();
//
//        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(NotifierMapsActivity.this, "Geofence successfully add", Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, "onSuccess: Geofence Added...");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
////                        String errorMessage = geofenceHelper.getErrorString(e);
//                        Toast.makeText(NotifierMapsActivity.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, "onFailure: ");
//                    }
//                });
//    }

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

    /***/
    private void checkCheckboxState() {
        if (binding.cbShowMap.isChecked()) {
            binding.cvMap.setVisibility(View.VISIBLE);
            binding.cbFullMap.setVisibility(View.VISIBLE);
        } else {
            binding.cvMap.setVisibility(View.GONE);
            binding.cbFullMap.setVisibility(View.GONE);
        }
    }

    /***/
    private void checkboxListener() {
        binding.cbShowMap.setOnCheckedChangeListener((compoundButton, b) -> {
            if (binding.cbShowMap.isChecked()) {
                binding.cvMap.setVisibility(View.VISIBLE);
                binding.cbFullMap.setVisibility(View.VISIBLE);
            } else {
                binding.cvMap.setVisibility(View.GONE);
                binding.cbFullMap.setVisibility(View.GONE);
                binding.cbFullMap.setChecked(false);
            }
        });

        binding.cbFullMap.setOnCheckedChangeListener((compoundButton, b) -> {
            if (binding.cbFullMap.isChecked()) {
                binding.clRV.setVisibility(View.GONE);
                binding.loHeader.getLayoutParams().height= ViewGroup.LayoutParams.MATCH_PARENT;

                // Get the constraint layout of the parent constraint view.
                ConstraintLayout mConstraintLayout = findViewById(R.id.loHeader);
                ConstraintSet mConstraintSet = new ConstraintSet();
                mConstraintSet.clone(mConstraintLayout);
                mConstraintSet.constrainPercentHeight(R.id.cvMap, 0.8F);
                mConstraintSet.applyTo(mConstraintLayout);

                binding.svSearchBar.setVisibility(View.GONE);

            } else {
                binding.clRV.setVisibility(View.VISIBLE);
                binding.loHeader.getLayoutParams().height= ViewGroup.LayoutParams.WRAP_CONTENT;

                ConstraintLayout mConstraintLayout = findViewById(R.id.loHeader);
                ConstraintSet mConstraintSet = new ConstraintSet();
                mConstraintSet.clone(mConstraintLayout);
                mConstraintSet.constrainPercentHeight(R.id.cvMap, 0.6F);
                mConstraintSet.applyTo(mConstraintLayout);

                binding.svSearchBar.setVisibility(View.VISIBLE);
            }
        });
    }

}