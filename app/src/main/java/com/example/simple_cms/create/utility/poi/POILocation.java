package com.example.simple_cms.create.utility.poi;

/**
 * This class is in charge of saving the location parameters
 */
public class POILocation {

    private String name;
    private double latitude;
    private double longitude;
    private double altitude;


    public POILocation(String name, double longitude, double latitude, double altitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }
}
