package com.example.simple_cms.create;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.simple_cms.R;
import com.example.simple_cms.create.utility.connection.LGConnectionTest;
import com.example.simple_cms.create.utility.model.ActionIdentifier;
import com.example.simple_cms.create.utility.model.poi.POI;
import com.example.simple_cms.create.utility.model.poi.POICamera;
import com.example.simple_cms.create.utility.model.poi.POIController;
import com.example.simple_cms.create.utility.model.poi.POILocation;
import com.example.simple_cms.dialog.CustomDialogUtility;
import com.example.simple_cms.utility.ConstantPrefs;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is in charge of getting the information of location action
 */
public class CreateStoryBoardActionLocationActivity extends AppCompatActivity {

    //private static final String TAG_DEBUG = "CreateStoryBoardActionLocationActivity";

    private EditText file_name, latitude, longitude, altitude, duration, heading, tilt, range, altitude_mode;
    private TextView connectionStatus, imageAvailable;

    private Handler handler = new Handler();
    private boolean isSave = false;
    private int position = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_storyboard_action_location);

        file_name = findViewById(R.id.file_name);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        altitude = findViewById(R.id.altitude);
        duration = findViewById(R.id.duration);
        heading = findViewById(R.id.heading);
        tilt = findViewById(R.id.tilt);
        range = findViewById(R.id.range);
        altitude_mode = findViewById(R.id.altitude_mode);
        connectionStatus = findViewById(R.id.connection_status);
        imageAvailable = findViewById(R.id.image_available);

        Button butt_test = findViewById(R.id.butt_test);
        Button butt_cancel = findViewById(R.id.butt_cancel);
        Button butt_add = findViewById(R.id.butt_add);
        Button butt__delete = findViewById(R.id.butt_delete);

        Intent intent = getIntent();
        POI poi = intent.getParcelableExtra(ActionIdentifier.LOCATION_ACTIVITY.name());
        if(poi != null){
            position = intent.getIntExtra(ActionIdentifier.POSITION.name(), -1);
            isSave = true;
            butt_add.setText(getResources().getString(R.string.button_save));
            butt__delete.setVisibility(View.VISIBLE);
            loadPoiData(poi);
        }else{
            loadData();
        }

        butt_cancel.setOnClickListener( (view) ->
            finish()
        );

        butt_test.setOnClickListener( (view) ->
            testConnection()
        );

        butt_add.setOnClickListener((view) ->
            addPOI()
        );

        butt__delete.setOnClickListener( (view) -> deletePoi());
    }

    /**
     * Charge the data for the poi
     * @param poi Poi that is going to be edit
     */
    private void loadPoiData(POI poi) {
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
        loadConnectionStatus(sharedPreferences);
        file_name.setText(poi.getPoiLocation().getName());
        latitude.setText(String.valueOf(poi.getPoiLocation().getLatitude()));
        longitude.setText(String.valueOf(poi.getPoiLocation().getLongitude()));
        altitude.setText(String.valueOf(poi.getPoiLocation().getAltitude()));
        duration.setText(String.valueOf(poi.getPoiCamera().getDuration()));
        heading.setText(String.valueOf(poi.getPoiCamera().getHeading()));
        tilt.setText(String.valueOf(poi.getPoiCamera().getTilt()));
        range.setText(String.valueOf(poi.getPoiCamera().getRange()));
        altitude_mode.setText(String.valueOf(poi.getPoiCamera().getAltitudeMode()));
    }

    /**
     * Load the data
     */
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
        loadConnectionStatus(sharedPreferences);
        file_name.setText(sharedPreferences.getString(ConstantPrefs.FILE_NAME.name(), ""));
        latitude.setText(sharedPreferences.getString(ConstantPrefs.LATITUDE.name(), ""));
        longitude.setText(sharedPreferences.getString(ConstantPrefs.LONGITUDE.name(), ""));
        altitude.setText(sharedPreferences.getString(ConstantPrefs.ALTITUDE.name(), ""));
        duration.setText(sharedPreferences.getString(ConstantPrefs.DURATION.name(), ""));
        heading.setText(sharedPreferences.getString(ConstantPrefs.HEADING.name(), ""));
        tilt.setText(sharedPreferences.getString(ConstantPrefs.TILT.name(), ""));
        range.setText(sharedPreferences.getString(ConstantPrefs.RANGE.name(), ""));
        altitude_mode.setText(sharedPreferences.getString(ConstantPrefs.ALTITUDE_MODE.name(), ""));
    }

    private void loadConnectionStatus(SharedPreferences sharedPreferences) {
        boolean isConnected = sharedPreferences.getBoolean(ConstantPrefs.IS_CONNECTED.name(), false);
        if(isConnected){
            connectionStatus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_status_connection_green));
            imageAvailable.setText(getResources().getString(R.string.image_available_on_screen));
        }
    }

    /**
     * Test the poi creation
     */
    private void testConnection() {
        String latitudeText = latitude.getText().toString();
        String longitudeText = longitude.getText().toString();
        String altitudeText = altitude.getText().toString();
        String durationText = duration.getText().toString();
        String headingText = heading.getText().toString();
        String tiltText = tilt.getText().toString();
        String rangeText = range.getText().toString();
        String altitudeModeText = altitude_mode.getText().toString();
        if(verificationData(latitudeText, longitudeText, altitudeText, durationText, headingText, tiltText, rangeText, altitudeModeText)){
            saveData(latitudeText, longitudeText, altitudeText, durationText, headingText, tiltText, rangeText, altitudeModeText);
            AtomicBoolean isConnected = new AtomicBoolean(false);
            LGConnectionTest.testPriorConnection(this, isConnected);
            SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
            handler.postDelayed(() -> {
                if(isConnected.get()){
                    POILocation poiLocation = new POILocation("Test", Double.parseDouble(latitudeText), Double.parseDouble(longitudeText), Double.parseDouble(altitudeText));
                    POICamera poiCamera = new POICamera(Double.parseDouble(headingText), Double.parseDouble(tiltText), Double.parseDouble(rangeText), altitudeModeText, Integer.parseInt(durationText));
                    POI poi = new POI().setPoiLocation(poiLocation).setPoiCamera(poiCamera);
                    POIController.getInstance().moveToPOI(poi, null);
                }else{
                    connectionStatus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_status_connection_red));
                }
                loadConnectionStatus(sharedPreferences);
                LGConnectionTest.cleanKML();
            }, 1200);
        }
    }


    /**
     * Safe the date in shared preference
     * @param latitudeText latitude
     * @param longitudeText longitude
     * @param altitudeText altitude
     * @param durationText duration
     * @param headingText heading
     * @param tiltText tilt
     * @param rangeText range
     * @param altitudeModeText altitude mode
     */
    private void saveData(String latitudeText, String longitudeText, String altitudeText, String durationText, String headingText, String tiltText, String rangeText, String altitudeModeText) {
        String fileNameText = file_name.getText().toString();
        SharedPreferences.Editor editor = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE).edit();
        editor.putString(ConstantPrefs.FILE_NAME.name(), fileNameText);
        editor.putString(ConstantPrefs.LATITUDE.name(), latitudeText);
        editor.putString(ConstantPrefs.LONGITUDE.name(), longitudeText);
        editor.putString(ConstantPrefs.ALTITUDE.name(), altitudeText);
        editor.putString(ConstantPrefs.DURATION.name(), durationText);
        editor.putString(ConstantPrefs.HEADING.name(), headingText);
        editor.putString(ConstantPrefs.TILT.name(), tiltText);
        editor.putString(ConstantPrefs.RANGE.name(), rangeText);
        editor.putString(ConstantPrefs.ALTITUDE_MODE.name(), altitudeModeText);
        editor.apply();
    }

    /**
     * Add a POI to the storyBoard
     */
    private void addPOI() {
        String latitudeText = latitude.getText().toString();
        String longitudeText = longitude.getText().toString();
        String altitudeText = altitude.getText().toString();
        String durationText = duration.getText().toString();
        String headingText = heading.getText().toString();
        String tiltText = tilt.getText().toString();
        String rangeText = range.getText().toString();
        String altitudeModeText = altitude_mode.getText().toString();
        if(verificationData(latitudeText, longitudeText, altitudeText, durationText, headingText,
                tiltText, rangeText, altitudeModeText)){
            String fileNameText = file_name.getText().toString();
            if(!fileNameText.equals("")){
                    POILocation poiLocation = new POILocation(fileNameText, Double.parseDouble(longitudeText),
                            Double.parseDouble(latitudeText), Double.parseDouble(altitudeText));
                    POICamera poiCamera = new POICamera(Double.parseDouble(headingText), Double.parseDouble(tiltText),
                            Double.parseDouble(rangeText), altitudeModeText, Integer.parseInt(durationText));
                    POI poi = new POI().setPoiLocation(poiLocation).setPoiCamera(poiCamera);
                    Intent returnInfoIntent = new Intent();
                    returnInfoIntent.putExtra(ActionIdentifier.LOCATION_ACTIVITY.name(), poi);
                    returnInfoIntent.putExtra(ActionIdentifier.IS_SAVE.name(), isSave);
                    returnInfoIntent.putExtra(ActionIdentifier.POSITION.name(), position);
                    setResult(Activity.RESULT_OK, returnInfoIntent);
                    finish();
            } else{
                CustomDialogUtility.showDialog(CreateStoryBoardActionLocationActivity.this,  getResources().getString(R.string.activity_create_location_missing_file_name));
            }
        }
    }

    /**
     * Send the information of deleting the POI selected
     */
    private void deletePoi() {
        Intent returnInfoIntent = new Intent();
        returnInfoIntent.putExtra(ActionIdentifier.POSITION.name(), position);
        returnInfoIntent.putExtra(ActionIdentifier.IS_DELETE.name(), true);
        setResult(Activity.RESULT_OK, returnInfoIntent);
        finish();
    }

    /**
     * verify the camps
     * @param latitudeText latitude
     * @param longitudeText longitude
     * @param altitudeText altitude
     * @param durationText duration
     * @param headingText heading
     * @param tiltText tilt
     * @param rangeText range
     * @param altitudeModeText altitude mode
     * @return false if a camp is empty
     */
    private boolean verificationData(String latitudeText, String longitudeText, String altitudeText, String durationText, String headingText, String tiltText, String rangeText, String altitudeModeText) {
        if(latitudeText.equals("")){
            CustomDialogUtility.showDialog(CreateStoryBoardActionLocationActivity.this,  getResources().getString(R.string.activity_create_location_missing_latitude_field_error));
            return false;
        }
        if(longitudeText.equals("")){
            CustomDialogUtility.showDialog(CreateStoryBoardActionLocationActivity.this,  getResources().getString(R.string.activity_create_location_missing_longitude_field_error));
            return false;
        }
        if(altitudeText.equals("")){
            CustomDialogUtility.showDialog(CreateStoryBoardActionLocationActivity.this,
                    getResources().getString(R.string.activity_create_location_missing_altitude_field_error));
            return false;
        }
        if(durationText.equals("")){
            CustomDialogUtility.showDialog(CreateStoryBoardActionLocationActivity.this,
                    getResources().getString(R.string.activity_create_location_missing_duration_field_error));
            return false;
        }
        if(headingText.equals("")){
            CustomDialogUtility.showDialog(CreateStoryBoardActionLocationActivity.this,
                    getResources().getString(R.string.activity_create_location_missing_heading_field_error));
            return false;
        }
        if(tiltText.equals("")){
            CustomDialogUtility.showDialog(CreateStoryBoardActionLocationActivity.this,
                    getResources().getString(R.string.activity_create_location_missing_tilt_field_error));
            return false;
        }
        if(rangeText.equals("")){
            CustomDialogUtility.showDialog(CreateStoryBoardActionLocationActivity.this,
                    getResources().getString(R.string.activity_create_location_missing_range_field_error));
            return false;
        }
        if(altitudeModeText.equals("")){
            CustomDialogUtility.showDialog(CreateStoryBoardActionLocationActivity.this,
                    getResources().getString(R.string.activity_create_location_missing_altitude_mode_field_error));
            return false;
        }
        return  true;
    }

}
