package com.example.sgbusandlocationalarm.Utils;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.sgbusandlocationalarm.MyLatLng;
import com.example.sgbusandlocationalarm.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.HashMap;

public class GoogleUtils  {

    private static double longitude;
    private static double latitude;
    private static String name;
    private static MyLatLng coordinates;
    private static HashMap<String, MyLatLng> locationHashMap = new HashMap<>();

    AutocompleteSupportFragment autocompleteFragment;

    /******************/
    /** AUTOCOMPLETE */
    /****************/

    /** */
    public static void configureAutoCompleteFragment (AutocompleteSupportFragment autocompleteFragment) {
        // no hint
        autocompleteFragment.setHint("Location");
        // Place data to return
        autocompleteFragment.setPlaceFields(Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG));
        // restrict to SG
        autocompleteFragment.setCountries("SG");
    }

    /** */
    public static void configureAutoCompleteText (Activity activity, AutocompleteSupportFragment autocompleteFragment) {
        EditText e = autocompleteFragment.getView().findViewById(
                com.google.android.libraries.places.R.id.places_autocomplete_search_input);

        // search icon
        (activity.findViewById(com.google.android.libraries.places.R.id.places_autocomplete_search_button)).setVisibility(View.GONE);
        // text size
        e.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        // text hint color
        e.setHintTextColor(ContextCompat.getColor(activity, R.color.hint));
        // text color
        e.setTextColor(ContextCompat.getColor(activity, R.color.on_surface));
        // font
        Typeface face = ResourcesCompat.getFont(activity,R.font.montserrat_medium);
        e.setTypeface(face);
        // padding
        e.setPadding(0, 52,0,52);

//        (activity.findViewById(com.google.android.libraries.places.R.id.places_autocomplete_powered_by_google)).setVisibility(View.GONE);
    }

    /** */
    public static HashMap<String, MyLatLng> getLocation(AutocompleteSupportFragment autocompleteFragment) {
        // set up PlaceSelectionListener to handle the response
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                name = place.getName();
                coordinates = new MyLatLng(latitude, longitude);
                locationHashMap.put(name, coordinates);
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        return locationHashMap;
    }

    //TODO
    /** */
    public static void clear (Activity activity, AutocompleteSupportFragment autocompleteFragment) {
        (activity.findViewById(com.google.android.libraries.places.R.id.places_autocomplete_clear_button)).callOnClick();
    }
}
