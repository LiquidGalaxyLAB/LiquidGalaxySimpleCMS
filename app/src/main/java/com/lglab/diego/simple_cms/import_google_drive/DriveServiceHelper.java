package com.lglab.diego.simple_cms.import_google_drive;

import android.util.Log;

import androidx.core.util.Pair;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DriveServiceHelper {

    private static final String TAG_DEBUG = "DriveServiceHelper";

    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    private final Drive mDriveService;
    private String drive_app_folder;
    public Map<String, String> files;

    private static final String FOLDER_MIME_TYPE = "application/vnd.google-apps.folder";
    private static final String JSON_MIME_TYPE = "application/json";

    public DriveServiceHelper(Drive driveService) {
        mDriveService = driveService;
    }

        public Task<String> createFile(String title) {
        return Tasks.call(mExecutor, () -> {


            FileList result = mDriveService.files().list()
                    .setQ("mimeType = '" + FOLDER_MIME_TYPE + "' and name = 'SimpleCMS'")
                    .setSpaces("drive")
                    .execute();
            if (result.getFiles().size() > 0) {
                drive_app_folder = result.getFiles().get(0).getId();
            }else{
                createAppFolderID();
            }


            File metadata = new File()
                    .setParents(Collections.singletonList(drive_app_folder))
                    .setMimeType(JSON_MIME_TYPE)
                    .setName(title);

            File googleFile = mDriveService.files().create(metadata).execute();
            if (googleFile == null) {
                throw new IOException("Null result when requesting file creation.");
            }

           /* Permission userPermission = new Permission()
                    .setType("user")
                    .setRole("reader")
                    .setEmailAddress("liquidgalaxylab@gmail.com");

            Permission permission = mDriveService.permissions().create(googleFile.getId(), userPermission).setFields("id").execute();
            if (permission == null) {
                throw new IOException("Null result when requesting file creation.");
            }*/

            Log.w(TAG_DEBUG, "FILE CREATED ID:" + googleFile.getId());
            return googleFile.getId();
        });
    }

    public Task<Pair<String, String>> readFile(String fileId) {
        return Tasks.call(mExecutor, () -> {
            // Retrieve the metadata as a File object.
            File metadata = mDriveService.files().get(fileId).execute();
            String name = metadata.getName();

            // Stream the file contents to a String.
            try (InputStream is = mDriveService.files().get(fileId).executeMediaAsInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                String contents = stringBuilder.toString();

                return Pair.create(name, contents);
            }
        });
    }

    /**
     * Updates the file identified by {@code fileId} with the given {@code name} and {@code
     * content}.
     */
    public Task<Void> saveFile(String fileId, String name, String content) {
        return Tasks.call(mExecutor, () -> {
            // Create a File containing any metadata changes.
            File metadata = new File().setName(name);

            // Convert content to an AbstractInputStreamContent instance.
            ByteArrayContent contentStream = ByteArrayContent.fromString("application/json", content);

            // Update the metadata and contents.
            mDriveService.files().update(fileId, metadata, contentStream).execute();
            return null;
        });
    }

    private Task<String> createAppFolderID() {
        return Tasks.call(mExecutor, () -> {

            List<String> root = Collections.singletonList("root");

            File metadata = new File()
                    .setParents(root)
                    .setMimeType(FOLDER_MIME_TYPE)
                    .setName("SimpleCMS");

            File googleFile = mDriveService.files().create(metadata).execute();
            if (googleFile == null) {
                throw new IOException("Null result when requesting file creation.");
            }

            drive_app_folder = googleFile.getId();
            return googleFile.getId();
        });
    }


    public void searchForAppFolderID(Runnable onSuccess, Runnable onFailure) {
        Tasks.call(mExecutor, () -> mDriveService.files().list().setQ("mimeType = '" + FOLDER_MIME_TYPE + "' and name = 'SimpleCMS'").setSpaces("drive").execute())
                .addOnSuccessListener(fileList -> {
                    List<File> files = fileList.getFiles();
                    if (files.size() > 0) {
                        drive_app_folder = files.get(0).getId();
                        Log.w(TAG_DEBUG, "App folder was already created: " + drive_app_folder);
                        searchForFilesInsideAppFolderID(onSuccess, onFailure);
                    } else {
                        createAppFolderID()
                                .addOnSuccessListener(file -> {
                                    drive_app_folder = file;
                                    Log.w(TAG_DEBUG, "App folder created: " + drive_app_folder);
                                    searchForFilesInsideAppFolderID(onSuccess, onFailure);
                                })
                                .addOnFailureListener(exception -> {
                                    Log.w(TAG_DEBUG, "Unable to search for appfolder", exception);
                                    if(onFailure != null)
                                        onFailure.run();
                                });
                    }
                })
                .addOnFailureListener(exception -> {
                    Log.w(TAG_DEBUG, "Unable to search for appfolder", exception);
                    if(onFailure != null)
                        onFailure.run();
                });
    }

    private void searchForFilesInsideAppFolderID(Runnable onSuccess, Runnable onFailure) {
        queryFiles()
                .addOnSuccessListener(fileList -> {
                    files = new HashMap<>();
                    for(File file : fileList) {
                        files.put(file.getId(), file.getName());
                    }
                    onSuccess.run();
                })
                .addOnFailureListener(exception -> {
                    Log.w(TAG_DEBUG, "Unable to search for for files inside appfolder", exception);
                    if(onFailure != null)
                        onFailure.run();
                });
    }

    private Task<List<File>> queryFiles() {
        return Tasks.call(mExecutor, () -> {
            List<File> fileList = new ArrayList<>();
            String pageToken = null;
            do {
                FileList result = mDriveService.files()
                        .list()
                        .setQ("mimeType = 'application/json' and parents in '" + drive_app_folder + "' ")
                        .setSpaces("drive")
                        .setFields("nextPageToken, files(id, name)")
                        .setPageToken(pageToken)
                        .execute();

                fileList.addAll(result.getFiles());
                pageToken = result.getNextPageToken();
            } while (pageToken != null);
            return fileList;
        });
    }
}
