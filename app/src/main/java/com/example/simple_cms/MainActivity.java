package com.example.simple_cms;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simple_cms.connection.LGCommand;
import com.example.simple_cms.connection.LGConnectionManager;
import com.example.simple_cms.dialog.CustomDialog;
import com.example.simple_cms.top_bar.TobBarActivity;
import com.example.simple_cms.utility.ConstantPrefs;

import java.util.regex.Pattern;

/**
 * This activity is in charge of connecting the application with liquid Galaxy
 */
public class MainActivity extends TobBarActivity {

    private static final String TAG_DEBUG = "MainActivity";
    private static final Pattern HOST_PORT = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]):[0-9]+$");

    private Button buttConnectMenu, buttConnectLiquidGalaxy;
    private TextView connecting;
    private EditText URI;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        View topBar = findViewById(R.id.top_bar);
        buttConnectMenu = topBar.findViewById(R.id.butt_connect_menu);
        connecting = findViewById(R.id.connecting);

        changeButtonClickableBackgroundColor();

        buttConnectLiquidGalaxy = findViewById(R.id.connect_liquid_galaxy);
        URI = findViewById(R.id.uri);

        loadSharedData();

        buttConnectLiquidGalaxy.setOnClickListener((view) -> connectionTest());
    }

    @Override
    protected void onResume() {
        Log.w(TAG_DEBUG, "RESUME");
        loadSharedData();
        super.onResume();
}

    /**
     * Load the data that is in shared preferences
     */
    private void loadSharedData() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);

        String text = sharedPreferences.getString(ConstantPrefs.URI_TEXT.name(), "");
        boolean isTryToReconnect = sharedPreferences.getBoolean(ConstantPrefs.TRY_TO_RECONNECT.name(), false);

        if(!text.equals("")) URI.setText(text);
        if(isTryToReconnect) buttConnectLiquidGalaxy.setText(getResources().getString(R.string.button_try_again));
    }

    /**
     * Create a connection to the liquid galaxy and Test if it is working
     */
    private void connectionTest() {
        String hostPort = URI.getText().toString();

        SharedPreferences.Editor editor = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE).edit();
        editor.putString(ConstantPrefs.URI_TEXT.name(), hostPort);

        if (!isValidHostNPort(hostPort)) {
            String message = getResources().getString(R.string.activity_connection_host_port_error);
            CustomDialog.showDialog(MainActivity.this, message);
            editor.apply();
            return;
        }
        //earth
        //mars
        String command = "echo 'planet=mars' > /tmp/query.txt";
        LGCommand lgCommand = new LGCommand(command, LGCommand.CRITICAL_MESSAGE, (String response) -> {
            if(response == null) {
                Toast.makeText(getApplicationContext(), "FAIL PRUEBA", Toast.LENGTH_LONG).show();
            }
            Toast.makeText(getApplicationContext(), "YEI PRUEBA", Toast.LENGTH_LONG).show();
        });

        createConnection(hostPort, lgCommand);
        editor.putBoolean(ConstantPrefs.TRY_TO_RECONNECT.name(), true);
        editor.apply();

        changeButtonText();
        sendMessageError(lgCommand);
    }

    /**
     * Change the Button for a TextView with the text "Connecting ..."
     */
    private void changeButtonText() {
        buttConnectLiquidGalaxy.setVisibility(View.INVISIBLE);
        connecting.setVisibility(View.VISIBLE);
        connecting.setText(getResources().getString(R.string.connecting));
    }


    /**
     * Create a Dialog to inform the user that the connection to the liquid galaxy has fail
     */
    private void sendMessageError(LGCommand lgCommand) {
        handler.postDelayed(() -> {
            connecting.setVisibility(View.INVISIBLE);
            buttConnectLiquidGalaxy.setVisibility(View.VISIBLE);
            buttConnectLiquidGalaxy.setText(getResources().getString(R.string.button_try_again));
            LGConnectionManager.getInstance().removeCommandFromLG(lgCommand);
            String message = getResources().getString(R.string.activity_connection_error);
            CustomDialog.showDialog(MainActivity.this, message);
        }, 2000);
    }

    /**
     * Create the connection between the application and the liquid galaxy
     *
     * @param hostPort The string with the host and the port
     */
    private void createConnection(String hostPort, LGCommand lgCommand) {
        String[] hostNPort = hostPort.split(":");
        String hostname = hostNPort[0];
        int port = Integer.parseInt(hostNPort[1]);
        LGConnectionManager lgConnectionManager = LGConnectionManager.getInstance();
        lgConnectionManager.setData(hostname, port);
        lgConnectionManager.startConnection();
        LGConnectionManager.getInstance().addCommandToLG(lgCommand);
    }

    /**
     * Validate if the string is valid
     *
     * @param hostPort The string with the host and the port
     * @return true if is a valid string with the host and the port
     */
    private boolean isValidHostNPort(String hostPort) {
        return HOST_PORT.matcher(hostPort).matches();
    }

    /**
     * Change the background color and the option clickable to false of the button_connect
     */
    private void changeButtonClickableBackgroundColor() {
        changeButtonClickableBackgroundColor(getApplicationContext(), buttConnectMenu);
    }

    @Override
    protected void onDestroy() {
        Log.w(TAG_DEBUG, "DESTROY");
        SharedPreferences.Editor editor = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE).edit();
        editor.putString(ConstantPrefs.URI_TEXT.name(), "");
        editor.putBoolean(ConstantPrefs.TRY_TO_RECONNECT.name(), false);
        editor.apply();
        super.onDestroy();
    }
}
