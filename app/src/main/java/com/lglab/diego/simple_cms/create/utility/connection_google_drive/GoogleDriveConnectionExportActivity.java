package com.lglab.diego.simple_cms.create.utility.connection_google_drive;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.lglab.diego.simple_cms.R;
import com.lglab.diego.simple_cms.dialog.CustomDialogUtility;
import com.lglab.diego.simple_cms.import_google_drive.DriveServiceHelper;
import com.lglab.diego.simple_cms.import_google_drive.GoogleDriveManager;
import com.lglab.diego.simple_cms.top_bar.TobBarActivity;

import java.util.Collections;

public class GoogleDriveConnectionExportActivity extends TobBarActivity {

    private static final String TAG_DEBUG = "GoogleDriveConnectionExportActivity";

    private String jsonToUpload;
    private String jsonNameToUpload;
    private String fileNameId;

    public void requestSignIn(String storyBoardJson, String name, String fileNameId) {
        this.jsonToUpload = storyBoardJson;
        this.jsonNameToUpload = name;
        this.fileNameId = fileNameId;
        if (isSignedIn()) {
            setFileGoogleDrive();
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

    public void disconnect(){
        GoogleDriveManager.GoogleSignInClient = null;
        GoogleDriveManager.DriveServiceHelper = null;
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
                            .setApplicationName("SimpleCMS")
                            .build();

                    GoogleDriveManager.DriveServiceHelper = new DriveServiceHelper(googleDriveService);
                    setFileGoogleDrive();
                })
                .addOnFailureListener(exception ->  {
                    Log.w(TAG_DEBUG, "Unable to sign in.", exception);
                    onFailedLogIn();
                });
    }

    public void onFailedLogIn() {
        CustomDialogUtility.showDialog(this, getResources().getString(R.string.message_google_drive_failed_log_in));
        this.jsonToUpload = null;
        this.jsonNameToUpload = null;
    }

    public void setFileGoogleDrive(){

        if(fileNameId == null) {
            GoogleDriveManager.DriveServiceHelper.createFile(jsonNameToUpload)
                    .addOnSuccessListener((result) -> GoogleDriveManager.DriveServiceHelper.saveFile(result, jsonNameToUpload, jsonToUpload)
                            .addOnFailureListener(exception -> {
                                CustomDialogUtility.showDialog(GoogleDriveConnectionExportActivity.this,
                                        getResources().getString(R.string.message_failed_upload));
                                this.jsonToUpload = null;
                                this.jsonNameToUpload = null;
                            })
                            .addOnSuccessListener(result2 -> {
                                CustomDialogUtility.showDialog(GoogleDriveConnectionExportActivity.this,
                                        getResources().getString(R.string.message_success_upload));
                                this.jsonToUpload = null;
                                this.jsonNameToUpload = null;
                            }))
                    .addOnFailureListener(exception -> {
                        CustomDialogUtility.showDialog(GoogleDriveConnectionExportActivity.this,
                                getResources().getString(R.string.message_failed_upload));
                        this.jsonToUpload = null;
                        this.jsonNameToUpload = null;
                    });
        }else{
            GoogleDriveManager.DriveServiceHelper.saveFile(fileNameId, jsonNameToUpload, jsonToUpload)
                    .addOnSuccessListener((result) -> {
                        CustomDialogUtility.showDialog(GoogleDriveConnectionExportActivity.this,
                                getResources().getString(R.string.message_success_upload));
                        this.jsonToUpload = null;
                        this.jsonNameToUpload = null;
                    })
                    .addOnFailureListener((result) -> {
                        Log.w(TAG_DEBUG, "RESULT ERROR:" + result.getMessage());
                        CustomDialogUtility.showDialog(GoogleDriveConnectionExportActivity.this,
                                getResources().getString(R.string.message_failed_upload));
                        this.jsonToUpload = null;
                        this.jsonNameToUpload = null;
                    });
        }
    }


}
