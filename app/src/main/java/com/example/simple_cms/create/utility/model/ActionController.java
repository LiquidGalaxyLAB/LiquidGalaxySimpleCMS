package com.example.simple_cms.create.utility.model;

import android.content.SharedPreferences;
import android.net.Uri;

import com.example.simple_cms.connection.LGCommand;
import com.example.simple_cms.connection.LGConnectionManager;
import com.example.simple_cms.create.utility.model.balloon.Balloon;
import com.example.simple_cms.create.utility.model.poi.POI;
import com.example.simple_cms.create.utility.model.poi.POICamera;
import com.example.simple_cms.create.utility.model.poi.POILocation;

public class ActionController {

    private static final String TAG_DEBUG = "POIController";

    private static ActionController instance = null;
    private POI currentPOI;

    public static final POI EARTH_POI = new POI()
            .setPoiLocation(new POILocation("earth", 10.52668d, 40.085941d, 0.0d)).setPoiCamera(new POICamera(0.0d, 0.0d, 5000000.0d, "relativeToSeaFloor", 3));

    public synchronized static ActionController getInstance() {
        if (instance == null)
            instance = new ActionController();
        return instance;
    }

    /**
     * Enforce private constructor
     */
    private ActionController() {}

    /**
     * Move the screen to the poi
     *
     * @param poi      The POI that is going to move
     * @param listener The listener of lgcommand
     * @return the lgcommand
     */
    public LGCommand moveToPOI(POI poi, LGCommand.Listener listener) {
        currentPOI = new POI(poi);
        return sendPoiToLG(poi, listener);
    }

    /**
     * Create the lGCommand to send to the liquid galaxy
     *
     * @param listener The LGCommand listener
     * @return LGCommand
     */
    private LGCommand sendPoiToLG(POI poi, LGCommand.Listener listener) {
        LGCommand lgCommand = new LGCommand(ActionBuildCommandUtility.buildCommandPOITest(poi), LGCommand.CRITICAL_MESSAGE, (String result) -> {
            if (listener != null) {
                listener.onResponse(result);
            }
        });
        LGConnectionManager lgConnectionManager = LGConnectionManager.getInstance();
        lgConnectionManager.startConnection();
        lgConnectionManager.addCommandToLG(lgCommand);
        return lgCommand;
    }


    /**
     * Create the orbit around the point
     */
    public synchronized void orbit() {
        POI newPoi = new POI(currentPOI);
        int initHeading = (int) newPoi.getPoiCamera().getHeading();
        int recorrido = 0;
        int heading = 0;
        int angles = 20;
        try{
            Thread.sleep(3000);
        }catch(Exception e){

        }
        while(recorrido < 18){
            heading += angles + initHeading;
            if(heading >= 360){
                heading = heading - 360;
            }
            newPoi.getPoiCamera().setHeading(heading);
            sendPoiToLG(newPoi, null);
            try{
                Thread.sleep(2000);
            }catch(Exception e){

            }
            recorrido++;
        }
        newPoi.getPoiCamera().setHeading(initHeading);
    }

    public void sendNetworkLink(LGCommand.Listener listener){
        LGCommand lgCommand = new LGCommand(ActionBuildCommandUtility.buildCommandBalloonNetworkLink(), LGCommand.CRITICAL_MESSAGE, (String result) -> {
            if (listener != null) {
                listener.onResponse(result);
            }
        });
        LGConnectionManager lgConnectionManager = LGConnectionManager.getInstance();
        lgConnectionManager.startConnection();
        lgConnectionManager.addCommandToLG(lgCommand);
    }

    public void sendBalloon(Balloon balloon, LGCommand.Listener listener){
        currentPOI = balloon.getPoi();
        LGCommand lgCommand = new LGCommand(ActionBuildCommandUtility.buildCommandBalloonTest(balloon), LGCommand.CRITICAL_MESSAGE, (String result) -> {
            if (listener != null) {
                listener.onResponse(result);
            }
        });
        LGConnectionManager lgConnectionManager = LGConnectionManager.getInstance();
        lgConnectionManager.startConnection();
        lgConnectionManager.addCommandToLG(lgCommand);
    }

    public void sendNetworkLinkUpdate(LGCommand.Listener listener){
        LGCommand lgCommand = new LGCommand(ActionBuildCommandUtility.buildCommandBalloonUpdateNetworkLink(), LGCommand.CRITICAL_MESSAGE, (String result) -> {
            if (listener != null) {
                listener.onResponse(result);
            }
        });
        LGConnectionManager lgConnectionManager = LGConnectionManager.getInstance();
        lgConnectionManager.startConnection();
        lgConnectionManager.addCommandToLG(lgCommand);
    }

    public void sendChangeBallon(Balloon balloon, LGCommand.Listener listener){
        currentPOI = balloon.getPoi();
        LGCommand lgCommand = new LGCommand(ActionBuildCommandUtility.buildCommandChangeBalloon(balloon), LGCommand.CRITICAL_MESSAGE, (String result) -> {
            if (listener != null) {
                listener.onResponse(result);
            }
        });
        LGConnectionManager lgConnectionManager = LGConnectionManager.getInstance();
        lgConnectionManager.startConnection();
        lgConnectionManager.addCommandToLG(lgCommand);
    }

    public void sendCreateImageFolder(LGCommand.Listener listener) {
        LGCommand lgCommand = new LGCommand(ActionBuildCommandUtility.buildCommandCreateFolder(), LGCommand.CRITICAL_MESSAGE, (String result) -> {
            if (listener != null) {
                listener.onResponse(result);
            }
        });
        LGConnectionManager lgConnectionManager = LGConnectionManager.getInstance();
        lgConnectionManager.startConnection();
        lgConnectionManager.addCommandToLG(lgCommand);
    }

    public void sendBalloonImage(SharedPreferences sharedPreferences, Uri imageUri, LGCommand.Listener listener) {
        LGCommand lgCommand = new LGCommand(ActionBuildCommandUtility.buildCommandBalloonImage(sharedPreferences, imageUri), LGCommand.CRITICAL_MESSAGE, (String result) -> {
            if (listener != null) {
                listener.onResponse(result);
            }
        });
        LGConnectionManager lgConnectionManager = LGConnectionManager.getInstance();
        lgConnectionManager.startConnection();
        lgConnectionManager.addCommandToLG(lgCommand);
    }
}
