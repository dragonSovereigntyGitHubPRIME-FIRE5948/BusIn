package com.example.sgbusandlocationalarm.Utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;

import com.example.sgbusandlocationalarm.BuildConfig;
import com.example.sgbusandlocationalarm.Geofence.NotifierMapsActivity;
import com.example.sgbusandlocationalarm.MyLatLng;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    /** */
    public static void startActivity(Activity activity, Class activityToStart) {
        Intent intent = new Intent(activity, activityToStart);
        activity.startActivity(intent);
        activity.finish();
    }

    /** */
    public static void startActivityPutLatLng(Activity activity, Class activityToStart, ArrayList<MyLatLng> geoCoordinates) {
        Intent intent = new Intent(activity, activityToStart);
        intent.putParcelableArrayListExtra("GeoCoordinates", geoCoordinates);
        activity.startActivity(intent);
        activity.finish();
    }

    /** */

    /** */
    public static String getEditTextString(Activity activity, EditText editText, String message) {
        if (editText != null) {
            return  editText.getText().toString();
        }
        else {
            Toast.makeText(activity.getApplicationContext(),message,Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /** */
    public static Integer getEditTextInteger(Activity activity, EditText editText, String message) {
        if (editText != null) {
            return  Integer.parseInt(editText.getText().toString());
        }
        else {
            Toast.makeText(activity.getApplicationContext(),message,Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /** Checks if user detail is empty or null and sets default text if it is */
    public static void setTextView(TextView tv, String text) {
        // check for empty String and whitespace only String
        if (text == null) {
            tv.setText("-");
        } else {
            if (!text.isEmpty() && text.trim().length() > 0) {
                tv.setText(text);
            } else {
                tv.setText("-");
            }
        }
    }

    /** Set uri image for shapeable image view */
    public static void setImageView(ShapeableImageView view, String urlProfilePic, Activity activity) {
        if (urlProfilePic != null) {
//            Glide.with(activity.getApplicationContext())
//                    .load(Uri.parse(urlProfilePic))
//                    .apply(RequestOptions.fitCenterTransform())
//                    .into(view);
        }
    }

    /** */
    public static boolean getSwitchState(SwitchCompat switchCompat) {
        if (switchCompat.isChecked()) {
            return true;
        } else {
            return false;
        }
    }

    /** */
    public static void startDatePickerDialog(Activity activity, EditText et) {

        // Get Current Date
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Initialise Listener
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                et.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        };

        // Initialise Dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                // context
                activity,
                // listener
                listener,
                // initial data
                mYear, mMonth, mDay);

        // Show Dialog
        datePickerDialog.show();
    }

    /** */
    public static void startTimePickerDialog(Activity activity, EditText et) {

        // Get Current Time
        int mHour, mMinute;
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Initialise Listener
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay,
                                  int minute) {

                int i;
                String AM_PM ;
                if(hourOfDay < 12) {
                    i = hourOfDay;
                    AM_PM = "AM";
                } else {
                    i = hourOfDay - 12;
                    AM_PM = "PM";
                }

                et.setText(i + ":" + minute + " "+ AM_PM);
            }
        };

        // Initialise Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                // context
                activity,
                // show spinner mode only
                // android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                // listener
                listener,
                // initial data
                mHour, mMinute, false);

        // Show Dialog
        timePickerDialog.show();
    }

        /** Convert Date Object to formatted string */
    // TODO: Create DateTime for lower build versions //
    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        if (date != null) {
            return dateFormat.format(date);
        } else {
            return "-";
        }
    }

    /** Get current date time */
    public static Date getCurrentDateTime() {
        LocalDateTime ldt = LocalDateTime.now();
        Instant instant = ldt.atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    /** Get the difference between 2 time in minutes */
    public static Integer timeDifferenceInMinutes(Date dateTime1, Date dateTime2) {
        if (dateTime1 != null && dateTime2 != null) {
            long difference = dateTime1.getTime() - dateTime2.getTime();
            int min = (int) (difference / (1000 * 60)) % 60;
            return min;
        } else {return null;}
    }

    /** Retrieve LTA Datamall API Key from local.properties */
    public static String getLtaDataMallApiKey() {return BuildConfig.LTA_DATAMALL_KEY;}
    /** Retrieve Google Maps API Key from local.properties */
    public static String getMapsApiKey() {return BuildConfig.MAPS_API_KEY;}

}
