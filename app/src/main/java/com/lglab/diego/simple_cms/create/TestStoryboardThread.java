package com.lglab.diego.simple_cms.create;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.lglab.diego.simple_cms.create.utility.model.Action;
import com.lglab.diego.simple_cms.create.utility.model.ActionController;
import com.lglab.diego.simple_cms.create.utility.model.balloon.Balloon;
import com.lglab.diego.simple_cms.create.utility.model.movement.Movement;
import com.lglab.diego.simple_cms.create.utility.model.poi.POI;
import com.lglab.diego.simple_cms.create.utility.model.poi.POICamera;
import com.lglab.diego.simple_cms.create.utility.model.poi.POILocation;
import com.lglab.diego.simple_cms.create.utility.model.shape.Shape;
import com.lglab.diego.simple_cms.dialog.CustomDialogUtility;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestStoryboardThread implements Runnable {

    private static final String TAG_DEBUG = "TestStoryboardThread";

    private final AtomicBoolean running = new AtomicBoolean(false);
    private List<Action> actions;
    private AppCompatActivity activity;
    private Button buttTest, buttStopTest;
    private Dialog dialog;


    TestStoryboardThread(List<Action> actions, AppCompatActivity activity, Button buttTest,
                         Button buttStopTest, Dialog dialog){
        this.actions = actions;
        this.activity = activity;
        this.buttTest = buttTest;
        this.buttStopTest = buttStopTest;
        this.dialog = dialog;
    }

    void start() {
        Thread worker = new Thread(this);
        worker.start();
    }

    void stop() {
        running.set(false);
    }

    @Override
    public void run() {
        running.set(true);
        ActionController actionController = ActionController.getInstance();
        passingAllImages(actionController);
        dialog.dismiss();
        Action actionSend;
        int duration = 4000;
        for (int i = 0; i < actions.size() && running.get(); i++) {
            actionSend = actions.get(i);
            if (actionSend instanceof POI) {
                POI poi = (POI) actionSend;
                duration = poi.getPoiCamera().getDuration() * 1000;
                actionController.moveToPOI(poi, null);
            } else if (actionSend instanceof Movement) {
                Movement movement = (Movement) actionSend;
                POI poi = movement.getPoi();
                if (movement.isOrbitMode()) {
                    actionController.orbit(poi, null);
                } else {
                    POICamera camera = poi.getPoiCamera();
                    POICamera poiCamera = new POICamera(camera.getHeading(), camera.getTilt(), camera.getRange(), camera.getAltitudeMode(), camera.getDuration());
                    POI poiSend = new POI();
                    poiCamera.setHeading(movement.getNewHeading());
                    poiCamera.setTilt(movement.getNewTilt());
                    poiSend.setPoiCamera(poiCamera);
                    poiSend.setPoiLocation(poi.getPoiLocation());
                    actionController.moveToPOI(poiSend, null);
                }
                duration = movement.getDuration() * 1000;
            } else if (actionSend instanceof Balloon) {
                Balloon balloon = (Balloon) actionSend;
                duration = balloon.getDuration() * 1000;
                actionController.sendBalloonTestStoryBoard(balloon, null);
            } else if (actionSend instanceof Shape) {
                Shape shape = (Shape) actionSend;
                duration = shape.getDuration() * 1000;
                actionController.sendShape(shape, null);
            }
            try {
                Log.w(TAG_DEBUG, "DURATION ACTION: " + duration);
                Thread.sleep(duration);
            } catch (Exception e) {
                Log.w(TAG_DEBUG, "ERROR: " + e.getMessage());
            }
        }
        actionController.cleanFileKMLs(1000);
        activity.runOnUiThread(() -> {
                buttTest.setVisibility(View.VISIBLE);
                buttStopTest.setVisibility(View.INVISIBLE);
        });
    }

    private void passingAllImages(ActionController actionController) {
        actionController.createResourcesFolder();
        Action action;
        for (int i = 0; i < actions.size() && running.get(); i++) {
            action = actions.get(i);
            if (action instanceof Balloon) {
                Balloon balloon = (Balloon) action;
                actionController.sendImageTestStoryboard(balloon);
            }
        }
    }
}
