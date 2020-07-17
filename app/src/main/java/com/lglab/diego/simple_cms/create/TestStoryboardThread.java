package com.lglab.diego.simple_cms.create;

import android.util.Log;

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
        int duration = 4000;
        for (int i = 0; i < actions.size(); i++) {
            actionSend = actions.get(i);
            if (actionSend instanceof POI) {
                POI poi = (POI) actionSend;
                duration = poi.getPoiCamera().getDuration() * 1000;
                actionController.moveToPOI(poi, null);
            } else if (actionSend instanceof Movement) {
                Movement movement = (Movement) actionSend;
                POI poi = movement.getPoi();
                if (movement.isOrbitMode()) {
                    actionController.orbit(poi);
                } else {
                    POICamera poiCamera = poi.getPoiCamera();
                    poiCamera.setHeading(movement.getNewHeading());
                    poiCamera.setTilt(movement.getNewTilt());
                    poi.setPoiCamera(poiCamera);
                    actionController.moveToPOI(poi, null);
                }
                duration = movement.getDuration() * 1000;
            } else if (actionSend instanceof Balloon) {
                Balloon balloon = (Balloon) actionSend;
                duration = balloon.getDuration() * 1000;
                actionController.sendBalloon(balloon, null, duration);
            } else if (actionSend instanceof Shape) {
                Shape shape = (Shape) actionSend;
                duration = shape.getDuration() * 1000;
                actionController.sendShape(shape, null, duration);
            }
            try {
                Log.w(TAG_DEBUG, "DURATION ACTION: " + duration);
                Thread.sleep(duration);
            } catch (Exception e) {
                Log.w(TAG_DEBUG, "ERROR: " + e.getMessage());
            }
        }
    }
}
