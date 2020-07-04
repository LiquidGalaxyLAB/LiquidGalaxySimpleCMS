package com.example.simple_cms.db.entity.poi;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.simple_cms.db.entity.Action;

@Entity
public class POI extends Action {

    public String namePOI;
    public double latitudePOI;
    public double longitudePOI;
    public double altitudePOI;
    public double headingPOI;
    public double tiltPOI;
    public double rangePOI;
    public String altitudeModePOI;
    public int durationPOI;
}
