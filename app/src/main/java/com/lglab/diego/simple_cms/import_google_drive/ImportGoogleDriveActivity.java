package com.lglab.diego.simple_cms.import_google_drive;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.lglab.diego.simple_cms.R;
import com.lglab.diego.simple_cms.create.utility.model.StoryBoard;
import com.lglab.diego.simple_cms.dialog.CustomDialogUtility;
import com.lglab.diego.simple_cms.top_bar.TobBarActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.util.ArrayList;
import java.util.Collections;

public class ImportGoogleDriveActivity extends TobBarActivity {

    private static final String TAG_DEBUG = "ImportGoogleDrive";

    Button buttGoogleDrive;

    private StoryBoard uploadingStoryBoard;
    private String jsonToUpload;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_drive);

        buttGoogleDrive = findViewById(R.id.butt_connect_liquid_galaxy);

        buttGoogleDrive.setOnClickListener( (view) -> {
            requestSignIn();
        });
    }

    private void requestSignIn() {
        if (isSignedIn()) {
            onSuccessLogIn();
            return;
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                //The scope should be changed in order to see other files... https://developers.google.com/drive/api/v3/about-auth https://www.googleapis.com/auth/drive
                .build();

        GoogleDriveManager.GoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        if (account != null)
            GoogleDriveManager.GoogleSignInClient.signOut();

        Intent signInIntent = GoogleDriveManager.GoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GoogleDriveManager.RC_SIGN_IN);
    }

    public boolean isSignedIn() {
        return GoogleDriveManager.GoogleSignInClient != null && GoogleDriveManager.DriveServiceHelper != null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        if (requestCode == GoogleDriveManager.RC_SIGN_IN) {
            if (resultCode == RESULT_OK && resultData != null) {
                handleSignInResult(resultData);
                return;
            }
            Log.w(TAG_DEBUG, "Sign-in failed with resultCode = " + resultCode);
            onFailedLogIn();
        }
    }

    private void handleSignInResult(Intent result) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener(googleAccount -> {
                    Log.w(TAG_DEBUG, "Signed in as " + googleAccount.getEmail());
                    CustomDialogUtility.showDialog(this,"Signed in as " + googleAccount.getEmail());

                    // Use the authenticated account to sign in to the Drive service.
                    GoogleAccountCredential credential =
                            GoogleAccountCredential.usingOAuth2(
                                    getApplicationContext(), Collections.singleton(DriveScopes.DRIVE_FILE)); //DRIVE
                    credential.setSelectedAccount(googleAccount.getAccount());
                    Drive googleDriveService = new Drive.Builder(new NetHttpTransport(), new GsonFactory(), credential)
                            .setApplicationName("LiquidGalaxyForEducation")
                            .build();

                    GoogleDriveManager.DriveServiceHelper = new DriveServiceHelper(googleDriveService);
                    /*GoogleDriveManager.DriveServiceHelper.searchForAppFolderID(() -> {

                    });*/
                    onSuccessLogIn();
                })
                .addOnFailureListener(exception ->  {
                    Log.w(TAG_DEBUG, "Unable to sign in.", exception);
                    onFailedLogIn();
                });
    }

    public void onFailedLogIn() {
        CustomDialogUtility.showDialog(this, getResources().getString(R.string.message_google_drive_failed_log_in));

        this.jsonToUpload = null;
        this.uploadingStoryBoard = null;
    }

    public void onSuccessLogIn() {
        if(GoogleDriveManager.DriveServiceHelper.files == null) {
            setLoadingDialog(getResources().getString(R.string.message_google_drive_success_log_in));
            GoogleDriveManager.DriveServiceHelper.searchForAppFolderID(() -> {
                for (String filename : GoogleDriveManager.DriveServiceHelper.files.values()) {
                    Log.w(TAG_DEBUG, filename);
                }

            }, null);
        } else {
            setLoadingDialog(getResources().getString(R.string.message_google_drive_success_log_in));
        }
    }

    /**
     * Set the dialog
     * @param message The message to be show
     */
    private void setLoadingDialog(String message) {
        @SuppressLint("InflateParams") View v = this.getLayoutInflater().inflate(R.layout.dialog_fragment, null);
        v.getBackground().setAlpha(220);
        Button ok = v.findViewById(R.id.ok);
        TextView textMessage = v.findViewById(R.id.message);
        textMessage.setText(message);
        textMessage.setTextSize(23);
        textMessage.setGravity(View.TEXT_ALIGNMENT_CENTER);
        Button cancel = v.findViewById(R.id.cancel);
        cancel.setVisibility(View.VISIBLE);

        createAlertDialog(v, ok, cancel);
    }

    /**
     * Create a alert dialog for the user
     * @param v view
     * @param ok button ok
     * @param cancel button cancel
     */
    private void createAlertDialog(View v, Button ok, Button cancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        ok.setOnClickListener(v1 -> {
            dialog.dismiss();
        });
        cancel.setOnClickListener(v1 -> dialog.dismiss());
    }

}
