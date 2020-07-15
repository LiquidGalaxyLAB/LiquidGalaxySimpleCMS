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
    private List<List<Action>> listActions;

    public static TestStoryboardThread getInstance() {
        if (instance == null) {
            instance = new TestStoryboardThread();
        }
        return instance;
    }

    void startConnection(List<List<Action>> listActions) {
        this.listActions = listActions;
        new Thread(instance).start();
    }


    @Override
    public void run() {
        Action actionSend;
        ActionController actionController = ActionController.getInstance();
        int durationStay = 0;
        int duration = 3;
        for(int i = 0; i < listActions.size(); i++) {
            List<Action> subActionsSend = listActions.get(i);
            for (int l = 0; l < subActionsSend.size(); l++) {
                actionSend = subActionsSend.get(l);
                if (actionSend instanceof POI) {
                    POI poi = (POI) actionSend;
                    durationStay = poi.getPoiCamera().getDuration()*1000;
                    duration = 15000;
                    actionController.moveToPOI(poi, null);
                } else if (actionSend instanceof Movement) {
                    Movement movement = (Movement) actionSend;
                    POI poi = movement.getPoi();
                    if (movement.isOrbitMode()) {
                        duration = 12000;
                        actionController.orbit(poi);
                    } else {
                        duration = 3000;
                        POICamera poiCamera = poi.getPoiCamera();
                        poiCamera.setHeading(movement.getNewHeading());
                        poiCamera.setTilt(movement.getNewTilt());
                        poi.setPoiCamera(poiCamera);
                        actionController.moveToPOI(poi, null);
                    }
                } else if (actionSend instanceof Balloon) {
                    Balloon balloon = (Balloon) actionSend;
                    duration = durationStay;
                    actionController.sendBalloon(balloon, null, durationStay);
                } else if (actionSend instanceof Shape) {
                    Shape shape = (Shape) actionSend;
                    duration = durationStay;
                    actionController.sendShape(shape, null, durationStay);
                }
                try {
                    Log.w(TAG_DEBUG, "DURATION ACTION: " + duration);
                    Thread.sleep(duration);
                } catch (Exception e) {
                    Log.w(TAG_DEBUG, "ERROR: " + e.getMessage());
                }
            }
            try {
                Log.w(TAG_DEBUG, "DURATION STAY: " + durationStay);
                Thread.sleep(durationStay);
            } catch (Exception e) {
                Log.w(TAG_DEBUG, "ERROR: " + e.getMessage());
            }
        }
    }
}
