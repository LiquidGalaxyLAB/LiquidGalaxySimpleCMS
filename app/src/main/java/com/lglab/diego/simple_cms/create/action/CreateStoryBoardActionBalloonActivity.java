package com.lglab.diego.simple_cms.create.action;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.lglab.diego.simple_cms.R;
import com.lglab.diego.simple_cms.create.utility.connection.LGConnectionTest;
import com.lglab.diego.simple_cms.create.utility.model.ActionController;
import com.lglab.diego.simple_cms.create.utility.model.ActionIdentifier;
import com.lglab.diego.simple_cms.create.utility.model.balloon.Balloon;
import com.lglab.diego.simple_cms.create.utility.model.poi.POI;
import com.lglab.diego.simple_cms.dialog.CustomDialogUtility;
import com.lglab.diego.simple_cms.utility.ConstantPrefs;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is in charge of getting the information of balloon action
 */
public class CreateStoryBoardActionBalloonActivity extends AppCompatActivity {

    private static final String TAG_DEBUG = "CreateStoryBoardActionBalloonActivity";

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE_IMAGE = 1001;

    private TextView connectionStatus, imageAvailable,
            locationName, locationNameTitle;

    private EditText description, videoURL;
    private ImageView imageView;

    private Handler handler = new Handler();
    private POI poi;
    private boolean isSave = false;
    private int position = -1;
    private Uri imageUri;
    private String imagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_storyboard_action_balloon);

        connectionStatus = findViewById(R.id.connection_status);
        imageAvailable = findViewById(R.id.admin_password);
        locationName = findViewById(R.id.location_name);
        locationNameTitle = findViewById(R.id.location_name_title);
        description = findViewById(R.id.description);
        imageView = findViewById(R.id.image_view);
        videoURL = findViewById(R.id.video_url);

        Button buttTest = findViewById(R.id.butt_test);
        Button buttCancel = findViewById(R.id.butt_cancel);
        Button buttAdd = findViewById(R.id.butt_add);
        Button buttDelete = findViewById(R.id.butt_delete);
        Button buttonAddImage = findViewById(R.id.butt_add_image);

        Intent intent = getIntent();
        poi = intent.getParcelableExtra(ActionIdentifier.LOCATION_ACTIVITY.name());
        if (poi != null) {
            setTextView();
        }

        Balloon balloon = intent.getParcelableExtra(ActionIdentifier.BALLOON_ACTIVITY.name());
        if (balloon != null) {
            position = intent.getIntExtra(ActionIdentifier.POSITION.name(), -1);
            isSave = true;
            buttAdd.setText(getResources().getString(R.string.button_save));
            buttDelete.setVisibility(View.VISIBLE);
            poi = balloon.getPoi();
            setTextView();
            description.setText(balloon.getDescription());
            imageUri = balloon.getImageUri();
            if(imageUri != null) imageView.setImageURI(imageUri);
            imagePath = balloon.getImagePath();
            videoURL.setText(balloon.getVideoPath());
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


        buttCancel.setOnClickListener((view) ->
                finish()
        );

        buttTest.setOnClickListener((view) ->
            testConnection()
        );

        buttAdd.setOnClickListener((view) ->
                addBalloon()
        );

        buttDelete.setOnClickListener((view) ->
            deleteBalloon()
        );
    }

    /**
     * Test the balloon action and the connection to the Liquid Galaxy
     */
    private void testConnection() {
        AtomicBoolean isConnected = new AtomicBoolean(false);
        LGConnectionTest.testPriorConnection(this, isConnected);
        SharedPreferences sharedPreferences = getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE);
        handler.postDelayed(() -> {
            if(isConnected.get()){
                Balloon balloon = new Balloon();
                if(imageUri != null){
                    imagePath = getFilePath(imageUri);
                }
                balloon.setPoi(poi).setDescription(description.getText().toString())
                        .setImageUri(imageUri).setImagePath(imagePath).setVideoPath(videoURL.getText().toString());
                ActionController.getInstance().sendBalloon(balloon, null, poi.getPoiCamera().getDuration() * 1000);
            }else{
                connectionStatus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_status_connection_red));
            }
            loadConnectionStatus(sharedPreferences);
        }, 1200);
    }

    /**
     * It return the absolute path of the file
     * @param uri uri of the file
     * @return the absolute path of the file
     */
    private String getFilePath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        Objects.requireNonNull(cursor).moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        String imagePath = cursor.getString(idx);
        cursor.close();
        return imagePath;
    }


    /**
     * Send the action to add a balloon
     */
    private void addBalloon() {
        Balloon balloon = new Balloon().setPoi(poi).setDescription(description.getText().toString())
                .setImageUri(imageUri).setImagePath(imagePath).setVideoPath(videoURL.getText().toString());
        Intent returnInfoIntent = new Intent();
        returnInfoIntent.putExtra(ActionIdentifier.BALLOON_ACTIVITY.name(), balloon);
        returnInfoIntent.putExtra(ActionIdentifier.IS_SAVE.name(), isSave);
        returnInfoIntent.putExtra(ActionIdentifier.POSITION.name(), position);
        setResult(Activity.RESULT_OK, returnInfoIntent);
        finish();
    }

    /**
     * Send the action to delete the balloon of the recyclerview in the CreateStoryBoardActivity
     */
    private void deleteBalloon() {
        Intent returnInfoIntent = new Intent();
        returnInfoIntent.putExtra(ActionIdentifier.POSITION.name(), position);
        returnInfoIntent.putExtra(ActionIdentifier.IS_DELETE.name(), true);
        setResult(Activity.RESULT_OK, returnInfoIntent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE_IMAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery();
            } else {
                CustomDialogUtility.showDialog(this, getResources().getString(R.string.alert_permission_denied_image));
            }
        }
    }

    /**
     * Start the activity to pick a image of the gallery
     */
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageUri = Objects.requireNonNull(data).getData();
            imageView.setImageURI(imageUri);
            imagePath = getFilePath(imageUri);
            Log.w(TAG_DEBUG, "imagePath: " + imagePath);
        }  else {
            Log.w(TAG_DEBUG, "ERROR there is no other request code type");
        }
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
