package com.example.simple_cms.create.utility.model.poi;

import com.example.simple_cms.connection.LGCommand;
import com.example.simple_cms.connection.LGConnectionManager;

public class POIController {

    //private static final String TAG_DEBUG = "POIController";

    private static POIController instance = null;
    private POI currentPOI;

    public static final POI EARTH_POI = new POI()
            .setPoiLocation(new POILocation("earth", 10.52668d, 40.085941d, 0.0d)).setPoiCamera(new POICamera(0.0d, 0.0d, 5000000.0d, "relativeToSeaFloor", 3));

    public synchronized static POIController getInstance() {
        if (instance == null)
            instance = new POIController();
        return instance;
    }

    /**
     * Enforce private constructor
     */
    private POIController() {}

    /**
     * Move the screen to the poi
     *
     * @param poi      The POI that is going to move
     * @param listener The listener of lgcommand
     * @return the lgcommand
     */
    public LGCommand moveToPOI(POI poi, LGCommand.Listener listener) {
        currentPOI = new POI(poi);
        return sendPoiToLG(listener);
    }

    public synchronized void orbit(double angle, double percentDistance) {
        //.setLongitude() [-180 to +180]: X (cos)
        //.setLatitude() [-90 to +90]: Y (sin)

        /*POI newPoi = new POI(currentPOI);
        //0.0001% of RANGE
        double STEP_XY = 0.000001;
        newPoi.setLongitude(newPoi.getLongitude() + Math.cos(angle) * percentDistance * STEP_XY * newPoi.getRange());
        while (newPoi.getLongitude() > 180) {
            newPoi.setLongitude(newPoi.getLongitude() - 360);
        }
        while (newPoi.getLongitude() < -180) {
            newPoi.setLongitude(newPoi.getLongitude() + 360);
        }

        newPoi.setLatitude(newPoi.getLatitude() - Math.sin(angle) * percentDistance * STEP_XY * newPoi.getRange());
        while (newPoi.getLatitude() > 90) {
            newPoi.setLatitude(newPoi.getLatitude() - 180);
        }
        while (newPoi.getLatitude() < -90) {
            newPoi.setLatitude(newPoi.getLatitude() + 180);
        }

        moveToPOI(newPoi, null);*/
    }

    /**
     * Create the lGCommand to send to the liquid galaxy
     *
     * @param listener The LGCommand listener
     * @return LGCommandTAG_DEBUG
     */
    private LGCommand sendPoiToLG(LGCommand.Listener listener) {
        LGCommand lgCommand = new LGCommand(buildCommand(currentPOI), LGCommand.CRITICAL_MESSAGE, (String result) -> {
            if (listener != null) {
                listener.onResponse(result);
            }
        });
        LGConnectionManager lgConnectionManager = LGConnectionManager.getInstance();
        lgConnectionManager.startConnection();
        lgConnectionManager.addCommandToLG(lgCommand);
        return lgCommand;
    }

    private static String buildCommand(POI poi) {

        POILocation poiLocation = poi.getPoiLocation();
        POICamera poiCamera = poi.getPoiCamera();

        return "echo 'flytoview=" +
                "<gx:duration>" + poiCamera.getDuration() + "</gx:duration>" +
                "<gx:flyToMode>smooth</gx:flyToMode><LookAt>" +
                "<longitude>" + poiLocation.getLongitude() + "</longitude>" +
                "<latitude>" + poiLocation.getLatitude() + "</latitude>" +
                "<altitude>" + poiLocation.getAltitude() + "</altitude>" +
                "<heading>" + poiCamera.getHeading() + "</heading>" +
                "<tilt>" + poiCamera.getTilt() + "</tilt>" +
                "<range>" + poiCamera.getRange() + "</range>" +
                "<gx:altitudeMode>" + poiCamera.getAltitudeMode() + "</gx:altitudeMode>" +
                "</LookAt>' > /tmp/query.txt";
    }
}
