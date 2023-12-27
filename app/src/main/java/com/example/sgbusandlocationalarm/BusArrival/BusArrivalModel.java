package com.example.sgbusandlocationalarm.BusArrival;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
//@JsonPropertyOrder({"odata.metadata", "BusStopCode", "author", "id", "topics", "address"})
public class BusArrivalModel {


    // CONSTRUCTOR //
    public BusArrivalModel(){}

    // PROPERTIES //
    @JsonProperty("Services")
    private ArrayList<BusArrivalDetails> busArrivalDetails;
    @JsonProperty("odata.metadata")
    private String odataMetadata;
    @JsonProperty("BusStopCode")
    private String busStopCode;

    // GETTERS, SETTERS //
    public ArrayList<BusArrivalDetails> getBusArrivalDetails() {return busArrivalDetails;}
    public String getOdataMetadata() {return odataMetadata;}
    public String getBusStopCode() {return busStopCode;}

    public static class BusArrivalDetails {
        // CONSTRUCTOR //
        public BusArrivalDetails(){}

        // PROPERTIES //
        @JsonProperty("ServiceNo")
        private String serviceNo;
        @JsonProperty("Operator")
        private String operator;
        @JsonProperty("NextBus")
        private NextBus nextBus;
        @JsonProperty("NextBus2")
        private NextBus2 nextBus2;
        @JsonProperty("NextBus3")
        private NextBus3 nextBus3;

        // GETTERS, SETTERS //

        public String getServiceNo() {
            return serviceNo;
        }
        public String getOperator() {
            return operator;
        }
        public NextBus getNextBus() {
            return nextBus;
        }
        public NextBus2 getNextBus2() {
            return nextBus2;
        }
        public NextBus3 getNextBus3() {
            return nextBus3;
        }
    }

    public static class NextBus {

        // CONSTRUCTOR //
        private NextBus(){}

        // PROPERTIES //
        @JsonProperty("OriginCode")
        private String originCode;
        @JsonProperty("DestinationCode")
        private String destinationCode;
        @JsonProperty("EstimatedArrival")
//        @JsonSerialize(using = LocalDateTimeSerializer.class)
//        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        private Date estimatedArrival;
        @JsonProperty("Latitude")
        private String latitude;
        @JsonProperty("Longitude")
        private String longitude;
        @JsonProperty("VisitNumber")
        private String visitNumber;
        @JsonProperty("Load")
        private String load;
        @JsonProperty("Feature")
        private String feature;
        @JsonProperty("Type")
        private String type;

        // GETTERS, SETTERS //

        public String getOriginCode() {
            return originCode;
        }
        public String getDestinationCode() {
            return destinationCode;
        }
        public Date getEstimatedArrival() {return estimatedArrival;}
        public String getLatitude() {
            return latitude;
        }
        public String getLongitude() {
            return longitude;
        }
        public String getVisitNumber() {
            return visitNumber;
        }
        public String getLoad() {
            return load;
        }
        public String getFeature() {
            return feature;
        }
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
    }

    public static class NextBus2 {

        // CONSTRUCTOR //
        public NextBus2(){}

        // PROPERTIES //
        @JsonProperty("OriginCode")
        private String originCode;
        @JsonProperty("DestinationCode")
        private String destinationCode;
        @JsonProperty("EstimatedArrival")
//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        private Date estimatedArrival;
        @JsonProperty("Latitude")
        private String latitude;
        @JsonProperty("Longitude")
        private String longitude;
        @JsonProperty("VisitNumber")
        private String visitNumber;
        @JsonProperty("Load")
        private String load;
        @JsonProperty("Feature")
        private String feature;
        @JsonProperty("Type")
        private String type;

        // GETTERS, SETTERS //

        public String getOriginCode() {
            return originCode;
        }

        public void setOriginCode(String originCode) {
            this.originCode = originCode;
        }

        public String getDestinationCode() {
            return destinationCode;
        }

        public void setDestinationCode(String destinationCode) {
            this.destinationCode = destinationCode;
        }

        public Date getEstimatedArrival() {
            return estimatedArrival;
        }
        public String getLatitude() {
            return latitude;
        }
        public String getLongitude() {
            return longitude;
        }
        public String getVisitNumber() {
            return visitNumber;
        }
        public String getLoad() {
            return load;
        }
        public String getFeature() {
            return feature;
        }
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
    }

    public static class NextBus3 {

        // CONSTRUCTOR //
        public NextBus3(){}

        // PROPERTIES //
        @JsonProperty("OriginCode")
        private String originCode;
        @JsonProperty("DestinationCode")
        private String destinationCode;
        @JsonProperty("EstimatedArrival")
        private Date estimatedArrival;
        @JsonProperty("Latitude")
        private String latitude;
        @JsonProperty("Longitude")
        private String longitude;
        @JsonProperty("VisitNumber")
        private String visitNumber;
        @JsonProperty("Load")
        private String load;
        @JsonProperty("Feature")
        private String feature;
        @JsonProperty("Type")
        private String type;

        // GETTERS, SETTERS //

        public Date getEstimatedArrival() {
            return estimatedArrival;
        }
        public String getLatitude() {
            return latitude;
        }
        public String getLongitude() {
            return longitude;
        }
        public String getVisitNumber() {
            return visitNumber;
        }
        public String getLoad() {
            return load;
        }
        public String getFeature() {
            return feature;
        }
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
    }
}

