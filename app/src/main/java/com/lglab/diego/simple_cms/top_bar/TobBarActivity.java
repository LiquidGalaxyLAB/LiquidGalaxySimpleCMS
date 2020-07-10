package com.lglab.diego.simple_cms.top_bar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.lglab.diego.simple_cms.MainActivity;
import com.lglab.diego.simple_cms.R;
import com.lglab.diego.simple_cms.account.LogIn;
import com.lglab.diego.simple_cms.create.CreateStoryBoardActivity;
import com.lglab.diego.simple_cms.import_google_drive.ImportGoogleDriveActivity;
import com.lglab.diego.simple_cms.my_storyboards.MyStoryBoardActivity;


/**
 * This Activity is in charge of the flow between the activities. It is present in all the activities in the top.
 */
public class TobBarActivity extends AppCompatActivity {

    //private static final String TAG_DEBUG = "TobBarActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Pass form the actual activity to the activity Create
     * @param view The view which is call.
     */
    public void buttCreateMenu(View view) {
        Intent intent = new Intent(getApplicationContext(), CreateStoryBoardActivity.class);
        startActivity(intent);
    }

    /**
     * Pass form the actual activity to the activity My Sotry Board
     * @param view The view which is call.
     */
    public void buttMyStoryboardsMenu(View view) {
        Intent intent = new Intent(getApplicationContext(), MyStoryBoardActivity.class);
        startActivity(intent);
    }

    /**
     * Pass form the actual activity to the activity Import Google Drive
     * @param view The view which is call.
     */
    public void buttImportGoogleDrive(View view) {
        Intent intent = new Intent(getApplicationContext(), ImportGoogleDriveActivity.class);
        startActivity(intent);
    }

    /**
     * Pass form the actual activity to the activity Main Activity (connect)
     * @param view The view which is call.
     */
    public void buttConnectMenu(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    /**
     * Pass form the actual activity to the activity LogIn
     * @param view The view which is call.
     */
    public void buttAccount(View view) {
        Intent intent = new Intent(getApplicationContext(), LogIn.class);
        startActivity(intent);
    }


    /**
     * Change the background color and the option clickable to false of the button_connect
     *
     * @param context The context which is the button
     * @param button The button that need to be modify
     */
    public void changeButtonClickableBackgroundColor(Context context, Button button){
        button.setBackgroundColor(ContextCompat.getColor(context, R.color.background));
        button.setTextColor(ContextCompat.getColor(context, R.color.textColorClick));
        button.setClickable(false);
    }
}
