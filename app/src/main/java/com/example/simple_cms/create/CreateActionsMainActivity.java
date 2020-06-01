package com.example.simple_cms.create;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.simple_cms.R;
import com.example.simple_cms.top_bar.TobBarActivity;

/**
 * This activity is in charge of creating the storyboards with the respective different actions
 */
public class CreateActionsMainActivity extends TobBarActivity {

    private View topBar;
    private Button buttCreate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_actions);


        changeButtonClickableBackgroundColor();
    }

    /**
     * Change the background color and the option clickable to false of the button_connect
     */
    private void changeButtonClickableBackgroundColor() {
        topBar = findViewById(R.id.top_bar);
        buttCreate = topBar.findViewById(R.id.butt_create_menu);
        changeButtonClickableBackgroundColor(getApplicationContext(), buttCreate);
    }

    public void onClickCancel(View view) {
    }

    public void onClickSave(View view) {
    }
}
