package com.example.sgbusandlocationalarm.Notifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.sgbusandlocationalarm.Constants;
import com.example.sgbusandlocationalarm.MyLatLng;
import com.example.sgbusandlocationalarm.NavListeners;
import com.example.sgbusandlocationalarm.R;

import android.Manifest;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sgbusandlocationalarm.Geofence.NotifierMapsActivity;
import com.example.sgbusandlocationalarm.Utils.GoogleUtils;
import com.example.sgbusandlocationalarm.Utils.PermissionUtils;
import com.example.sgbusandlocationalarm.Utils.PlayServicesUtils;
import com.example.sgbusandlocationalarm.Utils.Utils;
import com.example.sgbusandlocationalarm.databinding.ActivityNotifierFormBinding;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.protobuf.InvalidProtocolBufferException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NotifierFormActivity extends AppCompatActivity {

    //TODO CHECK DUPLICATE LOCATIONS

    // VARIABLES //
    private ActivityNotifierFormBinding binding;

    private final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private static final String permissionCoarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String permissionFineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String permissionBackgroundLocation = android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;
    public static final String permissionNoti = Manifest.permission.POST_NOTIFICATIONS;
//    public static final String permissionNotification = Manifest.permission.ACCNO;

    private static final String[] permissions =
            new String[] {
                    permissionCoarseLocation,
                    permissionFineLocation,
//                    permissionBackgroundLocation,
//                    permissionNoti
            };

    private NotifierManager notifierManager = NotifierManager.getInstance();
    private AutocompleteSupportFragment autocompleteFragment;
    private HashMap<String, MyLatLng> locationHashMap;
    private int counter = 0;


    // Intent
    private ArrayList<String> arrayListForIntentNames = new ArrayList<>();
    private ArrayList<MyLatLng> arrayListForIntentLatLng = new ArrayList<>();

    // LIFECYCLE //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotifierFormBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        NavListeners.navListener(binding.nav, this);

        // Check Google Play Services.
        PlayServicesUtils.checkGooglePlayServices(this, PLAY_SERVICES_RESOLUTION_REQUEST);
        // Initialise Places.
        if (!Places.isInitialized()) {Places.initialize(this, Utils.getMapsApiKey());}

        // Check Permissions.
        checkPermissions();
        // Set up listeners.
        setUpListeners();

        // Get AutoComplete fragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentLocationSearchBar);
        // Configure AutoComplete.
        GoogleUtils.configureAutoCompleteFragment(autocompleteFragment);

    }

    @Override
    protected void onResume() {
        super.onResume();
        GoogleUtils.configureAutoCompleteText(this, autocompleteFragment);
        getLocation();
    }

    // METHODS //

    /***/
    private void checkPermissions() {
        boolean permissionBackground = PermissionUtils.hasPermission(this, Constants.ACCESS_BACKGROUND_LOCATION);
        boolean permissionFine = PermissionUtils.hasPermission(this, Constants.ACCESS_FINE_LOCATION);

        if (!permissionFine) {
            PermissionUtils.requestPermission(this, permissions);
        }
    }

    /***/
    private void setUpListeners() {
        onClickButtonAddLocation();
        onClickEditTextDate();
        onClickEditTextTime();
        onClickSwitchActive();
        onClickButtonCreate();
        onClickButtonReset();
    }

    /***/
    private void onClickButtonAddLocation() {
        binding.btnAddLocation.setOnClickListener(view -> {

            EditText searchInput = this.findViewById(com.google.android.libraries.places.R.id.places_autocomplete_search_input);

            if (!locationHashMap.isEmpty() && !searchInput.getText().toString().equals("")) {
                Map.Entry<String, MyLatLng> entry = locationHashMap.entrySet().iterator().next();
                arrayListForIntentNames.add(entry.getKey());
                arrayListForIntentLatLng.add(entry.getValue());
                binding.tvNumberOfLocations.setText(arrayListForIntentNames.size()+"");
                GoogleUtils.clear(this,autocompleteFragment);
                Toast.makeText(this.getApplicationContext(), arrayListForIntentNames.get(locationHashMap.size()-1)+" Added" ,Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.getApplicationContext(), "Please choose a location" ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** */
    private void onClickEditTextDate() {
        binding.etStartDate.setOnClickListener(v -> {
            Utils.startDatePickerDialog(this, binding.etStartDate);
        });

        binding.etEndDate.setOnClickListener(v -> {
            Utils.startDatePickerDialog(this, binding.etEndDate);
        });
    }

    /** */
    private void onClickEditTextTime() {
        binding.etTime.setOnClickListener(v -> {
            Utils.startTimePickerDialog(this, binding.etTime);
        });
    }

    /** */
    private void onClickSwitchActive() {
        if (binding.switchSchedule.isChecked()) {
            binding.loDateTime.setVisibility(View.VISIBLE);
        }
        else {
            binding.loDateTime.setVisibility(View.GONE);
        }

        binding.switchSchedule.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    binding.loDateTime.setVisibility(View.VISIBLE);
                }
                else {
                    binding.loDateTime.setVisibility(View.GONE);
                }
            }
        });
    }

    /** */
    private void onClickButtonCreate() {
        binding.btnCreate.setOnClickListener(view -> {

            NotifierModel notifier;

            // Retrieve inputs.
            try {
                notifier = getUserInputs();
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e);
            }

            // Create Notifier Model and start new Activity.
            if (notifier != null) {
                // Save it to Firebase.
                notifierManager.createNotifier(notifier);
                // Start activity.
                Intent intent = new Intent(this, NotifierMapsActivity.class);
                intent.putStringArrayListExtra("NamesFromNotifierForm", arrayListForIntentNames);
                intent.putParcelableArrayListExtra("CoordinatesFromNotifierForm", arrayListForIntentLatLng);
                startActivity(intent);
                finish();
            } else {
                //TODO CREATE DIALOG
            }
        });
    }

    /** */
    private void onClickButtonReset() {
        binding.btnCancel.setOnClickListener(view -> {
            finish();
        });
    }

    /** */
    private NotifierModel getUserInputs() throws InvalidProtocolBufferException {

        NotifierModel notifier = null;

        // Name.
        String name = Utils.getEditTextString(this, binding.etName, "Please enter a name.");
        // Range.
        Integer range = Utils.getEditTextInteger(this, binding.etRange, "Please enter a range.");
        // Notifier is active or not.
        boolean isActive = Utils.getSwitchState( binding.switchNotifierActive);
        // Location Name.

        // Location Coordinates.
        if (arrayListForIntentNames.isEmpty() || arrayListForIntentLatLng.isEmpty()) {
            Toast.makeText(this.getApplicationContext(), "Please enter a location." ,Toast.LENGTH_SHORT).show();
        }

        String alertType = getAlertType();

        if (name != null &&
                range != null &&
                alertType != null &&
                !arrayListForIntentNames.isEmpty() &&
                !arrayListForIntentLatLng.isEmpty()) {
            //TODO ADD MORE

            if (!binding.switchSchedule.isChecked()) {
                notifier = new NotifierModel(
                        name,
                        "Single",
                        arrayListForIntentLatLng,
                        arrayListForIntentNames,
                        range,
                        alertType,
                        isActive);
            }
            else {

                String startDate = Utils.getEditTextString(this, binding.etStartDate, "Please enter a start date.");
                String endDate  = Utils.getEditTextString(this, binding.etEndDate, "Please enter an end date.");
                String time = Utils.getEditTextString(this, binding.etTime, "Please enter a time.");

                Date sdate = null;
                Date edate = null;

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm a");

                // .setTimeZone(TimeZone.getTimeZone("GMT"));

                try {
                    sdate = format.parse(startDate+" "+time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    edate = format.parse(endDate+" "+time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                notifier = new NotifierModel(
                        name,
                        "Single",
                        arrayListForIntentLatLng,
                        arrayListForIntentNames,
                        range,
                        alertType,
                        isActive,sdate, edate);
            }
        }

        return notifier;
    }

    /** */
    private void getLocation() {
        locationHashMap = GoogleUtils.getLocation(autocompleteFragment);
    }

    /** */
    private String getAlertType() {

        String alertType = null;

        if (binding.cbNotification.isChecked() || binding.cbAlarm.isChecked()) {
            if (binding.cbNotification.isChecked()) {
                alertType = "Notification";
            } else {
                alertType = "Alarm";
            }
        } else {
            Toast.makeText(this.getApplicationContext(), "Please select a Notifier type." ,Toast.LENGTH_SHORT).show();
        }
        return alertType;
    }

}