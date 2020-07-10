package com.lglab.diego.simple_cms.import_google_drive;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class GoogleDriveManager {

    public static final int RC_SIGN_IN = 0;
    public static final int RC_OPEN_FILE = 1;

    public static GoogleSignInClient GoogleSignInClient;
    public static DriveServiceHelper DriveServiceHelper;

    private static String OpenFileId;
    private static String OpenFileName;
    private static String OpenFileContent;

    public static void setReadOnlyMode(String fileName, String fileContent) {
        OpenFileId = null;
        OpenFileName = fileName;
        OpenFileContent = fileContent;
    }

    public static void setReadWriteMode(String fileId, String fileName, String fileContent) {
        OpenFileId = fileId;
        OpenFileName = fileName;
        OpenFileContent = fileContent;
    }
}
