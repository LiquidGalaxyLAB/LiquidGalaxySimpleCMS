package com.example.simple_cms.create;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.simple_cms.R;
import com.example.simple_cms.create.utility.model.ActionIdentifier;
import com.example.simple_cms.create.utility.model.movement.Movement;
import com.example.simple_cms.create.utility.model.placemark.PlaceMark;
import com.example.simple_cms.create.utility.model.poi.POI;
import com.example.simple_cms.utility.ConstantPrefs;

/**
 * This class is in charge of getting the information of placemark action
 */
public class CreateStoryBoardActionPlaceMarkActivity extends AppCompatActivity {

    //private static final String TAG_DEBUG = "CreateStoryBoardActionPlaceMarkActivity";

    private TextView connectionStatus, imageAvailable,
            locationName, locationNameTitle;

    private Handler handler = new Handler();
    private POI poi;
    private boolean isSave = false;
    private int position = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_storyboard_action_place_mark);

        connectionStatus = findViewById(R.id.connection_status);
        imageAvailable = findViewById(R.id.image_available);
        locationName = findViewById(R.id.location_name);
        locationNameTitle = findViewById(R.id.location_name_title);

        Button buttTest = findViewById(R.id.butt_test);
        Button buttCancel = findViewById(R.id.butt_cancel);
        Button buttAdd = findViewById(R.id.butt_add);
        Button buttDelete = findViewById(R.id.butt_delete);


        Intent intent = getIntent();
        poi = intent.getParcelableExtra(ActionIdentifier.LOCATION_ACTIVITY.name());
        if(poi != null){
            setTextView();
        }

        PlaceMark placeMark = intent.getParcelableExtra(ActionIdentifier.PLACE_MARK_ACTIVITY.name());
        if(placeMark != null){
            position = intent.getIntExtra(ActionIdentifier.POSITION.name(), -1);
            isSave = true;
            buttAdd.setText(getResources().getString(R.string.button_save));
            buttDelete.setVisibility(View.VISIBLE);
            poi = placeMark.getPoi();
            setTextView();
        }


        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
        loadConnectionStatus(sharedPreferences);

    }

    /**
     * Set the information of the oldHeading and oldTilt
     */
    private void setTextView() {
        locationName.setVisibility(View.VISIBLE);
        locationNameTitle.setVisibility(View.VISIBLE);
        locationNameTitle.setText(poi.getPoiLocation().getName());
    }


    /**
     * Set the conenction status on the view
     */
    private void loadConnectionStatus(SharedPreferences sharedPreferences) {
        boolean isConnected = sharedPreferences.getBoolean(ConstantPrefs.IS_CONNECTED.name(), false);
        if(isConnected){
            connectionStatus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_status_connection_green));
            imageAvailable.setText(getResources().getString(R.string.image_available_on_screen));
        }
    }
}
