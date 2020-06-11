package com.example.simple_cms.create;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.simple_cms.R;
import com.example.simple_cms.top_bar.TobBarActivity;
import com.example.simple_cms.utility.ConstantPrefs;

/**
 * This activity is in charge of creating the storyboards with the respective different actions
 */
public class CreateStoryBoardActivity extends TobBarActivity {

    private Button buttCreate, buttLocation, buttMovements, buttGraphics, buttShapes, buttDescription, buttCancel, buttSave;
    private TextView connectionStatus, imageAvailable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_storyboard);

        View topBar = findViewById(R.id.top_bar);
        buttCreate = topBar.findViewById(R.id.butt_create_menu);

        buttLocation = findViewById(R.id.butt_location);
        buttMovements = findViewById(R.id.butt_movements);
        buttGraphics = findViewById(R.id.butt_graphics);
        buttShapes = findViewById(R.id.butt_shapes);
        buttDescription = findViewById(R.id.butt_description);
        buttCancel = findViewById(R.id.butt_cancel);
        buttSave = findViewById(R.id.butt_save);
        connectionStatus = findViewById(R.id.connection_status);
        imageAvailable = findViewById(R.id.image_available);

        buttLocation.setOnClickListener( (view) -> {
            Intent intent = new Intent(getApplicationContext(), CreateStoryBoardActionLocationActivity.class);
            startActivity(intent);
        });

        changeButtonClickableBackgroundColor();
    }

    @Override
    protected void onResume() {
        loadData();
        super.onResume();
    }

    /**
     * Load the data
     */
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
        boolean isConnected = sharedPreferences.getBoolean(ConstantPrefs.IS_CONNECTED.name(), false);
        if(isConnected){
            connectionStatus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_status_connection_green));
            imageAvailable.setText(getResources().getString(R.string.image_available_on_screen));
        }
    }

    /**
     * Change the background color and the option clickable to false of the button_connect
     */
    private void changeButtonClickableBackgroundColor() {
        changeButtonClickableBackgroundColor(getApplicationContext(), buttCreate);
    }

}
