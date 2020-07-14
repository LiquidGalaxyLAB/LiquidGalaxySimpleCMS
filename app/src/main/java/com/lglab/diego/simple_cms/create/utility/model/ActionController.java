package com.lglab.diego.simple_cms.create.utility.model;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.lglab.diego.simple_cms.connection.LGCommand;
import com.lglab.diego.simple_cms.connection.LGConnectionManager;
import com.lglab.diego.simple_cms.connection.LGConnectionSendFile;
import com.lglab.diego.simple_cms.create.utility.model.balloon.Balloon;
import com.lglab.diego.simple_cms.create.utility.model.poi.POI;
import com.lglab.diego.simple_cms.create.utility.model.poi.POICamera;
import com.lglab.diego.simple_cms.create.utility.model.poi.POILocation;
import com.lglab.diego.simple_cms.create.utility.model.shape.Shape;

/**
 * This class is in charge of sending the commands to liquid galaxy
 */
public class ActionController {

    private static final String TAG_DEBUG = "ActionController";

    private static ActionController instance = null;
    private POI currentPOI;
    private Handler handler = new Handler(Looper.getMainLooper());

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
    private ActionController() {
    }

    /**
     * Move the screen to the poi
     *
     * @param poi      The POI that is going to move
     * @param listener The listener of lgcommand
     * @return the lgCommand
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
    public synchronized void orbit(POI poi) {
        POI newPoi = new POI(poi);
        int initHeading = (int) newPoi.getPoiCamera().getHeading();
        int distance = 0;
        int heading = initHeading;
        int angles = 20;
        try {
            Thread.sleep(3000);
        } catch (Exception e) {

        }
        while (distance < 18) {
            heading += angles;
            if (heading > 360) {
                heading = heading - 360;
            }
            newPoi.getPoiCamera().setHeading(heading);
            sendPoiToLG(newPoi, null);
            try {
                Thread.sleep(500);
            } catch (Exception e) {

            }
            distance++;
        }
        newPoi.getPoiCamera().setHeading(initHeading);
    }

    /**
     * @param balloon  Balloon with the information to build command
     * @param listener listener
     */
    public void sendBalloon(Balloon balloon, LGCommand.Listener listener) {
        Uri imageUri = balloon.getImageUri();
        if (imageUri != null) {
            createResourcesFolder(null);
            String imagePath = balloon.getImagePath();
            LGConnectionSendFile lgConnectionSendFile = LGConnectionSendFile.getInstance();
            lgConnectionSendFile.addPath(imagePath);
            lgConnectionSendFile.startConnection();
        }

        writeFileBalloonFile(null);

        currentPOI = balloon.getPoi();
        LGCommand lgCommand = new LGCommand(ActionBuildCommandUtility.buildCommandBalloonTest(balloon), LGCommand.CRITICAL_MESSAGE, (String result) -> {
            if (listener != null) {
                listener.onResponse(result);
            }
        });
        LGConnectionManager lgConnectionManager = LGConnectionManager.getInstance();
        lgConnectionManager.startConnection();
        lgConnectionManager.addCommandToLG(lgCommand);

        handler.postDelayed(() -> {
            cleanFileKMLs(null);
        }, 10000);
    }

    /**
     * Create the Resource folder
     *
     * @param listener listener
     */
    private void createResourcesFolder(LGCommand.Listener listener) {
        LGCommand lgCommand = new LGCommand(ActionBuildCommandUtility.buildCommandCreateResourcesFolder(), LGCommand.CRITICAL_MESSAGE, (String result) -> {
            if (listener != null) {
                listener.onResponse(result);
            }
        });
        LGConnectionManager lgConnectionManager = LGConnectionManager.getInstance();
        lgConnectionManager.startConnection();
        lgConnectionManager.addCommandToLG(lgCommand);
    }

    /**
     * Write the file of the balloon
     *
     * @param listener listener
     */
    private void writeFileBalloonFile(LGCommand.Listener listener) {
        LGCommand lgCommand = new LGCommand(ActionBuildCommandUtility.buildWriteBalloonFile(), LGCommand.CRITICAL_MESSAGE, (String result) -> {
            if (listener != null) {
                listener.onResponse(result);
            }
        });
        LGConnectionManager lgConnectionManager = LGConnectionManager.getInstance();
        lgConnectionManager.startConnection();
        lgConnectionManager.addCommandToLG(lgCommand);
    }


    private void writeFileShapeFile(LGCommand.Listener listener) {
        LGCommand lgCommand = new LGCommand(ActionBuildCommandUtility.buildWriteShapeFile(), LGCommand.CRITICAL_MESSAGE, (String result) -> {
            if (listener != null) {
                listener.onResponse(result);
            }
        });
        LGConnectionManager lgConnectionManager = LGConnectionManager.getInstance();
        lgConnectionManager.startConnection();
        lgConnectionManager.addCommandToLG(lgCommand);
    }

    /**
     * Send the command to liquid galaxy
     *
     * @param shape    Shape with the information to build the command
     * @param listener listener
     */
    public void sendShape(Shape shape, LGCommand.Listener listener) {
        writeFileShapeFile(null);

        LGCommand lgCommand = new LGCommand(ActionBuildCommandUtility.buildCommandSendShape(shape), LGCommand.CRITICAL_MESSAGE, (String result) -> {
            if (listener != null) {
                listener.onResponse(result);
            }
        });
        LGConnectionManager lgConnectionManager = LGConnectionManager.getInstance();
        lgConnectionManager.startConnection();
        lgConnectionManager.addCommandToLG(lgCommand);

        handler.postDelayed(() -> {
            cleanFileKMLs(null);
        }, 10000);
    }

    /**
     * It cleans the kmls.txt file
     */
    private void cleanFileKMLs(LGCommand.Listener listener) {
        LGCommand lgCommand = new LGCommand(ActionBuildCommandUtility.buildCleanKMLs(), LGCommand.CRITICAL_MESSAGE, (String result) -> {
            if (listener != null) {
                listener.onResponse(result);
            }
        });
        LGConnectionManager lgConnectionManager = LGConnectionManager.getInstance();
        lgConnectionManager.startConnection();
        lgConnectionManager.addCommandToLG(lgCommand);
    }
}
