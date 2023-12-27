package com.example.sgbusandlocationalarm.Notifier;

import androidx.annotation.NonNull;

import com.example.sgbusandlocationalarm.MyLatLng;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;

public class NotifierModel {

    // PROPERTIES //
    @NonNull
    private String name;
    @NonNull
    private String notifierType;
    @NonNull
    private ArrayList<MyLatLng> locationCoordinates;
    @NonNull
    private ArrayList<String> locationNames;
    @NonNull
    private Integer range;
    @NonNull
    private String alertType;
    @NonNull
    private Boolean isActive;
    private Date startDateTime;
    private Date endDateTime;

    // CONSTRUCTOR //

    // java.lang.RuntimeException: Could not deserialize object. Class com.example.collabbees.Accounts.AccountModel
    // does not define a no-argument constructor. If you are using ProGuard, make sure these constructors are not stripped
    //Because to deserialize using firestore requires no-arg constructor
    public NotifierModel(){}

    public NotifierModel(String name, String notifierType, ArrayList<MyLatLng> locationCoordinates, ArrayList<String> locationName,
                         Integer range, String alertType, Boolean isActive) {
        this.name = name;
        this.notifierType = notifierType;
        this.locationCoordinates = locationCoordinates;
        this.locationNames = locationName;
        this.range = range;
        this.alertType = alertType;
        this.isActive = isActive;
        //TODO GET CURRRENT DATE
        this.startDateTime = Timestamp.now().toDate();
        this.endDateTime = Timestamp.now().toDate();
    }

    public NotifierModel(String name, String notifierType, ArrayList<MyLatLng> locations, ArrayList<String> locationName,
                         Integer range, String alertType, Boolean isActive,
                         java.util.Date startDateTime, java.util.Date endDateTime) {
        this.name = name;
        this.notifierType = notifierType;
        this.locationCoordinates = locations;
        this.locationNames = locationName;
        this.range = range;
        this.alertType = alertType;
        this.isActive = isActive;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotifierType() {return notifierType;}

    public void setNotifierType(String notifierType) {this.notifierType = notifierType;}

    public ArrayList<MyLatLng> getLocationCoordinates() {
        return locationCoordinates;
    }

    public void setLocationCoordinates(ArrayList<MyLatLng> locationCoordinates) {
        this.locationCoordinates = locationCoordinates;
    }

    @NonNull
    public ArrayList<String> getLocationNames() {return locationNames;}
    public void setLocationNames(@NonNull ArrayList<String> locationNames) {this.locationNames = locationNames;}

    public Integer getRange() {
        return range;
    }

    public void setRange(Integer range) {
        this.range = range;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public java.util.Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(java.util.Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public java.util.Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(java.util.Date endDateTime) {
        this.endDateTime = endDateTime;
    }
}
