package com.lglab.diego.simple_cms.create.utility.model;


import android.util.Log;

import com.lglab.diego.simple_cms.create.utility.model.balloon.Balloon;
import com.lglab.diego.simple_cms.create.utility.model.poi.POI;
import com.lglab.diego.simple_cms.create.utility.model.poi.POICamera;
import com.lglab.diego.simple_cms.create.utility.model.poi.POILocation;
import com.lglab.diego.simple_cms.create.utility.model.shape.Point;
import com.lglab.diego.simple_cms.create.utility.model.shape.Shape;

import java.util.List;

/**
 * This class is in charge of creating the commands that are going to be send to the liquid galaxy
 */
public class ActionBuildCommandUtility {

    private static final String TAG_DEBUG = "ActionBuildCommandUtility";

    private static String BASE_PATH = "/var/www/html/";
    public static String RESOURCES_FOLDER_PATH = BASE_PATH + "resources/";


    /**
     * Build the command to fly to the position
     * @param poi POI with the information
     * @return String with the command
     */
    static String buildCommandPOITest(POI poi) {

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


    /**
     * @return Command to create the resources fule
     */
    static String buildCommandCreateResourcesFolder() {
        return "mkdir -p " + RESOURCES_FOLDER_PATH;
    }

    /**
     * @return Command to write the path to the balloon.kml
     */
    static String buildWriteBalloonFile() {
        String command = "echo \"http://localhost:81/balloon.kml\"  > " +
                BASE_PATH +
                "kmls.txt";
        Log.w(TAG_DEBUG, "command: " + command);
        return command;
    }

    /**
     * Build the command to paint a balloon in Liquid Galaxy
     * @param balloon Balloon with the information to be send
     * @return String with command
     */
    static String buildCommandBalloonTest(Balloon balloon) {

        POI poi = balloon.getPoi();

        String TEST_PLACE_MARK_ID = "testPlaceMark12345";
        String startCommand =  "echo '" +
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\"\n" +
                " xmlns:gx=\"http://www.google.com/kml/ext/2.2\">\n" +
                "\n" +
                " <Document>\n" +
                " <Placemark id=\"" + TEST_PLACE_MARK_ID + "\">\n" +
                "    <name>" + balloon.getPoi().getPoiLocation().getName() + "</name>\n" +
                "    <description>\n" +
                "<![CDATA[\n" +
                "  <head>\n" +
                "    <!-- Required meta tags -->\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" +
                "\n" +
                "    <!-- Bootstrap CSS -->\n" +
                "    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">\n" +
                "\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div class=\"p-lg-5\" align=\"center\">\n" +
                "\n";
                String description = "";
                if(!balloon.getDescription().equals("")) {
                    description = "        <h5>" + balloon.getDescription() + "</h5>\n" +
                            "        <br>\n";
                }
                String imageCommand = "";

                if(balloon.getImagePath() != null && !balloon.getImagePath().equals("")) {
                    imageCommand =  "        <img src=\"./resources/" + getFileName(balloon.getImagePath()) + "\" width=\"80\" height=\"60\"> \n" +
                            "        <br>\n";
                }
                String videoCommand = "";
                if(balloon.getVideoPath() != null && !balloon.getVideoPath().equals("")) {
                    videoCommand = "<iframe width=\"80\" height=\"60\"" +
                            " src=\""+ balloon.getVideoPath() + "\" frameborder=\"0\"" +
                            " allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen>" +
                            "</iframe>";
                }
                String endCommand = "    </div>\n    <script src=\"https://code.jquery.com/jquery-3.2.1.slim.min.js\" integrity=\"sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN\" crossorigin=\"anonymous\"></script>\n" +
                "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js\" integrity=\"sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q\" crossorigin=\"anonymous\"></script>\n" +
                "    <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js\" integrity=\"sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl\" crossorigin=\"anonymous\"></script>\n" +
                "  </body>\n" +
                "]]>" +
                "    </description>\n" +
                "    <gx:balloonVisibility>1</gx:balloonVisibility>\n" +
                "    <Point>\n" +
                "      <coordinates>" + poi.getPoiLocation().getLongitude() + "," + poi.getPoiLocation().getLatitude() + "</coordinates>\n" +
                "    </Point>\n" +
                "  </Placemark>\n" +
                "</Document>\n" +
                "</kml>" +
                "' > " +
                BASE_PATH +
                "balloon.kml";
                Log.w(TAG_DEBUG, startCommand + description +  imageCommand + videoCommand + endCommand);
                return startCommand + description + imageCommand + videoCommand + endCommand;
    }

    /**
     * Get the absolute path of the file
     * @param filePath The path of the file
     * @return the absolute path
     */
    private static String getFileName(String filePath) {
        String[] route = filePath.split("/");
        return route[route.length - 1];
    }


    /**
     * @return Command to write the path to the shape.kml
     */
    static String buildWriteShapeFile() {
        String command = "echo \"http://localhost:81/shape.kml\"  > " +
                BASE_PATH +
                "kmls.txt";
        Log.w(TAG_DEBUG, "command: " + command);
        return command;
    }

    /**
     * Build the command to paint the shape in liquid galaxy
     * @param shape Shape with the information
     * @return Command to paint the shape in Liquid Galaxy
     */
    static String buildCommandSendShape(Shape shape) {
        StringBuilder command = new StringBuilder();
        command.append("echo '").append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                .append("<kml xmlns=\"http://www.opengis.net/kml/2.2\"> <Placemark>\n")
                .append("<name>").append(shape.getPoi().getPoiLocation().getName()).append("</name>\n").append("<LineString>\n");
        if(shape.isExtrude()) command.append("<extrude>1</extrude>\n");
        command.append("<tessellate>1</tessellate>\n").append("<altitudeMode>absolute</altitudeMode>\n")
                .append("<coordinates>\n");
        List<Point> points = shape.getPoints();
        int pointsLength = points.size();
        Point point;
        for(int i = 0; i < pointsLength; i++){
            point = points.get(i);
            command.append("    ").append(point.getLongitude()).append(",").append(point.getLatitude())
                    .append(",").append(point.getAltitude()).append("\n");
        }
        command.append("</coordinates>\n").append("</LineString>\n").append("</Placemark>\n</kml> " + "' > ")
                .append(BASE_PATH).append("shape.kml");
        Log.w(TAG_DEBUG, "Command: " + command.toString());
        return  command.toString();
    }

    /**
     * @return Command to clean the kmls.txt
     */
    static String buildCleanKMLs() {
        String command = "echo '' > " +
                BASE_PATH +
                "kmls.txt";
        Log.w(TAG_DEBUG, "command: " + command);
        return command;
    }
}
