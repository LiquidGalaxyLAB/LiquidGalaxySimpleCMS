package com.example.simple_cms;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.simple_cms.connection.LGConnectionManager;
import com.example.simple_cms.top_bar.TobBarActivity;

/**
 * This activity is in charge of connecting the application with liquid Galaxy
 */
public class MainActivity extends TobBarActivity {

    private final static String TAG_DEBUG = "MainActivity";
    private View topBar;
    private Button buttConnectMenu, buttConnectLiquidGalaxy;
    private EditText URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeButtonClickableBackgroundColor();

        buttConnectLiquidGalaxy = findViewById(R.id.connect_liquid_galaxy);
        URI = findViewById(R.id.uri);

        buttConnectLiquidGalaxy.setOnClickListener((view) -> {
            String hostname ="192.168.0.17";
            int port = 22;
            String hostNPort =  URI.getText().toString();
            if(!hostNPort.equals("")){
                String[] hostport = URI.getText().toString().split(":");
                hostname = hostport[0];
                port = Integer.parseInt(hostport[1]);
                LGConnectionManager.getInstance(hostname, port);
            } else LGConnectionManager.getInstance(hostname, port);
        });
    }

    /**
     * Change the background color and the option clickable to false of the button_connect
     */
    private void changeButtonClickableBackgroundColor() {
        topBar = findViewById(R.id.top_bar);
        buttConnectMenu = topBar.findViewById(R.id.butt_connect_menu);
        changeButtonClickableBackgroundColor(getApplicationContext(), buttConnectMenu);
    }
}
