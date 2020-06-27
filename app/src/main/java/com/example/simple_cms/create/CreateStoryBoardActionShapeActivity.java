package com.example.simple_cms.create;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.simple_cms.R;
import com.example.simple_cms.create.utility.model.ActionIdentifier;
import com.example.simple_cms.create.utility.model.shape.Line;
import com.example.simple_cms.create.utility.model.shape.Shape;
import com.example.simple_cms.utility.ConstantPrefs;

/**
 * This class is in charge of getting the information of shape action
 */
public class CreateStoryBoardActionShapeActivity extends AppCompatActivity {

    private static final String TAG_DEBUG = "CreateStoryBoardActionShapeActivity";

    private TextView connectionStatus, imageAvailable;

    private boolean isSave = false;
    private int position = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_storyboard_action_shape);

        connectionStatus = findViewById(R.id.connection_status);
        imageAvailable = findViewById(R.id.image_available);

        Button buttTest = findViewById(R.id.butt_test);
        Button buttCancel = findViewById(R.id.butt_cancel);
        Button buttAdd = findViewById(R.id.butt_add);
        Button buttDelete = findViewById(R.id.butt_delete);

        Intent intent = getIntent();
        Shape shape = intent.getParcelableExtra(ActionIdentifier.SHAPES_ACTIVITY.name());
        if (shape != null) {
            position = intent.getIntExtra(ActionIdentifier.POSITION.name(), -1);
            isSave = true;
            buttAdd.setText(getResources().getString(R.string.button_save));
            buttDelete.setVisibility(View.VISIBLE);
        }

        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
        loadConnectionStatus(sharedPreferences);

        buttCancel.setOnClickListener((view) ->
                finish()
        );

        buttTest.setOnClickListener((view) ->
                testConnection()
        );

        buttAdd.setOnClickListener((view) ->
                addShape()
        );

        buttDelete.setOnClickListener((view) ->
                deleteShape()
        );
    }

    private void testConnection() {

    }

    private void addShape() {
        Shape shape = new Line();
        Intent returnInfoIntent = new Intent();
        returnInfoIntent.putExtra(ActionIdentifier.SHAPES_ACTIVITY.name(), shape);
        returnInfoIntent.putExtra(ActionIdentifier.IS_SAVE.name(), isSave);
        returnInfoIntent.putExtra(ActionIdentifier.POSITION.name(), position);
        setResult(Activity.RESULT_OK, returnInfoIntent);
        finish();
    }

    private void deleteShape() {
        Intent returnInfoIntent = new Intent();
        returnInfoIntent.putExtra(ActionIdentifier.POSITION.name(), position);
        returnInfoIntent.putExtra(ActionIdentifier.IS_DELETE.name(), true);
        setResult(Activity.RESULT_OK, returnInfoIntent);
        finish();
    }


    /**
     * Set the conenction status on the view
     */
    private void loadConnectionStatus(SharedPreferences sharedPreferences) {
        boolean isConnected = sharedPreferences.getBoolean(ConstantPrefs.IS_CONNECTED.name(), false);
        if (isConnected) {
            connectionStatus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_status_connection_green));
            imageAvailable.setText(getResources().getString(R.string.image_available_on_screen));
        }
    }

}
