package com.example.sgbusandlocationalarm.BusArrival;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIgnoreProperties
@JsonIgnoreProperties(ignoreUnknown=true)
public class BusStopModel {

    // CONSTRUCTORS //
    public BusStopModel(){}

    // PROPERTIES //
    @JsonProperty("odata.metadata")
    private String odataMetadata;
    @JsonProperty("value")
    private ArrayList<BusStopDetails> busStopDetails;

    // GETTERS, SETTERS //
    public String getOdataMetadata() {return odataMetadata;}
    public ArrayList<BusStopDetails> getBusStopDetails() {return busStopDetails;}

    // If the nested class does not access any of the variables of the enclosing class,
    // it can be made static. The advantage of this is that you do not need an
    // enclosing instance of the outer class to use the nested class.
    public static class BusStopDetails{

        // CONSTRUCTORS //
        public BusStopDetails(){}

        private boolean isExpandable = false;

        // PROPERTIES //
        @JsonProperty("BusStopCode")
        private String busStopCode;
        @JsonProperty("RoadName")
        private String roadName;
        @JsonProperty("Description")
        private String description;
        @JsonProperty("Latitude")
        private double latitude;
        @JsonProperty("Longitude")
        private double longitude;

        // GETTERS, SETTERS //
        public String getBusStopCode() {return busStopCode;}
        public String getRoadName() {return roadName;}
        public String getDescription() {return description;}
        public double getLatitude() {return latitude;}
        public double getLongitude() {return longitude;}
        public boolean isExpandable() {return isExpandable;}
        public void setExpandable(boolean expandable) {isExpandable = expandable;}
    }

}
