package com.example.simple_cms.db.entity.poi;

public class SimplePOI {

    public long simplePOIId;
    public String namePOI;
    public double latitudePOI;
    public double longitudePOI;
    public double altitudePOI;
    public double headingPOI;
    public double tiltPOI;
    public double rangePOI;
    public String altitudeModePOI;
    public int durationPOI;


    public static SimplePOI getSIMPLEPOIDBMODEL(com.example.simple_cms.create.utility.model.poi.POI poi) {
        SimplePOI simplePOI = new SimplePOI();
        simplePOI.simplePOIId = poi.getId();
        simplePOI.namePOI = poi.getPoiLocation().getName();
        simplePOI.latitudePOI = poi.getPoiLocation().getLatitude();
        simplePOI.longitudePOI = poi.getPoiLocation().getLongitude();
        simplePOI.altitudePOI = poi.getPoiLocation().getAltitude();
        simplePOI.headingPOI = poi.getPoiCamera().getHeading();
        simplePOI.tiltPOI = poi.getPoiCamera().getTilt();
        simplePOI.rangePOI = poi.getPoiCamera().getRange();
        simplePOI.altitudeModePOI = poi.getPoiCamera().getAltitudeMode();
        simplePOI.durationPOI = poi.getPoiCamera().getDuration();
        return simplePOI;
    }
}
