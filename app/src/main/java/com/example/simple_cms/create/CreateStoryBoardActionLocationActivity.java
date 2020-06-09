package com.example.simple_cms.create;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.simple_cms.R;
import com.example.simple_cms.create.utility.poi.POI;
import com.example.simple_cms.create.utility.poi.POIController;
import com.example.simple_cms.dialog.CustomDialog;
import com.example.simple_cms.dialog.CustomDialogUtility;

public class CreateStoryBoardActionLocationActivity extends AppCompatActivity {

    private EditText latitude, longitude, file_name, altitude;
    private Button butt_test, butt_cancel, butt_add;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_storyboard_action_location);

        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        file_name = findViewById(R.id.longitude);
        altitude = findViewById(R.id.altitude);

        butt_test = findViewById(R.id.butt_test);
        butt_cancel = findViewById(R.id.butt_cancel);

        butt_cancel.setOnClickListener( (view) -> {
            finish();
        });

        butt_test.setOnClickListener( (view) -> {
            String latitudeText = latitude.getText().toString();
            String longitudeText = longitude.getText().toString();
            String fileNameText = file_name.getText().toString();
            if(verificationData(latitudeText, longitudeText, fileNameText)){
                POI poi = new POI();
                POIController.getInstance().moveToPOI(POIController.EARTH_POI, null);
            }else{
                CustomDialogUtility.showDialog(CreateStoryBoardActionLocationActivity.this,  getResources().getString(R.string.activity_create_location_missing_field_error));
            }
        });

    }

    private boolean verificationData(String latitudeText, String longitudeText, String fileNameText) {
        if (latitudeText.equals("") || longitudeText.equals("") || fileNameText.equals("")) return false;
        return true;
    }
}
