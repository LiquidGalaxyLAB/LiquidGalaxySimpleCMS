package com.lglab.diego.simple_cms.create;

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
import com.lglab.diego.simple_cms.create.utility.model.shape.Shape;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestStoryboardThread implements Runnable {

    private static final String TAG_DEBUG = "TestStoryboardThread";

    private final AtomicBoolean running = new AtomicBoolean(false);
    private List<Action> actions;
    private AppCompatActivity activity;
    private Button buttTest, buttStopTest, buttDemo, buttDemoStop;
    private final boolean isDemo;

    TestStoryboardThread(List<Action> actions, AppCompatActivity activity, Button buttTest,
                         Button buttStopTest, Button buttDemo, Button buttDemoStop, boolean isDemo){
        this.actions = actions;
        this.activity = activity;
        this.buttTest = buttTest;
        this.buttStopTest = buttStopTest;
        this.buttDemo = buttDemo;
        this.buttDemoStop = buttDemoStop;
        this.isDemo = isDemo;
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
        Action actionSend;
        ActionController actionController = ActionController.getInstance();
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
                actionController.sendBalloon(balloon, null);
                actionController.cleanFileKMLs(duration - 200);
            } else if (actionSend instanceof Shape) {
                Shape shape = (Shape) actionSend;
                duration = shape.getDuration() * 1000;
                actionController.sendShape(shape, null);
                actionController.cleanFileKMLs(duration - 200);
            }
            try {
                Log.w(TAG_DEBUG, "DURATION ACTION: " + duration);
                Thread.sleep(duration);
            } catch (Exception e) {
                Log.w(TAG_DEBUG, "ERROR: " + e.getMessage());
            }
        }
        actionController.cleanBalloonKML(500);
        actionController.cleanShapeKML(500);
        actionController.cleanFileKMLs(500);
        activity.runOnUiThread(() -> {
            if(isDemo){
                buttDemo.setVisibility(View.VISIBLE);
                buttDemoStop.setVisibility(View.INVISIBLE);
                buttTest.setClickable(true);
                buttStopTest.setClickable(true);
            }else{
                buttTest.setVisibility(View.VISIBLE);
                buttStopTest.setVisibility(View.INVISIBLE);
                buttDemo.setClickable(true);
                buttDemoStop.setClickable(true);
            }
        });
    }
}
