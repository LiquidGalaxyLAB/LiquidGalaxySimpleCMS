package com.example.simple_cms.create.utility.model;


import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.example.simple_cms.create.utility.model.balloon.Balloon;
import com.example.simple_cms.create.utility.model.poi.POI;
import com.example.simple_cms.create.utility.model.poi.POICamera;
import com.example.simple_cms.create.utility.model.poi.POILocation;

public class ActionBuildCommandUtility {

    private static String TEST_PLACE_MARK_ID = "testPlaceMark12345";
    private static String BASE_PATH = "/var/www/html/";
    private static String IMAGE_FOLDER_NAME = "image/";
    public static String IMAGE_PATH = BASE_PATH + IMAGE_FOLDER_NAME;


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


    static String buildCommandBalloonNetworkLink() {
        return "echo '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
                "<NetworkLink>\n" +
                " <name>balloon.kml</name>\n" +
                " <Link>\n" +
                " <href>/var/www/html/balloon.kml</href>\n" +
                " </Link>\n" +
                "</NetworkLink>\n" +
                "</kml>' > " +
                BASE_PATH +
                "balloonNetworkLinkKml.kml";
    }


    static String buildCommandBalloonTest(Balloon balloon) {

        POI poi = balloon.getPoi();

        return "echo '" +
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
                "\n" +
                "        <h5>" + balloon.getDescription() + "</h3>\n" +
                "        <br>\n" +
                /*"        <img src=\"/var/www/html/img/" +  balloon.getImageUri() + *//*" width=\"40\" height=\"40\*//*"\">"  +*/
                /*"        <video src=" + balloon.getVideoUri() + "width=\"60\" height=\"40\" autoplay muted loop></video>" +*/
                "\n" +
                "    </div>\n" +
                "\n" +
                "    <script src=\"https://code.jquery.com/jquery-3.2.1.slim.min.js\" integrity=\"sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN\" crossorigin=\"anonymous\"></script>\n" +
                "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js\" integrity=\"sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q\" crossorigin=\"anonymous\"></script>\n" +
                "    <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js\" integrity=\"sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl\" crossorigin=\"anonymous\"></script>\n" +
                "  </body>\n" +
                "]]>" +
                "    </description>\n" +
                "    <gx:balloonVisibility>0</gx:balloonVisibility>\n" +
                "    <Point>\n" +
                "      <coordinates>" + poi.getPoiLocation().getLongitude() + "," + poi.getPoiLocation().getLatitude() + "</coordinates>\n" +
                "    </Point>\n" +
                "  </Placemark>\n" +
                "</Document>\n" +
                "</kml>" +
                "' > " +
                BASE_PATH +
                "balloon.kml";
    }

    static String buildCommandBalloonUpdateNetworkLink() {
        return "echo '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
                "<NetworkLink>\n" +
                " <name>Update</name>\n" +
                " <Link>\n" +
                " <href>/var/www/html/balloonUpdate.kml</href>" +
                "</Link> </NetworkLink>\n" +
                "</kml>' > " +
                BASE_PATH +
                "balloonUpdateNetworkLinkKml.kml";
    }


    static String buildCommandChangeBalloon(Balloon balloon) {

        POI poi = balloon.getPoi();

        return "echo '" +
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\"\n" +
                " xmlns:gx=\"http://www.google.com/kml/ext/2.2\">\n" +
                "\n" +
                " <NetworkLinkControl>\n" +
                " <Update>\n" +
                " <targetHref>/var/www/html/balloon.kml</targetHref>\n" +
                " <Change>\n" +
                "  <Placemark id=\"" + TEST_PLACE_MARK_ID + "\">\n" +
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
                "\n" +
                "        <h5>" + balloon.getDescription() + "</h3>\n" +
                "        <br>\n" +
                /*"        <img src=\"/var/www/html/img/" +  balloon.getImageUri() + *//*" width=\"40\" height=\"40\*//*"\">"  +*/
                /*"        <video src=" + balloon.getVideoUri() + "width=\"60\" height=\"40\" autoplay muted loop></video>" +*/
                "\n" +
                "    </div>\n" +
                "\n" +
                "    <script src=\"https://code.jquery.com/jquery-3.2.1.slim.min.js\" integrity=\"sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN\" crossorigin=\"anonymous\"></script>\n" +
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
                "\n" +
                "</Change> </Update>\n" +
                "</NetworkLinkControl>" +
                "</kml>" +
                "' > " +
                BASE_PATH +
                "balloonUpdate.kml";
    }

    static String buildCommandCreateFolder() {
        return "mkdir -p " + IMAGE_PATH;
    }
}
