package com.lglab.diego.simple_cms.create;

import android.util.Log;

import com.lglab.diego.simple_cms.connection.LGConnectionManager;
import com.lglab.diego.simple_cms.create.utility.model.Action;
import com.lglab.diego.simple_cms.create.utility.model.ActionController;
import com.lglab.diego.simple_cms.create.utility.model.balloon.Balloon;
import com.lglab.diego.simple_cms.create.utility.model.movement.Movement;
import com.lglab.diego.simple_cms.create.utility.model.poi.POI;
import com.lglab.diego.simple_cms.create.utility.model.poi.POICamera;
import com.lglab.diego.simple_cms.create.utility.model.shape.Shape;

import java.util.List;

public class TestStoryboardThread implements Runnable {

    private static final String TAG_DEBUG = "TestStoryboardThread";

    private static TestStoryboardThread instance = null;
    private List<Action> actions;

    public static TestStoryboardThread getInstance() {
        if (instance == null) {
            instance = new TestStoryboardThread();
        }
        return instance;
    }

    void startConnection(List<Action> actions) {
        this.actions = actions;
        new Thread(instance).start();
    }


    @Override
    public void run() {
        Action actionSend;
        ActionController actionController = ActionController.getInstance();
        int duration = 3;
        for (int l = 0; l < actions.size(); l++) {
            actionSend = actions.get(l);
            if (actionSend instanceof POI) {
                POI poi = (POI) actionSend;
                duration = 15000;
                actionController.moveToPOI(poi, null);
            } else if (actionSend instanceof Movement) {
                Movement movement = (Movement) actionSend;
                POI poi = movement.getPoi();
                if(movement.isOrbitMode()){
                    duration = 14000;
                    actionController.orbit(poi);
                } else{
                    duration = 3000;
                    POICamera poiCamera = poi.getPoiCamera();
                    poiCamera.setHeading(movement.getNewHeading());
                    poiCamera.setTilt(movement.getNewTilt());
                    poi.setPoiCamera(poiCamera);
                    actionController.moveToPOI(poi, null);
                }
            } else if (actionSend instanceof Balloon) {
                Balloon balloon = (Balloon) actionSend;
                duration = 10000;
                actionController.sendBalloon(balloon, null);
            } else if (actionSend instanceof Shape) {
                Shape shape = (Shape) actionSend;
                duration = 5000;
                actionController.sendShape(shape, null);
            }
            try {
                Log.w(TAG_DEBUG, "DURATION AVG: " + duration);
                Thread.sleep(duration);
            } catch (Exception e) {
                Log.w(TAG_DEBUG, "ERROR: " + e.getMessage());
            }
        }
    }
}
