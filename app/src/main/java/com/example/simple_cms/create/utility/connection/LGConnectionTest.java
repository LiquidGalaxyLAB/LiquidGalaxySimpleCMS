package com.example.simple_cms.create.utility.connection;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.simple_cms.R;
import com.example.simple_cms.connection.LGCommand;
import com.example.simple_cms.connection.LGConnectionManager;
import com.example.simple_cms.dialog.CustomDialogUtility;
import com.example.simple_cms.utility.ConstantPrefs;

import java.util.concurrent.atomic.AtomicBoolean;

import static android.content.Context.MODE_PRIVATE;

/**
 * This class is in charge of testing and cleaning the liquid galaxy connection
 */
public class LGConnectionTest {

    private static Handler handler = new Handler();

    /**
     * Test the connection with the previous data connection
     * @param activity the activity calling the test
     * @param atomicBoolean the boolean to know if the connection is succesful or not
     */
    public static void testPriorConnection(AppCompatActivity activity, AtomicBoolean atomicBoolean) {
        Dialog dialog = CustomDialogUtility.getDialog(activity, activity.getResources().getString(R.string.test_connection));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        LGCommand lgCommand = new LGCommand("echo 'connection';", LGCommand.CRITICAL_MESSAGE, response -> dialog.dismiss());
        sendCommand(lgCommand);
        handler.postDelayed(() -> {
            SharedPreferences.Editor editor = activity.getSharedPreferences(ConstantPrefs.SHARED_PREFS.name(), MODE_PRIVATE).edit();
            if (dialog.isShowing()) {
                LGConnectionManager.getInstance().removeCommandFromLG(lgCommand);
                dialog.dismiss();
                editor.putBoolean(ConstantPrefs.IS_CONNECTED.name(), false);
                CustomDialogUtility.showDialog(activity, activity.getResources().getString(R.string.activity_connection_error));
                atomicBoolean.set(false);
            }else{
                editor.putBoolean(ConstantPrefs.IS_CONNECTED.name(), true);
                atomicBoolean.set(true);
            }
            editor.apply();
        }, 1000);
    }

   /* *//**
     * Clean the query.text file
     *//*
    public static void cleanQuery() {
        handler.postDelayed(() -> {
            String cleanTempTxt = "chmod 777 /tmp/query.txt; echo '' > /tmp/query.txt";
            LGCommand lgCommand = new LGCommand(cleanTempTxt, LGCommand.CRITICAL_MESSAGE, null);
            sendCommand(lgCommand);
            } , 5000);
    }*/

    /**
     * Clean the kml of the balloon
     */
    public static void cleanKMLBalloon() {
        handler.postDelayed(() -> {
            String cleanKMLCommand = "chmod 777 /var/www/html/kmls.txt; echo '' > /var/www/html/balloon.kml";
            LGCommand lgCommand = new LGCommand(cleanKMLCommand, LGCommand.CRITICAL_MESSAGE, null);
            sendCommand(lgCommand);
            } , 5000);
    }

    /**
     * Send a command to the liquid galaxy
     * @param lgCommand lg command to be send
     */
    private static void sendCommand(LGCommand lgCommand) {
        LGConnectionManager lgConnectionManager = LGConnectionManager.getInstance();
        lgConnectionManager.startConnection();
        lgConnectionManager.addCommandToLG(lgCommand);
    }
}
