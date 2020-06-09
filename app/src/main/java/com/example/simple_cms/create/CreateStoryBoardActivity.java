package com.example.simple_cms.create;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.simple_cms.R;
import com.example.simple_cms.top_bar.TobBarActivity;

/**
 * This activity is in charge of creating the storyboards with the respective different actions
 */
public class CreateStoryBoardActivity extends TobBarActivity {

    private Button buttCreate, buttLocation, buttMovements, buttGraphics, buttShapes, buttDescription, buttCancel, buttSave;

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

        buttLocation.setOnClickListener( (view) -> {
            Intent intent = new Intent(getApplicationContext(), CreateStoryBoardActionLocationActivity.class);
            startActivity(intent);
        });


        changeButtonClickableBackgroundColor();
    }

    /**
     * Change the background color and the option clickable to false of the button_connect
     */
    private void changeButtonClickableBackgroundColor() {
        changeButtonClickableBackgroundColor(getApplicationContext(), buttCreate);
    }

}
