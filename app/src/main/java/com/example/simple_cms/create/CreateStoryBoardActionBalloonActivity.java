package com.example.simple_cms.create;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.simple_cms.R;
import com.example.simple_cms.connection.LGConnectionManager;
import com.example.simple_cms.create.utility.connection.LGConnectionTest;
import com.example.simple_cms.create.utility.model.ActionBuildCommandUtility;
import com.example.simple_cms.create.utility.model.ActionController;
import com.example.simple_cms.create.utility.model.ActionIdentifier;
import com.example.simple_cms.create.utility.model.balloon.Balloon;
import com.example.simple_cms.create.utility.model.poi.POI;
import com.example.simple_cms.dialog.CustomDialogUtility;
import com.example.simple_cms.utility.ConstantPrefs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is in charge of getting the information of placemark action
 */
public class CreateStoryBoardActionBalloonActivity extends AppCompatActivity {

    private static final String TAG_DEBUG = "CreateStoryBoardActionPlaceMarkActivity";

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE_IMAGE = 1001;
    private static final int VIDEO_PICK_CODE = 1002;
    private static final int PERMISSION_CODE_VIDEO = 1003;
    private static final int PERMISSION_CODE_SEND_IMAGE = 1004;

    private TextView connectionStatus, imageAvailable,
            locationName, locationNameTitle;

    private EditText description;
    private ImageView imageView;
    private VideoView videoView;

