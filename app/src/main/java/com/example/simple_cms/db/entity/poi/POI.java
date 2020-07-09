package com.example.simple_cms.db.entity.poi;

import androidx.room.Entity;

import com.example.simple_cms.db.entity.Action;

/**
 * This class represent a POI in the database
 */
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


    public static POI getPOIDBMODEL(com.example.simple_cms.create.utility.model.poi.POI action) {
        POI poi = new POI();

        poi.actionId = action.getId();
        poi.type = action.getType();
        poi.namePOI = action.getPoiLocation().getName();
        poi.latitudePOI = action.getPoiLocation().getLatitude();
        poi.longitudePOI = action.getPoiLocation().getLongitude();
        poi.altitudePOI = action.getPoiLocation().getAltitude();
        poi.headingPOI = action.getPoiCamera().getHeading();
        poi.tiltPOI = action.getPoiCamera().getTilt();
        poi.rangePOI = action.getPoiCamera().getRange();
        poi.altitudeModePOI = action.getPoiCamera().getAltitudeMode();
        poi.durationPOI = action.getPoiCamera().getDuration();

        return poi;
    }
}
