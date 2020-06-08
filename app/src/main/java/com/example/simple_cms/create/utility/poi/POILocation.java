package com.example.simple_cms.create.utility.poi;

/**
 * This class is in charge of saving the location parameters
 */
public class POILocation {

    private String name;
    private double latitude;
    private double longitude;


    public POILocation(String name, double longitude, double latitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
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
}
