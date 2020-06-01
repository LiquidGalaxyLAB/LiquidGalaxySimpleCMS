package com.example.simple_cms;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.simple_cms.top_bar.TobBarActivity;

/**
 * This activity is in charge of connecting the application with liquid Galaxy
 */
public class MainActivity extends TobBarActivity {

    //private final static String TAG_DEBUG = "MainActivity";
    private View topBar;
    private Button buttConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeButtonClickableBackgroundColor();
    }

    /**
     * Change the background color and the option clickable to false of the button_connect
     */
    private void changeButtonClickableBackgroundColor() {
        topBar = findViewById(R.id.top_bar);
        buttConnect = topBar.findViewById(R.id.butt_connect_menu);
        changeButtonClickableBackgroundColor(getApplicationContext(), buttConnect);
    }
}