    private Handler handler = new Handler();
    private POI poi;
    private boolean isSave = false;
    private int position = -1;
    private Uri imageUri;
    private Uri videoUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_storyboard_action_balloon);

        connectionStatus = findViewById(R.id.connection_status);
        imageAvailable = findViewById(R.id.image_available);
        locationName = findViewById(R.id.location_name);
        locationNameTitle = findViewById(R.id.location_name_title);
        description = findViewById(R.id.description);
        imageView = findViewById(R.id.image_view);
        videoView = findViewById(R.id.video_view);

        Button buttTest = findViewById(R.id.butt_test);
        Button buttCancel = findViewById(R.id.butt_cancel);
        Button buttAdd = findViewById(R.id.butt_add);
        Button buttDelete = findViewById(R.id.butt_delete);
        Button buttonAddImage = findViewById(R.id.butt_add_image);
        Button buttonAddVideo = findViewById(R.id.butt_add_video);


        Intent intent = getIntent();
        poi = intent.getParcelableExtra(ActionIdentifier.LOCATION_ACTIVITY.name());
        if (poi != null) {
            setTextView();
        }

        Balloon balloon = intent.getParcelableExtra(ActionIdentifier.PLACE_MARK_ACTIVITY.name());
        if (balloon != null) {
            position = intent.getIntExtra(ActionIdentifier.POSITION.name(), -1);
            isSave = true;
            buttAdd.setText(getResources().getString(R.string.button_save));
            buttDelete.setVisibility(View.VISIBLE);
            poi = balloon.getPoi();
            setTextView();
            description.setText(balloon.getDescription());
            imageUri = balloon.getImageUri();
            imageView.setImageURI(imageUri);
            videoUri = balloon.getVideoUri();
            if(videoUri != null){
                setVideoView();
            }
        }


        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
        loadConnectionStatus(sharedPreferences);

        buttonAddImage.setOnClickListener((view) -> {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE_IMAGE);
            } else {
                pickImageFromGallery();
            }
        });

        buttonAddVideo.setOnClickListener((view) -> {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE_VIDEO);
            } else {
                pickVideoFromGallery();
            }
        });

        buttCancel.setOnClickListener((view) ->
                finish()
        );

        buttTest.setOnClickListener((view) ->
            testConnection()
        );

        buttAdd.setOnClickListener((view) ->
                addPlaceMark()
        );

        buttDelete.setOnClickListener((view) ->
            deletePlaceMark()
        );
    }

    private void testConnection() {
        AtomicBoolean isConnected = new AtomicBoolean(false);
        LGConnectionTest.testPriorConnection(this, isConnected);
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
        handler.postDelayed(() -> {
            if(isConnected.get()){
                Balloon balloon = new Balloon();
                Log.w(TAG_DEBUG, "image uri: " + imageUri);
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE_SEND_IMAGE);


                balloon.setPoi(poi).setDescription(description.getText().toString())
                        .setImageUri(imageUri).setVideoUri(videoUri);
                ActionController.getInstance().sendCreateImageFolder(null);
/*
                ActionController.getInstance().sendBalloonImage(sharedPreferences, path, null);
*/



                /*ActionController.getInstance().sendBalloon(balloon, null);
                ActionController.getInstance().sendNetworkLink(null);*/
/*
                ActionController.getInstance().sendChangeBallon(balloon, null);
*/
                /*ActionController.getInstance().sendNetworkLinkUpdate(null);*/

/*                String username = sharedPreferences.getString(ConstantPrefs.USER_NAME.name(), "lg");
                String hostPort = sharedPreferences.getString(ConstantPrefs.URI_TEXT.name(), "192.168.0.17");
                String[] hostNPort = hostPort.split(":");
                String hostname = hostNPort[0];
                String command = "echo ' scp " + imageUri + " " + username + "@" + hostname + ":/var/www/html/img/" + "' > /";
                LGCommand lgCommand = new LGCommand(command, LGCommand.CRITICAL_MESSAGE, response -> {});
                LGConnectionManager.getInstance().addCommandToLG(lgCommand);*/

                /*String command = "echo 'google-earth-pro  /var/www/html/kmls.kml' > /";
                LGCommand lgCommand = new LGCommand(command, LGCommand.CRITICAL_MESSAGE, response -> {});
                LGConnectionManager.getInstance().addCommandToLG(lgCommand);*/
            }else{
                connectionStatus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_status_connection_red));
            }
            loadConnectionStatus(sharedPreferences);
        }, 1200);
    }

    private void sendImageLG() {
        Cursor cursor = getContentResolver().query(imageUri, null, null, null, null);
        Objects.requireNonNull(cursor).moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        String imagePath = cursor.getString(idx);
        cursor.close();
        Log.w(TAG_DEBUG, "Path: " + imagePath);
        LGConnectionManager.getInstance().sendImage(imagePath);
    }


    private void addPlaceMark() {
        Balloon balloon = new Balloon().setPoi(poi).setDescription(description.getText().toString()).setImageUri(imageUri).setVideoUri(videoUri);
        Intent returnInfoIntent = new Intent();
        returnInfoIntent.putExtra(ActionIdentifier.PLACE_MARK_ACTIVITY.name(), balloon);
        returnInfoIntent.putExtra(ActionIdentifier.IS_SAVE.name(), isSave);
        returnInfoIntent.putExtra(ActionIdentifier.POSITION.name(), position);
        setResult(Activity.RESULT_OK, returnInfoIntent);
        finish();
    }

    private void deletePlaceMark() {
        Intent returnInfoIntent = new Intent();
        returnInfoIntent.putExtra(ActionIdentifier.POSITION.name(), position);
        returnInfoIntent.putExtra(ActionIdentifier.IS_DELETE.name(), true);
        setResult(Activity.RESULT_OK, returnInfoIntent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE_IMAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    CustomDialogUtility.showDialog(this, getResources().getString(R.string.alert_permision_denied_image));
                }
            }
            case PERMISSION_CODE_VIDEO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    CustomDialogUtility.showDialog(this, getResources().getString(R.string.alert_permision_denied_image));
                }
            }
            case PERMISSION_CODE_SEND_IMAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendImageLG();
                } else {
                    CustomDialogUtility.showDialog(this, getResources().getString(R.string.alert_permision_denied_test_image));
                }
            }
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    private void pickVideoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, VIDEO_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageUri = Objects.requireNonNull(data).getData();
            imageView.setImageURI(imageUri);
        } else if (resultCode == RESULT_OK && requestCode == VIDEO_PICK_CODE) {
            videoUri = Objects.requireNonNull(data).getData();
            setVideoView();
        } else {
            Log.w(TAG_DEBUG, "ERROR there is no other request code type");
        }
    }

    private void setVideoView() {
        videoView.setVisibility(View.VISIBLE);
        videoView.setVideoURI(videoUri);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        mediaController.setAnchorView(videoView);
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
        if (isConnected) {
            connectionStatus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_status_connection_green));
            imageAvailable.setText(getResources().getString(R.string.image_available_on_screen));
        }
    }
}
