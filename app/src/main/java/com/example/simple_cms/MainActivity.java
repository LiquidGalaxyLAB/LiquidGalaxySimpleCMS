package com.example.simple_cms;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simple_cms.connection.LGCommand;
import com.example.simple_cms.connection.LGConnectionManager;
import com.example.simple_cms.create.utility.connection.LGConnectionTest;
import com.example.simple_cms.dialog.CustomDialogUtility;
import com.example.simple_cms.top_bar.TobBarActivity;
import com.example.simple_cms.utility.ConstantPrefs;

import java.util.regex.Pattern;

/**
 * This activity is in charge of connecting the application with liquid Galaxy
 */
public class MainActivity extends TobBarActivity {

    //private static final String TAG_DEBUG = "MainActivity";
    private static final Pattern HOST_PORT = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]):[0-9]+$");

    private Button buttConnectMenu, buttConnectLiquidGalaxy, buttTryAgain;
    private TextView connecting, textUsername, textPassword, textInsertUrl;
    private EditText URI, username, password;
    private ImageView logo;
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
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        textUsername = findViewById(R.id.text_username);
        textPassword = findViewById(R.id.text_password);
        textInsertUrl = findViewById(R.id.text_insert_url);
        logo = findViewById(R.id.logo);
        buttTryAgain = findViewById(R.id.butt_try_again);

        buttConnectLiquidGalaxy.setOnClickListener((view) -> connectionTest());
        buttTryAgain.setOnClickListener((view) -> {
            changeToMainView();
            SharedPreferences.Editor editor = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE).edit();
            editor.putBoolean(ConstantPrefs.IS_CONNECTED.name(), false);
            editor.apply();
        });
    }


    @Override
    protected void onResume() {
        loadSharedData();
        super.onResume();
}

    /**
     * Load the data that is in shared preferences
     */
    private void loadSharedData() {
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
        boolean isConnected = sharedPreferences.getBoolean(ConstantPrefs.IS_CONNECTED.name(), false);

        if(isConnected){
            changeToNewView();
        } else {
            String text = sharedPreferences.getString(ConstantPrefs.URI_TEXT.name(), "");
            String usernameText = sharedPreferences.getString(ConstantPrefs.USER_NAME.name(), "");
            String passwordText = sharedPreferences.getString(ConstantPrefs.USER_PASSWORD.name(), "");
            boolean isTryToReconnect = sharedPreferences.getBoolean(ConstantPrefs.TRY_TO_RECONNECT.name(), false);

            if(!text.equals("")) URI.setText(text);
            if(!usernameText.equals("")) username.setText(usernameText);
            if(!passwordText.equals("")) password.setText(passwordText);
            if(isTryToReconnect) buttConnectLiquidGalaxy.setText(getResources().getString(R.string.button_try_again));
        }
    }

    /**
     * Create a connection to the liquid galaxy and Test if it is working
     */
    private void connectionTest() {
        String hostPort = URI.getText().toString();
        String usernameText = username.getText().toString();
        String passwordText = password.getText().toString();

        SharedPreferences.Editor editor = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE).edit();
        editor.putString(ConstantPrefs.URI_TEXT.name(), hostPort);
        editor.putString(ConstantPrefs.USER_NAME.name(), usernameText);
        editor.putString(ConstantPrefs.USER_PASSWORD.name(), passwordText);

        if (!isValidHostNPort(hostPort)) {
            String message = getResources().getString(R.string.activity_connection_host_port_error);
            CustomDialogUtility.showDialog(MainActivity.this, message);
            editor.apply();
            return;
        }

        editor.putBoolean(ConstantPrefs.TRY_TO_RECONNECT.name(), true);
        editor.apply();

        Dialog dialog = CustomDialogUtility.getDialog(this, getResources().getString(R.string.connecting));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        changeButtonTextConnecting();
        createLgCommand(hostPort, usernameText, passwordText, dialog);
    }

    /**
     * Create LgCommand  for the test
     * @param hostPort A string with the host and the port
     * @param usernameText The username
     * @param passwordText The password
     * @param dialog The dialog that is going to be shown to the user
     */
    private void createLgCommand(String hostPort, String usernameText, String passwordText, Dialog dialog) {
       final String command = "echo 'flytoview=" +
                "<gx:duration> 3 </gx:duration>" +
                "<gx:flyToMode>smooth</gx:flyToMode>" +
                "<LookAt>" +
                "<longitude>" + -122.4783 + "</longitude>" +
                "<latitude>" + 37.8120 + "</latitude>" +
                "<altitude>" + 2.0 + "</altitude>" +
                "<heading>" + 10 + "</heading>" +
                "<tilt>" + 90.0 + "</tilt>" +
                "<range>" + 10 + "</range>" +
                "<gx:altitudeMode> clampToGround   </gx:altitudeMode>" +
                "</LookAt>' > /tmp/query.txt";
        final String command3 = "echo '" +
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\"\n" +
                " xmlns:gx=\"http://www.google.com/kml/ext/2.2\">\n" +
                "\n" +
                "<Document>\n" +
                "  <name>gx:AnimatedUpdate example</name>\n" +
                "\n" +
                "  <Style id=\"pushpin\">\n" +
                "    <IconStyle id=\"mystyle\">\n" +
                "      <Icon>\n" +
                "        <href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>\n" +
                "        <scale>1.0</scale>\n" +
                "      </Icon>\n" +
                "    </IconStyle>\n" +
                "  </Style>\n" +
                "\n" +
                "  <Placemark id=\"mountainpin1\">\n" +
                "    <name>Pin on a mountaintop</name>\n" +
                "    <styleUrl>#pushpin</styleUrl>\n" +
                "    <Point>\n" +
                "      <coordinates>170.1435558771009,-43.60505741890396,0</coordinates>\n" +
                "    </Point>\n" +
                "  </Placemark>\n" +
                "\n" +
                "  <gx:Tour>\n" +
                "    <name>12345</name>\n" +
                "\n" +
                "    <gx:Playlist>\n" +
                "\n" +
                "      <gx:FlyTo>\n" +
                "        <gx:flyToMode>bounce</gx:flyToMode>\n" +
                "        <gx:duration>3</gx:duration>\n" +
                "        <Camera>\n" +
                "          <longitude>170.157</longitude>\n" +
                "          <latitude>-43.671</latitude>\n" +
                "          <altitude>9700</altitude>\n" +
                "          <heading>-6.333</heading>\n" +
                "          <tilt>33.5</tilt>\n" +
                "        </Camera>\n" +
                "      </gx:FlyTo>\n" +
                "\n" +
                "      <gx:AnimatedUpdate>\n" +
                "        <gx:duration>5</gx:duration>\n" +
                "        <Update>\n" +
                "          <targetHref></targetHref>\n" +
                "          <Change>\n" +
                "            <IconStyle targetId=\"mystyle\">\n" +
                "              <scale>10.0</scale>\n" +
                "            </IconStyle>\n" +
                "          </Change>\n" +
                "        </Update>\n" +
                "      </gx:AnimatedUpdate>\n" +
                "\n" +
                "      <gx:Wait>\n" +
                "        <gx:duration>5</gx:duration>\n" +
                "      </gx:Wait>\n" +
                "\n" +
                "    </gx:Playlist>\n" +
                "  </gx:Tour>\n" +
                "\n" +
                "</Document>\n" +
                "</kml>' > /var/www/html/kmls.txt";
        final String command4 = "echo ' " +
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns2:kml xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:ns2=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:xal=\"urn:oasis:names:tc:ciq:xsdschema:xAL:2.0\">\n" +
                "    <ns2:Document>\n" +
                "        <ns2:name>My KML File</ns2:name>\n" +
                "        <ns2:open>1</ns2:open>\n" +
                "        <ns2:Style id=\"balloonstyle\">\n" +
                "            <ns2:BalloonStyle id=\"ID\">\n" +
                "                <ns2:text>&lt;font face='Courier' size='3'&gt;$[description]&lt;/font&gt;</ns2:text>\n" +
                "            </ns2:BalloonStyle>\n" +
                "        </ns2:Style>\n" +
                "        <ns2:Folder>\n" +
                "            <ns2:name>Placemark -- Points</ns2:name>\n" +
                "            <ns2:open>1</ns2:open>\n" +
                "            <ns2:Placemark>\n" +
                "                <ns2:name>My Placemark</ns2:name>\n" +
                "                <ns2:visibility>1</ns2:visibility>\n" +
                "                <ns2:open>1</ns2:open>\n" +
                "                <ns2:description>&lt;button type=&quot;button&quot; onclick=&quot;callAPI()&quot;/&gt;Call API&lt;/button&gt;&lt;script type=&quot;text/javascript&quot;&gt; function callAPI() { var xhttp = new XMLHttpRequest(); xhttp.open('GET', 'https://jsonplaceholder.typicode.com/todos/1', true); xhttp.send(); }&lt;/script&gt;</ns2:description>\n" +
                "                <ns2:styleUrl>#balloonstyle</ns2:styleUrl>\n" +
                "                <ns2:Point>\n" +
                "                    <ns2:extrude>0</ns2:extrude>\n" +
                "                    <ns2:altitudeMode>clampToGround</ns2:altitudeMode>\n" +
                "                    <ns2:coordinates>0.0,0.0</ns2:coordinates>\n" +
                "                </ns2:Point>\n" +
                "            </ns2:Placemark>\n" +
                "        </ns2:Folder>\n" +
                "    </ns2:Document>\n" +
                "</ns2:kml>' > /var/www/html/kmls.kml";
        LGCommand lgCommand = new LGCommand(command, LGCommand.CRITICAL_MESSAGE, response -> dialog.dismiss());
        createConnection(usernameText, passwordText, hostPort, lgCommand);
        sendMessageError(lgCommand, dialog);
    }

    /**
     * Create the connection to the liquid galaxy
     * @param username The username of the connection
     * @param password The password of the connection
     * @param hostPort The String with the hos and the port of the liquid galxy
     * @param lgCommand The command to be send
     */
    private void createConnection(String username, String password, String hostPort, LGCommand lgCommand) {
        String[] hostNPort = hostPort.split(":");
        String hostname = hostNPort[0];
        int port = Integer.parseInt(hostNPort[1]);
        LGConnectionManager lgConnectionManager = LGConnectionManager.getInstance();
        lgConnectionManager.setData(username, password, hostname, port);
        lgConnectionManager.startConnection();
        lgConnectionManager.addCommandToLG(lgCommand);
    }

    /**
     * Change the Button for a TextView with the text "Connecting ..."
     */
    private void changeButtonTextConnecting() {
        buttConnectLiquidGalaxy.setVisibility(View.INVISIBLE);
        connecting.setVisibility(View.VISIBLE);
        connecting.setText(getResources().getString(R.string.connecting));
    }


    /**
     * Create a Dialog to inform the user if the connection to the liquid galaxy has fail or not
     * @param lgCommand The command send to liquid galaxy
     * @param dialog The dialog that has been show to the user
     */
    private void sendMessageError(LGCommand lgCommand, Dialog dialog) {
        handler.postDelayed(() -> {
            if (dialog.isShowing()){
                LGConnectionManager.getInstance().removeCommandFromLG(lgCommand);
                dialog.dismiss();
                CustomDialogUtility.showDialog(MainActivity.this, getResources().getString(R.string.activity_connection_error));
                SharedPreferences.Editor editor = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE).edit();
                editor.putBoolean(ConstantPrefs.IS_CONNECTED.name(), false);
                editor.apply();
                connecting.setVisibility(View.INVISIBLE);
                buttConnectLiquidGalaxy.setVisibility(View.VISIBLE);
                buttConnectLiquidGalaxy.setText(getResources().getString(R.string.button_try_again));
            }else{
                CustomDialogUtility.showDialog(MainActivity.this, getResources().getString(R.string.activity_connection_success));
                SharedPreferences.Editor editor = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE).edit();
                editor.putBoolean(ConstantPrefs.IS_CONNECTED.name(), true);
                editor.apply();
                changeToNewView();
            }
        }, 2000);
    }

    /**
     * Change the view to the connected to liquid galaxy view
     */
    private void changeToNewView() {
        buttConnectLiquidGalaxy.setVisibility(View.INVISIBLE);
        connecting.setVisibility(View.INVISIBLE);
        URI.setVisibility(View.INVISIBLE);
        username.setVisibility(View.INVISIBLE);
        password.setVisibility(View.INVISIBLE);
        textUsername.setVisibility(View.INVISIBLE);
        textPassword.setVisibility(View.INVISIBLE);
        textInsertUrl.setVisibility(View.INVISIBLE);
        logo.setVisibility(View.VISIBLE);
        buttTryAgain.setVisibility(View.VISIBLE);
    }

    /**
     * Change the view to the try to connect to liquid galaxy view
     */
    private void changeToMainView() {
        buttConnectLiquidGalaxy.setVisibility(View.VISIBLE);
        connecting.setVisibility(View.VISIBLE);
        URI.setVisibility(View.VISIBLE);
        username.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        textUsername.setVisibility(View.VISIBLE);
        textPassword.setVisibility(View.VISIBLE);
        textInsertUrl.setVisibility(View.VISIBLE);
        logo.setVisibility(View.INVISIBLE);
        buttTryAgain.setVisibility(View.INVISIBLE);
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

}
